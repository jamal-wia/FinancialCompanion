package com.financialcompanion.android.core.presentation.paginator

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * @author Jamal Aliev (aliev.djamal.2000@gmail.com)
 */
class StateMapPaginator<K, V>(
    private val coroutineScope: CoroutineScope,
    private val requestFactory: suspend (page: Int) -> LinkedHashMap<K, V>,
    private val viewController: ViewController<K, V>,
) {

    var emptyProgressState = false
        private set
    var emptyErrorState = false
        private set
    var emptyDataState = false
        private set
    var pageProgressState = false
        private set
    var refreshProgressState = false
        private set

    private val firstPage = 1
    private var currentPage = 0
    var isLastPage = false
        private set

    private var currentState: State<K, V> = EmptyState()
    val data = linkedMapOf<K, V>()
    private var requestJob: Job? = null

    fun restart() = currentState.restart()
    fun refresh() = currentState.refresh()
    fun refreshAllPages() = currentState.refreshPages(firstPage..currentPage)
    fun loadNewPage() = currentState.loadNewPage()
    fun replaceData(newData: Map<K, V>) = currentState.replaceData(newData)
    fun release() = currentState.release()

    private fun loadPage(page: Int) {
        requestJob?.cancel()
        requestJob = coroutineScope.launch {
            try {
                val data = requestFactory.invoke(page)
                isLastPage = data.isEmpty()
                currentState.newData(data)
            } catch (e: Exception) {
                currentState.fail(e)
            }
        }
    }

    private fun loadPagesAsync(pages: IntRange) {
        requestJob?.cancel()
        requestJob = coroutineScope.launch {
            try {
                val requests = mutableListOf<Deferred<Map<K, V>>>()
                for (page in pages) requests.add(async { requestFactory(page) })
                val newData = linkedMapOf<K, V>()
                for (request in requests) newData.putAll(request.await())
                currentState.newData(newData)
            } catch (e: Exception) {
                Timber.e(e)
                currentState.fail(e)
            }
        }
    }

    /**
     * Callback'и, вызываемые в момент перехода от одного состояния к другому.
     */
    interface ViewController<K, V> {
        fun showEmptyProgress(show: Boolean) {}
        fun showEmptyError(show: Boolean, error: Throwable? = null) {}
        fun showEmptyData(show: Boolean) {}
        fun showData(show: Boolean, data: Map<K, V> = emptyMap()) {}
        fun showErrorMessage(error: Throwable) {}
        fun showRefreshProgress(show: Boolean) {}
        fun showPageProgress(show: Boolean) {}
    }

    private interface State<K, V> {
        fun restart() {}
        fun refresh() {}
        fun loadNewPage() {}
        fun newData(newData: Map<K, V>) {}
        fun fail(error: Throwable) {}
        fun release() {}
        fun refreshPages(pageRange: IntRange) {}
        fun replaceData(newData: Map<K, V>) {}
    }

    /**
     * Первоначальное пустое состояние, когда загрузка данных еще не выполнена.
     *
     * Возможные последующие состояния:
     * [EmptyProgressState], [ReleasedState].
     */
    private inner class EmptyState : State<K, V> {
        override fun refresh() {
            Timber.d("refresh")
            currentState = EmptyProgressState()

            emptyProgressState = true
            viewController.showEmptyProgress(emptyProgressState)
            loadPage(firstPage)
        }

        override fun release() {
            currentState = ReleasedState()
            requestJob?.cancel()
        }
    }

    /**
     * Состояние отображения прогресса на пустом экране и последующего отображения данных, если не было ошибок.
     *
     * Возможные последующие состояния:
     * [DataState], [EmptyDataState], [EmptyErrorState], [ReleasedState].
     */
    private inner class EmptyProgressState : State<K, V> {
        override fun restart() {
            Timber.d("restart")
            loadPage(firstPage)
        }

        override fun newData(newData: Map<K, V>) {
            Timber.d("newData: ${newData.size}")
            if (newData.isNotEmpty()) {
                currentState = DataState()

                data.clear()
                data.putAll(newData)
                currentPage = firstPage

                emptyProgressState = false
                viewController.showEmptyProgress(emptyProgressState)
                refreshProgressState = false
                viewController.showRefreshProgress(refreshProgressState)
                viewController.showData(true, data)
            } else {
                currentState = EmptyDataState()

                emptyProgressState = false
                viewController.showEmptyProgress(emptyProgressState)
                refreshProgressState = false
                viewController.showRefreshProgress(refreshProgressState)
                emptyDataState = true
                viewController.showEmptyData(emptyDataState)
            }
        }

        override fun fail(error: Throwable) {
            Timber.d("fail: ${error.javaClass.simpleName}")
            currentState = EmptyErrorState()

            emptyProgressState = false
            viewController.showEmptyProgress(emptyProgressState)
            refreshProgressState = false
            viewController.showRefreshProgress(refreshProgressState)
            emptyErrorState = true
            viewController.showEmptyError(emptyErrorState, error)
        }

        override fun release() {
            currentState = ReleasedState()
            requestJob?.cancel()
        }
    }

    /**
     * Состояние отображения ошибки на пустом экране с возможностью рестарта и обновления.
     *
     * Возможные последующие состояния:
     * [EmptyProgressState], [ReleasedState].
     */
    private inner class EmptyErrorState : State<K, V> {
        override fun restart() {
            Timber.d("restart")
            currentState = EmptyProgressState()

            emptyErrorState = false
            viewController.showEmptyError(emptyErrorState)
            emptyProgressState = true
            viewController.showEmptyProgress(emptyProgressState)
            loadPage(firstPage)
        }

        override fun refresh() {
            Timber.d("refresh")
            currentState = EmptyProgressState()

            emptyErrorState = false
            viewController.showEmptyError(false)
            emptyProgressState = true
            viewController.showEmptyProgress(true)
            loadPage(firstPage)
        }

        override fun release() {
            currentState = ReleasedState()
            requestJob?.cancel()
        }
    }

    /**
     * Состояние отображения пустых данных (т.е. их отсутствие) с возможностью рестарта и обновления.
     *
     * Возможные последующие состояния:
     * [EmptyProgressState], [ReleasedState].
     */
    private inner class EmptyDataState : State<K, V> {
        override fun restart() {
            Timber.d("restart")
            currentState = EmptyProgressState()

            emptyDataState = false
            viewController.showEmptyData(false)
            emptyProgressState = true
            viewController.showEmptyProgress(true)
            loadPage(firstPage)
        }

        override fun refresh() {
            Timber.d("refresh")
            currentState = EmptyProgressState()

            emptyDataState = false
            viewController.showEmptyData(false)
            emptyProgressState = true
            viewController.showEmptyProgress(true)
            loadPage(firstPage)
        }

        override fun release() {
            currentState = ReleasedState()
            requestJob?.cancel()
        }
    }

    /**
     * Состояние отображения данных (НЕ всех страниц).
     *
     * Возможные последующие состояния:
     * [EmptyProgressState], [RefreshState], [PageProgressState], [ReleasedState].
     */
    private inner class DataState : State<K, V> {
        override fun restart() {
            Timber.d("restart")
            currentState = EmptyProgressState()


            viewController.showData(false)
            emptyProgressState = true
            viewController.showEmptyProgress(emptyProgressState)
            loadPage(firstPage)
        }

        override fun refresh() {
            Timber.d("refresh")
            currentState = RefreshState()

            refreshProgressState = true
            viewController.showRefreshProgress(refreshProgressState)
            loadPage(firstPage)
        }

        override fun refreshPages(pageRange: IntRange) {
            Timber.d("refreshPages: $pageRange")
            currentState = RefreshAllPagesState()

            refreshProgressState = true
            viewController.showRefreshProgress(refreshProgressState)
            loadPagesAsync(pageRange)
        }

        override fun loadNewPage() {
            val nextPage = currentPage + 1
            Timber.d("loadNewPage: $nextPage")
            currentState = PageProgressState()

            pageProgressState = true
            viewController.showPageProgress(pageProgressState)
            loadPage(nextPage)
        }

        override fun replaceData(newData: Map<K, V>) {
            Timber.d("replaceData")

            data.clear()
            data.putAll(newData)
            viewController.showData(true, data)
        }

        override fun release() {
            currentState = ReleasedState()
            requestJob?.cancel()
        }
    }

    /**
     * Состояние обновления данных с последующим отображением данных (или пустых?), если не было ошибок.
     *
     * Возможные последующие состояния:
     * [EmptyProgressState], [DataState], [EmptyDataState], [ReleasedState].
     */
    private inner class RefreshState : State<K, V> {
        override fun restart() {
            Timber.d("restart")
            currentState = EmptyProgressState()

            viewController.showData(false)
            refreshProgressState = false
            viewController.showRefreshProgress(refreshProgressState)
            emptyProgressState = true
            viewController.showEmptyProgress(emptyProgressState)
            loadPage(firstPage)
        }

        override fun newData(newData: Map<K, V>) {
            Timber.d("newData: ${newData.size}")
            if (newData.isNotEmpty()) {
                currentState = DataState()

                data.clear()
                data.putAll(newData)
                currentPage = firstPage

                refreshProgressState = false
                viewController.showRefreshProgress(refreshProgressState)

                viewController.showData(true, newData)
            } else {
                currentState = EmptyDataState()

                data.clear()
                viewController.showData(false)
                refreshProgressState = false
                viewController.showRefreshProgress(refreshProgressState)
                emptyDataState = true
                viewController.showEmptyData(emptyDataState)
            }
        }

        override fun fail(error: Throwable) {
            Timber.d("fail: ${error.javaClass.simpleName}")
            currentState = DataState()

            refreshProgressState = false
            viewController.showRefreshProgress(refreshProgressState)
            viewController.showErrorMessage(error)
        }

        override fun release() {
            currentState = ReleasedState()
            requestJob?.cancel()
        }
    }

    /**
     * Состояние обновления данных с последующим отображением данных (или пустых?), если не было ошибок.
     *
     * Возможные последующие состояния:
     * [EmptyProgressState], [DataState], [EmptyDataState], [ReleasedState].
     */
    private inner class RefreshAllPagesState : State<K, V> {
        override fun restart() {
            Timber.d("restart")
            currentState = EmptyProgressState()

            viewController.showData(false)
            refreshProgressState = false
            viewController.showRefreshProgress(refreshProgressState)
            emptyProgressState = true
            viewController.showEmptyProgress(emptyProgressState)
            loadPage(firstPage)
        }

        override fun newData(newData: Map<K, V>) {
            Timber.d("newData: ${newData.size}")
            if (newData.isNotEmpty()) {
                currentState = DataState()

                data.clear()
                data.putAll(newData)

                refreshProgressState = false
                viewController.showRefreshProgress(refreshProgressState)

                viewController.showData(true, newData)
            } else {
                currentState = EmptyDataState()

                data.clear()
                viewController.showData(false)
                refreshProgressState = false
                viewController.showRefreshProgress(refreshProgressState)
                emptyDataState = true
                viewController.showEmptyData(emptyDataState)
            }
        }

        override fun fail(error: Throwable) {
            Timber.d("fail: ${error.javaClass.simpleName}")
            currentState = DataState()

            refreshProgressState = false
            viewController.showRefreshProgress(refreshProgressState)
            viewController.showErrorMessage(error)
        }

        override fun release() {
            currentState = ReleasedState()
            requestJob?.cancel()
        }
    }

    /**
     * Состояние подгрузки новой страницы.
     *
     * Возможные последующие состояния:
     * [EmptyProgressState], [DataState], [AllDataState], [RefreshState], [ReleasedState].
     */
    private inner class PageProgressState : State<K, V> {
        override fun restart() {
            Timber.d("restart")
            currentState = EmptyProgressState()

            viewController.showData(false)
            pageProgressState = false
            viewController.showPageProgress(pageProgressState)
            emptyProgressState = true
            viewController.showEmptyProgress(emptyProgressState)
            loadPage(firstPage)
        }

        override fun newData(newData: Map<K, V>) {
            Timber.d("newData: ${newData.size}")
            if (newData.isNotEmpty()) {
                currentState = DataState()

                data.putAll(newData)
                currentPage++

                pageProgressState = false
                viewController.showPageProgress(pageProgressState)
                viewController.showData(true, data)
            } else {
                currentState = AllDataState()

                pageProgressState = false
                viewController.showPageProgress(pageProgressState)
            }
        }

        override fun refresh() {
            Timber.d("refresh")
            currentState = RefreshState()

            pageProgressState = false
            viewController.showPageProgress(pageProgressState)
            refreshProgressState = true
            viewController.showRefreshProgress(refreshProgressState)
            loadPage(firstPage)
        }

        override fun fail(error: Throwable) {
            Timber.d("fail: ${error.javaClass.simpleName}")
            currentState = DataState()

            pageProgressState = false
            viewController.showPageProgress(pageProgressState)
            viewController.showErrorMessage(error)
        }

        override fun release() {
            currentState = ReleasedState()
            requestJob?.cancel()
        }
    }

    /**
     * Состояние, когда весь список (всее страницы) загружен.
     *
     * Возможные последующие состояния:
     * [EmptyProgressState], [RefreshState], [ReleasedState].
     */
    private inner class AllDataState : State<K, V> {
        override fun restart() {
            Timber.d("restart")
            currentState = EmptyProgressState()

            viewController.showData(false)
            emptyProgressState = true
            viewController.showEmptyProgress(emptyProgressState)
            loadPage(firstPage)
        }

        override fun refresh() {
            Timber.d("refresh")
            currentState = RefreshState()

            refreshProgressState = true
            viewController.showRefreshProgress(refreshProgressState)
            loadPage(firstPage)
        }

        override fun refreshPages(pageRange: IntRange) {
            Timber.d("refreshPages: $pageRange")
            currentState = RefreshAllPagesState()

            refreshProgressState = true
            viewController.showRefreshProgress(refreshProgressState)
            loadPagesAsync(pageRange)
        }

        override fun replaceData(newData: Map<K, V>) {
            Timber.d("replaceData")

            data.clear()
            data.putAll(newData)
            viewController.showData(true, data)
        }

        override fun release() {
            currentState = ReleasedState()
            requestJob?.cancel()
        }
    }

    /**
     * Состояние сброса состояния.
     */
    private inner class ReleasedState : State<K, V>
}