package com.codeenjoyers.mentaltracker.codeclasses

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class GraphView(context: Context, attributeSet: AttributeSet): View(context, attributeSet) {

    private val dataSet = mutableListOf<DataPoint>()
    private var xMin = 0
    private var xMax = 0
    private var yMin = 0
    private var yMax = 0
    private var minVal = 0F
    private var maxVal = 0F
    private var amplitude = 0F

    private val dataPointPaint = Paint().apply {
        color = Color.RED
        strokeWidth = 7f
        style = Paint.Style.STROKE
    }

    private val dataPointFillPaint = Paint().apply {
        color = Color.WHITE
    }

    private val dataPointLinePaint = Paint().apply {
        color = Color.BLUE
        strokeWidth = 7f
        isAntiAlias = true
    }

    private val axisLinePaint = Paint().apply {
        color = Color.BLACK
        strokeWidth = 10f
    }

    private val canvText = Paint().apply {
        color = Color.BLACK
        textSize=50F
        strokeWidth = 7f
        isAntiAlias = true
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        dataSet.forEachIndexed { index, currentDataPoint ->
            if (maxVal<currentDataPoint.yVal){
                maxVal= currentDataPoint.yVal.toFloat()
            }
        }
        minVal=maxVal
        dataSet.forEachIndexed { index, currentDataPoint ->
            if (minVal>currentDataPoint.yVal){
                minVal= currentDataPoint.yVal.toFloat()
            }
        }
        amplitude = maxVal-minVal
        dataSet.forEachIndexed { index, currentDataPoint ->
            val realX = currentDataPoint.xVal.toRealX()
            val realY = currentDataPoint.yVal.toRealY()

            if (index < dataSet.size - 1) {
                val nextDataPoint = dataSet[index + 1]
                val startX = currentDataPoint.xVal.toRealX()
                val startY = currentDataPoint.yVal.toRealY()
                val endX = nextDataPoint.xVal.toRealX()
                val endY = nextDataPoint.yVal.toRealY()
                canvas.drawLine(startX.toFloat(), startY.toFloat(), endX.toFloat(), endY.toFloat(), dataPointLinePaint)
            }

            canvas.drawCircle(realX.toFloat(), realY.toFloat(), 7f, dataPointFillPaint)
            canvas.drawCircle(realX.toFloat(), realY.toFloat(), 7f, dataPointPaint)

            canvas.drawText("${currentDataPoint.yVal}",realX.toFloat()+10,realY.toFloat()-10,canvText)
            canvas.drawCircle(realX.toFloat(), realY.toFloat(), 7f, dataPointFillPaint)
        }

        canvas.drawLine(0f, 0f, 0f, height.toFloat(), axisLinePaint)
        canvas.drawLine(0f, height.toFloat(), width.toFloat(), height.toFloat(), axisLinePaint)
    }

    fun setData(newDataSet: List<DataPoint>) {
        xMin = newDataSet.minBy { it.xVal }?.xVal ?: 0
        xMax = newDataSet.maxBy { it.xVal }?.xVal ?: 0
        yMin = newDataSet.minBy { it.yVal }?.yVal ?: 0
        yMax = newDataSet.maxBy { it.yVal }?.yVal ?: 0
        dataSet.clear()
        dataSet.addAll(newDataSet)
        invalidate()
    }

    private fun Int.toRealX() = (toFloat() / xMax * width/1.2).toInt()+30
    private fun Int.toRealY() = height-(height*(toFloat()/maxVal))/2
}

data class DataPoint(
    val xVal: Int,
    val yVal: Int
)