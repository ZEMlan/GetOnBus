package ru.zemlyanaya.getonbus.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable


@Entity(tableName = "favRoutes")
class FavRoute(
    @PrimaryKey
    var key: Int,
    @ColumnInfo
    val name: String,
    @ColumnInfo
    val destination: String,
    @ColumnInfo
    val icon:String?
) : Serializable {
    init { key = (name+destination).hashCode() }
}
