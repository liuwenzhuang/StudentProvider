package com.example.lwz.studentprovider;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewConfiguration;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.lwz.studentprovider.constant.DBConstant;
import com.example.lwz.studentprovider.provider.StudentProvider;

import java.lang.reflect.Field;


public class AddActivity extends ActionBarActivity {
    private EditText editName = null, editAddress = null;
    private RadioButton radioMale = null, radioFemale = null;
    private final static String TAG = "AddActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        editName = (EditText) findViewById(R.id.editName);
        editAddress = (EditText) findViewById(R.id.editAddress);
        radioMale = (RadioButton) findViewById(R.id.radioMale);
        radioFemale = (RadioButton) findViewById(R.id.radioFemale);
        //getOverflowMenu();
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add_ok) {
            String name = editName.getText().toString().trim();
            String address = editAddress.getText().toString().trim();
            String sex = null;
            if(radioMale.isChecked())
                sex = "男";
            else if(radioFemale.isChecked())
                sex = "女";
            if(name.equals("") || address.equals("")) {
                Toast.makeText(this, "姓名和地址为必填项", Toast.LENGTH_LONG).show();
                return false;
            }
            ContentResolver resolver = getContentResolver();
            ContentValues values = new ContentValues();
            values.put(DBConstant.COLUMN_NAME, name);
            values.put(DBConstant.COLUMN_ADDRESS, address);
            values.put(DBConstant.COLUMN_SEX, sex);
            Uri resultUri = resolver.insert(StudentProvider.CONTENTURI, values);
            if(resultUri != null) {
                Log.i(TAG, "--insert sucess-->" + resultUri.toString());
                Intent intent = new Intent();
                intent.putExtra("resultUri", resultUri.toString());
                setResult(RESULT_OK, intent);
                finish();
            } else {
                Toast.makeText(this, "插入失败,请稍后重试", Toast.LENGTH_LONG).show();
                return false;
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
