package com.codeenjoyers.mentaltracker.ui.slideshow

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.fragment.app.Fragment
import com.codeenjoyers.mentaltracker.R
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

class SlideshowFragment : Fragment() {
    private lateinit var imageView: ImageView
    private lateinit var seekBar: SeekBar
    private lateinit var next: Button
    private lateinit var submit: Button
    private lateinit var back: Button
    private var page : Int = 0
    private var selectionList: MutableList<String> = mutableListOf()

    companion object {
        const val FILENAME = "FILENAME"
        const val INTERSPACE = "&"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_slideshow, container, false)

        imageView = root.findViewById(R.id.imageView2)
        seekBar = root.findViewById(R.id.seekBar)
        back = root.findViewById(R.id.back)
        submit = root.findViewById(R.id.submit)
        next = root.findViewById(R.id.next)

        createPage(page)

        seekBar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onStopTrackingTouch(seekBar: SeekBar) {}

            override fun onStartTrackingTouch(seekBar: SeekBar) {}

            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                imageView.rotation = 180f + progress.toFloat()
                //Log.d("WHAT",MoodDataClass.at(180 + progress))
            }
        })

        next.setOnClickListener {
            selectionList.add(page.toString() /*getSelectionValue()*/)
            createPage(++page)
        }

        back.setOnClickListener {
            selectionList.removeAt(selectionList.lastIndex)
            createPage(--page)
        }

        submit.setOnClickListener {
            selectionList.add(page.toString() /*getSelectionValue()*/)


            saveData(selectionList.last())

            selectionList.removeAt(selectionList.lastIndex)
        }

        return root
    }

    fun saveData(data : String = "null", note : String = "null") : Unit{
        fun format(string: String) : String {
            fun convertTime(lastTimeUsed: Long): String {
                val date = Date(lastTimeUsed)
                val format = SimpleDateFormat("yyyy:MM:dd:HH:mm", Locale.ROOT)

                return format.format(date)
            }

            return (convertTime( System.currentTimeMillis() ) + INTERSPACE + string + INTERSPACE + note)
        }

        val SAVEFILE = File(context?.filesDir, FILENAME)
        val oldText = if (SAVEFILE.exists()) requireContext().openFileInput(FILENAME).bufferedReader().useLines { lines ->
            lines.fold("") { some, text ->
                "$some$text"
            }}.toString() else ""

        val newText = (oldText + format(data) + INTERSPACE)

        FileOutputStream(SAVEFILE).write(
            newText.toByteArray()
        )
    }

    private fun createPage(index: Int) : Unit {
        when (index) {
            0 -> {
                imageView.setImageResource(R.drawable.image)
                back.isEnabled = false
                next.isEnabled = true
            }
            1 -> {
                imageView.setImageResource(R.drawable.codeenjoyerslogo)
                back.isEnabled = true
                next.isEnabled = true
            }
            2 -> {
                imageView.setImageResource(R.drawable.image)
                back.isEnabled = true
                next.isEnabled = false
            }
        }
    }
}