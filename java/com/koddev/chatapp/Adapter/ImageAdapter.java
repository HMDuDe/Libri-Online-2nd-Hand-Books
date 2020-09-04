package com.koddev.chatapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.koddev.chatapp.R;
import com.koddev.chatapp.Model.Upload;
import com.koddev.chatapp.View_ad;

import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {
    private Context mContext;
    private List<Upload> mUploads;

    public ImageAdapter(Context context, List<Upload> uploads) {
        mContext = context;
        mUploads = uploads;

    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.ad, parent, false);
        return new ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, final int position) {
        final Upload uploadCurrent = mUploads.get(position);

        holder.ad_title.setText(uploadCurrent.getTitle());
        holder.ad_author.setText(uploadCurrent.getAuthor());
        holder.ad_year.setText(uploadCurrent.getYear());
        holder.ad_faculty.setText(uploadCurrent.getFaculty());
        holder.ad_status.setText(uploadCurrent.getStatus());
        holder.ad_price.setText("R" + uploadCurrent.getPrice());
        holder.ad_condition.setText(uploadCurrent.getCondition());
        holder.ad_seller.setText("Posted by: "+ uploadCurrent.getSeller());
        holder.ad_date.setText("on " + uploadCurrent.getDate());


        Glide.with(mContext)
                .load(uploadCurrent.getImageURL())
                .placeholder(R.mipmap.ic_launcher)
                .into(holder.ad_image);

        holder.advert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, View_ad.class);
                intent.putExtra("ad_id", uploadCurrent.getId());
                intent.putExtra("title", uploadCurrent.getTitle());
                intent.putExtra("author", uploadCurrent.getAuthor());
                intent.putExtra("status", uploadCurrent.getStatus());
                intent.putExtra("condition", uploadCurrent.getCondition());
                intent.putExtra("seller", uploadCurrent.getSeller());
                intent.putExtra("sellerID", uploadCurrent.getSellerID());
                intent.putExtra("price",  uploadCurrent.getPrice());
                intent.putExtra("date", uploadCurrent.getDate());
                intent.putExtra("image", uploadCurrent.getImageURL());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mUploads.size();
    }


    public class ImageViewHolder extends RecyclerView.ViewHolder {
        public TextView ad_title, ad_author, ad_condition, ad_year, ad_price, ad_status, ad_faculty, ad_seller, ad_date;
        public RelativeLayout advert;
        public ImageView ad_image;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            ad_title = itemView.findViewById(R.id.ad_title);
            ad_image = itemView.findViewById(R.id.ad_image);
            ad_author = itemView.findViewById(R.id.ad_author);
            ad_condition = itemView.findViewById(R.id.ad_condition);
            ad_year = itemView.findViewById(R.id.ad_year);
            ad_price = itemView.findViewById(R.id.ad_price);
            ad_status = itemView.findViewById(R.id.ad_status);
            ad_faculty = itemView.findViewById(R.id.ad_faculty);
            ad_seller = itemView.findViewById(R.id.ad_seller);
            ad_date = itemView.findViewById(R.id.ad_date);
            advert = itemView.findViewById(R.id.advert);

        }
    }
}


