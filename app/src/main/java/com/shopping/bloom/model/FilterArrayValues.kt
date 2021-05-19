package com.shopping.bloom.model

import com.google.gson.annotations.SerializedName

data class FilterArrayValues(
        @SerializedName("maxPrice") val maxPrice: String = "",
        @SerializedName("minPrice") val minPrice: String = "",
        @SerializedName("maxSalePercentage") val maxSalePercentage: String = "",
        @SerializedName("minSalePercentage") val minSalePercentage: String = "",
        @SerializedName("colorData") var colors: List<String>,
        @SerializedName("sizeData") var sizes: List<String>,
        var types: List<String>? = null
)
