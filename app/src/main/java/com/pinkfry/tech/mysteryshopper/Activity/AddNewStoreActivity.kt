package com.pinkfry.tech.mysteryshopper.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.database.FirebaseDatabase
import com.pinkfry.tech.mysteryshopper.R
import com.pinkfry.tech.mysteryshopper.model.ResponseGiven
import kotlinx.android.synthetic.main.activity_add_client.*
import kotlinx.android.synthetic.main.activity_add_new_store.*

class AddNewStoreActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_store)
        val name=intent.getStringExtra("name")
val total=intent.getIntExtra("total",0)
        var dref= FirebaseDatabase.getInstance().reference
        var childAddRef=dref.child(resources.getString(R.string.FirebaseClient)).child(name).child(resources.getString(R.string.firebaseStore))
        btnAddStore.setOnClickListener {
            val name=etStoreName.text.toString()
            val address=etAddress.text.toString()
            val phone=etPhoneNo.text.toString()
            if(name.isNotEmpty()&& address.isNotEmpty()&& phone.isNotEmpty()){
                val AnsGiven=ArrayList<Int>(total)
                for( a in 1..total)
                AnsGiven.add(0)
                Log.d("ANSA",AnsGiven.toString())
                val responseGiven=ResponseGiven(AnsGiven,name,address,phone)
                childAddRef.child(name).setValue(responseGiven).addOnSuccessListener {
                    etStoreName.setText("")
                    etAddress.setText("")
                    etPhoneNo.setText("")
                    Toast.makeText(this,"Store Successfully Added", Toast.LENGTH_SHORT).show()
                }.addOnCanceledListener {
                    Toast.makeText(this,"Failed To Add Store", Toast.LENGTH_SHORT).show()
                }
            }
            else{
                Toast.makeText(this,"Please Fill All the Fields", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
