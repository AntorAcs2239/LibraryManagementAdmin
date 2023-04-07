package com.example.bookhouseadmin;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.bookhouseadmin.databinding.ActivityBookAllDetailsBinding;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class BookAllDetailsActivity extends AppCompatActivity {
    TextView bname,wname,edition;
    ImageView image;
    RecyclerView recyclerView;
    ArrayList<Pendingbookmodel>list;
    PendingBookAdapter pendingBookAdapter;
    FirebaseFirestore firestore;
    String bookname,writername,bookedition,bookimg,bookid;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_all_details);
        bname=findViewById(R.id.BookNamedetail);
        wname=findViewById(R.id.writernamedetail);
        edition=findViewById(R.id.editiondetails);
        image=findViewById(R.id.BookImg);

        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Wait..");
        progressDialog.show();
        bookname=getIntent().getStringExtra("bookname");
        writername=getIntent().getStringExtra("writername");
        bookedition=getIntent().getStringExtra("edition");
        bookimg=getIntent().getStringExtra("image");
        bookid=getIntent().getStringExtra("bookid");

        recyclerView=findViewById(R.id.detailsrec);
        list=new ArrayList<>();
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this,RecyclerView.HORIZONTAL,false);
        pendingBookAdapter=new PendingBookAdapter(this,list);
        recyclerView.setAdapter(pendingBookAdapter);
        recyclerView.setLayoutManager(linearLayoutManager);

        firestore=FirebaseFirestore.getInstance();

        bname.setText("Book Name: "+bookname);
        wname.setText("Writer Name: "+writername);
        edition.setText("Edition: "+bookedition);
        Glide.with(this).load(bookimg).into(image);


        firestore.collection("PendingBook")
                .whereEqualTo("bookid",bookid)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        list.clear();
                        for (DocumentSnapshot snapshot:value.getDocuments())
                        {
                            Pendingbookmodel pendingbookmodel=snapshot.toObject(Pendingbookmodel.class);
                            list.add(pendingbookmodel);
                        }
                        pendingBookAdapter.notifyDataSetChanged();
                        progressDialog.dismiss();
                    }
                });
    }
}