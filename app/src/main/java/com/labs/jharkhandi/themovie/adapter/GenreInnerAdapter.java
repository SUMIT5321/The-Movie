package com.labs.jharkhandi.themovie.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.labs.jharkhandi.themovie.R;
import com.labs.jharkhandi.themovie.model.Movie;

import java.util.List;

/**
 * Created by sumit on 30/12/17.
 */

public class GenreInnerAdapter extends RecyclerView.Adapter<GenreInnerAdapter.GenreInnerVH>{

    private Context context;
    private List<Movie> movieList;
    private LayoutInflater layoutInflater;
    private String imageBaseUrl = "https://image.tmdb.org/t/p/w500/";

    public GenreInnerAdapter(Context context, List<Movie> movieList) {
        this.context = context;
        this.movieList = movieList;
        this.layoutInflater = LayoutInflater.from(context);
    }

    public void setMovieList(List<Movie> movieList){
        this.movieList = movieList;
    }

    @Override
    public GenreInnerVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.genere_inner_unit, parent, false);
        return new GenreInnerVH(view);
    }

    @Override
    public void onBindViewHolder(GenreInnerVH holder, int position) {
        String bannerImageUrl = imageBaseUrl + movieList.get(position).getPosterPath();

        Glide.with(context)
                .load(bannerImageUrl)
                .fallback(R.drawable.genre_image_fallback)
                .error(R.drawable.genre_image_fallback)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.movieBanner);

        holder.movieTitle.setText(movieList.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public class GenreInnerVH extends RecyclerView.ViewHolder{

        private ImageView movieBanner;
        private TextView movieTitle;

        public GenreInnerVH(View itemView) {
            super(itemView);
            movieBanner = itemView.findViewById(R.id.genre_movie_image);
            movieTitle = itemView.findViewById(R.id.genre_movie_title);
        }
    }
}
