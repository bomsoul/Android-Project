package com.example.se_android.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.se_android.Models.Classroom;
import com.example.se_android.R;

import java.util.List;

public class ClassListSTAdapter extends ArrayAdapter<Classroom> {
    public ClassListSTAdapter(Context context, int resource, List<Classroom> objects) {
        super(context, resource, objects);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.itemclassrow_st,null);
            Classroom classroom =getItem(position);
            TextView textView = convertView.findViewById(R.id.textView);
            textView.setText(classroom.getClassname());
            TextView textView2 = convertView.findViewById(R.id.textView2);
            textView2.setText(classroom.getCode());
            TextView textView3 = convertView.findViewById(R.id.textView3);
            textView3.setText(classroom.getTeacher());
        }
        return convertView;
    }
}
