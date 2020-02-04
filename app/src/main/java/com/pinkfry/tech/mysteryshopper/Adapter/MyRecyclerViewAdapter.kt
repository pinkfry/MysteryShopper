package com.asaantechnologies.uparkusers.SignInSection.Activity.utils

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.pinkfry.tech.mysteryshopper.R
import kotlinx.android.synthetic.main.adapter_carasole.view.*

class MyRecyclerViewAdapter(private var imagesArray: Array<Int>) :
    RecyclerView.Adapter<MyRecyclerViewAdapter.MyHolder>() {
    val TAG = "MRVA"
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        var li = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        var view = li.inflate(R.layout.adapter_carasole, parent, false)

        return MyHolder(view)
    }

    override fun getItemCount(): Int {
        return imagesArray.size
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.imageAvatar.setImageResource(imagesArray[position])
    }


    class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageAvatar: ImageView = itemView.imageAvatar

    }
}