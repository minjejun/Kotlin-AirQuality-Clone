package com.jejunMin.airquality.Retrofit

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface AirQualityService {
    @GET("nearest_city") // 이름 틀리면 안됨.
    fun getAirQualityData(@Query("lat") lat: String, @Query("lon") lon: String, @Query("key") key: String) : Call<AirQualityResponse>


}