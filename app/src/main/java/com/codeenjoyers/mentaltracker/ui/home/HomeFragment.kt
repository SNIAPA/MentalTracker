package com.codeenjoyers.mentaltracker.ui.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.textservice.TextInfo
import android.widget.BaseAdapter
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.codeenjoyers.mentaltracker.R
import com.codeenjoyers.mentaltracker.ui.slideshow.SlideshowFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.w3c.dom.Text
import java.io.File
import java.util.*


class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel

    private class record(dateTime: String, Mood:String,Custom:String){

        public val mdateTime: String
        public val mMood:String
        public val mCustom:String

        init {
            mdateTime = dateTime
            mMood = Mood
            mCustom = Custom
        }

    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
                ViewModelProvider(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)


        val fab: FloatingActionButton = root.findViewById(R.id.fab)
        fab.setOnClickListener {
            val select : SlideshowFragment = SlideshowFragment()
            val fragmentManager = requireActivity().supportFragmentManager
            val transaction = fragmentManager.beginTransaction()
            transaction.replace(this.id, select, "tag")
            transaction.addToBackStack(null)
            transaction.commit()
        }

        val SAVEFILE = File(context?.filesDir, "MOODSAVE.txt")
        var fileString: String
        if (SAVEFILE.exists()){

            fileString = requireContext().openFileInput("MOODSAVE.txt")!!.bufferedReader().useLines { lines ->
                lines.fold("") { some, text ->
                    "$some$text"
                }
            }

        }else{
            fileString = ""
        }

        fileString = "0000:00:00:11:12/Happy/None&0000:00:00:11:12/Happy/None&0000:00:00:11:12/Happy/None&"

        var splitedsting = fileString.split("&")

        var  records: MutableList<record> = mutableListOf()
        for(record in splitedsting){
            if (record == "") {
                continue
            }
            var recordsplited = record.split("/")
            var dataTime: String = recordsplited[0]

            records.add(record(dataTime,recordsplited[1],recordsplited[2]))

        }

        val ListViews = root.findViewById<ListView>(R.id.listView)

        ListViews.adapter = MyCustomAdapter(requireContext(),records)

        val TextInput = root.findViewById<EditText>(R.id.editTextDate)



        return root
    }

    private class MyCustomAdapter(context: Context,records: MutableList<record>): BaseAdapter() {

        private val mContext: Context
        private val mRecords: MutableList<record>

        init {
            mContext = context
            mRecords = records
        }

        // responsible for how many rows in my list
        override fun getCount(): Int {
            return mRecords.size
        }

        // you can also ignore this
        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        // you can ignore this for now
        override fun getItem(position: Int): Any {
            return "TEST STRING"
        }

        // responsible for rendering out each row
        override fun getView(position: Int, convertView: View?, viewGroup: ViewGroup?): View {
            val layoutInflater = LayoutInflater.from(mContext)
            val row = layoutInflater.inflate(R.layout.list_row,viewGroup,false)
            val textValue = row.findViewById<TextView>(R.id.textValue)
            val date = row.findViewById<TextView>(R.id.date)

            if(mRecords[position].mMood == "none"){
                textValue.text = mRecords[position].mCustom
            }else{
                textValue.text = mRecords[position].mMood
            }


            date.text= mRecords[position].mdateTime
            return row
        }



    }

}

