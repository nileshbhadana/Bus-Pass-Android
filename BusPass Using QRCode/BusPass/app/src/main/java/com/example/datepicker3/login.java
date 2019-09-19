package com.example.datepicker3;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class login extends AppCompatActivity {
    EditText name, email, gpassword, gemail, password;
    Button login, signup;
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final TextView btn1 = findViewById(R.id.button1);
        final TextView demo=findViewById(R.id.demotv);
        final TextView demo2=findViewById(R.id.demotv2);
        final TextView btn2 = findViewById(R.id.button2);
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        Fragment fragment=new loginfrag();
        transaction.replace(R.id.output, fragment);
        transaction.commit();
        btn1.setVisibility(View.GONE);
        demo.setVisibility(View.GONE);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager manager = getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                Fragment fragment=new loginfrag();
                transaction.replace(R.id.output, fragment);
                transaction.commit();
                btn1.setVisibility(view.GONE);
                demo2.setVisibility(view.GONE);
                demo.setVisibility(view.VISIBLE);
                btn2.setVisibility(view.VISIBLE);

            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager manager = getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                Fragment fragment=new signupfrag();
                transaction.replace(R.id.output, fragment);
                transaction.commit();
                btn1.setVisibility(view.VISIBLE);
                demo.setVisibility(view.GONE);
                demo2.setVisibility(view.VISIBLE);
                btn2.setVisibility(view.GONE);

            }
        });

    }
}


       /*
        SharedPreferences sp1=this.getSharedPreferences("Login", MODE_PRIVATE);
        String emailsp=sp1.getString("email", null);
        String passwordsp= sp1.getString("Psw", null);
        if(emailsp!=null && passwordsp!=null)
        {
            Log.d("DOne", "SP WORKED");
            tv.setText("DONE");
            String userIdsp=sp1.getString("userid",null);
            String namesp=sp1.getString("name",null);
            Intent intent = new Intent(login.this, MainActivity.class);
            intent.putExtra("custid", userIdsp);
            intent.putExtra("name",namesp);
            intent.putExtra("email",emailsp);
            startActivity(intent);
        }
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email1=email.getText().toString();
                String name1=name.getText().toString();
                String password1=password.getText().toString();
                int check=validitycheck(email1,name1,password1);
                if(check==3)
                {
                    register(email1,name1,password1);
                }
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String gemail1=gemail.getText().toString();
                String gpassword1=gpassword.getText().toString();
                boolean success=logincheck(gemail1,gpassword1);
                if(success){
                    tv.setText("DONE");
                }
            }
        });
    }

    public boolean logincheck(final String gemail1, final String gpassword1){
        boolean success=false;
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("users");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> keys = new ArrayList<String>();
                for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                    Log.v("MyTag",""+ childDataSnapshot.getKey()); //displays the key for the node
                    keys.add(childDataSnapshot.getKey());
                }
                tv.setText("NOPE");
                int i;
                for(i=0;i<keys.size();i++){
                    getdata(keys.get(i),gemail1,gpassword1);

                    if(tv.getText().toString().equals("DONE")){
                        tv.setText("JE BAAT");
                        break;
                    }
                    Log.v("TV LOG",tv.getText().toString());
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
                    SharedPreferences sp=getSharedPreferences("Login", MODE_PRIVATE);
                    SharedPreferences.Editor Ed=sp.edit();
                    Ed.putString("email",user.email);
                    Ed.putString("Psw",user.password);
                    Ed.putString("name",user.name);
                    Ed.putString("userid",userId);
                    Ed.commit();

                    Intent intent = new Intent(login.this, MainActivity.class);
                    intent.putExtra("custid", userId);
                    intent.putExtra("name",user.name);
                    intent.putExtra("email",user.email);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Fucked Up", "Failed to read value.", error.toException());
            }
        });

    }





    public int validitycheck(String email1,String name1,String password1)
    {
        int check=3;
        if(email1!=null && email1.equals(""))
        {
            Toast.makeText(login.this, "Email Cannot Be Empty", Toast.LENGTH_LONG).show();
            check--;
        }
        if(name1!=null && name1.equals(""))
        {
            Toast.makeText(login.this, "Name Cannot Be Empty", Toast.LENGTH_LONG).show();
            check--;
        }
        if(password1!=null && password1.equals(""))
        {
            Toast.makeText(login.this, "Password Cannot Be Empty", Toast.LENGTH_LONG).show();
            check--;
        }
        Log.v("result",""+check);
        return check;
    }

    public void register(String email1,String name1,String password1)
    {

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("users");
        final String userId = mDatabase.push().getKey();
        User user = new User(name1, email1, password1);
        mDatabase.child(userId).setValue(user);

    }
}
*/


