package com.pinkfry.tech.mysteryshopper.Activity

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.pinkfry.tech.mysteryshopper.Adapter.QuestionAdapter
import com.pinkfry.tech.mysteryshopper.Fragment.BottomSheetGalleryFragment
import com.pinkfry.tech.mysteryshopper.R
import com.pinkfry.tech.mysteryshopper.model.*
import kotlinx.android.synthetic.main.activity_quiz_show.*
import java.lang.reflect.Type
import kotlin.reflect.typeOf


class QuizShowActivity : AppCompatActivity() {
    companion object{
//        lateinit var ansToSendArrayList:ArrayList<AnsGivenModel>
        lateinit var arraylistGot:ArrayList<SIngleResponseModel>
//        lateinit var ansArray: ArrayList<AnsGivenModel>
        lateinit var lastResponseList:HashMap<String,ArrayList<SIngleResponseModel>>
        lateinit var singleResponse:SIngleResponseModel
        lateinit var date:String

        var latestDate="2019-2-16"

    }
    var idPositionOfLastResponse=0
    var isEditing=false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz_show)
        var storeName=intent.getStringExtra("storeName")
        val clientName=intent.getStringExtra("clientName")
         date=intent.getStringExtra("date")!!
        val ansToSend=intent.getStringExtra("ansToSend")
        var totalClient=intent.getIntExtra("totalClient",0)
         if(ansToSend.equals("null")) {
             arraylistGot = ArrayList()
         }
        else {
             val gson = Gson()
             val type: Type = object : TypeToken<ArrayList<SIngleResponseModel?>?>() {}.type
             arraylistGot = gson.fromJson<ArrayList<SIngleResponseModel>>(ansToSend, type)
         }
        lastResponseList= hashMapOf()
        singleResponse= SIngleResponseModel()

        supportActionBar!!.title = storeName
        var dref= FirebaseDatabase.getInstance().reference.child(resources.getString(R.string.FirebaseClient)).child(clientName).child("Questions")
            var questionArrayList=ArrayList<QuestionsModel>()
        var questionKeyArrayList=ArrayList<String>()


        dref.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                questionArrayList.clear()
                for(snapshot in dataSnapshot.children){
                    questionArrayList.add(snapshot.getValue(QuestionsModel::class.java)!!)
                     questionKeyArrayList.add(snapshot.key.toString())
                    singleResponse.eachAns.add(AnsGivenModel())
                    Log.d("QSA","$questionArrayList")

                }
                Log.d("QSA", "${singleResponse.eachAns.size}")
                var questionAdapter=QuestionAdapter(questionArrayList,this@QuizShowActivity,clientName,storeName,
                    this@QuizShowActivity,null)
                if(questionArrayList.size==0){
                    btnSubmit.isClickable=false
                    btnSubmit.alpha=0.5f
                    btnEditPreviousResponse.alpha=0.5f
                    btnEditPreviousResponse.isClickable=false
                }
                else{
                    btnSubmit.isClickable=true
                    btnSubmit.alpha=1f
                    btnEditPreviousResponse.alpha=1f
                    btnEditPreviousResponse.isClickable=true
                }
                rvQuizQuestions.layoutManager=LinearLayoutManager(this@QuizShowActivity)
                rvQuizQuestions.adapter=questionAdapter

            }
        })

        btnEditPreviousResponse.setOnClickListener {
            isEditing=true
           val dbref = FirebaseDatabase.getInstance().reference
                .child(resources.getString(R.string.FirebaseClient)).child(clientName!!)
                .child(resources.getString(R.string.firebaseStore)).child(storeName!!)
                    dbref.child(resources.getString(R.string.ansGiven)).addValueEventListener(object:ValueEventListener{
                    override fun onCancelled(p0: DatabaseError) {
                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        if (p0.exists()) {
                            for (snapshot in p0.children) {
                                var arraylist = ArrayList<SIngleResponseModel>()
                                for (element in snapshot.children) {
                                    arraylist.add(element.getValue(SIngleResponseModel::class.java)!!)
                                }
                                lastResponseList[snapshot.key.toString()] = arraylist
                            }
                            singleResponse = getLastResponse(lastResponseList)
                            for (elementVal in singleResponse.eachAns.size until questionArrayList.size) {
                                singleResponse.eachAns.add(AnsGivenModel())
                            }
                            val questionAdapter = QuestionAdapter(
                                questionArrayList, this@QuizShowActivity, clientName, storeName,
                                this@QuizShowActivity,
                                singleResponse
                            )
                            rvQuizQuestions.layoutManager = LinearLayoutManager(this@QuizShowActivity)
                            rvQuizQuestions.adapter = questionAdapter


                            dbref.removeEventListener(this)
                        }
                        else{
                            Toast.makeText(this@QuizShowActivity,resources.getString(R.string.noPreviousResponse),Toast.LENGTH_SHORT).show()
                        }
                    }
                })
        }
        btnSubmit.setOnClickListener {
            singleResponse.date= date
            if(isEditing){
              FirebaseDatabase.getInstance().reference
                    .child(resources.getString(R.string.FirebaseClient)).child(clientName!!)
                    .child(resources.getString(R.string.firebaseStore)).child(storeName!!)
                    .child(resources.getString(R.string.ansGiven))
                    .child(date).child(idPositionOfLastResponse.toString()).setValue(singleResponse)
                  .addOnSuccessListener {
                      Toast.makeText(this, resources.getString(R.string.successfullyAdded), Toast.LENGTH_SHORT).show()
                      finish()
                  }.addOnFailureListener {
                      Toast.makeText(this, resources.getString(R.string.failed), Toast.LENGTH_SHORT).show()
                  }
            }
            else {
                arraylistGot.add(singleResponse)
                dref = FirebaseDatabase.getInstance().reference
                    .child(resources.getString(R.string.FirebaseClient)).child(clientName!!)
                    .child(resources.getString(R.string.firebaseStore)).child(storeName!!)
                dref.child("totalClient").setValue(totalClient + 1)
                dref.child(resources.getString(R.string.ansGiven)).child(date).setValue(arraylistGot)
                    .addOnSuccessListener {
                        Toast.makeText(this, resources.getString(R.string.successfullyAdded), Toast.LENGTH_SHORT).show()
                        finish()
                    }.addOnFailureListener {
                    Toast.makeText(this, resources.getString(R.string.failed), Toast.LENGTH_SHORT).show()
                }
            }
        }



        imageGallery.setOnClickListener{
            val fragmentManager=supportFragmentManager.beginTransaction()

            val bottomFragment=BottomSheetGalleryFragment()
            bottomFragment.setClientName(clientName)
            bottomFragment.setStoreName(storeName)
           fragmentManager.add(bottomFragment,"bottomFragment")
            fragmentManager.commit()
        }
    }

    fun getLastResponse(shortedByDate:HashMap<String,ArrayList<SIngleResponseModel>>): SIngleResponseModel{
        latestDate="2019-2-16"
        var isThereAnyElement:Int=-1
        for(element in shortedByDate)
        {
            if(element.key.compareTo(latestDate)==1)
            latestDate=element.key
            isThereAnyElement=1

        }
        if(isThereAnyElement==1) {
            date = latestDate
            Log.d("QSA", latestDate)
            idPositionOfLastResponse=shortedByDate[date]!!.size-1
            Log.d("QSA", "$idPositionOfLastResponse")
            Log.d("QSA", shortedByDate.toString())
            return shortedByDate[date]!![idPositionOfLastResponse]
        }
        else {
            Toast.makeText(this,resources.getString(R.string.noPreviousResponse),Toast.LENGTH_SHORT).show()
            return SIngleResponseModel()
        }

    }
}
