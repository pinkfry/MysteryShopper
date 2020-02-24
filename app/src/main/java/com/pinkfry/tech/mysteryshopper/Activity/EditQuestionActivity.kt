package com.pinkfry.tech.mysteryshopper.Activity

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.pinkfry.tech.mysteryshopper.Adapter.OptionAdapter
import com.pinkfry.tech.mysteryshopper.R
import com.pinkfry.tech.mysteryshopper.model.OptionModels
import com.pinkfry.tech.mysteryshopper.model.QuestionsModel
import kotlinx.android.synthetic.main.activity_edit_question.*
import kotlinx.android.synthetic.main.activity_question_adding.*
import kotlinx.android.synthetic.main.activity_question_adding.view.*
import java.lang.reflect.Type

class EditQuestionActivity : AppCompatActivity() {
    lateinit var clientName: String
    var position: Int = 0
    private lateinit var alertDialog: AlertDialog.Builder
    lateinit var dref: DatabaseReference
    @SuppressLint("InflateParams")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_question)
//        var key=intent.getStringExtra("key")
        var questionModel = intent.getStringExtra("questionModel")
        clientName = intent.getStringExtra("clientName")!!
        position = intent.getIntExtra("position", 0)
        val gson = Gson()
        val type: Type = object : TypeToken<QuestionsModel>() {}.type
        var model = gson.fromJson<QuestionsModel>(questionModel, type)
        var layoutInflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = layoutInflater.inflate(R.layout.activity_question_adding, null, false)
        frameLayout.addView(view)
         dref= FirebaseDatabase.getInstance().reference.child(resources.getString(R.string.FirebaseClient)).child(clientName)
        showAlertDialogBox()
        when (model.type) {
            1 -> {

                view.linearQuestionSecond.visibility = View.GONE
                view.linearQuestionThird.visibility = View.GONE
                view.linearQuestionFourth.visibility = View.GONE
                view.etQuestion.setText(model.Question)
                view.btnAddQuestions.text = resources.getString(R.string.editQuestion)
                var optionArrayList = model.Options
                view.rvOptions.layoutManager = LinearLayoutManager(this)
                var optionAdapter = OptionAdapter(optionArrayList)
                rvOptions.adapter = optionAdapter
                view.btnAddOptions.setOnClickListener {
                    if (etOptions.text.toString().isNotEmpty() && etValue.text.toString().isNotEmpty()) {
                        var optionModels = OptionModels(etOptions.text.toString(), etValue.text.toString().toInt())
                        optionArrayList.add(optionModels)
                        etOptions.setText("")
                        etValue.setText("")
                        optionAdapter.notifyDataSetChanged()
                    } else {
                        Toast.makeText(this, resources.getString(R.string.pleaseEnterOption), Toast.LENGTH_SHORT).show()
                    }
                }
                view.btnAddQuestions.setOnClickListener {
                    Log.d("EQA", position.toString())
                    dref.child("Questions").child("$position")
                        .setValue(QuestionsModel(view.etQuestion.text.toString(), optionArrayList, 1, 1))
                        .addOnSuccessListener {
                            Toast.makeText(this@EditQuestionActivity, resources.getString(R.string.updatedSuccessfully), Toast.LENGTH_SHORT)
                                .show()
                            finish()
                        }
//                    editWholeStore(view.etQuestion.text.toString())
                }
                view.btnDeleteQuestion.setOnClickListener {
                    //                    dref.child("Questions").child("$position").child("visible").setValue(0)
//                    deleteQuestion()
                    alertDialog.show()
                }


            }
            2 -> {
                view.linearQuestionFirst.visibility = View.GONE
                view.linearQuestionThird.visibility = View.GONE
                view.linearQuestionFourth.visibility = View.GONE
                view.etDateQuestion.setText(model.Question)
                view.btnAddDateQuestions.text = resources.getString(R.string.editQuestion)
                view.btnAddDateQuestions.setOnClickListener {
                    Log.d("EQA", position.toString()!!)
                    dref.child("Questions").child("$position")
                        .setValue(QuestionsModel(view.etDateQuestion.text.toString(), ArrayList(), 2, 1))
                        .addOnSuccessListener {
                            Toast.makeText(this@EditQuestionActivity, resources.getString(R.string.updatedSuccessfully), Toast.LENGTH_SHORT)
                                .show()
                            finish()
                        }
//                    editWholeStore(view.etDateQuestion.text.toString())
                }
                view.btnDeleteDateQuestion.setOnClickListener {
                    //                    dref.child("Questions").child("$position").setValue(QuestionsModel(view.etDateQuestion.text.toString(),ArrayList(),2,0))
//                     deleteQuestion();
                    alertDialog.show()
                }

            }
            3 -> {
                view.linearQuestionFourth.visibility = View.GONE
                view.linearQuestionFirst.visibility = View.GONE
                view.linearQuestionSecond.visibility = View.GONE
                view.etTimeQuestion.setText(model.Question)
                view.btnAddTimeQuestions.text = resources.getString(R.string.editQuestion)
                view.btnAddTimeQuestions.setOnClickListener {
                    Log.d("EQA", position.toString()!!)
                    dref.child("Questions").child("$position")
                        .setValue(QuestionsModel(view.etTimeQuestion.text.toString(), ArrayList(), 3, 1))
                        .addOnSuccessListener {
                            Toast.makeText(this@EditQuestionActivity, resources.getString(R.string.updatedSuccessfully), Toast.LENGTH_SHORT)
                                .show()
                            finish()
                        }
//                    editWholeStore(view.etTimeQuestion.text.toString())
                }
                view.btnDeleteTimeQuestion.setOnClickListener {
//                                        dref.child("Questions").child("$position").setValue(QuestionsModel(view.etTimeQuestion.text.toString(),ArrayList(),3,0))
//                    deleteQuestion();
                    alertDialog.show()
                }
            }
            4 -> {
                view.linearQuestionFirst.visibility = View.GONE
                view.linearQuestionSecond.visibility = View.GONE
                view.linearQuestionThird.visibility = View.GONE
                view.etInputFieldQuestion.setText(model.Question)
                view.btnInputFieldQuestion.text = resources.getString(R.string.editQuestion)
                view.btnInputFieldQuestion.setOnClickListener {
                    Log.d("EQA", position.toString()!!)
                    dref.child("Questions").child("$position")
                        .setValue(QuestionsModel(view.etInputFieldQuestion.text.toString(), ArrayList(), 4, 1))
                        .addOnSuccessListener {
                            Toast.makeText(this@EditQuestionActivity, resources.getString(R.string.updatedSuccessfully), Toast.LENGTH_SHORT)
                                .show()
                            finish()
                        }
//                    editWholeStore(view.etInputFieldQuestion.text.toString())
                }
                view.btnDeleteInputQuestion.setOnClickListener {
                    //                    dref.child("Questions").child("$position").setValue(QuestionsModel(view.etInputFieldQuestion.text.toString(),ArrayList(),4,0))
//                    deleteQuestion()
                    alertDialog.show()
                }
            }
        }

    }

    fun editWholeStore(value: String) {
        var dref = FirebaseDatabase.getInstance().reference.child(resources.getString(R.string.FirebaseClient))
            .child(clientName)

        var endDref = dref.child(resources.getString(R.string.firebaseStore))
        endDref.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
//                Toast.makeText(this,)
                endDref.removeEventListener(this)
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {

                for (snapshot in dataSnapshot.children) {
                    endDref.child(snapshot.key.toString()).child(resources.getString(R.string.ansGiven))
                        .child(position.toString()).child("question").setValue(value)
                }
                Toast.makeText(this@EditQuestionActivity, resources.getString(R.string.updatedSuccessfully), Toast.LENGTH_SHORT)
                    .show()
                endDref.removeEventListener(this)
            }
        })

    }

    fun deleteQuestion() {
        var dref = FirebaseDatabase.getInstance().reference.child(resources.getString(R.string.FirebaseClient))
            .child(clientName)

        var endDref = dref.child(resources.getString(R.string.firebaseStore))
        endDref.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
//                Toast.makeText(this,)
                endDref.removeEventListener(this)
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {

                for (snapshot in dataSnapshot.children) {
                    endDref.child(snapshot.key.toString()).child(resources.getString(R.string.ansGiven))
                        .child(position.toString()).child("visible").setValue(0)
                }
                Toast.makeText(this@EditQuestionActivity, "Question has been successfully updated", Toast.LENGTH_SHORT)
                    .show()
                finish()
                endDref.removeEventListener(this)
            }
        })
    }

    fun showAlertDialogBox() {
        alertDialog = AlertDialog.Builder(this)
            .setMessage(resources.getString(R.string.doDeleteQuestion))
            .setTitle(resources.getString(R.string.deleteQuestions))
            .setPositiveButton(resources.getString(R.string.delete)) { dialog, which ->
                dref.child("Questions").child("$position").child("visible").setValue(0).addOnSuccessListener {
                    Toast.makeText(this@EditQuestionActivity, resources.getString(R.string.updatedSuccessfully), Toast.LENGTH_SHORT)
                        .show()
                    finish()
                }
//                deleteQuestion()

            }
            .setNegativeButton(resources.getString(R.string.cancel)) { dialog, which -> }
    }

}
