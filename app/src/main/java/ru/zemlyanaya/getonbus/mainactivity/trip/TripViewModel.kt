package ru.zemlyanaya.getonbus.mainactivity.trip

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlin.random.Random

class TripViewModel : ViewModel() {
    val possibleRoutes : MutableLiveData<List<String>?> = MutableLiveData()
    val currentInstruction : MutableLiveData<String> = MutableLiveData()

    init {
        possibleRoutes.value = listOf("27", "098", "022")
    }

    // Temp fun for data flow imitation
    fun nextInstruction(){
        if(Random.nextBoolean())
            possibleRoutes.value = listOf("1", "34", "057", "082")
        else
            currentInstruction.value = "Идите до места назначения пешком"
    }

    // TODO: Implement the ViewModel
}
