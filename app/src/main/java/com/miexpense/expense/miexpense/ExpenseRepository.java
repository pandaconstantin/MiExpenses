package com.miexpense.expense.miexpense;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.format.DateFormat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Created by constantin on 1/28/15.
 */
public class ExpenseRepository {

    private ExpensesHelper myDBHelper ;


    public ExpenseRepository(Context myContext) {
        myDBHelper = new ExpensesHelper(myContext);

    }

    //Here I insert data into Depense table

    /**
     *
     * @param expense
     * @return
     */

    public int insertNewExpense(Expense expense) {
        //Open connection to write data
        SQLiteDatabase expensedatabase  = myDBHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        SimpleDateFormat dateformat  = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss",Locale.FRANCE) ;

        values.put(expense.TABLE_COLUMN_PRODUCT,expense.getProduct());
        values.put(expense.TABLE_COLUMN_PRICE, expense.getPrice());
        values.put(expense.TABLE_COLUMN_DATE_DEPENSE,dateformat.format(expense.getDatedepense()));

        //Insert here an expense
        long expense_insert_id  =  expensedatabase.insert(expense.TABLE, null, values);
        expensedatabase.close();
        return (int) expense_insert_id ;
    }


    //Update an expense

    public  void updateExpense(Expense expense) {

        SQLiteDatabase db  =  myDBHelper.getWritableDatabase();
        ContentValues values  = new ContentValues();
        SimpleDateFormat dateformat  = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss",Locale.FRANCE) ;

        values.put(expense.TABLE_COLUMN_PRODUCT, expense.getProduct());
        values.put(expense.TABLE_COLUMN_PRICE, expense.getPrice());
        values.put(expense.TABLE_COLUMN_DATE_DEPENSE, dateformat.format(expense.getDatedepense()));

        db.update(expense.TABLE, values, expense.getDepense_ID() + "=2", new String[]{String.valueOf(expense.getDepense_ID())});
        db.close();

    }


/*


 */
    public ArrayList<HashMap<String,String>> getAllExpenseList() {

        SQLiteDatabase db = myDBHelper.getReadableDatabase() ;

        String selectAllQuery  = "SELECT * FROM  " + Expense.TABLE  + " order by " + Expense.TABLE_COLUMN_DATE_DEPENSE;

        ArrayList<HashMap<String, String>> expenseList  = new ArrayList<HashMap<String, String>>();

        Cursor cursor  = db.rawQuery(selectAllQuery, null);

        if (cursor.moveToFirst()) {
            do {

                HashMap<String,String> expense  =  new HashMap<String, String>();
                expense.put("id",String.valueOf(cursor.getInt(0)));
                expense.put("product", cursor.getString(1));
                expense.put("price", String.valueOf(cursor.getFloat(2)));

                String mydate  =  cursor.getString(cursor.getColumnIndexOrThrow("datedepense"));
                SimpleDateFormat formateur  = new SimpleDateFormat("dd-MM-yyyy", Locale.FRANCE);
                Date myDate  = new Date();
                try {
                    myDate  =  formateur.parse(mydate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                expense.put("datedepense", formateur.format(myDate));
                expenseList.add(expense);

            } while (cursor.moveToNext()) ;
        }
        cursor.close();
        db.close();
        return expenseList  ;
    }



    public List<Expense> getAllExpenses() {

         SQLiteDatabase db   = myDBHelper.getReadableDatabase();

         List<Expense> expenses  = new ArrayList<Expense>();

         String selectAllQuery  = "SELECT * FROM  " + Expense.TABLE  + " order by " + Expense.TABLE_COLUMN_DATE_DEPENSE;

         Cursor cursor  = db.rawQuery(selectAllQuery, null);

         cursor.moveToFirst();

         while(!cursor.isAfterLast()) {
             Expense expense  = new Expense() ;
             expense.setDepense_ID(cursor.getInt(0));
             expense.setProduct(cursor.getString(1));
             expense.setPrice(cursor.getFloat(2));
             SimpleDateFormat dateformat  = new SimpleDateFormat("dd-MM-yyyy",Locale.FRANCE);
             try {
                 expense.setDatedepense(dateformat.parse(String.valueOf(cursor.getColumnIndex("datedepense"))));
             } catch (ParseException e) {
                 e.printStackTrace();
             }

             expenses.add(expense);
             cursor.moveToNext();
         }

         cursor.close();
         return expenses ;
     }

    /**
     *
     * @param id
     * @return
     */
    public Expense getExpenseById(int id) {

        SQLiteDatabase db   = myDBHelper.getReadableDatabase() ;
        String SQLSelectQuery  = "SELECT id , product , price , datedepense FROM Depense where id = ? "  ;

        int indexCount  = 0 ;
        Expense depense  = new Expense();

        Cursor cursor  = db.rawQuery(SQLSelectQuery, new String[] {String.valueOf(id)}) ;

        if(cursor.moveToFirst()) {
            do {
                depense.setDepense_ID(cursor.getInt(cursor.getColumnIndex("id")));
                depense.setProduct(cursor.getString(cursor.getColumnIndex("product")));
                depense.setPrice(cursor.getFloat(cursor.getColumnIndex("price")));
                SimpleDateFormat dateformat  = new SimpleDateFormat("dd-MM-yyyy") ;
                try {
                    depense.setDatedepense(dateformat.parse(cursor.getString(cursor.getColumnIndex("datedepense"))));
                } catch (ParseException e) {
                    e.printStackTrace();
                }


            }while(cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return  depense;
    }



    public void deleteExpense(int expenseid) {
        SQLiteDatabase db   = myDBHelper.getReadableDatabase() ;
        db.delete(Expense.TABLE, Expense.TABLE_COLUMN_ID + " = ?" , new String[]{String.valueOf(expenseid)});
        db.close();
    }




}
