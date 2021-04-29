package com.shopping.bloom.model

data class ProductFilter(
        var subCategoryIds: String? = null,
        var priceHtoL: String? = null,
        var newArrival: String? = null,
        var mostPopular: String? = null,
        var colors: String? = null,
        var sizes: String? = null,
        var types: String? = null
)
