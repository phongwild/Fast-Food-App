package com.example.fastfood.Adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fastfood.List_Activity.Activity_Management.Bill_Detail_Manager;
import com.example.fastfood.List_Activity.Main_Activity.Bill_Detail_Activity;
import com.example.fastfood.List_Activity.Main_Activity.Confirmed_order;
import com.example.fastfood.Model.Bill;
import com.example.fastfood.Model.Notification;
import com.example.fastfood.Model.User;
import com.example.fastfood.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.UUID;

public class OrderConfirmation_Adapter extends FirebaseRecyclerAdapter<Bill, OrderConfirmation_Adapter.myViewHolder> {

    public OrderConfirmation_Adapter(@NonNull FirebaseRecyclerOptions<Bill> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull Bill model) {
        holder.billId_Manager.setText(model.getBill_Id());
        holder.Total_Manager.setText(String.valueOf(model.getTotal_Amount()) + " VNĐ");
        holder.DateTime_Manager.setText(model.getPurchase_Date() + " " + model.getTime());
        holder.userId_Manager.setText(model.getAddress());
        holder.Note_Manager.setText(model.getNote());
        int status = model.getStatus();
        if(status == 0){
            holder.statusBill_Manager.setText("wait for confirmation");
        }else if(status == 1){
            holder.statusBill_Manager.setText("confirmed");
        }else{
            holder.statusBill_Manager.setText("Has paid");
            holder.statusBill_Manager.setTextColor(holder.itemView.getContext().getResources().getColor(android.R.color.holo_green_dark));
        }
        String userId = model.getUser_Id();
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("User");
        Query query = userRef.orderByChild("user_Id").equalTo(userId);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                        User user = dataSnapshot.getValue(User.class);
                        holder.Name_Manager.setText(user.getFull_Name());
                        holder.Phone_Manager.setText("0" + String.valueOf(user.getPhone_Number()));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String billId = model.getBill_Id();
                Intent intent = new Intent(view.getContext(), Bill_Detail_Activity.class);
                intent.putExtra("billId", billId);
                view.getContext().startActivity(intent);
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                final DialogPlus dialogPlus = DialogPlus.newDialog(view.getContext())
                        .setContentHolder(new ViewHolder(R.layout.update_orderconfirmation))
                        .setContentWidth(ViewGroup.LayoutParams.MATCH_PARENT) // Đặt chiều rộng của nội dung
                        .setContentHeight(ViewGroup.LayoutParams.WRAP_CONTENT) // Đặt chiều cao của nội dung
                        .create();
                View view1 = dialogPlus.getHolderView();
                EditText updateBill_note = view1.findViewById(R.id.updateBill_note);
                Button updateBill = view1.findViewById(R.id.updateBill);
                updateBill_note.setText(model.getNote());

                updateBill.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String note = updateBill_note.getText().toString();
                        DatabaseReference billRef = FirebaseDatabase.getInstance().getReference().child("Bill").child(model.getBill_Id());
                        billRef.child("note").setValue(note);
                        dialogPlus.dismiss();
                    }
                });

                dialogPlus.show();
                return false;
            }
        });

        holder.OrderConfirmation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference billRef = FirebaseDatabase.getInstance().getReference().child("Bill").child(model.getBill_Id());
                billRef.child("status").setValue(1);
                addNotification(model);
            }
        });
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_orderconfirmation,parent,false);
        return new myViewHolder(view);
    }


    public static class myViewHolder extends RecyclerView.ViewHolder{
        TextView billId_Manager,Total_Manager,DateTime_Manager,userId_Manager,statusBill_Manager,Name_Manager,Phone_Manager,Note_Manager;
        Button OrderConfirmation;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            billId_Manager = itemView.findViewById(R.id.billId_Manager);
            Total_Manager = itemView.findViewById(R.id.Total_Manager);
            DateTime_Manager = itemView.findViewById(R.id.DateTime_Manager);
            userId_Manager = itemView.findViewById(R.id.userId_Manager);
            statusBill_Manager = itemView.findViewById(R.id.statusBill_Manager);
            OrderConfirmation = itemView.findViewById(R.id.OrderConfirmation);
            Name_Manager = itemView.findViewById(R.id.Name_Manager);
            Phone_Manager = itemView.findViewById(R.id.Phone_Manager);
            Note_Manager = itemView.findViewById(R.id.Note_Manager);
        }

    }

    private void addNotification(Bill bill){
        DatabaseReference notificationRef = FirebaseDatabase.getInstance().getReference().child("Notification").push();
        String notifi_Id = UUID.randomUUID().toString();
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy");
        String purchase_Date = dateFormat.format(calendar.getTime());

        Notification notification = new Notification();
        notification.setNotification_Id(notifi_Id);
        notification.setContent_Notification("Đơn hàng " + bill.getBill_Id() + " đã được xác nhận");
        notification.setSend_Date(purchase_Date);
        notification.setUser_Id(bill.getUser_Id());

        notificationRef.setValue(notification);
    }

}
