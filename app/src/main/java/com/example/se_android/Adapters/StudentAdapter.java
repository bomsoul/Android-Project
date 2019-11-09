package com.example.se_android.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.se_android.Models.Student;
import com.example.se_android.R;

import java.util.List;

public class StudentAdapter extends ArrayAdapter<Student> {
    public StudentAdapter(Context context, int resource, List<Student> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.manage_list,null);
            Student student =getItem(position);
            TextView textView = convertView.findViewById(R.id.std_name);
            textView.setText(student.getName());
            TextView textView2 = convertView.findViewById(R.id.std_point);
            textView2.setText(student.getPoint()+"");
        }
        return convertView;
    }
}
