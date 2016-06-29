## Step 1. 设置源

```
allprojects {
	repositories {
		...
		maven { url "https://jitpack.io" }
	}
}
```
	
## Step 2. 添加引用

```
dependencies {
	compile 'com.github.limengwei:XRecyclerListView:1.0.5'
}
```
---
## Usage

布局文件
```xml
<org.lmw.xrecyclerlistview.XRecyclerListView
	android:id="@+id/listView"
	android:layout_width="match_parent"
	android:layout_height="match_parent"
	app:xrl_layout_empty="@layout/customer_empty"
    app:xrl_layout_progressFooter="@layout/customer_footer"
	/>
```
自定义属性
- app:xrl_layout_empty ：设置空页面
- app:xrl_layout_progressFooter ：设置底部进加载进度条

---

在Activity和Fragment中使用

- 设置下拉刷新监听器
```
xListView.setRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
    @Override
    public void onRefresh() {
        //do samething
    }
});
```
- 设置分页监听器
```
xListView.setLodeMoreListener(new XRecyclerListView.LodeMoreListener() {
    @Override
    public void onLodeMore() {
       //do samething
    }
}, PAGE_SIZE);
```

- 更新列表
```
...
xListView.notifyDataSetChanged(pageIndex);
...
```


