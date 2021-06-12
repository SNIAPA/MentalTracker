package com.codeenjoyers.mentaltracker.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.codeenjoyers.mentaltracker.R
import com.codeenjoyers.mentaltracker.ui.slideshow.SlideshowFragment
import java.io.File
import java.io.FileOutputStream

class SettingsFragment : Fragment() {

    private lateinit var settingsViewModel: SettingsViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        settingsViewModel =
                ViewModelProvider(this).get(SettingsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_settings, container, false)
        val textView: TextView = root.findViewById(R.id.text_gallery)
        settingsViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        val SAVEFILE = File(context?.filesDir, SlideshowFragment.FILENAME)
        root.findViewById<Button>(R.id.resetBtn).setOnClickListener {
            FileOutputStream(SAVEFILE).write(
                "".toByteArray()
            )
        }
        return root
    }
}