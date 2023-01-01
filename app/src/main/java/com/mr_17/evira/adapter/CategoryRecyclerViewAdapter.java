package com.mr_17.evira.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.mr_17.evira.R;
import com.mr_17.evira.model.CategoryRecyclerViewModel;

import java.util.ArrayList;

public class CategoryRecyclerViewAdapter extends RecyclerView.Adapter<CategoryRecyclerViewAdapter.viewHolder>
{
    public RecyclerViewClickListener listener;

    ArrayList<CategoryRecyclerViewModel> list;
    Context context;

    boolean alignLeft = false;

    public CategoryRecyclerViewAdapter(ArrayList<CategoryRecyclerViewModel> list, Context context, RecyclerViewClickListener listener) {
        this.list = list;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        view = LayoutInflater.from(context).inflate(R.layout.products_category_recycler_view, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {

        CategoryRecyclerViewModel model = list.get(position);
        alignLeft = position % 2 == 0;
        holder.categoryName.setText(model.getCategoryName());
        holder.categoryProductCount.setText(model.getCategoryProductCount() + " Products");
        holder.backgroundImg.setImageDrawable(context.getResources().getDrawable(model.getBackgroundImg()));
    }

    @Override
    public int getItemCount() {

        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        CardView cardView;
        TextView categoryName, categoryProductCount;
        ImageView backgroundImg;

        public viewHolder(@NonNull View itemView) {
            super(itemView);

            /*if(alignLeft)
            {
                cardView = itemView.findViewById(R.id.left_aligned);
                cardView.setVisibility(View.VISIBLE);

                categoryName = itemView.findViewById(R.id.left_category_name);
                categoryProductCount = itemView.findViewById(R.id.left_category_product_count);
                backgroundImg = itemView.findViewById(R.id.left_background_img);
            }
            else
            {*/
                cardView = itemView.findViewById(R.id.right_aligned);
                cardView.setVisibility(View.VISIBLE);

                categoryName = itemView.findViewById(R.id.right_category_name);
                categoryProductCount = itemView.findViewById(R.id.right_category_product_count);
                backgroundImg = itemView.findViewById(R.id.right_background_img);
            //}
            cardView.setOnClickListener(this);
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

