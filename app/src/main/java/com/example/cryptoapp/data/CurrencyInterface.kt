package com.example.cryptoapp.data

import com.example.cryptoapp.data.models.CurrencyInfo
import com.example.cryptoapp.data.models.ExchangeCurrencies
import retrofit2.Response
import retrofit2.http.GET

interface CurrencyInterface {

    @GET("live?access_key=48b5ffa26ff89c2b4477df1aebce02bf")
    suspend fun getExchangeCurrencyDetails(): Response<ExchangeCurrencies>

    @GET("list?access_key=48b5ffa26ff89c2b4477df1aebce02bf")
    suspend fun getCurrencyInfoDetails(): Response<CurrencyInfo>
}