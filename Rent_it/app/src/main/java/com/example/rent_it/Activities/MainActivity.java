package com.example.rent_it.Activities;


import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.afollestad.materialdialogs.ModalDialog;
import com.chivorn.smartmaterialspinner.SmartMaterialSpinner;
import com.example.rent_it.Adapters.LotListAdapter;
import com.example.rent_it.Auth.Auth;
import com.example.rent_it.Interfaces.AreaItemClicked;
import com.example.rent_it.Interfaces.AuthOnCompleteRetreiveInterface;
import com.example.rent_it.Interfaces.OnAreaListFetchedInterface;
import com.example.rent_it.Interfaces.OnCityFetchInterface;
import com.example.rent_it.Interfaces.OnLotListFetchedInterface;
import com.example.rent_it.Interfaces.OnSearchResultFetchInterface;
import com.example.rent_it.Models.Area;
import com.example.rent_it.Models.City;
import com.example.rent_it.Models.Lot;
import com.example.rent_it.NetworkCalls.DashBoardNetworkUtility;
import com.example.rent_it.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.shreyaspatil.MaterialDialog.BottomSheetMaterialDialog;
import com.shreyaspatil.MaterialDialog.interfaces.DialogInterface;
import com.skydoves.powerspinner.IconSpinnerAdapter;
import com.skydoves.powerspinner.IconSpinnerItem;
import com.skydoves.powerspinner.OnSpinnerItemSelectedListener;
import com.skydoves.powerspinner.PowerSpinnerView;
import com.volobot.stringchooser.StringChooser;

import net.cachapa.expandablelayout.ExpandableLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;


public class MainActivity extends AppCompatActivity {

