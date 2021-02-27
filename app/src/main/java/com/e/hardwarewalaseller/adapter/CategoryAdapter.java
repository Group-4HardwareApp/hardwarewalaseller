package com.e.hardwarewalaseller.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.e.hardwarewalaseller.beans.Category;
import com.e.hardwarewalaseller.databinding.CategoryItemListBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>{
    Context context;
    ArrayList<Category> categoryList;
    onRecyclerViewClick listener;

    public CategoryAdapter(Context context, ArrayList<Category> categoryList)
    {
        this.context = context;
        this.categoryList = categoryList;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CategoryItemListBinding categoryItemListBinding = CategoryItemListBinding.inflate(LayoutInflater.from(context),parent,false);
        return new CategoryViewHolder(categoryItemListBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {

        Category c = categoryList.get(position);
        Picasso.get().load(c.getImageUrl()).into(holder.categoryItemListBinding.categoryimg);
        holder.categoryItemListBinding.categoryname.setText(c.getCategoryName());

    }

    @Override
    public int getItemCount() {
        return categoryList.size();

    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder
    {

        CategoryItemListBinding categoryItemListBinding;
        public CategoryViewHolder(CategoryItemListBinding categoryItemListBinding)
         {
            super(categoryItemListBinding.getRoot());
            this.categoryItemListBinding = categoryItemListBinding;


             categoryItemListBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View view) {
                     int position = getAdapterPosition();
                     if(position !=RecyclerView.NO_POSITION && listener != null)
                     {
                         Category c = categoryList.get(position);
                         listener.onItemClick(c,position);
                     }
                 }
             });
        }
    }


    public interface onRecyclerViewClick
    {
        public void onItemClick(Category c, int position);

    }
    public void setOnItemClickListener(CategoryAdapter.onRecyclerViewClick listener)
    {
        this.listener = listener;
    }







}
