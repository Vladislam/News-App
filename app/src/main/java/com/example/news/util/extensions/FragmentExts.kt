package com.example.news.util.extensions

import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.news.ui.activities.NewsActivity
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

inline fun Fragment.showSnackBarWithAction(
    title: String,
    actionTitle: String,
    crossinline action: Snackbar.() -> Unit
) =
    Snackbar.make(requireView(), title, Snackbar.LENGTH_LONG).apply {
        animationMode = Snackbar.ANIMATION_MODE_FADE
        setAction(actionTitle) {
            action()
        }
    }.show()

fun Fragment.slideUpBottomNavigationBar() =
    (requireActivity() as NewsActivity).slideUpBottomNavigationBar()

fun Fragment.slideDownBottomNavigationBar() =
    (requireActivity() as NewsActivity).slideDownBottomNavigationBar()

fun Fragment.launchViewLifecycleScope(action: suspend CoroutineScope.() -> Unit) =
    viewLifecycleOwner.lifecycleScope.launch { action.invoke(this) }