package com.example.zhanghongjie.radiogroupandfragment.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.zhanghongjie.radiogroupandfragment.R;
import com.example.zhanghongjie.radiogroupandfragment.WebViewActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ThirdFragment extends Fragment {

    private Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment3_layout, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder != null) {
            unbinder.unbind();
        }
    }

    @OnClick({R.id.webViewBeforeOptimize, R.id.webViewAfterOptimize})
    public void loadWebView(View v) {
        switch (v.getId()) {
            case R.id.webViewBeforeOptimize:
                WebViewActivity.startAction(this.getActivity(), false);
                break;
            case R.id.webViewAfterOptimize:
                WebViewActivity.startAction(this.getActivity(), true);
                break;
            default:
                break;
        }

    }
}
