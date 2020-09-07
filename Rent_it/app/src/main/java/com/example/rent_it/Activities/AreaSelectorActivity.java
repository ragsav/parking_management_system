package com.example.rent_it.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.rent_it.R;

import java.util.List;

public class AreaSelectorActivity extends AppCompatActivity {

    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.area_selector_activity);
        toolbar=findViewById(R.id.topAppBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        List<String> area_list = (List<String>) getIntent().getSerializableExtra("list");
        Integer code = getIntent().getIntExtra("code",3);

        ArrayAdapter adapter = new ArrayAdapter<String>(this,
                R.layout.area_selector_item, area_list);
        ListView listView = (ListView) findViewById(R.id.area_list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent();
                intent.putExtra("index", i);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        if(code==0)toolbar.setTitle("Select city");
        else if(code==1)toolbar.setTitle("Select area");
        else finish();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}