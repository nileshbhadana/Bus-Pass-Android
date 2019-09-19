package com.example.datepicker3;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;


public class loginfrag extends Fragment {
    EditText gpassword, gemail;
    TextView tv;
    ImageView login;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_loginfrag, container, false);
        gemail = view.findViewById(R.id.gemail);
        gpassword = view.findViewById(R.id.gpassword);
        login = view.findViewById(R.id.login);
        tv = view.findViewById(R.id.mytv);
        SharedPreferences sp1 = getActivity().getSharedPreferences("Login", MODE_PRIVATE);
        String emailsp = sp1.getString("email", null);
        String passwordsp = sp1.getString("Psw", null);
        if (emailsp != null && passwordsp != null) {
            Log.d("DOne", "SP WORKED");
            String userIdsp = sp1.getString("userid", null);
            String namesp = sp1.getString("name", null);
            Intent intent = new Intent(getActivity(), MainActivity.class);
            intent.putExtra("custid", userIdsp);
            intent.putExtra("name", namesp);
            intent.putExtra("email", emailsp);
            startActivity(intent);
        }
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String gemail1 = gemail.getText().toString();
                String gpassword1 = gpassword.getText().toString();
                boolean success = logincheck(gemail1, gpassword1);

            }
        });
        return view;
    }

    public boolean logincheck(final String gemail1, final String gpassword1) {
        boolean success = false;
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("users");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> keys = new ArrayList<String>();
                for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                    Log.v("MyTag", "" + childDataSnapshot.getKey()); //displays the key for the node
                    keys.add(childDataSnapshot.getKey());
                }
                int i;
                for (i = 0; i < keys.size(); i++) {
                    getdata(keys.get(i), gemail1, gpassword1);

                    if (tv.getText().toString().equals("DONE")) {
                        tv.setText("JE BAAT");
                        break;
                    }
                    Log.v("TV LOG", tv.getText().toString());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return success;


    }

    public void getdata(final String userId, final String gemail1, final String gpassword1){

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("users");

        mDatabase.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                User user = dataSnapshot.getValue(User.class);

                if(gemail1.equals(user.email) && gpassword1.equals(user.password))
                {
                    Log.d("DOne", "Password" + user.password+ ", email " + user.email);
                    tv.setText("DONE");
                    SharedPreferences sp=getActivity().getSharedPreferences("Login", MODE_PRIVATE);
                    SharedPreferences.Editor Ed=sp.edit();
                    Ed.putString("email",user.email);
                    Ed.putString("Psw",user.password);
                    Ed.putString("name",user.name);
                    Ed.putString("fileurl",user.fileurl);
                    Ed.putString("userid",userId);
                    Ed.commit();

                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    intent.putExtra("custid", userId);
                    intent.putExtra("name",user.name);
                    intent.putExtra("email",user.email);
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(getContext(),"WRONG EMAIL OR PASSWORD",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Fucked Up", "Failed to read value.", error.toException());
            }
        });

    }
}