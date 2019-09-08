package com.example.wbj;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.wbj.picture.browser.ImageBrowserActivity;
import com.example.wbj.service.EchoService;
import com.example.wbj.service.SocketServer;
import com.wbj.view.DragListActivity;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WbjActivity extends AppCompatActivity {
    private static final String TAG = WbjActivity.class.getSimpleName();
    private ListView mListView;
    private SparseArray<Class> sAtyArray = new SparseArray<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_aty);

        sAtyArray.put(0, WebViewActivity.class);
        sAtyArray.put(1, ProgressBarActivity.class);
        sAtyArray.put(2, ImageBrowserActivity.class);
        sAtyArray.put(3, DialogPriorityActivity.class);
        sAtyArray.put(4, NumberDynamicActivity.class);
        sAtyArray.put(5, RecyclerViewActivity.class);
        sAtyArray.put(6, SettingSetActivity.class);
        sAtyArray.put(7, ClipActivity.class);
        sAtyArray.put(8, DragListActivity.class);
        sAtyArray.put(9, MVPLoginActivity.class);
        sAtyArray.put(10, TitleScrollViewActivity.class);

        FrameLayout rootLayout = findViewById(android.R.id.content);
        Log.i(TAG, "rootLayout: " + rootLayout);

        mListView = findViewById(R.id.main_lv);
        final String[] listItems = getResources().getStringArray(R.array.main_item_list);
        mListView.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return listItems.length;
            }

            @Override
            public Object getItem(int i) {
                return listItems[i];
            }

            @Override
            public long getItemId(int i) {
                return i;
            }

            @Override
            public View getView(int pst, View view, ViewGroup viewGroup) {
                if (view == null) {
                    view = View.inflate(WbjActivity.this, R.layout.main_lv_adt, null);
                }
                ((TextView) view.findViewById(R.id.text_lv_item)).setText((pst + 1) + ". " + listItems[pst]);
                return view;
            }
        });

        openActivity(2);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pst, long id) {
                openActivity(pst);
            }
        });

        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int scrollState) {
                switch (scrollState) {//短时间(如一秒)内多次上下滚动列表，每次都会触发以下三个状态，哪怕时间很短暂
                    case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
                        Log.w(TAG, "scrollState: " + scrollState + ", 触发滚动");
                        break;
                    case AbsListView.OnScrollListener.SCROLL_STATE_FLING:
                        Log.w(TAG, "scrollState: " + scrollState + ", 滚动中");
                        break;
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                        Log.w(TAG, "scrollState: " + scrollState + ", 停止滚动；第一个可见位置：" + mListView.getFirstVisiblePosition());
                        if (mListView.getLastVisiblePosition() == mListView.getCount() - 1) {
                            Log.w(TAG, "滚动到底部，最后一个位置：" + mListView.getLastVisiblePosition());
                        }
                        break;
                    default:
                        break;
                }
            }

            /**
             * @param firstVisibleItem 屏幕里第一个Item(部分显示的也算)在ListView中的位置
             * @param visibleItemCount 屏幕里可以见到的Item(部分显示的也算)数量
             * @param totalItemCount Item总数
             */
            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                Log.i(TAG, "firstVisibleItem: " + firstVisibleItem + ", visibleItemCount: " + visibleItemCount + ", totalItemCount: " + totalItemCount);
                /*
                可根据totalItemCount是否大于visibleItemCount判断Item是否填满ListView的高度：如果大于则填满，可上下滚动；
                如果等于则刚刚填满，可能可以上下小幅度滚动(因为最后一项可能只是部分显示)；如果小于则填不满，不能上下滚动。
                 */
            }
        });
        //mListView.setSelection(12);//设置屏幕里第一个显示的Item，默认为0

        //this.start();//为什么如果不通过服务启动ServerSocket，客户端连接上服务端后会报错？
        startService(new Intent(this, EchoService.class));
    }

    public void start() {
        try {
            ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
            ServerSocket serverSocket = new ServerSocket(8093);
            while (true) {
                Log.d(TAG, "服务器正在运行，等待客户端连接......");
                Socket socket = serverSocket.accept();
                /*
                 多线程处理机制：每一个客户端连接之后都启动一个线程，以保证服务器可以同时与多个客户端通信。如果是单线程
                 处理机制，那么服务器每次只能与一个客户端连接，其他客户端无法同时连接服务器，要等待服务器出现空闲才可以连接。
                 */
                cachedThreadPool.execute(new SocketServer(socket));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openActivity(final int position) {
        try {
            Intent intent = new Intent(this, sAtyArray.get(position));
            this.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sAtyArray.clear();
        //stopService(new Intent(this, ClipService.class));在别处启动的服务，可以要退出主页时停止
    }
}
