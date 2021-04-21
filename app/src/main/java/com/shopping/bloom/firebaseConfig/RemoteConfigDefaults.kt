package com.shopping.bloom.firebaseConfig


class RemoteConfigDefaults {
    companion object {
        @JvmField
        val MAINSCREEN_CONFIG = HashMapPair("mainscreen_config", "{\"viewpager_image\":[{\"id\":1,\"path\":\"http://bloomapp.in/images/product/product_image_3.png\",\"order\":1},{\"id\":2,\"path\":\"http://bloomapp.in/images/product/product_image_3.png\",\"order\":2},{\"id\":3,\"path\":\"http://bloomapp.in/images/product/product_image_3.png\",\"order\":4},{\"id\":4,\"path\":\"http://bloomapp.in/images/product/product_image_3.png\",\"order\":3}],\"saleimagepath\":\"http://bloomapp.in/images/product/product_image_3.png\",\"offerImages\":[{\"id\":1,\"path\":\"http://bloomapp.in/images/product/product_image_3.png\",\"order\":1},{\"id\":2,\"path\":\"http://bloomapp.in/images/product/product_image_3.png\",\"order\":2},{\"id\":3,\"path\":\"http://bloomapp.in/images/product/product_image_3.png\",\"order\":3},{\"id\":4,\"path\":\"http://bloomapp.in/images/product/product_image_3.png\",\"order\":4}]}")
        val FRAGMENT_NEW_CONFIG = HashMapPair("newfragment_config", "{\"image1\":\"http://bloomapp.in/images/product/product_image_3.png\",\"image2\":\"http://bloomapp.in/images/product/product_image_3.png\"}")

        @JvmField
        val defaultMap = hashMapOf(
                MAINSCREEN_CONFIG.pair,
                FRAGMENT_NEW_CONFIG.pair
                )
    }
}