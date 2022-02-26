package com.shrewd.poppydealers.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.shrewd.poppydealers.R;
import com.shrewd.poppydealers.rxjava.RxClient;
import com.shrewd.poppydealers.views.activity.intro.ScreenItem;

import java.util.List;

public class ImageViewPagerAdapter extends PagerAdapter {

    Context mContext;
    List<ScreenItem> mListScreen;

    public ImageViewPagerAdapter(Context context, List<ScreenItem> mListScreen) {
        this.mContext = context;
        this.mListScreen = mListScreen;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layoutScreen = inflater.inflate(R.layout.image_screen_item, null);

        ImageView introImage = layoutScreen.findViewById(R.id.imageView);
        Glide.with(introImage).load(RxClient.BASE_URL + RxClient.IMAGE_BASE_URL + mListScreen.get(position).getImageUrl()).into(introImage);

        container.addView(layoutScreen);

        return layoutScreen;
    }

    @Override
    public int getCount() {
        return mListScreen.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {

        container.removeView((View) object);
    }
}
