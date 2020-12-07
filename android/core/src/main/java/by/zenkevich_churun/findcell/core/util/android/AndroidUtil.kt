package by.zenkevich_churun.findcell.core.util.android

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.*
import android.os.Build
import android.util.DisplayMetrics
import android.util.Size
import android.util.TypedValue
import android.view.WindowInsets
import androidx.annotation.RequiresPermission


object AndroidUtil {
    private val typval by lazy { TypedValue() }


    fun activitySize(activity: Activity): Size {
        if(Build.VERSION.SDK_INT < 30) {
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


    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
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


    fun themeColor(appContext: Context, attrRes: Int): Int {
        appContext.theme.resolveAttribute(attrRes, typval, true)
        return typval.data
    }


    fun mergeStrings(
        appContext: Context,
        vararg stringRess: Int
    ): String {

        val sb = StringBuilder(64*stringRess.size)

        for(stringRes in stringRess) {
            val string = appContext.getString(stringRes)

            if(!sb.isEmpty()) {
                sb.append(" ")
            }
            sb.append(string)
        }

        return sb.toString()
    }


    fun removeDialogBackground(target: Dialog) {
        val transparentDrawable = ColorDrawable(Color.TRANSPARENT)
        target.window?.setBackgroundDrawable(transparentDrawable)
    }
}