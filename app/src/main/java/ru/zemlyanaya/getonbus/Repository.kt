package ru.zemlyanaya.getonbus

import androidx.lifecycle.LiveData
import ru.zemlyanaya.getonbus.database.FavRoute
import ru.zemlyanaya.getonbus.database.IFavRouteDao


class Repository(private val favRouteDao: IFavRouteDao) {

    // Room executes all queries on a separate thread.
    val allFavRoutes: LiveData<List<FavRoute>?> = favRouteDao.getAll()

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