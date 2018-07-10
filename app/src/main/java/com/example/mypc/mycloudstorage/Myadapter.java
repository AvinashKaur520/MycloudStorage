package com.example.mypc.mycloudstorage;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by MY PC on 23-04-2018.
 */

public class Myadapter extends RecyclerView.Adapter<Myadapter.ViewHolder>
{
    private Context context;
    private List<upload> uploads;

    public Myadapter(Context context, List<upload> uploads)
    {
        this.context = context;
        this.uploads = uploads;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_images,parent,false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        upload upload = uploads.get(position);
        holder.textView.setText(upload.getName());
        Glide.with(context).load(upload.getUrl()).into(holder.imageView);     //glide is external library that helps to show image
    }


    @Override
    public int getItemCount()
    {
        return uploads.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder
    {
        public TextView textView;
        public ImageView imageView;

        public ViewHolder(View itemView)
        {
            super(itemView);
            textView = itemView.findViewById(R.id.textView3);
            imageView = itemView.findViewById(R.id.imageView2);
        }
    }

}
