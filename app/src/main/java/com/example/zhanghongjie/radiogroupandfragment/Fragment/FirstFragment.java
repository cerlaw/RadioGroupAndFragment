package com.example.zhanghongjie.radiogroupandfragment.Fragment;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.SyncStateContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.TimeUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.zhanghongjie.radiogroupandfragment.AlphaPageTransformer;
import com.example.zhanghongjie.radiogroupandfragment.R;

import java.lang.ref.WeakReference;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import butterknife.OnFocusChange;
import butterknife.Unbinder;

public class FirstFragment extends Fragment {

    private Unbinder unbinder;

    @BindView(R.id.editText)
    EditText editText;

    @BindView(R.id.editText2)
    EditText editText2;

    @BindView(R.id.fragment1_main)
    LinearLayout ll;

    @BindView(R.id.login_btn)
    Button login;

    @BindView(R.id.banner_ViewPager)
    ViewPager banner_viewPager;

    @BindView(R.id.banner_indicator)
    LinearLayout indicator_ll;
    private PagerAdapter mPagerAdapter;
    //图片集
    private int[] imgRes = {R.drawable.a, R.drawable.b,
            R.drawable.c, R.drawable.d};
    int length;
    private ImageView[] indicators = new ImageView[imgRes.length];

    private MyHandler myHandler;
    private int mPosition;
    //循环时间
    private static final int WHEEL_TIME = 3000;

    private Timer timer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment1_layout, container, false);
        unbinder = ButterKnife.bind(this, view);
        autoScrollView(ll, login);
        initBanner();
        initIndicator();
        myHandler = new MyHandler();
