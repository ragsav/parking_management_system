package com.example.se1.MallListRecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.se1.R;

import java.lang.reflect.Array;
import java.util.List;

public class TransactionsRecyclerViewAdapter extends RecyclerView.Adapter<TransactionsRecyclerViewAdapter.ViewHolder> {


    private List<String> l;
    private Context context;

    public TransactionsRecyclerViewAdapter(List<String> l, Context context) {
        this.l = l;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.transaction_item, parent, false);
        return new TransactionsRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String s[] = new String[2];
        s = l.get(position).split(",");
        holder.textView_mall_id.setText(s[0]);
        holder.textView_plot_id.setText(s[1]);
    }

    @Override
    public int getItemCount() {
        return l.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView_mall_id;
        TextView textView_plot_id;
        TextView textView_time;
        TextView textView_duration;
        TextView textView_extension;
        TextView textView_date;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView_duration = itemView.findViewById(R.id.duration);
            textView_time = itemView.findViewById(R.id.time);
            textView_mall_id = itemView.findViewById(R.id.mall_id);
            textView_plot_id = itemView.findViewById(R.id.plot_id);
            textView_extension = itemView.findViewById(R.id.extension);
            textView_date = itemView.findViewById(R.id.date);
        }
    }
}
