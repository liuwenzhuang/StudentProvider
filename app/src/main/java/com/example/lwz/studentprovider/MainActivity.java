package com.example.lwz.studentprovider;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ListView;

import com.example.lwz.studentprovider.adapter.StudentAdapter;
import com.example.lwz.studentprovider.constant.DBConstant;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private Loader loader = null;
    private LoaderManager loaderManager = null;
    private EditText editText = null;
    private ListView listView = null;
    private static String where_args = null;
    private final static String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = (EditText) findViewById(R.id.editText);
        listView = (ListView) findViewById(R.id.listView);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.i(TAG, "onTextChanged():" + s);
                where_args = s.toString();
                loaderManager.restartLoader(1000, null, MainActivity.this);
            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.i(TAG, "afterTextChanged():" + s);
            }
        });
        loaderManager = getSupportLoaderManager();
        loaderManager.initLoader(1000, null, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.i(TAG, "onCreateLoader");
        Uri uri = Uri.parse("content://com.example.lwz.studentprovider.provider.StudentProvider/student");
        String selection = null;
        if(where_args != null && !where_args.equals("")) {
            selection = " name=" + "'" + where_args + "'";
            Log.i(TAG, selection);
        }
        CursorLoader loader = new CursorLoader(MainActivity.this, uri, null, selection, null, null);
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.i(TAG, "onLoadFinished");
        List<String> list = new ArrayList<>();
        while(data!=null&&data.moveToNext()) {
            list.add(data.getString(data.getColumnIndex(DBConstant.COLUMN_NAME)));
        }
        StudentAdapter studentAdapter = new StudentAdapter(MainActivity.this);
        studentAdapter.setList(list);
        listView.setAdapter(studentAdapter);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
