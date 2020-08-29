package com.example.maste.todoapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<String> list;
    private ArrayAdapter<String> listAdapter;
    private ListView items;

    protected final static String ITEM_TXT = "itemText";
    protected final static String ITEM_POS =  "itemPos";
    private final int REQUEST_CODE = 50;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        readList();
        listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
        items = findViewById(R.id.itemList);
        items.setAdapter(listAdapter);

        setListViewListener();
    }

    public void onAddItem(View v) {
        EditText itemNew = findViewById(R.id.itemNew);
        String itemText = itemNew.getText().toString();
        listAdapter.add(itemText);
        itemNew.setText(null);
        writeList();
        Toast.makeText(getApplicationContext(), "Item added to list", Toast.LENGTH_SHORT).show();
    }

    private void setListViewListener() {
        Log.i("MainActivity", "Setup listener for ListView");
        items.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("MainActivity", "Item removed from list" + position);
                list.remove(position);
                listAdapter.notifyDataSetChanged();
                writeList();
                return true;
            }
        });

        items.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(MainActivity.this, ItemEditActivity.class);
                i.putExtra(ITEM_TXT, listAdapter.getItem(position));
                i.putExtra(ITEM_POS, position);
                startActivityForResult(i, REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE)
        {
            String update = data.getStringExtra(ITEM_TXT);
            int pos = data.getIntExtra(ITEM_POS, 0);
            list.set(pos, update);
            listAdapter.notifyDataSetChanged();
            writeList();
            Toast.makeText(this, "Item updated successfully.", Toast.LENGTH_SHORT).show();
        }
    }

    private File getDataFile() {
        return new File(getFilesDir(), "todo.txt");
    }

    private void readList() {
        try {
            list = new ArrayList<>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
        } catch (IOException e) {
            Log.e("MainActivity", "Error reading file");
            list = new ArrayList<>();
        }
    }

    private void writeList() {
        try {
            FileUtils.writeLines(getDataFile(), list);
        } catch (IOException e) {
            Log.e("MainActivity", "Error writing file");
        }
    }
}
