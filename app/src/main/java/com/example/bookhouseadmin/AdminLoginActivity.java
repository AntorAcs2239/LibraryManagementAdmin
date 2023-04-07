package com.example.bookhouseadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.example.bookhouseadmin.databinding.ActivityAdminLoginBinding;
import com.example.bookhouseadmin.databinding.ActivityAdminSignupBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
public class AdminLoginActivity extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    ActivityAdminLoginBinding binding;
    SharedPreferences sharepef;
    private static  Boolean CHECK;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);
        binding= ActivityAdminLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseAuth=FirebaseAuth.getInstance();
        sharepef=getPreferences(MODE_PRIVATE);
        CHECK=sharepef.getBoolean("check",false);
        if (CHECK==true)
        {
            startActivity(new Intent(AdminLoginActivity.this,MainActivity.class));
            finish();
        }
        FirebaseUser user;
        String email=binding.adminemail.getText().toString();
        String pass=binding.password.getText().toString();
        if (firebaseAuth.getCurrentUser()!=null)
        {
            FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
            firebaseUser.reload();
            binding.adminlogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                String name,email,password,phone,uid;
                email=binding.adminemail.getText().toString();
                if (TextUtils.isEmpty(email))
                {
                    binding.adminemail.setError("Email should fill up");
                    binding.adminemail.requestFocus();
                }
                else
                {
                        firebaseUser.reload();
                        if (firebaseUser.isEmailVerified())
                        {
                            CHECK=true;
                            saveme(CHECK);
                            startActivity(new Intent(AdminLoginActivity.this,MainActivity.class));
                        }
                        else
                        {

                        }
                }
            }
        });
        }
       else if (firebaseAuth.getCurrentUser()==null)
        {
            binding.adminlogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String useremail,password;
                    useremail=binding.adminemail.getText().toString();
                    password=binding.password.getText().toString();
                    if (TextUtils.isEmpty(useremail))
                    {
                        binding.adminemail.setError("Email should fill up");
                        binding.adminemail.requestFocus();
                    }
                    firebaseAuth.signInWithEmailAndPassword(useremail,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful())
                            {
                                CHECK=true;
                                saveme(CHECK);
                                startActivity(new Intent(AdminLoginActivity.this,MainActivity.class));
                                finish();
                            }
                            else
                            {
                                Toast.makeText(AdminLoginActivity.this,"Login Failed",Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            });
        }
//        binding.adminlogin.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                String name,email,password,phone,uid;
//                email=binding.adminemail.getText().toString();
//                if (TextUtils.isEmpty(email))
//                {
//                    binding.adminemail.setError("Email should fill up");
//                    binding.adminemail.requestFocus();
//                }
//                else
//                {
//                    firebaseAuth=FirebaseAuth.getInstance();
//                    if (firebaseAuth.getCurrentUser()==null)
//                    {
//                        Toast.makeText(AdminLoginActivity.this,"Create an account",Toast.LENGTH_LONG).show();
//                    }
//                    else
//                    {
//                        FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
//                        firebaseUser.reload();
//                        if (firebaseAuth.getCurrentUser()!=null&&firebaseUser.isEmailVerified())
//                        {
//                            startActivity(new Intent(AdminLoginActivity.this,MainActivity.class));
//                        }
//                        else if (firebaseAuth.getCurrentUser()==null)
//                        {
//                            Toast.makeText(AdminLoginActivity.this,"Create an account",Toast.LENGTH_LONG).show();
//                        }
//                        else if (!firebaseUser.isEmailVerified())
//                        {
//                            Toast.makeText(AdminLoginActivity.this,"Email is not Varifiet",Toast.LENGTH_LONG).show();
//                        }
//                    }
//                }
//            }
//        });
        binding.alredyhave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminLoginActivity.this,AdminSignupActivity.class));
            }
        });
    }
    public void saveme(Boolean name)
    {
        SharedPreferences.Editor editor=sharepef.edit();
        editor.putBoolean("check",name);
        editor.apply();
    }
}