package com.example.expensetracker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SMSReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(final Context context, Intent intent) {

        Log.i("SMS", "smsReceiver: SMS Received");

        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            Log.i("SMS", "smsReceiver : Reading Bundle");

            Object[] pdus = (Object[])bundle.get("pdus");
            SmsMessage sms = SmsMessage.createFromPdu((byte[])pdus[0]);
            final String sender  = sms.getOriginatingAddress();
            final String msg = sms.getMessageBody();
            final String debit_regex = "Rs. [0-9]+.[0-9]+";
            final Pattern debit_pattern = Pattern.compile(debit_regex, Pattern.MULTILINE);
            final Matcher debit_matcher = debit_pattern.matcher(msg);
            final String credit_regex = "INR [0-9]+.[0-9]+";
            final Pattern credit_pattern = Pattern.compile(credit_regex, Pattern.MULTILINE);
            final Matcher credit_matcher = credit_pattern.matcher(msg);
            Toast.makeText(context, "Message from "+sender+" : "+msg, Toast.LENGTH_LONG).show();
            Log.i("SMS", "smsBody : "+sms.getMessageBody());
            HandlerThread handlerThread =  new HandlerThread("database_helper");
            handlerThread.start();
            Handler handler =  new Handler(handlerThread.getLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    DatabaseHandler myDb = new DatabaseHandler(context);
                    int bank = myDb.dbcheck(sender);
                    if(bank!=0) {
                        if(msg.toUpperCase().contains("DEBITED")||msg.toUpperCase().contains("W/D")) {
                            float num = 0,msg_balance=0;
                            float balance = myDb.getBalance(bank);
                            while (debit_matcher.find()) {
                                if(num ==0) {
                                    num = Float.parseFloat(debit_matcher.group(0).substring(4));
                                }else {
                                    msg_balance = Float.parseFloat(debit_matcher.group(0).substring(4));
                                    break;
                                }
                            }
                            String timeStamp = String.valueOf(TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()));
                            myDb.insertExpense(bank, num, timeStamp,1);
                            Toast.makeText(context, "Debit recorded!", Toast.LENGTH_SHORT).show();
//                            if(msg_balance!=0){
//                                float credit=0;
//                                if(msg_balance>=balance){
//                                    credit = num + (msg_balance-balance);
//                                    //Toast.makeText(context, balance, Toast.LENGTH_SHORT).show();
//
//                                }else{
//                                    credit = msg_balance - (balance - num);
//                                }
//                                myDb.updateBalance(bank, msg_balance);
//
//                                myDb.insertExpense(bank, credit, timeStamp,2);
//                                Toast.makeText(context, "Credit recorded!", Toast.LENGTH_SHORT).show();
//                            }
                        }
//                        else if(msg.toUpperCase().contains("CREDITED")) {
//                            float num = 0,msg_balance=0;
//                            while (credit_matcher.find()) {
//                                Toast.makeText(context, "Credit", Toast.LENGTH_SHORT).show();
//                                if(num ==0) {
//                                    num = Float.parseFloat(credit_matcher.group(0).substring(4));
//                                    Toast.makeText(context, "Credit"+num, Toast.LENGTH_SHORT).show();
//                                }else {
//                                    msg_balance = Float.parseFloat(credit_matcher.group(0).substring(4));
//                                    break;
//                                }
//                            }
//                            if(num==0){
//                                while (debit_matcher.find()) {
//                                    num = Float.parseFloat(debit_matcher.group(0).substring(4));
//                                    break;
//                                }
//                            }
//                            String timeStamp = String.valueOf(TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()));
//                            myDb.insertExpense(bank, num, timeStamp,2);
//                            Toast.makeText(context, "Credit recorded!", Toast.LENGTH_SHORT).show();
//
//                        }
                    }
                }
            });
        }
    }
}