package com.e.hardwarewalaseller.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.e.hardwarewalaseller.R;
import com.e.hardwarewalaseller.OrderHistory;
import com.e.hardwarewalaseller.beans.ItemList;
import com.e.hardwarewalaseller.beans.Order;
import com.e.hardwarewalaseller.databinding.PackageHistoryItemListBinding;

import java.util.ArrayList;

public class PackageHistoryAdapter extends RecyclerView.Adapter<PackageHistoryAdapter.PackageHistoryViewHolder> {

    Context context;
    ArrayList<Order> orderList;

    public PackageHistoryAdapter(Context context, ArrayList<Order> orderList) {
        this.context = context;
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public PackageHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        PackageHistoryItemListBinding packageHistoryItemListBinding = PackageHistoryItemListBinding.inflate(LayoutInflater.from(context), parent, false);

        return new PackageHistoryViewHolder(packageHistoryItemListBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull PackageHistoryViewHolder holder, int position) {
        Order o = orderList.get(position);
        holder.packageHistoryItemListBinding.tvOrderId.setText(""+o.getOrderId());
        holder.packageHistoryItemListBinding.tvOrderDate.setText(""+o.getDate());
        holder.packageHistoryItemListBinding.tvTotalAmount.setText("₹ " + o.getTotalAmount() + " /-");
        holder.packageHistoryItemListBinding.tvDeliveryStatus.setText(o.getShippingStatus());
        String status = o.getShippingStatus();

//        String status="Delivered"; //hardcoded
        if (status.equalsIgnoreCase("Delivered")) {
            holder.packageHistoryItemListBinding.tvDeliveryStatus.setTextColor(context.getResources().getColor(R.color.colorgreen));
        } else if (status.equalsIgnoreCase("Cancelled")) {
            holder.packageHistoryItemListBinding.tvDeliveryStatus.setTextColor(context.getResources().getColor(R.color.colorRed));
        }

        ArrayList<ItemList> itemList = (ArrayList<ItemList>) o.getItemList();
        holder.packageHistoryItemListBinding.btnviewmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(context, OrderHistory.class);
                in.putExtra("itemList", itemList);
                in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                context.getApplicationContext().startActivity(in);
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public class PackageHistoryViewHolder extends RecyclerView.ViewHolder {
        PackageHistoryItemListBinding packageHistoryItemListBinding;

        public PackageHistoryViewHolder(PackageHistoryItemListBinding packageHistoryItemListBinding) {
            super(packageHistoryItemListBinding.getRoot());
            this.packageHistoryItemListBinding = packageHistoryItemListBinding;
        }
    }
}
