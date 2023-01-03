package com.mr_17.evira.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mr_17.evira.R;
import com.mr_17.evira.model.ProductsListRecyclerViewModel;
import com.mr_17.evira.model.UsersListRecyclerViewModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class UsersListRecyclerViewAdapter extends RecyclerView.Adapter<UsersListRecyclerViewAdapter.viewHolder>
{
    public RecyclerViewClickListener listener;

    ArrayList<UsersListRecyclerViewModel> list;
    Context context;

    public UsersListRecyclerViewAdapter(ArrayList<UsersListRecyclerViewModel> list, Context context, RecyclerViewClickListener listener) {
        this.list = list;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        view = LayoutInflater.from(context).inflate(R.layout.users_list_recycler_view, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {

        UsersListRecyclerViewModel model = list.get(position);
        holder.fullName.setText(model.getFirstName() + " " + model.getLastName());
        holder.username.setText(model.getUsername());
        if(!model.getProfilePicURL().isEmpty())
        Picasso.get().load(model.getProfilePicURL()).into(holder.profileImage);
    }

    @Override
    public int getItemCount() {

        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        TextView fullName, username;
        CircleImageView profileImage;

        public viewHolder(@NonNull View itemView) {
            super(itemView);

            fullName = itemView.findViewById(R.id.full_name);
            username = itemView.findViewById(R.id.username);
            profileImage = itemView.findViewById(R.id.profile_image);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onClick(v, getAdapterPosition());
        }
    }

    public interface RecyclerViewClickListener
    {
        void onClick(View v, int position );
    }
}

