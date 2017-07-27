package com.codesample.newpatient.ui.doctor;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.codesample.newpatient.R;
import com.codesample.newpatient.listeners.Listener;
import com.codesample.newpatient.network.pojo.Doctor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Acer on 26.06.2017.
 */

public class TabsAdapter extends FragmentStatePagerAdapter {
    private List<String> tabs;
    public TabsAdapter(Context context, FragmentManager fm) {
        super(fm);
        tabs = new ArrayList<>();
        tabs.add(context.getString(R.string.tab_online_consultation));
    }

    @Override
    public Fragment getItem(int position) {
        return ScheduleFragment.newInstance();
    }

    @Override
    public int getCount() {
        return tabs.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabs.get(position);
    }
}
