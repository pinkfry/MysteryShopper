package com.pinkfry.tech.mysteryshopper.Activity

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

import com.pinkfry.tech.mysteryshopper.R
import com.pinkfry.tech.mysteryshopper.model.ModelExportData
import com.pinkfry.tech.mysteryshopper.model.QuestionsModel
import com.pinkfry.tech.mysteryshopper.model.SingleStore
import kotlinx.android.synthetic.main.activity_download_data.*
import org.json.CDL
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.File
import java.io.IOException
import java.lang.reflect.Type
import java.util.*
import kotlin.collections.ArrayList

class DownloadDataActivity : AppCompatActivity() {
lateinit var questionArrayList:ArrayList<QuestionsModel>
    lateinit var dialog:DatePickerDialog
    lateinit var toDialog:DatePickerDialog
    lateinit var singleStoreList: ArrayList<SingleStore>
    lateinit var clientName:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_download_data)
        var fromDate=1
        var toDate=1
        var fromMonth=1
        var toMonth=1
        var fromYear=1
        var dateArrayList=ArrayList<String>()
        var toYear=1

        var singleStoreJSON=intent.getStringExtra("singleStore")
        clientName=intent.getStringExtra("clientName")!!
        val gson = Gson()


        val storeType: Type = object : TypeToken<ArrayList<SingleStore?>?>() {}.type
        singleStoreList =gson.fromJson<ArrayList<SingleStore>>(singleStoreJSON,storeType)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            dialog = DatePickerDialog(this)
            dialog.setOnDateSetListener { view, year, month, dayOfMonth ->
                tvFromDate.text="${dayOfMonth}/${month}/${year}"
                fromDate=dayOfMonth
                fromMonth=month
                fromYear=year
                Log.d("DDA",tvToDate.text.toString())
            }
            toDialog= DatePickerDialog(this)
            toDialog.setOnDateSetListener { view, year, month, dayOfMonth ->
                tvToDate.text="${dayOfMonth}/${month}/${year}"
                toDate=dayOfMonth
                toMonth=month
                toYear=year
                Log.d("DDA",tvToDate.text.toString())
            }
        }
        tvFromDate.setOnClickListener{
          dialog.show()
        }
        tvToDate.setOnClickListener {
            toDialog.show()
        }
        btnDownloadData.setOnClickListener {
            for(year in fromYear..toYear)
            {
                for(month in fromMonth..toMonth)
                {
                    for(day in fromDate..toDate){
                        dateArrayList.add("${day}${month}${year}")
                        Log.d("SSA",dateArrayList.toString())
                    }
                }
            }
            getExportedData(singleStoreList,dateArrayList)
        }
    }

    @SuppressLint("SetTextI18n")
    fun getExportedData(storeArrayList:ArrayList<SingleStore>, dateArray:ArrayList<String>){
        var store="NautiyalJi"
        var date=""
        var question="1"
        var arrayListOfData=ArrayList<ModelExportData>()
        var ans=""
        var innerJsonArray= JSONArray()
        var stringarry= dateArray
        Log.d("SSA","${storeArrayList.size}")
        for((index,value) in storeArrayList.withIndex()) {
            var upperAnsGivenArrayList=value.AnsGiven
            store=value.name
            for(k in stringarry) {
                date=k
                for (i in 0 until upperAnsGivenArrayList.size) {
                    var ansGiven = storeArrayList[index].AnsGiven[i].shortedByDate[date]

                    Log.d("SSA",ansGiven.toString())
                    if(ansGiven!=null) {
                        for (element in ansGiven.ans) {
                            ans += "$element   *   "
                        }
                    ans+=ansGiven.value
                    }

                    question= storeArrayList[index].AnsGiven[i].question
                    arrayListOfData.add(ModelExportData(clientName,store,question,date,ans))

                    ans=""
                    Log.d("SSA","${arrayListOfData.size}")
                }

            }




        }
        for(model in arrayListOfData) {
            var innerJsonObject = JSONObject()
            innerJsonObject.put("clientName", model.clientName)
            innerJsonObject.put("storeName", model.storeName)
            innerJsonObject.put("question", model.question)
            innerJsonObject.put("date", model.date)
            innerJsonObject.put("ans", model.ans)
            innerJsonArray.put(innerJsonObject)

        }
        Log.d("SSA",innerJsonArray.toString())
        try {
            val docs =innerJsonArray
            var path= Environment.getExternalStorageDirectory().absolutePath+"/MysteryShopper/"
            val file = File(path)
            if (!file.exists()) {
                file.mkdirs()
            }
            val csv: String = CDL.toString(docs)
            var timeStamp=System.currentTimeMillis()
            var calc=Calendar.getInstance()
            var datetoday="${calc.get(Calendar.DATE)}${calc.get(Calendar.MONTH)}${calc.get(Calendar.YEAR)}"
            with(File(file,"excel-$datetoday-$timeStamp.csv")) {
                writeBytes(csv.toByteArray())
            }


            Log.d("SSA",csv)

            Toast.makeText(this,"Successfully Downloaded Data", Toast.LENGTH_SHORT).show()
            tvLocation.text= "$path/excel-$datetoday-$timeStamp.csv/"
        } catch (e: JSONException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }
}