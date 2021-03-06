package com.pinkfry.tech.mysteryshopper.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.FirebaseDatabase
import com.pinkfry.tech.mysteryshopper.Activity.ShowStoreActivity.Companion.total
import com.pinkfry.tech.mysteryshopper.Adapter.OptionAdapter
import com.pinkfry.tech.mysteryshopper.R
import com.pinkfry.tech.mysteryshopper.model.AnsGivenModel
import com.pinkfry.tech.mysteryshopper.model.OptionModels
import com.pinkfry.tech.mysteryshopper.model.QuestionsModel
import com.pinkfry.tech.mysteryshopper.model.UpperAnsGivenModel
import kotlinx.android.synthetic.main.activity_question_adding.*

class QuestionAddingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question_adding)

        var optionArrayList = ArrayList<OptionModels>()
        val clientName = intent.getStringExtra("clientName")
        val keyArray = intent.getStringArrayListExtra("keyArray")
//        var total = intent.getIntExtra("total", 0)
        val dref = FirebaseDatabase.getInstance().reference.child(resources.getString(R.string.FirebaseClient))
            .child(clientName)
        val clientRef = dref.child("Questions")
        val keyRef = dref.child(resources.getString(R.string.firebaseStore))
        rvOptions.layoutManager = LinearLayoutManager(this)
        var optionAdapter = OptionAdapter(optionArrayList)
        btnDeleteDateQuestion.visibility= View.GONE
        btnDeleteQuestion.visibility=View.GONE
        btnDeleteTimeQuestion.visibility=View.GONE
        btnDeleteInputQuestion.visibility=View.GONE
        rvOptions.adapter = optionAdapter
        btnAddOptions.setOnClickListener {
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
        btnAddQuestions.setOnClickListener {
            if (etQuestion.text.toString().isNotEmpty()) {
                var questionModels = QuestionsModel(etQuestion.text.toString(), optionArrayList, 1,1)
                clientRef.child("$total").setValue(questionModels).addOnSuccessListener {
                    Toast.makeText(this, resources.getString(R.string.successfullyAdded), Toast.LENGTH_SHORT).show()
                }
                    .addOnFailureListener {
                        Toast.makeText(this, resources.getString(R.string.failed), Toast.LENGTH_SHORT).show()
                    }
                total += 1
                dref.child("total").setValue(total)
                etQuestion.setText("")
                etOptions.setText("")
                etValue.setText("")
                optionArrayList.clear()
                optionAdapter.notifyDataSetChanged()
            } else {
                Toast.makeText(this, resources.getString(R.string.pleaseEnterQuestion), Toast.LENGTH_SHORT).show()
            }

        }
        btnAddDateQuestions.setOnClickListener {
            if (etDateQuestion.text.toString().isNotEmpty()) {
                var questionModels = QuestionsModel(etDateQuestion.text.toString(), ArrayList(), 2,1)
                clientRef.child("$total").setValue(questionModels).addOnSuccessListener {
                    Toast.makeText(this, resources.getString(R.string.successfullyAdded), Toast.LENGTH_SHORT).show()
                }
                    .addOnFailureListener {
                        Toast.makeText(this, resources.getString(R.string.failed), Toast.LENGTH_SHORT).show()
                    }

                total += 1
                dref.child("total").setValue(total)

                etDateQuestion.setText("")

            }
        }
        btnAddTimeQuestions.setOnClickListener {
            if (etTimeQuestion.text.toString().isNotEmpty()) {
                var questionModels = QuestionsModel(etTimeQuestion.text.toString(), ArrayList(), 3,1)
                clientRef.child("$total").setValue(questionModels).addOnSuccessListener {
                    Toast.makeText(this, resources.getString(R.string.successfullyAdded), Toast.LENGTH_SHORT).show()
                }
                    .addOnFailureListener {
                        Toast.makeText(this, resources.getString(R.string.failed), Toast.LENGTH_SHORT).show()
                    }

                total += 1
                dref.child("total").setValue(total)

                etTimeQuestion.setText("")

            }
        }
        btnInputFieldQuestion.setOnClickListener {
            if (etInputFieldQuestion.text.toString().isNotEmpty()) {
                var questionModels = QuestionsModel(etInputFieldQuestion.text.toString(), ArrayList(), 4,1)
                clientRef.child("$total").setValue(questionModels).addOnSuccessListener {
                    Toast.makeText(this, resources.getString(R.string.successfullyAdded), Toast.LENGTH_SHORT).show()
                }
                    .addOnFailureListener {
                        Toast.makeText(this, resources.getString(R.string.failed), Toast.LENGTH_SHORT).show()
                    }

                total += 1
                dref.child("total").setValue(total)

                etInputFieldQuestion.setText("")
            }
        }

    }
}
