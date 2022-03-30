package com.news.weezo.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.news.weezo.R;
import com.news.weezo.model.MediaFile;
import com.online.academic.utils.OnRecycleItemClick;
import com.online.academic.utils.Utils;

import java.util.ArrayList;

/**
 * Created by abc on 12/18/2017.
 */

public class TextNewsAdapter extends RecyclerView.Adapter<TextNewsAdapter.NotificationViewHolder> {

    private ArrayList<MediaFile> listSectionModel;
    private Context context;
    private OnRecycleItemClick recycleItemClick;
    private int layoutView;

    public TextNewsAdapter(Context context, int layoutView, ArrayList<MediaFile> list, OnRecycleItemClick recycleItemClick) {
        listSectionModel = list;
        setHasStableIds(true);
        this.context = context;
        this.recycleItemClick = recycleItemClick;
        this.layoutView = layoutView;
    }

//    public void setState(String state) {
//        this.state = state;
//    }

    public void setList(ArrayList<MediaFile> listSectionModel) {
        this.listSectionModel.addAll(listSectionModel);
        notifyDataSetChanged();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public NotificationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = null;
        v = LayoutInflater.from(parent.getContext())
                .inflate(layoutView, parent, false);
        return new NotificationViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final NotificationViewHolder holder, int position) {
        MediaFile model = listSectionModel.get(position);
        
        if (model.getDescription() != null) {
            holder.tv_title.setText(Html.fromHtml(model.getName()).toString().trim());
        }
        holder.progress_bar.setVisibility(View.VISIBLE);
        RequestOptions requestOptions = new RequestOptions();
        Glide.with(context)
                .load(Glide.with(context).load(model.getMusic_filename())).addListener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
              //  holder.img_play.setVisibility(View.VISIBLE);
                holder.progress_bar.setVisibility(View.GONE);
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
             //   holder.img_play.setVisibility(View.VISIBLE);
                holder.progress_bar.setVisibility(View.GONE);
                return false;
            }
        })
                .apply(requestOptions)
                .thumbnail(Glide.with(context).load(model.getMusic_filename()))
                .into(holder.img_preview);
        holder.tv_total_likes.setText(""+Integer.parseInt(model.getLikes()));
        holder.tv_total_views.setText(""+Integer.parseInt(model.getViews()));
        if (Integer.parseInt(model.is_liked())==1) {
            holder.img_like.setColorFilter(ContextCompat.getColor(context, R.color.colorselected), android.graphics.PorterDuff.Mode.SRC_IN);
            //holder.img_like.setImageTintList();
        } else {
            holder.img_like.setColorFilter(ContextCompat.getColor(context, R.color.colorgray), android.graphics.PorterDuff.Mode.SRC_IN);
        }

      //  holder.tv_size.setText("Size : " +Utils.Companion.readableFileSize(Long.parseLong(model.getFile_size())));
     // String[] timearray=  model.getDatetime().split(" ");
        holder.tv_date_time.setText("" + Utils.Companion.getFormatedDate(model.getDatetime()));

//
    }

    @Override
    public int getItemCount() {
        return listSectionModel.size();
    }


    public ArrayList<MediaFile> getList() {
        return this.listSectionModel;
    }

    class NotificationViewHolder extends RecyclerView.ViewHolder {
        final TextView tv_title;
        final ImageView img_preview;
        final ImageView img_like;
        final ImageView img_share;
        final TextView tv_total_views;
        final TextView tv_total_likes;
        final TextView tv_date_time;
     //   final TextView tv_src;
        final TextView tv_click_toread_more;
    //    final TextView tv_size;
        final ProgressBar progress_bar;
        final CardView layout_play_video;

        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.tv_description);
            tv_total_views = itemView.findViewById(R.id.tv_total_views);
            tv_total_likes = itemView.findViewById(R.id.tv_total_likes);
            tv_date_time = itemView.findViewById(R.id.tv_date_time);
        //    tv_src = itemView.findViewById(R.id.tv_src);
         //   tv_size = itemView.findViewById(R.id.tv_size);
            img_preview = itemView.findViewById(R.id.img_preview);
            img_like = itemView.findViewById(R.id.img_like);
            img_share = itemView.findViewById(R.id.img_share);
            progress_bar = itemView.findViewById(R.id.progress_bar);
            tv_click_toread_more = itemView.findViewById(R.id.tv_click_toread_more);
            layout_play_video = itemView.findViewById(R.id.layout_play_video);
/*
            img_play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    recycleItemClick.onItemClick(getAdapterPosition(), v);
                }
            });
*/
         /*   img_preview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    img_play.performClick();
                }
            });*/
            img_share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    recycleItemClick.onItemClick(getAdapterPosition(), v);
                }
            });
            img_like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    recycleItemClick.onItemClick(getAdapterPosition(), v);
                }
            });
           /* tv_src.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    recycleItemClick.onItemClick(getAdapterPosition(), v);
                }
            });*/
            tv_click_toread_more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    recycleItemClick.onItemClick(getAdapterPosition(), v);
                }
            });


        }
    }


}
