package com.lu.bookbook.mustread;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;



/**
 * Created by Luba on 17.04.2018.
 */

public class ListAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private ArrayList<Model> list;

    public ListAdapter(Context context, int layout, ArrayList<Model> list) {
        this.context = context;
        this.layout = layout;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder {
        ImageView iView;
        TextView tName, tAuthor;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        View row=view;
        ViewHolder holder=new ViewHolder();

        if(row==null){
            LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row=inflater.inflate(layout, null);
            holder.tName=row.findViewById(R.id.itemName);
            holder.tAuthor=row.findViewById(R.id.itemAuthor);
            holder.iView=row.findViewById(R.id.listImageView);
            row.setTag(holder);
        }
        else{
            holder=(ViewHolder) row.getTag();
        }

        Model model= list.get(position);
        holder.tName.setText(model.getName());
        holder.tAuthor.setText(model.getAuthor());

        byte [] image =model.getImage();
        Bitmap bitmap= BitmapFactory.decodeByteArray(image, 0,image.length);
        holder.iView.setImageBitmap(bitmap);



        return row;
    }
}

