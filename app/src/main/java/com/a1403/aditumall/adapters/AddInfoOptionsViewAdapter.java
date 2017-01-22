package com.a1403.aditumall.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.a1403.aditumall.R;

/**
 * Created by dakfu on 1/21/2017.
 */

public class AddInfoOptionsViewAdapter extends RecyclerView.Adapter<AddInfoOptionsViewAdapter.ViewHolder> {

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_venue_detail_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 7;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView accessIcon;
        public final TextView accessName;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            accessIcon = (ImageView) view.findViewById(R.id.accessIcon);
            accessName = (TextView) view.findViewById(R.id.accessName);
        }

        @Override
        public String toString() {
            return super.toString();
        }
    }
}
