package ru.zemlyanaya.getonbus.mainactivity.trip

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import ru.zemlyanaya.getonbus.Resource
import ru.zemlyanaya.getonbus.mainactivity.data.repository.RemoteRepository
import kotlin.coroutines.CoroutineContext

class TripViewModel(private val remoteRepository: RemoteRepository) : ViewModel() {


    private val parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Default
    private val scope = CoroutineScope(coroutineContext)

    val possibleRoutes = MediatorLiveData<Resource<List<String>>>()
    val currentInstruction = MediatorLiveData<Resource<String>>()

    fun cancelAllRequests() {
        coroutineContext.cancel()
    }

    fun getPossibleRoutes() {
        possibleRoutes.value = Resource.loading(data = null)

        scope.launch(Dispatchers.IO) {
            Thread.sleep(2000L)
            try {
                possibleRoutes.postValue(
                    Resource.success(data = remoteRepository.getNextInstruction()))
            } catch (exception: Exception) {
                possibleRoutes.postValue(
                    Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
            }
        }
    }
    fun getInstruction(number: Int) {
        currentInstruction.value = Resource.loading(data = null)

        scope.launch(Dispatchers.IO) {
            Thread.sleep(2000L)
            try {
                currentInstruction.postValue(
                    Resource.success(data = remoteRepository.getNextInstruction(number)))
            } catch (exception: Exception) {
                currentInstruction.postValue(
                    Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
            }
        }
    }

}