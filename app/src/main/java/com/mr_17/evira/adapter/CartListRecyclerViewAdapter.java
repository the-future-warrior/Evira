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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CartListRecyclerViewAdapter extends RecyclerView.Adapter<CartListRecyclerViewAdapter.viewHolder>
{
    public RecyclerViewClickListener listener;

    ArrayList<ProductsListRecyclerViewModel> list;
    Context context;

    public CartListRecyclerViewAdapter(ArrayList<ProductsListRecyclerViewModel> list, Context context, RecyclerViewClickListener listener) {
        this.list = list;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        view = LayoutInflater.from(context).inflate(R.layout.cart_list_recycler_view, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {

        ProductsListRecyclerViewModel model = list.get(position);
        holder.productName.setText(model.getProductName());
        holder.productPrice.setText(model.getProductPrice());
        holder.productCategory.setText(model.getProductCategory());
        Picasso.get().load(model.getImageUrl()).into(holder.image);
    }

    @Override
    public int getItemCount() {

        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        TextView productName, productPrice, productCategory;
        ImageView image, delete;

        public viewHolder(@NonNull View itemView) {
            super(itemView);

            productPrice = itemView.findViewById(R.id.product_price);
            productName = itemView.findViewById(R.id.product_name);
            productCategory = itemView.findViewById(R.id.product_category);
            image = itemView.findViewById(R.id.product_image);
            delete = itemView.findViewById(R.id.delete_button);

            delete.setOnClickListener(this);
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

