package com.example.fastfood.Adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.fastfood.Model.ProductType;
import com.example.fastfood.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class Rcv1_adapter extends FirebaseRecyclerAdapter<ProductType, Rcv1_adapter.ViewHolder> {

    private OnItemClickListener listener;
    public Rcv1_adapter(@NonNull FirebaseRecyclerOptions<ProductType> options) {
        super(options);
    }

    public interface OnItemClickListener{
        void onItemClick(String productTypeId);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull ProductType model) {
        Log.d("rcv1", "Binding data for position: " + position + ", Product: " + model.toString());
        holder.tv_name_rcv1.setText(model.getType_Name());
        holder.tv_id_rcv1.setText(model.getProduct_Type_Id());
        Glide.with(holder.img_rcv1.getContext())
                .load(model.getImg_Product_Type())
                .circleCrop()
                .error(com.firebase.ui.database.R.drawable.common_google_signin_btn_icon_dark_normal)
                .into(holder.img_rcv1);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener != null){
                    listener.onItemClick(model.getProduct_Type_Id());
                }
            }
        });

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rcv1_main_user,parent,false);
        return new ViewHolder(v);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
    TextView tv_name_rcv1, tv_id_rcv1;
    ImageView img_rcv1;

    public ViewHolder(@NonNull View itemView) {
        super(itemView);
        tv_name_rcv1 = itemView.findViewById(R.id.item_tv_rcv1);
        img_rcv1 = itemView.findViewById(R.id.item_img_rcv1);
        tv_id_rcv1 = itemView.findViewById(R.id.item_id_rcv1);
    }
}
}
