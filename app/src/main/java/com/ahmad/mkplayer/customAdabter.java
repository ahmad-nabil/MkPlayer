package com.ahmad.mkplayer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.ahmad.mkplayer.databinding.ListitemsBinding;

public class customAdabter extends BaseAdapter {
String [] items;
Context context;

    public customAdabter(String[] items, Context context) {
        this.items = items;
        this.context = context;
    }

    @Override
    public int getCount() {
        return items.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ListitemsBinding listitemsBinding;
        if (convertView==null){
            listitemsBinding=ListitemsBinding.inflate(LayoutInflater.from(context),parent,false);
      convertView=listitemsBinding.getRoot();
            convertView.setTag(listitemsBinding);
        }else {
listitemsBinding= (ListitemsBinding) convertView.getTag();
        }
        listitemsBinding.titlesong.setText(items[position]);

        return convertView;
    }
}
