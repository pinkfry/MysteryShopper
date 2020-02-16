package com.pinkfry.tech.mysteryshopper.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.pinkfry.tech.mysteryshopper.Activity.EditQuestionActivity;
import com.pinkfry.tech.mysteryshopper.Activity.QuizShowActivity;
import com.pinkfry.tech.mysteryshopper.R;
import com.pinkfry.tech.mysteryshopper.model.AnsGivenModel;
import com.pinkfry.tech.mysteryshopper.model.OptionModels;
import com.pinkfry.tech.mysteryshopper.model.QuestionsModel;

import java.util.ArrayList;

public class QuestionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final String TAG = "QA";
    ArrayList<QuestionsModel> arrayList;
    Context context;
    String clientName, storeName;
    ArrayList<AnsGivenModel> listPreviousAns;
    DatabaseReference dref;
    int questionNumber=0;
    Activity activity;


    public QuestionAdapter(ArrayList<QuestionsModel> arrayList, Context context, String clientName, String storeName, Activity activity,ArrayList<AnsGivenModel> listPreviousAns) {
        this.arrayList = arrayList;
        this.context = context;
        this.clientName = clientName;
        this.activity=activity;
        this.listPreviousAns=listPreviousAns;
        this.storeName = storeName;
        dref = FirebaseDatabase.getInstance().getReference().child(context.getResources().getString(R.string.FirebaseClient)).child(clientName).child(context.getResources().getString(R.string.firebaseStore)).child(storeName).child(context.getResources().getString(R.string.ansGiven));

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater li = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (viewType == 1) {
            View view;
            view = li.inflate(R.layout.adapter_show_questions, parent, false);
//        Log.d(TAG, "QuestionAdapter: "+QuizShowActivity.ansToSendArrayList.size()+" "+arrayList.size());
            return new MyHolder(view);
        } else if (viewType == 2) {
            View view;
            view = li.inflate(R.layout.adapter_show_questions_2, parent, false);
            return new MyHolderDate(view);
        } else if (viewType == 3) {
            View view;
            view = li.inflate(R.layout.adapter_show_questions_3, parent, false);
            return new MyHolderTime(view);
        } else {
            View view;
            view = li.inflate(R.layout.adapter_show_questions_4, parent, false);
            return new MyHolderInput(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder,  final int position) {

        final QuestionsModel questionsModel = arrayList.get(position);

        if(questionsModel.getVisible()!=0) {
            questionNumber++;
            Log.d(TAG, "onDateSet: " + position);
            if (questionsModel.getType() == 1) {

                ((MyHolder) holder).etQuestion.setText(questionNumber + ". " + questionsModel.getQuestion());
                int index = 0;
                for (OptionModels option : questionsModel.getOptions()) {

                    ((MyHolder) holder).radioGroupQuestion.addView(addRadioView(option.getOption(), index));
                    index++;
                }
                ((MyHolder) holder).radioGroupQuestion.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        QuizShowActivity.ansArray.get(position).setValue(arrayList.get(position).getOptions().get(checkedId).getValue());
                    }
                });

                ((MyHolder) holder).imageEditQuestion.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(context, EditQuestionActivity.class);
//                        intent.putExtra("key",questionKeyArrayList.get(position));
                        Gson gson=new Gson();
                        String json= gson.toJson(questionsModel);
                        intent.putExtra("questionModel",json);
                        intent.putExtra("clientName",clientName);
                        intent.putExtra("storeName",storeName);
                        intent.putExtra("position",position);
                        context.startActivity(intent);
                        activity.finish();


                    }
                });

            } else if (questionsModel.getType() == 2) {
                if(listPreviousAns!=null && listPreviousAns.size()!=0){
                    int size=listPreviousAns.get(position).getAns().size();
                    ((MyHolderDate)holder).btnGetDate.setText(listPreviousAns.get(position).getAns().get(size-1));
                }
                DatePickerDialog dialog = null;
                DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        ((MyHolderDate) holder).btnGetDate.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                        Log.d(TAG, "onDateSet: " + position);
                        QuizShowActivity.ansArray.get(position).getAns().clear();
                        QuizShowActivity.ansArray.get(position).getAns().add(((MyHolderDate) holder).btnGetDate.getText().toString());
                    }
                };

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                    dialog = new DatePickerDialog(context);
                    dialog.setOnDateSetListener(listener);
                }
                ((MyHolderDate) holder).etQuestion.setText(questionNumber + ". " + questionsModel.getQuestion());
                final DatePickerDialog finalDialog = dialog;
                ((MyHolderDate) holder).btnGetDate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finalDialog.show();

                    }
                });
                ((MyHolderDate) holder).imageEditQuestion.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(context, EditQuestionActivity.class);
//                        intent.putExtra("key",questionKeyArrayList.get(position));
                        Gson gson=new Gson();
                        String json= gson.toJson(questionsModel);
                        intent.putExtra("questionModel",json);
                        intent.putExtra("clientName",clientName);
                        intent.putExtra("storeName",storeName);
                        intent.putExtra("position",position);
                        context.startActivity(intent);
                        activity.finish();


                    }
                });

            } else if (questionsModel.getType() == 3) {
                if(listPreviousAns!=null && listPreviousAns.size()!=0){
                    int size=listPreviousAns.get(position).getAns().size();
                    ((MyHolderTime)holder).btnGetTime.setText(listPreviousAns.get(position).getAns().get(size-1));
                }
                TimePickerDialog.OnTimeSetListener listener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        ((MyHolderTime) holder).btnGetTime.setText(hourOfDay + " : " + minute + " ");
                        QuizShowActivity.ansArray.get(position).getAns().clear();
                        QuizShowActivity.ansArray.get(position).getAns().add(((MyHolderTime) holder).btnGetTime.getText().toString());

                    }
                };

                final TimePickerDialog timePicker = new TimePickerDialog(context, listener, 0, 0, true);
                ((MyHolderTime) holder).etQuestion.setText(questionNumber + ". " + questionsModel.getQuestion());
                ((MyHolderTime) holder).btnGetTime.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        timePicker.show();
                    }
                });
                ((MyHolderTime) holder).imageEditQuestion.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(context, EditQuestionActivity.class);
