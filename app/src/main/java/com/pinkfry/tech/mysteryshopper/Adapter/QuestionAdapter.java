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
import com.pinkfry.tech.mysteryshopper.R;
import com.pinkfry.tech.mysteryshopper.model.QuestionsModel;

import java.util.ArrayList;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.MyHolder> {
    public static final String TAG="QA";
    ArrayList<QuestionsModel> arrayList;
    Context context;
    DatabaseReference dref;
    int[] ansArray;
    public QuestionAdapter(ArrayList<QuestionsModel> arrayList,Context context,DatabaseReference dref) {
        this.arrayList = arrayList;
        this.context=context;
        this.dref=dref;


    }
    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater li = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = li.inflate(R.layout.adapter_show_questions, parent, false);

        ansArray=new int[arrayList.size()];
        Log.d(TAG, "QuestionAdapter: "+ansArray.length+" "+arrayList.size());
        for(int i=0;i<ansArray.length;++i){
            ansArray[i]=-1;
        }
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, final int position) {
        final QuestionsModel questionsModel = arrayList.get(position);
        holder.etQuestion.setText(position+1+". "+questionsModel.getQuestion());
        int index=0;
        for (String option : questionsModel.getOptions()) {

            holder.radioGroupQuestion.addView(addRadioView(option,index));
            index++;
        }
        holder.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ansArray[position]!=-1)
                dref.child("Question_"+(position+1)).child("Answers").child(ansArray[position]+"").setValue(questionsModel.getAnswers().get(ansArray[position])+1);
                Toast.makeText(context,"Successfully Stored your review",Toast.LENGTH_SHORT).show();
            }
        });
        holder.radioGroupQuestion.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                ansArray[position]=checkedId;
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
        Button btnSubmit;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            radioGroupQuestion = itemView.findViewById(R.id.radioGroupOptions);
            etQuestion = itemView.findViewById(R.id.tvQuestion);
            btnSubmit=itemView.findViewById(R.id.btnSubmit);


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
