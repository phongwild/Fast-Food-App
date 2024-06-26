package com.example.fastfood.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.fastfood.Model.Bill_Detail;
import com.example.fastfood.Model.Product;
import com.example.fastfood.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.ArrayList;
import java.util.HashMap;

public class Bill_Detail_Adapter extends FirebaseRecyclerAdapter<Bill_Detail, Bill_Detail_Adapter.myViewHolder> {
    private Context context;
    ArrayList<Bill_Detail> selectedItems = new ArrayList<>();
    public Bill_Detail_Adapter(@NonNull FirebaseRecyclerOptions<Bill_Detail> options,Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, @SuppressLint("RecyclerView") int position, @NonNull Bill_Detail model) {
        holder.Billdetailid_BillDetail.setText(model.getInvoice_detail_id());
        holder.Quantity_BillDetail.setText(String.valueOf(model.getCart_quantity()));
        holder.Price_BillDetail.setText(String.valueOf(model.getPrice()) + " VNĐ");
        holder.TotalDetail_BillDetail.setText(String.valueOf(model.getTotal_details()) + " VNĐ");
        String productId = model.getId();
        DatabaseReference productRef = FirebaseDatabase.getInstance().getReference().child("Product");
        Query query = productRef.orderByChild("id").equalTo(productId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        Product product = dataSnapshot.getValue(Product.class);
                        holder.Nameproduct_BillDetail.setText(product.getName());
                        Glide.with(holder.image_BillDetail.getContext())
                                .load(product.getImg_Product())
                                .into(holder.image_BillDetail);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                String selectedKey = getSnapshots().getSnapshot(position).getKey(); // Lấy key của mục được chọn
                dialogUpdateBillDetail(selectedKey,model); // Truyền key vào hàm xử lý dialog

                return false;
            }
        });
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_billdetail,parent,false);
        return new myViewHolder(view);
    }

    public static class myViewHolder extends RecyclerView.ViewHolder{
        ImageView image_BillDetail;
        TextView Billdetailid_BillDetail,Nameproduct_BillDetail,Quantity_BillDetail,Price_BillDetail,TotalDetail_BillDetail;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            image_BillDetail = itemView.findViewById(R.id.image_BillDetail);
            Billdetailid_BillDetail = itemView.findViewById(R.id.Billdetailid_BillDetail);
            Nameproduct_BillDetail = itemView.findViewById(R.id.Nameproduct_BillDetail);
            Quantity_BillDetail = itemView.findViewById(R.id.Quantity_BillDetail);
            Price_BillDetail = itemView.findViewById(R.id.Price_BillDetail);
            TotalDetail_BillDetail = itemView.findViewById(R.id.TotalDetail_BillDetail);
        }
    }

    private void dialogUpdateBillDetail(String key, Bill_Detail billDetail){
        final DialogPlus dialogPlus = DialogPlus.newDialog(context)
                .setContentHolder(new ViewHolder(R.layout.update_billdetail))
                .setContentWidth(ViewGroup.LayoutParams.MATCH_PARENT)
                .setContentHeight(ViewGroup.LayoutParams.WRAP_CONTENT)
                .create();
        View view = dialogPlus.getHolderView();

        Spinner updateBill_spinner = view.findViewById(R.id.updateBill_spinner);
        EditText updateBill_quantity = view.findViewById(R.id.updateBill_quantity);
        EditText updateBill_Id = view.findViewById(R.id.updateBill_Id);
        Button updateBillDetail = view.findViewById(R.id.updateBillDetail);
        // thực hiện spinner
        ArrayList<HashMap<String,String>> productList = new ArrayList<>();
        DatabaseReference productRef = FirebaseDatabase.getInstance().getReference().child("Product");

        final SimpleAdapter productAdapter = new SimpleAdapter(
                context,
                productList,
                R.layout.spinner_billdetail,
                new String[]{"productId","productName"},
                new int[]{R.id.productId_spinner, R.id.nameProduct_spinner}
        );
        productRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                        Product product = dataSnapshot.getValue(Product.class);
                        HashMap<String, String> productItem = new HashMap<>();
                        productItem.put("productId", product.getId());
                        productItem.put("productName", product.getName());
                        productList.add(productItem);
                    }
                    productAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        updateBill_spinner.setAdapter(productAdapter);
        updateBill_quantity.setText(String.valueOf(billDetail.getCart_quantity()));
        updateBill_Id.setText(billDetail.getId());

        updateBillDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newProductId = updateBill_Id.getText().toString();
                int newQuantity = Integer.parseInt(updateBill_quantity.getText().toString());
                int price = billDetail.getPrice();

                int newTotal = price * newQuantity;

                DatabaseReference billDetailRef = FirebaseDatabase.getInstance().getReference().child("Bill_Detail");
                DatabaseReference specificBillDetailRef = billDetailRef.child(key);

                specificBillDetailRef.child("id").setValue(newProductId);
                specificBillDetailRef.child("cart_quantity").setValue(newQuantity);
                specificBillDetailRef.child("total_details").setValue(newTotal);

                // Update total_Amount trong billRef
                DatabaseReference billRef = FirebaseDatabase.getInstance().getReference().child("Bill");
                String billId = billDetail.getBill_Id();
                DatabaseReference specificBillRef = billRef.child(billId);
                specificBillRef.child("total_Amount").removeValue(); // Xóa total_Amount hiện tại

                // Tính toán lại total_Amount
                DatabaseReference billDetailRefCheck = FirebaseDatabase.getInstance().getReference().child("Bill_Detail");
                Query billDetailQuery = billDetailRefCheck.orderByChild("bill_Id").equalTo(billId);
                billDetailQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        int totalAmount = 0;
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Bill_Detail billDetail = dataSnapshot.getValue(Bill_Detail.class);
                            totalAmount += billDetail.getTotal_details();
                        }
                        specificBillRef.child("total_Amount").setValue(totalAmount);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Xử lý lỗi nếu cần
                    }
                });


                dialogPlus.dismiss();
            }
        });



        dialogPlus.show();
    }



}
