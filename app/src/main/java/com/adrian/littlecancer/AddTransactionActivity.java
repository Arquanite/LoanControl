package com.adrian.littlecancer;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

public class AddTransactionActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    private String name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_transaction);
        databaseHelper = new DatabaseHelper(this);
        name = getIntent().getStringExtra(MainListActivity.FRIENDS_NAME);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(getString(R.string.new_transaction));
        RadioButton someone = (RadioButton) findViewById(R.id.radioSomeone);
        someone.setText(name);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void addTransaction(View view) {
        EditText editText = (EditText) findViewById(R.id.amount);
        if(editText.getText().length() == 0){
            editText.setError(getString(R.string.fill_field_message));
            return;
        }
        float value = Float.valueOf(editText.getText().toString());

        RadioButton someone = (RadioButton) findViewById(R.id.radioSomeone);
        if(someone.isChecked()) value = -value;
        editText = (EditText) findViewById(R.id.note);
        String note = editText.getText().toString();

        if(note.isEmpty()){
            editText.setError(getString(R.string.add_note_message));
            return;
        }

        databaseHelper.addTransaction(name, value, note);
        onBackPressed();
    }
}
