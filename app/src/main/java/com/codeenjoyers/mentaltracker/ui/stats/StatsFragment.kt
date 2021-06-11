package com.codeenjoyers.mentaltracker.ui.stats

import android.R.attr.data
import android.content.Context
import android.graphics.*
import android.graphics.PorterDuff.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.graphics.drawable.shapes.RectShape
import android.os.Bundle
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.codeenjoyers.mentaltracker.R
import com.codeenjoyers.mentaltracker.codeclasses.DataPoint
import com.codeenjoyers.mentaltracker.codeclasses.GraphView
import java.util.*
import kotlin.math.absoluteValue
import java.util.*


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
        root.findViewById<com.codeenjoyers.mentaltracker.codeclasses.GraphView>(R.id.graph_view).setData(generateRandomDataPoints())


        return root
    }
    private fun generateRandomDataPoints(): List<DataPoint> {
        val random = Random()
        return (0..6).map {
            DataPoint(it, random.nextInt(10))
            //DataPoint(it, 1)
        }
    }
}