    private Auth auth;
    private RecyclerView lot_list;
    private LotListAdapter lotListAdapter;
    private TextInputEditText search_input_field;
    private LinearLayoutManager areaLayoutManager;
    private LinearLayout lot_list_view, no_connection_view, progress_bar_view;
    private ExpandableLayout search_view;
    private ImageButton refresh_list;
    private TextInputLayout search_input_layout;
    private Toolbar toolbar;
    private MenuItem search_item,history_item;
    Calendar date;
    private DashBoardNetworkUtility dashBoardNetworkUtility;
    private InputMethodManager imm;
    protected List<City> city_list_data;
    protected List<Area> area_list_data;
    private List<String> area_list_string_data,city_list_string_data;
    private BottomSheetMaterialDialog mBottomSheetDialog;
    private MaterialButton city_selector,area_selector,filter_selector;
    protected String city_id="5f49fe8533adbd254c574e88",area_id="SEND_ALL";
    private SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        setView();
        recyclerViewSetter();
        setDashBoardNetworkUtility();
        setListeners();
        setAuth();
    }

    private void setConfirmationModal(){
         mBottomSheetDialog= new BottomSheetMaterialDialog.Builder(this)
                .setTitle("Delete?")
                .setMessage("Are you sure want to delete this file?")
                .setCancelable(false)
                .setPositiveButton("Delete", new BottomSheetMaterialDialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        mBottomSheetDialog.dismiss();
                    }
                })
                .setNegativeButton("Cancel", new BottomSheetMaterialDialog.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        mBottomSheetDialog.dismiss();
                    }
                })
                .build();
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
        setContentView(R.layout.main_activity);
        toolbar = findViewById(R.id.topAppBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        search_view = findViewById(R.id.search_view_expandable);
        refresh_list = findViewById(R.id.refresh_button);
        lot_list_view = findViewById(R.id.lot_list_view);
        no_connection_view = findViewById(R.id.no_connection);
        search_input_layout = findViewById(R.id.search_input_layout);
        search_input_field = findViewById(R.id.search_input_field);
        progress_bar_view = findViewById(R.id.progress_bar_view);
        lot_list = findViewById(R.id.lot_list);
        city_selector = findViewById(R.id.city_selector);
        area_selector = findViewById(R.id.area_selector);
        filter_selector  = findViewById(R.id.filter_selector);

    }

    private void recyclerViewSetter() {

        lot_list.setHasFixedSize(true);
        areaLayoutManager = new LinearLayoutManager(this);
        lot_list.setLayoutManager(areaLayoutManager);
    }

    private void setDashBoardNetworkUtility() {
        this.dashBoardNetworkUtility = new DashBoardNetworkUtility(this
                , new OnAreaListFetchedInterface() {
                    @Override
                    public void onAreaListFetchSuccess(List<Area> area_list) {
                        final List<Area> dummy =area_list;
                        area_list_data = area_list;
                        new Runnable(){

                            @Override
                            public void run() {
                                List<String> idList = new ArrayList<>();
                                idList.add("Display all");
                                for (Area area :
                                        dummy) {
                                    idList.add(area.getName());
                                }
                                area_list_string_data = idList;
                            }

                        }.run();
                    }

                    @Override
                    public void onAreaListFetchFailure() {

                    }
                }
                , new OnLotListFetchedInterface() {
                    @Override
                    public void onLotListFetchSuccess(List<Lot> lot_list_data) {
                        if(lotListAdapter==null){
                            lotListAdapter = new LotListAdapter(lot_list_data, getApplication(), new AreaItemClicked() {
                                @Override
                                public void onClick(String city_id, String area_id) {
                                    showDateTimePicker();
                                }
                            });
                            lot_list.setAdapter(lotListAdapter);
                            search_item.setVisible(true);
                        }else{
                            lotListAdapter.refreshLotList(lot_list_data);
                        }

                        showLotList();
                    }

                    @Override
                    public void onLotListFetchFailure() {
                        showNoConnectionCard();
                    }
                }
                , new OnSearchResultFetchInterface() {
                    @Override
                    public void onSearchResultFetchSuccess(List<Lot> lot_list_search_result) {
                        if(lotListAdapter==null){
                            lotListAdapter = new LotListAdapter(lot_list_search_result, getApplication(), new AreaItemClicked() {
                                @Override
                                public void onClick(String city_id, String area_id) {
                                    showDateTimePicker();
                                }
                            });
                            lot_list.setAdapter(lotListAdapter);
                            search_item.setVisible(true);
                        }else{
                            lotListAdapter.refreshLotList(lot_list_search_result);
                        }

                        showLotList();

                    }

                    @Override
                    public void onSearchResultFetchFailure() {
                        showNoConnectionCard();
                    }
                }
                , new OnCityFetchInterface() {
                        @Override
                        public void onCityFetchSuccess(List<City> city_list) {
                            final List<City> dummy =city_list;
                            city_list_data = city_list;
                            new Runnable(){

                                @Override
                                public void run() {
                                    List<String> idList = new ArrayList<>();
                                    for (City city :
                                            dummy) {
                                        idList.add(city.getName());
                                    }
                                    city_list_string_data = idList;
                                }
                            }.run();

                        }

                        @Override
                        public void onCityFetchFailure() {
                            showNoConnectionCard();
                        }
                });

        this.dashBoardNetworkUtility.getLotList("5f49fe8533adbd254c574e88","SEND_ALL");
        this.dashBoardNetworkUtility.getCityList();
        this.dashBoardNetworkUtility.getAreaList("5f49fe8533adbd254c574e88");
    }

    private void setListeners(){
        city_selector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(city_list_data!=null){
                    Log.i("MAIN",">>>>>>>>>>>>>>>>>>>>>>>>>>>>>area_list_clicked");
                    Intent i = new Intent(getApplicationContext(), AreaSelectorActivity.class);
                    i.putExtra("list", (Serializable) city_list_string_data);
                    i.putExtra("code",0);
                    startActivityForResult(i, 0);
                }
            }
        });
        area_selector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(area_list_data!=null){
                    Log.i("MAIN",">>>>>>>>>>>>>>>>>>>>>>>>>>>>>area_list_clicked");
                    Intent i = new Intent(getApplicationContext(), AreaSelectorActivity.class);
                    i.putExtra("list", (Serializable) area_list_string_data);
                    i.putExtra("code",1);
                    startActivityForResult(i, 1);
                }

            }
        });
        refresh_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refreshLists();
            }
        });

        search_input_field.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                callSearch(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        search_input_layout.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                collapseSearchView();
            }
        });

    }

    public void callSearch(CharSequence charSequence){
        Log.i("MAIN",">>>>>>>>>>>inside call search");
        String key="SEND_ALL";
        showProgressBar();
        if(charSequence.length()!=0) {
            key = charSequence.toString();
        }
        dashBoardNetworkUtility.getSearchLotList("5f49fe8533adbd254c574e88",key);
    }

    public void collapseSearchView(){
        search_view.collapse();
        search_item.setVisible(true);
        callSearch("");
    }

    public void expandSearchView(){
        Log.i("MAIN",">>>>>>>>>>>inside search view expand");
        search_view.expand();
        search_item.setVisible(false);
    }

    public void showLotList(){
        no_connection_view.setVisibility(View.INVISIBLE);
        progress_bar_view.setVisibility(View.INVISIBLE);
        lot_list_view.setVisibility(View.VISIBLE);

    }

    public void showNoConnectionCard(){
        lot_list_view.setVisibility(View.INVISIBLE);
        no_connection_view.setVisibility(View.VISIBLE);
        progress_bar_view.setVisibility(View.INVISIBLE);
    }

    public void showProgressBar(){
        lot_list_view.setVisibility(View.INVISIBLE);
        no_connection_view.setVisibility(View.INVISIBLE);
        progress_bar_view.setVisibility(View.VISIBLE);
    }

    public void refreshLists(){
        showProgressBar();
        Log.i("MAIN",">>>>>>>>>>>>>>>>>>>>>>refreshing");
        dashBoardNetworkUtility.getLotList(city_id,area_id);
    }

    public void showDateTimePicker() {
        final Context context = this;
        final Calendar currentDate = Calendar.getInstance();
        date = Calendar.getInstance();
        final TimePickerDialog timePickerDialog = new TimePickerDialog(context,new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                date.set(Calendar.HOUR_OF_DAY, hourOfDay);
                date.set(Calendar.MINUTE, minute);
                Log.v("TAG", "The choosen one " + date.getTime());
            }
        }, currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(Calendar.MINUTE), false);
        timePickerDialog.show();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK) {
                Integer index = data.getIntExtra("index",0);
                if(index==0)area_id = "SEND_ALL";
                else area_id = area_list_data.get(index-1).getArea_id();
                showProgressBar();
                dashBoardNetworkUtility.getLotList(city_id,area_id);
            }
        }else if(requestCode==0){
            if(resultCode==RESULT_OK){
                Integer index = data.getIntExtra("index",0);
                city_id = city_list_data.get(index).getCity_id();
                showProgressBar();
                dashBoardNetworkUtility.getAreaList(city_id);
                dashBoardNetworkUtility.getLotList(city_id,"SEND_ALL");
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity_menu, menu);
        search_item = menu.findItem(R.id.search_button);
        search_item.setVisible(false);
        history_item = menu.findItem(R.id.history_button);
        history_item.setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
            {
                startActivity(new Intent(MainActivity.this,UserActivity.class));
//                mBottomSheetDialog.show();
//                Toast.makeText(this,"Profile",Toast.LENGTH_LONG).show();
                return true;
            }
            case R.id.search_button:
            {
                Log.i("MAIN",">>>>>>>>>>>search_button_clicked");
                expandSearchView();
                return true;
            }


            case R.id.history_button:
            {
                startActivity(new Intent(MainActivity.this, TransactionsActivity.class));
                return true;
            }

            default:
                return super.onOptionsItemSelected(item);

        }
    }
    @Override
    public void onBackPressed() {

        if(search_view.isExpanded()){
            collapseSearchView();
        }else{
            super.onBackPressed();
        }

    }


}