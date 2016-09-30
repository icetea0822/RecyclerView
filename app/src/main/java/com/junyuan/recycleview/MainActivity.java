package com.junyuan.recycleview;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    RecyclerView rv;
    MyRecyclerViewAdapter adapter;
    String json;
    Movie movie;
    String url = "https://api.douban.com/v2/movie/top250";
    List<Movie.SubjectsBean> movies = new ArrayList<>();
    boolean isFirst = true;
    Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            json = bundle.getString("json");
            Gson gson = new Gson();
            movie = gson.fromJson(json, Movie.class);
            for (int i = 0; i < movie.getSubjects().size(); i++) {
                movies.add(movie.getSubjects().get(i));
            }

            if (msg.what == 1) {
                adapter = new MyRecyclerViewAdapter(MainActivity.this, movies);
                adapter.footerOnClickListener(new FooterClickListener() {
                    @Override
                    public void footerOnClickListener(View view) {
                        Toast.makeText(MainActivity.this, "正在加载更多", Toast.LENGTH_SHORT).show();
                        start += 15;
                        getMovie(MainActivity.this.url);
                    }
                });
                rv.setAdapter(adapter);
                adapter.notifyItemInserted(movies.size());
//                rv.smoothScrollToPosition(movies.size() + 1);
                Log.d("123", "电影信息" + movie.getSubjects().toString());
            } else {
                adapter.notifyItemInserted(movies.size() + 1);
            }
            return false;
        }
    });
    int start = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }

        rv = (RecyclerView) findViewById(R.id.rv);
        toolbar = (Toolbar) findViewById(R.id.tb);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rv != null && adapter != null) {
                    rv.smoothScrollToPosition(0);
                }
            }
        });

        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setItemAnimator(new DefaultItemAnimator());

        getMovie(url);


    }


    public void getMovie(String url) {
        String startString = String.valueOf(start);
//        String countString = String.valueOf(count);
        OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder().add("start", startString).add("count", "15").build();


        final Request request = new Request.Builder().url(url)
                .post(requestBody).build();

        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                json = response.body().string();
                Message msg = new Message();
                if (isFirst) {
                    msg.what = 1;
                    isFirst = false;
                }
                Bundle data = new Bundle();
                data.putString("json", json);
                msg.setData(data);
                mHandler.sendMessage(msg);
            }
        });
    }
}
