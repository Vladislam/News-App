package com.example.news.ui.listeners

import android.widget.AbsListView
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.news.util.Constants

class PagingScrollListener(
    private val pagingAction: (String) -> Unit,
    private val arg: Any,

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
        val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
        val isNotAtBeginning = firstVisibleItemPosition >= 0
        val isTotalMoreThanVisible = totalItemCount >= Constants.QUERY_PAGE_SIZE
        val shouldPaginate =
            isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning && isTotalMoreThanVisible && isScrolling

        if (shouldPaginate) {
            pagingAction.invoke(when (arg) {
                is String -> {
                    arg
                }
                is EditText -> {
                    arg.text.toString()
                }
                else -> {
                    return
                }
            })
            isScrolling = false
        }
    }

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)

        if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
            isScrolling = true
        }
    }
}