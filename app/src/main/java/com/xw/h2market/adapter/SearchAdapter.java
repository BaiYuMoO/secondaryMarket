package com.xw.h2market.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xw.h2market.ParticularsActivity;
import com.xw.h2market.R;
import com.xw.h2market.pojo.Information;
import com.xw.h2market.util.MyImageLoader;

import java.util.List;

/**
 * Created by Administrator on 2018/5/26.
 */

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {

    private Context context;
    private List<Information> informationList;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView search_img;
        TextView tv_title, tv_type, tv_content, tv_time, tv_chaffer;
        LinearLayout item_search_layout;

        public ViewHolder(View itemView) {
            super(itemView);
            search_img = itemView.findViewById(R.id.item_search_img);
            tv_title = itemView.findViewById(R.id.item_search_tv_title);
            tv_type = itemView.findViewById(R.id.item_search_tv_type);
            tv_content = itemView.findViewById(R.id.item_search_tv_content);
            tv_time = itemView.findViewById(R.id.item_search_tv_time);
            tv_chaffer = itemView.findViewById(R.id.item_search_tv_chaffer);
            item_search_layout = itemView.findViewById(R.id.item_search_layout);
        }
    }

    public SearchAdapter(Context context, List<Information> informationList) {
        this.context = context;
        this.informationList = informationList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_layout, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Information infor = informationList.get(position);
        MyImageLoader.imgLoader.displayImage(infor.getImg1().getUrl(), holder.search_img);
        holder.tv_title.setText(infor.getTitle());
        holder.tv_type.setText(infor.getType());
        holder.tv_content.setText(infor.getContent());
        holder.tv_time.setText(infor.getCreatedAt());
        if (infor.getChaffer().equals("可小刀")) {
            holder.tv_chaffer.setSelected(true);
        } else {
            holder.tv_chaffer.setSelected(false);
        }
        holder.item_search_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                Intent intent = new Intent(context, ParticularsActivity.class);
                intent.putExtra("id", infor.getObjectId());
                intent.putExtra("username", infor.getUsername());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return informationList == null ? 0 : informationList.size();
    }
}
