package com.kco.myapplication;

import android.content.Context;
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
public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {

    private List<GirlPhotoBean> mUrlList;
    private static final String TAG = "ImageAdapter";
    private Context context;
    public Map<String, List<String>> imageMap = new ConcurrentHashMap<>();

    public ImageAdapter(List<GirlPhotoBean> urlList, Context context) {
        this.mUrlList = urlList;
        this.context = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.girl_group_img)
        ImageView imageView;
        @BindView(R.id.girl_group_name)
        TextView textView;

        private Context context;

        public ViewHolder(View itemView, Context context) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.context = context;
        }
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.image_item, parent, false);
        ViewHolder holder = new ViewHolder(view, context);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GirlPhotoBean girlPhotoBean = (GirlPhotoBean) v.getTag();
                List<String> list = imageMap.get(girlPhotoBean);
                if (list == null || list.isEmpty()){
                    CrawlerUtils.getImageUrl(girlPhotoBean.getUrl())
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
                                            .setListData(strings)    //图片数据List<String> list
                                            .setPosition(0)    //图片下标（从第position张图片开始浏览）
                                            .setDownloadPath("pictureviewer")    //图片下载文件夹地址
                                            .setIsShowNumber(true)//是否显示数字下标
                                            .needDownload(true)    //是否支持图片下载
                                            .setPlacrHolder(R.drawable.koala)    //占位符图片（图片加载完成前显示的资源图片，来源drawable或者mipmap）
                                            .build();
                                    ImagePagerActivity.startActivity(context, config);

                                }
                            });
                }else{
                    PictureConfig config = new PictureConfig.Builder()
                            .setListData((ArrayList<String>) list)    //图片数据List<String> list
                            .setPosition(0)    //图片下标（从第position张图片开始浏览）
                            .setDownloadPath("pictureviewer")    //图片下载文件夹地址
                            .setIsShowNumber(true)//是否显示数字下标
                            .needDownload(true)    //是否支持图片下载
                            .setPlacrHolder(R.drawable.koala)    //占位符图片（图片加载完成前显示的资源图片，来源drawable或者mipmap）
                            .build();
                    ImagePagerActivity.startActivity(context, config);
                }

            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
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
