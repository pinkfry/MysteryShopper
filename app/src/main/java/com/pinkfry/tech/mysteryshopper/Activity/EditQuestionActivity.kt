package com.pinkfry.tech.mysteryshopper.Activity

import android.annotation.SuppressLint
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.pinkfry.tech.mysteryshopper.R
import com.pinkfry.tech.mysteryshopper.model.OptionModels
import com.pinkfry.tech.mysteryshopper.model.QuestionsModel
import com.pinkfry.tech.mysteryshopper.model.SingleStore
import com.pinkfry.tech.mysteryshopper.model.UpperAnsGivenModel
import kotlinx.android.synthetic.main.activity_edit_question.*
import kotlinx.android.synthetic.main.activity_question_adding.view.*
import kotlinx.android.synthetic.main.adapter_show_questions_4.view.*
import java.lang.reflect.Type

class EditQuestionActivity : AppCompatActivity() {
lateinit var clientName:String
     var position:Int=0
    @SuppressLint("InflateParams")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_question)
//        var key=intent.getStringExtra("key")
        var questionModel=intent.getStringExtra("questionModel")
         clientName=intent.getStringExtra("clientName")!!
        position=intent.getIntExtra("position",0)
        val gson = Gson()
        val type: Type = object : TypeToken<QuestionsModel>() {}.type
        var model =gson.fromJson<QuestionsModel>(questionModel,type)
        var layoutInflater=getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view =layoutInflater.inflate(R.layout.activity_question_adding,null,false)
        frameLayout.addView(view)
        var dref= FirebaseDatabase.getInstance().reference.child(resources.getString(R.string.FirebaseClient)).child(clientName)

        when (model.type) {
            1 -> {

                view.linearQuestionSecond.visibility=View.GONE
                view.linearQuestionThird.visibility=View.GONE
                view.linearQuestionFourth.visibility=View.GONE
                view.etQuestion.setText(model.Question)


            }
            2 -> {
                view.linearQuestionFirst.visibility= View.GONE
                view.linearQuestionThird.visibility=View.GONE
                view.linearQuestionFourth.visibility=View.GONE
                view.etDateQuestion.setText(model.Question)
                view.btnAddDateQuestions.text = "Edit Question"
                view.btnAddDateQuestions.setOnClickListener {
                    Log.d("EQA",position.toString()!!)
                    dref.child("Questions").child("$position").setValue(QuestionsModel(view.etDateQuestion.text.toString(),ArrayList(),2,1))
                    editWholeStore(view.etDateQuestion.text.toString())
                }

            }
            3 -> {
                view.linearQuestionFourth.visibility=View.GONE
                view.linearQuestionFirst.visibility= View.GONE
                view.linearQuestionSecond.visibility=View.GONE
                view.etTimeQuestion.setText(model.Question)
            }
            4 -> {
                view.linearQuestionFirst.visibility= View.GONE
                view.linearQuestionSecond.visibility=View.GONE
                view.linearQuestionThird.visibility=View.GONE
                view.etInputFieldQuestion.setText(model.Question)
            }
        }

    }

    fun editWholeStore(value:String) {
        var dref= FirebaseDatabase.getInstance().reference.child(resources.getString(R.string.FirebaseClient)).child(clientName)

       var endDref= dref.child(resources.getString(R.string.firebaseStore))
        endDref.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
//                Toast.makeText(this,)
            endDref.removeEventListener(this)
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {

                for(snapshot in dataSnapshot.children){
                    endDref.child(snapshot.key.toString()).child(resources.getString(R.string.ansGiven)).child(position.toString()).child("question").setValue(value)
                }
                Toast.makeText(this@EditQuestionActivity,"Question has been successfully updated",Toast.LENGTH_SHORT).show()
                endDref.removeEventListener(this)
            }
        })

    }

    fun deleteWholeStore(value:String){
        var dref= FirebaseDatabase.getInstance().reference.child(resources.getString(R.string.FirebaseClient)).child(clientName)

        var endDref= dref.child(resources.getString(R.string.firebaseStore))
        endDref.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
//                Toast.makeText(this,)
                endDref.removeEventListener(this)
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {

                for(snapshot in dataSnapshot.children){
                    endDref.child(snapshot.key.toString()).child(resources.getString(R.string.ansGiven)).child(position.toString()).child("visible").setValue(0)
                }
                Toast.makeText(this@EditQuestionActivity,"Question has been successfully updated",Toast.LENGTH_SHORT).show()
                endDref.removeEventListener(this)
            }
        })
    }
}
