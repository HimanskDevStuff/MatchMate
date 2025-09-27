package saathi.core.service

import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback
import android.net.Network
import android.net.NetworkCapabilities
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.net.HttpURLConnection
import java.net.URL

interface InternetChecker {
    val isInternetAvailableFlow: StateFlow<Boolean>
    val isInternetAvailable: Boolean
    fun stopListenNetworkState()
}

class InternetCheckerImpl(
    context: Context,
) : InternetChecker {

    private val connectivityManager: ConnectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    private val isNetworkConnectedFlow: Flow<Boolean> = callbackFlow {
        val callback = object : NetworkCallback() {
            override fun onCapabilitiesChanged(
                network: Network,
                networkCapabilities: NetworkCapabilities
            ) {
                val connected = networkCapabilities.hasCapability(
                    NetworkCapabilities.NET_CAPABILITY_VALIDATED
                )
                trySend(connected)
            }

            override fun onUnavailable() {
                trySend(false).isSuccess
            }

            override fun onLost(network: Network) {
                trySend(false).isSuccess
            }

            override fun onAvailable(network: Network) {
                trySend(true).isSuccess
            }
        }

        connectivityManager.registerDefaultNetworkCallback(callback)

        awaitClose { connectivityManager.unregisterNetworkCallback(callback) }
    }

    private val _isInternetAvailableFlow = MutableStateFlow(true)
    override val isInternetAvailableFlow: StateFlow<Boolean> = _isInternetAvailableFlow

    override val isInternetAvailable: Boolean
        get() = _isInternetAvailableFlow.value

    init {
        scope.launch {
            isNetworkConnectedFlow.collectLatest { systemConnected ->
                if (!systemConnected) {
                    _isInternetAvailableFlow.value = false
                } else {
                    launch {
                        while (isActive && systemConnected) {
                            val reachable = checkInternetByPing()
                            _isInternetAvailableFlow.value = systemConnected && reachable
                            delay(5000)
                        }
                    }
                }
            }
        }
    }

    private fun checkInternetByPing(): Boolean {
        return try {
            val url = URL("https://clients3.google.com/generate_204")
            (url.openConnection() as HttpURLConnection).run {
                connectTimeout = 1500
                readTimeout = 1500
                requestMethod = "GET"
                connect()
                val success = responseCode == 204
                disconnect()
                success
            }
        } catch (e: Exception) {
            false
        }
    }

    override fun stopListenNetworkState() {
        scope.cancel()
    }
}
