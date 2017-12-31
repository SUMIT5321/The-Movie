package com.labs.jharkhandi.themovie.api;

import com.labs.jharkhandi.themovie.model.DiscoverResult;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by sumit on 29/12/17.
 */

public class ApiClient {

    public static String BASE_URL = "https://api.themoviedb.org/3/";

    private static TheMovieDBApiInterface theMovieDBApiInterface;

    public static TheMovieDBApiInterface getTheMovieDBApiInterface(){
        if(theMovieDBApiInterface == null){
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            theMovieDBApiInterface = retrofit.create(TheMovieDBApiInterface.class);
        }
        return theMovieDBApiInterface;
    }

    public interface TheMovieDBApiInterface{
        @GET("discover/movie")
        Call<DiscoverResult> getTopTrendingMovies(@Query("api_key") String apiKey,
                                                  @Query("language") String language,
                                                  @Query("region") String region,
                                                  @Query("sort_by") String sortBy,
                                                  @Query("certification_country") String certificationCountry,
                                                  @Query("page") Integer pageNo,
                                                  @Query("primary_release_date.gte") String releaseDateGT,
                                                  @Query("primary_release_date.lte") String releaseDateLT);

        @GET("discover/movie")
        Call<DiscoverResult> getTopMoviesByGenre(@Query("api_key") String apiKey,
                                                 @Query("language") String language,
                                                 @Query("region") String region,
                                                 @Query("sort_by") String sortBy,
                                                 @Query("certification_country") String certificationCountry,
                                                 @Query("page") Integer pageNo,
                                                 @Query("primary_release_year") Integer releaseYear,
                                                 @Query("with_genres") List<Integer> genres);
    }
}
