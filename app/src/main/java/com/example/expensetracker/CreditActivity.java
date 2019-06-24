package com.example.expensetracker;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CreditActivity extends AppCompatActivity implements   AdapterView.OnItemSelectedListener {

    TextView txtdisplay;
    String[] country = { "Weekly", "Monthly"};
    DatabaseHandler myDb;
    TextView txtcontent;
    int bid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit);
        txtcontent = findViewById(R.id.txtcontent);
        txtdisplay = findViewById(R.id.txtdisplay);
        myDb = new DatabaseHandler(this);



        //  txtcontent.setText("Hi sijo");

        //Getting the instance of Spinner and applying OnItemSelectedListener on it
        Spinner spin = (Spinner) findViewById(R.id.spinner);
        spin.setOnItemSelectedListener(this);


        //Creating the ArrayAdapter instance having the country list
        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,country);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spin.setAdapter(aa);
        Intent i = getIntent();
        Toast.makeText(getApplicationContext(),"BID"+i.getStringExtra("bid") , Toast.LENGTH_LONG).show();
        txtdisplay.setText(i.getStringExtra("content"));

        // bid = Integer.parseInt(i.getStringExtra("bid"));
         Toast.makeText(this, i.getStringExtra("content"), Toast.LENGTH_LONG).show();


    }

    //Performing action onItemSelected and onNothing selected
    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
        txtdisplay.setText("");
        Float sum = 0f;
        String pattern = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String content="";
        String curDate = simpleDateFormat.format(new Date());
        Intent i = getIntent();
        Cursor res = myDb.getWeeklyCredits(Integer.parseInt(i.getStringExtra("bid")));
        try {
            if (country[position].equals("Monthly")) {
                while (res.moveToNext()) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    String date = getDate(res.getString(9));
                    Date date1=new SimpleDateFormat("yyyy-MM-dd").parse(date);
                    Date fDate= simpleDateFormat.parse( simpleDateFormat.format(date1));
                    Date cDate= simpleDateFormat.parse(curDate);
                    //Toast.makeText(getApplicationContext(), "FDate:"+fDate.toString()+"Cur Date:"+ cDate.toString(), Toast.LENGTH_LONG).show();
                    if(fDate.getMonth()==cDate.getMonth()) {
                        if(!res.getString(8).equals("0"))
                            content+=getDate(res.getString(9))+"--Amount--"+res.getString(8)+"\n";
                        sum+=Float.parseFloat(res.getString(8));
                    }

                }
                txtcontent.setText(content);
                txtdisplay.setText("Total::"+sum);
            }
            else if(country[position].equals("Weekly")){
                while (res.moveToNext()) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    String date = getDate(res.getString(9));
                    Date date1=new SimpleDateFormat("yyyy-MM-dd").parse(date);
                    Date fDate= simpleDateFormat.parse( simpleDateFormat.format(date1));
                    Date cDate= simpleDateFormat.parse(curDate);
                    //Toast.makeText(getApplicationContext(), "FDate:"+fDate.toString()+"Cur Date:"+ cDate.toString(), Toast.LENGTH_LONG).show();
                    if(fDate.getDate()>=(cDate.getDate()-7)) {
                        if(!res.getString(8).equals("0"))
                            content+=getDate(res.getString(9))+"--Amount--"+res.getString(8)+"\n";
                        sum+=Float.parseFloat(res.getString(8));
                    }

                }
                txtcontent.setText(content);
                txtdisplay.setText("Total::"+sum);
            }
            Toast.makeText(getApplicationContext(), country[position], Toast.LENGTH_LONG).show();
        }
        catch (ParseException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }
    private String getDate(String time) {
        long timestamp = Long.parseLong(time);
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(timestamp * 1000);
        String date = DateFormat.format("yyyy-MM-dd hh:mm", cal).toString();
        return date;
    }
}
