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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.bookhouseadmin.databinding.ActivityBookdetailsBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

public class BookdetailsActivity extends AppCompatActivity {
    private ActivityBookdetailsBinding binding;
   private FirebaseFirestore firestore;
   private FirebaseAuth firebaseAuth;
   private String image,writer,bookname,bookid,catagory;
   private int numofbook,totalnumberofbook;
   private int updatenumofbook;
   private int c;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookdetails);
        binding= ActivityBookdetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseAuth=FirebaseAuth.getInstance();
        firestore=FirebaseFirestore.getInstance();
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Updating...");
        image=getIntent().getStringExtra("image");
        writer=getIntent().getStringExtra("wname");
        bookname=getIntent().getStringExtra("bname");
        bookid=getIntent().getStringExtra("bookid");
        catagory=getIntent().getStringExtra("catagory");
        numofbook=getIntent().getIntExtra("numofbook",0);
        totalnumberofbook=getIntent().getIntExtra("totalnumberofbook",0);
        binding.BookName.setText(bookname);
        binding.writerName.setText(writer);
        binding.numofbook.setText("Current number of Books Available: "+numofbook+"\n"+"Total number of book: "+totalnumberofbook);
        Glide.with(this).load(image).into(binding.image);
        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this,R.array.amountofbook, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerbooknum.setAdapter(adapter);
        Check();
        binding.spinnerbooknum.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String t=adapterView.getItemAtPosition(i).toString();
                updatenumofbook=Integer.parseInt(t);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                updatenumofbook=0;
            }
        });
        binding.updatebookname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                firestore.collection(catagory)
                        .document(bookid)
                        .update("bookname",binding.BookName.getText().toString())
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(BookdetailsActivity.this,"Updated Successfully",Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(BookdetailsActivity.this,"Update failed",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        binding.Updatewritername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                firestore.collection(catagory)
                        .document(bookid)
                        .update("writername",binding.writerName.getText().toString())
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                progressDialog.dismiss();
                                Toast.makeText(BookdetailsActivity.this,"Updated Successfully",Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(BookdetailsActivity.this,"Update failed",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        binding.increment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                firestore.collection(catagory)
                        .document(bookid)
                        .update("numofbook",FieldValue.increment(updatenumofbook))
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                progressDialog.dismiss();
                                Toast.makeText(BookdetailsActivity.this,"Updated Successfully",Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(BookdetailsActivity.this,"Update failed",Toast.LENGTH_SHORT).show();
                    }
                });
                firestore.collection(catagory)
                        .document(bookid)
                        .update("totalnumofbook",FieldValue.increment(updatenumofbook))
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                progressDialog.dismiss();
                                Toast.makeText(BookdetailsActivity.this,"Updated Successfully",Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(BookdetailsActivity.this,"Update failed",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
    public void Check()
    {
        ConnectivityManager connectivityManager=(ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi=connectivityManager.getNetworkInfo(connectivityManager.TYPE_WIFI);
        NetworkInfo mobileinternet=connectivityManager.getNetworkInfo(connectivityManager.TYPE_MOBILE);
        if(wifi.isConnected()||mobileinternet.isConnected())
        {
           binding.increment.setVisibility(View.VISIBLE);
           binding.updatebookname.setVisibility(View.VISIBLE);
           binding.Updatewritername.setVisibility(View.VISIBLE);
           binding.nointernet.setVisibility(View.GONE);
        }
        else
        {
            binding.increment.setVisibility(View.GONE);
            binding.updatebookname.setVisibility(View.GONE);
            binding.Updatewritername.setVisibility(View.GONE);
            binding.nointernet.setVisibility(View.VISIBLE);
        }
    }
}