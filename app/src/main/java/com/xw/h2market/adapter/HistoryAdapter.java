package com.xw.h2market.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.xw.h2market.R;
import com.xw.h2market.pojo.Record;

import java.util.List;

/**
 * Created by Administrator on 2018/5/26.
 */

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    private List<Record> rc_list;
    private ClickCallback callback;
    private Record r;

    /**
     * 设置点击事件的回调接口
     *
     * @param callback
     */
    public void setClickCallback(ClickCallback callback) {
        this.callback = callback;
    }

    /**
     * 点击事件的回调接口
     *
     * @author Administrator
     */
    public static interface ClickCallback {
        public void click(View v, Record r);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        Button record_btn;

        public ViewHolder(View itemView) {
            super(itemView);
            record_btn = itemView.findViewById(R.id.record_item_btn);
            record_btn.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            r = (Record) v.getTag();
            if (callback != null) {
                callback.click(v, r);
            }
        }
    }

    public HistoryAdapter(List<Record> rc_list) {
        this.rc_list = rc_list;
    }

    @Override
    public HistoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history_layout, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(HistoryAdapter.ViewHolder holder, int position) {
        r = rc_list.get(position);
        holder.record_btn.setText(r.getName());
        holder.record_btn.setTag(r);
    }

    @Override
    public int getItemCount() {
        return rc_list == null ? 0 : rc_list.size();
    }
}
