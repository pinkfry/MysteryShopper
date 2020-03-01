package com.pinkfry.tech.mysteryshopper.Activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.firebase.database.FirebaseDatabase
import com.pinkfry.tech.mysteryshopper.R
import kotlinx.android.synthetic.main.activity_big_image_view.*

class BigImageViewActivity : AppCompatActivity() {
    lateinit var alertDialog: AlertDialog.Builder
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_big_image_view)
        val url=intent.getStringExtra("url")
        var key=intent.getStringExtra("key");
        var storeName=intent.getStringExtra("storeName")
        var clientName=intent.getStringExtra("clientName")
        val title=intent.getStringExtra("title")
        createAlerDialog(clientName!!,storeName!!,key!!)
        Glide.with(this).load(url)
            .thumbnail(0.5f)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(imageFullImage)

        tvTagLine.text = title
    }



    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_big_image, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_delete -> {
                alertDialog.show()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    fun createAlerDialog(clientName:String,storeName:String,key:String){
        alertDialog= AlertDialog.Builder(this@BigImageViewActivity)
            .setMessage(resources.getString(R.string.deleteImage))
            .setTitle(resources.getString(R.string.delete))
            .setPositiveButton(resources.getString(R.string.delete)
            ) { dialog, which ->
                var dref= FirebaseDatabase.getInstance().reference.child(resources.getString(R.string.FirebaseClient)).child(clientName).child(resources.getString(R.string.firebaseStore)).child(storeName)
                        dref.child("gallery").child(key).setValue(null)
                finish()

            }
            .setNegativeButton(resources.getString(R.string.cancel)) { dialog, which -> }
    }


}
