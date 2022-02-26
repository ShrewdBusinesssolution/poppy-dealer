package com.shrewd.poppydealers.listeners;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.shrewd.poppydealers.model.BedSizeModal;

public interface InterFaceSize {
    void SizeSelect(BedSizeModal bedsize ,int pos, View binding);
}