//        startTimer();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        myHandler.sendEmptyMessageDelayed(1, 0);
//        stopTimer();
    }

    @OnClick({R.id.fragment1_main})
    public void hideSoftInput() {
        Log.d("boss", "hide soft input");
        InputMethodManager imm = (InputMethodManager) getActivity().
                getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        }
    }

    //移动界面使按钮能显示出来
    private int scrollToPosition = 0;
    private void autoScrollView(final View root, final View scrollToView) {
        root.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect rect = new Rect();

                //获取root View在视图中的可视区域
                root.getWindowVisibleDisplayFrame(rect);

                //获取root在窗体中不可视区域高度
                int InVisibleHeight = root.getRootView().getHeight() - rect.bottom;
                Log.d("boss", "不可见区域高度:" + InVisibleHeight);

                //判断不可视高度大于300为打开了键盘
                if (InVisibleHeight > 300) {
                    //获取要移动的View在窗体中的坐标（scrollToView）
                    //location(0) 是X坐标，location(1)是Y坐标
                    int[] location = new int[2];
                    scrollToView.getLocationInWindow(location);

                    //计算root滚动的高度，使scrollToView在可见区域的底部
//                    int scrollHeight = (location[1] + scrollToView.getHeight()) - rect.bottom;
                    int scrollHeight = location[1] - InVisibleHeight;
                    scrollToPosition += scrollHeight;
                }else {
                    //键盘隐藏
                    scrollToPosition = 0;
                }
                root.scrollTo(0, scrollToPosition);
            }
        });
    }

    @OnClick(R.id.login_btn)
    public void login() {
        String s = editText.getText().toString();
        String s2 = editText2.getText().toString();
        if (s.equals("") && s2.equals("")) {
            Toast.makeText(getActivity(), "plz enter text", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(getActivity(), "you enter " + s + s2, Toast.LENGTH_SHORT).show();
        }
        hideSoftInput();
    }

    private void initBanner() {
        //设置page间隔
        banner_viewPager.setPageMargin(20);
        //设置缓存的页面数量
        banner_viewPager.setOffscreenPageLimit(imgRes.length);
        banner_viewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return imgRes.length + 2;
            }

            @Override
            public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
                return view == o;
            }

            @NonNull
            @Override
            public Object instantiateItem(@NonNull ViewGroup container, final int position) {
//                final ImageView imageView = new ImageView(getActivity());
                View view = View.inflate(getActivity(), R.layout.banner_viewpager_item, null);
                ImageView imageView = view.findViewById(R.id.banner_item_image);
                length = imgRes.length;
                //关键代码，将两个数组联系在一起
                final int realPosition = (position - 1 + length) % length;
                imageView.setImageResource(imgRes[realPosition]);
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        hideSoftInput();
                        Toast.makeText(getActivity(), realPosition + " be clicked",
                                Toast.LENGTH_SHORT).show();
                    }
                });
                container.addView(view);
                return view;
            }

            @Override
            public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
                container.removeView((View) object);
            }

        });
        banner_viewPager.setPageTransformer(true, new AlphaPageTransformer());
        //设置首页
        banner_viewPager.setCurrentItem(1);
    }

    private void initIndicator() {
        for (int i = 0; i < indicators.length; i++) {
            ImageView dot = new ImageView(getActivity());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(15,15);
            params.setMargins(5, 0, 5, 0);
            dot.setLayoutParams(params);
            if (i == 0) {
                dot.setBackgroundResource(R.drawable.dot);
            }else {
                dot.setBackgroundResource(R.drawable.dot_unselected);
            }
            indicators[i] = dot;
            indicator_ll.addView(dot);
        }
        //设置onPageChangeListener
        banner_viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.d("position", position + "");
                if (position == imgRes.length && positionOffset > 0.95) {
                    Log.d("offset", positionOffset + "");
                    //在position4左滑且左滑positionOffset百分比接近1时,
                    //即页面即将全部展示出来，偷偷替换为position1（原本会滑到position5）
                    banner_viewPager.setCurrentItem(1, false);
                } else if (position == 0 && positionOffset < 0.05) {
                    Log.d("offset", positionOffset + "");
                    //在position1右滑且右滑百分比接近0时，偷偷替换为position4（原本会滑到position0）
                    banner_viewPager.setCurrentItem(4, false);
                }
            }

            @Override
            public void onPageSelected(int position) {
                int length = imgRes.length;
                mPosition = (position - 1 + length) % length;
                setIndicator(mPosition);
            }

            @Override
            public void onPageScrollStateChanged(int i) {
                switch (i) {
                    case 0:
                        //什么都没做 空闲状态
                        break;
                    case 1:
                        //正在滑动，暂停轮播
                        myHandler.sendEmptyMessage(1);
//                        stopTimer();
                        break;
                    case 2:
                        //滑动结束，开始轮播
                        myHandler.sendEmptyMessageDelayed(0, WHEEL_TIME);
//                        startTimer();
                        break;
                }
            }
        });
    }

    private void setIndicator(int i) {
        int length = indicators.length;
        for (int j = 0; j < length; j++) {
            if (j == i) {
                indicators[i].setBackgroundResource(R.drawable.dot);
            }else {
                indicators[j].setBackgroundResource(R.drawable.dot_unselected);
            }
        }
    }

    //处理轮播的Handler
    public class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    Log.d("jjj", "receive message");
                    //移除所有的msg.what为0的信息，保证只有一个信息队列再跑
                    removeMessages(0);
                    int currentPos = (banner_viewPager.getCurrentItem() + 1) % (imgRes.length + 2);
                    Log.d("jjj", "position" + currentPos);
                    banner_viewPager.setCurrentItem(currentPos);
                    break;
                case 1:
                    removeMessages(0);
                    break;
                default:
                    myHandler.sendEmptyMessage(0);
                    break;
            }
        }
    }

//    private void startTimer() {
//        if (timer == null) {
//            timer = new Timer();
//        }
//        timer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                myHandler.sendEmptyMessage(0);
//            }
//        }, 3000, Integer.MAX_VALUE);
//    }

//    private void stopTimer() {
//        if (timer != null) {
//            timer.cancel();
//            timer = null;
//        }
//    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("jjj", "onResume");
        myHandler.sendEmptyMessageDelayed(0, 3000);
    }
}
