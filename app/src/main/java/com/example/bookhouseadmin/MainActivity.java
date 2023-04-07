package com.example.bookhouseadmin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.bookhouseadmin.databinding.ActivityMainBinding;

import me.ibrahimsn.lib.OnItemSelectedListener;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        binding= ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        FragmentTransaction transactio=getSupportFragmentManager().beginTransaction();
        binding.bottomBar.setItemActiveIndex(1);
        transactio.replace(R.id.frame,new BookListFragment());
        transactio.commit();
        binding.bottomBar.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public boolean onItemSelect(int i) {
                FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();

                switch (i)
                {
                    case 0:
                        transaction.replace(R.id.frame,new AddBookFragment());
                        break;
                    case 1:
                        transaction.replace(R.id.frame,new BookListFragment());
                        break;
                    case 2:
                        transaction.replace(R.id.frame,new RequestForOrderFragment());
                        break;
                    case 3:
                        transaction.replace(R.id.frame,new PendingBooksFragment());
                        break;
                    case 4:
                        transaction.replace(R.id.frame,new StudentlistFragment());
                        break;
                }
                transaction.commit();
                return true;
            }
        });
    }
}