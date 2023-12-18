package com.example.cryptoapp

import android.app.Application
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.cryptoapp.data.CryptoUIModel
import com.example.cryptoapp.data.CurrencyInterface
import com.example.cryptoapp.data.RetrofitHelper
import com.example.cryptoapp.data.models.CurrencyInfo
import com.example.cryptoapp.data.models.ExchangeCurrencies
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CryptoViewModel(application: Application) : AndroidViewModel(application) {
    private val cryptos: MutableLiveData<List<CryptoUIModel?>> = MutableLiveData<List<CryptoUIModel?>>()
    val cryptosLive: LiveData<List<CryptoUIModel?>> = cryptos
    private val successStatus: MutableLiveData<Boolean> = MutableLiveData()
    val successStatusLive: LiveData<Boolean> = successStatus


    fun fetchData() {
        val currencyApi = RetrofitHelper.getInstance().create(CurrencyInterface::class.java)
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val exchangeCurrencies = currencyApi.getExchangeCurrencyDetails()
                val currencyInfo = currencyApi.getCurrencyInfoDetails()
                if (exchangeCurrencies != null || currencyInfo != null) {
                    val exchange = exchangeCurrencies.body()
                    val infos = currencyInfo.body()
                    if (exchange != null && infos != null) {
                        withContext(Dispatchers.Main) {
                            cryptos.value = mapCryptoData(exchange, infos)
                            successStatus.value = true
                        }
                    } else {
                        withContext(Dispatchers.Main) {
                            successStatus.value = false

                        }
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    successStatus.value = false

                }

            }

        }

    }

    private fun mapCryptoData(exchangeCurrencies: ExchangeCurrencies, infos: CurrencyInfo): List<CryptoUIModel> {
        return infos.crypto.map {
            CryptoUIModel(
                iconUrl = it.value.icon_url,
                nameFull = it.value.name_full,
                timeStamp = exchangeCurrencies.timestamp,
                exchangeRate = exchangeCurrencies.rates[it.key] ?: 0.0f,
            )
        }
    }


}