package com.app.pdfdemo;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.pdfdemo.databinding.ItemImagesBinding;

import java.io.IOException;
import java.util.List;

public class ImagesAdapter extends RecyclerView.Adapter<ImagesAdapter.Viewholder> {
    private Context context;
    private List<String> uriList;

    public ImagesAdapter(MainActivity mainActivity, List<String> uriList) {
        context = mainActivity;
        this.uriList = uriList;

    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        ItemImagesBinding binding = DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()), R.layout.item_images, viewGroup, false);
        return new Viewholder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder viewholder, int i) {
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), Uri.parse(uriList.get(i)));
            viewholder.binding.image.setImageBitmap(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return uriList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        private ItemImagesBinding binding;

        public Viewholder(@NonNull ItemImagesBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }
}
