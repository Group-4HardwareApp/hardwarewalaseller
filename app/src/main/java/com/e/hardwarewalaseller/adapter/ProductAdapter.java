package com.e.hardwarewalaseller.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.e.hardwarewalaseller.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.e.hardwarewalaseller.beans.Product;
import com.e.hardwarewalaseller.databinding.ProductItemListBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder>{

    Context context;
    ArrayList<Product> productList;
    onRecyclerViewClick listener;
    public ProductAdapter(Context context,ArrayList<Product> productList)
    {
        this.context = context;
        this.productList = productList;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
        public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        ProductItemListBinding productItemListBinding = ProductItemListBinding.inflate(LayoutInflater.from(context),parent,false);
        return new ProductViewHolder(productItemListBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product p = productList.get(position);
        Picasso.get().load(p.getImageUrl()).placeholder(R.drawable.nm).into(holder.productItemListBinding.productimg);
        /*holder.productItemListBinding.productname.setText("Product :"+p.getName());
        holder.productItemListBinding.brandname.setText("Brand:"+p.getBrand());
        holder.productItemListBinding.quantityinstock.setText("Quantity : "+p.getQtyInStock()+" pcs");
*/
        holder.productItemListBinding.productname.setText(""+p.getName());
            //commented ""ordered by chief
        /*
        holder.productItemListBinding.brandname.setText(""+p.getBrand());
        holder.productItemListBinding.quantityinstock.setText(""+p.getQtyInStock()+" pcs");
*/


    }

    @Override
    public int getItemCount() {
        return productList.size();
//        return productList == null ? 0 : productList.size();
    }


    public class ProductViewHolder extends RecyclerView.ViewHolder
{
    ProductItemListBinding productItemListBinding;
    public ProductViewHolder(ProductItemListBinding productItemListBinding) {
        super(productItemListBinding.getRoot());

        this.productItemListBinding = productItemListBinding;
            productItemListBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if(position!=RecyclerView.NO_POSITION && listener != null)
                    {
                        Product p = productList.get(position);
                        listener.onItemClick(p,position);
                    }
                }
            });

    }
}



public interface onRecyclerViewClick
{
    public void onItemClick(Product p, int position);

}
public void setOnItemClickListener(onRecyclerViewClick listener)
{
    this.listener = listener;
}


}
