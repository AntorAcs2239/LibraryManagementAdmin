package com.example.bookhouseadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.bookhouseadmin.databinding.ActivityPendingbookDetailsBinding;
import com.example.bookhouseadmin.databinding.ActivityRequestDetialsBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;

public class PendingbookDetails extends AppCompatActivity {
    private String img,bookname,writername,studentname,pendingid,bookid,catagory,studentid,bookserial,registration,time,phone;
    private ActivityPendingbookDetailsBinding binding;
    private FirebaseFirestore firestore;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pendingbook_details);
        binding= ActivityPendingbookDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        img=getIntent().getStringExtra("img");
        bookname=getIntent().getStringExtra("bookname");
        writername=getIntent().getStringExtra("writername");
        studentname=getIntent().getStringExtra("studentname");
        pendingid=getIntent().getStringExtra("pendingid");
        bookid=getIntent().getStringExtra("bookid");
        studentid=getIntent().getStringExtra("studentid");
        catagory=getIntent().getStringExtra("catagory");
        bookserial=getIntent().getStringExtra("serial");
        registration=getIntent().getStringExtra("registration");
        phone=getIntent().getStringExtra("phone");
        firestore=FirebaseFirestore.getInstance();
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Receiving ...");
        Glide.with(this).load(img).into(binding.pendingbookimg);
        binding.studentnamereq.setText("Student Name: "+studentname);
        binding.pendingwriter.setText("Writer Name: "+writername);
        binding.bname.setText("Book Name: "+bookname);
        binding.studentregistration.setText("Regi_No: "+registration);
        binding.bookserial.setText("Book Serial: "+bookserial);
        function();
    }
    public void function() {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = connectivityManager.getNetworkInfo(connectivityManager.TYPE_WIFI);
        NetworkInfo mobileinternet = connectivityManager.getNetworkInfo(connectivityManager.TYPE_MOBILE);
        if (mobileinternet.isConnected()||wifi.isConnected())
        {
            binding.Delete.setVisibility(View.VISIBLE);
            binding.Delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    progressDialog.show();
                    firestore.collection("PendingBook")
                            .document(pendingid)
                            .delete()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    firestore.collection(catagory)
                                            .document(bookid)
                                            .update("available",true,"numofbook",FieldValue.increment(1))
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    firestore.collection("StudentRecord")
                                                            .document(studentid)
                                                            .update("numofbook",FieldValue.increment(-1))
                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void aVoid) {
                                                                    Toast.makeText(PendingbookDetails.this,"Received",Toast.LENGTH_SHORT).show();
                                                                    DocumentReference documentReference=firestore.collection("Past Record").document();
                                                                    Pendingbookmodel pendingbookmodel=new Pendingbookmodel(bookid,catagory,studentid,studentname,bookname,writername,documentReference.getId(),new Timestamp(new Date()),img,bookserial,phone,registration);
                                                                    documentReference.set(pendingbookmodel);
                                                                    startActivity(new Intent(PendingbookDetails.this,MainActivity.class));
                                                                }
                                                            })
                                                            .addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception e) {
                                                                    Toast.makeText(PendingbookDetails.this,e.toString(),Toast.LENGTH_LONG).show();
                                                                }
                                                            });
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(PendingbookDetails.this,e.toString(),Toast.LENGTH_LONG).show();
                                                }
                                            });
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(PendingbookDetails.this,e.toString(),Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }
        else
        {
            Toast.makeText(PendingbookDetails.this,"No internet Connection",Toast.LENGTH_LONG).show();
            binding.Delete.setVisibility(View.INVISIBLE);
        }
    }
}