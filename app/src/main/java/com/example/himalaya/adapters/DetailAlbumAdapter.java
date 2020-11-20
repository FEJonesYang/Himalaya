package com.example.himalaya.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.himalaya.R;
import com.ximalaya.ting.android.opensdk.model.track.Track;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @author JonesYang
 * @Data 2020-11-19
 * @Function 详情界面的适配器
 */
public class DetailAlbumAdapter extends RecyclerView.Adapter<DetailAlbumAdapter.InnerViewHolder> {

    //存储数据的集合
    private List<Track> mData = new ArrayList<>();
    //格式化时间
    private SimpleDateFormat mUpdateDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat mDurationFormat = new SimpleDateFormat("mm:ss");

    @NonNull
    @Override
    public DetailAlbumAdapter.InnerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_detail_album, parent, false);
        return new InnerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DetailAlbumAdapter.InnerViewHolder holder, final int position) {
        //绑定数据
        holder.itemView.setTag(position);
        //点击事件，点击单个的 itemView，跳转到音乐播放界面
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO:
                Toast.makeText(v.getContext(), "you click " + position + " item", Toast.LENGTH_SHORT).show();
            }
        });
        //设置数据
        holder.setData(mData.get(position));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setData(List<Track> tracks) {
        //清除原来的值
        mData.clear();
        //添加数据
        mData.addAll(tracks);
        //设置UI
        notifyDataSetChanged();
    }

    public class InnerViewHolder extends RecyclerView.ViewHolder {
        public InnerViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        public void setData(Track track) {
            //获取 itemView 的控件
            //顺序ID
            TextView orderTv = itemView.findViewById(R.id.item_order);
            //标题
            TextView titleTv = itemView.findViewById(R.id.title_detail);
            //播放次数
            TextView playCount = itemView.findViewById(R.id.detail_play_count);
            //播放时长
            TextView playTime = itemView.findViewById(R.id.detail_total_time);
            //更新时间
            TextView updateTime = itemView.findViewById(R.id.tv_detail_time);

            //设置获取的数据
            orderTv.setText(String.valueOf(track.getOrderNum() + 1));
            titleTv.setText(String.valueOf(track.getTrackTitle()));
            playCount.setText(String.valueOf(track.getPlayCount()));

            int durationMil = track.getDuration() * 1000;
            String duration = mDurationFormat.format(durationMil);
            playTime.setText(duration);
            updateTime.setText(mUpdateDateFormat.format(track.getUpdatedAt()));
        }
    }
}
