package com.miexpense.expense.miexpense;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * Created by constantin on 1/28/15.
 */
public class ExpensesHelper extends SQLiteOpenHelper{

    //Version of the database
    private static final int DATABASE_VERSION = 1 ;
    //Database name
    private static final String DATABASE_NAME = "Expenses.sqlite";

    public ExpensesHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
       String  SQL_CREATE_TABLE  = "CREATE TABLE " + Expense.TABLE +  " ( " + Expense.TABLE_COLUMN_ID + "  INTEGER PRIMARY KEY AUTOINCREMENT ,  " + Expense.TABLE_COLUMN_PRODUCT  + "   TEXT  , "  + Expense.TABLE_COLUMN_PRICE + "   FLOAT , "   +  Expense.TABLE_COLUMN_DATE_DEPENSE  + " DATE ) ;" ;
       sqLiteDatabase.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {
        //Drop the older table and create a new one
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS"  + Expense.TABLE );
        onCreate(sqLiteDatabase);
    }
}
