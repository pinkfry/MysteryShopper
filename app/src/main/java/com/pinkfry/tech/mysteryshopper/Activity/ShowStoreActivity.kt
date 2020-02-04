package com.pinkfry.tech.mysteryshopper.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
lateinit var keyArray:ArrayList<String>;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_store)
        keyArray= ArrayList()
        var clientName=intent.getStringExtra("name")
        var total=intent.getIntExtra("total",0)
        supportActionBar!!.title = clientName
        var arrayList=ArrayList<SingleStore>()
        rvStore.layoutManager= LinearLayoutManager(this) as RecyclerView.LayoutManager?
        val adapter=ClientStoreAdapter(arrayList,this,clientName)
        rvStore.adapter=adapter
        fabAddStore.setOnClickListener {
            var intent=Intent(this,AddNewStoreActivity::class.java)
            intent.putExtra("name",clientName)
            intent.putExtra("total",total)
            startActivity(intent)
        }
        btnAddQuestions.setOnClickListener {
            var intent=Intent(this@ShowStoreActivity,QuestionAddingActivity::class.java)
            intent.putExtra("clientName",clientName)
            intent.putStringArrayListExtra("keyArray",keyArray)
            intent.putExtra("total",total)
            startActivity(intent)
        }

        var dref=FirebaseDatabase.getInstance().reference.child(resources.getString(R.string.FirebaseClient)).child(clientName)
        dref.child(resources.getString(R.string.firebaseStore)).addValueEventListener(object :ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                arrayList.clear()
               for(snapshot in dataSnapshot.children){
                   keyArray.add(snapshot.key.toString())
                   arrayList.add(snapshot.getValue(SingleStore::class.java)!!)
                   adapter.notifyDataSetChanged()
               }
            }
        })
    }
}
