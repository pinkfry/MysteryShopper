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
import com.pinkfry.tech.mysteryshopper.Activity.QuizShowActivity;
import com.pinkfry.tech.mysteryshopper.Activity.ShowStoreActivity;
import com.pinkfry.tech.mysteryshopper.R;

import java.util.ArrayList;

public class ClientStoreAdapter extends RecyclerView.Adapter<ClientStoreAdapter.MyHolder> {
    ArrayList<String> arrayList;
    Activity activity;

    public ClientStoreAdapter(ArrayList<String> arrayList, Activity activity) {
        this.arrayList = arrayList;
        this.activity=activity;
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
        holder.tvClientName.setText(arrayList.get(position));
        holder.tvClientName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(activity, QuizShowActivity.class);
                intent.putExtra("name",arrayList.get(position));
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
