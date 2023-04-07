package com.example.bookhouseadmin;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.PatternMatcher;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class PastRecord extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<Pendingbookmodel>list;
    PendingBookAdapter pendingBookAdapter;
    FirebaseFirestore firestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_record);
        list=new ArrayList<>();
        pendingBookAdapter=new PendingBookAdapter(PastRecord.this,list);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        recyclerView=findViewById(R.id.pastrecord);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(pendingBookAdapter);
        firestore=FirebaseFirestore.getInstance();
        Query query=firestore.collection("Past Record")
                .orderBy("timestamp", Query.Direction.DESCENDING);
        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        list.clear();
                        for (DocumentSnapshot snapshot:value.getDocuments())
                        {
                            Pendingbookmodel pendingbookmodel=snapshot.toObject(Pendingbookmodel.class);
                            list.add(pendingbookmodel);
                        }
                        pendingBookAdapter.notifyDataSetChanged();
                    }
                });
        pendingBookAdapter.setOnclickliten(new PendingBookAdapter.Onclickliten() {
            @Override
            public void onclick(Pendingbookmodel booksmode) {
                Toast.makeText(PastRecord.this,booksmode.getBookname(),Toast.LENGTH_SHORT).show();
            }
            @Override
            public Boolean longpress(int position) {
                return true;
            }
        });
    }
}