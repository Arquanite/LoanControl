package com.adrian.littlecancer;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainListActivity extends AppCompatActivity {
    public static final String FRIENDS_NAME = "com.adrian.littlecancer.NAME";
    public static final int MENU_EDIT = 0;
    public static final int MENU_REMOVE = 1;
    //private DatabaseHelper databaseHelper;

    private FriendDAO dao = new FriendDAO(this);

    private ListView listView;
    private RecyclerView recyclerView;
    private FriendAdapter recyclerAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_list);


        //listView = (ListView) findViewById(R.id.listView);
        //databaseHelper = new DatabaseHelper(this);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerAdapter = new FriendAdapter(dao.getFriends(), this);
        recyclerView.setAdapter(recyclerAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(),
                LinearLayoutManager.VERTICAL));

//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                String s = adapterView.getItemAtPosition(i).toString();
//                Intent intent = new Intent(getApplicationContext(), TransactionListActivity.class);
//                intent.putExtra(FRIENDS_NAME, s);
//                startActivity(intent);
//            }
//        });
//
//        listView.setEmptyView(findViewById(R.id.empty_list_item));
//
//        populateListView();
    }

//    private void populateListView() {
//        Cursor data = dao.getNames();
//        ArrayList<String> listData = new ArrayList<>();
//        while (data.moveToNext()){
//            listData.add(data.getString(0));
//        }
//
//        ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listData);
//        listView.setAdapter(adapter);
//    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
//        Toast.makeText(this, String.valueOf(item.getOrder()))
        String friendName = dao.getFriends().get(item.getOrder()).getName();
        switch(item.getItemId()){
            case MENU_EDIT:
                showEditDialog(friendName);
                break;
            case MENU_REMOVE:
                showRemoveDialog(friendName);
                break;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void onResume(){
        super.onResume();
        recyclerAdapter.notifyDataSetChanged();
    }

    public void showRemoveDialog(final String friendName){
        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Remove " + friendName)
                .setMessage("This will permanently remove " + friendName + " from the list")
                .setPositiveButton(android.R.string.ok, null)
                .setNegativeButton(android.R.string.cancel, null)
                .create();
        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dao.deleteFriendByName(friendName);
                recyclerAdapter.update(dao.getFriends());
                dialog.dismiss();
            }
        });
    }

    public void showEditDialog(final String name){
        LayoutInflater li = LayoutInflater.from(this);
        final View enterNameView = li.inflate(R.layout.enter_name, null);
        EditText nameField = enterNameView.findViewById(R.id.name);
        nameField.setText(name);

        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(enterNameView)
                .setTitle(R.string.edit_friend)
                .setPositiveButton(android.R.string.ok, null)
                .setNegativeButton(android.R.string.cancel, null)
                .create();

        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()        {
            @Override
            public void onClick(View v)            {
                TextView textView = enterNameView.findViewById(R.id.name);
                if(textView.length() == 0) {
                    textView.setError(getString(R.string.name_empty_error));
                }
                else {
                    dao.updateFriendName(name, textView.getText().toString());
                    dialog.dismiss();
                    recyclerAdapter.update(dao.getFriends());
                }
            }
        });
    }

    public void showAddDialog(View v){
        LayoutInflater li = LayoutInflater.from(this);
        final View enterNameView = li.inflate(R.layout.enter_name, null);

        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(enterNameView)
                .setTitle(R.string.add_friend)
                .setPositiveButton(android.R.string.ok, null)
                .setNegativeButton(android.R.string.cancel, null)
                .create();

        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()        {
            @Override
            public void onClick(View v)            {
                TextView textView = enterNameView.findViewById(R.id.name);
                if(textView.length() == 0) {
                    textView.setError(getString(R.string.name_empty_error));
                }
                else {
                    dao.addName(textView.getText().toString());
                    dialog.dismiss();
                    recyclerAdapter.update(dao.getFriends());
                }
            }
        });
    }
}
