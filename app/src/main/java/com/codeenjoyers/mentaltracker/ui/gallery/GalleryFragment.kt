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
import java.lang.Exception

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
                try {
                    root.findViewById<Button>(R.id.breathBtn).isEnabled=false
                    root.findViewById<Button>(R.id.breathBtn).startAnimation(AnimationUtils.loadAnimation(context,R.anim.fade_out))
                    root.findViewById<TextView>(R.id.text_gallery).startAnimation(AnimationUtils.loadAnimation(context,R.anim.fade_out))
                }catch (e: Exception){}


                Handler().postDelayed({
                    try {
                        root.findViewById<TextView>(R.id.text_gallery).alpha=1F
                        root.findViewById<TextView>(R.id.text_gallery).startAnimation(AnimationUtils.loadAnimation(context,R.anim.blink))
                        root.findViewById<Button>(R.id.breathBtn).alpha=0F
                    }catch (e: Exception){}

                }, 1000)

                Handler().postDelayed({
                    try {
                        root.findViewById<TextView>(R.id.text_gallery).text="2"
                        root.findViewById<TextView>(R.id.text_gallery).startAnimation(AnimationUtils.loadAnimation(context,R.anim.blink))
                    }catch (e: Exception){}

                }, 3000)

                Handler().postDelayed({
                    try {
                        root.findViewById<TextView>(R.id.text_gallery).text="1"
                        root.findViewById<TextView>(R.id.text_gallery).startAnimation(AnimationUtils.loadAnimation(context,R.anim.blink))
                    }catch (e: Exception){}

                }, 5000)
                Handler().postDelayed({
                    try {
                        root.findViewById<TextView>(R.id.text_gallery).alpha=0F
                        root.findViewById<TextView>(R.id.text_gallery).text="3"
                    }catch (e: Exception){}

                }, 7000)

                Handler().postDelayed({ try {root.findViewById<ImageView>(R.id.imageView3).startAnimation(AnimationUtils.loadAnimation(context,R.anim.first_zoom_in))

                }catch (e: Exception){} }, 7000)
                Handler().postDelayed({
                    try {
                        root.findViewById<ImageView>(R.id.imageView3).startAnimation(AnimationUtils.loadAnimation(context,R.anim.zoom_out))
                    }catch (e: Exception){} }, 10000)
                Handler().postDelayed({
                    try {
                        root.findViewById<ImageView>(R.id.imageView3).startAnimation(AnimationUtils.loadAnimation(context,R.anim.zoom_in))
                    }catch (e: Exception){}
                     }, 13000)
                Handler().postDelayed({
                    try {
                        root.findViewById<ImageView>(R.id.imageView3).startAnimation(AnimationUtils.loadAnimation(context,R.anim.zoom_out))
                    }catch (e: Exception){} }, 16000)
                Handler().postDelayed({
                    try {
                        root.findViewById<ImageView>(R.id.imageView3).startAnimation(AnimationUtils.loadAnimation(context,R.anim.zoom_in))
                    }catch (e: Exception){} }, 19000)
                Handler().postDelayed({
                    try {
                        root.findViewById<ImageView>(R.id.imageView3).startAnimation(AnimationUtils.loadAnimation(context,R.anim.zoom_out))
                    }catch (e: Exception){} }, 21000)
                Handler().postDelayed({
                    try {
                        root.findViewById<ImageView>(R.id.imageView3).startAnimation(AnimationUtils.loadAnimation(context,R.anim.last_zoom_out))
                    }catch (e: Exception){} }, 24000)
                Handler().postDelayed({
                    try {
                        root.findViewById<Button>(R.id.breathBtn).startAnimation(AnimationUtils.loadAnimation(context,R.anim.fade_in))
                    }catch (e: Exception){} }, 27000)

                Handler().postDelayed({
                    try {
                        root.findViewById<Button>(R.id.breathBtn).alpha=1F
                        root.findViewById<Button>(R.id.breathBtn).isEnabled=true
                    }catch (e: Exception){}

                }, 28000)





        }
        return root


    }

}