//                        intent.putExtra("key",questionKeyArrayList.get(position));
                        Gson gson=new Gson();
                        String json= gson.toJson(questionsModel);
                        intent.putExtra("questionModel",json);
                        intent.putExtra("clientName",clientName);
                        intent.putExtra("storeName",storeName);
                        intent.putExtra("position",position);
                        context.startActivity(intent);
                        activity.finish();


                    }
                });


            } else if (questionsModel.getType() == 4) {

                if(listPreviousAns!=null && listPreviousAns.size()!=0){
                    int size=listPreviousAns.get(position).getAns().size();
                    ((MyHolderInput)holder).tvWriteSomething.setText(listPreviousAns.get(position).getAns().get(size-1));
                }
                ((MyHolderInput) holder).etQuestion.setText(questionNumber + ". " + questionsModel.getQuestion());
                LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                @SuppressLint("InflateParams") View view = li.inflate(R.layout.dialogue_add_text, null, false);
                final EditText etAnswer = view.findViewById(R.id.etAnswer);

                final AlertDialog alertDialog = new AlertDialog.Builder(context)
                        .setView(view)
                        .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ((MyHolderInput) holder).tvWriteSomething.setText(etAnswer.getText().toString());
                                QuizShowActivity.ansArray.get(position).getAns().clear();
                                QuizShowActivity.ansArray.get(position).getAns().add(etAnswer.getText().toString());
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).create();
                ((MyHolderInput) holder).tvWriteSomething.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.show();
                    }
                });
                ((MyHolderInput) holder).imageEditQuestion.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(context, EditQuestionActivity.class);
//                        intent.putExtra("key",questionKeyArrayList.get(position));
                        Gson gson=new Gson();
                        String json= gson.toJson(questionsModel);
                        intent.putExtra("questionModel",json);
                        intent.putExtra("clientName",clientName);
                        intent.putExtra("storeName",storeName);
                        intent.putExtra("position",position);
                        context.startActivity(intent);
                        activity.finish();


                    }
                });


            }
        }
        else{
            holder.itemView.findViewById(R.id.linearQuestion).setLayoutParams(new LinearLayout.LayoutParams(0,0));
        }
    }

    @Override
    public int getItemCount() {
        Log.d("QA", "getItemCount: " + arrayList.size() + "");
        return arrayList.size();

    }

    class MyHolder extends RecyclerView.ViewHolder {
       public RadioGroup radioGroupQuestion;
       public TextView etQuestion;
       ImageView imageEditQuestion;
       LinearLayout linearQuestion;

        MyHolder(@NonNull View itemView) {
            super(itemView);
            radioGroupQuestion = itemView.findViewById(R.id.radioGroupOptions);
            etQuestion = itemView.findViewById(R.id.tvQuestion);
            imageEditQuestion=itemView.findViewById(R.id.imageEditQuestion);
            linearQuestion=itemView.findViewById(R.id.linearQuestion);
        }
    }

    class MyHolderDate extends RecyclerView.ViewHolder {
       public TextView etQuestion;
        public Button btnGetDate;
        ImageView imageEditQuestion;
        LinearLayout linearQuestion;
        public MyHolderDate(@NonNull View itemView) {
            super(itemView);
            btnGetDate = itemView.findViewById(R.id.btnGetDate);
            etQuestion = itemView.findViewById(R.id.tvQuestion);
            imageEditQuestion=itemView.findViewById(R.id.imageEditQuestion);
            linearQuestion =itemView.findViewById(R.id.linearQuestion);

        }
    }

    class MyHolderTime extends RecyclerView.ViewHolder {
        TextView etQuestion;
        Button btnGetTime;
        ImageView imageEditQuestion;
        LinearLayout linearQuestion;

        public MyHolderTime(@NonNull View itemView) {
            super(itemView);
            btnGetTime = itemView.findViewById(R.id.btnGetTime);
            imageEditQuestion=itemView.findViewById(R.id.imageEditQuestion);
           linearQuestion=itemView.findViewById(R.id.linearQuestion);
            etQuestion = itemView.findViewById(R.id.tvQuestion);
        }
    }

    class MyHolderInput extends RecyclerView.ViewHolder {
        TextView etQuestion;
        TextView tvWriteSomething;
          ImageView imageEditQuestion;
          LinearLayout linearQuestion;
        public MyHolderInput(@NonNull View itemView) {
            super(itemView);
            imageEditQuestion=itemView.findViewById(R.id.imageEditQuestion);
            tvWriteSomething = itemView.findViewById(R.id.tvWriteSomeThing);
            linearQuestion=itemView.findViewById(R.id.linearQuestion);
            etQuestion = itemView.findViewById(R.id.tvQuestion);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return arrayList.get(position).getType();
    }

    RadioButton addRadioView(String option, int index) {
        RadioButton rb = new RadioButton(context);
        rb.setId(index);
        rb.setText(option);
        rb.setTextColor(Color.BLACK);
        return rb;
    }
}
