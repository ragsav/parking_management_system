package com.example.rent_it.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.rent_it.Adapters.LotListAdapter;
import com.example.rent_it.Auth.Auth;
import com.example.rent_it.Interfaces.AuthOnCompleteRetreiveInterface;
import com.example.rent_it.NetworkCalls.DashBoardNetworkUtility;
import com.example.rent_it.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import net.cachapa.expandablelayout.ExpandableLayout;

import org.json.JSONArray;
import org.json.JSONObject;

public class UserActivity extends AppCompatActivity {

    private Auth auth;
    private ProgressBar list_progress_bar;
    private ImageButton refresh_user;
    private CardView no_connection_card;
    private Toolbar toolbar;
    private MenuItem search_item,history_item;
    private DashBoardNetworkUtility dashBoardNetworkUtility;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView();
        setAuth();
    }

    private void setAuth(){
        auth = new Auth(this);
        auth.getUser(new AuthOnCompleteRetreiveInterface() {
            @Override
            public void onServerUserRetrieveSuccess(JSONObject user, JSONArray vehicles) {
                history_item.setVisible(true);
            }

            @Override
            public void onServerUserRetrieveFailure() {

            }
        });


    }

    private void setView(){
        setContentView(R.layout.user_activity);
        toolbar = findViewById(R.id.topAppBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.user_activity_menu, menu);
        history_item = menu.findItem(R.id.history_button);
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
            case R.id.history_button:
            {
                startActivity(new Intent(UserActivity.this, TransactionsActivity.class));
                return true;
            }

            default:
                return super.onOptionsItemSelected(item);

        }
    }
}