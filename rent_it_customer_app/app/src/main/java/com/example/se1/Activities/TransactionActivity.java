package com.example.se1.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;


import com.example.se1.Auth.Auth;
import com.example.se1.MallListRecyclerView.MallListRecyclerViewAdapter;
import com.example.se1.MallListRecyclerView.TransactionsRecyclerViewAdapter;
import com.example.se1.MallListViewModel.MallDataViewModel;
import com.example.se1.MallListViewModel.MallViewModelFactory;
import com.example.se1.Models.Mall;
import com.example.se1.R;

import java.util.ArrayList;
import java.util.List;

public class TransactionActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TransactionsRecyclerViewAdapter transactionsRecyclerViewAdapter;
    private List<String> transactons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);
        transactons = (List<String>) getIntent().getSerializableExtra("transactions");

        Log.i("TAG", "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!" + transactons.get(0));
        setUpMallRecyclerView();
    }


    private void setUpMallRecyclerView() {
        recyclerView = findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        transactionsRecyclerViewAdapter = new TransactionsRecyclerViewAdapter(
                transactons, TransactionActivity.this);
        recyclerView.setAdapter(transactionsRecyclerViewAdapter);

    }
}
