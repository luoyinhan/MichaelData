package com.xiaoluo.michaelutil.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xiaoluo.michaelutil.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 瀑布流适配器
 * Created by Monkey on 2015/6/29.
 */
public class MyStaggeredViewAdapter extends RecyclerView.Adapter<MyRecyclerViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }

    public OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }


    public Context mContext;
    public List<String> mDatas;
    public List<Integer> mHeights;
    public LayoutInflater mLayoutInflater;

    public MyStaggeredViewAdapter(Context mContext) {
        this.mContext = mContext;
        mLayoutInflater = LayoutInflater.from(mContext);
        mDatas = new ArrayList<>();
        mHeights = new ArrayList<>();//随机瀑布流 item高度
        for (int i = 'A'; i <= 'z'; i++) {
            mDatas.add((char) i + "");
        }
        for (int i = 0; i < mDatas.size(); i++) {
            mHeights.add((int) (Math.random() * 300) + 200);
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

        ViewGroup.LayoutParams mLayoutParams = holder.mTextView.getLayoutParams();
        mLayoutParams.height = mHeights.get(position);
        holder.mTextView.setLayoutParams(mLayoutParams);
        holder.mTextView.setText(mDatas.get(position));
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }
}
