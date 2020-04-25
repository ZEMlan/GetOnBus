package ru.zemlyanaya.getonbus.mainactivity.database

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

    @Query("UPDATE favRoutes SET name=:newName, destination=:newDestination, icon=:newIcon WHERE `key`=:oldKey")
    fun edit(oldKey: Int, newName: String, newDestination: String, newIcon: String?)
}