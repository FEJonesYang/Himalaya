package com.example.himalaya.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.himalaya.R;
import com.ximalaya.ting.android.opensdk.model.album.Album;

import java.util.ArrayList;
import java.util.List;

/**
 * @author JonesYang
 * @Data 2020-10-19
 * @Function 推荐列表的 item RecyclerView 适配器
 */
public class RecommendListAdapter extends RecyclerView.Adapter<RecommendListAdapter.InnerHolder> {

    //接收数据的集合
    private List<Album> mData = new ArrayList<>();
    //点击单个 View 事件的接口成员
    private OnItemViewClickListener onItemViewClickListener;

    //构造函数，接受获取到的数据
    public RecommendListAdapter() {
    }

    @NonNull
    @Override
    public InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //找到View
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recommend, parent, false);
        InnerHolder innerHolder = new InnerHolder(itemView);
        return innerHolder;
    }

    // onBindViewHolder 是把数据交给对应的 InnerHolder 来显示。
    @Override
    public void onBindViewHolder(@NonNull InnerHolder holder, final int position) {
        //绑定数据
        holder.itemView.setTag(position);
        //点击事件，点击单个的 itemView，跳转到详情界面
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemViewClickListener != null) {
                    onItemViewClickListener.itemViewClickListener(position, mData.get(position));
                }
            }
        });
        holder.setData(mData.get(position));
    }


    // getItemCount 方法要求返回数据的数量。
    @Override
    public int getItemCount() {
        if (mData != null) {
            return mData.size();
        }
        return 0;
    }

    //设置数据
    public void setData(List<Album> albumList) {
        if (mData != null) {
            mData.clear();
            mData.addAll(albumList);
        }
        //更新UI
        notifyDataSetChanged();
    }

    public class InnerHolder extends RecyclerView.ViewHolder {
        public InnerHolder(@NonNull View itemView) {
            super(itemView);
        }

        public void setData(Album album) {
            //找到各个控件，设置数据
            //专辑封面
            ImageView albumCoverIv = itemView.findViewById(R.id.album_cover);
            //标题
            TextView albumTitleTv = itemView.findViewById(R.id.album_title_tv);
            //描述
            TextView albumDescTv = itemView.findViewById(R.id.album_description_tv);
            //播放量
            TextView albumPlayCount = itemView.findViewById(R.id.album_play_count);
            //集数
            TextView albumContentSize = itemView.findViewById(R.id.album_content_size);

            //把获取的数据显示到UI
            albumTitleTv.setText(album.getAlbumTitle());
            albumDescTv.setText(album.getAlbumIntro());
            albumPlayCount.setText(album.getPlayCount() / 10000 + "万");
            albumContentSize.setText(album.getIncludeTrackCount() + "");
            //Glide 的使用，加载封面的图片
            Glide.with(itemView.getContext()).load(album.getCoverUrlLarge()).into(albumCoverIv);

        }
    }


    /**
     * 点击单个 View 事件的接口
     */
    public void setOnItemViewClickListener(OnItemViewClickListener onItemViewClickListener) {
        this.onItemViewClickListener = onItemViewClickListener;
    }

    public interface OnItemViewClickListener {
        void itemViewClickListener(int position, Album album);
    }
}
