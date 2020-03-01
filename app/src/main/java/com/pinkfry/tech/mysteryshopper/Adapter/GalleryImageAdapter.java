package com.pinkfry.tech.mysteryshopper.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.pinkfry.tech.mysteryshopper.Activity.BigImageViewActivity;
import com.pinkfry.tech.mysteryshopper.Activity.GalleryActivity;
import com.pinkfry.tech.mysteryshopper.R;
import com.pinkfry.tech.mysteryshopper.model.GalleryModel;

import java.util.ArrayList;

public class GalleryImageAdapter extends RecyclerView.Adapter<GalleryImageAdapter.MyHolder> {
    ArrayList<GalleryModel> arrayList;
    ArrayList<String> keyArrayList;
    Context context;
    private AlertDialog.Builder alertDialog;
    String clientName;
    String storeName;
    Activity activity;

    public GalleryImageAdapter(ArrayList<GalleryModel> arrayList,ArrayList<String> keyArrayList,Context context,Activity activity,String clientName,String storeName) {
        this.arrayList = arrayList;
        this.context=context;
        this.activity=activity;
        this.clientName=clientName;
        this.storeName=storeName;
        this.keyArrayList=keyArrayList;
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
                intent.putExtra("key",keyArrayList.get(position));
                intent.putExtra("clientName",clientName);
                intent.putExtra("storeName",storeName);
                intent.putExtra("title",arrayList.get(position).getTagLine());
                activity.startActivity(intent);
            }
        });
        holder.imageSingleGallery.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
       showAlertDialogBox(clientName,position,storeName,keyArrayList.get(position));
       alertDialog.show();
                return true;
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

    void showAlertDialogBox(final String clientName, final int position, final String storeName,final String key){
        alertDialog= new AlertDialog.Builder(activity)
                .setMessage(activity.getResources().getString(R.string.deleteImage))
                .setTitle(activity.getResources().getString(R.string.delete))
                .setPositiveButton(activity.getResources().getString(R.string.delete), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DatabaseReference dref= FirebaseDatabase.getInstance().getReference().child(activity.getResources().getString(R.string.FirebaseClient));
                        dref.child("gallery").child(key).setValue(null);
//                        StorageReference fstore= FirebaseStorage.getInstance().getReference();
//                        fstore.child("IMAGES").child(clientName).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
//                            @Override
//                            public void onComplete(@NonNull Task<Void> task) {
//                                Toast.makeText(activity, activity.getResources().getString(R.string.successfullyDeleted), Toast.LENGTH_SHORT).show();
//                            }
//                        });
                        arrayList.remove(position);
                        keyArrayList.remove(position);
                        notifyDataSetChanged();
                    }
                })


                .setNegativeButton(activity.getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
    }
}
