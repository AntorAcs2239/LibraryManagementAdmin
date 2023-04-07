package com.example.bookhouseadmin;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.bookhouseadmin.databinding.ActivityAllBooksBinding;
import com.example.bookhouseadmin.databinding.ActivityStudentDetailsBinding;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class StudentDetails extends AppCompatActivity {
    ActivityStudentDetailsBinding binding;
    FirebaseFirestore firestore;
    String sname,dept,regi,phone,sid;
    ArrayList<Pendingbookmodel>list;
    PendingBookAdapter studentAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_details);
        binding= ActivityStudentDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firestore=FirebaseFirestore.getInstance();
        sname=getIntent().getStringExtra("studentname");
        dept=getIntent().getStringExtra("department");
        regi=getIntent().getStringExtra("registration");
        sid=getIntent().getStringExtra("studentid");
        phone=getIntent().getStringExtra("phone");

        list=new ArrayList<>();
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this, RecyclerView.HORIZONTAL,false);
        studentAdapter=new PendingBookAdapter(this,list);
        binding.rec.setLayoutManager(linearLayoutManager);
        binding.rec.setAdapter(studentAdapter);
        binding.studentdepartment.setText("Department: "+dept);
        binding.studentnamedetails.setText("Student Name: "+sname);
        binding.studentphonenumber.setText("Phone: "+phone);
        binding.studentregistraion.setText("Registraion: "+regi);


        firestore.collection("PendingBook")
                .whereEqualTo("studentid",sid)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        list.clear();
                        for (DocumentSnapshot snapshot:value.getDocuments())
                        {
                            Pendingbookmodel pendingbookmodel=snapshot.toObject(Pendingbookmodel.class);
                            list.add(pendingbookmodel);
                        }
                        studentAdapter.notifyDataSetChanged();
                    }
                });
        studentAdapter.setOnclickliten(new PendingBookAdapter.Onclickliten() {
            @Override
            public void onclick(Pendingbookmodel booksmode) {
                Toast.makeText(StudentDetails.this,"Student Name: "+booksmode.getStudentname(),Toast.LENGTH_SHORT).show();
            }

            @Override
            public Boolean longpress(int position) {
                return true;
            }
        });
    }
}