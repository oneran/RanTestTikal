package com.app.test.rantesttikal;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.test.rantesttikal.adapter.MovieAdapter;
import com.app.test.rantesttikal.data.MovieRepository;
import com.app.test.rantesttikal.data.model.Movie;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnGalleryInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GalleryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GalleryFragment extends Fragment implements MovieAdapter.OnMovieItemClickedListener{

    private final static String TAG = GalleryFragment.class.getSimpleName();

    private MovieAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private MovieRepository mRepository;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnGalleryInteractionListener mListener;

    public GalleryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GalleryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GalleryFragment newInstance(String param1, String param2) {
        GalleryFragment fragment = new GalleryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);
        mRecyclerView = view.findViewById(R.id.recycler_view_movie_list);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnGalleryInteractionListener) {
            mListener = (OnGalleryInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnDetailsInteractionListener");
        }
        mRepository = new MovieRepository(getActivity().getApplication().getApplicationContext());
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        mAdapter = new MovieAdapter(getContext(), this);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);
        mRepository.getMovies(new RepositoryCallBack(mAdapter));
    }

    private static class RepositoryCallBack implements MovieRepository.OnMoviesReadyCallback {

        private final WeakReference<MovieAdapter> movieAdapterWeakReference;

        public RepositoryCallBack(MovieAdapter adapter){
            this.movieAdapterWeakReference = new WeakReference<MovieAdapter>(adapter);
        }

        @Override
        public void onDataReady(ArrayList<Movie> movies) {
            MovieAdapter movieAdapter = movieAdapterWeakReference.get();
            if(movieAdapter != null) {
                movieAdapter.setMovieList(movies);
            }
        }

        @Override
        public void onDataNotAvailable() {
            //
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnGalleryInteractionListener {
        void onFragmentInteraction(Movie movie);
    }

    @Override
    public void OnMovieItemClicked(Movie movie) {
        Log.d(TAG, "click on item: " + movie.getTitle() );
        if (mListener != null) {
            mListener.onFragmentInteraction(movie);
        }
    }
}
