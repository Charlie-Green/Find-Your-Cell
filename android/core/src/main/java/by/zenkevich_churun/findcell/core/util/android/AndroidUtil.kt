package by.zenkevich_churun.findcell.core.util.android

import android.app.Activity
import android.content.Context
import android.net.*
import android.os.Build
import android.util.DisplayMetrics
import android.util.Size
import android.view.WindowInsets


object AndroidUtil {
    fun activitySize(activity: Activity): Size {
        if(Build.VERSION.SDK_INT < 29) {
            val metrics = DisplayMetrics()
            activity.windowManager.defaultDisplay.getMetrics(metrics)
            return Size(metrics.widthPixels, metrics.heightPixels)
        }

        val metrics = activity.windowManager.currentWindowMetrics
        val insets = metrics.windowInsets.getInsetsIgnoringVisibility(
            WindowInsets.Type.navigationBars() or WindowInsets.Type.displayCutout() )
        val iw = insets.left + insets.right
        val ih = insets.top + insets.bottom

        val bounds = metrics.bounds
        return Size(bounds.width() - iw, bounds.height() - ih)
    }


    fun whenInternetAvailable(
        appContext: Context,
        action: (ConnectivityManager, ConnectivityManager.NetworkCallback) -> Unit ) {

        val connectMan = appContext.getSystemService(ConnectivityManager::class.java)!!

        val request = NetworkRequest
            .Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()

        val callback = object: ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                action(connectMan, this)
            }
        }

        connectMan.registerNetworkCallback(request, callback)
    }
}