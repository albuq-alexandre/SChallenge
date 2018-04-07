package br.iesb.a1631088056.schallenge.fragments;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import br.iesb.a1631088056.schallenge.R;
import br.iesb.a1631088056.schallenge.fragments.CellBemFragment.OnListFragmentInteractionListener;
import br.iesb.a1631088056.schallenge.helpers.dummy.DummyContent.DummyItem;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyCellBemRecyclerViewAdapter extends RecyclerView.Adapter<MyCellBemRecyclerViewAdapter.ViewHolder> {

    private final List<DummyItem> mValues;
    private final OnListFragmentInteractionListener mListener;

    public MyCellBemRecyclerViewAdapter(List<DummyItem> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_cellbem, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mCodBemView.setText(mValues.get(position).mCodBem);
        holder.mPBMSView.setText(mValues.get(position).mPBMS);
        holder.mNomeBemView.setText(mValues.get(position).mNomeBem);
        holder.mAcessoryCellBem.setImageResource(android.R.drawable.checkbox_on_background);


        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mCodBemView;
        public final TextView mPBMSView;
        public final TextView mNomeBemView;
        public final ImageView mCategoryBem;
        public final ImageView mAcessoryCellBem;
        public DummyItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mCodBemView = (TextView) view.findViewById(R.id.txtViewCodBem);
            mPBMSView = (TextView) view.findViewById(R.id.txtViewPBMS);
            mNomeBemView = (TextView) view.findViewById(R.id.txtViewNomeBem);
            mCategoryBem = (ImageView) view.findViewById(R.id.imgViewCategoryBem);
            mAcessoryCellBem = (ImageView) view.findViewById(R.id.imgViewAccessoryIcon);


        }

        @Override
        public String toString() {
            return super.toString() + " '" + mNomeBemView.getText() + "'";
        }



    }

    public OnListFragmentInteractionListener getmListener() {
        return mListener;
    }
}
