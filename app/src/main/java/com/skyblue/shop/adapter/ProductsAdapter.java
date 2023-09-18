package com.skyblue.shop.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.skyblue.shop.model.Products;
import com.skyblue.shop.R;
import com.skyblue.shop.activity.product_view.ProductViewActivity;

import java.util.List;

public class ProductsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private final int VIEW_TYPE_LOADING = 1;
        private final int VIEW_TYPE_ITEM = 0;

        Context context;
        private List<Products> list;
        View v;

        public ProductsAdapter(Context context, List<Products> list) {
            this.context = context;
            this.list = list;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            if (viewType == VIEW_TYPE_ITEM) {
                View view = LayoutInflater.from(context).inflate(R.layout.row_model_home, parent, false);
                return new ProductViewHolder(view);
            } else if (viewType == VIEW_TYPE_LOADING) {
                View view = LayoutInflater.from(context).inflate(R.layout.model_dialog_progress_dialog, parent, false);
                return new LoadingViewHolder(view);
            }
            return null;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {

            if (holder instanceof ProductViewHolder) {
                ProductViewHolder productViewHolder = (ProductViewHolder) holder;

                final Products products = list.get(position);

                productViewHolder.productNameText.setText(products.getProduct_name());
                productViewHolder.titleText.setText(products.getTitle());
                productViewHolder.discountPriceTextView.setPaintFlags(productViewHolder.discountPriceTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                productViewHolder.salePriceTextView.setText(products.getSale_price());
                productViewHolder.discountPriceTextView.setText(products.getDiscount_price());
                productViewHolder.ratingTextView.setText(products.getRating());

                Glide
                        .with(context)
                        .load(products.getThumbnail())
                        .placeholder(R.drawable.default_placeholder)
                        .into(productViewHolder.productImageView);

                productViewHolder.itemView.setOnClickListener(v -> {
                    Intent intent = new Intent(context , ProductViewActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("product_id", products.getId());
                    intent.putExtra("thumbnail", products.getThumbnail());
                    intent.putExtra("product_name", products.getProduct_name());
                    intent.putExtra("title", products.getTitle());
                    intent.putExtra("rating", products.getRating());
                    intent.putExtra("sale_price", products.getSale_price());
                    intent.putExtra("discount_price", products.getDiscount_price());
                    intent.putExtra("feature_1", products.getFeature_1());
                    intent.putExtra("feature_2", products.getFeature_2());
                    intent.putExtra("feature_3", products.getFeature_3());
                    intent.putExtra("feature_4", products.getFeature_4());
                    intent.putExtra("feature_5", products.getFeature_5());
                    context.startActivity(intent);
                });

                productViewHolder.buyNowBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context , ProductViewActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("product_id", products.getId());
                        intent.putExtra("thumbnail", products.getThumbnail());
                        intent.putExtra("product_name", products.getProduct_name());
                        intent.putExtra("title", products.getTitle());
                        intent.putExtra("rating", products.getRating());
                        intent.putExtra("sale_price", products.getSale_price());
                        intent.putExtra("discount_price", products.getDiscount_price());
                        intent.putExtra("feature_1", products.getFeature_1());
                        intent.putExtra("feature_2", products.getFeature_2());
                        intent.putExtra("feature_3", products.getFeature_3());
                        intent.putExtra("feature_4", products.getFeature_4());
                        intent.putExtra("feature_5", products.getFeature_5());
                        context.startActivity(intent);
                    }
                });

            } else if (holder instanceof LoadingViewHolder) {
                LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
                loadingViewHolder.progressBar.setIndeterminate(true);
            }
        }

        @Override
        public int getItemViewType(int position) {
            return list.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
        }

        static class LoadingViewHolder extends RecyclerView.ViewHolder {
            public ProgressBar progressBar;

            public LoadingViewHolder(View itemView) {
                super(itemView);
                progressBar = (ProgressBar) itemView.findViewById(R.id.progressbar);
            }
        }

        static class ProductViewHolder extends RecyclerView.ViewHolder {
            ImageView productImageView;
            TextView productNameText , titleText , discountPriceTextView , salePriceTextView;
            TextView ratingTextView;
            Button buyNowBtn;

            public ProductViewHolder(View itemView) {
                super(itemView);
                productImageView = itemView.findViewById(R.id.product_image_view);
                productNameText = itemView.findViewById(R.id.home_product_name);
                titleText = itemView.findViewById(R.id.home_product_title);
                discountPriceTextView = itemView.findViewById(R.id.discount_price_text_view);
                salePriceTextView = itemView.findViewById(R.id.home_sale_price);
                ratingTextView = itemView.findViewById(R.id.product_home_rating_text_view);
                buyNowBtn = itemView.findViewById(R.id.buy_now_btn_home);
            }
        }

        @Override
        public int getItemCount() {
            return list == null ? 0 : list.size();
        }


//        public class ViewHolder extends RecyclerView.ViewHolder {
//            ImageView productImageView;
//            TextView productNameText , titleText , discountPriceTextView , salePriceTextView;
//            TextView ratingTextView;
//            Button buyNowBtn;
//
//            public ViewHolder(final View itemView) {
//                super(itemView);
//
//                productImageView = itemView.findViewById(R.id.product_image_view);
//                productNameText = itemView.findViewById(R.id.home_product_name);
//                titleText = itemView.findViewById(R.id.home_product_title);
//                discountPriceTextView = itemView.findViewById(R.id.discount_price_text_view);
//                salePriceTextView = itemView.findViewById(R.id.home_sale_price);
//                ratingTextView = itemView.findViewById(R.id.product_home_rating_text_view);
//                buyNowBtn = itemView.findViewById(R.id.buy_now_btn_home);
//            }
//        }

    }
