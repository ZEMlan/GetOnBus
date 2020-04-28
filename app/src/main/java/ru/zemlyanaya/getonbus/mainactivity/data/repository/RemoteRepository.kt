package ru.zemlyanaya.getonbus.mainactivity.data.repository

import ru.zemlyanaya.getonbus.mainactivity.data.api.IApiService
import ru.zemlyanaya.getonbus.mainactivity.data.model.StopsRespond
import kotlin.random.Random

class RemoteRepository (private val apiService: IApiService) {

    private val stops by lazy{
        listOf(
            StopsRespond.Stop(null, "Уральский Федеральный Университет", null, null, null),
            StopsRespond.Stop(null, "Восточная", null, null, null),
            StopsRespond.Stop(null, "Бажова", null, null, null),
            StopsRespond.Stop(null, "Оперный Театр", null, null, null),
            StopsRespond.Stop(null, "Театр Музыкальной Комедии", null, null, null),
            StopsRespond.Stop(null, "Площадь 1905 года", null, null, null),
            StopsRespond.Stop(null, "Сакко и Ванцетти", null, null, null),
            StopsRespond.Stop(null, "Дворец Молодёжи", null, null, null),
            StopsRespond.Stop(null, "Высоцкого", null, null, null),
            StopsRespond.Stop(null, "Сулимова", null, null, null),
            StopsRespond.Stop(null, "Авангард", null, null, null),
            StopsRespond.Stop(null, "Центральный Парк Культуры и Отдыха", null, null, null),
            StopsRespond.Stop(null, "Комсомольская", null, null, null),
            StopsRespond.Stop(null, "Гостинница Исеть", null, null, null),
            StopsRespond.Stop(null, "Дом Кино", null, null, null)
        )
    }
    private val stopsRespond = StopsRespond(stops)

    private val buses = listOf(
        Random.nextInt(1, 27).toString(),
        Random.nextInt(27, 100).toString(),
        Random.nextInt(19, 34).toString(),
        Random.nextInt(78, 98).toString(),
        Random.nextInt(1, 34).toString()
    )

    //suspend fun getAllStops() = apiService.getAllStopsAsync()
    fun getAllStops() = stopsRespond

    suspend fun getNames(names: List<Int>) = apiService.getNamesByID(names)

    suspend fun getRoute(from: Int, to: Int, transp: List<String>, mode: String)
            = apiService.getRoute(from, to, transp, mode)

    //suspend fun getNextInstruction(number: Int) = apiService.getNextInstruction(number)
    suspend fun getNextInstruction(number: Int) = "Идите пешком до остановки MAGIC_CONST"

    //suspend fun getNextInstruction() = apiService.getNextInstruction()
    suspend fun getNextInstruction() = buses
}