package com.codeenjoyers.mentaltracker.ui.stats

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.Telephony.Mms.Part.FILENAME
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.codeenjoyers.mentaltracker.R
import com.codeenjoyers.mentaltracker.codeclasses.DataPoint
import com.codeenjoyers.mentaltracker.ui.slideshow.SlideshowFragment
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*
import android.provider.Telephony.Mms.Part.FILENAME


@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class StatsFragment : Fragment() {

    private lateinit var slideshowViewModel: StatsViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        slideshowViewModel =
                ViewModelProvider(this).get(StatsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_stats, container, false)
        val textView: TextView = root.findViewById(R.id.text_home)
        slideshowViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        generateDataPoints()
        root.findViewById<com.codeenjoyers.mentaltracker.codeclasses.GraphView>(R.id.graph_view).setData(generateRandomDataPoints(6))
        root.findViewById<SeekBar>(R.id.seekBar2).setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, currentValue: Int, p2: Boolean) {
                if (currentValue == 0 ){
                    root.findViewById<com.codeenjoyers.mentaltracker.codeclasses.GraphView>(R.id.graph_view).setData(generateRandomDataPoints(6))
                }
                if (currentValue == 1 ){
                    root.findViewById<com.codeenjoyers.mentaltracker.codeclasses.GraphView>(R.id.graph_view).setData(generateRandomDataPoints(4))
                }
                if (currentValue == 2 ){
                    root.findViewById<com.codeenjoyers.mentaltracker.codeclasses.GraphView>(R.id.graph_view).setData(generateRandomDataPoints(12))
                }

            }

            override fun onStartTrackingTouch(p0: SeekBar?) {}
            override fun onStopTrackingTouch(p0: SeekBar?) {}

        })

        return root
    }



    private fun generateRandomDataPoints(dataNum:Int): List<DataPoint> {
        val random = Random()
        return (0 until dataNum).map {
            DataPoint(it, random.nextInt(10))
            //DataPoint(it, 1)
        }
    }
    @SuppressLint("SimpleDateFormat")
    private fun generateDataPoints(): List<DataPoint> {
        val random = Random()
        val SAVEFILE = File(context?.filesDir, FILENAME)
        var fileString: String
        if (SAVEFILE.exists()){

            fileString = requireContext().openFileInput(FILENAME)!!.bufferedReader().useLines { lines ->
                lines.fold("") { some, text ->
                    "$some$text"
                }
            }

        }else{
            //fileString = ""
            fileString = "0000:00:00:11:12/Happy/none&0000:00:00:11:12/Happy/none&0000:00:00:11:12/Happy/none&0000:00:00:11:12/Happy/none&0000:00:00:11:12/Happy/none&0000:00:00:11:12/Happy/none&0000:00:00:11:12/Happy/none&0000:00:00:11:12/Happy/none&0000:00:00:11:12/Happy/none&0000:00:00:11:12/Happy/none&"
            saveData(fileString)
        }

        var splitedsting = fileString.split("&")
        var dates = mutableListOf<Date>()
        for(record in splitedsting){
            if (record == "") {
                continue
            }
            var recordsplited = record.split("/")
            var splittedData = recordsplited[0].split(":")
            //Log.d("Test", splittedData.toString()+" "+record)
            if(record!="/null"){
                var newData = splittedData[0] +"-"+splittedData[1]+"-"+splittedData[2]+" "+splittedData[3]+"-"+splittedData[4]
                val parser = SimpleDateFormat("yyyy-MM-dd HH-mm")
                val output = parser.parse(newData)
                dates.add(output)
            }
        }

        dates.reverse()
        var DataPointGraph= mutableListOf<DataPoint>()
        var lastWeek= mutableListOf<Date>()
        var lastDate = dates[0]
        val parser = SimpleDateFormat("yyyy-MM-dd HH-mm")
        for (date in dates){
            if(date.after(Date(lastDate.time-(86400000*7)))){
                lastWeek.add(date)
            }
        }
        lastWeek.sortBy { Ddate ->
            Ddate.day
        }
        var lastDaydata = -1
        var newDays = 0
        lastWeek.forEachIndexed{i,date->
                if (lastDaydata!=date.day){
                    newDays+=1
                    lastDaydata=date.day
                    DataPointGraph.add(DataPoint(newDays, 1))
                }
                else{
                    DataPointGraph[newDays-1].yVal+=1
                }

        }

        return DataPointGraph
    }


    fun saveData(data : String = null?:"null", note : String = null?:"null") : Unit{
        fun format(string: String) : String {
            fun convertTime(lastTimeUsed: Long): String {
                val date = Date(lastTimeUsed)
                val format = SimpleDateFormat("yyyy:MM:dd:HH:mm", Locale.ROOT)

                return format.format(date)
            }

            return (convertTime( System.currentTimeMillis() ) + "/" + string + "/" + note)
        }

        val SAVEFILE = File(context?.filesDir, FILENAME)
        val oldText = if (SAVEFILE.exists()) requireContext().openFileInput(FILENAME).bufferedReader().useLines { lines ->
            lines.fold("") { some, text ->
                "$some$text"
            }}.toString() else ""

        val newText = (oldText+format(data) + "/")

        FileOutputStream(SAVEFILE).write(
            newText.toByteArray()
        )
    }
}