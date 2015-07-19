package com.example.lwz.studentprovider;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.net.Uri;
import android.test.AndroidTestCase;

import com.example.lwz.studentprovider.constant.DBConstant;
import com.example.lwz.studentprovider.provider.StudentProvider;

/**
 * Created by lwz on 7/19/15.
 */
public class MyTest extends AndroidTestCase {
    public MyTest() {
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
    }

    public void testAdd() throws Exception {
        ContentResolver resolver = getContext().getContentResolver();
        ContentValues values = new ContentValues();
        values.put(DBConstant.COLUMN_NAME, "wen");
        values.put(DBConstant.COLUMN_ADDRESS, "nanjing");
        values.put(DBConstant.COLUMN_SEX, "nan");
        resolver.insert(StudentProvider.CONTENTURI, values);
    }
}
