package com.example.josh.pointsofinterest;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PlacesRecyclerViewAdapter extends RecyclerView.Adapter<PlacesRecyclerViewAdapter.ViewHolder> {

    private final List<PlaceModel> mPlaceModels;
    private final Context mContext;
    private final OnPlaceSelectedListener mOnPlaceSelectedListener;

    public PlacesRecyclerViewAdapter(List<PlaceModel> placeModels, OnPlaceSelectedListener onPlaceSelectedListener, Context context) {
        mPlaceModels = placeModels;
        mContext = context;
        mOnPlaceSelectedListener = onPlaceSelectedListener;
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

        viewHolder.mPlacesItemContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnPlaceSelectedListener != null) {
                    mOnPlaceSelectedListener.onPlaceSelected(placeModel);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mPlaceModels.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.placesItemContainer) View mPlacesItemContainer;
        @BindView(R.id.placeNameView) TextView mPlaceNameView;
        @BindView(R.id.ratingValueView) TextView mRatingValueView;
        @BindView(R.id.ratingBarView) RatingBar mRatingBarView;
        @BindView(R.id.descriptionView) TextView mDescriptionView;
        @BindView(R.id.addressView) TextView mAddressView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
