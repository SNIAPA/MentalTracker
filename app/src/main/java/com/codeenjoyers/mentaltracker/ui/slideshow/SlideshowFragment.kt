package com.codeenjoyers.mentaltracker.ui.slideshow

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.SeekBar.*
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.codeenjoyers.mentaltracker.R
import com.codeenjoyers.mentaltracker.ui.home.HomeFragment
import java.io.File
import java.io.FileOutputStream
import java.lang.Math.*
import java.text.SimpleDateFormat
import java.util.*

class SlideshowFragment : Fragment() {
    private lateinit var imageView: ImageView
    private lateinit var arrowView: View
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

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_slideshow, container, false)

        MoodDataClass.init()
        imageView = root.findViewById(R.id.imageView2)
        arrowView = root.findViewById(R.id.imageView3)
        seekBar = root.findViewById(R.id.seekBar)
        submit = root.findViewById(R.id.submit)
        next = root.findViewById(R.id.next)
        back = root.findViewById(R.id.back)

        imageView.isClickable = true
        imageView.setOnTouchListener( object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                view?.performClick()
                if (event?.action == MotionEvent.ACTION_MOVE) {

                    val location: IntArray = IntArray(2)
                    imageView.getLocationOnScreen(location);

                    val relPosX =event.rawX - location[0] - imageView.width/2
                    val relPosY =event.rawY - location[1] - imageView.height/2

                    val angle = atan2(relPosY.toDouble(),relPosX.toDouble())
                    Log.i("REL COORD", relPosX.toString() + " " + relPosY.toString())

                    Log.d("rad", angle.toString())
                    Log.d("deg", (angle * 180/PI).toString())


                    // COUNT DISTANCE

                    if (selectionList.size == 1){

                        val triNum = MoodDataClass!!.innerData[selectionList[0]]!!.size
                        val final = round(((angle * 180/PI) / (360/triNum)) + (triNum/2)-0.5).toInt()
                        selectionList.add(MoodDataClass!!.innerData[selectionList[0]]!![final])
                        submit.performClick()
                        Log.d("FINAL", final.toString())
                        return true
                    }

                    val final = round(((angle * 180/PI) / 60) + 2.5F).toInt()
                    Log.d("FINAL", final.toString())
                    selectionList.add(MoodDataClass.data[final])


                    createPage(1)
                    submit.visibility = VISIBLE
                    return true
                }

                return false
            }
        })

        createPage(page)

        seekBar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onStopTrackingTouch(seekBar: SeekBar) {}

            override fun onStartTrackingTouch(seekBar: SeekBar) {}

            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                imageView.rotation = 180f + progress.toFloat()

                if (selectionList.size != 0)
                    Log.d("Value",MoodDataClass.at(MoodDataClass.innerData[selectionList[0]]!!, 180 + seekBar.progress))
                else
                    Log.d("Value",MoodDataClass.at(MoodDataClass.data, 180 + seekBar.progress))
            }
        })

        next.setOnClickListener {
            selectionList.add(MoodDataClass.at(MoodDataClass.data, 180 + seekBar.progress))
            Log.d("LIST", selectionList.toString())
            createPage(++page)
        }

        back.setOnClickListener {
            selectionList.removeAt(selectionList.lastIndex)
            Log.d("LIST", selectionList.toString())
            createPage(--page)
        }

        seekBar.visibility = GONE
        submit.visibility = INVISIBLE
        next.visibility = INVISIBLE
        back.visibility = INVISIBLE
        arrowView.visibility = INVISIBLE

        submit.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Wpisz Notatkę")

            val input = EditText(requireContext())
            var inputedText = ""
            input.inputType = InputType.TYPE_CLASS_TEXT.or(InputType.TYPE_TEXT_FLAG_MULTI_LINE)
            builder.setView(input)

            builder.setPositiveButton( "Dodaj", object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    inputedText = input.text.toString()
                    saveData(selectionList.last(), inputedText)
                    selectionList.removeAt(selectionList.lastIndex)
                    val fragment2 = HomeFragment()
                    val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
                    val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
                    fragmentTransaction.replace(this@SlideshowFragment.id, fragment2, "tag")
                    fragmentTransaction.addToBackStack(null)
                    fragmentTransaction.commit()
                }

            })
            builder.setOnCancelListener {
                val fragment2 = HomeFragment()
                val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
                val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
                fragmentTransaction.replace(this@SlideshowFragment.id, fragment2, "tag")
                fragmentTransaction.addToBackStack(null)
                fragmentTransaction.commit()
            }

            builder.show()

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