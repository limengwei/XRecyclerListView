package org.lmw.xrecyclerlistview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.annotation.Size;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;


/**
 * Created by lmw on 2016/3/4.
 * RecyclerView版ListView
 */
public class RecyclerListView extends RecyclerView {

    Context context;
    HorizontalDividerItemDecoration divider;

    public RecyclerListView(Context context) {
        super(context);
        init(context);
    }

    public RecyclerListView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public RecyclerListView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }


    void init(Context context) {
        this.context = context;
        //关闭顶部底部阴影
        setOverScrollMode(OVER_SCROLL_NEVER);

        //设置布局管理器
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayout.VERTICAL);
        setLayoutManager(layoutManager);
        setHasFixedSize(true);

        addItemDecoration(R.color.divider_gray, 2);
    }

    public void addItemDecoration(int colorResId, int size) {
        //设置Item分割线
        HorizontalDividerItemDecoration divider = new HorizontalDividerItemDecoration.Builder(context)
                .colorResId(colorResId)
                .showLastDivider()
                .size(size)
                .build();

        addItemDecoration(divider);
    }

    public void removeItemDecoration() {
        if (divider != null) {
            removeItemDecoration(divider);
            divider = null;
        }
    }
}