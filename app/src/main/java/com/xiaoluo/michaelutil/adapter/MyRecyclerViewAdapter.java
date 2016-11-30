package com.xiaoluo.michaelutil.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xiaoluo.michaelutil.R;

import java.util.ArrayList;
import java.util.List;


public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewHolder> {
    private int mType = 0;


    //dta

    public interface OnItemClickListener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }

    public OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public void setmType(int mType) {
        this.mType = mType;
    }

    public Context mContext;
    public List<String> mDatas;
    public LayoutInflater mLayoutInflater;


    public MyRecyclerViewAdapter(Context mContext) {
        this.mContext = mContext;
        mLayoutInflater = LayoutInflater.from(mContext);
        mDatas = new ArrayList<>();

        String[] contents = mContext.getResources().getStringArray(R.array.content_titles);
//        for (int i = 'A'; i <= 'z'; i++) {
//            mDatas.add((char) i + "");
//        }
        for (int i = 0; i < contents.length; i++) {
            mDatas.add(contents[i]);
        }
    }

    /**
     * 创建ViewHolder
     */
    @Override
    public MyRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mView = mLayoutInflater.inflate(R.layout.item_main, parent, false);
        MyRecyclerViewHolder mViewHolder = new MyRecyclerViewHolder(mView);
        return mViewHolder;
    }

    /**
     * 绑定ViewHoler，给item中的控件设置数据
     */
    @Override
    public void onBindViewHolder(final MyRecyclerViewHolder holder, final int position) {
        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(holder.itemView, position);
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mOnItemClickListener.onItemLongClick(holder.itemView, position);
                    return true;
                }
            });
        }
        ViewGroup.LayoutParams mLayoutParams = holder.mCardView.getLayoutParams();
        if (mType == 0) {
            mLayoutParams.width = mLayoutParams.MATCH_PARENT;
            holder.mCardView.setLayoutParams(mLayoutParams);
        } else {
            mLayoutParams.width = 400;
            holder.mCardView.setLayoutParams(mLayoutParams);
        }
        holder.mTextView.setText(mDatas.get(position));
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public void add() {
        this.notifyItemChanged(0);//局部更新
        this.notifyDataSetChanged();//全部更新
    }
}
