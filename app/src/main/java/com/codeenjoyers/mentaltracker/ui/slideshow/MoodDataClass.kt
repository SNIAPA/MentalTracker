package com.codeenjoyers.mentaltracker.ui.slideshow

class MoodDataClass {
    companion object {
        val data = mutableListOf<String>("1","2","3","4","5","6","7","8")

        fun at(index: Float) = at(index.toInt())
        fun at(index: Double) = at(index.toInt())
        fun at(index: Int) : String {
            if (data.size == 0) {
                error("Index Aut Of Bounds")
            }
            return data[(((index.toFloat() / 360) * data.size) - (data.size.toFloat() / 2)).toInt() % data.size]
        }
    }
}