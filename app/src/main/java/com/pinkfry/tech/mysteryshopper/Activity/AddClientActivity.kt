package com.pinkfry.tech.mysteryshopper.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.database.FirebaseDatabase
import com.pinkfry.tech.mysteryshopper.R
import kotlinx.android.synthetic.main.activity_add_client.*

class AddClientActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_client)
        var dref=FirebaseDatabase.getInstance().reference
        var childAddRef=dref.child(resources.getString(R.string.FirebaseClient))
        btnAddClient.setOnClickListener {
            var name=etClientName.text.toString()
            if(name.isNotEmpty()){
                childAddRef.child(name).child("name").setValue(name).addOnSuccessListener {
                    Toast.makeText(this,"Client Successfully Added",Toast.LENGTH_SHORT).show()
                }.addOnCanceledListener {
                    Toast.makeText(this,"Failed To Added",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
