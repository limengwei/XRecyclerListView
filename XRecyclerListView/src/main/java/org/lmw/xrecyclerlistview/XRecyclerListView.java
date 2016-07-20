package org.lmw.xrecyclerlistview;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewStub;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import kale.ui.view.rcv.ExRcvAdapterWrapper;
import kale.ui.view.rcv.OnRcvScrollListener;

/**
 * Created by lmw on 2016/6/17.
 */
public class XRecyclerListView extends FrameLayout {
    protected int PAGE_SIZE = 10;

    protected View mRootView;
    protected RecyclerListView mListView;

    protected View mFooterView;

    protected ViewStub mEmptyViewStub;
    protected View mEmptyView;
    TextView tips_msg;
    ImageView tips_img;


    protected SwipeRefreshLayout mSwipeRefreshLayout;
    SwipeRefreshLayout.OnRefreshListener refreshListener;
    LodeMoreListener lodeMoreListener;
    ExRcvAdapterWrapper adapterWrapper;


    int mEmptyViewId, mFooterViewId;

    public XRecyclerListView(Context context) {
        super(context);
        initView();
    }

    public XRecyclerListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(attrs);
        initView();
    }

    public XRecyclerListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initAttrs(attrs);
        initView();
    }

    private void initAttrs(AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.xrecyclerlistview);
        mEmptyViewId = a.getResourceId(R.styleable.xrecyclerlistview_xrl_layout_empty, R.layout.xrecyclerlistview_view_empty);
        mFooterViewId = a.getResourceId(R.styleable.xrecyclerlistview_xrl_layout_progressFooter, R.layout.xrecyclerlistview_view_footer_progress);
    }

    private void initView() {
        if (isInEditMode()) {
            return;
        }
        mRootView = LayoutInflater.from(getContext()).inflate(R.layout.xrecyclerlistview_view_xrecyclerview, this);

        mEmptyViewStub = (ViewStub) mRootView.findViewById(R.id.emptyView);
        mEmptyViewStub.setLayoutResource(mEmptyViewId);
        if (mEmptyViewId != 0) {
            mEmptyView = mEmptyViewStub.inflate();
            mEmptyView.setVisibility(GONE);
            tips_msg = (TextView) mEmptyView.findViewById(R.id.tips_msg);
            tips_img = (ImageView) mEmptyView.findViewById(R.id.tips_img);
        }

        mFooterView = LayoutInflater.from(getContext()).inflate(mFooterViewId, null);

        mSwipeRefreshLayout = (SwipeRefreshLayout) mRootView.findViewById(R.id.swipeRefreshLayout);

        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);

        mSwipeRefreshLayout.setEnabled(false);

        mListView = (RecyclerListView) mRootView.findViewById(R.id.recyclerListView);
    }

    public void setEmptyTips(String msg) {
        setEmptyTips(msg, -1);
    }

    public void setEmptyTips(String msg, int drawableTopResId) {
        if (tips_msg != null)
            tips_msg.setText(msg);

        if (tips_img != null && drawableTopResId != -1)
            tips_img.setImageResource(drawableTopResId);
    }

    public RecyclerListView getListView() {
        return mListView;
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
        mSwipeRefreshLayout.setEnabled(true);
        refreshListener = listener;
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
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
            mSwipeRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    mSwipeRefreshLayout.setRefreshing(refreshing);
                }
            });
        } else {
            mSwipeRefreshLayout.setRefreshing(false);
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
        mSwipeRefreshLayout.setColorSchemeResources(colRes1, colRes2, colRes3, colRes4);
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
        mSwipeRefreshLayout.setRefreshing(false);

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
                        mEmptyView.setVisibility(VISIBLE);
                    } else {
                        if (mEmptyView.getVisibility() == VISIBLE)
                            mEmptyView.setVisibility(GONE);
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


    public LinearLayoutManager getLayoutManager() {
        return (LinearLayoutManager) mListView.getLayoutManager();
    }

    /**
     * 加载更多 接口
     */
    public interface LodeMoreListener {

        void onLodeMore();
    }
}