package com.example.josh.pointsofinterest;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PlacesRecyclerViewAdapter extends RecyclerView.Adapter<PlacesRecyclerViewAdapter.ViewHolder> {

    private final List<PlaceModel> mPlaceModels;
    private final Context mContext;
    private final OnPlaceSelectedListener mOnPlaceSelectedListener;
    private int mSelectedIndex = -1;

    public PlacesRecyclerViewAdapter(List<PlaceModel> placeModels, OnPlaceSelectedListener onPlaceSelectedListener, Context context) {
        mPlaceModels = new ArrayList<>(placeModels);
        mContext = context;
        mOnPlaceSelectedListener = onPlaceSelectedListener;

        Collections.sort(mPlaceModels, new Comparator<PlaceModel>() {
            @Override
            public int compare(PlaceModel placeModel, PlaceModel otherPlaceModel) {
                return new Float(placeModel.getDistance()).compareTo(new Float(otherPlaceModel.getDistance()));
            }
        });
    }

    /**
     * Returns the index of the given PlaceModel in this adapter. The sort order of this adapter's models
     * may be different than when the adapter was constructed.
     * @param placeModel - The model to match against.
     * @return - the index of the model, or -1 if no matching model is found.
     */
    public int getIndex(PlaceModel placeModel) {
        for (int i = 0 ; i < mPlaceModels.size(); i++) {
            if (mPlaceModels.get(i).equals(placeModel)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public PlacesRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View placeView = inflater.inflate(R.layout.places_list_item, parent, false);
        return new ViewHolder(placeView);
    }

    @Override
    public void onBindViewHolder(PlacesRecyclerViewAdapter.ViewHolder viewHolder, int position) {
        final PlaceModel placeModel = mPlaceModels.get(position);
        viewHolder.mAddressView.setText(placeModel.getAddress());
        viewHolder.mDescriptionView.setText(placeModel.getDescription());
        viewHolder.mPlaceNameView.setText(placeModel.getName());
        viewHolder.mRatingBarView.setRating(placeModel.getRating());
        viewHolder.mRatingValueView.setText(placeModel.getRating() + "");
        viewHolder.mRatingValueView.setVisibility(placeModel.getRating() >= 0 ? View.VISIBLE : View.INVISIBLE);

        String distanceTextTemplate = mContext.getString(R.string.distance_miles_template);
        // Display very short distances as "< 0.1 mi" and round all other distances to the nearest tenth of a mile.
        viewHolder.mDistanceView.setText(
            placeModel.getDistance() < 0.1 ?
            distanceTextTemplate.replace("{%}", "< 0.1") :
            distanceTextTemplate.replace("{%}", String.format("%.1f", placeModel.getDistance()))
        );
        viewHolder.mDistanceView.setVisibility(placeModel.getDistance() > 0 ? View.VISIBLE : View.INVISIBLE);

        viewHolder.mPlacesItemContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnPlaceSelectedListener != null) {
                    mOnPlaceSelectedListener.onPlaceItemSelected(placeModel);
                }
            }
        });

        viewHolder.mPlacesItemContainer.setCardBackgroundColor(
                mSelectedIndex == position ?
                ContextCompat.getColor(mContext, R.color.card_selection_background) :
                ContextCompat.getColor(mContext, R.color.card_default_background)
        );
    }

    @Override
    public int getItemCount() {
        return mPlaceModels.size();
    }

    public void setSelectedPlace(PlaceModel placeModel) {
        int currentSelectedIndex = mSelectedIndex;
        mSelectedIndex = getIndex(placeModel);
        notifyItemChanged(currentSelectedIndex);
        if (mSelectedIndex != -1) {
            notifyItemChanged(mSelectedIndex);
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.placesItemContainer) CardView mPlacesItemContainer;
        @BindView(R.id.placeNameView) TextView mPlaceNameView;
        @BindView(R.id.ratingValueView) TextView mRatingValueView;
        @BindView(R.id.ratingBarView) RatingBar mRatingBarView;
        @BindView(R.id.descriptionView) TextView mDescriptionView;
        @BindView(R.id.addressView) TextView mAddressView;
        @BindView(R.id.distanceView) TextView mDistanceView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
