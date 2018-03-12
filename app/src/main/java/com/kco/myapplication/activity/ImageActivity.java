package com.kco.myapplication.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.bumptech.glide.Glide;
import com.kco.myapplication.R;
import com.kco.myapplication.adapter.ImageViewPager;
import com.kco.myapplication.utils.CrawlerUtils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by 666666 on 2018/3/12.
 */
public class ImageActivity extends AppCompatActivity {

    @BindView(R.id.viewPager)
    public ViewPager viewPager;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        ButterKnife.bind(this);

        List<String> list = new CopyOnWriteArrayList<>();
        ImageViewPager imageViewPager = new ImageViewPager(this, list);
        viewPager.setAdapter(imageViewPager);
        init(list, imageViewPager);
    }

    private void init(final List<String> list, final ImageViewPager imageViewPager) {
        Log.d("ImageActivity", "init: " + getIntent().getStringExtra("url"));
        CrawlerUtils.getImageUrl(getIntent().getStringExtra("url"))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        list.add(s);
                        imageViewPager.notifyDataSetChanged();
                    }
                });
    }
}
