package com.ygaps.travelapp.Activity.Fragments;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ygaps.travelapp.Activity.R;
import com.ygaps.travelapp.Api.MyAPIClient;
import com.ygaps.travelapp.Api.UserService;
import com.ygaps.travelapp.Object.PointStat;
import com.ygaps.travelapp.model.DetailServiceResponse;
import com.ygaps.travelapp.model.FeedbackServiceRequest;
import com.ygaps.travelapp.model.PointServiceResponse;
import com.ygaps.travelapp.model.StopPointResponse;
import com.taufiqrahman.reviewratings.BarLabels;
import com.taufiqrahman.reviewratings.RatingReviews;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link GeneralFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GeneralFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GeneralFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private int id;

    private OnFragmentInteractionListener mListener;

    public GeneralFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GeneralFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GeneralFragment newInstance(String param1, String param2) {
        GeneralFragment fragment = new GeneralFragment();
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
            id = getArguments().getInt("id", 0);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_general, container, false);
        Button send = v.findViewById(R.id.point_info_send_review);
        final RatingBar userRating = v.findViewById(R.id.point_info_user_rating);
        LayerDrawable stars = (LayerDrawable) userRating.getProgressDrawable();
        final RatingReviews ratingReviews = v.findViewById(R.id.point_info_rating_reviews);
        final TextView name = v.findViewById(R.id.point_info_name);
        final TextView address = v.findViewById(R.id.point_info_address);
        final TextView priceTour = v.findViewById(R.id.point_info_pricetour);
        final TextView levelTour = v.findViewById(R.id.point_info_priceLevel);
        final TextView selfPoint = v.findViewById(R.id.point_info_textView);
        final TextView total = v.findViewById(R.id.point_info_total);
        final RatingBar selfRatingBar = v.findViewById(R.id.point_info_ratingBar);
        final TextView serviceType = v.findViewById(R.id.point_info_service_type);
        final ImageView imgType = v.findViewById(R.id.point_info_service_type_img);
        final EditText userReview = v.findViewById(R.id.point_info_user_reviews);

        stars.getDrawable(2).setColorFilter(Color.parseColor("#0f9d58"), PorterDuff.Mode.SRC_ATOP); // for filled stars

        final UserService userService;
        userService = MyAPIClient.getInstance().getAdapter().create(UserService.class);
        if (id != 0){
            userService.getDetailService(id, new Callback<DetailServiceResponse>() {
                @Override
                public void success(DetailServiceResponse detailServiceResponse, Response response) {
                    name.setText(detailServiceResponse.getName());
                    address.setText(detailServiceResponse.getAddress());
                    priceTour.setText(detailServiceResponse.getMinCost() + " VND - " + detailServiceResponse.getMaxCost() + " VND");
                    long levelPrice = Math.abs(detailServiceResponse.getMaxCost() - detailServiceResponse.getMinCost());
                    if (levelPrice < 5000000) {
                        levelTour.setText("Normal");
                    } else {
                        levelTour.setText("VIP");
                    }

                    if (detailServiceResponse.getServiceTypeId() == 1){
                        serviceType.setText("Restaurant");
                        imgType.setImageResource(R.drawable.ic_cutlery);
                    }
                    else if (detailServiceResponse.getServiceTypeId() == 2){
                        serviceType.setText("Hotel");
                        imgType.setImageResource(R.drawable.ic_hotel);
                    }
                    else if (detailServiceResponse.getServiceTypeId() == 3){
                        serviceType.setText("Rest Station");
                        imgType.setImageResource(R.drawable.ic_parking);
                    }
                    else if (detailServiceResponse.getServiceTypeId() == 4){
                        serviceType.setText("Other");
                        imgType.setImageResource(R.drawable.ic_24_hours);
                    }
                    if (detailServiceResponse.getSelfStarRatings() != null) {
                        selfPoint.setText(String.valueOf(detailServiceResponse.getSelfStarRatings()));
                        selfRatingBar.setRating(detailServiceResponse.getSelfStarRatings());
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    Toast.makeText(getActivity(), "Fail", Toast.LENGTH_SHORT).show();
                }
            });

            userService.getFeedbackPoint(id, new Callback<PointServiceResponse>() {
                @Override
                public void success(PointServiceResponse pointServiceResponse, Response response) {
                    List<PointStat> list = pointServiceResponse.getPointStats();
                    int[] raters = new int[5];
                    for (int j = 0; j<5; j++) {
                        raters[j] = list.get(j).getTotal();
                    }
                    int s = 0;
                    for (int i:raters) {
                        s+=i;
                    }
                    total.setText(String.valueOf(s));
                    ratingReviews.createRatingBars(100, BarLabels.STYPE3, Color.parseColor("#0f9d58"), raters);
                }

                @Override
                public void failure(RetrofitError error) {

                }
            });

            send.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FeedbackServiceRequest feedbackServiceRequest = new FeedbackServiceRequest();
                    feedbackServiceRequest.setServiceId(id);
                    if (userRating.getRating() > 0 && !userReview.getText().toString().equals("")) {
                        feedbackServiceRequest.setPoint(Math.round(userRating.getRating()));
                        feedbackServiceRequest.setFeedback(userReview.getText().toString());
                        userService.sendFeedbackService(feedbackServiceRequest, new Callback<StopPointResponse>() {
                            @Override
                            public void success(StopPointResponse stopPointResponse, Response response) {
                                Toast.makeText(getActivity(), "Thanks for your rating!", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void failure(RetrofitError error) {
                                Toast.makeText(getActivity(), "Fail", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            });

        }

        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
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
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
