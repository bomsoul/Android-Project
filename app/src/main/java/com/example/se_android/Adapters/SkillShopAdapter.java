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

public class SkillShopAdapter extends ArrayAdapter<Skill> {

    public SkillShopAdapter(Context context, int resource, List<Skill> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.skill_row,null);
            Skill skill = getItem(position);
            TextView title = convertView.findViewById(R.id.skill_title);
            TextView description = convertView.findViewById(R.id.skill_desc);
            TextView point = convertView.findViewById(R.id.skill_point);
            title.setText(skill.getTitle());
            description.setText(skill.getDescription());
            point.setText("point : "+skill.getPoint());
        }
        return convertView;
    }
}
