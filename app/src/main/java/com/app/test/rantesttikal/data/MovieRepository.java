package com.app.test.rantesttikal.data;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.util.Log;

import com.app.test.rantesttikal.Utils.AppExecutors;
import com.app.test.rantesttikal.Utils.NetManager;
import com.app.test.rantesttikal.data.dao.ProtocolDao;
import com.app.test.rantesttikal.data.database.AppDatabase;
import com.app.test.rantesttikal.data.model.Movie;
import com.app.test.rantesttikal.data.model.MoviesResponse;
import com.app.test.rantesttikal.network.RetrofitInstance;
import com.app.test.rantesttikal.network.TheMovieDbDataService;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Ran on 1/24/2018.
 */

public class MovieRepository {

    private final static String TAG = MovieRepository.class.getSimpleName();
    private final static String API_KEY = "6e87ff63eaa1e3d3a6dfbf27c0327825";

    //private Context mAppContext;
    private NetManager netManager;
    private ProtocolDao mProtocolDao;
    private AppExecutors mAppExecutors;

    public MovieRepository(Context applicationContext) {
        //mAppContext = applicationContext;
        netManager = new NetManager(applicationContext);
        AppDatabase database = AppDatabase.getAppDatabase(applicationContext);
        mProtocolDao = database.protocolDao();
        mAppExecutors = new AppExecutors();
    }

    public void getMovies(@NonNull OnMoviesReadyCallback callback) {
        if (netManager.isConnectedToInternet()) {
            getMoviesFromServer(callback);
        } else {
            getMoviesFromLocalDB(callback);
        }
    }

    private void getMoviesFromLocalDB(final OnMoviesReadyCallback callback) {
        Log.d(TAG, "Load movies from LOCAL DB");
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                final List<Movie> movies = mProtocolDao.getAllMovies();
                mAppExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if(movies == null){
                            // Room configuration fallbackToDestructiveMigration
                            // re-create the database with empty tables
                            // This will be called if the table is new or just empty.
                            Log.e(TAG, "LOCAL DB data not available");
                            callback.onDataNotAvailable();
                        } else {
                            ArrayList<Movie> arrayListMovies = new ArrayList<>(movies);
                            Log.d(TAG, "ArrayListMovies=" + arrayListMovies.size() + " ArrayList=" + movies.size());
                            callback.onDataReady(arrayListMovies);
                        }
                    }
                });
            }
        };
        mAppExecutors.diskIO().execute(runnable);
    }

    private void getMoviesFromServer(@NonNull final OnMoviesReadyCallback callback) {
        TheMovieDbDataService service = RetrofitInstance.getRetrofitInstance().
                create(TheMovieDbDataService.class);
        Call<MoviesResponse> call = service.getMoviesData(API_KEY);
        Log.wtf(TAG, "URL Called: " + call.request().url());
        call.enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                ArrayList<Movie> moviesList = response.body().getResults();
                Log.d(TAG, "Number of Movies: " + moviesList.size());
                //getAllPosterImages(moviesList);
                refreshLocalMovieDB(moviesList);
                callback.onDataReady(moviesList);
            }

            @Override
            public void onFailure(Call<MoviesResponse> call, Throwable t) {
                Log.e(TAG, "Error getting movies from server: " + t.getMessage());
                getMoviesFromLocalDB(callback);
            }
        });
    }

//    private void getAllPosterImages(final ArrayList<Movie> moviesList) {
//        Runnable glideRunnable = new Runnable() {
//            @Override
//            public void run() {
//                for(final Movie movie : moviesList){
//                    Glide.with(mAppContext)
//                            .load(movie.getPosterPath())
//                            .asBitmap()
//                            .into(new SimpleTarget<Bitmap>(100,100) {
//                                @Override
//                                public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation)  {
//                                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                                    resource.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//                                    movie.setImageBlob(baos.toByteArray());
//                                    movie.setImageBlobSize(movie.getImageBlob().length);
//                                }
//                            });
//                }
//            }
//        };
//        mAppExecutors.mainThread().execute(glideRunnable);
//    }

    private void refreshLocalMovieDB(ArrayList<Movie> movies) {
        deleteAllMovies();
        saveMovies(movies);
    }

    public void deleteAllMovies() {
        Runnable deleteRunnable = new Runnable() {
            @Override
            public void run() {
                mProtocolDao.deleteAll();
            }
        };
        mAppExecutors.diskIO().execute(deleteRunnable);
    }

    public void saveMovies(@NonNull final ArrayList<Movie> movies) {
        Runnable saveRunnable = new Runnable() {
            @Override
            public void run() {
                mProtocolDao.insert(movies);
            }
        };
        mAppExecutors.diskIO().execute(saveRunnable);
    }

    public interface OnMoviesReadyCallback {
        void onDataReady(ArrayList<Movie> movies);
        void onDataNotAvailable();
    }

}
