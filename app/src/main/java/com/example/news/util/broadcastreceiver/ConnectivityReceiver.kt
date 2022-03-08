package com.example.news.util.broadcastreceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import com.example.news.util.ConnectionHelper
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class ConnectivityReceiver @Inject constructor(
    private val connectionHelper: ConnectionHelper
) : BroadcastReceiver() {

    private var callback: (() -> Unit)? = null
    private var isRegistered = false

    fun register(context: Context, filter: IntentFilter?): Intent? {
        return try {
            if (!isRegistered) context.registerReceiver(this, filter) else null
        } finally {
            isRegistered = true
        }
    }

    fun unregister(context: Context): Boolean {
        return (isRegistered
                && unregisterInternal(context))
    }

    private fun unregisterInternal(context: Context): Boolean {
        context.unregisterReceiver(this)
        isRegistered = false
        return true
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        if (connectionHelper.hasInternetConnection()) {
            callback?.invoke()
        }
    }

    fun registerCallback(callback: () -> Unit) {
        this.callback = callback
    }
}