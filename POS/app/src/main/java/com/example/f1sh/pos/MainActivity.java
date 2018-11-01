package com.example.f1sh.pos;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.View;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    String nama1, harga1;
    byte[] bytes;
    ListView listView;
    protected Cursor cursor;
    DbHelper mDbHelper;
    ArrayList<ListContent> contents = new ArrayList<ListContent>();

    public static MainActivity ma;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ma = this;
        mDbHelper = new DbHelper(this);
        listView = (ListView) findViewById(R.id.listView1);

        registerForContextMenu(listView);

        RefreshList();


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intCreate = new Intent(MainActivity.this, NewProduct.class);
                startActivity(intCreate);
            }
        });

    }


    public void RefreshList() {

        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        cursor = db.rawQuery("SELECT * FROM entry2", null);
        cursor.moveToFirst();

        TextView textView = findViewById(R.id.no_data);
        TextView textView1 = findViewById(R.id.text_help);

        if(cursor.moveToFirst()&& cursor.getString(0) != null){
            textView.setVisibility(View.GONE);
            textView1.setVisibility(View.GONE);
        }else {
            textView.setVisibility(View.VISIBLE);
            textView1.setVisibility(View.VISIBLE);
        }
        contents.clear();

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                String id = cursor.getString(0);
                bytes = cursor.getBlob(cursor.getColumnIndex("image"));
                nama1 = cursor.getString(cursor.getColumnIndex("name"));
                harga1 = cursor.getString(cursor.getColumnIndex("price"));
                contents.add(new ListContent(nama1, harga1, bytes, id));
            } while (cursor.moveToNext());
        }


        ListAdapter adapter = new ListAdapter(ma, contents);
        listView.setAdapter(adapter);
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
                .getMenuInfo();
        ListContent that = (ListContent)listView.getItemAtPosition(info.position);
        String id = that.getmId();
        switch (item.getItemId()) {
            case R.id.action_edit:
                Intent i = new Intent(getApplicationContext(), EditProduct.class);
                i.putExtra("_id",id );
                startActivity(i);
                return true;
            case R.id.action_delete:
                SQLiteDatabase db = mDbHelper.getWritableDatabase();
                db.execSQL("delete from entry2 where _ID = '" + id + "'");
                RefreshList();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
}
