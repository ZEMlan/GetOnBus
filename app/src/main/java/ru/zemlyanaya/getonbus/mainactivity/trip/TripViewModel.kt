package ru.zemlyanaya.getonbus.mainactivity.trip

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import ru.zemlyanaya.getonbus.RequestStatus
import ru.zemlyanaya.getonbus.mainactivity.model.RemoteRepository
import kotlin.coroutines.CoroutineContext
import kotlin.random.Random

class TripViewModel : ViewModel() {

    val requestStatus = MutableLiveData<RequestStatus>()

    private val parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Default
    private val scope = CoroutineScope(coroutineContext)

    private val repository = RemoteRepository()

    val possibleRoutes = MutableLiveData<List<String>?>()
    val currentInstruction = MutableLiveData<String>()

    fun cancelAllRequests() = coroutineContext.cancel()

    fun getRoute(a: String, b: String){
        //TODO send request to the server
        nextInstruction()
    }

    fun nextInstruction() {
        requestStatus.value = RequestStatus.LOADING
        scope.launch {
            Thread.sleep(2000L)
            val routs: MutableList<String> = ArrayList()
            val posts = repository.getPosts()
            if(posts == null)
                requestStatus.postValue(RequestStatus.ERROR)
            else {
                var i = Random.nextInt(2, 6)
                while(i-- != 0){
                    routs.add(posts[Random.nextInt(0, 50)].id.toString())
                }
                requestStatus.postValue(RequestStatus.SUCCESS)
                possibleRoutes.postValue(routs as List<String>)
            }
        }
    }

    fun nextInstruction(number: Int){
        requestStatus.value = RequestStatus.LOADING
        scope.launch {
            //TODO send request to the server
            Thread.sleep(2000L)
            val posts = repository.getPosts()
            if (posts == null)
                requestStatus.postValue(RequestStatus.ERROR)
            else {
                val max = posts.size
                requestStatus.postValue(RequestStatus.SUCCESS)
                currentInstruction.postValue(posts[Random.nextInt(0, max)].title)
            }
        }
    }
}