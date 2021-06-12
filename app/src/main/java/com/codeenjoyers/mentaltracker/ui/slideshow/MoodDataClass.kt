package com.codeenjoyers.mentaltracker.ui.slideshow

import java.time.format.DateTimeFormatter
import java.util.*

class MoodDataClass {
    companion object {
        val data = mutableListOf<String>("Obrzydzenie","Złość","Strach","Zaskoczenie","Szczęście","Smutek")

        val innerData : MutableMap<String, MutableList<String>> = mutableMapOf()

        fun init() {
            innerData.clear()
            innerData[data[0]] = mutableListOf("Znieszmaczony", "Niechętny", "Krytyczny", "Rozczarowany")
            innerData[data[1]] = mutableListOf("Nienawiść","Zraniony","Skeptycznie","Podejrzanie","Zagrożony","Zfrustrowany","Zły","Agresywny")
            innerData[data[2]] = mutableListOf("Odrzucony", "Upokorzony", "Niepewny", "Podatny", "Zlękniony", "Przestraszony")
            innerData[data[3]] = mutableListOf("Zadziwiony", "Zainponowany","Zmieszanie","Podekscytowany")
            innerData[data[4]] = mutableListOf("Spokojny", "Optymistyczny", "Silny", "Potężny", "Zainteresowany", "Dumny", "Radosny", "Zaakceptowany")
            innerData[data[5]] = mutableListOf("Winny","Samotny","Znudzony", "Rozpaczony", "Zdesperowany", "Porzucony")

        }

        fun at(dataList: MutableList<String>,index: Float) = at(dataList, index.toInt())
        fun at(dataList: MutableList<String>,index: Double) = at(dataList, index.toInt())
        fun at(dataList: MutableList<String>, index: Int) : String {
            if (data.size == 0) {
                error("Index Aut Of Bounds -> No Date In Array")
            }

            return dataList[(((index.toFloat() / 360) * dataList.size) - (dataList.size.toFloat() / 2)).toInt() % dataList.size]
        }

        fun noPolish(string: String) : String {
            return string.toLowerCase(Locale.ROOT).replace('ą','a').replace('ć','c').replace('ę','e')
                .replace('ł','l').replace('ń','n').replace('ó','o').replace('ś','s')
                .replace('ż','z').replace('ź','z')
        }
    }
}