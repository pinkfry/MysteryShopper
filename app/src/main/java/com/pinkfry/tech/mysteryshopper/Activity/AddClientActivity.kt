package com.pinkfry.tech.mysteryshopper.Activity

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.firebase.database.FirebaseDatabase

import androidx.viewpager2.widget.ViewPager2
import com.asaantechnologies.uparkusers.SignInSection.Activity.utils.MyRecyclerViewAdapter
import com.google.firebase.storage.FirebaseStorage
import com.pinkfry.tech.mysteryshopper.R
import com.pinkfry.tech.mysteryshopper.Utils.HorizontalMarginItemDecoration
import kotlinx.android.synthetic.main.activity_add_client.*
import java.lang.Math.abs

class AddClientActivity : AppCompatActivity() {
    companion object{var selectedPosition=0}
    val PICK_REQUEST = 1234
    var mode=0;
    lateinit var filePath: Uri
    var storage = FirebaseStorage.getInstance().reference
    private val databaseReference = FirebaseDatabase.getInstance().reference
    var count = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_client)
        mode=intent.getIntExtra("mode",0)
        var clientName=intent.getStringExtra("clientName")
        var nodeName=intent.getStringExtra("nodeName")
        if(mode==1){
            if(clientName!=null)
            etClientName.setText(clientName)
        }
        var imagesArray = arrayOf(R.drawable.image_first_male,R.drawable.image_first_female,R.drawable.image_second_male,R.drawable.image_second_female,R.drawable.image_third_male,R.drawable.image_third_female,R.drawable.image_forth_male,R.drawable.image_forth_female,R.drawable.image_fifth_male,R.drawable.image_fifth_female)
        viewPager2.adapter = MyRecyclerViewAdapter(imagesArray)

// You need to retain one page on each side so that the next and previous items are visible
        viewPager2.offscreenPageLimit = 1

// Add a PageTransformer that translates the next and previous items horizontally
// towards the center of the screen, which makes them visible
        val nextItemVisiblePx = resources.getDimension(R.dimen.viewpager_next_item_visible)
        val currentItemHorizontalMarginPx = resources.getDimension(R.dimen.viewpager_current_item_horizontal_margin)
        val pageTranslationX = nextItemVisiblePx + currentItemHorizontalMarginPx
        val pageTransformer = ViewPager2.PageTransformer { page: View, position: Float ->
            page.translationX = -pageTranslationX * position
            // Next line scales the item's height. You can remove it if you don't want this effect
            page.scaleY = 1 - (0.25f * kotlin.math.abs(position))
            // If you want a fading effect uncomment the next line:
////             page.alpha = 0.25f + (1 - abs(position))
//            var imageView=page.imageview;

        }
        viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                selectedPosition =position
//                Log.d(TAG, "$selectedPosition+ here")
            }
        })
        viewPager2.setPageTransformer(pageTransformer)

        btnChooseImage.setOnClickListener { choosePhoto() }



// The ItemDecoration gives the current (centered) item horizontal margin so that
// it doesn't occupy the whole screen width. Without it the items overlap
        val itemDecoration = HorizontalMarginItemDecoration(
            this@AddClientActivity,
            R.dimen.viewpager_current_item_horizontal_margin
        )
        viewPager2.addItemDecoration(itemDecoration)
        var dref=FirebaseDatabase.getInstance().reference
        var childAddRef=dref.child(resources.getString(R.string.FirebaseClient))
        btnAddClient.setOnClickListener {
            var name = etClientName.text.toString()
            if (mode == 0) {
                if (name.isNotEmpty()) {
                    childAddRef.child(name).child("imagePosition").setValue(selectedPosition)
                    childAddRef.child(name).child("nodeName").setValue(name)
                    childAddRef.child(name).child("name").setValue(name).addOnSuccessListener {
                        etClientName.setText("")
                        uploadPhoto(name)
                    }.addOnCanceledListener {

                        Toast.makeText(this, "Failed To Added", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "Please Enter the Name", Toast.LENGTH_SHORT).show()
                }
            }
            else{
                if (name.isNotEmpty()) {
                    if (nodeName!=null&& nodeName.isNotEmpty()) {
                        childAddRef.child(nodeName).child("imagePosition").setValue(selectedPosition)
                        childAddRef.child(nodeName).child("name").setValue(name).addOnSuccessListener {
                            etClientName.setText("")
                            uploadPhoto(nodeName)
                        }.addOnCanceledListener {

                            Toast.makeText(this, "Failed To Added", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this, "Please Enter the Name", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }


    private fun choosePhoto() {
        var intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT;
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                filePath = data!!.data!!
                try {
                    val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filePath)
                    imageShowSelectedImage.setImageBitmap(bitmap)

                    count = 1
                } catch (e: Exception) {

                }

            }

        }
    }

    private fun uploadPhoto(name:String) {
        if (count == 1) {
            var childReference = storage.child("IMAGES").child(name.toUpperCase())
                .child(System.currentTimeMillis().toString())
            childReference.putFile(filePath).addOnSuccessListener { it ->
                Toast.makeText(this, "${it.storage.downloadUrl}", Toast.LENGTH_SHORT).show()
                childReference.downloadUrl.addOnSuccessListener {
                    databaseReference.child(resources.getString(R.string.FirebaseClient)).child(name).child("imageUrl").setValue(it.toString())

                }
//                  databaseReference.child(intent.getStringExtra("name")).child("PGPROFILE").child("IMAGES").push().setValue(it.storage.downloadUrl)
                Toast.makeText(this,"Client Successfully Added",Toast.LENGTH_SHORT).show()
                count = 0
            }.addOnProgressListener {
                tvPercentage.text=(it.bytesTransferred/it.totalByteCount*100.0).toString()+"%"

            }.addOnFailureListener {
                Toast.makeText(this,"Failed to add the client",Toast.LENGTH_SHORT).show()
            }
        }
        else{
            Toast.makeText(this,"Client Updated Successfully",Toast.LENGTH_SHORT).show()
        }
    }
}
