package com.codesample.newpatient.ui.doctor;

import com.codesample.newpatient.abs.AbsPresenter;
import com.codesample.newpatient.network.pojo.Doctor;
import com.codesample.newpatient.ui.doctorslist.DoctorListModel;
import com.codesample.newpatient.ui.doctorslist.Filter;
import com.codesample.newpatient.utils.PhotoResource;

import java.util.Calendar;

public interface DoctorContract {

    public static String EXTRA_DOCTOR = "extra_doctor";

    interface ViewActionsListener extends AbsPresenter{
        void onInfoClicked();

        void onTodayClicked();

        void onTommorowClicked();

        void onDayAfterTommorowClicked();

        void initDoctor(Doctor doc);

        Doctor getDoctor();

        void onImmediateClicked();

        void onItemSelected(Schedule schedule, int position);

        void onFooterSelected();

        void setOnUpdateListener(ScheduleListener scheduleListener);

        void showScheduleDialog(Schedule schedule);

        void onDateSelected(int year, int monthOfYear, int dayOfMonth);
    }

    interface View {

        void initView(String title, String fio, float rating, PhotoResource photo, boolean isOnline, String tabName);

        void showDatePickerDialog(Calendar[] selectableDays);

        void showSelectionDialog(String title, String[] names);

        void showCityDialog(String title, String[] names);

        void showClinicDialog(String title, String[] names);

        void notifyDataSetChanged();

        void displayError(String s);

        void displayError(Throwable t);

        void showScheduleDialog(Schedule schedule);

    }

    public interface ScheduleView {

    }
}
