package ru.zemlyanaya.getonbus.mainactivity.model

import com.fasterxml.jackson.annotation.*


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("Stops")
class StopsRespond {
    @JsonProperty("Stops")
    var stops: List<Stop>? = null

    @JsonIgnore
    private val additionalProperties: MutableMap<String, Any> =
        HashMap()

    @JsonAnyGetter
    fun getAdditionalProperties(): Map<String, Any> {
        return additionalProperties
    }

    @JsonAnySetter
    fun setAdditionalProperty(name: String, value: Any) {
        additionalProperties[name] = value
    }
}

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("ID", "Name", "Transport", "Routes", "Siblings")
class Stop {
    @JsonProperty("ID")
    var iD: Int? = null

    @JsonProperty("Name")
    var name: String? = null

    @JsonProperty("Transport")
    var transport: List<String>? = null

    @JsonProperty("Routes")
    var routes: List<String>? = null

    @JsonProperty("Siblings")
    var siblings: List<Int>? = null

    @JsonIgnore
    private val additionalProperties: MutableMap<String, Any> =
        HashMap()

    @JsonAnyGetter
    fun getAdditionalProperties(): Map<String, Any> {
        return additionalProperties
    }

    @JsonAnySetter
    fun setAdditionalProperty(name: String, value: Any) {
        additionalProperties[name] = value
    }
}