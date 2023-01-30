package com.skyblue.shop.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.skyblue.shop.model.Orders;
import com.skyblue.shop.R;

import java.util.List;

public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.ViewHolder> {
    Context context;
    private List<Orders> list;
    View v;

    public OrderListAdapter(Context context, List<Orders> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public OrderListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        v = LayoutInflater.from(context).inflate(R.layout.row_model_order_list, parent, false);
        return new OrderListAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderListAdapter.ViewHolder holder, int position) {
        final Orders orders = list.get(position);

        Glide
                .with(context)
                .load(orders.getThumbnail())
                .placeholder(R.drawable.default_placeholder)
                .into(holder.productImageView);

        holder.productNameText.setText(orders.getProduct_name());
        holder.salePriceTextView.setText(orders.getAmount());

    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView productImageView;
        TextView productNameText , productName2Text , discountPriceTextView , salePriceTextView;
        TextView ratingTextView;
        Button buyNowBtn;

        public ViewHolder(final View itemView) {
            super(itemView);

            productImageView = itemView.findViewById(R.id.product_image_view);
            productNameText = itemView.findViewById(R.id.home_product_name);
            productName2Text = itemView.findViewById(R.id.home_product_name_2);
            discountPriceTextView = itemView.findViewById(R.id.discount_price_text_view);
            salePriceTextView = itemView.findViewById(R.id.home_sale_price);
            ratingTextView = itemView.findViewById(R.id.product_home_rating_text_view);
            buyNowBtn = itemView.findViewById(R.id.buy_now_btn_home);
        }
    }
}
