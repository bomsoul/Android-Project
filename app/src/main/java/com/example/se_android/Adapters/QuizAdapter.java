package com.example.se_android.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.se_android.Models.Quiz;
import com.example.se_android.R;

import java.util.List;

public class QuizAdapter extends ArrayAdapter<Quiz> {

    public QuizAdapter(Context context, int resource, List<Quiz> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.quiz_row,null);
            Quiz quiz = getItem(position);
            TextView quesion =convertView.findViewById(R.id.question);
            quesion.setText(quiz.getQuestion());
            TextView ans1 =convertView.findViewById(R.id.ans1);
            ans1.setText(quiz.getAns1());
            TextView ans2 =convertView.findViewById(R.id.ans2);
            ans2.setText(quiz.getAns2());
            TextView ans3 =convertView.findViewById(R.id.ans3);
            ans3.setText(quiz.getAns3());
            TextView ans4 =convertView.findViewById(R.id.ans4);
            ans4.setText(quiz.getAns4());
        }
        return convertView;
    }
}
