package edu.buffalo.cse.cse486586.groupmessenger1;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.database.sqlite.SQLiteDatabase;
import android.database.SQLException;
import android.database.sqlite.SQLiteQueryBuilder;



import android.content.ContentUris;
import android.content.Context;
import android.content.UriMatcher;
import android.text.TextUtils;

import edu.buffalo.cse.cse486586.groupmessenger1.dbTable;

/**
 * GroupMessengerProvider is a key-value table. Once again, please note that we do not implement
 * full support for SQL as a usual ContentProvider does. We re-purpose ContentProvider's interface
 * to use it as a key-value table.
 * 
 * Please read:
 * 
 * http://developer.android.com/guide/topics/providers/content-providers.html
 * http://developer.android.com/reference/android/content/ContentProvider.html
 * 
 * before you start to get yourself familiarized with ContentProvider.
 * 
 * There are two methods you need to implement---insert() and query(). Others are optional and
 * will not be tested.
 * 
 * @author stevko
 *
 */
public class GroupMessengerProvider extends ContentProvider {

    private DBHelper db;

    private static final String PROVIDER_NAME = "edu.buffalo.cse.cse486586.groupmessenger1.provider";
    private static final String BASE_PATH="FirstTable";
    static final String URL = "content://" + PROVIDER_NAME+"/"+BASE_PATH;
    static final Uri CONTENT_URI = Uri.parse(URL);


    private SQLiteDatabase database;
    private static final String DATABASE_NAME = "PA2";

        @Override
        public boolean onCreate()
        {
            db= new DBHelper(getContext());
            return true;

        }



    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // You do not need to implement this.
        return 0;
    }
    public String getType(Uri uri) {
        // You do not need to implement this.
        return null;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        // You do not need to implement this.
        return 0;}

    @Override
    public Uri insert(Uri uri, ContentValues values) {

        /* TODO: You need to implement this method. Note that values will have two columns (a key
         * column and a value column) and one row that contains the actual (key, value) pair to be
         * inserted.
         * 
         * For actual storage, you can use any option. If you know how to use SQL, then you can use
         * SQLite. But this is not a requirement. You can use other storage options, such as the
         * internal storage option that we used in PA1. If you want to use that option, please
         * take a look at the code for PA1.
         */

        SQLiteDatabase sqlDB = db.getWritableDatabase();
        String[] argu = {values.getAsString("key")};

        Cursor c1 = sqlDB.query(dbTable.TABLE_NAME,
                null,
                "key=?",
                argu,
                null,
                null,
                null);
        if(c1.getCount()<1)
        {
             sqlDB.insert(dbTable.TABLE_NAME, null, values);
        }

        else
        {
            sqlDB.update(dbTable.TABLE_NAME,values,"key=?",argu);
        }

        Uri _uri = ContentUris.withAppendedId(CONTENT_URI, c1.getCount());
        getContext().getContentResolver().notifyChange(_uri, null);
        return _uri;

    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        /*
         * TODO: You need to implement this method. Note that you need to return a Cursor object
         * with the right format. If the formatting is not correct, then it is not going to work.
         *
         * If you use SQLite, whatever is returned from SQLite is a Cursor object. However, you
         * still need to be careful because the formatting might still be incorrect.
         *
         * If you use a file storage option, then it is your job to build a Cursor * object. I
         * recommend building a MatrixCursor described at:
         * http://developer.android.com/reference/android/database/MatrixCursor.html
         */

        SQLiteDatabase data = db.getWritableDatabase();
        String[] args = {selection};
        Cursor c = data.query(dbTable.TABLE_NAME,
                null,
                "key=?",
                args,
                null,
                null,
                null);
         return c;
    }
}
