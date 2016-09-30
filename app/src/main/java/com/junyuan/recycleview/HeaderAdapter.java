package com.junyuan.recycleview;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by JY on 2016/9/30.
 */

public class HeaderAdapter extends PagerAdapter {

    Context context;
    ArrayList<ImageView> images;

    public HeaderAdapter(Context context, ArrayList<ImageView> images) {
        this.context = context;
        if (images == null) {
            this.images = new ArrayList<>();
        } else {
            this.images = images;
        }
    }


    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView iv = images.get(position);
        container.addView(iv);
        return iv;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
