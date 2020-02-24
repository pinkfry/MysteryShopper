package com.pinkfry.tech.mysteryshopper.Activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.pinkfry.tech.mysteryshopper.R
import kotlinx.android.synthetic.main.activity_big_image_view.*

class BigImageViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val url=intent.getStringExtra("url")
        val title=intent.getStringExtra("title")
        setContentView(R.layout.activity_big_image_view)

        Glide.with(this).load(url)
            .thumbnail(0.5f)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(imageFullImage)

        tvTagLine.text = title
    }

}
