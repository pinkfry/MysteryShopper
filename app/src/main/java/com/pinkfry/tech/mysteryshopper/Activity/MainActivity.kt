package com.pinkfry.tech.mysteryshopper.Activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.pinkfry.tech.mysteryshopper.Adapter.ClientListAdapter
import com.pinkfry.tech.mysteryshopper.R
import com.pinkfry.tech.mysteryshopper.model.SingleClient

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
       rvClient.layoutManager= LinearLayoutManager(this@MainActivity)
        val arrayList=ArrayList<String>()
        var adapter= ClientListAdapter(arrayList,this@MainActivity)
        rvClient.adapter=adapter
        var dref=FirebaseDatabase.getInstance().reference
        var clientRef=dref.child(resources.getString(R.string.FirebaseClient)).addValueEventListener(object: ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                arrayList.clear()
                Log.d("MAIN","${arrayList.size}")
                for(snapshot in dataSnapshot.children)
                arrayList.add(snapshot.getValue(SingleClient::class.java)!!.name)
                adapter.notifyDataSetChanged()
            }
        })
        fab.setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show()
            var intent=Intent(this@MainActivity, AddClientActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> {
                
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
