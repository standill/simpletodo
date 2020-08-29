package com.example.maste.todoapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import static com.example.maste.todoapp.MainActivity.ITEM_POS;
import static com.example.maste.todoapp.MainActivity.ITEM_TXT;

public class ItemEditActivity extends AppCompatActivity {

    private EditText input;
    private int pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_edit);

        input = findViewById(R.id.editText);
        input.setText(getIntent().getStringExtra(ITEM_TXT));
        pos = getIntent().getIntExtra(ITEM_POS, 0);
        getSupportActionBar().setTitle("Edit Item");
    }

    public void onSave(View v)
    {
        Intent i = new Intent();
        i.putExtra(ITEM_TXT, input.getText().toString());
        i.putExtra(ITEM_POS, pos);
        setResult(RESULT_OK, i);
        finish();
    }
}
