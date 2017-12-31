package com.labs.jharkhandi.themovie.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.labs.jharkhandi.themovie.R;
import com.labs.jharkhandi.themovie.adapter.GenreAdapter;
import com.labs.jharkhandi.themovie.adapter.GenreInnerAdapter;
import com.labs.jharkhandi.themovie.adapter.GenreListAdapter;
import com.labs.jharkhandi.themovie.adapter.TrendingAdapter;
import com.labs.jharkhandi.themovie.api.ApiClient;
import com.labs.jharkhandi.themovie.model.DiscoverResult;
import com.labs.jharkhandi.themovie.model.Genre;
import com.labs.jharkhandi.themovie.model.Movie;
import com.labs.jharkhandi.themovie.utillity.Constants;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mTrendingRecycler;

    private TrendingAdapter mTrendingAdapter;

    private List<Movie> mTrendingList;

//    private CardView mLoadingCard;

    private RecyclerView mGenreRecycler;

    private GenreAdapter mGenreAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* Set the support action bar */
        Toolbar toolBar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolBar);

        bindViews();

        setUpTrendingRecycler();

        setUpGenreRecycler();


    }

    private void bindViews() {
        /* bind trending recycler */
        mTrendingRecycler = findViewById(R.id.trending_recycler);

        /* bind loading banner */
//        mLoadingCard = findViewById(R.id.loading_card);

        mGenreRecycler = findViewById(R.id.genre_recycler);

       /* mGenreListView = findViewById(R.id.genre_list);
        GenreListAdapter genreListAdapter = new GenreListAdapter(this, R.layout.genere_unit, Genre.GenreList);
        mGenreListView.setAdapter(genreListAdapter);*/

    }

    private void setUpTrendingRecycler() {
        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(mTrendingRecycler);

        mTrendingRecycler.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false));

        mTrendingAdapter = new TrendingAdapter(this, new ArrayList<Movie>());

        mTrendingRecycler.setAdapter(mTrendingAdapter);
    }

    private void setUpGenreRecycler() {
        mGenreRecycler.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));
        /* for smooth scrolling */
        mGenreRecycler.setNestedScrollingEnabled(false);

        mGenreAdapter = new GenreAdapter(this, Genre.GenreList);
        mGenreRecycler.setAdapter(mGenreAdapter);
    }

    @Override
    @SuppressLint("DefaultLocale")
    protected void onResume() {
        super.onResume();

        ApiClient.TheMovieDBApiInterface service = ApiClient.getTheMovieDBApiInterface();
        if(mTrendingList == null){
            Calendar today = Calendar.getInstance();

            String todayString = String.format("%d-%02d-%02d", today.get(Calendar.YEAR),
                    today.get(Calendar.MONTH)+1, today.get(Calendar.DAY_OF_MONTH));

            today.add(Calendar.DAY_OF_MONTH, -30);
            String oneMonthBackString = String.format("%d-%02d-%02d", today.get(Calendar.YEAR),
                    today.get(Calendar.MONTH)+1, today.get(Calendar.DAY_OF_MONTH));

            Log.d("MainActivity", oneMonthBackString + " " + todayString);

            Call<DiscoverResult> call =service.getTopTrendingMovies(getString(R.string.api_key),
                    Constants.QueryDefaultVal.LANG, Constants.QueryDefaultVal.REGION,
                    Constants.QueryDefaultVal.SORT_BY, Constants.QueryDefaultVal.CERT_COUNTRY,
                    Constants.QueryDefaultVal.PAGE_NO, oneMonthBackString, todayString);

            call.enqueue(new Callback<DiscoverResult>() {
                @Override
                public void onResponse(Call<DiscoverResult> call, Response<DiscoverResult> response) {
                    mTrendingList = response.body().getResults();
//                    mLoadingCard.setVisibility(View.INVISIBLE);
                    mTrendingAdapter.setTrendingMovieList(mTrendingList);
                    mTrendingAdapter.notifyDataSetChanged();
                }

                @Override
                public void onFailure(Call<DiscoverResult> call, Throwable t) {
                    Log.e("MainActivity", "Error in fetching data: " + t.getMessage());
                }
            });
        }
    }
}