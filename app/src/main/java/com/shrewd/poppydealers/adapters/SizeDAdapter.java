package com.shrewd.poppydealers.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shrewd.poppydealers.R;
import com.shrewd.poppydealers.databinding.CustomListViewBinding;
import com.shrewd.poppydealers.listeners.InterFaceSize;
import com.shrewd.poppydealers.listeners.InterfaceBedSize;
import com.shrewd.poppydealers.model.BedSizeModal;

import java.util.List;

public class SizeDAdapter extends RecyclerView.Adapter<SizeDAdapter.ViewHolder> {

    List<BedSizeModal> bedSizeModalList;
    Context context;
    InterFaceSize select;
    View BottomSheet;
    private int selected_position;

    public SizeDAdapter(List<BedSizeModal> bedSizeModalList, Context context, InterFaceSize select, View bottomSheet) {
        this.bedSizeModalList = bedSizeModalList;
        this.context = context;
        this.select = select;
        BottomSheet = bottomSheet;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SizeDAdapter.ViewHolder(CustomListViewBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BedSizeModal modal= bedSizeModalList.get(position);
        if (modal.getImage()==1){
            holder.binding.itemchosesizelayout.setBackground(context.getResources().getDrawable(R.drawable.custom_list_choose_selected));
            holder.binding.itemchosesizetext.setTextColor(Color.WHITE);
        }else {
            holder.binding.itemchosesizelayout.setBackground(context.getResources().getDrawable(R.drawable.custom_list_choose_select));
            holder.binding.itemchosesizetext.setTextColor(context.getResources().getColor(R.color.gradient_end_color));
        }

        holder.binding.itemchosesizetext.setText(modal.getTittle());
    }

    @Override
    public int getItemCount() {
        return bedSizeModalList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CustomListViewBinding binding;
        public ViewHolder(@NonNull CustomListViewBinding itemView) {
            super(itemView.getRoot());
            binding=itemView;
            binding.itemchosesizelayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selected_position= getPosition();
                    select.SizeSelect(bedSizeModalList.get(getAdapterPosition()),selected_position,BottomSheet);
                }
            });
        }
    }
}
