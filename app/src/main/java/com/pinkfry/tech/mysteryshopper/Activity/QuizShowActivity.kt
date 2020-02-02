package com.pinkfry.tech.mysteryshopper.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.pinkfry.tech.mysteryshopper.R
import com.pinkfry.tech.mysteryshopper.model.QuestionsModel
import com.pinkfry.tech.mysteryshopper.model.SingleStore

class QuizShowActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz_show)
        var storeName=intent.getStringExtra("StoreName")
        val clientName=intent.getStringExtra("clientName")
        supportActionBar!!.title = storeName
        var questionArrayList=ArrayList<QuestionsModel>()
        var dref= FirebaseDatabase.getInstance().reference.child(resources.getString(R.string.FirebaseClient)).child(storeName)
        dref.child(resources.getString(R.string.firebaseStore)).addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                questionArrayList.clear()
                for(snapshot in dataSnapshot.children){
                    questionArrayList.add(snapshot.getValue(QuestionsModel::class.java)!!)
//                    adapter.notifyDataSetChanged()
                }
            }
        })
    }
}
