package org.lmw.xrecyclerlistview;

import android.content.Context;
import android.support.annotation.ColorRes;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import kale.ui.view.rcv.ExRcvAdapterWrapper;
import kale.ui.view.rcv.OnRcvScrollListener;

/**
 * Created by lmw on 2016/6/17.
 */
public class XRecyclerListView extends FrameLayout {
    protected int PAGE_SIZE = 10;

    protected View view;
    protected RecyclerListView mListView;
    protected View mFooterView;
    protected TextView emptyView;

    protected SwipeRefreshLayout swipeRefreshLayout;

    SwipeRefreshLayout.OnRefreshListener refreshListener;
    LodeMoreListener lodeMoreListener;
    ExRcvAdapterWrapper adapterWrapper;

    public XRecyclerListView(Context context) {
        super(context);
        initView();
    }

    public XRecyclerListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public XRecyclerListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    private void initView() {
        if (isInEditMode()) {
            return;
        }

        view = LayoutInflater.from(getContext()).inflate(R.layout.view_xrecyclerview, this);

        mFooterView = LayoutInflater.from(getContext()).inflate(R.layout.view_footer_progress, null);

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);

        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);

        swipeRefreshLayout.setEnabled(false);

        mListView = (RecyclerListView) view.findViewById(R.id.recyclerListView);

        emptyView = (TextView) view.findViewById(R.id.emptyView);


    }


    /**
     * 设置适配器
     */
    public void setAdapter(RecyclerView.Adapter adapter) {
        setAdapterInternal(adapter, false, true);
    }

    /**
     * 设置下拉刷新监听
     */
    public void setRefreshListener(SwipeRefreshLayout.OnRefreshListener listener) {
        swipeRefreshLayout.setEnabled(true);
        refreshListener = listener;
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapterWrapper.removeFooterView();
                refreshListener.onRefresh();
            }
        });
    }

    /**
     * 开关下拉刷新
     */
    public void setRefreshing(final boolean refreshing) {
        if (refreshing) {
            swipeRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    swipeRefreshLayout.setRefreshing(refreshing);
                }
            });
        } else {
            swipeRefreshLayout.setRefreshing(false);
        }
    }


    /**
     * 设置分页加载监听
     */
    public void setLodeMoreListener(LodeMoreListener listener, int pageSize) {
        this.lodeMoreListener = listener;
        this.PAGE_SIZE = pageSize;

        mListView.addOnScrollListener(new OnRcvScrollListener() {
            @Override
            public void onScrollUp() {

            }

            @Override
            public void onScrollDown() {

            }

            @Override
            public void onBottom() {
                lodeMoreListener.onLodeMore();
            }

            @Override
            public void onScrolled(int distanceX, int distanceY) {

            }
        });
    }


    /**
     * 设置下拉刷新控件颜色
     */
    public void setRefreshingColorResources(@ColorRes int colRes1, @ColorRes int colRes2, @ColorRes int colRes3, @ColorRes int colRes4) {
        swipeRefreshLayout.setColorSchemeResources(colRes1, colRes2, colRes3, colRes4);
    }

    /**
     * 适配器封装和添加监听
     *
     * @param adapter
     * @param compatibleWithPrevious
     * @param removeAndRecycleExistingViews
     */
    private void setAdapterInternal(final RecyclerView.Adapter adapter, boolean compatibleWithPrevious, boolean removeAndRecycleExistingViews) {
        adapterWrapper = new ExRcvAdapterWrapper(adapter, mListView.getLayoutManager());

        if (compatibleWithPrevious)
            mListView.swapAdapter(adapterWrapper, removeAndRecycleExistingViews);
        else
            mListView.setAdapter(adapterWrapper);

        mListView.setVisibility(View.VISIBLE);
        swipeRefreshLayout.setRefreshing(false);

        if (null != adapterWrapper)
            adapterWrapper.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {

                @Override
                public void onChanged() {
                    super.onChanged();
                    update();
                }

                private void update() {
                    setRefreshing(false);

                    if (adapter.getItemCount() == 0) {
                        emptyView.setVisibility(VISIBLE);
                    } else {
                        if (emptyView.getVisibility() == VISIBLE)
                            emptyView.setVisibility(GONE);
                    }
                }
            });
    }

    /**
     * 刷新适配器
     *
     * @param pageIndex 页码从1开始
     */
    public void notifyDataSetChanged(int pageIndex) {

        if (adapterWrapper.getFooterView() == null) {
            if (lodeMoreListener != null)
                adapterWrapper.setFooterView(mFooterView);
        }

        int total = mListView.getAdapter().getItemCount() - 1;

        if ((total - (pageIndex - 1) * PAGE_SIZE) < PAGE_SIZE) {
            if (adapterWrapper.getFooterView() != null)
                adapterWrapper.removeFooterView();
        }

        adapterWrapper.notifyDataSetChanged();
    }

    /**
     * 添加滑动监听
     */
    public void addOnScrollListener(OnRcvScrollListener listener) {
        mListView.addOnScrollListener(listener);
    }

    /**
     * 获取当前适配器
     */
    public RecyclerView.Adapter getAdapter() {
        return mListView.getAdapter();
    }


    public void scrollTo(int position) {
        mListView.scrollToPosition(position);
    }

    /**
     * 加载更多 接口
     */
    public interface LodeMoreListener {
        void onLodeMore();
    }
}