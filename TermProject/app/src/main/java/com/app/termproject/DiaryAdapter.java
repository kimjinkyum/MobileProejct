package com.app.termproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class DiaryAdapter extends ArrayAdapter {
    private Context context;
    private LayoutInflater inflate;
//    int img[];
    private ArrayList<String> name;
    ViewHolder viewHolder;

    public DiaryAdapter(Context context, ArrayList<String> name) {
        super(context,0,name);
        this.context = context;
//        this.img = img;
        this.name=name;
        this.inflate = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return name.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = inflate.inflate(R.layout.list_diary, parent,false);

            viewHolder = new ViewHolder();
//            viewHolder.iv = (ImageView) convertView.findViewById(R.id.imageView1);
            viewHolder.tv = (TextView) convertView.findViewById(R.id.name);

            convertView.setTag(viewHolder);
        }
        else{
            viewHolder =(ViewHolder)convertView.getTag();
        }
//        viewHolder.iv.setImageResource(img[position]);
        viewHolder.tv.setText(name.get(position));
        return convertView;
    }
}

class ViewHolder{
    public TextView tv;
//    public ImageView iv;
}
