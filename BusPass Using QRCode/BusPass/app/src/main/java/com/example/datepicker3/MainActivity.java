package com.example.datepicker3;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.squareup.timessquare.CalendarPickerView;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {


    private static final int MY_PERMISSIONS_REQUEST_EXTERNAL_STORAGE = 0;
    long diff=0;
    double finalamount=0;
    boolean check=true;
    String first_date="",last_date="";
    String custid,name,email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        String name = intent.getExtras().getString("name");
        final String custid = intent.getExtras().getString("custid");
        String email= intent.getExtras().getString("email");
        Toast.makeText(this,"Welcome "+name,Toast.LENGTH_LONG).show();


        while(check) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_EXTERNAL_STORAGE);

            } else {

                check=false;
                final CalendarPickerView calendar_view = findViewById(R.id.calendar_view);
                final Random random = new Random();
//getting current
                Calendar nextYear = Calendar.getInstance();
                nextYear.add(Calendar.YEAR, 1);
                Date today = new Date();
                final Button btn_show_dates = findViewById(R.id.btn_show_dates);

//add one year to calendar from todays date
                calendar_view.init(today, nextYear.getTime())
                        .inMode(CalendarPickerView.SelectionMode.RANGE);

                //action while clicking on a date
                calendar_view.setOnDateSelectedListener(new CalendarPickerView.OnDateSelectedListener() {
                    @Override
                    public void onDateSelected(Date date) {

                        //Toast.makeText(getApplicationContext(),"Selected Date is : " +date.toString(),Toast.LENGTH_SHORT).show();

                        try {
                            String[] first_datearr = calendar_view.getSelectedDates().get(0).toString().split(" ");
                            int first_month = (calendar_view.getSelectedDates().get(0).getMonth())+1;
                            first_date = first_datearr[2] + "/" + first_month + "/" + first_datearr[5];
                            String[] last_datearr = calendar_view.getSelectedDates().get(calendar_view.getSelectedDates().size() - 1).toString().split(" ");
                            int last_month = (calendar_view.getSelectedDates().get(calendar_view.getSelectedDates().size() - 1).getMonth())+1;
                            last_date = last_datearr[2] + "/" + last_month + "/" + last_datearr[5];
                            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

                            Date startdate = sdf.parse(first_date);
                            Date enddate = sdf.parse(last_date);
                            diff = enddate.getTime() - startdate.getTime();
                            long days = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
                            Toast.makeText(getApplicationContext(), "Days: " + (days + 1), Toast.LENGTH_SHORT).show();
                            finalamount = ((days + 1.0) * 50);
                            btn_show_dates.setText("Pay " + finalamount);
                            btn_show_dates.setEnabled(true);
                            btn_show_dates.setBackgroundColor(getResources().getColor(R.color.buttonactive));

                        } catch (ParseException e) {
                            Toast.makeText(getApplicationContext(), "SELECT DATES PLEASE", Toast.LENGTH_SHORT).show();
                        }
                        if (btn_show_dates.isEnabled()) {
                            btn_show_dates.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    int orderid1 = random.nextInt(99999);
                                    //int custid1 = random.nextInt(9999);
                                    //String custid = "Cust" + custid1;
                                    String orderid = "" + orderid1;
                                    String finalamount1 = "" + finalamount;
                                    Intent intent = new Intent(MainActivity.this, com.example.datepicker3.paytmgateway.class);
                                    intent.putExtra("orderid", orderid);
                                    intent.putExtra("custid", custid);
                                    intent.putExtra("finalamount", finalamount1);
                                    intent.putExtra("fdate", first_date);
                                    intent.putExtra("ldate", last_date);
                                    startActivity(intent);
                                }
                            });
                        }

                    }



                    @Override
                    public void onDateUnselected(Date date) {

                        //Toast.makeText(getApplicationContext(),"UnSelected Date is : " +date.toString(),Toast.LENGTH_SHORT).show();
                    }
                });


            }


        }


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // show menu when menu button is pressed
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mymenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // display a message when a button was pressed
        if (item.getItemId() == R.id.logout) {
            SharedPreferences sp=getSharedPreferences("Login", MODE_PRIVATE);
            SharedPreferences.Editor Ed=sp.edit();
            Ed.putString("email",null);
            Ed.putString("Psw",null);
            Ed.commit();
            Intent intent=new Intent(this, login.class);
            startActivity(intent);
        }
        if (item.getItemId() == R.id.showalldetail) {
            Intent intent=new Intent(this, showdetails.class);
            startActivity(intent);
        }

        return true;
    }
}
