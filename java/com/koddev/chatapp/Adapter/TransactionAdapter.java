package com.koddev.chatapp.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.koddev.chatapp.Model.Transaction;
import com.koddev.chatapp.R;

import java.util.List;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.ImageViewHolder> {
    private Context context;
    private List<Transaction> transactionList;

    public TransactionAdapter(Context context, List<Transaction> transaction) {
        this.context = context;
        this.transactionList = transaction;
    }

    @NonNull
    @Override
    public TransactionAdapter.ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.transaction, parent,false);
        return  new ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionAdapter.ImageViewHolder holder, int position) {
        Transaction transaction = transactionList.get(position);

        Log.d("BOOKLIST HOLDER: ", "" + transaction.getBooks());
        holder.dateLbl.setText(transaction.getDate());
        holder.bookList.setText(transaction.getBooks());
        holder.priceLbl.setText(transaction.getTotal());

    }

    @Override
    public int getItemCount() {
        return transactionList.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder{

        private TextView dateLbl, bookList, priceLbl;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);

            dateLbl = itemView.findViewById(R.id.dateTxt);
            bookList = itemView.findViewById(R.id.bookTxt);
            priceLbl = itemView.findViewById(R.id.priceTxt);

        }
    }
}
