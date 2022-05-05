package hu.mobilalk.shop;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> implements Filterable {

    private ArrayList<Items> items;
    private ArrayList<Items> itemsAll;
    private Context context;
    private int lastPosition = -1;

    ItemAdapter(Context context, ArrayList<Items> items) {
        this.context = context;
        this.items = items;
        this.itemsAll = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(this.context).inflate(R.layout.list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ItemAdapter.ViewHolder holder, int position) {
        Items currentItem = items.get(position);

        holder.bindTo(currentItem);

        if(holder.getAdapterPosition() > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.animation);
            holder.itemView.startAnimation(animation);
            lastPosition = holder.getAdapterPosition();
        }
    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    private Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            ArrayList<Items> filteredList = new ArrayList<>();
            FilterResults results = new FilterResults();

            if(charSequence == null || charSequence.length() == 0) {
                results.count = itemsAll.size();
                results.values = itemsAll;
            }
            else {
                String filterPattern = charSequence.toString().toLowerCase().trim();

                for(Items item : itemsAll) {
                    if(item.getName().toLowerCase().contains(filterPattern) || item.getInfo().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }

                results.count = filteredList.size();
                results.values = filteredList;
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            items = (ArrayList) filterResults.values;
            notifyDataSetChanged();
        }
    };

    class ViewHolder extends RecyclerView.ViewHolder{

        private TextView titleText;
        private TextView infoText;
        private TextView priceText;
        private ImageView itemImage;
        private RatingBar ratingBar;

        public ViewHolder(View itemView) {
            super(itemView);

            titleText = itemView.findViewById(R.id.item_listTitle);
            infoText = itemView.findViewById(R.id.item_listSubTitle);
            priceText = itemView.findViewById(R.id.item_listPrice);
            itemImage = itemView.findViewById(R.id.list_itemImageView);
            ratingBar = itemView.findViewById(R.id.item_listRatingBar);
        }

        public void bindTo(Items currentItem) {
            titleText.setText(currentItem.getName());
            infoText.setText(currentItem.getInfo());
            priceText.setText(currentItem.getPrice());
            ratingBar.setRating(currentItem.getRate());

            Glide.with(context).load(currentItem.getImage()).into(itemImage);
            itemView.findViewById(R.id.item_listAddCart).setOnClickListener(view -> {
                ((MainActivity) context).updateCount(currentItem);
                Animation animation = AnimationUtils.loadAnimation(context, R.anim.image_animation);
                itemView.findViewById(R.id.list_itemImageView).startAnimation(animation);
            });
            itemView.findViewById(R.id.item_listDelete).setOnClickListener(view -> ((MainActivity) context).deleteItem(currentItem));
        }
    }
}