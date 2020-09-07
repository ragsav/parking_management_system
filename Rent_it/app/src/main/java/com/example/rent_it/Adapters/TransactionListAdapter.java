package com.example.rent_it.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rent_it.Models.Transaction;
import com.example.rent_it.R;

import java.util.List;

public class TransactionListAdapter extends RecyclerView.Adapter<TransactionListAdapter.MyViewHolder> {
    private List<Transaction> mDataset;
    private Context context;
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView transaction_id,time;
        private CardView completed;


        public MyViewHolder(View v) {
            super(v);
            transaction_id = v.findViewById(R.id.transaction_id);
            time = v.findViewById(R.id.time);
            completed = v.findViewById(R.id.completed);
        }
    }
    public void refreshData(List<Transaction> myDataset){
        this.mDataset = myDataset;
        notifyDataSetChanged();
    }
    public TransactionListAdapter(List<Transaction> myDataset, Context context) {
//        this.auth = auth;
        this.mDataset = myDataset;
        this.context = context;
    }

    @Override
    public TransactionListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.transaction_item, parent, false);


        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        return new TransactionListAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final TransactionListAdapter.MyViewHolder holder, final int position) {
        holder.transaction_id.setText(mDataset.get(position).getTransaction_id());
        String startTimeString,endTimeString;
        Integer s = mDataset.get(position).getTime();
        Integer e = mDataset.get(position).getTime()+mDataset.get(position).getDuration();
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

        holder.time.setText(startTimeString+"-"+endTimeString);
        if(mDataset.get(position).isCompleted()){
            holder.completed.setCardBackgroundColor(Color.GREEN);
        }else{
            holder.completed.setCardBackgroundColor(Color.RED);
        }
    }

    @Override
    public int getItemCount() {
        if (mDataset == null) return 0;
        return mDataset.size();
    }
}
