package com.example.zhanghongjie.radiogroupandfragment.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.zhanghongjie.radiogroupandfragment.Adapter.ShareFragmentRecyclerAdapter;
import com.example.zhanghongjie.radiogroupandfragment.R;
import com.example.zhanghongjie.radiogroupandfragment.Model.ShareItem;

import java.util.ArrayList;
import java.util.List;

public class ShareFragment extends BottomSheetDialogFragment {

    private static String mTitle;
    private static String mUrl;
    private Context mContex;
    private List<ResolveInfo> mShareResolveInfoList;
    private List<ShareItem> mShareItemList;

    public static ShareFragment getInstance(String title, String url) {
        mTitle = title;
        mUrl = url;
        return new ShareFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //将白色背景设为透明
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContex = getContext();
        View view = inflater.inflate(R.layout.sharefragment_layout, container, false);
        initialData();
        initialView(view);
        return view;
    }

    private void initialData() {
        //获取要分享的app list
        mShareItemList = new ArrayList<>();
        mShareResolveInfoList = getShareList();
        int size = mShareResolveInfoList.size();
        for (int i = 0; i < size; i++) {
            ShareItem item = new ShareItem();
            item.setIcon(mShareResolveInfoList.get(i).loadIcon(mContex.getPackageManager()));
            item.setTitle(mShareResolveInfoList.get(i).loadLabel(mContex.getPackageManager()).toString());
            mShareItemList.add(item);
            Log.d("share", item.getTitle());
        }
    }

    private void initialView(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.fragment_share_recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(mContex, 3));
        ShareFragmentRecyclerAdapter adapter = new ShareFragmentRecyclerAdapter(mShareItemList, mContex);
        adapter.setOnItemListener(new ShareFragmentRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT, "这是我的分享喔");
                intent.setType("text/plain");
                ActivityInfo info = mShareResolveInfoList.get(position).activityInfo;
                intent.setClassName(info.packageName, info.name);
                startActivity(intent);
                //该Fragment不再显示
                dismiss();
            }
        });
        recyclerView.setAdapter(adapter);
    }


    public List<ResolveInfo> getShareList() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        PackageManager packageManager = mContex.getPackageManager();
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent, 0);
        return list;
    }
}
