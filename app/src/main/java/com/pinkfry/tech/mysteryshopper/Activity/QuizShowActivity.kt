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
import com.pinkfry.tech.mysteryshopper.R
import com.pinkfry.tech.mysteryshopper.model.AnsGivenModel
import com.pinkfry.tech.mysteryshopper.model.QuestionsModel
import com.pinkfry.tech.mysteryshopper.model.UpperAnsGivenModel
import kotlinx.android.synthetic.main.activity_quiz_show.*
import java.lang.reflect.Type


class QuizShowActivity : AppCompatActivity() {
    companion object{
        lateinit var ansToSendArrayList:ArrayList<AnsGivenModel>
        lateinit var arraylistGot:ArrayList<UpperAnsGivenModel>
        lateinit var ansArray: ArrayList<AnsGivenModel>
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz_show)
        var storeName=intent.getStringExtra("storeName")
        val clientName=intent.getStringExtra("clientName")
        val ansToSend=intent.getStringExtra("ansToSend")
        var totalClient=intent.getIntExtra("totalClient",0)
        val gson = Gson()
        val type: Type = object : TypeToken<ArrayList<UpperAnsGivenModel?>?>() {}.type
        arraylistGot=gson.fromJson<ArrayList<UpperAnsGivenModel>>(ansToSend,type)
        ansToSendArrayList= ArrayList()
        for((element,index) in arraylistGot){
            element["06022020"]?.let { ansToSendArrayList.add(it) }
            Log.d("QSA", ansToSendArrayList.toString()+ arraylistGot.size)
        }
        ansArray= ArrayList()

        supportActionBar!!.title = storeName
        var dref= FirebaseDatabase.getInstance().reference.child(resources.getString(R.string.FirebaseClient)).child(clientName).child("Questions")
            var questionArrayList=ArrayList<QuestionsModel>()


        dref.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                questionArrayList.clear()
                for(snapshot in dataSnapshot.children){
                    questionArrayList.add(snapshot.getValue(QuestionsModel::class.java)!!)
                    Log.d("QSA","$questionArrayList")

                }
                for(value in 1..questionArrayList.size)
                {
                    ansArray.add(AnsGivenModel())
                }
                var questionAdapter=QuestionAdapter(questionArrayList,this@QuizShowActivity,clientName,storeName)
                rvQuizQuestions.layoutManager=LinearLayoutManager(this@QuizShowActivity)
                rvQuizQuestions.adapter=questionAdapter

            }
        })
        btnSubmit.setOnClickListener {
            dref = FirebaseDatabase.getInstance().reference
                .child(resources.getString(R.string.FirebaseClient)).child(clientName!!)
                .child(resources.getString(R.string.firebaseStore)).child(storeName!!)
            dref.child("totalClient").setValue(totalClient+1)
            dref.child(resources.getString(R.string.ansGiven)).setValue(getFinalOptionStatus()).addOnSuccessListener {
                Toast.makeText(this,"Successfully Added Response",Toast.LENGTH_SHORT).show()
                finish()
            }.addOnFailureListener{
                Toast.makeText(this,"Unable to connect to internet",Toast.LENGTH_SHORT).show()
            }
        }
    }
    fun getFinalOptionStatus() : ArrayList<UpperAnsGivenModel>{

       for((index,element)in ansToSendArrayList.withIndex())
       {
           var value=element.ans
           value.addAll(ansArray[index].ans)
           var ansGivenModel=AnsGivenModel(value,element.value+ ansArray[index].value)
           Log.d("QSA",ansGivenModel.toString())
           arraylistGot[index].shortedByDate["06022020"] = ansGivenModel
       }
        return arraylistGot
    }
}
