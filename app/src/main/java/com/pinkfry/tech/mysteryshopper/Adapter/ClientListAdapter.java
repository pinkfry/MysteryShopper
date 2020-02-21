package com.pinkfry.tech.mysteryshopper.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.pinkfry.tech.mysteryshopper.Activity.AddClientActivity;
import com.pinkfry.tech.mysteryshopper.Activity.ShowStoreActivity;
import com.pinkfry.tech.mysteryshopper.R;
import com.pinkfry.tech.mysteryshopper.model.SingleClient;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ClientListAdapter extends RecyclerView.Adapter<ClientListAdapter.MyHolder> {
    ArrayList<SingleClient> arrayList;
    Activity activity;
    int  []imagesArray;
    private AlertDialog.Builder alertDialog;
public static final String TAG="CLA";
    public ClientListAdapter(ArrayList<SingleClient> arrayList, Activity activity) {
        this.arrayList = arrayList;
        this.activity=activity;

        imagesArray = new int[]{R.drawable.image_first_male,R.drawable.image_first_female,R.drawable.image_second_male,R.drawable.image_second_female,R.drawable.image_third_male,R.drawable.image_third_female,R.drawable.image_forth_male,R.drawable.image_forth_female,R.drawable.image_fifth_male,R.drawable.image_fifth_female};

    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater li= (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=li.inflate(R.layout.adapter_client_list,parent,false);
        return  new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, final int position) {
        final SingleClient singleClient=arrayList.get(position);
        holder.tvClientName.setText(singleClient.getName());
        if (singleClient.getImageUrl().isEmpty()) {
            holder.imageAvtar.setImageResource(imagesArray[singleClient.getImagePosition()]);
        }
        else{
            Picasso.get().load(singleClient.getImageUrl()).placeholder(imagesArray[singleClient.getImagePosition()]).into(holder.imageAvtar);
        }

        holder.linearSingleClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(activity, ShowStoreActivity.class);
                intent.putExtra("nodeName",singleClient.getNodeName());
                intent.putExtra("name",singleClient.getName());
                intent.putExtra("total",singleClient.getTotal());
                Gson gson=new Gson();
//                Log.d(TAG, "onClick: "+arrayList.get(position).getQuestions().size());
//                Log.d(TAG, "onClick: "+arrayList.get(position).getTotal());

                activity.startActivity(intent);
            }
        });
        holder.imageAvtar.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                Intent intent=new Intent(activity, AddClientActivity.class);
                intent.putExtra("mode",1);
                intent.putExtra("clientName",singleClient.getName());
                intent.putExtra("nodeName",singleClient.getNodeName());
                activity.startActivity(intent);
                return true;
            }
        });

        holder.linearSingleClient.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                showAlertDialogBox(arrayList.get(position).getNodeName(),position);
                alertDialog.show();

                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {
        TextView tvClientName;
        LinearLayout linearSingleClient;
        ImageView imageAvtar;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            linearSingleClient=itemView.findViewById(R.id.linearSingleClient);
            tvClientName=itemView.findViewById(R.id.tvClientName);
            imageAvtar=itemView.findViewById(R.id.imageAvtar);
        }
    }
    void showAlertDialogBox(final String clientName, final int position){
        alertDialog= new AlertDialog.Builder(activity)
                .setMessage("Do you want to delete "+clientName)
                .setTitle("Delete Client")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DatabaseReference dref= FirebaseDatabase.getInstance().getReference().child(activity.getResources().getString(R.string.FirebaseClient)).child(clientName);
                        StorageReference fstore=FirebaseStorage.getInstance().getReference();
                                fstore.child("IMAGES").child(clientName).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(activity, "Successfully deleted the Client", Toast.LENGTH_SHORT).show();
                                    }
                                });
                        dref.setValue(null);
                        arrayList.remove(position);
                        notifyDataSetChanged();
                    }
                })


                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
    }
}
