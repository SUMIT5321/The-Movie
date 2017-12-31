package com.labs.jharkhandi.themovie.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.labs.jharkhandi.themovie.R;
import com.labs.jharkhandi.themovie.api.ApiClient;
import com.labs.jharkhandi.themovie.model.DiscoverResult;
import com.labs.jharkhandi.themovie.model.Genre;
import com.labs.jharkhandi.themovie.model.Movie;
import com.labs.jharkhandi.themovie.utillity.Constants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by sumit on 30/12/17.
 */

public class GenreAdapter extends RecyclerView.Adapter<GenreAdapter.GenreVH>{

    private static int INITIAL_PREFETCH_COUNT = 3;

    private Context context;
    private List<Genre> genreList;
    private LayoutInflater layoutInflater;
    private SparseArray<GenreInnerAdapter> genreInnerAdapters;
    private SparseArray<List<Movie>> genreWiseMovies;
    private RecyclerView.RecycledViewPool recyclerViewPool;

    public GenreAdapter(Context context, final List<Genre> genreList) {
        this.context = context;
        this.genreList = genreList;
        this.layoutInflater = LayoutInflater.from(context);

        recyclerViewPool = new RecyclerView.RecycledViewPool();

         /* initialize all adapters */
        this.genreInnerAdapters = new SparseArray<>(genreList.size());
        for (Genre genre: genreList) {
            genreInnerAdapters.put(genre.getId(),new GenreInnerAdapter(context,
                            new ArrayList<Movie>()));
        }

        /* send request for movie list */
        this.genreWiseMovies = new SparseArray<>(genreList.size());
        populateGenreWiseMovies();
    }

    private void populateGenreWiseMovies() {
        ApiClient.TheMovieDBApiInterface service = ApiClient.getTheMovieDBApiInterface();
        for (final Genre genre:genreList) {
            Call<DiscoverResult> call = service.getTopMoviesByGenre(context.getString(R.string.api_key),
                    Constants.QueryDefaultVal.LANG,
                    Constants.QueryDefaultVal.REGION, Constants.QueryDefaultVal.SORT_BY,
                    Constants.QueryDefaultVal.CERT_COUNTRY, Constants.QueryDefaultVal.PAGE_NO,
                    2017, Collections.singletonList(genre.getId()));

//            test(call, genre);
            call.enqueue(new Callback<DiscoverResult>() {
                @Override
                public void onResponse(Call<DiscoverResult> call, Response<DiscoverResult> response) {
                    List<Movie> movieList = response.body().getResults();
                    genreWiseMovies.put(genre.getId(),movieList);

                    genreInnerAdapters.get(genre.getId()).setMovieList(movieList);
                    genreInnerAdapters.get(genre.getId()).notifyDataSetChanged();
                    Log.d("MainActivity", "movie list size for genre " +
                            genre.getName() + ":" + movieList.size());
                }

                @Override
                public void onFailure(Call<DiscoverResult> call, Throwable t) {
                    Log.e("GenreAdapter", "unable to get movies by genre: " +
                            genre.getName() + " error: " + t.getMessage());
                }
            });
        }
    }

    @Override
    public GenreVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.genere_unit, parent, false);
        GenreVH genreVH = new GenreVH(view);
        genreVH.genreInnerRecycler.setRecycledViewPool(recyclerViewPool);
        genreVH.genreInnerRecycler.setNestedScrollingEnabled(false);
        return genreVH;
    }

    @Override
    public void onBindViewHolder(GenreVH holder, int position) {
        /* Set Genre Heading */
        holder.genreHeading.setText(genreList.get(position).getName());

        /* set layout manager to inner recycler*/
        if(holder.genreInnerRecycler.getLayoutManager() == null){
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context,
                    LinearLayoutManager.HORIZONTAL, false);
            linearLayoutManager.setInitialPrefetchItemCount(INITIAL_PREFETCH_COUNT);
            holder.genreInnerRecycler.setLayoutManager(linearLayoutManager);
        }

        /* set adapter to inner recycler */
        holder.genreInnerRecycler.setAdapter(genreInnerAdapters.get(genreList.get(position).getId()));
    }

    @Override
    public int getItemCount() {
        return genreList.size();
    }

    public class GenreVH extends RecyclerView.ViewHolder{
        private TextView genreHeading;
        private RecyclerView genreInnerRecycler;

        public GenreVH(View itemView) {
            super(itemView);
            genreHeading = itemView.findViewById(R.id.genre_heading);
            genreInnerRecycler = itemView.findViewById(R.id.genre_inner_recycler);
        }
    }
}