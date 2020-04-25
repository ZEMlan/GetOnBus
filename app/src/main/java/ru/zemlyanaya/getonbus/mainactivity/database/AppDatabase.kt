package ru.zemlyanaya.getonbus.mainactivity.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [FavRoute::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun favRouteDao(): IFavRouteDao

    companion object{
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "getOnBusApp_database"
                )
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}