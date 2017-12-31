package com.labs.jharkhandi.themovie.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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
 * Created by sumit on 31/12/17.
 */

public class GenreListAdapter extends ArrayAdapter<Genre>{

    private Context context;
    private SparseArray<List<Movie>> genreWiseMovies;
    private SparseArray<RecyclerView> genreInnerRecyclers;

    public GenreListAdapter(@NonNull Context context, @LayoutRes int resource, List<Genre> genreList) {
        super(context, resource, genreList);
        this.context = context;

        Log.d("ListView", "From Listview Adapter");

        genreInnerRecyclers = new SparseArray<>(Genre.GenreList.size());

        /* send request for movie list */
        this.genreWiseMovies = new SparseArray<>(Genre.GenreList.size());
//        populateGenreWiseMovies();

    }

    private void populateGenreWiseMovies(final int position) {
        ApiClient.TheMovieDBApiInterface service = ApiClient.getTheMovieDBApiInterface();
//        for (final Genre genre:Genre.GenreList) {
            Call<DiscoverResult> call = service.getTopMoviesByGenre(context.getString(R.string.api_key),
                    Constants.QueryDefaultVal.LANG,
                    Constants.QueryDefaultVal.REGION, Constants.QueryDefaultVal.SORT_BY,
                    Constants.QueryDefaultVal.CERT_COUNTRY, Constants.QueryDefaultVal.PAGE_NO,
                    2017, Collections.singletonList(Genre.GenreList.get(position).getId()));

            call.enqueue(new Callback<DiscoverResult>() {
                @Override
                public void onResponse(Call<DiscoverResult> call, Response<DiscoverResult> response) {
                    List<Movie> movieList = response.body().getResults();
                    genreWiseMovies.put(Genre.GenreList.get(position).getId(),movieList);
                    ((GenreInnerAdapter)genreInnerRecyclers.get(Genre.GenreList.get(position).getId()).getAdapter()).setMovieList(movieList);
                    genreInnerRecyclers.get(Genre.GenreList.get(position).getId()).getAdapter().notifyDataSetChanged();
                    Log.d("MainActivity", "movie list size for genre " +
                            Genre.GenreList.get(position).getName() + ":" + movieList.size());
                }

                @Override
                public void onFailure(Call<DiscoverResult> call, Throwable t) {
                    Log.e("GenreAdapter", "unable to get movies by genre: " +
                            Genre.GenreList.get(position).getName() + " error: " + t.getMessage());
                }
            });
//        }
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Log.d("ListView", "ook getting called");
        View view = convertView;
        ViewHolder viewHolder;

        if (view == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(context);
            view  = inflater.inflate(R.layout.genere_unit, parent, false);

            viewHolder.genreHeading = view.findViewById(R.id.genre_heading);
            viewHolder.genreInnerRecycler = view.findViewById(R.id.genre_inner_recycler);

            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) view.getTag();
        }

//        genreInnerRecyclers.put(Genre.GenreList.get(position).getId(), genreInnerRecycler);

        viewHolder.genreHeading.setText(Genre.GenreList.get(position).getName());

        /* set layout manager */
        viewHolder.genreInnerRecycler.setLayoutManager(new LinearLayoutManager(context,
                LinearLayoutManager.HORIZONTAL, false));

        GenreInnerAdapter genreInnerAdapter = new GenreInnerAdapter(context,
                        new ArrayList<Movie>());
        viewHolder.genreInnerRecycler.setAdapter(genreInnerAdapter);

//        populateGenreWiseMovies(position);

        return view;
    }

    private static class ViewHolder {
        TextView genreHeading;
        RecyclerView genreInnerRecycler;
    }
}
