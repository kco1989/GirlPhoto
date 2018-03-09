package com.kco.myapplication;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.SuperKotlin.pictureviewer.ImagePagerActivity;
import com.SuperKotlin.pictureviewer.PictureConfig;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 666666 on 2018/3/9.
 */
public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {

    private List<String> mUrlList;
    private static final String TAG = "ImageAdapter";
    private Context context;
    public ImageAdapter(Context context){
        this.mUrlList = new ArrayList<>();
        this.context = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.image_button)
        public Button button;
        private Context context;
        public ViewHolder(View itemView, Context context) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.context = context;
        }

        @OnClick(R.id.image_button)
        public void onClick(View view){
            Log.d(TAG, "onClick: " + view);
            List<String> list = new ArrayList<>();
            CrawlerUtils.getImageUrl((String) ((Button)view).getText())
                    .subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.io())
                    .reduce(new ArrayList<String>(), new BiFunction<ArrayList<String>, String, ArrayList<String>>() {
                        @Override
                        public ArrayList<String> apply(ArrayList<String> objects, String s) throws Exception {
                            objects.add(s);
                            return objects;
                        }
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<ArrayList<String>>() {
                        @Override
                        public void accept(ArrayList<String> strings) throws Exception {
                            PictureConfig config = new PictureConfig.Builder()
                            .setListData(strings)	//图片数据List<String> list
                            .setPosition(0)	//图片下标（从第position张图片开始浏览）
                            .setDownloadPath("pictureviewer")	//图片下载文件夹地址
                            .setIsShowNumber(true)//是否显示数字下标
                            .needDownload(true)	//是否支持图片下载
                            .setPlacrHolder(R.drawable.koala)	//占位符图片（图片加载完成前显示的资源图片，来源drawable或者mipmap）
                            .build();
                            ImagePagerActivity.startActivity(context , config);
                        }
                    });
        }
    }



    public void addItem(String url){
        this.mUrlList.add(url);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.image_item, parent, false);
        ViewHolder holder = new ViewHolder(view, context);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String url = mUrlList.get(position);
        holder.button.setText(url);
    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount: ");
        return mUrlList.size();
    }


}
