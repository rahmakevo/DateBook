package com.example.datebook.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.datebook.fragments.CallsFragment;
import com.example.datebook.fragments.ChatsFragment;
import com.example.datebook.fragments.MatchesFragment;

public class ViewPagerFragmentAdapter extends FragmentPagerAdapter {
    public ViewPagerFragmentAdapter(@NonNull FragmentManager fm) { super(fm); }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new MatchesFragment();
            case 1:
                fragment = new ChatsFragment();
            case 2:
                fragment = new CallsFragment();
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title = null;
        switch (position) {
            case 0:
                title = "Matches";
            case 1:
                title = "Chats";
            case 2:
                title = "Calls";
        }
        return title;
    }
}
