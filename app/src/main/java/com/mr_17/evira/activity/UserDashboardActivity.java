package com.mr_17.evira.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.mr_17.evira.R;
import com.mr_17.evira.adapter.CategoryRecyclerViewAdapter;
import com.mr_17.evira.model.CategoryRecyclerViewModel;

import java.util.ArrayList;
import java.util.Calendar;

public class UserDashboardActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<CategoryRecyclerViewModel> list;
    private CategoryRecyclerViewAdapter.RecyclerViewClickListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dashboard);

        InitializeCategoryRecyclerView();
    }

    private void InitializeCategoryRecyclerView()
    {
        SetOnClickListener();

        recyclerView = findViewById(R.id.recycler_view);
        list = new ArrayList<>();

        CategoryRecyclerViewAdapter adapter = new CategoryRecyclerViewAdapter(list, this, listener);
        recyclerView.setAdapter(adapter);

        list.add(new CategoryRecyclerViewModel("Clothes", 5, R.drawable.icon_clothes));
        list.add(new CategoryRecyclerViewModel("Bags", 5, R.drawable.icon_bags));
        list.add(new CategoryRecyclerViewModel("Shoes", 5, R.drawable.shoes));
        list.add(new CategoryRecyclerViewModel("Electronics", 6, R.drawable.icon_clothes));
        list.add(new CategoryRecyclerViewModel("Jewellery", 5, R.drawable.icon_clothes));

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(gridLayoutManager);
    }

    private void SetOnClickListener()
    {
        listener = new CategoryRecyclerViewAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View v, int position)
            {

            }
        };
    }

    private String GetWishing()
    {
        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);

        if(timeOfDay < 12)
            return "Morning";
        else if(timeOfDay < 16)
            return "Afternoon";
        else if(timeOfDay < 21)
            return "Evening";
        else
            return "Night";
    }
}