package com.codeenjoyers.mentaltracker.ui.slideshow

import java.time.format.DateTimeFormatter
import java.util.*

class MoodDataClass {
    companion object {
        val data = mutableListOf<String>("Obrzydzenie","Złość","Strach","Zaskoczenie","Szczęście","Smutek")

        val innerData : MutableMap<String, MutableList<String>> = mutableMapOf()

        fun init() {
            innerData.clear()
            innerData[data[0]] = mutableListOf("")
        }

        fun at(index: Float) = at(index.toInt())
        fun at(index: Double) = at(index.toInt())
        fun at(index: Int) : String {
            if (data.size == 0) {
                error("Index Aut Of Bounds -> No Date In Array")
            }

            return data[(((index.toFloat() / 360) * data.size) - (data.size.toFloat() / 2)).toInt() % data.size]
        }

        fun noPolish(string: String) : String {
            return string.toLowerCase(Locale.ROOT).replace('ą','a').replace('ć','c').replace('ę','e')
                .replace('ł','l').replace('ń','n').replace('ó','o').replace('ś','s')
                .replace('ż','z').replace('ź','z')
        }
    }
}