package ru.zemlyanaya.getonbus.mainactivity.trip

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
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

    fun nextInstruction() {
        scope.launch {
            val routs: MutableList<String> = ArrayList()
            val posts = repository.getPosts()
            var i = Random.nextInt(2, 6)
            while(i-- != 0){
                routs.add(posts?.get(Random.nextInt(0, 50))?.id.toString())
            }
            possibleRoutes.postValue(routs as List<String>)
        }
    }

    fun nextInstruction(number: Int){
        scope.launch {
            val posts = repository.getPosts()
            val max = posts!!.size
            currentInstruction.postValue(posts[Random.nextInt(0, max)].title)
        }
    }
}