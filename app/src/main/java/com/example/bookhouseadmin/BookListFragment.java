package com.example.bookhouseadmin;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bookhouseadmin.databinding.FragmentBookListBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BookListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BookListFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public BookListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BookListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BookListFragment newInstance(String param1, String param2) {
        BookListFragment fragment = new BookListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    BooksAdapter booksAdapter1,booksAdapter2,booksAdapter3,booksAdapter4,booksAdapter5,booksAdapter6,booksAdapter7,booksAdapter8;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    ArrayList<BooksDetails> list1;
    ArrayList<BooksDetails> list2;
    ArrayList<BooksDetails> list3;
    ArrayList<BooksDetails> list4;
    ArrayList<BooksDetails> list5;
    ArrayList<BooksDetails> list6;
    ArrayList<BooksDetails> list7;
    ArrayList<BooksDetails> list8;
    FragmentBookListBinding binding;
    String dept;
    int c=0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentBookListBinding.inflate(inflater, container, false);
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();
        list1=new ArrayList<>();
        list2=new ArrayList<>();
        list3=new ArrayList<>();
        list4=new ArrayList<>();
        list5=new ArrayList<>();
        list6=new ArrayList<>();
        list7=new ArrayList<>();
        list8=new ArrayList<>();
        booksAdapter1=new BooksAdapter(getContext(),list1);
        booksAdapter2=new BooksAdapter(getContext(),list2);
        booksAdapter3=new BooksAdapter(getContext(),list3);
        booksAdapter4=new BooksAdapter(getContext(),list4);
        booksAdapter5=new BooksAdapter(getContext(),list5);
        booksAdapter6=new BooksAdapter(getContext(),list6);
        booksAdapter7=new BooksAdapter(getContext(),list7);
        booksAdapter8=new BooksAdapter(getContext(),list8);

        setrecyclerview(booksAdapter1,binding.reccse);
        setrecyclerview(booksAdapter2,binding.receee);
        setrecyclerview(booksAdapter3,binding.reccv);
        setrecyclerview(booksAdapter4,binding.recgeneral);
        setrecyclerview(booksAdapter5,binding.recforbanga);
        setrecyclerview(booksAdapter6,binding.recforthesis);
        setrecyclerview(booksAdapter7,binding.recforothers);

        Bookcollectfromfirebase("CSE",list1,booksAdapter1);
        Bookcollectfromfirebase("EEE",list2,booksAdapter2);
        Bookcollectfromfirebase("Civil",list3,booksAdapter3);
        Bookcollectfromfirebase("General",list4,booksAdapter4);
        Bookcollectfromfirebase("Bangabondho",list5,booksAdapter5);
        Bookcollectfromfirebase("Thesis",list6,booksAdapter6);
        Bookcollectfromfirebase("Others",list7,booksAdapter7);

        binding.cse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(),AllBooksActivity.class);
                intent.putExtra("catagory","CSE");
                startActivity(intent);
            }
        });
        binding.eee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(),AllBooksActivity.class);
                intent.putExtra("catagory","EEE");
                startActivity(intent);
            }
        });
        binding.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(),AllBooksActivity.class);
                intent.putExtra("catagory","Civil");
                startActivity(intent);
            }
        });
        binding.general.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(),AllBooksActivity.class);
                intent.putExtra("catagory","General");
                startActivity(intent);
            }
        });
        binding.banga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(),AllBooksActivity.class);
                intent.putExtra("catagory","Bangabondho");
                startActivity(intent);
            }
        });
        binding.thesis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(),AllBooksActivity.class);
                intent.putExtra("catagory","Thesis");
                startActivity(intent);
            }
        });
        binding.others.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(),AllBooksActivity.class);
                intent.putExtra("catagory","Others");
                startActivity(intent);
            }
        });
        booksAdapter1.setOnclickliten(new BooksAdapter.Onclickliten() {
            @Override
            public void onclick(BooksDetails booksmode) {
              gotoalldetials(booksmode);
            }

            @Override
            public void longpress(BooksDetails booksmode) {
              gotobookdetails(booksmode);
            }
        });
        booksAdapter2.setOnclickliten(new BooksAdapter.Onclickliten() {
            @Override
            public void onclick(BooksDetails booksmode) {
                gotoalldetials(booksmode);
            }

            @Override
            public void longpress(BooksDetails booksmode) {
                gotobookdetails(booksmode);

            }
        });
        booksAdapter3.setOnclickliten(new BooksAdapter.Onclickliten() {
            @Override
            public void onclick(BooksDetails booksmode) {
                gotoalldetials(booksmode);
            }

            @Override
            public void longpress(BooksDetails booksmode) {
                gotobookdetails(booksmode);
            }
        });
        booksAdapter4.setOnclickliten(new BooksAdapter.Onclickliten() {
            @Override
            public void onclick(BooksDetails booksmode) {
                gotoalldetials(booksmode);
            }

            @Override
            public void longpress(BooksDetails booksmode) {
                gotobookdetails(booksmode);
            }
        });
        booksAdapter5.setOnclickliten(new BooksAdapter.Onclickliten() {
            @Override
            public void onclick(BooksDetails booksmode) {
                gotoalldetials(booksmode);
            }

            @Override
            public void longpress(BooksDetails booksmode) {
                gotobookdetails(booksmode);
            }
        });
        booksAdapter6.setOnclickliten(new BooksAdapter.Onclickliten() {
            @Override
            public void onclick(BooksDetails booksmode) {
                gotoalldetials(booksmode);
            }

            @Override
            public void longpress(BooksDetails booksmode) {
                gotobookdetails(booksmode);
            }
        });
        booksAdapter7.setOnclickliten(new BooksAdapter.Onclickliten() {
            @Override
            public void onclick(BooksDetails booksmode) {
                gotoalldetials(booksmode);
            }

            @Override
            public void longpress(BooksDetails booksmode) {
                gotobookdetails(booksmode);
            }
        });
        return binding.getRoot();
    }
    public void setrecyclerview(BooksAdapter booksAdapter,RecyclerView recyclerView)
    {
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext(),RecyclerView.HORIZONTAL,false);
        recyclerView.setAdapter(booksAdapter);
        recyclerView.setLayoutManager(linearLayoutManager);
    }
    public void Bookcollectfromfirebase(String catagory,ArrayList<BooksDetails>list,BooksAdapter booksAdapter)
    {
        firebaseFirestore.collection(catagory)
                .whereEqualTo("available", true)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        list.clear();
                        for (DocumentSnapshot snapshot : value.getDocuments()) {
                            BooksDetails booksmode=snapshot.toObject(BooksDetails.class);
                            booksmode.setBookid(snapshot.getId());
                            list.add(booksmode);
                        }
                        //Collections.sort(list,Booksmode.booksmodecomparator);
                        booksAdapter.notifyDataSetChanged();
                    }
                });
    }
    public void gotobookdetails(BooksDetails booksmode)
    {
        Intent intent=new Intent(getContext(),BookdetailsActivity.class);
        intent.putExtra("image",booksmode.getImage());
        intent.putExtra("bname",booksmode.getBookname());
        intent.putExtra("wname",booksmode.getWritername());
        intent.putExtra("numofbook",booksmode.getNumofbook());
        intent.putExtra("catagory",booksmode.getCatagory());
        intent.putExtra("bookid",booksmode.getBookid());
        intent.putExtra("totalnumberofbook",booksmode.getTotalnumofbook());
        startActivity(intent);
    }
    public void gotoalldetials(BooksDetails booksmode)
    {
        Intent intent=new Intent(getContext(),BookAllDetailsActivity.class);
        intent.putExtra("bookid",booksmode.getBookid());
        intent.putExtra("bookname",booksmode.getBookname());
        intent.putExtra("writername",booksmode.getWritername());
        intent.putExtra("edition",booksmode.getEdition());
        intent.putExtra("image",booksmode.getImage());
        startActivity(intent);
    }
}