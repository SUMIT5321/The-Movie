package com.labs.jharkhandi.themovie.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.labs.jharkhandi.themovie.R;
import com.labs.jharkhandi.themovie.model.Movie;

import java.util.List;

/**
 * Created by sumit on 30/12/17.
 */

public class TrendingAdapter extends RecyclerView.Adapter<TrendingAdapter.TrendingVH>{

    private static final int VIEW_TYPE_EMPTY_LIST_PLACEHOLDER = 0;
    private static final int VIEW_TYPE_NORMAL_VIEW = 1;

    private List<Movie> trendingMovieList;
    private Context context;
    private int MAX_COUNT = 10;
    private LayoutInflater layoutInflater;
    private String imageBaseUrl = "https://image.tmdb.org/t/p/w1280/";

    public TrendingAdapter(Context context, List<Movie> trendingMovieList) {
        this.trendingMovieList = trendingMovieList;
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    public void setTrendingMovieList(List<Movie> movieList){
        this.trendingMovieList =  movieList;
    }

    @Override
    public TrendingVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = layoutInflater.inflate(R.layout.trending_unit, parent,false);
        TrendingVH trendingVH = new TrendingVH(v);
        switch (viewType){
            case VIEW_TYPE_EMPTY_LIST_PLACEHOLDER:
                trendingVH.bannerNum.setVisibility(View.GONE);
                trendingVH.movieTitle.setVisibility(View.GONE);
                break;
            case VIEW_TYPE_NORMAL_VIEW:
                trendingVH.progressBar.setVisibility(View.GONE);
                break;
        }
        return trendingVH;
    }

    @Override
    public int getItemViewType(int position) {
        if(trendingMovieList.isEmpty()){
            return VIEW_TYPE_EMPTY_LIST_PLACEHOLDER;
        }else{
            return VIEW_TYPE_NORMAL_VIEW;
        }
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(TrendingVH holder, int position) {

        /* Don't do any thing if this is an empty list case */
        int viewType = getItemViewType(position);
        if(VIEW_TYPE_EMPTY_LIST_PLACEHOLDER == viewType){
            return;
        }

        ImageView bannerImage = holder.bannerImage;

        String bannerImageUri = imageBaseUrl + trendingMovieList.get(position)
                .getBackdropPath();
        Glide.with(context)
                .load(bannerImageUri)
                .fallback(R.drawable.image_fallback)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(bannerImage);

        holder.movieTitle.setText(trendingMovieList.get(position).getTitle());
        holder.bannerNum.setText(String.format("%02d/%d", position+1, getItemCount()));
    }

    @Override
    public int getItemCount() {

        /* to show empty state view */
        if(trendingMovieList.size() == 0) return 1;

        return trendingMovieList.size() > MAX_COUNT ?
                MAX_COUNT: trendingMovieList.size();
    }

    public class TrendingVH extends RecyclerView.ViewHolder{

        private View view;
        private ImageView bannerImage;
        private TextView movieTitle;
        private TextView bannerNum;
        private ProgressBar progressBar;

        public TrendingVH(View itemView) {
            super(itemView);
            this.view = itemView;
            this.bannerImage = itemView.findViewById(R.id.banner_image);
            this.movieTitle = itemView.findViewById(R.id.movie_title);
            this.bannerNum = itemView.findViewById(R.id.banner_number);
            this.progressBar = itemView.findViewById(R.id.progress_bar);
        }
    }
}