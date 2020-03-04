package ru.zemlyanaya.getonbus.routing

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import ru.zemlyanaya.getonbus.Repository
import ru.zemlyanaya.getonbus.database.AppDatabase
import ru.zemlyanaya.getonbus.database.FavRoute

class RoutingViewModel(app: Application) : AndroidViewModel(app) {

    private var isComputing : MutableLiveData<Boolean> = MutableLiveData()
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


    fun compute(a: String, b: String){
        isComputing.postValue(true)
        Thread.sleep(3000)
        isComputing.postValue(false)
    }

    fun getComputingState() : MutableLiveData<Boolean> = isComputing

}