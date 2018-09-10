package com.example.zhanghongjie.radiogroupandfragment.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zhanghongjie.radiogroupandfragment.R;
import com.example.zhanghongjie.radiogroupandfragment.Model.ShareItem;

import java.util.List;

public class ShareFragmentRecyclerAdapter extends RecyclerView.Adapter<
        ShareFragmentRecyclerAdapter.ShareFragmentRecyclerAdapterViewHolder> {

    private Context mContext;
    private List<ShareItem> appShareList;
    private OnItemClickListener listener;

    public ShareFragmentRecyclerAdapter(List<ShareItem> list, Context context) {
        mContext = context;
        appShareList = list;
    }

    public interface OnItemClickListener {
        void onItemClick(int pos);
    }

    @NonNull
    @Override
    public ShareFragmentRecyclerAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.sharefragment_recycler_item, null);
        ShareFragmentRecyclerAdapterViewHolder viewHolder = new ShareFragmentRecyclerAdapterViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ShareFragmentRecyclerAdapterViewHolder shareFragmentRecyclerAdapterViewHolder,
                                 final int position) {
        ShareItem item = appShareList.get(position);
        shareFragmentRecyclerAdapterViewHolder.imageView.setImageDrawable(item.getIcon());
        shareFragmentRecyclerAdapterViewHolder.textView.setText(item.getTitle());
        shareFragmentRecyclerAdapterViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClick(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return appShareList.size();
    }

    public void setOnItemListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public class ShareFragmentRecyclerAdapterViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;
        private TextView textView;

        public ShareFragmentRecyclerAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.sharefragment_app_icon);
            textView = itemView.findViewById(R.id.sharefragment_app_name);
        }
    }
}
