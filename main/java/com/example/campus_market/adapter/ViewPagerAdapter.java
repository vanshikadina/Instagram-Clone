package com.example.campus_market.adapter;

import android.icu.text.CaseMap;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends FragmentStateAdapter {
    public ViewPagerAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }
    List<Fragment> fragmentList = new ArrayList<>();
    List<String> titleList = new ArrayList<>();
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    public String getPageTitle(int position) {
        return titleList.get(position);
    }

    public void addFragment(Fragment fragment,String Title){
        fragmentList.add(fragment);
        titleList.add(Title);
    }
    @Override
    public int getItemCount() {
        return fragmentList.size(); // Number of tabs
    }

}
