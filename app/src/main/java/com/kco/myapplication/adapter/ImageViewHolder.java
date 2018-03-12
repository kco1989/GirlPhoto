package com.kco.myapplication.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.kco.myapplication.R;

/**
 * Created by 666666 on 2018/3/12.
 */
public class ImageViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.girl_group_img)
    ImageView imageView;
    @BindView(R.id.girl_group_name)
    TextView textView;

    private Context context;

    public ImageViewHolder(View itemView, Context context) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        this.context = context;
    }
}
