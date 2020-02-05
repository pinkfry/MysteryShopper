package com.pinkfry.tech.mysteryshopper.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.gson.Gson;
import com.pinkfry.tech.mysteryshopper.Activity.QuizShowActivity;
import com.pinkfry.tech.mysteryshopper.Activity.ShowStoreActivity;
import com.pinkfry.tech.mysteryshopper.R;
import com.pinkfry.tech.mysteryshopper.model.SingleStore;

import java.util.ArrayList;
import java.util.Random;

public class ClientStoreAdapter extends RecyclerView.Adapter<ClientStoreAdapter.MyHolder> {
    ArrayList<SingleStore> arrayList;
    Activity activity;
    String clientName;
    int []colorArray;
    public ClientStoreAdapter(ArrayList<SingleStore> arrayList, Activity activity, String clientName) {
        this.arrayList = arrayList;
        this.activity=activity;
        this.clientName=clientName;
        colorArray=new int[]{Color.RED,Color.GREEN,Color.BLACK,Color.GRAY,Color.BLUE};
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater li= (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=li.inflate(R.layout.adapter_store_list,parent,false);
        return  new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, final int position) {
        final SingleStore singleStore=arrayList.get(position);
        holder.tvClientName.setText(singleStore.getName());
        holder.tvAddress.setText(singleStore.getAddress());
        holder.tvPhoneNo.setText(singleStore.getPhoneNo());
        holder.tvScore.setText(String.valueOf(getToatalScore(singleStore.getAnsGiven())));
        holder.tvAvatar.setText(singleStore.getName().substring(0,2).toUpperCase());
        holder.tvAvatar.setBackgroundTintList(ColorStateList.valueOf(colorArray[new Random().nextInt(5)]));
        holder.linearSingleStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(activity, QuizShowActivity.class);
                intent.putExtra("storeName",singleStore.getName());
                intent.putExtra("clientName",clientName);
                intent.putExtra("totalClient",singleStore.getTotalClient());
                Gson gson=new Gson();
                String jsonToSend=gson.toJson(singleStore.getAnsGiven());
                intent.putExtra("ansToSend",jsonToSend);
                activity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {
        TextView tvClientName,tvAvatar,tvPhoneNo,tvAddress,tvScore;
        LinearLayout linearSingleStore;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            tvAvatar=itemView.findViewById(R.id.tvAvatar);
            tvPhoneNo=itemView.findViewById(R.id.tvPhoneNo);
            tvAddress=itemView.findViewById(R.id.tvAddress);
            tvScore=itemView.findViewById(R.id.tvScore);
            linearSingleStore=itemView.findViewById(R.id.linearSingleStore);
            tvClientName=itemView.findViewById(R.id.tvClientName);

        }
    }
    int getToatalScore(ArrayList<Integer> ansList){
        int ans=0;
        for(int value: ansList){
            ans+=value;
        }
        return  ans;

    }
}
