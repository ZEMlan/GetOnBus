package ru.zemlyanaya.getonbus.mainactivity.data.repository

import androidx.lifecycle.LiveData
import ru.zemlyanaya.getonbus.mainactivity.database.FavRoute
import ru.zemlyanaya.getonbus.mainactivity.database.IFavRouteDao


class DeviceRepository(private val favRouteDao: IFavRouteDao) {

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

    companion object {

        // For Singleton instantiation
        @Volatile private var instance: DeviceRepository? = null

        fun getInstance(iFavRouteDao: IFavRouteDao) =
            instance
                ?: synchronized(this) {
                instance
                    ?: DeviceRepository(
                        iFavRouteDao
                    )
                        .also { instance = it }
            }
    }
}