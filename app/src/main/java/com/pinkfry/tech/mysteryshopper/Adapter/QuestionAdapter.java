package com.pinkfry.tech.mysteryshopper.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pinkfry.tech.mysteryshopper.Activity.QuizShowActivity;
import com.pinkfry.tech.mysteryshopper.R;
import com.pinkfry.tech.mysteryshopper.model.OptionModels;
import com.pinkfry.tech.mysteryshopper.model.QuestionsModel;

import java.util.ArrayList;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.MyHolder> {
    public static final String TAG="QA";
    ArrayList<QuestionsModel> arrayList;
    Context context;
    String clientName,storeName;
    DatabaseReference dref;
    public QuestionAdapter(ArrayList<QuestionsModel> arrayList,Context context,String  clientName,String storeName,ArrayList<Integer> ansToSendArrayList) {
        this.arrayList = arrayList;
        this.context=context;
        this.clientName=clientName;
        this.storeName=storeName;

        dref= FirebaseDatabase.getInstance().getReference().child(context.getResources().getString(R.string.FirebaseClient)).child(clientName).child(context.getResources().getString(R.string.firebaseStore)).child(storeName).child(context.getResources().getString(R.string.ansGiven));


    }
    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater li = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = li.inflate(R.layout.adapter_show_questions, parent, false);
        Log.d(TAG, "QuestionAdapter: "+QuizShowActivity.ansToSendArrayList.size()+" "+arrayList.size());
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, final int position) {
        final QuestionsModel questionsModel = arrayList.get(position);
        holder.etQuestion.setText(position+1+". "+questionsModel.getQuestion());
        int index=0;
        for (OptionModels option : questionsModel.getOptions()) {

            holder.radioGroupQuestion.addView(addRadioView(option.getOption(),index));
            index++;
        }
//        holder.btnSubmit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                if (QuizShowActivity.ansArray[position]!=-1) {
//////                    int value = QuizShowActivity.ansToSendArrayList.get(position) + QuizShowActivity.ansArray[position];
//////                    dref.child((position + "")).setValue(value);
////                    Toast.makeText(context, "Successfully Stored your review", Toast.LENGTH_SHORT).show();
////                }
//            }
//        });
        holder.radioGroupQuestion.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                QuizShowActivity.ansArray[position]=arrayList.get(position).getOptions().get(checkedId).getValue();
            }
        });
    }

    @Override
    public int getItemCount() {
        Log.d("QA", "getItemCount: "+arrayList.size()+"");
        return arrayList.size();

    }

    class MyHolder extends RecyclerView.ViewHolder {
        RadioGroup radioGroupQuestion;
        TextView etQuestion;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            radioGroupQuestion = itemView.findViewById(R.id.radioGroupOptions);
            etQuestion = itemView.findViewById(R.id.tvQuestion);



        }
    }

    RadioButton addRadioView(String option,int index) {
        RadioButton rb = new RadioButton(context);
        rb.setId(index);
        rb.setText(option);
        rb.setTextColor(Color.BLACK);
        return rb;
    }
}
