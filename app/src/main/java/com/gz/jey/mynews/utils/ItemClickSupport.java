package com.gz.jey.mynews.utils;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CompoundButton;

public class ItemClickSupport {
    private final RecyclerView mRecyclerView;
    private OnItemClickListener mOnItemClickListener;
    private CompoundButton.OnCheckedChangeListener mOnCheckedChangeListener;

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        /**
         * @param v View
         * onClick function
          */
        @Override
        public void onClick(View v) {
            if (mOnItemClickListener != null) {
                RecyclerView.ViewHolder holder = mRecyclerView.getChildViewHolder(v);
                mOnItemClickListener.onItemClicked(mRecyclerView, holder.getAdapterPosition(), v);
            }
        }
    };

    private CompoundButton.OnCheckedChangeListener mOnCheckedListener = new CompoundButton.OnCheckedChangeListener() {
        /**
         * @param bv CompoundButton
         * @param isChecked boolean
         * onChecked function
         */
        @Override
        public void onCheckedChanged(CompoundButton bv, boolean isChecked) {
            if (mOnCheckedChangeListener != null) {
                mOnCheckedChangeListener.onCheckedChanged(bv, isChecked);
            }
        }
    };

    /**
     * @param recyclerView RecyclerView
     * @param itemID int
     *               to attach the recycler view's item to listener
     */
    private ItemClickSupport(RecyclerView recyclerView, int itemID) {
        mRecyclerView = recyclerView;
        mRecyclerView.setTag(itemID, this);
        RecyclerView.OnChildAttachStateChangeListener mAttachListener = new RecyclerView.OnChildAttachStateChangeListener() {
            /**
             * @param view View
             */
            @Override
            public void onChildViewAttachedToWindow(View view) {
                if (mOnItemClickListener != null) {
                    view.setOnClickListener(mOnClickListener);
                }

                if (mOnCheckedChangeListener != null) {
                    CompoundButton cb = (CompoundButton) view;
                    cb.setOnCheckedChangeListener(mOnCheckedListener);
                }
            }

            /**
             * @param view View
             */
            @Override
            public void onChildViewDetachedFromWindow(View view) {

            }
        };
        mRecyclerView.addOnChildAttachStateChangeListener(mAttachListener);
    }

    /**
     * @param view RecyclerView
     * @param itemID int
     * @return ItemClickSupport
     */
    public static ItemClickSupport addTo(RecyclerView view, int itemID) {
        ItemClickSupport support = (ItemClickSupport) view.getTag(itemID);
        if (support == null) {
            support = new ItemClickSupport(view, itemID);
        }
        return support;
    }

    /**
     * @param listener OnItemClickListener
     */
    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    /**
     * @param listener OnCheckedChangeListener
     */
    public void setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener listener) {
        mOnCheckedChangeListener = listener;
    }


    public interface OnItemClickListener {
        /**
         * @param recyclerView RecyclerView
         * @param position int
         * @param v View
         */
        void onItemClicked(RecyclerView recyclerView, int position, View v);
    }

}