package com.pinkfry.tech.mysteryshopper.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.gson.Gson;
import com.pinkfry.tech.mysteryshopper.Activity.ShowStoreActivity;
import com.pinkfry.tech.mysteryshopper.R;
import com.pinkfry.tech.mysteryshopper.model.SingleClient;

import java.util.ArrayList;

public class ClientListAdapter extends RecyclerView.Adapter<ClientListAdapter.MyHolder> {
    ArrayList<SingleClient> arrayList;
    Activity activity;
    int  []imagesArray;
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
        holder.tvClientName.setText(arrayList.get(position).getName());
        holder.imageAvtar.setImageResource(imagesArray[arrayList.get(position).getImagePosition()]);
        holder.tvClientName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(activity, ShowStoreActivity.class);
                intent.putExtra("name",arrayList.get(position).getName());
                intent.putExtra("total",arrayList.get(position).getTotal());
                Gson gson=new Gson();
//                Log.d(TAG, "onClick: "+arrayList.get(position).getQuestions().size());
//                Log.d(TAG, "onClick: "+arrayList.get(position).getTotal());

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
        ImageView imageAvtar;
        public MyHolder(@NonNull View itemView) {
            super(itemView);

            tvClientName=itemView.findViewById(R.id.tvClientName);
            imageAvtar=itemView.findViewById(R.id.imageAvtar);
        }
    }
}
