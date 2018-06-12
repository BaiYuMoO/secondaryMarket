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
 * Created by Administrator on 2018/5/21.
 */

public class InformationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private List<Information> informationList;

    public InformationAdapter(Context context, List<Information> informationList) {
        mContext = context;
        this.informationList = informationList;
        mLayoutInflater = LayoutInflater.from(context);
    }


    public static class NormalViewHolder extends RecyclerView.ViewHolder {
        ImageView img_preview, img_type;
        TextView tv_title, tv_price, tv_chaffer;
        LinearLayout item_layout;

        public NormalViewHolder(View itemView) {
            super(itemView);
            img_preview = itemView.findViewById(R.id.item_img_preview);
            img_type = itemView.findViewById(R.id.item_img_type);
            tv_title = itemView.findViewById(R.id.item_tv_title);
            tv_price = itemView.findViewById(R.id.item_tv_price);
            tv_chaffer = itemView.findViewById(R.id.item_tv_chaffer);
            item_layout = itemView.findViewById(R.id.item_layout);
        }

    }

    @Override
    public NormalViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new NormalViewHolder(mLayoutInflater.inflate(R.layout.item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        NormalViewHolder viewholder = (NormalViewHolder) holder;
        Information information = informationList.get(position);
        MyImageLoader.imgLoader.displayImage(information.getImg1().getUrl(), viewholder.img_preview);
        if (information.getSell_and_buy().equals("出售")) {
            viewholder.img_type.setImageResource(R.drawable.goods_cs);
        } else if(information.getSell_and_buy().equals("求购")){
            viewholder.img_type.setImageResource(R.drawable.goods_qg);
        }else{
            viewholder.img_type.setImageResource(R.drawable.goods_ys);
        }
        if (information.getChaffer().equals("可小刀")) {
            viewholder.tv_chaffer.setSelected(true);
        } else {
            viewholder.tv_chaffer.setSelected(false);
        }
        viewholder.tv_title.setText(information.getTitle());
        viewholder.tv_price.setText("¥ " + information.getPrice());
        viewholder.item_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Information infor = informationList.get(position);
                Intent intent = new Intent(mContext, ParticularsActivity.class);
                intent.putExtra("id",infor.getObjectId());
                intent.putExtra("username",infor.getUsername());
                mContext.startActivity(intent);
            }
        });
    }

    //获取数据的数量
    @Override
    public int getItemCount() {
        return informationList == null ? 0 : informationList.size();
    }
}
