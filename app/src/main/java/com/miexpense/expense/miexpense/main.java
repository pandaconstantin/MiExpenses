package com.miexpense.expense.miexpense;

import android.annotation.TargetApi;
import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;


@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class main extends ListActivity implements View.OnClickListener, LoaderManager.LoaderCallbacks<Cursor> {

    ImageButton buttonNewExpense , buttonListExpenses;
    TextView expenseID , expensesDate ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buttonNewExpense  = (ImageButton) findViewById(R.id.buttonAdd);
        buttonNewExpense.setOnClickListener(this);
        buttonListExpenses  = (ImageButton) findViewById(R.id.buttoncloseExpenses);
        buttonListExpenses.setOnClickListener(this);

        //Initialize here the background query
        getLoaderManager().initLoader(0,null, this) ;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onClick(View view) {
        //Test the button clicked and start the activity concerned
        if(view == findViewById(R.id.buttonAdd)) {
            Intent intentExpenseSubmission  = new Intent(this, ExpenseSubmission.class);
            intentExpenseSubmission.putExtra("expenseID", 0);
            startActivity(intentExpenseSubmission);
        } else if( view == findViewById(R.id.buttoncloseExpenses)){
            //Close the application
            finish();
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,ExpenseProvider.CONTENT_URI,null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {

        ArrayList<HashMap<String,String>> myExpenseListe  =  new ArrayList<HashMap<String, String>>() ;

        if(cursor.moveToFirst()) {

            do {

                HashMap<String,String> map  =  new HashMap<String, String>() ;
                map.put(Expense.TABLE_COLUMN_ID, String.valueOf(cursor.getInt(0)));
                map.put(Expense.TABLE_COLUMN_PRODUCT, cursor.getString(1)) ;
                map.put(Expense.TABLE_COLUMN_PRICE, String.valueOf(cursor.getFloat(2)));
                String mydate  =  cursor.getString(cursor.getColumnIndexOrThrow(Expense.TABLE_COLUMN_DATE_DEPENSE));
                SimpleDateFormat formateur  = new SimpleDateFormat("dd-MM-yyyy", Locale.FRANCE);
                Date myDate  = new Date();

                try {
                    myDate  =  formateur.parse(mydate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                map.put(Expense.TABLE_COLUMN_DATE_DEPENSE, formateur.format(myDate));
                myExpenseListe.add(map);

            } while(cursor.moveToNext()) ;
        }

        ListView expenseListView =  getListView(); //Here I have the listeview


        if( cursor.getCount() != 0) {

            expenseListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    expenseID  = (TextView) view.findViewById(R.id.expenseID);
                    Intent intent  =  new Intent(getApplicationContext(),ExpenseSubmission.class) ;
                    String ExpenseId = expenseID.getText().toString();
                    intent.putExtra("expenseID",Integer.parseInt(ExpenseId));
                    startActivity(intent);
                }
            });

            ListAdapter adapter = new SimpleAdapter(this,myExpenseListe,R.layout.activity_expense_detail,new String[]{"id", "product","price","datedepense"}, new int[]{R.id.expenseID,R.id.expenseProduct,R.id.expensePrice,R.id.expenseDate});
            setListAdapter(adapter);

        } else{
            Toast.makeText(this, "No expense!", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {


    }

    protected void onDestroy() {
        super.onDestroy();
    }
}
