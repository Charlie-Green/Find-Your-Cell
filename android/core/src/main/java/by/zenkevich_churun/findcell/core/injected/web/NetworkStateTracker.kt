package by.zenkevich_churun.findcell.core.injected.web

import android.Manifest
import android.annotation.SuppressLint
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

    private var available = false


    init {
        startTrack()
    }


    @get:RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    val isInternetAvailable: Boolean
        get() = available


    @SuppressLint("MissingPermission")
    private fun startTrack() {
        val netMan = appContext.getSystemService(ConnectivityManager::class.java)!!

        val request = NetworkRequest
            .Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()

        val callback = object: ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                available = true
            }

            override fun onLost(network: Network) {
                available = false
            }

            override fun onUnavailable() {
                available = false
            }
        }

        netMan.registerNetworkCallback(request, callback)
    }
}