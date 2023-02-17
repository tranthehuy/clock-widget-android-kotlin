package com.mehannah.clock

import android.content.Context
import android.widget.ArrayAdapter
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import java.io.IOException
import java.io.InputStream
import java.nio.charset.Charset

data class Items (
    @SerializedName("name"  ) var name  : String? = null,
    @SerializedName("value" ) var value : String? = null
)

data class Options (
    @SerializedName("items" ) var items : ArrayList<Items> = arrayListOf()
)

class Utils {

    companion object {
        fun getOptionValue(currentValue: String, options: Options): String {
            val items = options.items.map{
                it.name
            }

            val index = items.indexOf(currentValue)

            if (index == -1) return options.items[0]?.value ?: "";

            return options.items[index]?.value ?: ""
        }

        fun loadJSONFromAsset(input: InputStream): String {
            return try {
                val size: Int = input.available()
                val buffer = ByteArray(size)
                input.read(buffer)
                input.close()
                String(buffer, Charset.forName("UTF-8")).trimIndent()
            } catch (ex: IOException) {
                ex.printStackTrace()
                return ""
            }
        }

        fun createItemList(items: List<String>, context: Context): ArrayAdapter<String> {
            return ArrayAdapter<String>(
                context, android.R.layout.simple_dropdown_item_1line, items
            )
        }

        inline fun <reified T>getJsonResource(context: Context, id: Int): T {
            val str = context.resources.openRawResource(id)
            return Gson().fromJson(
                loadJSONFromAsset(str), T::class.java
            )
        }
    }


}