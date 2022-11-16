package com.c1ph3r.zomatorestaurant.Adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.c1ph3r.zomatorestaurant.R;

import java.util.ArrayList;

public class ListOfImagesViewerAdapter extends RecyclerView.Adapter<ListOfImagesViewerAdapter.MyViewHolder> {
    Context context;
    ArrayList<Uri> ListOfImages;

    public ListOfImagesViewerAdapter(Context context, ArrayList<Uri> ListOfImages){
       this.context = context;
       this.ListOfImages = ListOfImages;
    }

    @NonNull
    @Override
    public ListOfImagesViewerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_of_images_viewer, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListOfImagesViewerAdapter.MyViewHolder holder, int position) {
        Glide.with(context).load(ListOfImages.get(position)).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return ListOfImages.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.ImageViewer);
        }
    }
}
