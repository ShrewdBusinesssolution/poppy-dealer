package com.shrewd.poppydealers.adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.shrewd.poppydealers.model.Series;
import com.shrewd.poppydealers.views.fragment.placeholder.PlaceHolderFragment;

import java.util.ArrayList;
import java.util.List;

public class SectionPagerAdapter extends FragmentPagerAdapter {

    List<Series> list;

    public SectionPagerAdapter(@NonNull FragmentManager fm, List<Series> mlist) {
        super(fm);
        list = new ArrayList<>();
        list = mlist;

    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        return PlaceHolderFragment.newInstance(position);
    }


    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return list.get(position).getSeries();
    }

    @Override
    public int getCount() {
        return list.size();
    }
}
