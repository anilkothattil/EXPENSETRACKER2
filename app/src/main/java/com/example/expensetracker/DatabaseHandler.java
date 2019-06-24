package com.example.expensetracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by geomathe on 11/20/2017.
 */

public class DatabaseHandler extends SQLiteOpenHelper
{

    private static final int DATABASE_VERSION=1;
    private static final String DATABASE_NAME="BankDatabase.db";


    public DatabaseHandler(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        getReadableDatabase();
        Log.i("DB","Construct");
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {

        Log.i("DB","Create");
       // SQLiteDatabase db  = this.getWritableDatabase();
        db.execSQL("create table " + "bank" +" (ID INTEGER PRIMARY KEY AUTOINCREMENT,NAME TEXT)");
        db.execSQL("CREATE TABLE "+"sender" + "( `sid` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, `bid` INTEGER NOT NULL, `sname` TEXT NOT NULL )");
        db.execSQL("CREATE TABLE "+"user" + "( `uid` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, `bid` INTEGER NOT NULL, `acno` INTEGER NOT NULL , `balance` FLOAT NOT NULL)");
        db.execSQL("CREATE TABLE "+"expense" + "( `eid` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, `bid` INTEGER NOT NULL, `amount` FLOAT NOT NULL, `timestamp` TEXT NOT NULL ,`type` INTEGER NOT NULL)");

        insertbank(db,1, "Allahabad bank");
        insertbank(db,2, "SBI");
        this.insertsender(db,1, "BT-ALBANK");
        this.insertsender(db,1, "VK-ALBANK");
        this.insertsender(db,1, "BW-ALBANK");
        this.insertsender(db,2, "BZ-ATMSBI");
        this.insertsender(db,2, "BP-ATMSBI");
    }

    public int dbcheck(String number)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from sender where sname like '"+number+"'",null);
        if(res.getCount()>0){
            int num=0;
            while (res.moveToNext()) {
                num = Integer.parseInt(res.getString(1));
            }
            return num;
        }
        return 0;
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1)
    {
        Log.i("DB","Upgrade");
        db.execSQL("DROP TABLE IF EXISTS bank");
        onCreate(db);
    }

    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from bank",null);
        return res;
    }

    public Cursor getAllUsers() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from user",null);
        return res;
    }

    public boolean insertbank(SQLiteDatabase db, int id,String bank)
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put("ID",id);
        contentValues.put("NAME",bank);
        long result = db.insert("bank",null ,contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }

    public boolean insertsender(SQLiteDatabase db, int bid,String sname)
    {
        ContentValues contentValues = new ContentValues();
        //contentValues.put("ID",id);
        contentValues.put("bid", bid);
        contentValues.put("sname", sname);
        long result = db.insert("sender", null, contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }

    public boolean insertuser(int bid,String acno)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        //contentValues.put("ID",id);
        contentValues.put("bid", bid);
        contentValues.put("acno", acno);
        contentValues.put("balance", 0);
        long result = db.insert("user", null, contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }

    public boolean insertExpense(int bank, float amount, String time, int type)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        //contentValues.put("ID",id);
        contentValues.put("bid", bank);
        contentValues.put("amount", amount);
        contentValues.put("timestamp", time);
        contentValues.put("type", type);
        long result = db.insert("expense", null, contentValues);
        if(result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean updateBalance(int bank, float amount){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("balance", amount);
        int result = db.update("user",contentValues,"bid="+bank,null);
        if(result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Cursor getAllExpenses() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from expense",null);
        return res;
    }
    public Cursor getExpense(int bid) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from bank,user,expense where bank.id=user.bid and user.bid=expense.bid and bank.id="+bid +" and expense.type=1",null);
        return res;
    }
    public Cursor getWeeklyCredits(int bid) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from bank,user,expense where bank.id=user.bid and user.bid=expense.bid and bank.id="+bid+" and expense.type=2 ORDER BY timestamp",null);
        return res;
    }
    public Cursor getWeeklyExpense(int bid) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from bank,user,expense where bank.id=user.bid and user.bid=expense.bid and bank.id="+bid+" and expense.type=1 ORDER BY timestamp",null);
        return res;
    }

    public float getBalance(int bank)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from user where bid = "+bank,null);
        if(res.getCount()>0){
            float num=0;
            while (res.moveToNext()) {
                num = Float.parseFloat(res.getString(3));
            }
            return num;
        }
        return 0;
    }
}
