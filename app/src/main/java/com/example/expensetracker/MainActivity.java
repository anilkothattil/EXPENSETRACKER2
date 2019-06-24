package com.example.expensetracker;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    Spinner spinner;
    DatabaseHandler myDb;
    int selected;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        spinner = findViewById(R.id.spinner);

        Log.i("DB","Create Main");
        myDb = new DatabaseHandler(this);
        selected=-1;
        readRecords();
    }
    public void readRecords() {
        Cursor res_user = myDb.getAllUsers();
        if(res_user.getCount() == 0) {
            // show message
            Toast.makeText(this, "No data", Toast.LENGTH_SHORT).show();

            startActivity(new Intent(getApplicationContext(),registration.class));
            finish();
        }

        Cursor res = myDb.getAllData();
        ArrayList<String> spinnerArray = new ArrayList<String>();
        while (res.moveToNext()) {
            spinnerArray.add(res.getString(1));
        }
//        Toast.makeText(this, spinnerArray.toString(), Toast.LENGTH_SHORT).show();
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item,
                        spinnerArray); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout
                .simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerArrayAdapter);
        spinner.setVisibility(View.VISIBLE);spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long arg3)
            {
                String city = "The bank is " + parent.getItemAtPosition(position).toString();
                Toast.makeText(parent.getContext(), city, Toast.LENGTH_LONG).show();
                selected = position;
            }

            public void onNothingSelected(AdapterView<?> arg0)
            {
                // TODO Auto-generated method stub
            }
        });
    }

    public void changePage(View view) {
        String city = "The bank is " + spinner.getItemAtPosition(selected).toString();
        Toast.makeText(this, city, Toast.LENGTH_LONG).show();
        startActivity(new Intent(getApplicationContext(),activity_screen2.class));
    }

    public void bankwiseExpense(View view) {
        int bid = selected+1;
        Cursor res = myDb.getExpense(selected+1);
        Intent i = new Intent(getApplicationContext(),activity_screen2.class);
        String content="";
        while (res.moveToNext()) {
            content+=getDate(res.getString(9))+"--Amount--"+res.getString(8)+"\n";
        }

        i.putExtra("content",content);
        i.putExtra("bid",String.valueOf(bid));
        startActivity(i);
    }

//    public void showCredits(View view) {
//        int bid = selected+1;
//        Cursor res = myDb.getWeeklyCredits(selected+1);
//        Intent i = new Intent(getApplicationContext(),CreditActivity.class);
//        String content="";
//        while (res.moveToNext()) {
//            content+=getDate(res.getString(9))+"--Amount--"+res.getString(8)+"\n";
//        }
//
//        i.putExtra("content",content);
//        i.putExtra("bid",String.valueOf(bid));
//        startActivity(i);
//    }
    public void registerbank(View view) {
        startActivity(new Intent(getApplicationContext(),registration.class));
        finish();
    }

    public void showExpense(View view) {
        startActivity(new Intent(getApplicationContext(),ExpenseActivity.class));
        finish();
    }
    private String getDate(String time) {
        long timestamp = Long.parseLong(time);
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(timestamp * 1000);
        String date = DateFormat.format("dd-MM-yyyy hh:mm a", cal).toString();
        return date;
    }

//    public void onResume(){
//        Log.i("cs.fsu", "smsActivity : onResume");
//        super.onResume();
//        setContentView(R.layout.activity_main);
//
//        Intent intent = getIntent();
//        Bundle bundle = intent.getBundleExtra("mySMS");
//
//        if (bundle != null) {
//            Object[] pdus = (Object[])bundle.get("pdus");
//            SmsMessage sms = SmsMessage.createFromPdu((byte[])pdus[0]);
//            Log.i("SMS", "smsActivity : SMS is <" +  sms.getMessageBody() +">");
//
//            //strip flag
//            String message = sms.getMessageBody();
//            while (message.contains("FLAG"))
//                message = message.replace("FLAG", "");
//
//            TextView tx = (TextView) findViewById(R.id.txtSMS);
//            tx.setText(message);
//        } else
//            Log.i("cs.fsu", "smsActivity : NULL SMS bundle");
//    }

}
