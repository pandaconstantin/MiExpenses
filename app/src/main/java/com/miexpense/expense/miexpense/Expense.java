package com.miexpense.expense.miexpense;

import java.util.Date;

/**
 * Created by constantin on 1/28/15.
 */
public class Expense {

    //Label table name
    public static final String TABLE  = "Depense";

    //Columns of the table
    public static final String TABLE_COLUMN_ID  = "id" ;
    public static final String TABLE_COLUMN_PRODUCT = "product" ;
    public static final String TABLE_COLUMN_PRICE  = "price";
    public static final String TABLE_COLUMN_DATE_DEPENSE = "datedepense" ;

    // Properties of the class Depense
    private int Depense_ID ;
    private String product ;
    private float price ;
    private Date datedepense;

    public int getDepense_ID() {
        return Depense_ID;
    }

    public void setDepense_ID(int depense_ID) {
        Depense_ID = depense_ID;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public Date getDatedepense() {
        return datedepense;
    }

    public void setDatedepense(Date datedepense) {
        this.datedepense = datedepense;
    }
}
