package com.example.fastfood.Adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fastfood.List_Activity.Activity_Management.Bill_Detail_Manager;
import com.example.fastfood.Model.Bill;
import com.example.fastfood.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class BillHistory_Adapter extends FirebaseRecyclerAdapter<Bill, BillHistory_Adapter.myViewHolder> {

    public BillHistory_Adapter(@NonNull FirebaseRecyclerOptions<Bill> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull Bill model) {
        holder.billId_Manager.setText(model.getBill_Id());
        holder.Total_Manager.setText(String.valueOf(model.getTotal_Amount()) + " VNĐ");
        holder.DateTime_Manager.setText(model.getPurchase_Date() + " " + model.getTime());
        holder.userId_Manager.setText(model.getUser_Id());
        int status = model.getStatus();
        if(status == 0){
            holder.statusBill_Manager.setText("wait for confirmation");
        }else if(status == 1){
            holder.statusBill_Manager.setText("confirmed");
        }else{
            holder.statusBill_Manager.setText("Has paid");
            holder.statusBill_Manager.setTextColor(holder.itemView.getContext().getResources().getColor(android.R.color.holo_green_dark));
        }
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bill,parent,false);
        return new myViewHolder(view);
    }

    public static class myViewHolder extends RecyclerView.ViewHolder{
        TextView billId_Manager,Total_Manager,DateTime_Manager,userId_Manager,statusBill_Manager;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            billId_Manager = itemView.findViewById(R.id.billId_Manager);
            Total_Manager = itemView.findViewById(R.id.Total_Manager);
            DateTime_Manager = itemView.findViewById(R.id.DateTime_Manager);
            userId_Manager = itemView.findViewById(R.id.userId_Manager);
            statusBill_Manager = itemView.findViewById(R.id.statusBill_Manager);
        }

    }

}
