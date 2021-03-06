package com.example.administrator.mytestdemo.widge.recycler;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import com.example.administrator.mytestdemo.R;
import com.example.administrator.mytestdemo.util.KLog;

import java.lang.ref.WeakReference;

import static android.support.v7.widget.RecyclerView.LayoutManager;


/**
 * <p/>
 * 可刷新的RecycleView, 也可分页，使用时会填充整个父布局
 */
public class CustomRefreshRecycleView extends SwipeRefreshLayout {

    private CustomRefreshRecycleView refreshView;
    private LoadMoreRecyclerView mRecycleView;
    private OnLoadMoreListener mOnLoadMoreListener;
    private OnFooterClickListener mOnFooterClickListener;
    private boolean loadMoreEnable = true;  //是否能加载更多
    private boolean mShowFooter = true;      //是否显示Footer
    private View header, footer;             // 头部和脚部
    private FooterStatusHandle mFooterStatus;  //Footer状态

    public CustomRefreshRecycleView(Context context) {
        this(context, null);
    }

    public CustomRefreshRecycleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        refreshView = this;
        mRecycleView = new LoadMoreRecyclerView(context, this);
        addView(mRecycleView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        setColorSchemeColors(
                getResources().getColor(android.R.color.holo_blue_dark),
                getResources().getColor(android.R.color.holo_blue_dark),
                getResources().getColor(android.R.color.holo_blue_dark));
    }

    /**
     * 设置适配器
     */
    public void setAdapter(CustomRefreshAdapter adapter) {
        mRecycleView.setAdapter(adapter);
    }


    public LoadMoreRecyclerView getRecycleView() {
        return mRecycleView;
    }

    /**
     * @return 当前正在使用的适配器
     */
    public CustomRefreshAdapter getAdapter() {
        return (CustomRefreshAdapter) mRecycleView.getAdapter();
    }

