package com.example.josh.pointsofinterest;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.List;

public class PlacesRecyclerViewAdapter extends RecyclerView.Adapter<PlacesRecyclerViewAdapter.ViewHolder> {

    private final List<PlaceModel> mPlaceModels;
    private Context mContext;

    public PlacesRecyclerViewAdapter(List<PlaceModel> placeModels, Context context) {
        mPlaceModels = placeModels;
        mContext = context;
    }

    @Override
    public PlacesRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View placeView = inflater.inflate(R.layout.places_list_item, parent, false);
        return new ViewHolder(placeView);
    }

    @Override
    public void onBindViewHolder(PlacesRecyclerViewAdapter.ViewHolder viewHolder, int position) {
        PlaceModel placeModel = mPlaceModels.get(position);
        viewHolder.mAddressView.setText(placeModel.getAddress());
        viewHolder.mDescriptionView.setText(placeModel.getDescription());
        viewHolder.mPlaceNameView.setText(placeModel.getName());
        viewHolder.mRatingBarView.setRating(placeModel.getRating());
        viewHolder.mRatingValueView.setText(placeModel.getRating() + "");
        viewHolder.mRatingValueView.setVisibility(placeModel.getRating() >= 0 ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public int getItemCount() {
        return mPlaceModels.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public final TextView mPlaceNameView;
        public final TextView mRatingValueView;
        public final RatingBar mRatingBarView;
        public final TextView mDescriptionView;
        public final TextView mAddressView;

        public ViewHolder(View itemView) {
            super(itemView);
            mPlaceNameView = (TextView) itemView.findViewById(R.id.placeNameView);
            mRatingValueView = (TextView) itemView.findViewById(R.id.ratingValueView);
            mRatingBarView = (RatingBar) itemView.findViewById(R.id.ratingBarView);
            mDescriptionView = (TextView) itemView.findViewById(R.id.descriptionView);
            mAddressView = (TextView) itemView.findViewById(R.id.addressView);
        }
    }
}
