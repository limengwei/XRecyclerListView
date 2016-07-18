package org.lmw.xrecyclerlistview.sample;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.lmw.xrecyclerlistview.XRecyclerListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    final int PAGE_SIZE = 10;
    int pageIndex = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    XRecyclerListView listView;

    List<String> list;

    SampleAdapter adapter;

    private void initView() {
        listView = (XRecyclerListView) findViewById(R.id.listView);
        list = new ArrayList<>();
        adapter = new SampleAdapter(this, list);
        listView.setAdapter(adapter);

        listView.setRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageIndex = 1;
                getData();
            }
        });

        listView.setLodeMoreListener(new XRecyclerListView.LodeMoreListener() {
            @Override
            public void onLodeMore() {
                pageIndex++;
                getData();
            }
        }, PAGE_SIZE);

        listView.setRefreshing(true);
        getData();
    }

    public void hello(View v){
        Toast.makeText(MainActivity.this,"hellooo",Toast.LENGTH_SHORT).show();
        listView.getLayoutManager().scrollToPositionWithOffset(5,0);
    }

    boolean datageting = false;

    private void getData() {
        if (datageting) return;
        datageting = true;

        //模拟数据
        listView.postDelayed(new Runnable() {
            @Override
            public void run() {

                if (pageIndex == 1) {
                    list.clear();
                }

                int k = PAGE_SIZE;

                if (pageIndex == 3) {
                    k = 5;
                }

                for (int i = 0; i < k; i++) {
                    list.add("Item" + (PAGE_SIZE * (pageIndex - 1)) + i);
                }

                listView.notifyDataSetChanged(pageIndex);

                datageting = false;
            }
        }, 3000);
    }


    class SampleAdapter extends RecyclerView.Adapter<SampleAdapter.ViewHolder> {
        Context context;
        List<String> list;

        public SampleAdapter(Context context, List<String> list) {
            this.context = context;
            this.list = list;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.view_item, parent, false));
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.textView.setText(list.get(position));
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView textView;

            public ViewHolder(View itemView) {
                super(itemView);
                textView = (TextView) itemView.findViewById(R.id.text1);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(MainActivity.this,"hellooo",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }
}
