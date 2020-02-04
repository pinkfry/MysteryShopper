package com.pinkfry.tech.mysteryshopper.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.firebase.database.FirebaseDatabase

import androidx.viewpager2.widget.ViewPager2
import com.asaantechnologies.uparkusers.SignInSection.Activity.utils.MyRecyclerViewAdapter
import com.pinkfry.tech.mysteryshopper.R
import com.pinkfry.tech.mysteryshopper.Utils.HorizontalMarginItemDecoration
import kotlinx.android.synthetic.main.activity_add_client.*
import java.lang.Math.abs

class AddClientActivity : AppCompatActivity() {
    companion object{var selectedPosition=0}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_client)
        var imagesArray = arrayOf<Int>(R.drawable.image_first_male,R.drawable.image_first_female,R.drawable.image_second_male,R.drawable.image_second_female,R.drawable.image_third_male,R.drawable.image_third_female,R.drawable.image_forth_male,R.drawable.image_forth_female,R.drawable.image_fifth_male,R.drawable.image_fifth_female)
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
            var name=etClientName.text.toString()
            if(name.isNotEmpty()){
                childAddRef.child(name).child("imagePosition").setValue(selectedPosition)
                childAddRef.child(name).child("name").setValue(name).addOnSuccessListener {
                    Toast.makeText(this,"Client Successfully Added",Toast.LENGTH_SHORT).show()
                }.addOnCanceledListener {
                    Toast.makeText(this,"Failed To Added",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
