package com.pinkfry.tech.mysteryshopper.Activity

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.google.gson.Gson
import com.pinkfry.tech.mysteryshopper.Adapter.ClientStoreAdapter
import com.pinkfry.tech.mysteryshopper.R
import com.pinkfry.tech.mysteryshopper.model.ModelExportData
import com.pinkfry.tech.mysteryshopper.model.SingleStore
import kotlinx.android.synthetic.main.activity_add_store.*
import org.json.CDL
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.File
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList


class ShowStoreActivity : AppCompatActivity() {
lateinit var keyArray:ArrayList<String>
    lateinit var arrayList:ArrayList<SingleStore>
    private lateinit var clientName:String
    private lateinit var alertDialog: AlertDialog.Builder
    lateinit var resetRef:DatabaseReference
    companion object{
        var total:Int = 0
    }
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_store)

        keyArray= ArrayList()
         clientName=intent.getStringExtra("name")!!
         total=intent.getIntExtra("total",0)
        var questionList=intent.getStringExtra("questionList")
        createAlerDialog()
        var dref=FirebaseDatabase.getInstance().reference.child(resources.getString(R.string.FirebaseClient)).child(clientName)
        toolbar.title = clientName
          toolbar.inflateMenu(R.menu.menu_store_option)
        setSupportActionBar(toolbar)
        window.statusBarColor= getColor(R.color.colorPrimaryDark)
          arrayList= ArrayList()
        var calendar=Calendar.getInstance();
        var date="${calendar.get(Calendar.YEAR)}-${calendar.get(Calendar.MONTH)+1}-${calendar.get(Calendar.DATE)}"
        rvStore.layoutManager= LinearLayoutManager(this) as RecyclerView.LayoutManager?
        val adapter=ClientStoreAdapter(arrayList,this@ShowStoreActivity,clientName,total,date)
        rvStore.adapter=adapter
        fabAddStore.setOnClickListener {
            val intent=Intent(this,AddNewStoreActivity::class.java)
            intent.putExtra("name",clientName)
            startActivity(intent)
        }
//        btnResetAll.setOnClickListener {
//        resetRef= dref.child(resources.getString(R.string.firebaseStore))
//            resetRef.addValueEventListener(object :ValueEventListener{
//             override fun onCancelled(p0: DatabaseError) {
//            Toast.makeText(this@ShowStoreActivity,"Failed, No internet Connectivity",Toast.LENGTH_SHORT).show()
//                 resetRef.removeEventListener(this)
//             }
//
//             override fun onDataChange(dataSnapshot: DataSnapshot) {
//               for(snapshot in dataSnapshot.children){
//                   var storeModel=snapshot.getValue(SingleStore::class.java)
//                   if(storeModel!=null) {
//                       for ((index, element) in storeModel.AnsGiven.withIndex()) {
//                           resetRef.child(snapshot.key.toString())
//                               .child(resources.getString(R.string.ansGiven)).child(index.toString())
//                               .child("shortedByDate").setValue(null)
//                       }
//                       resetRef.child(snapshot.key.toString()).child("totalClient").setValue(0)
//                   }
//
//                   resetRef.removeEventListener(this)
//
//               }
//             }
//         })
//        }
        btnAddQuestions.setOnClickListener {
            if(arrayList.size!=0) {
                var intent = Intent(this@ShowStoreActivity, QuestionAddingActivity::class.java)
                intent.putExtra("clientName", clientName)
                intent.putStringArrayListExtra("keyArray", keyArray)

                startActivity(intent)
            }
            else{
                Toast.makeText(this,"Please Add A Store first",Toast.LENGTH_SHORT).show()
            }
        }


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


