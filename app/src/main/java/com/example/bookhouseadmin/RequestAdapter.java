package com.example.bookhouseadmin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.viewholde>{

    Context context;
    ArrayList<RequestModelforreq> list;
    ArrayList<RequestModelforreq> filterdata;
    public Onclickliten onclickliten;
    public interface Onclickliten
    {
        void onclick(RequestModelforreq booksmode);
        Boolean longpress(int position);
    }
    public void setOnclickliten(Onclickliten onclickliten) {
        this.onclickliten = onclickliten;
    }
    public RequestAdapter(Context context, ArrayList<RequestModelforreq> list) {
        this.context = context;
        this.list = list;
        this.filterdata=list;
    }
    //    @Override
//    public Filter getFilter() {
//        return new Filter() {
//            @Override
//            protected FilterResults performFiltering(CharSequence charSequence) {
//                String key=charSequence.toString();
//                if(key.isEmpty())
//                {
//                    list=filterdata;
//                }
//                else
//                {
//                    ArrayList<Booksmode>tem=new ArrayList<>();
//                    for (Booksmode booksmode:list)
//                    {
//                        if (booksmode.getBookname().toLowerCase().contains(key.toLowerCase()))
//                        {
//                            tem.add(booksmode);
//                        }
//                    }
//                    filterdata=tem;
//                }
//                FilterResults filterResults=new FilterResults();
//                filterResults.values=filterdata;
//                list=filterdata;
//                return  filterResults;
//            }
//            @Override
//            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
//                list.clear();
//                list=(ArrayList<Booksmode>)filterResults.values;
//                notifyDataSetChanged();
//            }
//        };
//    }
    @NonNull
    @Override
    public viewholde onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.rowfororderrequest,parent,false);
        return new viewholde(view,onclickliten);
    }
    @Override
    public void onBindViewHolder(@NonNull viewholde holder, int position) {
        RequestModelforreq requestModel=list.get(position);
        holder.studentname.setText("Student Phone: "+requestModel.getPhone());
        holder.bookname.setText("Book Name: "+requestModel.getBookname());
        holder.writename.setText("Writer Name: "+requestModel.getWritername());
        holder.registration.setText("Registration NO: "+requestModel.getRegistration());


        Glide.with(context).load(requestModel.getBoookimg()).into(holder.bookimg);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onclickliten.onclick(requestModel);
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                onclickliten.longpress(position);
                return true;
            }
        });
    }
    @Override
    public int getItemCount() {
        return list.size();
    }
    public class viewholde extends RecyclerView.ViewHolder {
        ImageView bookimg;
        TextView bookname,writename,studentname,registration;
        public viewholde(@NonNull View itemView,Onclickliten onclickliten) {
            super(itemView);
            bookimg=itemView.findViewById(R.id.img);
            bookname=itemView.findViewById(R.id.bname);
            writename=itemView.findViewById(R.id.wname);
            studentname=itemView.findViewById(R.id.studentname);
            registration=itemView.findViewById(R.id.registration);
            // itemView.setOnClickListener(v->onclickliten.onclick(getAdapterPosition()));
            //itemView.setOnLongClickListener(v->onclickliten.longpress(getAdapterPosition()));
        }
    }
    public void filter(ArrayList<RequestModelforreq>backup)
    {
        list=backup;
        notifyDataSetChanged();
    }
}
