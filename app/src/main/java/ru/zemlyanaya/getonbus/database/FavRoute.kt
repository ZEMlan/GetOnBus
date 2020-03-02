package ru.zemlyanaya.getonbus.database

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable


@Entity(tableName = "favRoutes")
class FavRoute(
    @PrimaryKey
    @NonNull
    val name: String,
    @ColumnInfo
    @NonNull
    val destination: String,
    @ColumnInfo
    val icon:String?
) : Serializable
