package com.pinkfry.tech.mysteryshopper.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.gson.Gson;
import com.pinkfry.tech.mysteryshopper.Activity.QuizShowActivity;
import com.pinkfry.tech.mysteryshopper.Activity.ShowStoreActivity;
import com.pinkfry.tech.mysteryshopper.R;
import com.pinkfry.tech.mysteryshopper.model.SingleStore;

import java.util.ArrayList;

public class ClientStoreAdapter extends RecyclerView.Adapter<ClientStoreAdapter.MyHolder> {
    ArrayList<SingleStore> arrayList;
    Activity activity;
    String clientName;

    public ClientStoreAdapter(ArrayList<SingleStore> arrayList, Activity activity, String clientName) {
        this.arrayList = arrayList;
        this.activity=activity;
        this.clientName=clientName;
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
        holder.tvClientName.setText(arrayList.get(position).getName());
        holder.tvClientName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(activity, QuizShowActivity.class);
                intent.putExtra("storeName",arrayList.get(position).getName());
                intent.putExtra("clientName",clientName);
                Gson gson=new Gson();
                String jsonToSend=gson.toJson(arrayList.get(position).getAnsGiven());
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
        TextView tvClientName;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            tvClientName=itemView.findViewById(R.id.tvClientName);

        }
    }
}
