package ru.zemlyanaya.getonbus.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface IFavRouteDao {
    @Query("SELECT * FROM favRoutes")
    fun getAll(): LiveData<List<FavRoute>?>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(route: FavRoute)

    @Delete
    fun delete(route: FavRoute)
}