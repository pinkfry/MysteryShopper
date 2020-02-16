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
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class DownloadDataActivity : AppCompatActivity() {
    lateinit var questionArrayList: ArrayList<QuestionsModel>
    lateinit var dialog: DatePickerDialog
    lateinit var toDialog: DatePickerDialog
    lateinit var singleStoreList: ArrayList<SingleStore>
    lateinit var clientName: String
    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_download_data)
        var fromDate = 1
        var toDate = 1
        var fromMonth = 1
        var toMonth = 1
        var fromYear = 1
        var dateArrayList = ArrayList<String>()
        var toYear = 1

        var singleStoreJSON = intent.getStringExtra("singleStore")
        clientName = intent.getStringExtra("clientName")!!
        val gson = Gson()


        val storeType: Type = object : TypeToken<ArrayList<SingleStore?>?>() {}.type
        singleStoreList = gson.fromJson<ArrayList<SingleStore>>(singleStoreJSON, storeType)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            dialog = DatePickerDialog(this)
            dialog.setOnDateSetListener { view, year, month, dayOfMonth ->
                tvFromDate.text = "${dayOfMonth}/${month + 1}/${year}"
                fromDate = dayOfMonth
                fromMonth = month + 1
                fromYear = year
                Log.d("DDA", tvToDate.text.toString())
            }
            toDialog = DatePickerDialog(this)
            toDialog.setOnDateSetListener { view, year, month, dayOfMonth ->
                tvToDate.text = "${dayOfMonth}/${month + 1}/${year}"
                toDate = dayOfMonth
                toMonth = month + 1
                toYear = year
                Log.d("DDA", tvToDate.text.toString())
            }
        }
        tvFromDate.setOnClickListener {
            dialog.show()
        }
        tvToDate.setOnClickListener {
            toDialog.show()
        }
        btnDownloadData.setOnClickListener {
            dateArrayList.clear()
            var startDate = "$fromYear-$fromMonth-$fromDate"
            var endDate = "$toYear-$toMonth-$toDate"
//            getDates(startDate, endDate)
            var cal = Calendar.getInstance();
            for (date in getDates(startDate, endDate)!!) {
                cal.time = date
                var formatedDate =
                    "${cal.get(Calendar.YEAR)}-${(cal.get(Calendar.MONTH) + 1)}-${cal.get(Calendar.DATE)}"
                Log.d("SSA", formatedDate)
                dateArrayList.add(formatedDate)
            }

            Log.d("SSA", dateArrayList.toString())
            if (dateArrayList.isNotEmpty())
                getExportedData(singleStoreList, dateArrayList)
            else {
                Toast.makeText(this, "Please select a proper date", Toast.LENGTH_SHORT).show()
            }
//            getDataExported(singleStoreList, dateArrayList)
        }
    }

    @SuppressLint("SetTextI18n")
    fun getExportedData(storeArrayList: ArrayList<SingleStore>, dateArray: ArrayList<String>) {
        var store = "NautiyalJi"
        var date = ""
        var question = "1"
        var arrayListOfData = ArrayList<ModelExportData>()
        var ans = ""
        var innerJsonArray = JSONArray()
        var stringarry = dateArray
        Log.d("SSA", "${storeArrayList.size}")
        Log.d("SSA", "${stringarry.size} in date")

        if (storeArrayList.size > 0) {
            var upperAnsGivenArrayList = singleStoreList[0].AnsGiven.size

            for (k in stringarry) {

                for (i in 0 until upperAnsGivenArrayList) {
                    var jsonObject = JSONObject()
                    date = k


                    for ((index, value) in storeArrayList.withIndex()) {

                        store = value.name
                        var ansGiven = storeArrayList[index].AnsGiven[i].shortedByDate[date]

//                    Log.d("SSA", ansGiven.toString())
                        if (storeArrayList[index].AnsGiven[i].visible != 0) {
                            if (ansGiven != null) {
                                jsonObject.put("Date", date)
                                jsonObject.put("question", storeArrayList[index].AnsGiven[i].question)

                                for (element in ansGiven.ans) {
                                    ans += "$element   *   "
                                }
                                ans += ansGiven.value


//                    question =
//                    arrayListOfData.add(ModelExportData(clientName, store, question, date, ans))

                                jsonObject.put(store, ans)
                                ans = ""
                                Log.d("SSA", "${arrayListOfData.size}")

                            }
                        }

                    }
//
                    if (jsonObject.length() != 0)
                        innerJsonArray.put(jsonObject)

                }


            }
        }

        Log.d("SSA", innerJsonArray.toString())

        if (CDL.toString(innerJsonArray) != null) {
            try {
                val docs = innerJsonArray
                var path = Environment.getExternalStorageDirectory().absolutePath + "/MysteryShopper/"
                val file = File(path)
                if (!file.exists()) {
                    file.mkdirs()
                }
                val csv: String = CDL.toString(docs)
                var timeStamp = System.currentTimeMillis()
                var calc = Calendar.getInstance()
                var datetoday = "${calc.get(Calendar.DATE)}${calc.get(Calendar.MONTH) + 1}${calc.get(Calendar.YEAR)}"
                with(File(file, "$clientName-$datetoday-$timeStamp.csv")) {

                    writeBytes(csv.toByteArray())
                }


                Log.d("SSA", csv)

                Toast.makeText(this, "Successfully Downloaded Data", Toast.LENGTH_SHORT).show()
                tvLocation.text = "$clientName-$datetoday-$timeStamp.csv"
            } catch (e: JSONException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        } else {
            Toast.makeText(this, "The data is totally empty, Nothing to download", Toast.LENGTH_SHORT).show()

        }

    }

    fun getDataExported(storeArrayList: ArrayList<SingleStore>, dateArray: ArrayList<String>) {
        var store = "NautiyalJi"
        var date = ""
        var question = "1"
        var arrayListOfData = ArrayList<ModelExportData>()
        var ans = ""
        var innerJsonArray = JSONArray()
        var stringarry = dateArray
        Log.d("SSA", "${storeArrayList.size}")
        if (storeArrayList.size > 0) {
            var upperAnsGivenArrayList = singleStoreList[0].AnsGiven.size
            for (k in stringarry) {

                for (i in 0 until upperAnsGivenArrayList) {
                    var jsonObject = JSONObject()
                    date = k


                    for ((index, value) in storeArrayList.withIndex()) {
                        jsonObject.put("question", storeArrayList[index].AnsGiven[i].question)

                        store = value.name
                        var ansGiven = storeArrayList[index].AnsGiven[i].shortedByDate[date]

//                    Log.d("SSA", ansGiven.toString())
                        if (ansGiven != null) {
                            for (element in ansGiven.ans) {
                                ans += "$element   *   "
                            }
                            ans += ansGiven.value


//                    question =
//                    arrayListOfData.add(ModelExportData(clientName, store, question, date, ans))

                            jsonObject.put(store, ans)
                            ans = ""
                            Log.d("SSA", "${arrayListOfData.size}")
                            jsonObject.put("Date", date)
                        }

                    }

                    innerJsonArray.put(jsonObject)

                }


            }
        }
        Log.d("DDA", innerJsonArray.toString())
    }


    private fun getDates(dateString1: String, dateString2: String): List<Date>? {
        val dates = ArrayList<Date>()
        val df1: DateFormat = SimpleDateFormat("yyyy-M-d")
        var date1: Date? = null
        var date2: Date? = null
        try {
            date1 = df1.parse(dateString1)
            date2 = df1.parse(dateString2)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        val cal1 = Calendar.getInstance()
        cal1.time = date1
        val cal2 = Calendar.getInstance()
        cal2.time = date2
        while (!cal1.after(cal2)) {
            dates.add(cal1.time)
            cal1.add(Calendar.DATE, 1)
        }
        return dates
    }
}
