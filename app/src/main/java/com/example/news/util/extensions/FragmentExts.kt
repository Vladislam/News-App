package com.example.news.util.extensions

import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar

inline fun Fragment.showSnackBarWithAction(
    title: Int,
    actionTitle: Int,
    crossinline action: Snackbar.() -> Unit
) =
    Snackbar.make(requireView(), getString(title), Snackbar.LENGTH_LONG).apply {
        animationMode = Snackbar.ANIMATION_MODE_FADE
        setAction(getString(actionTitle)) {
            action()
        }
    }.show()
