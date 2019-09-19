package com.example.busqrscanner;

import android.app.ProgressDialog;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.text.ParseException;
import java.util.Date;


public class MainActivity  extends AppCompatActivity {
    Button btnScan;
    TextView tv_qr_readTxt;
    ImageView profilepic;
    String finalresult, userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnScan = findViewById(R.id.btnScan);
        tv_qr_readTxt = findViewById(R.id.tv_qr_readTxt);
        profilepic = findViewById(R.id.ivprofile);

        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator integrator = new IntentIntegrator(MainActivity.this);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                integrator.setPrompt("Scan");
                integrator.setCameraId(0);
                integrator.setBeepEnabled(false);
                integrator.setBarcodeImageEnabled(false);
                integrator.initiateScan();

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Log.e("Scan*******", "Cancelled scan");

            } else {
                Log.e("Scan", "Scanned");
                finalresult = result.getContents();
                String startdate = finalresult.split(" ")[0];
                String lastdate = finalresult.split(" ")[1];
                userId = finalresult.split(" ")[2];
                // tv_qr_readTxt.setText(result.getContents());
                try {
                    boolean check = true;
                    try {
                        check = betweendates(startdate, lastdate);
                      //  tv_qr_readTxt.setText(check+"");
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if (check) {
                        getuserdata(userId);
                    } else {
                        Toast.makeText(this, "NOT ALLOWED", Toast.LENGTH_LONG).show();
                        tv_qr_readTxt.setText("NOT ALLOWED");
                        profilepic.setVisibility(View.GONE);
                    }
                    //Toast.makeText(this, "SCANNED", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    // Toast.makeText(this, "WRONG QR CODE", Toast.LENGTH_LONG).show();
                    tv_qr_readTxt.setText("WRONG QR CODE");
                    profilepic.setVisibility(View.GONE);
                }
            }
        } else {
            // This is important, otherwise the result will not be passed to the fragment
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void getuserdata(String userId) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading...");
        progressDialog.show();


        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("users");
        mDatabase.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                User user = dataSnapshot.getValue(User.class);
                tv_qr_readTxt.setText(user.name + "\n" + "ALLOWED");
                profilepic.setVisibility(View.VISIBLE);
                Glide.with(getApplicationContext()).load(user.fileurl).into(profilepic);
                progressDialog.dismiss();

            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w("LOG", "Failed to read value.", error.toException());
            }
        });

    }

    private boolean betweendates(String startdate, String lastdate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        sdf.setLenient(false);
        Date min = sdf.parse(startdate);
        Date max = sdf.parse(lastdate);
        Date d = new Date();
        String currdate = sdf.format(d);
        Date date3 = sdf.parse(currdate);
       // tv_qr_readTxt.setText(date3+"   HEY   "+min+"  "+max);
        boolean success = false;
        if(((date3.after(min) && (date3.before(max)))) || (date3.equals(min) || date3.equals(max))){
            success=true;
        }
        return success;
    }
}

