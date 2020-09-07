package com.example.rent_it.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.rent_it.Adapters.TransactionListAdapter;
import com.example.rent_it.Auth.Auth;
import com.example.rent_it.Interfaces.AuthOnCompleteRetreiveInterface;
import com.example.rent_it.Interfaces.OnUserTransactionsRetrieved;
import com.example.rent_it.Models.Transaction;
import com.example.rent_it.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class TransactionsActivity extends AppCompatActivity {

    private Auth auth;
    private RecyclerView transaction_list;
    private LinearLayoutManager transactionLayoutManager;
    private TransactionListAdapter transactionListAdapter;
    private LinearLayout transaction_list_view, no_connection, progress_bar_view;
    private Toolbar toolbar;
    private MenuItem search_list_item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setViews();
        recyclerViewSetter();
        setAuth();
    }
    private void setViews(){
        setContentView(R.layout.transactions_activity);
        toolbar = findViewById(R.id.topAppBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        transaction_list = findViewById(R.id.transaction_list);
        no_connection = findViewById(R.id.no_connection);
        progress_bar_view = findViewById(R.id.progress_bar_view);
        transaction_list_view = findViewById(R.id.transaction_list_view);

    }
    private void setAuth(){
        auth = new Auth(this);
        auth.getUser(new AuthOnCompleteRetreiveInterface() {
            @Override
            public void onServerUserRetrieveSuccess(JSONObject user, JSONArray vehicles) {
                getTransactions();
            }

            @Override
            public void onServerUserRetrieveFailure() {
                setConnectionProblem();
            }
        });


    }
    private void recyclerViewSetter() {
        transaction_list.setHasFixedSize(true);
        transactionLayoutManager = new LinearLayoutManager(this);
        transaction_list.setLayoutManager(transactionLayoutManager);
    }
    private void setConnectionProblem(){
        no_connection.setVisibility(View.VISIBLE);
        progress_bar_view.setVisibility(View.INVISIBLE);
        transaction_list_view.setVisibility(View.INVISIBLE);
    }
    private void setTransactionView(){
        search_list_item.setVisible(true);
        no_connection.setVisibility(View.INVISIBLE);
        progress_bar_view.setVisibility(View.INVISIBLE);
        transaction_list_view.setVisibility(View.VISIBLE);
    }
    private void getTransactions(){
        auth.getUserTransactions(new OnUserTransactionsRetrieved() {
            @Override
            public void onUserTransactionRetrieveSuccess(List<Transaction> list_transaction) {
                if(transactionListAdapter==null){
                    transactionListAdapter = new TransactionListAdapter(list_transaction,getApplication());
                    transaction_list.setAdapter(transactionListAdapter);
                }else{
                    transactionListAdapter.refreshData(list_transaction);
                }
                setTransactionView();
            }

            @Override
            public void onUserTransactionRetrieveFailure() {
                setConnectionProblem();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.transaction_activity_menu, menu);
        search_list_item = menu.findItem(R.id.search_button);
        search_list_item.setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
            {
                finish();
                return true;
            }
            case R.id.search_button:
            {
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);

        }
    }
}