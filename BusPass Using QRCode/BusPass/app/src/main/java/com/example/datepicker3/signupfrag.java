package com.example.datepicker3;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;
import java.util.regex.Pattern;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;


public class signupfrag extends Fragment {

    EditText name,email,password;
    ImageView signup;
    private ImageView btnChoose;
    private Uri filePath;
    FirebaseStorage storage;
    StorageReference storageReference;
    String fileurl;
    private final int PICK_IMAGE_REQUEST = 71;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_signupfrag, container, false);
        name=view.findViewById(R.id.name);
        email=view.findViewById(R.id.email);
        password=view.findViewById(R.id.password);
        signup=view.findViewById(R.id.signup);

        btnChoose = view.findViewById(R.id.btnChoose);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        signup.setEnabled(false);
        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*SharedPreferences sp=getActivity().getSharedPreferences("signupcheck", MODE_PRIVATE);
                SharedPreferences.Editor Ed=sp.edit();
                Ed.putString("success","true");
                Ed.commit();
                Log.v("VALUE",sp.getString("success",null));
                sp=null;*/
               // String email1=email.getText().toString();
                //logincheck(email1);
                if(fileurl==null)
                {
                    Toast.makeText(getActivity(), "Upload Image First", Toast.LENGTH_LONG).show();
                }
                else{
                    String email1=email.getText().toString();
                    String name1=name.getText().toString();
                    String password1=password.getText().toString();
                    int check=validitycheck(email1,name1,password1);
                    if(check==3)
                    {
                        Log.v("CHECK HERE",email1+name1+password1);
                        register(email1,name1,password1,fileurl);
                    }
                //readytoregister(email1,name1,password1);
            }
            }
        });
        return view;

    }
/*
    public void readytoregister(String email1,String name1,String password1)
    {
        int check=validitycheck(email1,name1,password1);
        SharedPreferences sp1 = getActivity().getSharedPreferences("signupcheck", MODE_PRIVATE);
        String check2 = sp1.getString("success", null);
        sp1=null;
        Log.v("VALUE",""+!check2.equals("false"));
        if(check==3)
        {
            if(check2.equals("false"))
            {
                Log.v("CHECK HERE",email1+name1+password1);
                register(email1,name1,password1);
            }
        }
    }*/


    public int validitycheck(String email1,String name1,String password1)
    {
        int check=3;
        if(email1!=null && email1.equals(""))
        {
            Toast.makeText(getActivity(), "Email Cannot Be Empty", Toast.LENGTH_LONG).show();
            check--;
        }
        else{
            if (!isValid(email1))
            {
                Toast.makeText(getActivity(), "ENTER A VALID EMAIL ADDRESS", Toast.LENGTH_LONG).show();
                check--;
            }
        }
        if(name1!=null && name1.equals(""))
        {
            Toast.makeText(getActivity(), "Name Cannot Be Empty", Toast.LENGTH_LONG).show();
            check--;
        }
        if(password1!=null && password1.equals(""))
        {
            Toast.makeText(getActivity(), "Password Cannot Be Empty", Toast.LENGTH_LONG).show();
            check--;
        }
        Log.v("result",""+check);
        return check;
    }

    public void register(String email1,String name1,String password1,String fileurl1)
    {

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("users");
        final String userId = mDatabase.push().getKey();
        User user = new User(name1, email1, password1,fileurl1);
        mDatabase.child(userId).setValue(user);
        SharedPreferences sp=getActivity().getSharedPreferences("Login", MODE_PRIVATE);
        SharedPreferences.Editor Ed=sp.edit();
        Ed.putString("email",email1);
        Ed.putString("Psw",password1);
        Ed.putString("name",name1);
        Ed.putString("fileurl",fileurl);
        Ed.putString("userid",userId);
        Ed.commit();

        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.putExtra("custid", userId);
        intent.putExtra("name",name1);
        intent.putExtra("email",email1);
        startActivity(intent);
    }


    public static boolean isValid(String email)
    {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }


    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            try {
                uploadImage();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



    private void uploadImage() throws IOException {

        if(filePath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(getActivity());
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            final StorageReference ref=storageReference.child("images/"+ UUID.randomUUID().toString());
            Bitmap bmp = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 25, baos);
            byte[] data = baos.toByteArray();
            //uploading the image
            UploadTask uploadTask2 = ref.putBytes(data);
            uploadTask2.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.dismiss();
                    Toast.makeText(getActivity(), "Uploaded", Toast.LENGTH_SHORT).show();
                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            // getting image uri and converting into string
                            Uri downloadUrl = uri;
                            fileurl = downloadUrl.toString();
                            Log.v("TAG",fileurl);
                        }
                    });
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(), "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+(int)progress+"%");
                            signup.setEnabled(true);
                        }
                    });
        }
    }

/*
    public boolean logincheck(final String email1) {
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
                    getdata(keys.get(i), email1);
                    Log.v("DOING","WORK");
                }
                Log.v("DOING","WORKED");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return success;
    }

    public void getdata(final String userId, final String gemail1) {

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("users");

        mDatabase.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                User user = dataSnapshot.getValue(User.class);

                if (gemail1.equals(user.email)) {
                    Log.d("DOne", "Password" + user.password + ", email " + user.email);
                    Toast.makeText(getContext(), "ALREADY REGISTERED WITH THIS EMAIL ID. Please Login!!", Toast.LENGTH_SHORT).show();
                    SharedPreferences sp2=getActivity().getSharedPreferences("signupcheck", MODE_PRIVATE);
                    SharedPreferences.Editor Ed=sp2.edit();
                    Ed.putString("success","false");
                    Ed.commit();
                    String check2 = sp2.getString("success", null);
                    sp2=null;
                    Log.v("VALUE2",check2);
                }
                else {
                    SharedPreferences sp=getActivity().getSharedPreferences("signupcheck", MODE_PRIVATE);
                    SharedPreferences.Editor Ed=sp.edit();
                    Ed.putString("success","true");
                    Ed.commit();
                }
                Log.v("WORK","DONE");
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Fucked Up", "Failed to read value.", error.toException());
            }
        });
    }*/
}
