package ru.zemlyanaya.getonbus.mainactivity.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.zemlyanaya.getonbus.mainactivity.database.FavRoute
import ru.zemlyanaya.getonbus.mainactivity.database.IFavRouteDao


class DeviceRepository(private val favRouteDao: IFavRouteDao) {

    // Room executes all queries on a separate thread.
    val allFavRoutes: LiveData<List<FavRoute>?> = favRouteDao.getAll()

    lateinit var stops: MutableLiveData<Stops>

    fun insert(route: FavRoute){
        favRouteDao.insert(route)
    }

    fun delete(route: FavRoute){
        favRouteDao.delete(route)
    }

    fun edit(oldRoute: FavRoute, newRoute: FavRoute) {
        favRouteDao.delete(oldRoute)
        favRouteDao.insert(newRoute)
    }
}