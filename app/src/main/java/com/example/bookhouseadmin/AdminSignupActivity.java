package com.example.bookhouseadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.bookhouseadmin.databinding.ActivityAdminSignupBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class AdminSignupActivity extends AppCompatActivity {
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    ActivityAdminSignupBinding binding;
    String post;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_signup);
        binding=ActivityAdminSignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseAuth=FirebaseAuth.getInstance();
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Please Wait a moment..");
        firebaseFirestore=FirebaseFirestore.getInstance();
        binding.adminsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name,email,password,phone,uid;
                name=binding.adminname.getText().toString();
                email=binding.adminemail.getText().toString();
                password=binding.adminpassword.getText().toString();
                phone=binding.adminphone.getText().toString();
                if (TextUtils.isEmpty(email))
                {
                    binding.adminemail.setError("Email should fill up");
                    binding.adminemail.requestFocus();
                }
               else if (password.isEmpty())
                {
                    binding.adminpassword.setError("Password should fill up");
                    binding.adminpassword.requestFocus();
                }
              else  if (phone.isEmpty())
                {
                    binding.adminphone.setError("Phone should fill up");
                    binding.adminphone.requestFocus();
                }
               else if (name.isEmpty())
                {
                    binding.adminname.setError("Name should fill up");
                    binding.adminname.requestFocus();
                }
                else
                {
                    firebaseAuth.createUserWithEmailAndPassword(email,password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful())
                                    {
                                        FirebaseUser user=firebaseAuth.getCurrentUser();
                                        user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(AdminSignupActivity.this,"Verification code is sent",Toast.LENGTH_LONG).show();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {

                                            }
                                        });
                                        String uid=task.getResult().getUser().getUid();
                                        AdminModel adminModel=new AdminModel(name,email,password,phone,uid,post);
                                        firebaseFirestore.collection("AdminRecord")
                                                .document(uid)
                                                .set(adminModel)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful())
                                                        {
                                                            progressDialog.dismiss();
                                                            startActivity(new Intent(AdminSignupActivity.this,AdminLoginActivity.class));
                                                            Toast.makeText(AdminSignupActivity.this,"Successfully Admin Added",Toast.LENGTH_LONG).show();
                                                        }
                                                        else
                                                        {
                                                            Toast.makeText(AdminSignupActivity.this,task.getException().getLocalizedMessage(),Toast.LENGTH_LONG).show();
                                                        }
                                                    }
                                                });
                                        progressDialog.show();
                                    }
                                    else
                                    {
                                        progressDialog.dismiss();
                                        Toast.makeText(AdminSignupActivity.this,task.getException().getLocalizedMessage(),Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                }
            }
        });
        ArrayAdapter<CharSequence> adapter2=ArrayAdapter.createFromResource(AdminSignupActivity.this,R.array.librarypost, android.R.layout.simple_spinner_dropdown_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.librarypost.setAdapter(adapter2);
        binding.librarypost.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                post=adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
}