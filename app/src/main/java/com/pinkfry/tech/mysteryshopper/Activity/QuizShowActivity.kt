package com.pinkfry.tech.mysteryshopper.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.pinkfry.tech.mysteryshopper.Adapter.QuestionAdapter
import com.pinkfry.tech.mysteryshopper.R
import com.pinkfry.tech.mysteryshopper.model.QuestionsModel
import kotlinx.android.synthetic.main.activity_quiz_show.*

class QuizShowActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz_show)
        var storeName=intent.getStringExtra("storeName")
        val clientName=intent.getStringExtra("clientName")
        supportActionBar!!.title = storeName
        val dref= FirebaseDatabase.getInstance().reference.child(resources.getString(R.string.FirebaseClient)).child(clientName).child(resources.getString(R.string.firebaseStore)).child(storeName).child("Questions")
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
                var questionAdapter=QuestionAdapter(questionArrayList,this@QuizShowActivity,dref)
                rvQuizQuestions.layoutManager=LinearLayoutManager(this@QuizShowActivity)
                rvQuizQuestions.adapter=questionAdapter

            }
        })
    }
}
