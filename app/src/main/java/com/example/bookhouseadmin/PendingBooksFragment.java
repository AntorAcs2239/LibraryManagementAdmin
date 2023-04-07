package com.example.bookhouseadmin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bookhouseadmin.databinding.FragmentPendingBooksBinding;
import com.example.bookhouseadmin.databinding.FragmentRequestForOrderBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PendingBooksFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PendingBooksFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PendingBooksFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PendingBooksFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PendingBooksFragment newInstance(String param1, String param2) {
        PendingBooksFragment fragment = new PendingBooksFragment();
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
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    FragmentPendingBooksBinding binding;
    String dept,name,roll;
    PendingBookAdapter pendingBookAdapter;
    ArrayList<Pendingbookmodel>list;
    ProgressDialog progressDialog;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPendingBooksBinding.inflate(inflater, container, false);
        firebaseFirestore=FirebaseFirestore.getInstance();
        list=new ArrayList<>();
        pendingBookAdapter=new PendingBookAdapter(getContext(),list);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext(), RecyclerView.VERTICAL,false);
        binding.pendinglist.setAdapter(pendingBookAdapter);
        binding.pendinglist.setLayoutManager(linearLayoutManager);
        progressDialog=new ProgressDialog(getContext());
        progressDialog.setMessage("Fetching Info...");
        Query query=firebaseFirestore.collection("PendingBook")
                .orderBy("timestamp", Query.Direction.ASCENDING);
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
        binding.pastrecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(),PastRecord.class));
            }
        });
//        firebaseFirestore.collection("PendingBook")
//                .addSnapshotListener(new EventListener<QuerySnapshot>() {
//                    @Override
//                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
//
//                    }
//                });

        pendingBookAdapter.setOnclickliten(new PendingBookAdapter.Onclickliten() {
            @Override
            public void onclick(Pendingbookmodel booksmode) {
                progressDialog.show();
                Intent intent=new Intent(getContext(),PendingbookDetails.class);
                firebaseFirestore.collection(booksmode.getCatagory())
                        .document(booksmode.getBookid())
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                String img=documentSnapshot.toObject(BooksDetails.class).getImage();
                                progressDialog.dismiss();
                                intent.putExtra("studentname",booksmode.getStudentname());
                                intent.putExtra("studentid",booksmode.getStudentid());
                                intent.putExtra("img",img);
                                intent.putExtra("writername",booksmode.getWritername());
                                intent.putExtra("bookname",booksmode.getBookname());
                                intent.putExtra("pendingid",booksmode.getPendingid());
                                intent.putExtra("bookid",booksmode.getBookid());
                                intent.putExtra("catagory",booksmode.getCatagory());
                                intent.putExtra("serial",booksmode.getBookserial());
                                intent.putExtra("registration",booksmode.getRegistration());
                                intent.putExtra("phone",booksmode.getStudentphone());
                                startActivity(intent);
                            }
                        });
            }
            @Override
            public Boolean longpress(int position) {
                return null;
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
        return binding.getRoot();
    }
    private void filterlist(String toString) {
        ArrayList<Pendingbookmodel>tem=new ArrayList<>();
        for (Pendingbookmodel obj : list) {
            if (obj.getStudentphone()!=null&&obj.getStudentname()!=null) {
                if (obj.getStudentname().toLowerCase().contains(toString.toLowerCase()) || obj.getStudentphone().toLowerCase().contains(toString.toLowerCase())) {
                    tem.add(obj);
                }
            }
        }
        pendingBookAdapter.filter(tem);
        pendingBookAdapter.notifyDataSetChanged();
    }
}