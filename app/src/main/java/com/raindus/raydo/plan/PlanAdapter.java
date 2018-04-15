package com.raindus.raydo.plan;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.raindus.raydo.R;
import com.raindus.raydo.RaydoApplication;
import com.raindus.raydo.common.DateUtils;
import com.raindus.raydo.dao.ObjectBox;
import com.raindus.raydo.dialog.PlanContentDialog;
import com.raindus.raydo.dialog.PlanDetailDialog;
import com.raindus.raydo.dialog.PlanPriorityDialog;
import com.raindus.raydo.dialog.PlanStatusDialog;
import com.raindus.raydo.dialog.PlanTagDialog;
import com.raindus.raydo.dialog.PlanTimeDialog;
import com.raindus.raydo.plan.entity.PlanEntity;
import com.raindus.raydo.plan.entity.PlanPriority;
import com.raindus.raydo.plan.entity.PlanStatus;
import com.raindus.raydo.plan.entity.PlanTag;
import com.raindus.raydo.plan.entity.PlanTime;

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
                    ((PlanEntity) mData.get(position)).deleteRepeatPlanEntity();
                    ObjectBox.PlanEntityBox.delete((PlanEntity) mData.get(position));
                    if (mPlanAdapterListener != null)
                        mPlanAdapterListener.onPlanDeleted();
                }
            });
            dialog.show();
        }

        @Override
        public void onPlanItemLongClick(View view, int position) {
            mLongClickPosition = position;
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

            itemView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
                @Override
                public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                    ((Activity) mContext).getMenuInflater().inflate(R.menu.menu_plan_item, menu);
                    menu.setHeaderTitle("更新");
                    menu.getItem(0).setOnMenuItemClickListener(mOnMenuItemClickListener);
                    menu.getItem(1).setOnMenuItemClickListener(mOnMenuItemClickListener);
                    menu.getItem(2).setOnMenuItemClickListener(mOnMenuItemClickListener);
                    menu.getItem(3).setOnMenuItemClickListener(mOnMenuItemClickListener);
                    menu.getItem(4).setOnMenuItemClickListener(mOnMenuItemClickListener);
                }
            });
        }
    }

    private int mLongClickPosition = -1;
    private MenuItem.OnMenuItemClickListener mOnMenuItemClickListener = new MenuItem.OnMenuItemClickListener() {

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if (DateUtils.isHappen(((PlanEntity) mData.get(mLongClickPosition)).getTime().getStartTime())) {
                if (item.getItemId() != R.id.menu_plan_status) {
                    toast("该计划已不可编辑");
                    return false;
                }
            }

            switch (item.getItemId()) {
                case R.id.menu_plan_time:
                    updatePlanTime();
                    break;
                case R.id.menu_plan_status:
                    updatePlanStatus();
                    break;
                case R.id.menu_plan_priority:
                    updatePlanPriority();
                    break;
                case R.id.menu_plan_tag:
                    updatePlanTag();
                    break;
                case R.id.menu_plan_detail:
                    updatePlanDetail();
                    break;
            }
            return false;
        }
    };

    private void updatePlan(boolean remind) {
        if (remind)
            ObjectBox.PlanEntityBox.putAndRemind((PlanEntity) mData.get(mLongClickPosition));
        else
            ObjectBox.PlanEntityBox.put((PlanEntity) mData.get(mLongClickPosition));
        notifyItemChanged(mLongClickPosition);
        toast("计划已更新");
    }

    private void updatePlanTime() {
        PlanTimeDialog timeDialog = new PlanTimeDialog(mContext, ((PlanEntity) mData.get(mLongClickPosition)).getTime().clone());
        timeDialog.setOnPlanTimeCallback(new PlanTimeDialog.OnPlanTimeCallback() {
            @Override
            public void onPlanTime(PlanTime planTime) {
                ((PlanEntity) mData.get(mLongClickPosition)).setTime(planTime);
                ((PlanEntity) mData.get(mLongClickPosition)).updateRepeatPlanEntity(0);
                updatePlan(true);
            }
        });
        timeDialog.show();
    }

    private void updatePlanStatus() {
        PlanStatusDialog statusDialog = new PlanStatusDialog(mContext, ((PlanEntity) mData.get(mLongClickPosition)).getStatus());
        statusDialog.setOnStatusCallback(new PlanStatusDialog.OnStatusCallback() {
            @Override
            public void onCallback(PlanStatus status) {
                ((PlanEntity) mData.get(mLongClickPosition)).setStatus(status);
                updatePlan(false);
            }
        });
        statusDialog.show();
    }

    private void updatePlanPriority() {
        PlanPriorityDialog priorityDialog = new PlanPriorityDialog(mContext, ((PlanEntity) mData.get(mLongClickPosition)).getPriority());
        priorityDialog.setOnPriorityCallback(new PlanPriorityDialog.OnPriorityCallback() {
            @Override
            public void onCallback(PlanPriority priority) {
                ((PlanEntity) mData.get(mLongClickPosition)).setPriority(priority);
                ((PlanEntity) mData.get(mLongClickPosition)).updateRepeatPlanEntity(1);
                updatePlan(false);
            }
        });
        priorityDialog.show();
    }

    private void updatePlanTag() {
        PlanTagDialog tagDialog = new PlanTagDialog(mContext, ((PlanEntity) mData.get(mLongClickPosition)).getTag());
        tagDialog.setOnTagCallback(new PlanTagDialog.OnTagCallback() {
            @Override
            public void onCallback(PlanTag tag) {
                ((PlanEntity) mData.get(mLongClickPosition)).setTag(tag);
                ((PlanEntity) mData.get(mLongClickPosition)).updateRepeatPlanEntity(2);
                updatePlan(false);
            }
        });
        tagDialog.show();
    }

    private void updatePlanDetail() {
        PlanContentDialog contentDialog = new PlanContentDialog(mContext, ((PlanEntity) mData.get(mLongClickPosition)).detail);
        contentDialog.setOnContentCallback(new PlanContentDialog.OnContentCallback() {
            @Override
            public void onCallback(String content) {
                ((PlanEntity) mData.get(mLongClickPosition)).detail = content;
                ((PlanEntity) mData.get(mLongClickPosition)).updateRepeatPlanEntity(3);
                updatePlan(false);
            }
        });
        contentDialog.show();
    }

    private void toast(String s) {
        Toast.makeText(mContext, s, Toast.LENGTH_SHORT).show();
    }
}