//    fun getExportedData(storeArrayList:ArrayList<SingleStore>){
//        var name="Anant"
//        var store="NautiyalJi"
//        var date=""
//        var question="1"
//        var arrayListOfData=ArrayList<ModelExportData>()
//        var ans=""
//        var innerJsonArray=JSONArray()
//        var stringarry= arrayListOf("712020")
//        for(value in storeArrayList) {
//            var upperAnsGivenArrayList=value.AnsGiven
//            var dateJsonArray=JSONObject()
//            for(k in stringarry) {
//                date=k
//                for (i in 0 until upperAnsGivenArrayList.size) {
//                    var singleDateJson=JSONObject()
//                    var ansGiven = storeArrayList[0].AnsGiven[i].shortedByDate[date]
//
//                    Log.d("SSA",ansGiven.toString())
//                    if(ansGiven!=null) {
//                        for (element in ansGiven.ans) {
////                        ansJsonObject.put(element)
//                            ans += "$element   *   "
//                        }
//                        ans+=ansGiven.value
//                    }
////                    singleDateJson.put("ans",ansJsonObject)
//
////                    singleDateJson.put("value",ansGiven.value)
//
////                    shortByDateJsonArray.put(singleDateJson)
//                    question=i.toString()
//                    arrayListOfData.add(ModelExportData(name,store,question,date,ans))
//
//                    ans=""
//                }
//
////                mapList[k] = ansGivenArrayList
////                ansGivenArrayList.clear()
////                dateJsonArray.put(k,shortByDateJsonArray)
//
//
//            }
////            var storeJsonObject=JSONObject()
//
////            storeJsonObject.put(value.name,dateJsonArray)
//            store=value.name
//
//
//            for(model in arrayListOfData)
//            {
//                var innerJsonObject=JSONObject()
//                innerJsonObject.put("clientName",model.clientName)
//                innerJsonObject.put("storeName",model.storeName)
//                innerJsonObject.put("question",model.question)
//                innerJsonObject.put("date",model.date)
//                innerJsonObject.put("ans",model.ans)
//                innerJsonArray.put(innerJsonObject)
//
//            }//            finalJson.put(storeJsonObject)
//            Log.d("SSA",innerJsonArray.toString())
//        }
//        try {
////            output = JSONObject(jsonString)
//            val docs =innerJsonArray
//            var path=Environment.getExternalStorageDirectory().absolutePath+"/MysteryShopper/"
//            val file = File(path)
//            if (!file.exists()) {
//                file.mkdirs()
//            }
//            val csv: String = CDL.toString(docs)
//            var timeStamp=System.currentTimeMillis()
//            with(File(file,"excel-$timeStamp.csv")) {
//                writeBytes(csv.toByteArray())
//            }
//
//
//            Log.d("SSA",csv)
//
//            Toast.makeText(this,"Successfully Downloaded Data",Toast.LENGTH_SHORT).show()
//
//        } catch (e: Exception) {
//            e.printStackTrace()
//        } catch (e: IOException) {
//            e.printStackTrace()
//        }
//
//    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==1805){
            if(resultCode== Activity.RESULT_OK)
            {
                Toast.makeText(this,"Permission Accessed Now you can download data",Toast.LENGTH_SHORT).show()

            }
            else{
                Toast.makeText(this,"Permission is required to download the data",Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_store_option, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_downLoad -> {

                if(ContextCompat.checkSelfPermission(this,android.Manifest.permission.WRITE_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED&& ContextCompat.checkSelfPermission(this,android.Manifest.permission.READ_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED)
                {
                    Log.d("SSA","HERE")
//                    getExportedData(arrayList)
                    var intent=Intent(this@ShowStoreActivity,DownloadDataActivity::class.java)
                    val gson = Gson()
                    val jsonToSend = gson.toJson(arrayList)
                    intent.putExtra("singleStore",jsonToSend)
                    intent.putExtra("clientName",clientName)
                    intent.putExtra("total", total)
                    startActivity(intent)
                }
                else{
                    ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE,android.Manifest.permission.READ_EXTERNAL_STORAGE),1805)
                }

                return true
            }
            R.id.actionReset->{
                alertDialog.show()

             return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    fun createAlerDialog(){
        alertDialog= AlertDialog.Builder(this@ShowStoreActivity)
            .setMessage("Do you want to delete all Store of this client")
            .setTitle("Reset")
            .setPositiveButton("Reset"
            ) { dialog, which ->
                resetData()

            }
            .setNegativeButton("Cancel",object : DialogInterface.OnClickListener{
                override fun onClick(dialog: DialogInterface?, which: Int) {

                }
            })
    }
    fun resetData() {
        var dref=FirebaseDatabase.getInstance().reference.child(resources.getString(R.string.FirebaseClient)).child(clientName)
        resetRef= dref.child(resources.getString(R.string.firebaseStore))
        resetRef.addValueEventListener(object :ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                Toast.makeText(this@ShowStoreActivity,"Failed, No internet Connectivity",Toast.LENGTH_SHORT).show()
                resetRef.removeEventListener(this)
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for(snapshot in dataSnapshot.children){
                    resetRef.child(snapshot.key.toString()).child(resources.getString(R.string.ansGiven)).setValue(null)
//                    var storeModel=snapshot.getValue(SingleStore::class.java)
//                    if(storeModel!=null) {
//                        for ((index, element) in storeModel.AnsGiven.withIndex()) {
//                            resetRef.child(snapshot.key.toString())
//                                .child(resources.getString(R.string.ansGiven)).child(index.toString())
//                                .child("shortedByDate").setValue(null)
//                        }
//                        resetRef.child(snapshot.key.toString()).child("totalClient").setValue(0).addOnSuccessListener {
//                            Toast.makeText(this@ShowStoreActivity,"Reset Successful",Toast.LENGTH_SHORT).show()
//
//                        }.addOnFailureListener {
//                            Toast.makeText(this@ShowStoreActivity,"Reset Failed, It may be due to bad Internet Connectivity",Toast.LENGTH_SHORT).show()
//
//                        }
//                    }
                }
                resetRef.removeEventListener(this)
                Toast.makeText(this@ShowStoreActivity,"Reset Successful",Toast.LENGTH_SHORT).show()
            }
        })


    }
}
