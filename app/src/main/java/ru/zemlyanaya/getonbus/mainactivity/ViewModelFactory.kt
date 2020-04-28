package ru.zemlyanaya.getonbus.mainactivity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.zemlyanaya.getonbus.mainactivity.data.api.IApiService
import ru.zemlyanaya.getonbus.mainactivity.data.repository.DeviceRepository
import ru.zemlyanaya.getonbus.mainactivity.data.repository.RemoteRepository
import ru.zemlyanaya.getonbus.mainactivity.database.IFavRouteDao
import ru.zemlyanaya.getonbus.mainactivity.routing.RoutingViewModel
import ru.zemlyanaya.getonbus.mainactivity.trip.TripViewModel

class ViewModelFactory(
    private val apiService: IApiService?,
    private val favDao: IFavRouteDao?
    ) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if(favDao == null)
            TripViewModel(RemoteRepository(apiService!!)) as T
        else
            RoutingViewModel(
                RemoteRepository(apiService!!),
                DeviceRepository.getInstance(favDao)
            ) as T
    }

}

