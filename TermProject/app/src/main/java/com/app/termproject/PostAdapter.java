package com.app.termproject;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {
    Context context;
    List<PostListItem> items;
    int item_layout;
    private ItemClick onItemClickListener;
    public PostAdapter(Context context, List<PostListItem> items, int item_layout) {
        this.context=context;
        this.items=items;
        this.item_layout=item_layout;
    }
    public interface ItemClick
    {
        public void onClick(View view, int posistion);
    }
    public PostAdapter(ItemClick itemClick)
    {
        this.onItemClickListener=itemClick;
}
    public PostAdapter()
    {

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post,null);
        return new ViewHolder(v);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final PostListItem item=items.get(position);

        String year;
        String month;
        String day;
        String date;
        String dateChanged;

        date=item.getDate();
        year=date.substring(0,4);
        month=date.substring(4,6);
        day=date.substring(6,8);

        dateChanged=year+"년 "+month+"월 "+day + "일";

        Glide.with(holder.itemView.getContext()).load(item.getImage()).into(holder.image);
        holder.title.setText(item.getTitle());
        holder.date.setText(dateChanged);
        holder.cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(context,DiaryDetail.class);
                i.putExtra("name",item.getTitle());
                i.putExtra("uri",item.getImage());
                i.putExtra("content",item.getContent());
                i.putExtra("key",item.getPostKey());
                i.putExtra("pinnumber",item.getPinnumber());
                i.putExtra("fileName",item.getFileName());
                i.putExtra("date",item.getDate());
                context.startActivity(i);

            }
        });
    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView title,date;
        CardView cardview;

        public ViewHolder(View itemView) {
            super(itemView);
            image=(ImageView)itemView.findViewById(R.id.image);
            title=(TextView)itemView.findViewById(R.id.title);
            cardview=(CardView)itemView.findViewById(R.id.cardview);
            date=(TextView)itemView.findViewById(R.id.date);
        }

    }
}