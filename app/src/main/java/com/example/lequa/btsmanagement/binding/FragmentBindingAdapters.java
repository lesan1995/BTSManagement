package com.example.lequa.btsmanagement.binding;

import android.databinding.BindingAdapter;
import android.support.v4.app.Fragment;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import javax.inject.Inject;

public class FragmentBindingAdapters {
    final Fragment fragment;

    @Inject
    public FragmentBindingAdapters(Fragment fragment) {
        this.fragment = fragment;
    }
    @BindingAdapter("imageUrl")
    public void bindImage(ImageView imageView, String url) {
        Glide.with(fragment).load("http://serverbts220180515045504.azurewebsites.net/image/"+url).into(imageView);
    }
}
