package com.example.bookhouseadmin;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;

import com.example.bookhouseadmin.databinding.ActivityAllBooksBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class AllBooksActivity extends AppCompatActivity {
    ActivityAllBooksBinding binding;
    FirebaseFirestore firestore;
    ArrayList<BooksDetails> list;
    FirebaseAuth firebaseAuth;
    BooksAdapter adapter;
    String collection;
    ProgressDialog progressDialog;
    int c;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_books);
        binding= ActivityAllBooksBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        list=new ArrayList<>();
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Books Loading...");
        firebaseAuth=FirebaseAuth.getInstance();
        firestore=FirebaseFirestore.getInstance();
        collection=getIntent().getStringExtra("catagory");
        adapter=new BooksAdapter(this,list);
        GridLayoutManager gridLayoutManager=new GridLayoutManager(getApplicationContext(),2, RecyclerView.VERTICAL,false);
        binding.recycler.setAdapter(adapter);
        binding.recycler.setLayoutManager(gridLayoutManager);
        progressDialog.show();
        firestore.collection(collection)
                .whereEqualTo("available",true)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        for (DocumentSnapshot snapshot:value.getDocuments())
                        {
                            BooksDetails booksmode=snapshot.toObject(BooksDetails.class);
                            list.add(booksmode);
                        }
                        adapter.notifyDataSetChanged();
                        progressDialog.dismiss();
                    }
                });
        binding.search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //booksAdapter.getFilter().filter(charSequence);
            }
            @Override
            public void afterTextChanged(Editable editable) {
                filterlist(editable.toString());
            }
        });
        adapter.setOnclickliten(new BooksAdapter.Onclickliten() {
            @Override
            public void onclick(BooksDetails booksmode) {
                Intent intent=new Intent(AllBooksActivity.this,BookAllDetailsActivity.class);
                intent.putExtra("bookid",booksmode.getBookid());
                intent.putExtra("bookname",booksmode.getBookname());
                intent.putExtra("writername",booksmode.getWritername());
                intent.putExtra("edition",booksmode.getEdition());
                intent.putExtra("image",booksmode.getImage());
                startActivity(intent);
            }
            @Override
            public void longpress(BooksDetails booksmode) {
                Intent intent=new Intent(AllBooksActivity.this,BookdetailsActivity.class);
                intent.putExtra("bookid",booksmode.getBookid());
                intent.putExtra("image",booksmode.getImage());
                intent.putExtra("bname",booksmode.getBookname());
                intent.putExtra("wname",booksmode.getWritername());
                intent.putExtra("numofbook",booksmode.getNumofbook());
                intent.putExtra("catagory",booksmode.getCatagory());
                startActivity(intent);
                finish();
            }
        });
    }
    private void filterlist(String toString) {
        ArrayList<BooksDetails>tem=new ArrayList<>();
        for (BooksDetails obj : list) {
            if (obj.getWritername()!=null&&obj.getBookname()!=null) {
                if (obj.getBookname().toLowerCase().contains(toString.toLowerCase()) || obj.getWritername().toLowerCase().contains(toString.toLowerCase())) {
                    tem.add(obj);
                }
            }
        }
        adapter.filter(tem);
        adapter.notifyDataSetChanged();
    }
}