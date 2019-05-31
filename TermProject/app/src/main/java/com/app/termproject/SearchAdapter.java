package com.app.termproject;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.List;

public class SearchAdapter extends BaseAdapter {

    private Context context;
    private List<String> list1;
    private List<String> list2;
    private LayoutInflater inflate;
    private ViewHolder viewHolder;

    public SearchAdapter(List<String> list1, List<String> list2, Context context) {
        this.list1 = list1;
        this.list2 = list2;
        this.context = context;
        this.inflate = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list1.size();
    }

    @Override
    public Object getItem(int i) {
        return list1.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        if (convertView == null) {
            convertView = inflate.inflate(R.layout.row_listview, null);

            viewHolder = new ViewHolder();

            viewHolder.img = (ImageView) convertView.findViewById(R.id.diaryImageView);
            viewHolder.name = (TextView) convertView.findViewById(R.id.diaryName);
            //viewHolder.date = (TextView) convertView.findViewById(R.id.diaryDate);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // 리스트에 있는 데이터를 리스트뷰 셀에 뿌린다.
        viewHolder.name.setText(list1.get(position));
        viewHolder.date.setText(list2.get(position));

        //image도 집어넣어야 함
//        viewHolder.img.setImageResource();
        return convertView;
    }

    class ViewHolder {
        public TextView name;
        public TextView date;
        public ImageView img;
    }

}