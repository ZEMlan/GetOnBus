package ru.zemlyanaya.getonbus.mainactivity

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import ru.zemlyanaya.getonbus.mainactivity.database.AppDatabase
import ru.zemlyanaya.getonbus.mainactivity.database.FavRoute
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket
import kotlin.random.Random

class MainViewModel(app: Application) : AndroidViewModel(app) {

    private var connection = false

    //remote storage
    val possibleRoutes: MutableLiveData<List<String>?> = MutableLiveData()
    val currentInstruction: MutableLiveData<String> = MutableLiveData()

    //device storage
    private val repository: Repository
    val favRoutes: LiveData<List<FavRoute>?>

    init {
        val favRouteDao = AppDatabase.getDatabase(app).favRouteDao()
        repository = Repository(favRouteDao)
        favRoutes = repository.allFavRoutes
    }

    fun insert(route: FavRoute) = GlobalScope.launch {
        repository.insert(route)
    }

    fun delete(route: FavRoute) = GlobalScope.launch {
        repository.delete(route)
    }

    fun edit(oldRoute: FavRoute, newRoute: FavRoute) = GlobalScope.launch {
        repository.edit(oldRoute, newRoute)
    }

    // Temp fun for data flow imitation
    fun nextInstruction() {
        if (Random.nextBoolean())
            possibleRoutes.value = listOf("1", "34", "057", "082")
        else
            currentInstruction.value = "Идите до места назначения пешком"
    }

    fun hasInternetConnection(): Boolean {
        GlobalScope.launch(Dispatchers.IO) {
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
}