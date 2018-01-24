package com.app.test.rantesttikal.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.app.test.rantesttikal.R;
import com.app.test.rantesttikal.data.model.Movie;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by Ran on 1/23/2018.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private Context mContext;
    private ArrayList<Movie> dataList;
    private OnMovieItemClickedListener mListener;

    public MovieAdapter(Context context, OnMovieItemClickedListener listener) {
        this.mContext = context;
        mListener = listener;
//        if (context instanceof OnMovieItemClickedListener) {
//            mListener = (OnMovieItemClickedListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnMovieItemClickedListener");
//        }
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.movie_image, parent, false);
        final MovieViewHolder viewHolder = new MovieViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = viewHolder.getAdapterPosition();
                mListener.OnMovieItemClicked(dataList.get(position));
//                Intent intent = new Intent(mContext, MovieDetailActivity.class);
//                intent.putExtra(MovieDetailActivity.EXTRA_MOVIE, mMovieList.get(position));
//                mContext.startActivity(intent);
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        Movie movie = dataList.get(position);
        Glide.with(mContext)
            .load(movie.getPosterPath())
            .placeholder(R.color.colorAccent)
            .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return (dataList == null) ? 0 : dataList.size();
    }

    public void setMovieList(ArrayList<Movie> movieList) {
        this.dataList = new ArrayList<>();
        this.dataList.addAll(movieList);
        notifyDataSetChanged();
    }

    class MovieViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public MovieViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }

    public interface OnMovieItemClickedListener {
        void OnMovieItemClicked(Movie movie);
    }

}
