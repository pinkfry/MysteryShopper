package com.pinkfry.tech.mysteryshopper.Activity

import android.content.Intent
import android.os.Bundle
import android.os.FileUtils
import android.util.Log
import android.widget.Toast
import org.json.CDL;
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.pinkfry.tech.mysteryshopper.Adapter.ClientStoreAdapter
import com.pinkfry.tech.mysteryshopper.R
import com.pinkfry.tech.mysteryshopper.model.AnsGivenModel
import com.pinkfry.tech.mysteryshopper.model.ModelExportData
import com.pinkfry.tech.mysteryshopper.model.SingleStore
import kotlinx.android.synthetic.main.activity_add_store.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.File
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList


class ShowStoreActivity : AppCompatActivity() {
lateinit var keyArray:ArrayList<String>;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_store)
        keyArray= ArrayList()
        var clientName=intent.getStringExtra("name")
        var total=intent.getIntExtra("total",0)
        supportActionBar!!.title = clientName
        var arrayList=ArrayList<SingleStore>()
        var calendar=Calendar.getInstance();
        var date="${calendar.get(Calendar.DATE)}${calendar.get(Calendar.MONTH)}${calendar.get(Calendar.YEAR)}"
        rvStore.layoutManager= LinearLayoutManager(this) as RecyclerView.LayoutManager?
        val adapter=ClientStoreAdapter(arrayList,this,clientName,total,date)
        rvStore.adapter=adapter
        fabAddStore.setOnClickListener {
            val intent=Intent(this,AddNewStoreActivity::class.java)
            intent.putExtra("name",clientName)
            intent.putExtra("total",total)
            startActivity(intent)
        }
        btnAddQuestions.setOnClickListener {
            if(arrayList.size!=0) {
                var intent = Intent(this@ShowStoreActivity, QuestionAddingActivity::class.java)
                intent.putExtra("clientName", clientName)
                intent.putStringArrayListExtra("keyArray", keyArray)
                intent.putExtra("total", total)
                startActivity(intent)
            }
            else{
                Toast.makeText(this,"Please Add A Store first",Toast.LENGTH_SHORT).show()
            }
        }
            btnCreateJson.setOnClickListener {
                getExportedData(arrayList)
            }

        var dref=FirebaseDatabase.getInstance().reference.child(resources.getString(R.string.FirebaseClient)).child(clientName)
        dref.child(resources.getString(R.string.firebaseStore)).addValueEventListener(object :ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                arrayList.clear()
               for(snapshot in dataSnapshot.children){
                   keyArray.add(snapshot.key.toString())
                   arrayList.add(snapshot.getValue(SingleStore::class.java)!!)
                   adapter.notifyDataSetChanged()
               }
            }
        })
    }

    fun getTheDataset( storeArrayList:ArrayList<SingleStore>){
        var stringarry= arrayListOf("06022020","07022020")
        var mapList= hashMapOf<String,ArrayList<AnsGivenModel>>()
        var finalJson=JSONArray()
        var ansGivenArrayList=ArrayList<AnsGivenModel>()
        for(value in storeArrayList) {
            var upperAnsGivenArrayList=value.AnsGiven
            var dateJsonArray=JSONObject()
            for(k in stringarry) {

                var shortByDateJsonArray=JSONArray()
                for (i in 0 until upperAnsGivenArrayList.size) {
                    var singleDateJson=JSONObject()
                    var ansGiven = storeArrayList[0].AnsGiven[i].shortedByDate["06022020"]
//                    ansGiven?.let { ansGivenArrayList.add(it) }
                    var ansJsonObject=JSONArray()
                    for(element in ansGiven!!.ans) {
                        ansJsonObject.put(element)
                    }
                    singleDateJson.put("ans",ansJsonObject)
                    singleDateJson.put("value",ansGiven.value)
                    shortByDateJsonArray.put(singleDateJson)
                }

//                mapList[k] = ansGivenArrayList
//                ansGivenArrayList.clear()
                dateJsonArray.put(k,shortByDateJsonArray)
                Log.d("SSA",dateJsonArray.toString())
            }
            var storeJsonObject=JSONObject()
            storeJsonObject.put(value.name,dateJsonArray)

            finalJson.put(storeJsonObject)
        }
        var clientJsonObject=JSONObject()
        clientJsonObject.put("anant",finalJson)

     Log.d("SSA",clientJsonObject.toString())

        


    }
    fun getExportedData(storeArrayList:ArrayList<SingleStore>){
        var name="Anant"
        var store="NautiyalJi"
        var date=""
        var question="1"
        var arrayListOfData=ArrayList<ModelExportData>()
        var ans=""
        var innerJsonArray=JSONArray()
        var stringarry= arrayListOf("712020")
        for(value in storeArrayList) {
            var upperAnsGivenArrayList=value.AnsGiven
            var dateJsonArray=JSONObject()
            for(k in stringarry) {
                date=k
                for (i in 0 until upperAnsGivenArrayList.size) {
                    var singleDateJson=JSONObject()
                    var ansGiven = storeArrayList[0].AnsGiven[i].shortedByDate[date]

                    Log.d("SSA",ansGiven.toString())
                    if(ansGiven!=null) {
                        for (element in ansGiven.ans) {
//                        ansJsonObject.put(element)
                            ans += "$element   *   "
                        }
                        ans+=ansGiven.value
                    }
//                    singleDateJson.put("ans",ansJsonObject)

//                    singleDateJson.put("value",ansGiven.value)

//                    shortByDateJsonArray.put(singleDateJson)
                    question=i.toString()
                    arrayListOfData.add(ModelExportData(name,store,question,date,ans))

                    ans=""
                }

//                mapList[k] = ansGivenArrayList
//                ansGivenArrayList.clear()
//                dateJsonArray.put(k,shortByDateJsonArray)


            }
//            var storeJsonObject=JSONObject()

//            storeJsonObject.put(value.name,dateJsonArray)
            store=value.name


            for(model in arrayListOfData)
            {
                var innerJsonObject=JSONObject()
                innerJsonObject.put("clientName",model.clientName)
                innerJsonObject.put("storeName",model.storeName)
                innerJsonObject.put("question",model.question)
                innerJsonObject.put("date",model.date)
                innerJsonObject.put("ans",model.ans)
                innerJsonArray.put(innerJsonObject)

            }//            finalJson.put(storeJsonObject)
            Log.d("SSA",innerJsonArray.toString())
        }
        val output: JSONObject
        try {
//            output = JSONObject(jsonString)
            val docs =innerJsonArray
            val file = File("/tmp2/fromJSON.csv")
            val csv: String = CDL.toString(docs)
            Log.d("SSA",csv)
//            FileUtils.writeStringToFile(file, csv)
        } catch (e: JSONException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }
}
