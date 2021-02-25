package com.e.hardwarewalaseller.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.e.hardwarewalaseller.R;
import com.e.hardwarewalaseller.beans.ItemList;
import com.e.hardwarewalaseller.databinding.ActivityPackageHistoryBinding;
import com.e.hardwarewalaseller.databinding.ImagezoomBinding;
import com.e.hardwarewalaseller.databinding.OrderHistoryItemListBinding;
import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.OrderHistoryViewHolder>{


    Context context;
    ArrayList<ItemList> orderItemsList;



    public OrderHistoryAdapter(Context context,ArrayList<ItemList> orderItemsList)
    {
        this.context = context;
        this.orderItemsList = orderItemsList;
    }

    @NonNull
    @Override
    public OrderHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        OrderHistoryItemListBinding orderHistoryItemListBinding = OrderHistoryItemListBinding.inflate(LayoutInflater.from(context),parent,false);
        return new OrderHistoryViewHolder(orderHistoryItemListBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderHistoryViewHolder holder, int position) {

        ItemList i = orderItemsList.get(position);

        holder.orderHistoryItemListBinding.tvAmount.setText("₹ "+i.getTotal()+" /-");
//        holder.orderHistoryItemListBinding.tvOrderItemId.setText(""+i.getOrderItemId());
        holder.orderHistoryItemListBinding.tvProductId.setText(""+i.getProductId());
        holder.orderHistoryItemListBinding.tvProductName.setText(""+i.getName());
        holder.orderHistoryItemListBinding.tvQuantity.setText(""+i.getQty()+" units");
        Picasso.get().load(i.getImageUrl()).placeholder(R.drawable.nm).into(holder.orderHistoryItemListBinding.productimage);
        holder.orderHistoryItemListBinding.productimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* AlertDialog.Builder ab =new AlertDialog.Builder(context);
                ab.setTitle("ZOOM");

                ImagezoomBinding imagezoomBinding = ImagezoomBinding.inflate(LayoutInflater.from(context));
                ab.setView(R.layout.imagezoom);
                Picasso.get().load(i.getImageUrl()).into(imagezoomBinding.photoview);
                AlertDialog mDialog = ab.create();
                mDialog.show();

*/

                AlertDialog.Builder mBuilder = new AlertDialog.Builder(context);

                View mView = LayoutInflater.from(context).inflate(R.layout.imagezoom, null);
                PhotoView photoView = mView.findViewById(R.id.photoview);
                Picasso.get().load(i.getImageUrl())
                        .placeholder(R.drawable.nm)
                        .into(photoView);
                mBuilder.setView(mView);
                AlertDialog mDialog = mBuilder.create();
                mDialog.show();

            }
        });
    }

    @Override
    public int getItemCount() {
        return orderItemsList.size();
    }


    public class OrderHistoryViewHolder extends RecyclerView.ViewHolder
    {

        OrderHistoryItemListBinding orderHistoryItemListBinding;

        public OrderHistoryViewHolder(OrderHistoryItemListBinding orderHistoryItemListBinding) {
            super(orderHistoryItemListBinding.getRoot());
            this.orderHistoryItemListBinding =orderHistoryItemListBinding;
        }
    }
}
