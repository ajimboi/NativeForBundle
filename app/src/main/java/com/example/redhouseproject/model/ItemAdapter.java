package com.example.redhouseproject.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.redhouseproject.R;
import com.example.redhouseproject.model.Item;

import java.util.List;


public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    /**
     * Create ViewHolder class to bind list item view
     */
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener{

        public TextView tvName;
        public TextView tvSize;
        public TextView tvColour;
        public TextView tvPrice;

        public ViewHolder(View itemView) {
            super(itemView);

            tvName = (TextView) itemView.findViewById(R.id.tvName);
            tvSize = (TextView) itemView.findViewById(R.id.tvSize);
            tvColour = (TextView) itemView.findViewById(R.id.tvColour);
            tvPrice = (TextView) itemView.findViewById(R.id.tvPrice);

            itemView.setOnLongClickListener(this);
        }

        @Override
        public boolean onLongClick(View view) {
            currentPos = getAdapterPosition(); //key point, record the position here
            return false;
        }
    }

    private List<Item> mListData;   // list of book objects
    private Context mContext;       // activity context
    private int currentPos;         //current selected position.

    public ItemAdapter(Context context, List<Item> listData){
        mListData = listData;
        mContext = context;
    }

    private Context getmContext(){return mContext;}


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the single item layout
        View view = inflater.inflate(R.layout.item_list_item, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // bind data to the view holder
        Item m = mListData.get(position);
        holder.tvName.setText(m.getItemName());
        holder.tvSize.setText(m.getItemSize());
        holder.tvColour.setText(m.getItemColour());
        holder.tvPrice.setText(m.getItemPrice());

    }

    @Override
    public int getItemCount() {
        return mListData.size();
    }

    public Item getSelectedItem() {
        if(currentPos>=0 && mListData!=null && currentPos<mListData.size()) {
            return mListData.get(currentPos);
        }
        return null;
    }


}