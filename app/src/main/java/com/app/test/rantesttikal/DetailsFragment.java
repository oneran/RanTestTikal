package com.app.test.rantesttikal;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.test.rantesttikal.data.model.Movie;
import com.bumptech.glide.Glide;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnDetailsInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private Movie mMovie;
    private TextView title;
    private TextView release;
    private TextView length;
    private TextView vote;
    private TextView overview;
    private ImageView mPoster;

    private OnDetailsInteractionListener mListener;

    public DetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param movie Parameter 1.
     * @return A new instance of fragment DetailsFragment.
     */
    public static DetailsFragment newInstance(Movie movie) {
        DetailsFragment fragment = new DetailsFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM1, movie);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mMovie = getArguments().getParcelable(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_details, container, false);
        title = v.findViewById(R.id.title_textView);
        release = v.findViewById(R.id.release_textView);
        length = v.findViewById(R.id.length_textView);
        vote = v.findViewById(R.id.vote_textView);
        overview = v.findViewById(R.id.overview_textView);
        mPoster = v.findViewById(R.id.poster_image);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(mMovie != null)
            setMovieLayout();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onDetailsFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnDetailsInteractionListener) {
            mListener = (OnDetailsInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnDetailsInteractionListener");
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
    public interface OnDetailsInteractionListener {
        // TODO: Update argument type and name
        void onDetailsFragmentInteraction(Uri uri);
    }

    public void setMovie(Movie movie){
        mMovie = movie;
        setMovieLayout();
    }

    private void setMovieLayout(){
        title.setText(mMovie.getTitle());
        release.setText(mMovie.getReleaseDate().toString());
        //length.setText(mMovie.get);
        vote.setText(mMovie.getVoteCount().toString());
        overview.setText(mMovie.getOverview());

        Glide.with(getContext())
                .load(mMovie.getPosterPath())
                .placeholder(R.color.colorAccent)
                .into(mPoster);
    }
}
