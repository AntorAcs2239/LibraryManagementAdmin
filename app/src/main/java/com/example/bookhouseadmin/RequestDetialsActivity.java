package com.example.bookhouseadmin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.bookhouseadmin.databinding.ActivityRequestDetialsBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ServerTimestamp;

import java.text.SimpleDateFormat;
import java.util.Date;

public class RequestDetialsActivity extends AppCompatActivity {
   private Button button;
   private String serial;
   private String bookid,bookname,writername,studentid,studentname,catagory,bookimg,phone,requestid,description,registration;
   private int n;
   private FirebaseFirestore firestore;
   private   ActivityRequestDetialsBinding binding;
   private int numofbook,numofbookstudenthave;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_detials);
        binding= ActivityRequestDetialsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        button=findViewById(R.id.add);
        firestore=FirebaseFirestore.getInstance();

        bookid=getIntent().getStringExtra("bookid");
        catagory=getIntent().getStringExtra("catagory");
        studentid=getIntent().getStringExtra("studentid");
        studentname=getIntent().getStringExtra("studentname");
        bookname=getIntent().getStringExtra("bookname");
        writername=getIntent().getStringExtra("writername");
        bookimg=getIntent().getStringExtra("bookimg");
        requestid=getIntent().getStringExtra("requestid");
        description=getIntent().getStringExtra("description");
        registration=getIntent().getStringExtra("registration");
        phone=getIntent().getStringExtra("phone");
        numofbook=getIntent().getIntExtra("numofbook",0);
        numofbookstudenthave=getIntent().getIntExtra("numofbookstudenthave",0);

        Glide.with(this).load(bookimg).into(binding.pendingbookimg);
        binding.bname.setText("Book Name :"+bookname);
        binding.pendingwriter.setText("Writer Name :"+writername);
        binding.studentnamereq.setText("Student Name :"+studentname);
        binding.phone.setText("Phone Number: "+phone);
        binding.description.setText("Description : \n"+description);
        binding.registration.setText("Reg_No: "+registration);

        Toast.makeText(RequestDetialsActivity.this,"Num of Current Book:"+numofbook,Toast.LENGTH_LONG).show();
        function();
    }
    public void function()
    {
        ConnectivityManager connectivityManager=(ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi=connectivityManager.getNetworkInfo(connectivityManager.TYPE_WIFI);
        NetworkInfo mobileinternet=connectivityManager.getNetworkInfo(connectivityManager.TYPE_MOBILE);
        if (wifi.isConnected()||mobileinternet.isConnected())
        {
            if (numofbook==0||numofbookstudenthave>=3)
            {
                binding.add.setVisibility(View.GONE);
                binding.Delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        firestore.collection("RequestQ")
                                .document(requestid)
                                .delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        startActivity(new Intent(RequestDetialsActivity.this,MainActivity.class));
                                        Toast.makeText(RequestDetialsActivity.this,"Rejected Request",Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        startActivity(new Intent(RequestDetialsActivity.this,MainActivity.class));
                                        Toast.makeText(RequestDetialsActivity.this,"Rejection Failed",Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                });
                    }
                });
            }
            else {

                binding.add.setVisibility(View.VISIBLE);
                binding.add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(RequestDetialsActivity.this);
                        View view2 = LayoutInflater.from(RequestDetialsActivity.this).inflate(R.layout.dialog, null);
                        builder.setView(view2);
                        AlertDialog alertDialog = builder.create();
                        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                        alertDialog.show();
                        Button delete, edit;
                        TextView class_edit, subject_edit, title;
                        EditText serialedit;
                        serialedit=view2.findViewById(R.id.serialnum);
                        delete = view2.findViewById(R.id.cancle);
                        edit = view2.findViewById(R.id.add);

                        delete.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                alertDialog.dismiss();
                            }
                        });
                        edit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                serial=serialedit.getText().toString();
                                DocumentReference documentReference = firestore.collection("PendingBook").document();
                                Pendingbookmodel pendingbookmodel = new Pendingbookmodel(bookid, catagory, studentid, studentname, bookname, writername, documentReference.getId(), new Timestamp(new Date()),bookimg,serial,phone,registration);
                                documentReference.set(pendingbookmodel);
                                firestore.collection(catagory)
                                        .document(bookid)
                                        .update("numofbook", FieldValue.increment(-1));
                                if (numofbook-1==0)
                                {
                                    firestore.collection(catagory)
                                            .document(bookid)
                                            .update("available",false);
                                }
                                firestore.collection("StudentRecord")
                                        .document(studentid)
                                        .update("numofbook",FieldValue.increment(1));
                                firestore.collection("RequestQ")
                                        .document(requestid)
                                        .delete()
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(RequestDetialsActivity.this, "Succesfully Deleted", Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(RequestDetialsActivity.this, "Failed Deleted", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                startActivity(new Intent(RequestDetialsActivity.this, MainActivity.class));
                                finish();
                            }
                        });

                    }
                });
                binding.Delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        firestore.collection("RequestQ")
                                .document(requestid)
                                .delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(RequestDetialsActivity.this,"Succesfully Deleted",Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(RequestDetialsActivity.this,"Failed Deleted",Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                });
            }
        }
        else
        {
            Toast.makeText(RequestDetialsActivity.this,"No Internet Connection",Toast.LENGTH_LONG).show();
            binding.add.setVisibility(View.INVISIBLE);
            binding.Delete.setVisibility(View.INVISIBLE);
        }
    }
}