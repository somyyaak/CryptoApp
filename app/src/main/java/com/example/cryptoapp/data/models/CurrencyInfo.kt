package com.example.cryptoapp.data.models

import com.example.cryptoapp.data.models.InfoItem

data class CurrencyInfo(
    val crypto: HashMap<String, InfoItem>,
    val fiat: HashMap<String, String>,
    val success: Boolean
)