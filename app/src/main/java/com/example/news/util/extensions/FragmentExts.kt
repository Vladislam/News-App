package com.example.news.util.extensions

import androidx.fragment.app.Fragment
import com.example.news.R
import com.google.android.material.snackbar.Snackbar

fun Fragment.showSnackBarWithDismiss(string: Int) =
    Snackbar.make(requireView(), getString(string), Snackbar.LENGTH_LONG).apply {
        setAction(getString(R.string.dismiss)) {
            animationMode = Snackbar.ANIMATION_MODE_FADE
            setAction(getString(R.string.dismiss)) {
                dismiss()
            }
        }
    }.show()