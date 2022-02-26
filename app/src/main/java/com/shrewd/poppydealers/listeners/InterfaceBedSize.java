package com.shrewd.poppydealers.listeners;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.shrewd.poppydealers.databinding.CustomListViewBinding;
import com.shrewd.poppydealers.model.BedSizeModal;

import java.util.List;

public interface InterfaceBedSize {
    void BedSelect(BedSizeModal bedsize ,int pos, View binding);



}
