package com.codeenjoyers.mentaltracker.ui.slideshow

import android.content.res.Resources
import android.graphics.drawable.Drawable
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
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.codeenjoyers.mentaltracker.R
import com.codeenjoyers.mentaltracker.ui.home.HomeFragment
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*


class SlideshowFragment : Fragment() {
    private lateinit var imageView: ImageView
    private lateinit var seekBar: SeekBar
    private lateinit var submit: Button
    private lateinit var back: Button
    private lateinit var next: Button

    private var page : Int = 0
    private var selectionList: MutableList<String> = mutableListOf()

    companion object {
        const val FILENAME = "FILENAME"
        const val INTERSPACE = "&"
        const val INFO_INTERSPACE = "/"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_slideshow, container, false)

        imageView = root.findViewById(R.id.imageView2)
        seekBar = root.findViewById(R.id.seekBar)
        submit = root.findViewById(R.id.submit)
        next = root.findViewById(R.id.next)
        back = root.findViewById(R.id.back)

        createPage(page)

        seekBar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onStopTrackingTouch(seekBar: SeekBar) {}

            override fun onStartTrackingTouch(seekBar: SeekBar) {}

            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                imageView.rotation = 180f + progress.toFloat()
            }
        })

        next.setOnClickListener {
            selectionList.add(MoodDataClass.at(180 + seekBar.progress))
            Log.d("LIST", selectionList.toString())
            createPage(++page)
        }

        back.setOnClickListener {
            selectionList.removeAt(selectionList.lastIndex)
            Log.d("LIST", selectionList.toString())
            createPage(--page)
        }

        submit.setOnClickListener {
            selectionList.add(MoodDataClass.at(180 + seekBar.progress))
            saveData(selectionList.last())
            Log.d("LIST", selectionList.toString())
            selectionList.removeAt(selectionList.lastIndex)

            val fragment2 = HomeFragment()
            val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
            val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(this.id, fragment2, "tag")
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }

        return root
    }

    fun saveData(data: String = null ?: "null", note: String = null ?: "null") : Unit{
        fun format(string: String) : String {
            fun convertTime(lastTimeUsed: Long): String {
                val date = Date(lastTimeUsed)
                val format = SimpleDateFormat("yyyy:MM:dd:HH:mm", Locale.ROOT)

                return format.format(date)
            }

            return (convertTime(System.currentTimeMillis()) + INFO_INTERSPACE + string + INFO_INTERSPACE + note)
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
                Log.d("LIST", MoodDataClass.noPolish(selectionList[0]))
                imageView.setImageResource(resources.getIdentifier(MoodDataClass.noPolish(selectionList[0]), "drawable", activity?.packageName))
                back.isEnabled = true
                next.isEnabled = false
            }
        }
    }
}