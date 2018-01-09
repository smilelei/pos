package com.example.administrator.pospatrol.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.administrator.pospatrol.R;
import com.example.administrator.pospatrol.beans.PosBean;

public class PosListAdapter extends BaseAdapter {
    Context context;
    List<PosBean> poslist;

    public PosListAdapter(Context context, List<PosBean> poslist) {
        this.context = context;
        this.poslist = poslist;
    }

    @Override
    public int getCount() {
        return poslist.size();
    }

    @Override
    public Object getItem(int position) {
        return poslist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PosBean posBean = poslist.get(position);
        View v = LayoutInflater.from(context).inflate(R.layout.pos_list_item,
                null);
        if (position % 2 == 0) {
            v.setBackgroundResource(R.drawable.list_item_bg_color1);
//			v.setBackgroundColor(Color.parseColor("#f0f0f0"));
        } else {
            v.setBackgroundResource(R.drawable.list_item_bg_color2);
//			v.setBackgroundColor(Color.WHITE);
        }

        TextView posName = (TextView) v.findViewById(R.id.pos_name);
        posName.setText(posBean.getPosName());
        return v;
    }

}
