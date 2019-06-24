package com.example.expensetracker;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Spinner;

import java.util.ArrayList;


public class registration extends AppCompatActivity {

EditText acno, bname;
    Spinner spinner;
    DatabaseHandler myDb;
    int selected;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        acno = findViewById(R.id.edtAccount);
        spinner = findViewById(R.id.spinner);

        Log.i("DB","Create Main");
        myDb = new DatabaseHandler(this);
        selected=-1;
        Cursor res = myDb.getAllData();
        if(res.getCount() == 0) {
            // show message
            Toast.makeText(this, "No data", Toast.LENGTH_SHORT).show();

        }

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

    public void registerbank(View view) {
        myDb.insertuser(selected+1,acno.getText().toString());
        Toast.makeText(this, "saved", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
    }


}
