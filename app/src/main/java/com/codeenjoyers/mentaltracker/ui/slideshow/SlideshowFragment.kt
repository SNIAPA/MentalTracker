package com.codeenjoyers.mentaltracker.ui.slideshow

import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.fragment.app.Fragment
import com.codeenjoyers.mentaltracker.R


class SlideshowFragment : Fragment() {

    private lateinit var imageView: ImageView
    private lateinit var seekBar: SeekBar
    private lateinit var next: Button
    private lateinit var submit: Button
    private lateinit var back: Button
    private var page : Int = 0
    private var selectionList: MutableList<String> = mutableListOf()

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
            override fun onStopTrackingTouch(seekBar: SeekBar) {
                // TODO Auto-generated method stub
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                // TODO Auto-generated method stub
            }

            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                imageView.rotation = 180f + progress.toFloat()
            }
        })

        next.setOnClickListener {
            //selectionList.add(getSelectionValue())
            createPage(++page)
        }

        back.setOnClickListener {
            selectionList.removeLast()
            createPage(--page)
        }

        submit.setOnClickListener {
        }

        return root
    }

    private fun createPage(index: Int) : Unit {
        when (index) {
            0 -> {
                imageView.background = (Drawable.createFromPath("@drawable/image"))
                back.isEnabled = false
                next.isEnabled = true
            }
            1 -> {
                back.isEnabled = true
                next.isEnabled = true
            }
            2 -> {
                back.isEnabled = true
                next.isEnabled = false
            }
        }
    }
}