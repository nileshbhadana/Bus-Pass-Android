package com.example.datepicker3;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.io.File;

public class showdetails extends AppCompatActivity {
ImageView profilepic;
String userid,name,url;
TextView testtv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showdetails);
        SharedPreferences sp1 = getSharedPreferences("Login", MODE_PRIVATE);
        userid= sp1.getString("userid", null);
        name= sp1.getString("name", null);
        url= sp1.getString("fileurl", null);
        if(userid==null)
        {
            Intent intent=new Intent(this,login.class);
        }
        else {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Loading...");
            progressDialog.show();


            testtv=findViewById(R.id.testtv);
            Log.v("userid",userid);
            Log.v("name",name);

            Log.v("url",url);
            String root = Environment.getExternalStorageDirectory().toString();
            File myDir = new File(root + "/QRCode/Latest.jpg");

            Bitmap myBitmap = BitmapFactory.decodeFile(myDir.getAbsolutePath());
            ImageView qrimg=findViewById(R.id.showdetailsqr);
            profilepic=findViewById(R.id.ivprofile);
            qrimg.setImageBitmap(myBitmap);
            testtv.setText(name);
            Glide.with(getApplicationContext()).load(url).into(profilepic);

            progressDialog.dismiss();
        }
       // loaddata(userid);

    }
}
