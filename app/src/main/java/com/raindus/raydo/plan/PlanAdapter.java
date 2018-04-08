package com.raindus.raydo.plan;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.raindus.raydo.R;
import com.raindus.raydo.RaydoApplication;
import com.raindus.raydo.common.DateUtils;
import com.raindus.raydo.dao.ObjectBox;
import com.raindus.raydo.dialog.PlanDetailDialog;
import com.raindus.raydo.plan.entity.PlanEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Raindus on 2018/3/26.
 */

public class PlanAdapter extends RecyclerView.Adapter {

    public static final int VIEW_TYPE_TITLE_BAR = 1;
    public static final int VIEW_TYPE_PLAN = 2;

    private Context mContext;
    private List<Object> mData;
    private PlanAdapterListener mPlanAdapterListener;
    private OnPlanItemListener mOnPlanItemListener;

    public PlanAdapter(Context context) {
        mContext = context;
        mData = new ArrayList<>();
        mOnPlanItemListener = mDefaultOnPlanItemListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_TITLE_BAR)
            return new TitleViewHolder(LayoutInflater.from(mContext)
                    .inflate(R.layout.item_plan_title_bar, parent, false));
        else
            return new PlanViewHolder(LayoutInflater.from(mContext)
                    .inflate(R.layout.item_plan, parent, false));
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof TitleViewHolder) {
            ((TitleViewHolder) holder).mTvTitleBar.setText((String) mData.get(position));
            return;
        }
        PlanEntity plan = (PlanEntity) mData.get(position);
        final PlanViewHolder planViewHolder = (PlanViewHolder) holder;

        planViewHolder.mIvStatus.setImageResource(plan.getStatus().getIcon());
        planViewHolder.mIvStatus.setColorFilter(mContext.getResources().getColor(plan.getPriority().getColor()));
        planViewHolder.mTvTitle.setText(plan.title);
        planViewHolder.mTvDetail.setText(plan.detail);
        planViewHolder.mIvTag.setImageResource(plan.getTag().getIcon());
        planViewHolder.mTvTime.setText(DateUtils.formatTime(plan.getTime().getStartTime()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnPlanItemListener != null)
                    mOnPlanItemListener.onPlanItemClick(holder.itemView, position);
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mOnPlanItemListener != null)
                    mOnPlanItemListener.onPlanItemLongClick(holder.itemView, position);
                return false;
            }
        });

    }

    public void setPlanData(List<Object> data) {
        if (data == null)
            mData = new ArrayList<>();
        else
            mData = data;

        if (mPlanAdapterListener != null)
            mPlanAdapterListener.onDataChanged(mData.size());

        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (mData.get(position) instanceof String)
            return VIEW_TYPE_TITLE_BAR;
        else
            return VIEW_TYPE_PLAN;
    }

    public void setPlanAdapterListener(PlanAdapterListener listener) {
        mPlanAdapterListener = listener;
    }

    public interface PlanAdapterListener {
        void onDataChanged(int itemCount);

        void onPlanDeleted();
    }

    public void setOnPlanItemListener(OnPlanItemListener listener) {
        mOnPlanItemListener = listener;
    }

    public interface OnPlanItemListener {
        void onPlanItemClick(View view, int position);

        void onPlanItemLongClick(View view, int position);
    }

    // 默认实现
    private OnPlanItemListener mDefaultOnPlanItemListener = new OnPlanItemListener() {
        @Override
        public void onPlanItemClick(View view, final int position) {
            PlanDetailDialog dialog = new PlanDetailDialog(mContext, (PlanEntity) mData.get(position));
            dialog.setOnPlanDeleteCallback(new PlanDetailDialog.OnPlanDeleteCallback() {
                @Override
                public void onDelete() {
                    ObjectBox.PlanEntityBox.delete(RaydoApplication.get(), (PlanEntity) mData.get(position));
                    if (mPlanAdapterListener != null)
                        mPlanAdapterListener.onPlanDeleted();
                }
            });
            dialog.show();
        }

        @Override
        public void onPlanItemLongClick(View view, int position) {
            //TODO
        }
    };

    class TitleViewHolder extends RecyclerView.ViewHolder {

        TextView mTvTitleBar;

        public TitleViewHolder(View itemView) {
            super(itemView);
            mTvTitleBar = itemView.findViewById(R.id.item_plan_title_bar);
        }
    }

    class PlanViewHolder extends RecyclerView.ViewHolder {

        ImageView mIvStatus;
        TextView mTvTitle;
        ImageView mIvTag;
        TextView mTvDetail;
        TextView mTvTime;

        public PlanViewHolder(View itemView) {
            super(itemView);
            mIvStatus = itemView.findViewById(R.id.item_plan_status);
            mTvTitle = itemView.findViewById(R.id.item_plan_title);
            mIvTag = itemView.findViewById(R.id.item_plan_tag);
            mTvDetail = itemView.findViewById(R.id.item_plan_detail);
            mTvTime = itemView.findViewById(R.id.item_plan_time);
        }
    }
}
