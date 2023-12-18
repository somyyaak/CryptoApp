package com.example.cryptoapp.data.models

data class ExchangeCurrencies(
    val success: Boolean,
    val terms: String,
    val privacy: String,
    val timestamp: Long,
    val target: String,
    val rates: HashMap<String, Float>,
)
