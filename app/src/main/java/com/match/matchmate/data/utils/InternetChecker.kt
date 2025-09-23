package saathi.core.service

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

interface InternetChecker {
    /**
     * Emits [Boolean] value when the current network becomes available or unavailable.
     */
    val isNetworkConnectedFlow: StateFlow<Boolean>

    val isInternetAvailable: Boolean

    fun stopListenNetworkState()
}

class InternetCheckerImpl(
    context: Context,
) : InternetChecker {

    private val connectivityManager: ConnectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private val networkCallback = NetworkCallback()

    private val _currentNetwork = MutableStateFlow(provideDefaultCurrentNetwork())

    private val internetCheckerScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    init {
        startListenNetworkState()
    }

    override val isNetworkConnectedFlow: StateFlow<Boolean> =
        _currentNetwork
            .map { it.isConnected() }
            .stateIn(
                scope = internetCheckerScope,
                started = SharingStarted.Eagerly, // Changed to Eagerly
                initialValue = _currentNetwork.value.isConnected()
            )

    override val isInternetAvailable: Boolean
        get() = _currentNetwork.value.isConnected() // Direct access instead of flow

    fun startListenNetworkState() {
        if (_currentNetwork.value.isListening) {
            return
        }

        Log.d("internet?:::", "startListenNetworkState")

        // Get current network state before registering callback
        val activeNetwork = connectivityManager.activeNetwork
        val networkCapabilities = activeNetwork?.let {
            connectivityManager.getNetworkCapabilities(it)
        }

        // Initialize with current network state
        _currentNetwork.update {
            CurrentNetwork(
                isListening = true,
                networkCapabilities = networkCapabilities,
                isAvailable = activeNetwork != null,
                isBlocked = false // We assume unblocked initially
            )
        }

        connectivityManager.registerDefaultNetworkCallback(networkCallback)

        // Log initial state
        Log.d("internet?:::", "Initial network state: ${_currentNetwork.value}")
        Log.d("internet?:::", "isConnected: ${_currentNetwork.value.isConnected()}")
    }

    override fun stopListenNetworkState() {
        if (!_currentNetwork.value.isListening) {
            return
        }

        _currentNetwork.update {
            it.copy(isListening = false)
        }

        connectivityManager.unregisterNetworkCallback(networkCallback)
    }

    private inner class NetworkCallback : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            _currentNetwork.update {
                it.copy(isAvailable = true)
            }

            Log.d(
                "internet?:::",
                "After onAvailable - isConnected: ${_currentNetwork.value.isConnected()}"
            )
        }

        override fun onLost(network: Network) {

            _currentNetwork.update {
                it.copy(
                    isAvailable = false,
                    networkCapabilities = null
                )
            }

            Log.d(
                "internet?:::",
                "After onLost - isConnected: ${_currentNetwork.value.isConnected()}"
            )
        }

        override fun onUnavailable() {

            _currentNetwork.update {
                it.copy(
                    isAvailable = false,
                    networkCapabilities = null
                )
            }

            Log.d(
                "internet?:::",
                "After onUnavailable - isConnected: ${_currentNetwork.value.isConnected()}"
            )
        }

        override fun onCapabilitiesChanged(
            network: Network,
            networkCapabilities: NetworkCapabilities
        ) {

            _currentNetwork.update {
                it.copy(networkCapabilities = networkCapabilities)
            }

            Log.d(
                "internet?:::",
                "After onCapabilitiesChanged - isConnected: ${_currentNetwork.value.isConnected()}"
            )
        }

        override fun onBlockedStatusChanged(network: Network, blocked: Boolean) {

            _currentNetwork.update {
                it.copy(isBlocked = blocked)
            }

            Log.d(
                "internet?:::",
                "After onBlockedStatusChanged - isConnected: ${_currentNetwork.value.isConnected()}"
            )
        }
    }

    /**
     * On Android 9, [ConnectivityManager.NetworkCallback.onBlockedStatusChanged] is not called when
     * we call the [ConnectivityManager.registerDefaultNetworkCallback] function.
     * Hence we assume that the network is unblocked by default.
     */
    private fun provideDefaultCurrentNetwork(): CurrentNetwork {
        return CurrentNetwork(
            isListening = false,
            networkCapabilities = null,
            isAvailable = false,
            isBlocked = false
        )
    }

    private data class CurrentNetwork(
        val isListening: Boolean,
        val networkCapabilities: NetworkCapabilities?,
        val isAvailable: Boolean,
        val isBlocked: Boolean
    )

    private fun CurrentNetwork.isConnected(): Boolean {
        // Since we don't know the network state if NetworkCallback is not registered.
        // We assume that it's disconnected.
        val capabilitiesValid = networkCapabilities.isNetworkCapabilitiesValid()

        Log.d(
            "internet?:::",
            "isConnected: isListening=$isListening isAvailable=$isAvailable isBlocked=$isBlocked networkCapabilities=$capabilitiesValid"
        )

        return isListening &&
                isAvailable &&
                !isBlocked &&
                capabilitiesValid
    }

    private fun NetworkCapabilities?.isNetworkCapabilitiesValid(): Boolean = when {
        this == null -> false
        hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED) &&
                (hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                        hasTransport(NetworkCapabilities.TRANSPORT_VPN) ||
                        hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                        hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) -> true

        else -> false
    }
}