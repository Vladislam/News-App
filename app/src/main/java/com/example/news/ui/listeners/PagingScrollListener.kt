package com.example.news.ui.listeners

import android.widget.AbsListView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.news.util.const.Constants

class PagingScrollListener(
    private var pagingAction: () -> Unit
) : RecyclerView.OnScrollListener() {

    var isLoading: Boolean = false
    var isLastPage: Boolean = false
    private var isScrolling: Boolean = false

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        val layoutManager = recyclerView.layoutManager as LinearLayoutManager
        val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
        val visibleItemCount = layoutManager.childCount
        val totalItemCount = layoutManager.itemCount

        val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
        val isLastItemToPaginate = firstVisibleItemPosition + visibleItemCount > totalItemCount - 2
        val isNotAtBeginning = firstVisibleItemPosition >= 0
        val isTotalMoreThanVisible = totalItemCount >= Constants.QUERY_PAGE_SIZE
        val shouldPaginate =
            isNotLoadingAndNotLastPage && isLastItemToPaginate && isNotAtBeginning && isTotalMoreThanVisible && isScrolling

        if (shouldPaginate) {
            pagingAction.invoke()
            isScrolling = false
        }
    }

    fun registerCallback(callback: () -> Unit) {
        this.pagingAction = callback
    }

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)

        if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
            isScrolling = true
        }
    }
}