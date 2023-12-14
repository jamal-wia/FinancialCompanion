package com.financialcompanion.android.core.presentation.paginator

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.TreeSet

/**
 * @author Jamal Aliev (aliev.djamal.2000@gmail.com)
 */
class SortedSetPaginator<T>(
    private val coroutineScope: CoroutineScope,
    private val ioDispatcher: CoroutineDispatcher,
    private val uiDispatcher: CoroutineDispatcher,
    private val requestFactory: suspend (page: Int, pivotNext: T?, pivotPrev: T?) -> List<T>,
    private val viewController: ViewController<T>,
    private val comparator: Comparator<T>? = null,
    defaultPages: Map<Int, List<T>>? = null
) {

    private val pages by lazy { sortedMapOf<Int, PageState>() }
    private val data by lazy { comparator?.let { sortedSetOf(it) } ?: sortedSetOf() }

    var currentPage = INCORRECT_PAGE
        private set
    var beforePage = currentPage
        private set

    val minPage
        get() = try {
            pages.firstKey()
        } catch (e: NoSuchElementException) {
            INCORRECT_PAGE
        }

    val maxPage
        get() = try {
            pages.lastKey()
        } catch (e: NoSuchElementException) {
            INCORRECT_PAGE
        }

    val isCorrectPages: Boolean
        get() = minPage != INCORRECT_PAGE && maxPage != INCORRECT_PAGE
                && currentPage != INCORRECT_PAGE

    var currentState: PageState = PageState.Empty
        private set(value) {
            beforeState = currentState
            field = value
        }

    var beforeState: PageState? = null
        private set

    init {
        defaultPages?.let {
            it.forEach { (page, pageData) ->
                pages[page] = if (pageData.isEmpty()) PageState.Empty else PageState.Data
                data.addAll(pageData)
            }
        }
    }

    var jumpJob: Job? = null
        private set

    /** Прыжок на определенную страницу, используется также для загрузки стартовой/первой */
    fun jump(page: Int = FIRST_PAGE) {
        if (page < FIRST_PAGE) throw IllegalArgumentException("Jump to $page impossible")
        if (page == currentPage + 1) return loadNextPage()
        else if (page == currentPage - 1) return loadPrevPage()
        if (jumpJob != null) return
        jumpJob = coroutineScope.launch(ioDispatcher) {
            val snapshot = data.toSet()
            release()

            withContext(uiDispatcher) {
                viewController.showEmptyProgress(true)
                viewController.showEmptyError(null)
                viewController.showEmptyData(false)
            }

            try {
                val requestData = requestFactory.invoke(page, null, null)
                pages[page] = if (requestData.isEmpty()) PageState.Empty else PageState.Data
                data.addAll(requestData)
                setCurrentPage(page)
                withContext(uiDispatcher) {
                    viewController.showEmptyData(data.isEmpty())
                    viewController.showData(data)
                }
            } catch (e: Exception) {
                data.addAll(snapshot)
                withContext(uiDispatcher) { viewController.showEmptyError(e) }
            } finally {
                withContext(uiDispatcher) { viewController.showEmptyProgress(false) }
            }

            jumpJob = null
        }
    }

    fun jumpBack() {
        if (beforePage == INCORRECT_PAGE) throw IllegalArgumentException("Jump to $beforePage impossible")
        jump(beforePage)
    }

    val isLoadingNextPage get() = loadingNextPages.isNotEmpty()

    private val loadingNextPages by lazy { mutableListOf<Int>() }
    private val errorNextPages by lazy { mutableListOf<Int>() }

    /** Загружеает следующию страницу от последней существующией */
    fun loadNextPage() {
        if (currentPage < FIRST_PAGE) return jump()
        val nextPage = maxPage + 1
        loadingNextPages.remove(nextPage)
        loadingNextPages.add(nextPage)
        loadPage(nextPage)
    }

    val isLoadingPrevPage get() = loadingPrevPages.isNotEmpty()

    private val loadingPrevPages by lazy { mutableListOf<Int>() }
    private val errorPrevPages by lazy { mutableListOf<Int>() }

    /** Загружеает предыдущию страницу от последней существующией */
    fun loadPrevPage() {
        if (currentPage <= FIRST_PAGE) return jump()
        val prevPage = minPage - 1
        if (prevPage > FIRST_PAGE) {
            loadingPrevPages.add(prevPage)
            loadPage(prevPage)
        } else {
            throw IllegalArgumentException("loadPrevPage for $prevPage impossible")
        }
    }

    val isRefreshingState get() = refreshAllJob != null || refreshJob != null
    var refreshAllJob: Job? = null
        private set

    fun refreshAll() {
        if (currentPage < FIRST_PAGE) return jump()
        if (isRefreshingState) return
        refreshAllJob = coroutineScope.launch {
            viewController.showRefreshProgress(show = isRefreshingState)

            pages.keys.forEach { loadPage(it) }
            loadPageJobs.values.forEach { it.join() }
            refreshAllJob = null

            viewController.showRefreshProgress(show = isRefreshingState)
        }
    }

    private var refreshJob: Job? = null
    fun refresh() {
        if (currentPage < FIRST_PAGE) return jump()
        if (isRefreshingState) return
        refreshJob = coroutineScope.launch {
            viewController.showRefreshProgress(show = isRefreshingState)

            loadPage(currentPage)
            loadPageJobs.values.forEach { it.join() }
            refreshJob = null

            viewController.showRefreshProgress(show = isRefreshingState)
        }
    }

    fun release() {
        setCurrentPage(INCORRECT_PAGE)
        pages.clear()
        data.clear()
        loadingNextPages.clear()
        loadingPrevPages.clear()
        errorNextPages.clear()
        errorPrevPages.clear()
        loadPageJobsSafe?.values?.forEach { it.cancel() }
        loadPageJobsSafe?.clear()
        refreshAllJob = null
        refreshJob = null
    }

    fun add(element: T, silently: Boolean = false) {
        data.add(element)
        if (!silently) {
            viewController.showEmptyData(false)
            viewController.showEmptyProgress(false)
            viewController.showEmptyError(null)
            viewController.showData(data)
        }
    }

    fun replaceFirstIf(
        item: T, predicate: (T) -> Boolean,
        descending: Boolean = false,
        silently: Boolean = false
    ): T? {
        val removed = removeFirstIf(predicate, descending, silently = true)
        if (removed != null) data.add(item)
        if (!silently) viewController.showData(data)
        return removed
    }

    fun replaceFirstIf(
        item: () -> T, predicate: (T) -> Boolean,
        descending: Boolean = false,
        silently: Boolean = false
    ): T? {
        val removed = removeFirstIf(predicate, descending, silently = true)
        if (removed != null) add(item())
        if (!silently) viewController.showData(data)
        return removed
    }

    fun removeFirstIf(
        predicate: (T) -> Boolean,
        descending: Boolean = false,
        silently: Boolean = false
    ): T? {
        val iterator: Iterator<T> = if (descending) data.descendingIterator() else data.iterator()
        while (iterator.hasNext()) {
            val next = iterator.next()
            if (predicate(next)) {
                data.remove(next)
                if (!silently) viewController.showData(data)
                return next
            }
        }
        return null
    }

    fun removeAllIf(
        predicate: (T) -> Boolean, descending: Boolean = false,
        silently: Boolean = false
    ): Boolean {
        var removed = false
        val iterator: Iterator<T> = if (descending) data.descendingIterator() else data.iterator()
        while (iterator.hasNext()) {
            val next = iterator.next()
            if (predicate(next)) {
                data.remove(next)
                removed = true
            }
        }
        if (!silently) viewController.showData(data)
        return removed
    }

    fun remove(element: T, silently: Boolean = false) {
        data.remove(element)
        if (!silently) viewController.showData(data)
    }

    fun possibleNextPagination(): Boolean {
        val maxPage = maxPage
        if (maxPage == INCORRECT_PAGE) return false
        val lastPageState = pages[maxPage]
        return lastPageState !is PageState.Empty
    }

    fun possiblePrevPagination(): Boolean {
        val lastPageState = pages[minPage]
        return lastPageState !is PageState.Empty
    }

    private var loadPageJobsLazy: Lazy<*>? = null
    private val loadPageJobsSafe get() = if (loadPageJobsLazy?.isInitialized() == true) loadPageJobs else null
    private val loadPageJobs by lazy { hashMapOf<Int, Job>() }
        .also { loadPageJobsLazy = it }

    fun loadPageSilently(page: Int) = loadPage(page, silently = true)
    private fun loadPage(page: Int, silently: Boolean = false) {
        if (loadPageJobs[page]?.isActive == true) return
        loadPageJobs[page] = coroutineScope.launch(ioDispatcher) {
            try {
                if (!silently) withContext(uiDispatcher) { viewController.showData(data) }

                val pivotNext = if (page > currentPage) data.lastOrNull() else null
                val pivotPrev = if (page < currentPage) data.firstOrNull() else null
                val requestData = requestFactory.invoke(page, pivotNext, pivotPrev)
                pages[page] = if (requestData.isEmpty()) PageState.Empty else PageState.Data
                data.addAll(requestData)

                errorNextPages.remove(page)
                errorPrevPages.remove(page)

                setCurrentPage(page)
                if (!silently) withContext(uiDispatcher) { viewController.showData(data) }
            } catch (e: Exception) {
                if (page > currentPage) errorNextPages.add(page)
                else if (page < currentPage) errorPrevPages.add(page)
            } finally {
                loadingNextPages.remove(page)
                loadingPrevPages.remove(page)
                if (!silently) withContext(uiDispatcher) { viewController.showData(data) }
            }
        }
    }

    private fun setCurrentPage(page: Int) {
        beforePage = currentPage
        currentPage = page
    }

    companion object {
        private const val FIRST_PAGE = 1
        private const val INCORRECT_PAGE = -1
    }

    sealed class PageState {
        object Empty : PageState()
        object Error : PageState()
        object Data : PageState()
        object Progress : PageState()
        object Refresh : PageState()
    }

    interface ViewController<T> {
        fun showEmptyProgress(show: Boolean) {}
        fun showEmptyError(error: Throwable? = null) {}
        fun showEmptyData(show: Boolean) {}
        fun showData(data: TreeSet<T>) {}
        fun showRefreshProgress(show: Boolean) {}
    }
}

