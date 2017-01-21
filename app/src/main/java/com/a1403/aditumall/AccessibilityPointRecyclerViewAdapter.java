package com.a1403.aditumall;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.a1403.aditumall.VenueDetailListFragment.OnListFragmentInteractionListener;
import com.a1403.aditumall.model.AccessibilityPoint;

import java.util.List;

public class AccessibilityPointRecyclerViewAdapter extends RecyclerView.Adapter<AccessibilityPointRecyclerViewAdapter.ViewHolder> {

    private final List<AccessibilityPoint> aps;
    private final OnListFragmentInteractionListener mListener;

    public AccessibilityPointRecyclerViewAdapter(List<AccessibilityPoint> items, OnListFragmentInteractionListener listener) {
        aps = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.apNameView.setText(aps.get(position).getName());
        holder.cntView.setText(Integer.toString(aps.get(position).getUpvotes() - aps.get(position).getDownvotes()));

        holder.upvoteView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (aps.get(position).setVoteStatus(AccessibilityPoint.VoteStatus.UPVOTED))
                {
                    holder.upvoteView.setImageResource(R.drawable.arrow_green);
                    holder.downvoteView.setImageResource(R.drawable.arrow_red_inactive);
                }
                else
                    holder.upvoteView.setImageResource(R.drawable.arrow_green_inactive);
                holder.cntView.setText(Integer.toString(aps.get(position).getScore()));

//                if (null != mListener) {
//                    // Notify the active callbacks interface (the activity, if the
//                    // fragment is attached to one) that an item has been selected.
//                    //mListener.onListFragmentInteraction(holder.mItem);
//
//                }
            }
        });
        holder.downvoteView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (aps.get(position).setVoteStatus(AccessibilityPoint.VoteStatus.DOWNVOTED))
                {
                    holder.downvoteView.setImageResource(R.drawable.arrow_red);
                    holder.upvoteView.setImageResource(R.drawable.arrow_green_inactive);
                }
                else
                    holder.downvoteView.setImageResource(R.drawable.arrow_red_inactive);
                holder.cntView.setText(Integer.toString(aps.get(position).getScore()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return aps.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView mapPinView;
        public final TextView apNameView;
        public final TextView cntView;
        public final ImageView upvoteView;
        public final ImageView downvoteView;
        //public DummyItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mapPinView = (ImageView) view.findViewById(R.id.mapPin);
            apNameView = (TextView) view.findViewById(R.id.apName);
            cntView = (TextView) view.findViewById(R.id.count);
            upvoteView = (ImageView) view.findViewById(R.id.upvote);
            downvoteView = (ImageView) view.findViewById(R.id.downvote);
        }

        @Override
        public String toString() {
            return super.toString();
        }
    }
}
