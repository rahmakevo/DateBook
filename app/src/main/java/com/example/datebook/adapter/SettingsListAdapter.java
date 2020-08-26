package com.example.datebook.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.datebook.R;

public class SettingsListAdapter extends BaseAdapter {
    Context context;
    String mSettingsList[];
    int mSettingsIconList[];
    LayoutInflater mInflater;

    public SettingsListAdapter(Context context, String[] mSettingsList, int[] mSettingsIconList, LayoutInflater mInflater) {
        this.context = context;
        this.mSettingsList = mSettingsList;
        this.mSettingsIconList = mSettingsIconList;
        this.mInflater = mInflater;
    }

    @Override
    public int getCount() {
        return mSettingsList.length;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }
}