    /**
     * 设置布局管理器
     */
    public void setLayoutManager(final LayoutManager manager) {
        if (manager instanceof GridLayoutManager) {
            ((GridLayoutManager) manager).setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return getAdapter().isFooter(position) || getAdapter().isHeader(position) ?
                            ((GridLayoutManager) manager).getSpanCount() : 1;
                }
            });
        }

        mRecycleView.setLayoutManager(manager);
        initDefaultFooter();
    }

    /**
     * @return 布局管理器
     */
    public LayoutManager getLayoutManager() {
        return mRecycleView.getLayoutManager();
    }

    /**
     * 添加分割线
     */
    public void addItemDecoration(RecyclerView.ItemDecoration decor) {
        mRecycleView.addItemDecoration(decor);
    }


    public void setOnFooterClickListener(OnFooterClickListener listener) {
        mOnFooterClickListener = listener;
    }

    @Override
    public void setOnRefreshListener(OnRefreshListener listener) {
        setFooterStatus(FooterStatusHandle.TYPE_PULL_LOAD_MORE);
        super.setOnRefreshListener(listener);
    }

    public void onRefreshAndLoadComplete(boolean hasMore) {
        setRefreshing(false);
        if (hasMore) {
            setFooterStatus(FooterStatusHandle.TYPE_PULL_LOAD_MORE);
        } else {
            setFooterStatus(FooterStatusHandle.TYPE_NO_MORE);
        }
    }


    /**
     * 设置Footer显示状态
     */
    public void setFooterStatus(FooterStatusHandle type) {
        if (mRecycleView == null) {
            throw new NullPointerException("-----mRecycleView is null!!!");
        }
        mFooterStatus = type;
        switch (type) {
            case TYPE_PULL_LOAD_MORE:
                mRecycleView.mIsLoadMore = false;
                loadMoreEnable = true;
                break;
            case TYPE_LOADING_MORE:
                mRecycleView.mIsLoadMore = true;
                loadMoreEnable = true;
                break;
            case TYPE_ERROR:
                mRecycleView.mIsLoadMore = false;
                loadMoreEnable = true;
                break;
            case TYPE_NO_MORE:
                mRecycleView.mIsLoadMore = false;
                loadMoreEnable = false;
                break;
        }
        refreshFooter(type);
    }

    private void refreshFooter(FooterStatusHandle type) {
        if (loadMoreEnable) {
            if (getFooter() != null) {
                if (type == FooterStatusHandle.TYPE_PULL_LOAD_MORE) {
                    getFooter().findViewById(R.id.ll_more).setVisibility(GONE);
                    getFooter().findViewById(R.id.tv_no_more).setVisibility(GONE);
                    getFooter().findViewById(R.id.tv_pull_load_more).setVisibility(VISIBLE);
                    getFooter().findViewById(R.id.tv_error).setVisibility(GONE);
                    return;
                }

                if (type == FooterStatusHandle.TYPE_LOADING_MORE) {
                    getFooter().findViewById(R.id.ll_more).setVisibility(VISIBLE);
                    getFooter().findViewById(R.id.tv_no_more).setVisibility(GONE);
                    getFooter().findViewById(R.id.tv_pull_load_more).setVisibility(GONE);
                    getFooter().findViewById(R.id.tv_error).setVisibility(GONE);
                    return;
                }

                if (type == FooterStatusHandle.TYPE_ERROR) {
                    getFooter().findViewById(R.id.ll_more).setVisibility(GONE);
                    getFooter().findViewById(R.id.tv_no_more).setVisibility(GONE);
                    getFooter().findViewById(R.id.tv_pull_load_more).setVisibility(GONE);
                    getFooter().findViewById(R.id.tv_error).setVisibility(VISIBLE);
                }
            }
        } else {
            if (getFooter() != null) {
                getFooter().findViewById(R.id.ll_more).setVisibility(GONE);
                getFooter().findViewById(R.id.tv_pull_load_more).setVisibility(GONE);
                getFooter().findViewById(R.id.tv_error).setVisibility(GONE);
                if (getShowFooterWithNoMore()) {
                    getFooter().findViewById(R.id.tv_no_more).setVisibility(VISIBLE);
                } else {
                    getFooter().findViewById(R.id.tv_no_more).setVisibility(GONE);
                }
            }
        }
    }


    /**
     * @return 是否可以加载更多
     */
    public boolean getLoadMoreEnable() {
        return loadMoreEnable;
    }


    /**
     * 设置加载更多监听
     */
    public void setOnLoadMoreListener(OnLoadMoreListener listener) {
        setFooterStatus(FooterStatusHandle.TYPE_PULL_LOAD_MORE);
        mOnLoadMoreListener = listener;
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    /**
     * 添加头部
     *
     * @param header 作为头部的布局
     */
    public void setHeader(View header) {
        this.header = header;
    }

    /**
     * 删除头部
     */
    public void removeHeader() {
        this.header = null;
    }

    /**
     * 添加头部
     */
    public void setHeader(int resId) {
        this.header = LayoutInflater.from(getContext()).inflate(resId, mRecycleView, false);
    }

    public View getHeader() {
        return header;
    }

    /**
     * 添加脚部
     */
    public void setFooter(View footer) {
        this.footer = footer;
    }

    /**
     * 添加脚部
     */
    public void setFooter(int resId) {
        this.footer = LayoutInflater.from(getContext()).inflate(resId, mRecycleView, false);

        footer.findViewById(R.id.tv_error).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnFooterClickListener != null) {
                    setFooterStatus(FooterStatusHandle.TYPE_LOADING_MORE);
                    mOnFooterClickListener.onErrorClick();
                }
            }
        });

        footer.findViewById(R.id.tv_pull_load_more).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnFooterClickListener != null) {
                    setFooterStatus(FooterStatusHandle.TYPE_LOADING_MORE);
                    mOnFooterClickListener.onLoadMoreClick();
                }
            }
        });
    }

    /**
     * 初始化默认的footer
     */
    private void initDefaultFooter() {
        setFooter(R.layout.custom_refresh_recyclerview_load_more);
    }

    /**
     * @return 脚部视图
     */
    public View getFooter() {
        return footer;
    }


    //没有更多时是否显示Footer
    public void setShowFooterWithNoMore(boolean show) {
        mShowFooter = show;
    }

    public boolean getShowFooterWithNoMore() {
        return mShowFooter;
    }

    /**
     * @return 获取最后一个可见视图的位置
     */
    public int findLastVisibleItemPosition() {
        LayoutManager manager = mRecycleView.getLayoutManager();
        // 获取最后一个正在显示的View
        if (manager instanceof GridLayoutManager) {
            return ((GridLayoutManager) manager).findLastVisibleItemPosition();
        } else if (manager instanceof LinearLayoutManager) {
            return ((LinearLayoutManager) manager).findLastVisibleItemPosition();
        } else if (manager instanceof StaggeredGridLayoutManager) {
            int[] into = new int[((StaggeredGridLayoutManager) manager).getSpanCount()];
            ((StaggeredGridLayoutManager) manager).findLastVisibleItemPositions(into);
            return findMax(into);
        }
        return -1;
    }

    /**
     * 设置子视图充满一行
     *
     * @param view 子视图
     */
    public void setFullSpan(View view) {
        LayoutManager manager = getLayoutManager();
        // 根据布局设置参数, 使"加载更多"的布局充满一行
        if (manager instanceof StaggeredGridLayoutManager) {
            StaggeredGridLayoutManager.LayoutParams params = new StaggeredGridLayoutManager.LayoutParams(
                    StaggeredGridLayoutManager.LayoutParams.MATCH_PARENT, StaggeredGridLayoutManager.LayoutParams.WRAP_CONTENT);
            params.setFullSpan(true);
            view.setLayoutParams(params);
        }
    }


    /**
     * 可分页加载更多的RecyclerView
     */
    public class LoadMoreRecyclerView extends RecyclerView {
        private WeakReference<CustomRefreshRecycleView> customRefreshRecycleView;

        public LoadMoreRecyclerView(Context context, CustomRefreshRecycleView view) {
            super(context);
            customRefreshRecycleView = new WeakReference<CustomRefreshRecycleView>(view);
            post(new Runnable() {
                @Override
                public void run() {
                    if (getBottom() != 0 && getChildAt(findLastVisibleItemPosition()) != null && getBottom() >= getChildAt(findLastVisibleItemPosition()).getBottom()) {
                        // 最后一条正在显示的子视图在RecyclerView的上面, 说明子视图未充满RecyclerView
                        setFooterStatus(FooterStatusHandle.TYPE_LOADING_MORE); // 未充满则不能加载更多
                        getAdapter().notifyDataSetChanged();
                    }
                }
            });
        }

        public boolean mIsLoadMore = false;

        @Override
        public void onScrollStateChanged(int state) {
            super.onScrollStateChanged(state);
            CustomRefreshRecycleView view = customRefreshRecycleView.get();
            if (view != null) {
                if (state == SCROLL_STATE_IDLE && // 停止滚动
                        loadMoreEnable
                        && view.mOnLoadMoreListener != null  // 可以加载更多, 且有加载监听
                        && mIsUp
                        && !mIsLoadMore
                        && findLastVisibleItemPosition() == getLayoutManager().getItemCount() - 1) { // 滚动到了最后一个子视图
                    KLog.i("-------loadmore");
                    if (mFooterStatus != FooterStatusHandle.TYPE_ERROR) {
                        setFooterStatus(FooterStatusHandle.TYPE_LOADING_MORE);
                        view.mOnLoadMoreListener.onLoadMore(); // 执行加载更多
                    }
                }
            }
        }


        private float mOldY = 0.0f;
        private float mNewY = 0.0f;
        private boolean mIsUp = false;

        @Override
        public boolean onTouchEvent(MotionEvent e) {
            switch (e.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mOldY = e.getY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    mNewY = e.getY();
                    //下拉
                    if (mNewY - mOldY >= ViewConfiguration.get(getContext()).getScaledTouchSlop() * 1.0f) {
                        mIsUp = false;
                    }
                    //上拉
                    else if (mOldY - mNewY >= ViewConfiguration.get(getContext()).getScaledTouchSlop() * 1.0f) {
                        mIsUp = true;
                    }
                    mOldY = mNewY;
                    break;

                case MotionEvent.ACTION_UP:
                    mOldY = 0.0f;
                    mNewY = 0.0f;
                    break;
            }
            return super.onTouchEvent(e);
        }
    }


    private int findMax(int[] lastPositions) {
        int max = lastPositions[0];
        for (int value : lastPositions) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }
}
