package com.app.test.rantesttikal.network;

import com.app.test.rantesttikal.data.model.MoviesResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Ran on 1/23/2018.
 */

public interface TheMovieDbDataService {

//    @GET("3/discover/movie/{categories}")
//    Call<MovieList> getMoviesData(@Path("categories") String categories, @Query("api_key") String apiKey);


    @GET("discover/movie?sort_by=popularity.desc")
    Call<MoviesResponse> getMoviesData(@Query("api_key") String apiKey);

    @GET("movie/{id}")
    Call<MoviesResponse> getMovieDetails(@Path("id") int id, @Query("api_key") String apiKey);
}
