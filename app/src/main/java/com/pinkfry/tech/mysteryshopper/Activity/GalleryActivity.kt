package com.pinkfry.tech.mysteryshopper.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
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
private  val arrayList=ArrayList<GalleryModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)
        val clientName=intent.getStringExtra("clientName")
        val storeName=intent.getStringExtra("storeName")


        val galleryImageAdapter=GalleryImageAdapter(arrayList,this,this@GalleryActivity)
        rvGalleryImage.layoutManager=GridLayoutManager(this,4)
        rvGalleryImage.adapter=galleryImageAdapter
        val dref=FirebaseDatabase.getInstance().reference.child(resources.getString(R.string.FirebaseClient)).child(clientName!!).child(resources.getString(R.string.firebaseStore)).child(storeName!!)
        dref.child("gallery").addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                arrayList.clear()
                for(snapshot in p0.children) {
                 arrayList.add(snapshot.getValue(GalleryModel::class.java)!!)
                }
                if(arrayList.size!=0){
                    tvNothingToShow.visibility= View.GONE
                }
                galleryImageAdapter.notifyDataSetChanged()
            }
        })
    }
}
