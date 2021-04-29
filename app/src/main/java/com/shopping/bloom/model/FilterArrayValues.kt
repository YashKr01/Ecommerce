package com.shopping.bloom.model

import com.google.gson.annotations.SerializedName

data class FilterArrayValues(
        @SerializedName("colorData") var colors: List<String>,
        @SerializedName("sizeData") var sizes: List<String>,
        var types: List<String>? = null
)
