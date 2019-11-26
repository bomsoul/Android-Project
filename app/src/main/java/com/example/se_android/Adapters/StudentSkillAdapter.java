package com.example.se_android.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.se_android.Models.Skill;
import com.example.se_android.R;

import java.util.List;

public class StudentSkillAdapter extends ArrayAdapter<Skill> {
    public StudentSkillAdapter(Context context, int resource, List<Skill> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.student_skill_row,null);
            Skill skill = getItem(position);
            TextView title = convertView.findViewById(R.id.skillname);
            TextView amount = convertView.findViewById(R.id.amount);
            title.setText(skill.getTitle());
            amount.setText("Amount : "+skill.getAmount());
        }
        return convertView;
    }
}
