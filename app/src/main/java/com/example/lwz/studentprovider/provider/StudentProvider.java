package com.example.lwz.studentprovider.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import com.example.lwz.studentprovider.constant.DBConstant;
import com.example.lwz.studentprovider.db.DBHelper;

/**
 * Created by lwz on 2015/4/27.
 */
public class StudentProvider extends ContentProvider {
    public final static String AUTHORITY = "com.example.lwz.studentprovider.provider.StudentProvider";
    public final static Uri CONTENTURI = Uri.parse("content://" + AUTHORITY + "/" + DBConstant.TABLE_NAME);
    private static UriMatcher uriMatcher = null;
    private static DBHelper dbHelper = null;
    private final static int STUDENT = 1, STUDENTS = 2;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, DBConstant.TABLE_NAME, STUDENTS);
        uriMatcher.addURI(AUTHORITY, DBConstant.TABLE_NAME + "/#", STUDENT);
    }

    public StudentProvider() {
        super();
    }

    /**
     * Implement this to initialize your content provider on startup.
     * This method is called for all registered content providers on the
     * application main thread at application launch time.  It must not perform
     * lengthy operations, or application startup will be delayed.
     * <p/>
     * <p>You should defer nontrivial initialization (such as opening,
     * upgrading, and scanning databases) until the content provider is used
     * (via {@link #query}, {@link #insert}, etc).  Deferred initialization
     * keeps application startup fast, avoids unnecessary work if the provider
     * turns out not to be needed, and stops database errors (such as a full
     * disk) from halting application launch.
     * <p/>
     * <p>If you use SQLite, {@link SQLiteOpenHelper}
     * is a helpful utility class that makes it easy to manage databases,
     * and will automatically defer opening until first use.  If you do use
     * SQLiteOpenHelper, make sure to avoid calling
     * {@link SQLiteOpenHelper#getReadableDatabase} or
     * {@link SQLiteOpenHelper#getWritableDatabase}
     * from this method.  (Instead, override
     * {@link SQLiteOpenHelper#onOpen} to initialize the
     * database when it is first opened.)
     *
     * @return true if the provider was successfully loaded, false otherwise
     */
    @Override
    public boolean onCreate() {
        dbHelper = new DBHelper(getContext());
        return true;
    }

    /**
     * Implement this to handle query requests from clients.
     * This method can be called from multiple threads, as described in
     * <a href="{@docRoot}guide/topics/fundamentals/processes-and-threads.html#Threads">Processes
     * and Threads</a>.
     * <p/>
     * Example client call:<p>
     * <pre>// Request a specific record.
     * Cursor managedCursor = managedQuery(
     * ContentUris.withAppendedId(Contacts.People.CONTENT_URI, 2),
     * projection,    // Which columns to return.
     * null,          // WHERE clause.
     * null,          // WHERE clause value substitution
     * People.NAME + " ASC");   // Sort order.</pre>
     * Example implementation:<p>
     * <pre>// SQLiteQueryBuilder is a helper class that creates the
     * // proper SQL syntax for us.
     * SQLiteQueryBuilder qBuilder = new SQLiteQueryBuilder();
     * <p/>
     * // Set the table we're querying.
     * qBuilder.setTables(DATABASE_TABLE_NAME);
     * <p/>
     * // If the query ends in a specific record number, we're
     * // being asked for a specific record, so set the
     * // WHERE clause in our query.
     * if((URI_MATCHER.match(uri)) == SPECIFIC_MESSAGE){
     * qBuilder.appendWhere("_id=" + uri.getPathLeafId());
     * }
     * <p/>
     * // Make the query.
     * Cursor c = qBuilder.query(mDb,
     * projection,
     * selection,
     * selectionArgs,
     * groupBy,
     * having,
     * sortOrder);
     * c.setNotificationUri(getContext().getContentResolver(), uri);
     * return c;</pre>
     *
     * @param uri           The URI to query. This will be the full URI sent by the client;
     *                      if the client is requesting a specific record, the URI will end in a record number
     *                      that the implementation should parse and add to a WHERE or HAVING clause, specifying
     *                      that _id value.
     * @param projection    The list of columns to put into the cursor. If
     *                      {@code null} all columns are included.
     * @param selection     A selection criteria to apply when filtering rows.
     *                      If {@code null} then all rows are included.
     * @param selectionArgs You may include ?s in selection, which will be replaced by
     *                      the values from selectionArgs, in order that they appear in the selection.
     *                      The values will be bound as Strings.
     * @param sortOrder     How the rows in the cursor should be sorted.
     *                      If {@code null} then the provider is free to define the sort order.
     * @return a Cursor or {@code null}.
     */
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor cursor = null;
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        switch (uriMatcher.match(uri)) {
            case STUDENT:
                long id = ContentUris.parseId(uri);
                String where_args = " id = " + id;
                if(selection!=null && !selection.equals("")) {
                    where_args += " and " + selection;
                }
                cursor = database.query(DBConstant.TABLE_NAME, projection, where_args, selectionArgs, null, null, sortOrder);
                break;
            case STUDENTS:
                cursor = database.query(DBConstant.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Unknown uri:" + uri.toString());
        }
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case STUDENT:
                return "vnd.android.cursor.item/vnd.com.example.lwz.studentprovider.provider.StudentProvider." + DBConstant.TABLE_NAME;
            case STUDENTS:
                return "vnd.android.cursor.item/vnd.com.example.lwz.studentprovider.provider.StudentProvider." + DBConstant.TABLE_NAME;
            default:
                throw new IllegalArgumentException("Unknown uri:" + uri.toString());
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Uri resultUri = null;
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        if(uriMatcher.match(uri) != STUDENTS)
            throw new IllegalArgumentException("Unknown uri:"+uri);
        long id = database.insert(DBConstant.TABLE_NAME, null, values);
        if(id > 0)
            resultUri = ContentUris.withAppendedId(CONTENTURI, id);
        return resultUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int result = 0;
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        switch (uriMatcher.match(uri)) {
            case STUDENT:
                long id = ContentUris.parseId(uri);
                String where_args = " id = " + id;
                if(selection!=null && !selection.equals("")) {
                    where_args += " and " + selection;
                }
                result = database.delete(DBConstant.TABLE_NAME, where_args, selectionArgs);
                break;
            case STUDENTS:
                result = database.delete(DBConstant.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown uri:" + uri.toString());
        }
        return result;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int result = 0;
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        switch (uriMatcher.match(uri)) {
            case STUDENT:
                long id = ContentUris.parseId(uri);
                String where_args = " id = " + id;
                if(selection!=null && !selection.equals("")) {
                    where_args += " and " + selection;
                }
                result = database.update(DBConstant.TABLE_NAME, values, where_args, selectionArgs);
                break;
            case STUDENTS:
                result = database.update(DBConstant.TABLE_NAME, values, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown uri:" + uri.toString());
        }
        return result;
    }
}
