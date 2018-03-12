package com.kco.myapplication.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.kco.myapplication.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 666666 on 2018/3/12.
 */
public class ImageViewPager extends PagerAdapter {

    private List<String> list;
    private Map<String, View> viewMap = new HashMap<>();
    private Context context;

    public ImageViewPager(Context context, List<String> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        ImageView imageView = new ImageView(context);
        imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));

        Glide.with(context)
                .load(list.get(position))
                .placeholder(R.drawable.timg)
                .into(imageView);
        container.addView(imageView);
        viewMap.put(list.get(position), imageView);
        return imageView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView(viewMap.get(list.get(position)));
    }
}
