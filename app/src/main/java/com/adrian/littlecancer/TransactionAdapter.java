package com.adrian.littlecancer;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by adrian on 25.09.17.
 */

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.ViewHolder> {
        private ArrayList<Transaction> transactions;

        public static class ViewHolder extends RecyclerView.ViewHolder {
            public TextView value;
            public TextView note;
            public ImageView sign;

            public ViewHolder(View itemView) {
                super(itemView);
                value = itemView.findViewById(R.id.name);
                note = itemView.findViewById(R.id.balance);
                sign = itemView.findViewById(R.id.letter_view);
            }
        }

        public TransactionAdapter(ArrayList<Transaction> transactions) {
            this.transactions = transactions;
        }

        @Override
        public TransactionAdapter.ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_list_item, parent, false);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String name = ((TextView)view.findViewById(R.id.name)).getText().toString();
//                    Intent intent = new Intent(parent.getContext(), TransactionListActivity.class);
//                    intent.putExtra(FRIENDS_NAME, name);
//                    parent.getContext().startActivity(intent);
                }
            });

            v.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    return false;
                }
            });
            return new TransactionAdapter.ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(TransactionAdapter.ViewHolder holder, int position) {
            holder.note.setText(transactions.get(position).getNote());
            NumberFormat format = NumberFormat.getCurrencyInstance(Locale.getDefault());
            float balance = transactions.get(position).getValue();

            String sign = "+";
            int color;

            if(balance > 0) {
                color = Color.parseColor("#4CAF50");
            }
            else {
                color = Color.parseColor("#F44336");
                sign = "-";
            }

            holder.value.setTextColor(color);
            holder.value.setText(format.format(transactions.get(position).getValue()));

            TextDrawable drawable = TextDrawable.builder().buildRound(sign, color);
            holder.sign.setImageDrawable(drawable);
        }

        @Override public int getItemCount() {
            return transactions.size();
        }

    public void update(ArrayList<Transaction> transactions) {
        this.transactions = transactions;
        notifyDataSetChanged();
    }
//
//    public void remove(ViewModel item) {
//        int position = items.indexOf(item);
//        items.remove(position);
//        notifyItemRemoved(position);
//    }
}
