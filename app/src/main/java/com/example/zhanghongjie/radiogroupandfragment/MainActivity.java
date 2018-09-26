package com.example.zhanghongjie.radiogroupandfragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.zhanghongjie.radiogroupandfragment.Fragment.FirstFragment;
import com.example.zhanghongjie.radiogroupandfragment.Fragment.FourthFragment;
import com.example.zhanghongjie.radiogroupandfragment.Fragment.SecondFragment;
import com.example.zhanghongjie.radiogroupandfragment.Fragment.ShareFragment;
import com.example.zhanghongjie.radiogroupandfragment.Fragment.ThirdFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity{

    public static final String TAG = "boss";

    @BindView(R.id.main_fragment)
    ViewPager fragementViewPager;

    @BindViews({R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4})
    List<RadioButton> btn1, btn2, btn3, btn4;

    @BindView(R.id.radio_group)
    RadioGroup radioGroup;

    private Fragment mFragment;
    private FragmentManager fm;
    private SparseArray<Fragment> fragments;
    /**左滑为true，右滑为false*/
    private boolean direction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //设置后将不会绘制主体背景图，只适用于背景被覆盖时使用
//        getWindow().setBackgroundDrawable(null);
        ButterKnife.bind(this);

        fragments = new SparseArray<Fragment>(4);
        fragments.put(0, new FirstFragment());
        fragments.put(1, new SecondFragment());
        fragments.put(2, new ThirdFragment());
        fragments.put(3, new FourthFragment());

        initView();
    }

    private void initView() {
        fragementViewPager.setAdapter(new FragmentStatePagerAdapter(
                getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int i) {
                mFragment = fragments.get(i);
                return mFragment;
            }

            @Override
            public int getCount() {
                return fragments.size();
            }
        });

        fragementViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
                direction = i1 - i < 0;
            }

            @Override
            public void onPageSelected(int i) {
                radioGroup.clearCheck();
                switch (i) {
                    case 0:
                        radioGroup.check(R.id.btn1);
                        break;
                    case 1:
                        radioGroup.check(R.id.btn2);
                        break;
                    case 2:
                        radioGroup.check(R.id.btn3);
                        break;
                    case 3:
                        radioGroup.check(R.id.btn4);
                        break;
                    default:
                        break;
                }

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }


    @OnClick({R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.btn1:
                Log.d(TAG, "press 0");
                switchFragment(0);
                break;
            case R.id.btn2:
                Log.d(TAG, "press 1");
                switchFragment(1);
                break;
            case R.id.btn3:
                Log.d(TAG, "press 2");
                switchFragment(2);
                break;
            case R.id.btn4:
                Log.d(TAG, "press 3");
                switchFragment(3);
                break;
            default:
                break;
        }
    }

    private void switchFragment(int index) {
        fragementViewPager.setCurrentItem(index);
    }

    //显示分享按钮
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.sharebutton, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.shareButton:
                ShareFragment fragment = new ShareFragment();
                fragment.show(getSupportFragmentManager(), "dialog");
                Toast.makeText(this, "分享", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
