package com.financialcompanion.android.core.presentation.paginator

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * @author Jamal Aliev (aliev.djamal.2000@gmail.com)
 */
class MapArrayPaginator<T>(
    private val coroutineScope: CoroutineScope,
    private val ioDispatcher: CoroutineDispatcher,
    private val uiDispatcher: CoroutineDispatcher,
    private val requestFactory: suspend (page: Int) -> List<T>,
    var pageStateController: ((page: Int, oldPageState: PageState?, currentPageState: PageState?) -> Unit)? = null,
    /** Опциональное поле. Используется только в случае вызова метода resize */
    var maxPageSize: Int = 20,
    defaultPages: Map<Int, List<T>>? = null
) {

    private val pages = hashMapOf<Int, PageState>()
    fun putPage(page: Int, pageState: PageState) = pages.put(page, pageState)
    fun getPageState(page: Int) = pages[page]!!

    var extraNextPage = mutableListOf<T>()
    var extraPrevPage = mutableListOf<T>()

    init {
        defaultPages?.forEach { (page, data) ->
            pages[page] = PageState.Data(data)
        }
    }

    var currentPage = INCORRECT_PAGE
        private set
    var beforePage = currentPage
        private set

    val minPage get() = pages.keys.minOrNull() ?: INCORRECT_PAGE
    val maxPage get() = pages.keys.maxOrNull() ?: INCORRECT_PAGE
    val isCorrectPages: Boolean
        get() = minPage != INCORRECT_PAGE && maxPage != INCORRECT_PAGE
                && currentPage != INCORRECT_PAGE

    private var refreshAllJob: Job? = null
    var isRefreshing: Boolean = false
        private set
    var isResizing: Boolean = false
        private set

    /**
     * refreshListeners вызываются перед концом выполнения refresh. Т.е. isRefreshing может иметь другое значение
     * refreshListeners вызываются только для операции refreshAllPages
     * */
    private val refreshListeners by lazy { hashMapOf<Int, (Boolean) -> Unit>() }
        .also { refreshListenersLazy = it }
    private var refreshListenersLazy: Lazy<Any>? = null
    fun removeRefreshStateListener(idObserver: Int) = refreshListeners.remove(idObserver)
    fun setRefreshStateListener(idObserver: Int, listener: (isRefreshing: Boolean) -> Unit) {
        refreshListeners[idObserver] = listener
    }

    /**
     * resizeListeners вызываются перед концом выполнения resize. Т.е. isResizing может иметь другое значение
     * */
    private val resizeListeners by lazy { hashMapOf<Int, (Boolean) -> Unit>() }
        .also { resizeListenersLazy = it }
    private var resizeListenersLazy: Lazy<Any>? = null
    fun removeResizeStateListener(idObserver: Int) = resizeListeners.remove(idObserver)
    fun setResizeStateListener(idObserver: Int, listener: (isResizing: Boolean) -> Unit) {
        resizeListeners[idObserver] = listener
    }

    /** Прыжок на определенную страницу, используется также для загрузки стартовой/первой */
    fun jump(page: Int = FIRST_PAGE) {
        if (page < FIRST_PAGE) throw IllegalArgumentException("Jump to $page impossible")
        if (page == currentPage + 1) return loadNextPage()
        else if (page == currentPage - 1) return loadPrevPage()
        loadOrNotifyPage(page)
    }

    fun jumpBack() {
        if (beforePage == INCORRECT_PAGE) throw IllegalArgumentException("Jump to $beforePage impossible")
        jump(beforePage)
    }

    /** Загружеает следующию страницу от последней существующией */
    fun loadNextPage() {
        if (currentPage < FIRST_PAGE) return jump()
        val pivotPage = getPivotNextPage()
        loadOrNotifyPage(pivotPage + 1)
    }

    /** Загружеает предыдущию страницу от последней существующией */
    fun loadPrevPage() {
        if (currentPage <= FIRST_PAGE) return jump()
        val pivotPage = getPivotPrevPage()
        if (pivotPage > FIRST_PAGE) loadOrNotifyPage(pivotPage - 1)
    }

    fun refresh() {
        if (currentPage < FIRST_PAGE) return jump()
        loadPage(currentPage)
    }

    fun refreshAllPages() {
        if (currentPage < FIRST_PAGE) return jump()
        if (refreshAllJob?.isActive == true) return
        refreshAllJob = coroutineScope.launch(ioDispatcher) {
            isRefreshing = true
            withContext(uiDispatcher) {
                refreshListeners.values.forEach { it.invoke(isRefreshing) }
            }

            pages.keys.forEach(::loadPage)
            loadPageJobs.values.forEach { it.join() }

            isRefreshing = false
            withContext(uiDispatcher) {
                refreshListeners.values.forEach { it.invoke(isRefreshing) }
            }
        }
    }

    var snapshotData: MutableList<T>? = null
        private set

    /** Производит сканирование текущей и всех соседних страниц и возвращает их данные */
    fun snapshot(page: Int = currentPage): MutableList<T> {
        val startPivotPage = getPivotDataPrevPage(page)
        val endPivotPage = getPivotDataNextPage(page)

        if (snapshotData == null) {
            var capacity = maxPageSize
            capacity += (endPivotPage - startPivotPage) * maxPageSize
            capacity += extraPrevPage.size + extraNextPage.size
            snapshotData = ArrayList(capacity)
        }

        snapshotData?.clear()
        snapshotData?.addAll(extraPrevPage)
        for (i in startPivotPage..endPivotPage)
            snapshotData?.addAll(getDataPage(i))
        snapshotData?.addAll(extraNextPage)

        return snapshotData!!
    }

    fun isCurrentPage(page: Int) = currentPage == page
    fun isNextOrPrevPage(page: Int) = isNextPage(page) || isPrevPage(page)
    fun isTooFarPage(page: Int) = !isNextOrPrevPage(page) && !isCurrentPage(page)

    fun isNextPage(page: Int): Boolean {
        if (page < currentPage || currentPage == INCORRECT_PAGE) return false
        for (i in page downTo currentPage) if (pages[i] == null) return false
        return true
    }

    fun isPrevPage(page: Int): Boolean {
        if (page > currentPage || currentPage == INCORRECT_PAGE) return false
        for (i in page..currentPage) if (pages[i] == null) return false
        return true
    }

    fun pageOfFirst(predicate: (T) -> Boolean): Int {
        for (page in pages.keys) {
            val pageState = pages[page]
            if (pageState is PageState.Data<*>) {
                val index = pageState.toDataState().data.indexOfFirst(predicate)
                if (index != -1) return page
            }
        }
        return -1
    }


    fun replaceFirstIf(item: () -> T, predicate: (T) -> Boolean): Int {
        for (page in pages.keys) {
            val replacedPage = replaceItemBy(page, item, predicate)
            if (replacedPage != -1) return replacedPage
        }
        return -1
    }

    /**
     * replaceListeners вызываются после успешной замены элемента
     * */
    private val replaceListeners by lazy { hashMapOf<Int, (page: Int, snapshotIndex: Int, predicate: (T) -> Boolean, oldItem: T, newItem: T) -> Unit>() }
        .also { replaceListenersLazy = it }
    private var replaceListenersLazy: Lazy<Any>? = null
    fun removeReplaceStateListener(idObserver: Int) = replaceListeners.remove(idObserver)
    fun setReplaceStateListener(
        idObserver: Int,
        listener: (page: Int, snapshotIndex: Int, predicate: (T) -> Boolean, oldItem: T, newItem: T) -> Unit
    ) {
        replaceListeners[idObserver] = listener
    }

    fun replaceItemBy(page: Int, item: () -> T, predicate: (T) -> Boolean): Int {
        val pageState = pages[page]
        if (pageState !is PageState.Data<*>) return -1
        val data = pageState.toDataState().data
        val index = data.indexOfFirst(predicate)
        if (index == -1) return -1
        val newItem = item()
        val oldItem = (data as MutableList<T>)[index]
        data[index] = newItem
        val indexSnapshot = snapshotData?.indexOfFirst(predicate) ?: -1
        if (indexSnapshot != -1) snapshotData?.set(indexSnapshot, newItem)
        if (replaceListenersLazy?.isInitialized() == true) {
            replaceListeners.values.forEach { replaceListener ->
                replaceListener(page, indexSnapshot, predicate, oldItem, newItem)
            }
        }
        return page
    }

    fun removeItemBy(predicate: (T) -> Boolean): T? {
        for (page in pages.keys) {
            val removedObject = removeItemBy(page, predicate)
            if (removedObject != null) return removedObject
        }
        return null
    }

    fun removeItemBy(page: Int, predicate: (T) -> Boolean): T? {
        val pageState = pages[page]
        if (pageState !is PageState.Data<*>) return null
        val data = pageState.toDataState().data as MutableList
        val index = data.indexOfFirst(predicate)
        if (index == -1) return null
        val removedItem = data.removeAt(index)
        val indexSnapshot = snapshotData?.indexOfFirst(predicate) ?: -1
        if (indexSnapshot != -1) snapshotData?.removeAt(indexSnapshot)
        if (data.isEmpty()) pages[page] = PageState.Empty
        return removedItem
    }

    fun add(element: T, index: Int? = null, pageNum: Int = minPage) {
        var page = pages[pageNum]
        if (page !is PageState.Data<*>) {
            page = PageState.Data<T>(mutableListOf())
            pages[pageNum] = page
        }
        val dataPage = page.castToDataState()?.data as MutableList
        if (index != null) dataPage.add(index, element)
        else dataPage.add(element)
    }

    fun swap(firstPage: Int, firstPosition: Int, secondPage: Int, secondPosition: Int): Boolean {
        val firstPageState = pages[firstPage].castToDataState() ?: return false
        val secondPageState = pages[secondPage].castToDataState() ?: return false
        val firstPageData = firstPageState.data as MutableList
        val secondPageData = secondPageState.data as MutableList
        val firstElement = firstPageData.getOrNull(firstPosition) ?: return false
        val secondElement = secondPageData.getOrNull(secondPosition) ?: return false
        firstPageData[firstPosition] = secondElement
        secondPageData[secondPosition] = firstElement
        return true
    }

    fun getDataPage(page: Int): List<T> {
        if (page < FIRST_PAGE) throw IllegalArgumentException("getDataPage from $page impossible")
        return pages[page].castToDataState()
            ?.data.orEmpty()
    }

    fun getAllPages() = pages.keys.toList()
    fun getAllPageStates() = pages.values.toList()
    fun getAllData(): List<T> = mutableListOf<T>().apply {
        addAll(extraPrevPage)
        getAllPages().sorted()
            .forEach { page -> addAll(getDataPage(page)) }
        addAll(extraNextPage)
    }

    fun getPivotDataPrevPage(page: Int = currentPage): Int {
        var pivotPage = page
        for (i in pivotPage downTo Int.MIN_VALUE) {
            if (pages[i] !is PageState.Data<*>) break
            else pivotPage = i
        }
        return pivotPage
    }

    fun getPivotPrevPage(page: Int = currentPage): Int {
        var pivotPage = page
        for (i in pivotPage downTo Int.MIN_VALUE) {
            if (pages[i] == null) break
            else pivotPage = i
        }
        return pivotPage
    }

    fun getPivotDataNextPage(page: Int = currentPage): Int {
        var pivotPage = page
        for (i in pivotPage..Int.MAX_VALUE) {
            if (pages[i] !is PageState.Data<*>) break
            else pivotPage = i
        }
        return pivotPage
    }

    fun getPivotNextPage(page: Int = currentPage): Int {
        var pivotPage = page
        for (i in pivotPage..Int.MAX_VALUE) {
            if (pages[i] == null) break
            else pivotPage = i
        }
        return pivotPage
    }

    private val resizePagesUpInCoroutine by lazy {
        coroutineScope.launch(
            context = ioDispatcher,
            start = CoroutineStart.LAZY
        ) { resizePagesUp() }
    }

    fun resizePagesUpInCoroutine() {
        if (resizePagesUpInCoroutine.isActive) resizePagesUpInCoroutine.cancel()
        resizePagesUpInCoroutine.start()
    }

    /**
     * Перебрасывает елементы с предудущих страниц на следующие учитывая maxPageSize.
     * Примечания - страница без данных и все которые идут после нее будут удалены, кроме одной
     * Имеет смысл вызывать при добавлении элементов
     * */
    suspend fun resizePagesUp() = coroutineScope {
        if (!isCorrectPages) return@coroutineScope
        setCurrentPage(minPage)
        withContext(uiDispatcher) {
            isResizing = true
            resizeListeners.values.forEach { it.invoke(isResizing) }
        }

        for (page in minPage..maxPage) {
            var currentPageState = pages[page]
            if (currentPageState !is PageState.Data<*>) {
                for (i in (page + 1)..maxPage) pages.remove(i)
                break
            }

            currentPageState = currentPageState.castToDataState()
            val currentPageData = currentPageState?.data as MutableList<T>

            val nextPageState = pages[page + 1]?.castToDataState()
            val nextPageData = nextPageState?.data as? MutableList<T>

            if (currentPageData.size > maxPageSize) {
                do {
                    val removedElement = currentPageData.removeLast()
                    nextPageData?.add(0, removedElement)
                } while (currentPageData.size > maxPageSize)

                withContext(uiDispatcher) { notifyPageState(page, currentPageState) }
                setCurrentPage(page)

                if (nextPageData != null && nextPageData.size < maxPageSize) {
                    for (i in (page + 1)..maxPage) pages.remove(i)
                    break
                }
            }
        }

        withContext(uiDispatcher) {
            isResizing = false
            resizeListeners.values.forEach { it.invoke(isResizing) }
        }
    }

    /**
     * Перебрасывает елементы с последующих страниц на предыдущие учитывая maxPageSize.
     * Примечания - страница без данных и все которые идут после нее будут удалены
     * Имеет смысл вызывать при удалении элементов
     * */
    suspend fun resizePagesDown() = coroutineScope {
        if (!isCorrectPages) return@coroutineScope
        setCurrentPage(minPage)

        withContext(uiDispatcher) {
            isResizing = true
            resizeListeners.values.forEach { it.invoke(isResizing) }
        }

        val loadJobs = mutableListOf<Job>()

        for (page in minPage..maxPage) {
            if (pages[page] !is PageState.Data<*>) {
                for (i in page..maxPage) pages.remove(i)
                break
            }

            val currentPageState = pages[page].toDataState()
            val currentPageData = currentPageState.data as MutableList<T>

            val srcPageRange = (page + 1)..maxPage
            if (srcPageRange.step > 0 && resizePagesDown(currentPageData, srcPageRange)) {
                withContext(uiDispatcher) { notifyPageState(page, currentPageState) }
                setCurrentPage(page)
            } else {
                loadPageSilently(page)
                loadJobs.add(loadPageJobs.getValue(page))
            }
        }

        loadJobs.forEach { it.join() }

        withContext(uiDispatcher) {
            isResizing = false
            resizeListeners.values.forEach { it.invoke(isResizing) }
        }
    }

    private fun resizePagesDown(srcPage: MutableList<T>, srcPageRange: IntRange): Boolean {
        for (page in srcPageRange) {
            val srcNextPageState = pages[page].toDataState()
            val srcNextPageData = srcNextPageState.data as MutableList<T>
            resizePagesDown(srcPage, srcNextPageData)
            if (srcPage.size == maxPageSize) return true
        }
        return false
    }

    private fun resizePagesDown(src1: MutableList<T>, src2: MutableList<T>) {
        if (src1 === src2) return
        while (src1.size < maxPageSize && src2.isNotEmpty())
            src1.add(src2.removeFirst())
    }

    private fun loadOrNotifyPage(page: Int) {
        if (pages[page] !is PageState.Data<*>) loadPage(page)
        else {
            notifyPageState(page, getPageState(page))
            setCurrentPage(page)
        }
    }

    private val loadPageJobs = hashMapOf<Int, Job>()
    fun loadPageSilently(page: Int) = loadPage(page, true)
    private fun loadPage(page: Int, silently: Boolean = false) {
        if (loadPageJobs[page]?.isActive == true) return
        loadPageJobs[page] = coroutineScope.launch(ioDispatcher) {

            var oldPageState = pages[page]
            val refreshOrProgressPageState =
                if (oldPageState is PageState.Data<*>) PageState.Refresh(oldPageState.data)
                else PageState.Progress
            pages[page] = refreshOrProgressPageState

            if (!silently) {
                withContext(uiDispatcher) {
                    notifyPageState(page, oldPageState, refreshOrProgressPageState)
                }
            }

            var finalState: PageState? = null
            try {
                val requestData = requestFactory.invoke(page)
                val data =
                    if (requestData.isEmpty()) emptyList()
                    else ArrayList<T>(maxPageSize).apply { addAll(requestData) }

                oldPageState = refreshOrProgressPageState
                finalState = if (data.isEmpty()) PageState.Empty else PageState.Data(data)
                pages[page] = finalState

            } catch (e: Exception) {
                e.printStackTrace()
                oldPageState = refreshOrProgressPageState
                finalState = PageState.Error(e)
                pages[page] = finalState
            } finally {
                if (!silently) {
                    withContext(uiDispatcher) { notifyPageState(page, oldPageState, finalState) }
                    setCurrentPage(page)
                }
            }
        }
    }

    private fun notifyPageState(page: Int, oldPageState: PageState?) {
        pageStateController?.invoke(page, oldPageState, pages[page])
    }

    private fun notifyPageState(page: Int, oldPageState: PageState?, currentPageState: PageState?) {
        pageStateController?.invoke(page, oldPageState, currentPageState)
    }

    private fun setCurrentPage(page: Int) {
        beforePage = currentPage
        currentPage = page
    }

    fun release() {
        snapshotData?.clear()
        snapshotData = null

        loadPageJobs.values.forEach(Job::cancel)
        loadPageJobs.clear()

        refreshAllJob?.cancel()
        refreshAllJob = null

        if (refreshListenersLazy?.isInitialized() == true) refreshListeners.clear()
        if (resizeListenersLazy?.isInitialized() == true) resizeListeners.clear()
        if (replaceListenersLazy?.isInitialized() == true) replaceListeners.clear()

        pages.clear()
        currentPage = INCORRECT_PAGE
    }

    sealed class PageState {
        object Progress : PageState()
        data class Refresh<T>(val data: List<T>) : PageState()
        object Empty : PageState()
        data class Error(val exception: Exception) : PageState()
        data class Data<T>(val data: List<T>) : PageState()

        fun copy(): PageState {
            return when (this) {
                is Data<*> -> Data(data.toList())
                is Error -> Error(exception)
                is Refresh<*> -> Refresh(data.toList())
                Empty -> Empty
                Progress -> Progress
            }
        }
    }

    private fun PageState?.castToDataState() = this as? PageState.Data<T>
    private fun PageState?.toDataState() = this as PageState.Data<T>

    companion object {
        private const val FIRST_PAGE = 1
        private const val INCORRECT_PAGE = -1
    }
}
