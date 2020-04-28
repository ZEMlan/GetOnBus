package ru.zemlyanaya.getonbus.mainactivity.routing


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import ru.zemlyanaya.getonbus.Resource
import ru.zemlyanaya.getonbus.mainactivity.data.repository.DeviceRepository
import ru.zemlyanaya.getonbus.mainactivity.data.repository.RemoteRepository
import ru.zemlyanaya.getonbus.mainactivity.database.FavRoute
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket
import kotlin.coroutines.CoroutineContext

class RoutingViewModel(
    private val remoteRepository: RemoteRepository,
    private val deviceRepository: DeviceRepository
    ) : ViewModel() {

    val connection : MutableLiveData<Boolean> = MutableLiveData(false)

    private val parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Default
    private val scope = CoroutineScope(coroutineContext)

    val favRoutes = deviceRepository.allFavRoutes

    fun insert(route: FavRoute) = scope.launch {
        deviceRepository.insert(route)
    }

    fun delete(route: FavRoute) = scope.launch {
        deviceRepository.delete(route)
    }

    fun edit(oldRoute: FavRoute, newRoute: FavRoute) = scope.launch {
        deviceRepository.edit(oldRoute, newRoute)
    }

    fun hasInternetConnection() {
        scope.launch(Dispatchers.IO) {
            try {
                Thread.sleep(500L)
                // Connect to Google DNS to check for connection
                val timeoutMs = 1500
                val socket = Socket()
                val socketAddress = InetSocketAddress("8.8.8.8", 53)

                socket.connect(socketAddress, timeoutMs)
                socket.close()

                connection.postValue(true)
            } catch (e: IOException) {
                connection.postValue(false)
            }
        }
    }


    fun getStops() = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = remoteRepository.getAllStops()))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }


}