package com.example.bookhouseadmin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.bookhouseadmin.databinding.FragmentRequestForOrderBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RequestForOrderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RequestForOrderFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public RequestForOrderFragment() {
        // Required empty public constructor
    }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RequestForOrderFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RequestForOrderFragment newInstance(String param1, String param2) {
        RequestForOrderFragment fragment = new RequestForOrderFragment();
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
    ArrayList<RequestModelforreq> listreq;
    ArrayList<RequestModelforreq> list2=new ArrayList<>();
    FragmentRequestForOrderBinding binding;
    String dept,name,roll;
    RequestAdapter requestAdapter;
    ProgressDialog progressDialog;
    int numofbookstudenthave;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRequestForOrderBinding.inflate(inflater, container, false);
        firebaseFirestore=FirebaseFirestore.getInstance();
        listreq=new ArrayList<>();
        requestAdapter=new RequestAdapter(getContext(),listreq);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext(), RecyclerView.VERTICAL,false);
        binding.request.setAdapter(requestAdapter);
        progressDialog=new ProgressDialog(getContext());
        progressDialog.setMessage("Fetching Info...");
        binding.request.setLayoutManager(linearLayoutManager);
        firebaseFirestore.collection("RequestQ")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        listreq.clear();
                        for (DocumentSnapshot snapshot:value.getDocuments())
                        {
                            RequestModelforreq requestModel=snapshot.toObject(RequestModelforreq.class);
                            listreq.add(requestModel);
                        }
                        requestAdapter.notifyDataSetChanged();
                    }
                });
        requestAdapter.setOnclickliten(new RequestAdapter.Onclickliten() {
            @Override
            public void onclick(RequestModelforreq booksmode) {
                progressDialog.show();
                firebaseFirestore.collection(booksmode.getCatagory())
                        .document(booksmode.getBookid())
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                int num=documentSnapshot.toObject(BooksDetails.class).getNumofbook();
                                firebaseFirestore.collection("StudentRecord")
                                        .document(booksmode.getStudentid())
                                        .get()
                                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                            @Override
                                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                numofbookstudenthave=documentSnapshot.toObject(StudentModel.class).getNumofbook();
                                                Intent intent=new Intent(getContext(),RequestDetialsActivity.class);
                                                intent.putExtra("bookid",booksmode.getBookid());
                                                intent.putExtra("catagory",booksmode.getCatagory());
                                                intent.putExtra("studentid",booksmode.getStudentid());
                                                intent.putExtra("studentname",booksmode.getStudentname());
                                                intent.putExtra("bookname",booksmode.getBookname());
                                                intent.putExtra("writername",booksmode.getWritername());
                                                intent.putExtra("bookimg",booksmode.getBoookimg());
                                                intent.putExtra("requestid",booksmode.getRequestid());
                                                intent.putExtra("numofbook",num);
                                                intent.putExtra("phone",booksmode.getPhone());

                                                intent.putExtra("description",booksmode.getDescription());
                                                intent.putExtra("registration",booksmode.getRegistration());

                                                intent.putExtra("numofbookstudenthave",numofbookstudenthave);
                                                startActivity(intent);
                                                progressDialog.dismiss();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(getContext(),"Failed fatching",Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(),"Failed fatching",Toast.LENGTH_SHORT).show();
                    }
                });
            }
            @Override
            public Boolean longpress(int position) {
                return true;
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
        ArrayList<RequestModelforreq> tem = new ArrayList<>();
        for (RequestModelforreq obj : listreq) {
            if (obj.getRegistration()!=null&&obj.getPhone()!=null)
            {
                if (obj.getRegistration().toLowerCase().contains(toString.toLowerCase())||obj.getPhone().toLowerCase().contains(toString.toLowerCase()))
                {
                    tem.add(obj);
                }
            }
        }
        requestAdapter.filter(tem);
        requestAdapter.notifyDataSetChanged();
    }
}