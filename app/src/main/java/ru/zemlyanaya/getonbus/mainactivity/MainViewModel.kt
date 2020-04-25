package ru.zemlyanaya.getonbus.mainactivity

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import ru.zemlyanaya.getonbus.mainactivity.database.AppDatabase
import ru.zemlyanaya.getonbus.mainactivity.database.FavRoute
import kotlin.random.Random

class MainViewModel(app: Application) : AndroidViewModel(app) {

    //remote storage
    val possibleRoutes : MutableLiveData<List<String>?> = MutableLiveData()
    val currentInstruction : MutableLiveData<String> = MutableLiveData()

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
    fun nextInstruction(){
        if(Random.nextBoolean())
            possibleRoutes.value = listOf("1", "34", "057", "082")
        else
            currentInstruction.value = "Идите до места назначения пешком"
    }

}