package com.pinkfry.tech.mysteryshopper.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.pinkfry.tech.mysteryshopper.Activity.BigImageViewActivity;
import com.pinkfry.tech.mysteryshopper.Activity.GalleryActivity;
import com.pinkfry.tech.mysteryshopper.R;
import com.pinkfry.tech.mysteryshopper.model.GalleryModel;

import java.util.ArrayList;

public class GalleryImageAdapter extends RecyclerView.Adapter<GalleryImageAdapter.MyHolder> {
    ArrayList<GalleryModel> arrayList;
    Context context;
    Activity activity;

    public GalleryImageAdapter(ArrayList<GalleryModel> arrayList,Context context,Activity activity) {
        this.arrayList = arrayList;
        this.context=context;
        this.activity=activity;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater li= (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=li.inflate(R.layout.adapter_gallery_images,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, final int position) {
        Glide.with(context).load(arrayList.get(position).getUrl())
                .thumbnail(0.3f)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into( holder.imageSingleGallery);
        holder.imageSingleGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(activity, BigImageViewActivity.class);
                intent.putExtra("url",arrayList.get(position).getUrl());
                intent.putExtra("title",arrayList.get(position).getTagLine());
                activity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class  MyHolder extends RecyclerView.ViewHolder{
        ImageView imageSingleGallery;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            imageSingleGallery=itemView.findViewById(R.id.imageSingleGallery);
        }
    }
}
