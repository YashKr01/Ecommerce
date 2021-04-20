package com.shopping.bloom.utils

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.shopping.bloom.R
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class CommonUtils {
    companion object {

        @JvmStatic
        fun isNotNull(string: String?): Boolean {
            return !(string == null || string.isEmpty())
        }

        @JvmStatic
        fun isNull(string: String?): Boolean {
            return (string == null || string.isEmpty())
        }

        @JvmStatic
        fun makeArrayList(jsonArray: JSONArray?): ArrayList<JSONObject> {
            val listdata = ArrayList<JSONObject>()
            val jArray = jsonArray as JSONArray
            for (i in 0 until jArray.length()) {
                listdata.add(jArray.getJSONObject(i))
            }
            return listdata
        }

        @JvmStatic
        fun loadImageWithGlide(context: Context, url: String?, imageView: ImageView, showPlaceHolder: Boolean) {
            val builder = Glide.with(context)
            if (showPlaceHolder) {
                builder.load(url)
                        .placeholder(R.drawable.ic_placeholder_product)
                        .thumbnail(0.3f)
                        .into(imageView)
            } else {
                builder.load(url)
                        .thumbnail(0.3f)
                        .into(imageView)
            }
        }

        @JvmStatic
        fun getStringFromDate(long: Long, outputFromat: String): String? {
            val date = Date(long)
            val df2 = SimpleDateFormat(outputFromat, Locale.ENGLISH)
            return df2.format(date)
        }



        @JvmStatic
        fun getInitialText(word: String): Char {
            return word.get(0)
        }
    }

    interface ACTION {
        companion object {
            const val MAIN_ACTION = "com.tota.app.action.main"
            const val STARTFOREGROUND_ACTION =
                    "com.tota.app.action.startforeground"
            const val STOPFOREGROUND_ACTION =
                    "com.tota.app.action.stopforeground"
        }
    }



}