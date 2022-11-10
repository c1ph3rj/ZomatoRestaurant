package com.c1ph3r.zomatorestaurant.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;

public class RegistrationProcess extends FragmentStateAdapter {


    private ArrayList<Fragment> ListOfFragments = new ArrayList<>();

    public RegistrationProcess(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return ListOfFragments.get(position);
    }

    @Override
    public int getItemCount() {
        return ListOfFragments.size();
    }

    public void addFragment(Fragment fragment){
        ListOfFragments.add(fragment);
    }
}
