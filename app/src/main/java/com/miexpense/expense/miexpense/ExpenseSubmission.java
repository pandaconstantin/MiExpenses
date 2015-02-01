package com.miexpense.expense.miexpense;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.miexpense.expense.miexpense.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ExpenseSubmission extends ActionBarActivity implements View.OnClickListener {

    private EditText dateText ; //The textfield that will contain the Date
    private DatePickerDialog dateExpenseDialog; // Date picker
    private SimpleDateFormat dateformater;
    private ExpenseRepository myDatabase ;
    private ImageButton buttonSumit ;
    private ImageButton buttonDelete ;
    private ImageButton buttonliste ;
    private int anexpenseid  ;
    private EditText productName ;
    private EditText productPrice ;
    private EditText txtDescription ;
    EditText txtPrice ;
    EditText txtDateDepense ;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_submission);
        dateformater = new SimpleDateFormat("dd-MM-yyyy"); //create an object to format the date
        setDateExpense(); //Call here the method to set the date picker
        myDatabase = new ExpenseRepository(this.getApplicationContext());
        buttonSumit  = (ImageButton) findViewById(R.id.imageButton) ;
        buttonSumit.setOnClickListener(this); //Listen the click of the button

        buttonDelete  = (ImageButton) findViewById(R.id.imageDelete) ;
        buttonDelete.setOnClickListener(this);

        buttonliste  = (ImageButton) findViewById(R.id.imagelisteAll);
        buttonliste.setOnClickListener(this);

        anexpenseid =0;
        Intent intent = getIntent();
        anexpenseid =intent.getIntExtra("expenseID", 0);

        Expense  anexpense  = new Expense();
        anexpense = myDatabase.getExpenseById(anexpenseid);

        productName = (EditText) findViewById(R.id.txtProductDescription) ;
        productPrice = (EditText) findViewById(R.id.txtProductPrice);

        SimpleDateFormat formateur  = new SimpleDateFormat("dd-MM-yyyy", Locale.FRANCE);
        Date myDate  = new Date();
        dateText.setText(formateur.format(myDate));
        productName.setText(anexpense.getProduct());
        productPrice.setText(String.valueOf(anexpense.getPrice()));


        txtDescription  = (EditText) findViewById(R.id.txtProductDescription);
        txtPrice  = (EditText) findViewById(R.id.txtProductPrice);
        txtDateDepense  = (EditText) findViewById(R.id.DateDepense);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.expense_submission, menu);
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

    private void setDateExpense() {
        dateText  = (EditText) findViewById(R.id.DateDepense);
        dateText.setOnClickListener(this);

        Calendar myExpenseCalendar  = Calendar.getInstance();
        dateExpenseDialog =  new DatePickerDialog(this , new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePickerView, int year , int month, int day) {
                Calendar dateChoosen = Calendar.getInstance();
                dateChoosen.set(year,month,day);
                dateText.setText(dateformater.format(dateChoosen.getTime()));
            }
        }, myExpenseCalendar.get(Calendar.YEAR), myExpenseCalendar.get(Calendar.MONTH), myExpenseCalendar.get(Calendar.DAY_OF_MONTH));

    }

    @Override
    public void onClick(View view) {

        if(view == dateText) {
            dateExpenseDialog.show();
        }

        if (view == buttonSumit) {

            Date datedepense = new Date();
            SimpleDateFormat formater  = new SimpleDateFormat("dd-MM-yyyy");
            try {
                datedepense = formater.parse(txtDateDepense.getText().toString());
                Toast.makeText(ExpenseSubmission.this, txtDateDepense.getText().toString(), Toast.LENGTH_SHORT).show();
            } catch (ParseException e) {
                e.printStackTrace();
            }

            ContentValues  values  = new ContentValues();
            values.put(Expense.TABLE_COLUMN_PRODUCT, String.valueOf(txtDescription.getText()));
            values.put(Expense.TABLE_COLUMN_PRICE,Float.parseFloat(txtPrice.getText().toString()));
            values.put(Expense.TABLE_COLUMN_DATE_DEPENSE,formater.format(datedepense));

            getContentResolver().insert(ExpenseProvider.CONTENT_URI,values);

            Toast.makeText(ExpenseSubmission.this, "New expense inserted", Toast.LENGTH_SHORT).show();

            Intent intent  =  new Intent(getApplicationContext(),main.class) ;
            startActivity(intent);

        }


        if(view == buttonDelete) {
            getContentResolver().delete(ExpenseProvider.CONTENT_URI,"id=?" , new String[]{String.valueOf(anexpenseid )});
            Toast.makeText(ExpenseSubmission.this, "Delete item", Toast.LENGTH_SHORT).show();
            Intent intent  =  new Intent(getApplicationContext(),main.class) ;
            startActivity(intent);
        }


        if(view == buttonliste) {

            Date datedepense = new Date();
            SimpleDateFormat formater  = new SimpleDateFormat("dd-MM-yyyy");
            try {
                datedepense = formater.parse(txtDateDepense.getText().toString());
                Toast.makeText(ExpenseSubmission.this, txtDateDepense.getText().toString(), Toast.LENGTH_SHORT).show();
            } catch (ParseException e) {
                e.printStackTrace();
            }

            ContentValues values = new ContentValues();
            values.put(Expense.TABLE_COLUMN_ID, anexpenseid);
            values.put(Expense.TABLE_COLUMN_PRODUCT, String.valueOf(txtDescription.getText()));
            values.put(Expense.TABLE_COLUMN_PRICE,Float.parseFloat(txtPrice.getText().toString()));
            values.put(Expense.TABLE_COLUMN_DATE_DEPENSE,formater.format(datedepense));

            getContentResolver().update(ExpenseProvider.CONTENT_URI,values,"id=?",new String[]{String.valueOf(anexpenseid )});

            Toast.makeText(ExpenseSubmission.this, "Expense updated", Toast.LENGTH_SHORT).show();

            Intent intent  =  new Intent(getApplicationContext(),main.class) ;
            startActivity(intent);
        }
    }
}
