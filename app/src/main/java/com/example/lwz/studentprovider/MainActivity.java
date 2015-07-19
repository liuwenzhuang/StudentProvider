package com.example.lwz.studentprovider;

import android.content.ContentResolver;
import android.content.Intent;
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
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.lwz.studentprovider.adapter.StudentAdapter;
import com.example.lwz.studentprovider.constant.DBConstant;
import com.example.lwz.studentprovider.provider.StudentProvider;

import java.lang.reflect.Field;
import java.text.Format;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private Loader loader = null;
    private LoaderManager loaderManager = null;
    private EditText editText = null;
    private StudentAdapter studentAdapter = null;
    private ListView listView = null;
    private static String selected_name = null;
    private static String where_args = null;
    private final static String TAG = "MainActivity";
    private final static int REQUESTCODE = 1001;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //getOverflowMenu();
        editText = (EditText) findViewById(R.id.editText);
        listView = (ListView) findViewById(R.id.listView);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        listView.setMultiChoiceModeListener(new ListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                selected_name = (String)studentAdapter.getItem(position);
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                mode.getMenuInflater().inflate(R.menu.menu_main, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_add:
                        startActivityForResult(new Intent(MainActivity.this, AddActivity.class), REQUESTCODE);
                        break;
                    case R.id.action_delete:
                        ContentResolver resolver = MainActivity.this.getContentResolver();
                        String selection = " name = " + "'" + selected_name + "'";
                        int count = resolver.delete(StudentProvider.CONTENTURI, selection, null);
                        if(count > 0) {
                            loaderManager.restartLoader(1000, null, MainActivity.this);
                        } else {
                            Toast.makeText(MainActivity.this, "删除失败,请稍后重试", Toast.LENGTH_LONG).show();
                            return false;
                        }
                        break;
                    case R.id.action_refresh:
                        loaderManager.restartLoader(1000, null, MainActivity.this);
                        break;
                    case R.id.action_settings:
                        break;
                    default:
                        break;
                }
                return true;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {

            }
        });
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                where_args = s.toString();
                loaderManager.restartLoader(1000, null, MainActivity.this);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        loaderManager = getSupportLoaderManager();
        loaderManager.initLoader(1000, null, this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUESTCODE && resultCode == RESULT_OK && data != null) {
            loaderManager.restartLoader(1000, null, this);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }



    /**
     * 强制显示Overflow Menu
     */
    private void getOverflowMenu() {
        try {
            ViewConfiguration config = ViewConfiguration.get(this);
            Field menuKeyField = ViewConfiguration.class
                    .getDeclaredField("sHasPermanentMenuKey");
            if (menuKeyField != null) {
                menuKeyField.setAccessible(true);
                menuKeyField.setBoolean(config, false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        studentAdapter = new StudentAdapter(MainActivity.this);
        studentAdapter.setList(list);
        listView.setAdapter(studentAdapter);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
