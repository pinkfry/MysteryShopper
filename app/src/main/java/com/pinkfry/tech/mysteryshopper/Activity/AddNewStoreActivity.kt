package com.pinkfry.tech.mysteryshopper.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.pinkfry.tech.mysteryshopper.Activity.ShowStoreActivity.Companion.total
import com.pinkfry.tech.mysteryshopper.R
import com.pinkfry.tech.mysteryshopper.model.AnsGivenModel
import com.pinkfry.tech.mysteryshopper.model.QuestionsModel
import com.pinkfry.tech.mysteryshopper.model.ResponseGiven
import com.pinkfry.tech.mysteryshopper.model.UpperAnsGivenModel
import kotlinx.android.synthetic.main.activity_add_new_store.*

class AddNewStoreActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_store)

        val name=intent.getStringExtra("name")

        val ansGiven=ArrayList<UpperAnsGivenModel>(total)
        var dref= FirebaseDatabase.getInstance().reference
        var childAddRef=dref.child(resources.getString(R.string.FirebaseClient)).child(name).child(resources.getString(R.string.firebaseStore))
//        dref.child(resources.getString(R.string.FirebaseClient)).child(name).child("Questions").addValueEventListener(object :ValueEventListener{
//            override fun onCancelled(p0: DatabaseError) {
//
//            }
//
//            override fun onDataChange(p0: DataSnapshot) {
//                for(snapshort in p0.children){
//                    var questionModel=snapshort.getValue(QuestionsModel::class.java)
//                    if(questionModel!=null)
//                    ansGiven.add(UpperAnsGivenModel(hashMapOf(),questionModel.type,questionModel.Question,1))
//
//                }
//            }
//        })
        btnAddStore.setOnClickListener {
            val name=etStoreName.text.toString()
            val address=etAddress.text.toString()
            val phone=etPhoneNo.text.toString()
            if(name.isNotEmpty()&& address.isNotEmpty()&& phone.isNotEmpty()){

                for( a in 1..total)

                Log.d("ANSA",ansGiven.toString())
                val responseGiven=ResponseGiven(name,address,phone)
                childAddRef.child(name).setValue(responseGiven).addOnSuccessListener {
                    etStoreName.setText("")
                    etAddress.setText("")
                    etPhoneNo.setText("")
                    Toast.makeText(this,resources.getString(R.string.storeAddedSuccessfully), Toast.LENGTH_SHORT).show()
                }.addOnCanceledListener {
                    Toast.makeText(this,resources.getString(R.string.failed), Toast.LENGTH_SHORT).show()
                }
            }
            else{
                Toast.makeText(this,resources.getString(R.string.fillAllFields), Toast.LENGTH_SHORT).show()
            }
        }
    }
}
