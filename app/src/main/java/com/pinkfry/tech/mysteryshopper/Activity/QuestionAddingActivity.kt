package com.pinkfry.tech.mysteryshopper.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.FirebaseDatabase
import com.pinkfry.tech.mysteryshopper.Adapter.OptionAdapter
import com.pinkfry.tech.mysteryshopper.R
import com.pinkfry.tech.mysteryshopper.model.OptionModels
import com.pinkfry.tech.mysteryshopper.model.QuestionsModel
import kotlinx.android.synthetic.main.activity_question_adding.*

class QuestionAddingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question_adding)

        var optionArrayList=ArrayList<OptionModels>()
        val clientName=intent.getStringExtra("clientName")
        val keyArray=intent.getStringArrayListExtra("keyArray")
        var total=intent.getIntExtra("total",0)
        val dref=FirebaseDatabase.getInstance().reference.child(resources.getString(R.string.FirebaseClient)).child(clientName)
            val clientRef=dref.child("Questions")
        val keyRef=dref.child(resources.getString(R.string.firebaseStore))
        rvOptions.layoutManager=LinearLayoutManager(this)
        var optionAdapter=OptionAdapter(optionArrayList)
        rvOptions.adapter=optionAdapter
        btnAddOptions.setOnClickListener {
            var optionModels=OptionModels(etOptions.text.toString(),etValue.text.toString().toInt())
            optionArrayList.add(optionModels)
            optionAdapter.notifyDataSetChanged()
        }
        btnAddQuestions.setOnClickListener {
           var questionModels=QuestionsModel(etQuestion.text.toString(),optionArrayList)
            clientRef.push().setValue(questionModels)
            for( key in keyArray)
            {
             keyRef.child(key).child(resources.getString(R.string.ansGiven)).child( "$total").setValue(0)
                total += 1;
                dref.child("total").setValue(total)
            }

        }

    }
}
