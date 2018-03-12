package com.kco.myapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.SuperKotlin.pictureviewer.ImagePagerActivity;
import com.SuperKotlin.pictureviewer.PictureConfig;
import com.kco.myapplication.activity.ImageActivity;
import com.kco.myapplication.bean.GirlPhotoBean;
import com.kco.myapplication.R;
import com.kco.myapplication.utils.CrawlerUtils;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by 666666 on 2018/3/9.
 */
public class ImageAdapter extends RecyclerView.Adapter<ImageViewHolder> {

    private List<GirlPhotoBean> mUrlList;
    private static final String TAG = "ImageAdapter";
    private Context context;
    public Map<String, List<String>> imageMap = new ConcurrentHashMap<>();

    public ImageAdapter(List<GirlPhotoBean> urlList, Context context) {
        this.mUrlList = urlList;
        this.context = context;
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.image_item, parent, false);
        ImageViewHolder holder = new ImageViewHolder(view, context);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GirlPhotoBean girlPhotoBean = (GirlPhotoBean) v.getTag();
                Intent intent = new Intent(context, ImageActivity.class);
                intent.putExtra("url", girlPhotoBean.getUrl());
                context.startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(final ImageViewHolder holder, int position) {
        final GirlPhotoBean girlPhotoBean = mUrlList.get(position);
        holder.textView.setText(girlPhotoBean.getName());
        Single.create(new SingleOnSubscribe<Bitmap>() {
            @Override
            public void subscribe(SingleEmitter<Bitmap> singleEmitter) throws Exception {
                singleEmitter.onSuccess(getBitmap(girlPhotoBean.getImageUrl()));
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Bitmap>() {
                    @Override
                    public void accept(Bitmap bitmap) throws Exception {
                        holder.imageView.setImageBitmap(bitmap);
                    }
                });
        holder.itemView.setTag(girlPhotoBean);
    }

    public Bitmap getBitmap(String path) {
        try {
            URL url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setRequestMethod("GET");
            if (conn.getResponseCode() == 200) {
                InputStream inputStream = conn.getInputStream();
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                return bitmap;
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount: ");
        return mUrlList.size();
    }


}
