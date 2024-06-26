package com.example.fastfood.Adapter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.fastfood.Model.Cart;
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

public class Cart_Adapter extends FirebaseRecyclerAdapter<Cart, Cart_Adapter.myViewHolder> {
    public Cart_Adapter(@NonNull FirebaseRecyclerOptions<Cart> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull Cart model) {

        final int itemPosition = position;

        String productId = model.getId();
        DatabaseReference productRef = FirebaseDatabase.getInstance().getReference().child("Product");
        Query query = productRef.orderByChild("id").equalTo(productId);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Product product = dataSnapshot.getValue(Product.class);
                        // Xử lý dữ liệu sản phẩm ở đây
                        holder.name_cart.setText(product.getName());
                        holder.price_cart.setText(String.valueOf(product.getPrice() + " đ"));
                        Glide.with(holder.Img_Cart.getContext())
                                .load(product.getImg_Product())
                                .into(holder.Img_Cart);
                        holder.quantity_cart.setText(String.valueOf(model.getCart_quantity()));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý khi truy vấn bị hủy
            }
        });
        // Trong onBindViewHolder của Adapter
        // tăng số lượng hàng
        holder.add_cart_quantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int quantity = model.getCart_quantity();
                quantity++;
                DatabaseReference cartRef = getRef(itemPosition);
                cartRef.child("cart_quantity").setValue(quantity);
            }
        });
        //giảm số lượng
        holder.minus_cart_quantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int quantity = model.getCart_quantity();
                if(quantity > 1){
                    quantity--;
                    DatabaseReference cartRef = getRef(itemPosition);
                    cartRef.child("cart_quantity").setValue(quantity);
                }
            }
        });
        // xóa cart
        // Trong phương thức onBindViewHolder của Adapter
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(holder.itemView.getContext());
                builder.setTitle("Xóa sản phẩm");
                builder.setMessage("Bạn có chắc chắn muốn xóa sản phẩm này khỏi giỏ hàng?");
                builder.setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        DatabaseReference cartItemRef = getRef(itemPosition);
                        cartItemRef.removeValue(); // Xóa sản phẩm khỏi giỏ hàng của người dùng
                    }
                });
                builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Không làm gì khi người dùng hủy xóa sản phẩm
                    }
                });
                builder.create().show();
            }
        });

    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_addtocart,parent,false);
        return new myViewHolder(view);
    }

    public static class myViewHolder extends RecyclerView.ViewHolder{
        ImageView Img_Cart, minus_cart_quantity, add_cart_quantity;
        TextView name_cart, price_cart, quantity_cart;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            Img_Cart = itemView.findViewById(R.id.Img_Cart);
            minus_cart_quantity = itemView.findViewById(R.id.minus_cart_quantity);
            add_cart_quantity = itemView.findViewById(R.id.add_cart_quantity);
            name_cart = itemView.findViewById(R.id.name_cart);
            price_cart = itemView.findViewById(R.id.price_cart);
            quantity_cart = itemView.findViewById(R.id.quantity_cart);
        }
    }



}
