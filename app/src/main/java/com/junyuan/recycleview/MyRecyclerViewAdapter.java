package com.junyuan.recycleview;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by JY on 2016/9/29.
 */

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    List<Movie.SubjectsBean> movies;
    public static final int TYPE_HEADER = 0;  //说明是带有Header的
    public static final int TYPE_FOOTER = 1;  //说明是带有Footer的
    public static final int TYPE_ITEM = 2;  //说明是不带有header和footer的
    MyViewHolder item;
    VHHeader header;
    VHFooter footer;
    FooterClickListener mFooterClickListener;
    HeaderAdapter adapter;
    ArrayList<ImageView> images = new ArrayList<>();
    Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {

            return false;
        }
    });


    public MyRecyclerViewAdapter(Context context, List<Movie.SubjectsBean> movies) {
        this.context = context;
        if (movies == null) {
            this.movies = new ArrayList<>();
        } else {
            this.movies = movies;
        }

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d("1233", "viewtype为：" + viewType);
        if (viewType == TYPE_ITEM) {
            View holder = LayoutInflater.from(context).inflate(R.layout.item_recyclerview, parent, false);
            item = new MyViewHolder(holder);
            return item;
        } else if (viewType == TYPE_HEADER) {
            ImageView iv1 = new ImageView(context);
            iv1.setScaleType(ImageView.ScaleType.FIT_XY);
            Glide.with(context).load("http://img1.imgtn.bdimg.com/it/u=4044918537,3514587026&fm=21&gp=0.jpg").into(iv1);
            images.add(iv1);

            ImageView iv2 = new ImageView(context);
            iv2.setScaleType(ImageView.ScaleType.FIT_XY);
            Glide.with(context).load("http://pic2.ooopic.com/13/58/87/63bOOOPICb3_1024.jpg").into(iv2);
            images.add(iv2);
            View holder = LayoutInflater.from(context).inflate(R.layout.header_recyclerview, parent, false);
            header = new VHHeader(holder);
            return header;
        } else if (viewType == TYPE_FOOTER) {
            LinearLayout ll = new LinearLayout(context);
            ll.setGravity(Gravity.CENTER_HORIZONTAL);
            footer = new VHFooter(ll);
            return footer;
        }
        throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");

    }


    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEADER;
        } else if (position == movies.size() + 1) {
            return TYPE_FOOTER;
        }
        return TYPE_ITEM;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof MyViewHolder) {//设置正常内容的布局
            String picUrl = movies.get(position - 1).getImages().getMedium();
            Glide.with(context).load(picUrl).into(((MyViewHolder) holder).ivPic);

            ((MyViewHolder) holder).tvTitle.setText(position + " " + movies.get(position - 1).getTitle());
            String directors = "";
            for (int i = 0; i < movies.get(position - 1).getDirectors().size(); i++) {
                directors += movies.get(position - 1).getDirectors().get(i).getName();
                if (i < movies.get(position - 1).getDirectors().size() - 1) {
                    directors += "/";
                }
            }
            ((MyViewHolder) holder).tvDirector.setText("导演：" + directors);
            String cast = "";
            for (int i = 0; i < movies.get(position - 1).getCasts().size(); i++) {
                cast += movies.get(position - 1).getCasts().get(i).getName();
                if (i < movies.get(position - 1).getCasts().size() - 1) {
                    cast += "/";
                }
            }
            ((MyViewHolder) holder).tvCasts.setText("演员：" + cast);
            ((MyViewHolder) holder).tvRate.setText("评分：" + movies.get(position - 1).getRating().getAverage());
        } else if (holder instanceof VHHeader) {//设置头部布局



            if(adapter!=null) {
                adapter.notifyDataSetChanged();
            }

        } else if (holder instanceof VHFooter) {//设置底部布局
            footer.button.setText("点老子加载更多");
            footer.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mFooterClickListener.footerOnClickListener(v);
                }
            });
        }
    }

    public void footerOnClickListener(FooterClickListener footerClickListener) {
        this.mFooterClickListener = footerClickListener;
    }

    @Override
    public int getItemCount() {
        return movies.size() + 2;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView ivPic;
        TextView tvTitle, tvDirector, tvCasts, tvRate;

        public MyViewHolder(View itemView) {
            super(itemView);

            ivPic = (ImageView) itemView.findViewById(R.id.ivPic);
            tvDirector = (TextView) itemView.findViewById(R.id.tvDirector);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            tvCasts = (TextView) itemView.findViewById(R.id.tvCasts);
            tvRate = (TextView) itemView.findViewById(R.id.tvRate);

        }
    }

    class VHHeader extends RecyclerView.ViewHolder {
        ViewPager vp;

        public VHHeader(View itemView) {
            super(itemView);

            vp = (ViewPager) itemView.findViewById(R.id.vp);
            adapter = new HeaderAdapter(context, images);
            vp.setAdapter(adapter);
        }
    }

    class VHFooter extends RecyclerView.ViewHolder {
        Button button;

        public VHFooter(View itemView) {
            super(itemView);
            LinearLayout ll = (LinearLayout) itemView;
            button = new Button(context);
            ll.addView(button);
        }
    }


}




