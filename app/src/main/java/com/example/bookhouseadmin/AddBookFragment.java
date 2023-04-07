package com.example.bookhouseadmin;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.bookhouseadmin.databinding.FragmentAddBookActivityBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddBookFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddBookFragment extends Fragment{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AddBookFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddBookActivity.
     */
    // TODO: Rename and change types and number of parameters
    public static AddBookFragment newInstance(String param1, String param2) {
        AddBookFragment fragment = new AddBookFragment();
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
    FragmentAddBookActivityBinding binding;
    FirebaseFirestore firestore;
    ActivityResultLauncher<String> launcher;
    FirebaseStorage storage;
    String bookname,writename,catagory;
    Spinner spinner;
    int numofbook;
    Uri uri;
    int t=1;
    ProgressDialog progressDialog;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentAddBookActivityBinding.inflate(inflater,container,false);
        firestore= FirebaseFirestore.getInstance();
        storage=FirebaseStorage.getInstance();
        progressDialog=new ProgressDialog(getContext());
        progressDialog.setMessage("Uploading Please Wait");
        launcher=registerForActivityResult(new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri result) {
                        binding.bookimg.setImageURI(result);
                        uri=result;
                    }
                });
        binding.bookimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launcher.launch("image/*");
            }
        });
        binding.upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (uri==null)
                {
                    Toast.makeText(getContext(),"Please Select Image",Toast.LENGTH_SHORT).show();
                }
               else if (catagory.isEmpty())
                {
                    Toast.makeText(getContext(),"Please Select Catagory",Toast.LENGTH_SHORT).show();
                }
               else if (binding.bookname.getText().toString().isEmpty())
                {
                    Toast.makeText(getContext(),"Please Select Book Name",Toast.LENGTH_SHORT).show();
                }
                else if (numofbook==0)
                {
                    Toast.makeText(getContext(),"Please Select Number of Book",Toast.LENGTH_SHORT).show();
                }
                else if (binding.writername.getText().toString().isEmpty())
                {
                    Toast.makeText(getContext(),"Please Select Writer Name",Toast.LENGTH_SHORT).show();
                }
                else if (binding.edition.getText().toString().isEmpty())
                {
                    Toast.makeText(getContext(),"Please Select Edtion",Toast.LENGTH_SHORT).show();
                }
                else if (binding.description.getText().toString().isEmpty())
                {
                    Toast.makeText(getContext(),"Please Select short description",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    progressDialog.show();
                    bookname=binding.bookname.getText().toString();
                    writename=binding.writername.getText().toString();
                    upload(uri);
                }
            }
        });
        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(getContext(),R.array.catagory, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinner.setAdapter(adapter);
      // binding.spinner.setOnItemSelectedListener(this);
       binding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
           @Override
           public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
               catagory=adapterView.getItemAtPosition(i).toString();
           }

           @Override
           public void onNothingSelected(AdapterView<?> adapterView) {

           }
       });
        ArrayAdapter<CharSequence> adapter2=ArrayAdapter.createFromResource(getContext(),R.array.amountofbook, android.R.layout.simple_spinner_dropdown_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinner2.setAdapter(adapter2);
        // binding.spinner.setOnItemSelectedListener(this);
        binding.spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String t=adapterView.getItemAtPosition(i).toString();
                numofbook=Integer.parseInt(t);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        return binding.getRoot();
    }
    private void upload(Uri result) {
        final StorageReference storageReference=storage.getReference()
                .child(bookname);
        storageReference.putFile(result)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        storageReference.getDownloadUrl()
                                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        DocumentReference documentReference=firestore.collection(catagory).document();
                                        BooksDetails booksDetails=new BooksDetails(bookname,uri.toString(),writename,catagory,documentReference.getId(),true,numofbook,binding.edition.getText().toString(),binding.description.getText().toString(),numofbook);
                                        documentReference.set(booksDetails);
                                        progressDialog.dismiss();
                                        Toast.makeText(getContext(),"Uploaded Sucessfully",Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();
                                    }
                                });
                    }
                });
    }
}