package com.adrian.littlecancer;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import static com.adrian.littlecancer.MainListActivity.FRIENDS_NAME;
import static com.adrian.littlecancer.MainListActivity.MENU_EDIT;
import static com.adrian.littlecancer.MainListActivity.MENU_REMOVE;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.ViewHolder> {
    private ArrayList<Friend> friends;
    private Context context;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public TextView balance;
        public ImageView letter;

        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            balance = itemView.findViewById(R.id.balance);
            letter = itemView.findViewById(R.id.letter_view);
            itemView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
                @Override
                public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
                    contextMenu.add(Menu.NONE, MENU_EDIT, getAdapterPosition(), R.string.edit);
                    contextMenu.add(Menu.NONE, MENU_REMOVE, getAdapterPosition(), R.string.remove);
                }
            });
        }

        private MenuItem.OnMenuItemClickListener onMenuClick = new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                return true;
            }
        };
    }

    public FriendAdapter(ArrayList<Friend> friends, Context context) {
        this.friends = friends;
        this.context = context;
    }

    @Override public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_list_item, parent, false);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = ((TextView)view.findViewById(R.id.name)).getText().toString();
                Intent intent = new Intent(parent.getContext(), TransactionListActivity.class);
                intent.putExtra(FRIENDS_NAME, name);
                parent.getContext().startActivity(intent);
            }
        });
        return new ViewHolder(v);
    }

    @Override public void onBindViewHolder(ViewHolder holder, int position) {
        holder.name.setText(friends.get(position).getName());
        NumberFormat format = NumberFormat.getCurrencyInstance(Locale.getDefault());
        float balance = friends.get(position).getBalance();

        if(balance > 0.01) holder.balance.setTextColor(Color.parseColor("#4CAF50"));
        else if (balance < -0.01) holder.balance.setTextColor(Color.parseColor("#F44336"));
        else holder.balance.setTextColor(Color.parseColor("#757575"));

        holder.balance.setText(format.format(friends.get(position).getBalance()));

        ColorGenerator generator = ColorGenerator.DEFAULT;
        int color = generator.getColor(holder.name.getText());
        TextDrawable drawable = TextDrawable.builder().buildRound(String.valueOf(holder.name.getText().charAt(0)), color);
        holder.letter.setImageDrawable(drawable);
    }

    @Override public int getItemCount() {
        return friends.size();
    }

    public void update(ArrayList<Friend> friends) {
        this.friends = friends;
        notifyDataSetChanged();
    }
//
//    public void remove(ViewModel item) {
//        int position = items.indexOf(item);
//        items.remove(position);
//        notifyItemRemoved(position);
//    }
}
