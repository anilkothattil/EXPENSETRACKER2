package com.example.expensetracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.expensetracker.Bank;
import java.util.ArrayList;
import java.util.List;
    public class TableControllerBank extends DatabaseHandler {
        public TableControllerBank(Context context) {
            super(context);
        }
        public List<Bank> read() {

            List<Bank> recordsList = new ArrayList<Bank>();

            String sql = "SELECT * FROM bank ORDER BY bid";

            SQLiteDatabase db=this.getWritableDatabase();

            Cursor cursor=db.rawQuery(sql,null);


            if (cursor.moveToFirst())
            {

                do {
                    {
                        int bid = Integer.parseInt(cursor.getString(cursor.getColumnIndex("bid")));
                        String bankName = cursor.getString(cursor.getColumnIndex("bankname"));

                        Bank bank = new Bank();
                        bank.bid = bid;
                        bank.bankName = bankName;
                        recordsList.add(bank);
                    }
                }while (cursor.moveToNext());

            }

            cursor.close();
            db.close();

            return recordsList;
        }
    }
