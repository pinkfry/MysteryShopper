package com.pinkfry.tech.mysteryshopper.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.gson.Gson;
import com.pinkfry.tech.mysteryshopper.Activity.QuizShowActivity;
import com.pinkfry.tech.mysteryshopper.R;
import com.pinkfry.tech.mysteryshopper.model.OptionModels;
import com.pinkfry.tech.mysteryshopper.model.SingleStore;

import java.util.ArrayList;

public class OptionAdapter extends RecyclerView.Adapter<OptionAdapter.MyHolder> {
    ArrayList<OptionModels> arrayList;
    Activity activity;
    String clientName;

    public OptionAdapter(ArrayList<OptionModels> arrayList) {
        this.arrayList = arrayList;

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
        holder.tvClientName.setText(arrayList.get(position).getOption());
        holder.imageAvtar.setVisibility(View.GONE);

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {
        TextView tvClientName;
        ImageView imageAvtar;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            tvClientName=itemView.findViewById(R.id.tvClientName);
            imageAvtar=itemView.findViewById(R.id.imageAvtar);

        }
    }
}
