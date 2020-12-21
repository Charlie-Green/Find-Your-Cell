package by.zenkevich_churun.findcell.core.injected.web

import android.Manifest
import android.content.Context
import android.net.*
import android.util.Log
import androidx.annotation.RequiresPermission
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class NetworkStateTracker @Inject constructor(
    @ApplicationContext private val appContext: Context ) {

    private var tracking = false
    private var available = true

    @get:RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    val isInternetAvailable: Boolean
        get() {
            if(!tracking) {
                tracking = true
                track()
            }
            return available
        }


    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    private fun track() {
        val netMan = appContext.getSystemService(ConnectivityManager::class.java)!!

        val request = NetworkRequest
            .Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()

        val callback = object: ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                Log.v("CharlieDebug", "Available")
                available = true
            }

            override fun onUnavailable() {
                Log.v("CharlieDebug", "Unavailable")
                available = false
            }
        }

        netMan.registerNetworkCallback(request, callback)
    }
}