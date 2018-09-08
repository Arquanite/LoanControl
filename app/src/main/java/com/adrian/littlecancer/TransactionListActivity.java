package com.adrian.littlecancer;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class TransactionListActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_list);

        //listView = (Recycl) findViewById(R.id.recyclerView);
        databaseHelper = new DatabaseHelper(this);

        name = getIntent().getStringExtra(MainListActivity.FRIENDS_NAME);

        setTitle(name);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        TransactionAdapter recyclerAdapter = new TransactionAdapter(databaseHelper.getTransactionsByName(name));
        recyclerView.setAdapter(recyclerAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(),
                LinearLayoutManager.VERTICAL));
    }

    @Override
    public void onResume(){
        super.onResume();
//        populateListView();
        updateBalance();
    }

    private void updateBalance(){
        TextView balance = (TextView) findViewById(R.id.total);
        balance.setText(String.valueOf(databaseHelper.getBalance(name)));
    }

//    private void populateListView() {
//        Cursor data = databaseHelper.getTransactionsByName(name);
//        ArrayList<Float> listData = new ArrayList<>();
//        while (data.moveToNext()){
//            listData.add((float) data.getDouble(0));
//        }
//
//        ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listData);
//        listView.setAdapter(adapter);
//    }

    public void addTransaction(View view) {
        Intent intent = new Intent(getApplicationContext(), AddTransactionActivity.class);
        intent.putExtra(MainListActivity.FRIENDS_NAME, name);
        startActivity(intent);
    }


}
