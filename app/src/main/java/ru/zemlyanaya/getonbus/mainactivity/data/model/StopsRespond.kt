package ru.zemlyanaya.getonbus.mainactivity.data.model

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import java.util.*


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("Stops")
class StopsRespond {
    @JsonProperty("Stops")
    var stops: List<Stop>? = null

    fun getStopsList(): MutableList<String?> {
        val res: MutableList<String?> = ArrayList()
        stops?.forEach { stop -> res.add(stop.name) }
        return res
    }

    constructor(data: List<Stop>){
        this.stops = data
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonPropertyOrder("ID", "Name", "Transport", "Routes", "Siblings")
    data class Stop(
        @JsonProperty("ID")
        var id: Int? = null,

        @JsonProperty("Name")
        var name: String? = null,

        @JsonProperty("Transport")
        var transport: List<String>? = null,

        @JsonProperty("Routes")
        var routes: List<String>? = null,

        @JsonProperty("Siblings")
        var siblings: List<Int>? = null)
}
