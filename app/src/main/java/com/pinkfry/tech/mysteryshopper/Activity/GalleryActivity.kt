package com.pinkfry.tech.mysteryshopper.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.pinkfry.tech.mysteryshopper.Adapter.GalleryImageAdapter
import com.pinkfry.tech.mysteryshopper.R
import com.pinkfry.tech.mysteryshopper.model.GalleryModel
import kotlinx.android.synthetic.main.activity_gallery.*

class GalleryActivity : AppCompatActivity() {
    lateinit var alertDialog: AlertDialog.Builder
private  val arrayList=ArrayList<GalleryModel>()
    private  val keyArrayList=ArrayList<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)
        val clientName=intent.getStringExtra("clientName")
        val storeName=intent.getStringExtra("storeName")
        createAlerDialog(clientName!!,storeName!!)
       tvNothingToShow.visibility=View.VISIBLE
        val galleryImageAdapter=GalleryImageAdapter(arrayList,keyArrayList,this,this@GalleryActivity,clientName,storeName)
        rvGalleryImage.layoutManager=GridLayoutManager(this,4)
        rvGalleryImage.adapter=galleryImageAdapter
        val dref=FirebaseDatabase.getInstance().reference.child(resources.getString(R.string.FirebaseClient)).child(clientName).child(resources.getString(R.string.firebaseStore)).child(storeName)
        dref.child("gallery").addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                arrayList.clear()
                keyArrayList.clear()
                for(snapshot in p0.children) {
                    keyArrayList.add(snapshot.key.toString())
                 arrayList.add(snapshot.getValue(GalleryModel::class.java)!!)
                }
                if(arrayList.size!=0){
                    tvNothingToShow.visibility= View.GONE
                }
                galleryImageAdapter.notifyDataSetChanged()
            }
        })
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
                alertDialog.show()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    fun createAlerDialog(clientName:String,storeName:String){
        alertDialog= AlertDialog.Builder(this@GalleryActivity)
            .setMessage(resources.getString(R.string.deleteImage))
            .setTitle(resources.getString(R.string.resetAll))
            .setPositiveButton(resources.getString(R.string.resetAll)
            ) { dialog, which ->
                var dref=FirebaseDatabase.getInstance().reference.child(resources.getString(R.string.FirebaseClient)).child(clientName).child(resources.getString(R.string.firebaseStore)).child(storeName).child("gallery")
                    .setValue(null)

            }
            .setNegativeButton(resources.getString(R.string.cancel)) { dialog, which -> }
    }
}
