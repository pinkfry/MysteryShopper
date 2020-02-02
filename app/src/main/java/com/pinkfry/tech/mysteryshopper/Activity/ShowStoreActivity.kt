package com.pinkfry.tech.mysteryshopper.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.pinkfry.tech.mysteryshopper.Adapter.ClientListAdapter
import com.pinkfry.tech.mysteryshopper.Adapter.ClientStoreAdapter
import com.pinkfry.tech.mysteryshopper.R
import com.pinkfry.tech.mysteryshopper.model.SingleClient
import com.pinkfry.tech.mysteryshopper.model.SingleStore
import kotlinx.android.synthetic.main.activity_add_store.*

class ShowStoreActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_store)
        var name=intent.getStringExtra("name")
        supportActionBar!!.title = name
        var arrayList=ArrayList<String>()
        rvStore.layoutManager=LinearLayoutManager(this)
        val adapter=ClientStoreAdapter(arrayList,this)
        rvStore.adapter=adapter
        fabAddStore.setOnClickListener {
            var intent=Intent(this,AddNewStoreActivity::class.java)
            intent.putExtra("name",name)
            startActivity(intent)
        }

        var dref=FirebaseDatabase.getInstance().reference.child(resources.getString(R.string.FirebaseClient)).child(name)
        dref.child(resources.getString(R.string.firebaseStore)).addValueEventListener(object :ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                arrayList.clear()
               for(snapshot in dataSnapshot.children){
                   arrayList.add(snapshot.getValue(SingleStore::class.java)!!.name)
                   adapter.notifyDataSetChanged()
               }
            }
        })
    }
}
