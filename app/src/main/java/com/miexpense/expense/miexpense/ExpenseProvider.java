package com.miexpense.expense.miexpense;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import android.database.SQLException;
import android.text.TextUtils;

import java.util.HashMap;

/**
 * Created by constantin on 1/30/15.
 *
 */
public class ExpenseProvider extends ContentProvider {

    private static final String AUTHORITY  = "com.miexpense.expense.miexpense.ExpenseProvider";
    public static final Uri CONTENT_URI =  Uri.parse("content://" + AUTHORITY + "/Depense") ;
    private static HashMap<String,String> EXPENSES_TABLE_MAP ;
    private  SQLiteDatabase db ;

    static final int URI_EXPENSES = 1 ;
    static final int URI_EXPENSE_ID = 2 ;

    static final UriMatcher uriMatcher;

    static {
        uriMatcher  = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, "Depense", URI_EXPENSES);
        uriMatcher.addURI(AUTHORITY,"Depense/#",URI_EXPENSE_ID);
    }


    @Override
    public boolean onCreate() {
        Context context  =  getContext();
        ExpensesHelper myDBHelper  = new ExpensesHelper(context);
        db = myDBHelper.getWritableDatabase();
        return (db==null) ? false:true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionargs, String sortOrder) {

        SQLiteQueryBuilder qb  = new SQLiteQueryBuilder();
        qb.setTables(Expense.TABLE);

        switch (uriMatcher.match(uri)) {
            case URI_EXPENSES :
                qb.setProjectionMap(EXPENSES_TABLE_MAP);
                break;
            case URI_EXPENSE_ID:
                qb.appendWhere( Expense.TABLE_COLUMN_ID + "=" +  uri.getPathSegments().get(1));
                break;
            default:
                throw new IllegalArgumentException("unknown URI" + uri);
        }

       if(sortOrder == null || sortOrder == "") {
           sortOrder  = Expense.TABLE_COLUMN_DATE_DEPENSE ;
       }

        Cursor c  = qb.query(db, projection, selection, selectionargs, null, null, sortOrder) ;
        c.setNotificationUri(getContext().getContentResolver(), uri);

        return c ;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        //Add a new expense record
        long rowID  = db.insert(Expense.TABLE, "",contentValues);
        if(rowID > 0) {
            Uri _uri  = ContentUris.withAppendedId(CONTENT_URI, rowID);
            getContext().getContentResolver().notifyChange(_uri, null);
            return _uri ;
        }
        throw new SQLException("Impossible to add a record into " + uri);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionargs) {

        int count = 0 ;

        switch(uriMatcher.match(uri)) {
            case URI_EXPENSES :
                count = db.delete(Expense.TABLE, selection, selectionargs);
                break;
            case URI_EXPENSE_ID :
                String id  = uri.getPathSegments().get(1);
                count = db.delete(Expense.TABLE, Expense.TABLE_COLUMN_ID + " = " + id + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')': ""),selectionargs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI" + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return count;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionargs) {

        int count  =  0 ;

        switch (uriMatcher.match(uri)) {

            case URI_EXPENSES :
                count = db.update(Expense.TABLE,contentValues,  selection, selectionargs);
                break;
            case URI_EXPENSE_ID :
                String id  = uri.getPathSegments().get(1);
                count = db.update(Expense.TABLE,contentValues, Expense.TABLE_COLUMN_ID + " = " + id + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')': ""),selectionargs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI" + uri);
        }

        return count;
    }
}
