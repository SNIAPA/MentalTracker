package com.codeenjoyers.mentaltracker.ui.gallery

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.codeenjoyers.mentaltracker.R
import org.w3c.dom.Text

class GalleryFragment : Fragment() {

    private lateinit var galleryViewModel: GalleryViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        galleryViewModel =
                ViewModelProvider(this).get(GalleryViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_gallery, container, false)
        val textView: TextView = root.findViewById(R.id.text_gallery)
        galleryViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        root.findViewById<TextView>(R.id.text_gallery).isEnabled=false
        root.findViewById<TextView>(R.id.text_gallery).alpha=0.0F


        root.findViewById<Button>(R.id.breathBtn).setOnClickListener {
            root.findViewById<Button>(R.id.breathBtn).isEnabled=false
            root.findViewById<Button>(R.id.breathBtn).startAnimation(AnimationUtils.loadAnimation(context,R.anim.fade_out))
            root.findViewById<TextView>(R.id.text_gallery).startAnimation(AnimationUtils.loadAnimation(context,R.anim.fade_out))

            Handler().postDelayed({
                root.findViewById<TextView>(R.id.text_gallery).alpha=1F
                root.findViewById<TextView>(R.id.text_gallery).startAnimation(AnimationUtils.loadAnimation(context,R.anim.blink))
                root.findViewById<Button>(R.id.breathBtn).alpha=0F
            }, 1000)

            Handler().postDelayed({
                root.findViewById<TextView>(R.id.text_gallery).text="2"
                root.findViewById<TextView>(R.id.text_gallery).startAnimation(AnimationUtils.loadAnimation(context,R.anim.blink))
            }, 3000)

            Handler().postDelayed({
                root.findViewById<TextView>(R.id.text_gallery).text="1"
                root.findViewById<TextView>(R.id.text_gallery).startAnimation(AnimationUtils.loadAnimation(context,R.anim.blink))
            }, 5000)
            Handler().postDelayed({
                root.findViewById<TextView>(R.id.text_gallery).alpha=0F
            }, 7000)

            Handler().postDelayed({ root.findViewById<ImageView>(R.id.imageView3).startAnimation(AnimationUtils.loadAnimation(context,R.anim.first_zoom_in)) }, 8000)
            Handler().postDelayed({ root.findViewById<ImageView>(R.id.imageView3).startAnimation(AnimationUtils.loadAnimation(context,R.anim.zoom_out)) }, 100000)
            Handler().postDelayed({ root.findViewById<ImageView>(R.id.imageView3).startAnimation(AnimationUtils.loadAnimation(context,R.anim.zoom_in)) }, 12000)
            Handler().postDelayed({ root.findViewById<ImageView>(R.id.imageView3).startAnimation(AnimationUtils.loadAnimation(context,R.anim.zoom_out)) }, 14000)
            Handler().postDelayed({ root.findViewById<ImageView>(R.id.imageView3).startAnimation(AnimationUtils.loadAnimation(context,R.anim.last_zoom_out)) }, 16000)
            Handler().postDelayed({ root.findViewById<Button>(R.id.breathBtn).startAnimation(AnimationUtils.loadAnimation(context,R.anim.fade_in)) }, 180000)

            Handler().postDelayed({
                root.findViewById<Button>(R.id.breathBtn).alpha=1F
                root.findViewById<Button>(R.id.breathBtn).isEnabled=true
            }, 140000)



        }
        return root


    }

}