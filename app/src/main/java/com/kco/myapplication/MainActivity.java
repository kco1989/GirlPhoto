package com.kco.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private final static String TAG = "MainActivity";
    @BindView(R.id.recycler_view)
    public RecyclerView recyclerView;

    private List<GirlPhotoBean> imageUrlList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        ImageAdapter adapter = new ImageAdapter(imageUrlList, MainActivity.this);
        recyclerView.setAdapter(adapter);
        initUrl(adapter);
    }

    private void initUrl(final ImageAdapter adapter) {

        CrawlerUtils.findIndex().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<GirlPhotoBean>() {
                    @Override
                    public void accept(GirlPhotoBean girlPhotoBean) throws Exception {
                        imageUrlList.add(girlPhotoBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e(TAG, "accept: " + throwable);
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        adapter.notifyDataSetChanged();
                    }
                });
    }

}
