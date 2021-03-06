package com.codeenjoyers.mentaltracker.ui.home

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.FileObserver.DELETE
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.textservice.TextInfo
import android.widget.*
import androidx.collection.arrayMapOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.codeenjoyers.mentaltracker.R
import com.codeenjoyers.mentaltracker.ui.slideshow.MoodDataClass
import com.codeenjoyers.mentaltracker.ui.slideshow.SlideshowFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.w3c.dom.Text
import java.io.File
import java.util.*
import kotlin.text.toLowerCase
import kotlin.text.indexOf

class HomeFragment : Fragment() {


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
        val root = inflater.inflate(R.layout.fragment_home, container, false)


        MoodDataClass.init()
        val fab: FloatingActionButton = root.findViewById(R.id.fab)
        fab.setOnClickListener {
            val select : SlideshowFragment = SlideshowFragment()
            val fragmentManager = requireActivity().supportFragmentManager
            val transaction = fragmentManager.beginTransaction()
            transaction.replace(this.id, select, "tag")
            transaction.addToBackStack(null)
            transaction.commit()
        }
        val SAVEFILE = File(context?.filesDir, SlideshowFragment.FILENAME)
        var fileString: String
        if (SAVEFILE.exists()){

            fileString = requireContext().openFileInput(SlideshowFragment.FILENAME)!!.bufferedReader().useLines { lines ->
                lines.fold("") { some, text ->
                    "$some$text"
                }
            }

        }else{
            fileString = ""
        }

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
        val myAdapter= MyCustomAdapter(requireContext(),records)



        val searchButton:ImageButton = root.findViewById<ImageButton>(R.id.search)
        val editTextSearch:EditText = root.findViewById<EditText>(R.id.editTextDate)
        editTextSearch.setOnKeyListener() { view: View, i: Int, keyEvent: KeyEvent ->
            var newRecords: MutableList<record> = mutableListOf()
            if (editTextSearch.text.toString() == "" || (editTextSearch.text.length == 1 && i == 67)) {
                newRecords = records
            } else {
                for (x in records) {
                    if (x.mMood.toString().toLowerCase().startsWith(editTextSearch.text.toString()
                            .toLowerCase())
                    ) {
                        newRecords.add(x)
                    }
                }
            }
            myAdapter.updateRecords(newRecords)
            return@setOnKeyListener false
        }
        searchButton.setOnClickListener {
            var  newRecords: MutableList<record> = mutableListOf()
            if (editTextSearch.text.toString() == ""){
                newRecords = records
            }else{
                for (x in records){
                    if (x.mMood.toString().toLowerCase() == editTextSearch.text.toString().toLowerCase()){
                        newRecords.add(x)
                    }
                }
            }
            myAdapter.updateRecords(newRecords)

        }
        ListViews.adapter = myAdapter
        return root
    }

    private class MyCustomAdapter(context: Context, records : MutableList<record>): BaseAdapter() {

        private val mContext: Context
        private var mRecords : MutableList<record>

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

        fun updateRecords(records: MutableList<record>){
            mRecords = records
            this.notifyDataSetChanged()
        }

        // responsible for rendering out each row
        override fun getView(position: Int, convertView: View?, viewGroup: ViewGroup?): View {
            val layoutInflater = LayoutInflater.from(mContext)
            val row = layoutInflater.inflate(R.layout.list_row,viewGroup,false)
            val textValue = row.findViewById<TextView>(R.id.textValue)
            val textValue2 = row.findViewById<TextView>(R.id.textValue2)
            val date = row.findViewById<TextView>(R.id.date)

            if((mRecords[position].mCustom != "none") && (mRecords[position].mCustom != "null")){
                textValue.text = mRecords[position].mCustom
            }else {
                textValue.text = "No notes"
            }
            textValue2.text = mRecords[position].mMood

            val colors = arrayListOf(
                Color.rgb(0,150,0),
                Color.RED,
                Color.GRAY,
                Color.WHITE,
                Color.YELLOW,
                Color.rgb(100,100,230)
            ).asReversed()

            var index = 0
            for ((x,y) in MoodDataClass.innerData ){

                if(mRecords[position].mMood in y){
                    index =  MoodDataClass.data.indexOf(x)
                }
                if(mRecords[position].mMood in MoodDataClass.data){
                    index =  MoodDataClass.data.indexOf(mRecords[position].mMood)
                }
            }



            textValue2.setTextColor(colors[index]!!)



            val datetime= mRecords[position].mdateTime.split(":")

            date.text = datetime[0].toString() + "/" + datetime[1].toString() + "/" + datetime[2].toString() +" "+ datetime[3].toString() + ":" +datetime[4].toString()

            return  row
        }



    }

}

