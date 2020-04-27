package ru.zemlyanaya.getonbus.mainactivity.trip

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import ru.zemlyanaya.getonbus.mainactivity.model.RemoteRepository
import kotlin.coroutines.CoroutineContext
import kotlin.random.Random

class TripViewModel : ViewModel() {

    private val parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Default
    private val scope = CoroutineScope(coroutineContext)

    private val repository = RemoteRepository()

    val possibleRoutes: MutableLiveData<List<String>?> = MutableLiveData()
    val currentInstruction: MutableLiveData<String> = MutableLiveData()

    fun cancelAllRequests() = coroutineContext.cancel()

    // Temp fun for data flow imitation
    fun nextInstruction() {
        if (Random.nextBoolean())
            possibleRoutes.value = listOf("1", "34", "057", "082")
        else
            currentInstruction.value = "Идите до места назначения пешком"
    }
}