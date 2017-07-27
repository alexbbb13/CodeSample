package com.codesample.newpatient.ui.doctor;

/**
 * Created by Acer on 26.06.2017.
 */

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.codesample.newpatient.R;
import com.codesample.newpatient.listeners.Listener;
import com.codesample.newpatient.network.pojo.Doctor;
import com.codesample.newpatient.ui.doctorslist.CustomDividerItemDecoration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ScheduleFragment extends Fragment {
    @BindView(R.id.schedule_now_btn)
    Button schedule_now_btn;

    @BindView(R.id.schedule_list)
    RecyclerView schedule_list;


    private ScheduleAdapter scheduleAdapter;
    private Doctor doctor;
    private DoctorContract.ViewActionsListener presenter;

    public static ScheduleFragment newInstance(){
        ScheduleFragment f = new ScheduleFragment();
        return f;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_doctor_schedule, container, false);
        ButterKnife.bind(this, rootView);
        presenter = DoctorPresenter.getInstance();
        doctor = presenter.getDoctor();

        setupSchedule();
        return rootView;
    }

    private void setupSchedule() {
        schedule_list.setLayoutManager(new LinearLayoutManager(getActivity()));
        schedule_list.addItemDecoration(new CustomDividerItemDecoration(ContextCompat.getDrawable(this.getContext(), R.drawable.list_divider), 0));


        scheduleAdapter = new ScheduleAdapter(getActivity());
        schedule_list.setAdapter(scheduleAdapter);
        presenter.setOnUpdateListener(new ScheduleListener() {
            @Override
            public void onUpdate(List<Schedule> newSchedule) {
                scheduleAdapter.setList(newSchedule);
                scheduleAdapter.notifyDataSetChanged();
            }
        });

    }

}

