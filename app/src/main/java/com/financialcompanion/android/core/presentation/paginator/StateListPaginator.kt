package com.financialcompanion.android.core.presentation.paginator

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * @author Jamal Aliev (aliev.djamal.2000@gmail.com)
 */
class StateListPaginator<T>(
    private val coroutineScope: CoroutineScope,
    private val requestFactory: suspend (page: Int, pivotElement: T?) -> List<T>,
    private val viewController: ViewController<T>,
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

    private var currentState: State<T> = EmptyState()
    private val currentData = mutableListOf<T>()
    private var requestJob: Job? = null

    fun restart() = currentState.restart()
    fun refresh() = currentState.refresh()
    fun refreshAllPages() = currentState.refreshPages(firstPage..currentPage)
    fun loadNewPage() = currentState.loadNewPage()
    fun replaceData(data: List<T>) = currentState.replaceData(data)
    fun release() = currentState.release()

    private fun loadPage(page: Int, pivotElement: T?) {
        requestJob?.cancel()
        requestJob = coroutineScope.launch {
            try {
                val data = requestFactory.invoke(page, pivotElement)
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
            val pagesWithData = pages.map { page ->
                page to async { requestFactory.invoke(page, null) }
            }.map { (page, asyncData) ->
                try {
                    if (isActive) page to asyncData.await()
                    else page to emptyList()
                } catch (exception: Exception) {
                    currentState.fail(exception)
                    this.cancel(exception.message.orEmpty(), exception)
                    page to emptyList()
                }
            }
            if (isActive) {
                val allData = mutableListOf<T>()
                pagesWithData.forEach { (page, data) -> allData.addAll(data) }
                isLastPage = pagesWithData.last().second.isEmpty()
                currentState.newData(allData)
            }
        }
    }

    /**
     * Callback'и, вызываемые в момент перехода от одного состояния к другому.
     */
    interface ViewController<T> {
        fun showEmptyProgress(show: Boolean) {}
        fun showEmptyError(show: Boolean, error: Throwable? = null) {}
        fun showEmptyData(show: Boolean) {}
        fun showData(show: Boolean, data: List<T> = emptyList()) {}
        fun showErrorMessage(error: Throwable) {}
        fun showRefreshProgress(show: Boolean) {}
        fun showPageProgress(show: Boolean) {}
    }

    private interface State<T> {
        fun restart() {}
        fun refresh() {}
        fun loadNewPage() {}
        fun newData(data: List<T>) {}
        fun fail(error: Throwable) {}
        fun release() {}
        fun refreshPages(pageRange: IntRange) {}
        fun replaceData(data: List<T>) {}
    }

    /**
     * Первоначальное пустое состояние, когда загрузка данных еще не выполнена.
     *
     * Возможные последующие состояния:
     * [EmptyProgressState], [ReleasedState].
     */
    private inner class EmptyState : State<T> {
        override fun refresh() {
            Timber.d("refresh")
            currentState = EmptyProgressState()

            emptyProgressState = true
            viewController.showEmptyProgress(emptyProgressState)
            loadPage(firstPage, null)
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
    private inner class EmptyProgressState : State<T> {
        override fun restart() {
            Timber.d("restart")
            loadPage(firstPage, null)
        }

        override fun newData(data: List<T>) {
            Timber.d("newData: ${data.size}")
            if (data.isNotEmpty()) {
                currentState = DataState()

                currentData.clear()
                currentData.addAll(data)
                currentPage = firstPage

                emptyProgressState = false
                viewController.showEmptyProgress(emptyProgressState)
                refreshProgressState = false
                viewController.showRefreshProgress(refreshProgressState)
                viewController.showData(true, currentData)
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
    private inner class EmptyErrorState : State<T> {
        override fun restart() {
            Timber.d("restart")
            currentState = EmptyProgressState()

            emptyErrorState = false
            viewController.showEmptyError(emptyErrorState)
            emptyProgressState = true
            viewController.showEmptyProgress(emptyProgressState)
            loadPage(firstPage, null)
        }

        override fun refresh() {
            Timber.d("refresh")
            currentState = EmptyProgressState()

            emptyErrorState = false
            viewController.showEmptyError(false)
            emptyProgressState = true
            viewController.showEmptyProgress(true)
            loadPage(firstPage, null)
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
    private inner class EmptyDataState : State<T> {
        override fun restart() {
            Timber.d("restart")
            currentState = EmptyProgressState()

            emptyDataState = false
            viewController.showEmptyData(false)
            emptyProgressState = true
            viewController.showEmptyProgress(true)
            loadPage(firstPage, null)
        }

        override fun refresh() {
            Timber.d("refresh")
            currentState = EmptyProgressState()

            emptyDataState = false
            viewController.showEmptyData(false)
            emptyProgressState = true
            viewController.showEmptyProgress(true)
            loadPage(firstPage, null)
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
    private inner class DataState : State<T> {
        override fun restart() {
            Timber.d("restart")
            currentState = EmptyProgressState()


            viewController.showData(false)
            emptyProgressState = true
            viewController.showEmptyProgress(emptyProgressState)
            loadPage(firstPage, null)
        }

        override fun refresh() {
            Timber.d("refresh")
            currentState = RefreshState()

            refreshProgressState = true
            viewController.showRefreshProgress(refreshProgressState)
            loadPage(firstPage, null)
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
            loadPage(nextPage, currentData.lastOrNull())
        }

        override fun replaceData(data: List<T>) {
            Timber.d("replaceData")

            currentData.clear()
            currentData.addAll(data)
            viewController.showData(true, currentData)
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
    private inner class RefreshState : State<T> {
        override fun restart() {
            Timber.d("restart")
            currentState = EmptyProgressState()

            viewController.showData(false)
            refreshProgressState = false
            viewController.showRefreshProgress(refreshProgressState)
            emptyProgressState = true
            viewController.showEmptyProgress(emptyProgressState)
            loadPage(firstPage, null)
        }

        override fun newData(data: List<T>) {
            Timber.d("newData: ${data.size}")
            if (data.isNotEmpty()) {
                currentState = DataState()

                currentData.clear()
                currentData.addAll(data)
                currentPage = firstPage

                refreshProgressState = false
                viewController.showRefreshProgress(refreshProgressState)

                viewController.showData(true, data)
            } else {
                currentState = EmptyDataState()

                currentData.clear()
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
    private inner class RefreshAllPagesState : State<T> {
        override fun restart() {
            Timber.d("restart")
            currentState = EmptyProgressState()

            viewController.showData(false)
            refreshProgressState = false
            viewController.showRefreshProgress(refreshProgressState)
            emptyProgressState = true
            viewController.showEmptyProgress(emptyProgressState)
            loadPage(firstPage, null)
        }

        override fun newData(data: List<T>) {
            Timber.d("newData: ${data.size}")
            if (data.isNotEmpty()) {
                currentState = DataState()

                currentData.clear()
                currentData.addAll(data)

                refreshProgressState = false
                viewController.showRefreshProgress(refreshProgressState)

                viewController.showData(true, data)
            } else {
                currentState = EmptyDataState()

                currentData.clear()
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
    private inner class PageProgressState : State<T> {
        override fun restart() {
            Timber.d("restart")
            currentState = EmptyProgressState()

            viewController.showData(false)
            pageProgressState = false
            viewController.showPageProgress(pageProgressState)
            emptyProgressState = true
            viewController.showEmptyProgress(emptyProgressState)
            loadPage(firstPage, null)
        }

        override fun newData(data: List<T>) {
            Timber.d("newData: ${data.size}")
            if (data.isNotEmpty()) {
                currentState = DataState()

                currentData.addAll(data)
                currentPage++

                pageProgressState = false
                viewController.showPageProgress(pageProgressState)
                viewController.showData(true, currentData)
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
            loadPage(firstPage, null)
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
    private inner class AllDataState : State<T> {
        override fun restart() {
            Timber.d("restart")
            currentState = EmptyProgressState()

            viewController.showData(false)
            emptyProgressState = true
            viewController.showEmptyProgress(emptyProgressState)
            loadPage(firstPage, null)
        }

        override fun refresh() {
            Timber.d("refresh")
            currentState = RefreshState()

            refreshProgressState = true
            viewController.showRefreshProgress(refreshProgressState)
            loadPage(firstPage, null)
        }

        override fun refreshPages(pageRange: IntRange) {
            Timber.d("refreshPages: $pageRange")
            currentState = RefreshAllPagesState()

            refreshProgressState = true
            viewController.showRefreshProgress(refreshProgressState)
            loadPagesAsync(pageRange)
        }

        override fun replaceData(data: List<T>) {
            Timber.d("replaceData")

            currentData.clear()
            currentData.addAll(data)
            viewController.showData(true, currentData)
        }

        override fun release() {
            currentState = ReleasedState()
            requestJob?.cancel()
        }
    }

    /**
     * Состояние сброса состояния.
     */
    private inner class ReleasedState : State<T>
}