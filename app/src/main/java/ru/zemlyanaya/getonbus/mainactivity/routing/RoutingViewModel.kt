package ru.zemlyanaya.getonbus.mainactivity.routing

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.*
import ru.zemlyanaya.getonbus.mainactivity.database.AppDatabase
import ru.zemlyanaya.getonbus.mainactivity.database.FavRoute
import ru.zemlyanaya.getonbus.mainactivity.model.DeviceRepository
import ru.zemlyanaya.getonbus.mainactivity.model.PlaceholderPosts
import ru.zemlyanaya.getonbus.mainactivity.model.RemoteRepository
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket
import kotlin.coroutines.CoroutineContext

class RoutingViewModel(app: Application) : AndroidViewModel(app) {

    private var connection = false

    private val parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Default
    private val scope = CoroutineScope(coroutineContext)

    private val remoteRepository = RemoteRepository()

    //val stopsLiveData = MutableLiveData<MutableList<String?>>()
    val postLiveData = MutableLiveData<List<PlaceholderPosts>?>()

    private val deviceRepository: DeviceRepository
    val favRoutes: LiveData<List<FavRoute>?>

    init {
        val favRouteDao = AppDatabase.getDatabase(app).favRouteDao()
        deviceRepository =
            DeviceRepository(
                favRouteDao
            )
        favRoutes = deviceRepository.allFavRoutes
        fetchPosts()
    }

    fun insert(route: FavRoute) = GlobalScope.launch {
        deviceRepository.insert(route)
    }

    fun delete(route: FavRoute) = GlobalScope.launch {
        deviceRepository.delete(route)
    }

    fun edit(oldRoute: FavRoute, newRoute: FavRoute) = GlobalScope.launch {
        deviceRepository.edit(oldRoute, newRoute)
    }

    fun hasInternetConnection(): Boolean {
        scope.launch(Dispatchers.IO) {
            connection = try {
                // Connect to Google DNS to check for connection
                val timeoutMs = 1500
                val socket = Socket()
                val socketAddress = InetSocketAddress("8.8.8.8", 53)

                socket.connect(socketAddress, timeoutMs)
                socket.close()

                true
            } catch (e: IOException) {
                false
            }
        }
        return connection
    }

//    fun fetchStops(){
//        scope.launch {
//            val stops = remoteRepository.getAllStops()
//            stopsLiveData.postValue(stops)
//        }
//    }

    fun fetchPosts(){
        scope.launch {
            val posts = remoteRepository.getPosts()
            postLiveData.postValue(posts)
        }
    }

}