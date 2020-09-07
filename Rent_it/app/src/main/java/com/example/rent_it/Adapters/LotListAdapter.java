package com.example.rent_it.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import com.example.rent_it.Interfaces.AreaItemClicked;
import com.example.rent_it.Models.Lot;
import com.example.rent_it.R;
import com.example.rent_it.Utilities.StringOperations;
import com.google.android.material.button.MaterialButton;
import com.wajahatkarim3.easyflipview.EasyFlipView;

import java.util.List;

public class LotListAdapter extends RecyclerView.Adapter<LotListAdapter.AreaViewHolder> {
    private List<Lot> mDataset;
    private AreaItemClicked areaItemClicked;
    private Context context;
//    private Auth auth;
    public void refreshLotList(List<Lot> dataset){
        mDataset = dataset;
        notifyDataSetChanged();
    }
    public static class AreaViewHolder extends RecyclerView.ViewHolder {
        public TextView area_name,opening_time,closing_time,city_name,lot_name,status_text;
        LinearLayout area_card_image;
        CardView area_card,info_card,status_color;
        ImageButton info_cancel,info_ok;
        MaterialButton book;
        boolean info_visible=false;

        public AreaViewHolder(View v) {
            super(v);
            status_color = v.findViewById(R.id.status_color);
            status_text = v.findViewById(R.id.status_text);
            lot_name = v.findViewById(R.id.lot_name);
            opening_time = v.findViewById(R.id.opening_time);
            closing_time = v.findViewById(R.id.closing_time);
            city_name = v.findViewById(R.id.city_name);
            area_card = v.findViewById(R.id.area_card);
            info_card = v.findViewById(R.id.info_card);
            book=v.findViewById(R.id.book);
            info_cancel = v.findViewById(R.id.info_cancel);
            info_ok = v.findViewById(R.id.info_ok);
            area_card_image = v.findViewById(R.id.area_card_image);
            area_name = v.findViewById(R.id.area_name);

        }
    }

    public LotListAdapter(List<Lot> myDataset, Context context,AreaItemClicked areaItemClicked) {
//        this.auth = auth;
        this.mDataset = myDataset;
        this.areaItemClicked = areaItemClicked;
        Log.i("AREA LIST",">>>>>>>>>>>>>>>>>>>>>>inside area list adapter constructor");
        this.context = context;
    }

    @Override
    public AreaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.main_activity_recycler_view_lot_item, parent, false);



        return new AreaViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final AreaViewHolder holder, final int position) {
//        Log.i("TAG", mDataset.get(position).getName());

        Integer s = mDataset.get(position).getOpen_time();
        Integer e = mDataset.get(position).getClose_time();
        String startTimeString,endTimeString;
        if(s<12){
            startTimeString = s.toString()+"am";
        }else{
            startTimeString = s.toString()+"pm";
        }
        if(e<12){
            endTimeString = e.toString()+"am";
        }else{
            endTimeString = e.toString()+"pm";
        }

        if(mDataset.get(position).isStatus()){
            holder.status_text.setText("RESERVATIONS OPEN");
            holder.status_color.setCardBackgroundColor(Color.GREEN);
            holder.book.setBackgroundColor(context.getResources().getColor(R.color.secondary));
            holder.book.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    areaItemClicked.onClick(mDataset.get(position).getCity_id(),mDataset.get(position).getArea_id());
                }
            });
        }else{
            holder.status_text.setText("RESERVATIONS CLOSED");
            holder.status_color.setCardBackgroundColor(Color.RED);
            holder.book.setBackgroundColor(Color.LTGRAY);
        }
        Log.i("STATUS",">>>>>>>>>>>>>>>>>>>>"+mDataset.get(position).isStatus());
        String cleanImage = mDataset.get(position).getCover_image()
                .replace("data:image/png;base64,", "").replace("data:image/jpeg;base64,","");
        byte[] decodedString = Base64.decode(cleanImage, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        BitmapDrawable background = new BitmapDrawable(decodedByte);
        holder.area_card_image.setBackground(background);
        holder.opening_time.setText(startTimeString);
        holder.closing_time.setText(endTimeString);
        holder.area_name.setText(StringOperations.ToTitleCase(mDataset.get(position).getArea_name()));
        holder.city_name.setText(StringOperations.ToTitleCase(mDataset.get(position).getCity_name()));
        holder.lot_name.setText(StringOperations.ToTitleCase(mDataset.get(position).getName().toUpperCase()));
        Log.i("AREA LIST",">>>>>>>>>>>>>>>>>>>>>>bind"+mDataset.get(position).getName());
    }

    @Override
    public int getItemCount() {
        if (mDataset == null) return 0;
        return mDataset.size();
    }
}
