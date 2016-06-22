## Step 1. Add the JitPack repository to your build file

Add it in your root build.gradle at the end of repositories:

```
allprojects {
	repositories {
		...
		maven { url "https://jitpack.io" }
	}
}
```
	
## Step 2. Add the dependency

```
dependencies {
	compile 'com.github.limengwei:XRecyclerListView:1.0.2'
}
```

## Usage

in yor layout xml
```xml
<org.lmw.xrecyclerlistview.XRecyclerListView
	android:id="@+id/listView"
	android:layout_width="match_parent"
	android:layout_height="match_parent" />
```

in yor activity or fragment

- setRefreshListener
```
xListView.setRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
    @Override
    public void onRefresh() {
        //do samething
    }
});
```
- setLodeMoreListener
```
xListView.setLodeMoreListener(new XRecyclerListView.LodeMoreListener() {
    @Override
    public void onLodeMore() {
       //do samething
    }
}, PAGE_SIZE);
```

- notifyDataSetChanged
```
...
xListView.notifyDataSetChanged(pageIndex);
...
```



	

