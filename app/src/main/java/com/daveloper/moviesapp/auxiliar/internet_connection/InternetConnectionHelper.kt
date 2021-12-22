package com.daveloper.moviesapp.auxiliar.internet_connection

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import javax.inject.Inject

class InternetConnectionHelper @Inject constructor(
    private val context: Context
){
    fun internetIsConnected (
    ) : Boolean {
        val connectivityManager: ConnectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val activeNetwork: Network? = connectivityManager.activeNetwork
        val networkCapabilities: NetworkCapabilities? =
            connectivityManager.getNetworkCapabilities(activeNetwork)

        if (networkCapabilities != null) {
            return when {
                // WIFI Network
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                // MOBILE DATA Network
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                else -> false
            }
        } else {
            return false
        }
    }
}