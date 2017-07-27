package com.codesample.newpatient.ui.doctor;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;

import com.codesample.newpatient.R;
import com.codesample.newpatient.abs.AbsPresenter;
import com.codesample.newpatient.application.ErrorHandler;
import com.codesample.newpatient.application.PatientApplication;
import com.codesample.newpatient.application.Security;
import com.codesample.newpatient.application.Storage;
import com.codesample.newpatient.navigator.AndroidNavigator;
import com.codesample.newpatient.navigator.Navigator;
import com.codesample.newpatient.network.Api;
import com.codesample.newpatient.network.UserModel;
import com.codesample.newpatient.network.pojo.Doctor;
import com.codesample.newpatient.network.pojo.DoctorService;
import com.codesample.newpatient.services.RegistrationIntentService_MembersInjector;
import com.codesample.newpatient.utils.PhotoResource;
import com.wdullaer.materialdatetimepicker.time.Timepoint;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by alex on 11.05.17.
 */

public class DoctorPresenter implements DoctorContract.ViewActionsListener {

    DoctorContract.View view;

    Navigator navigator;
    @Inject
    UserModel userModel;
    @Inject
    Api api;
    @Inject
    Security security;
    @Inject
    Context context;
    @Inject
    Storage storage;

    private static DoctorContract.ViewActionsListener instance;
    private ScheduleListener scheduleListener;
    private static final int ONLINE_CONSULTATION_POS_IN_SERVICES_LIST = 0;
    private static final int TIMESLOT_LENGTH = 15;

    public DoctorPresenter(DoctorContract.View view, AndroidNavigator androidNavigator) {
        PatientApplication.getComponent().inject(this);
        this.view=view;
        navigator=androidNavigator;
        instance=this;
    }

    public static DoctorContract.ViewActionsListener getInstance() {
        return instance;
    }

    @Override
    public void stopPresenting() {
        view=null;
    }

    @Override
    public void startPresenting() {
        //Doctor is already set, so we are creating schedule
        //Async init all views
        Doctor doctor = DoctorModel.getInstance().getDoctor();
        if(null!=doctor){
            //@TODO Dirty hack  - we should get lists if all services here
           //we are getting one and only: online consultation
            api.getReceprionSlots(doctor.getServices().get(ONLINE_CONSULTATION_POS_IN_SERVICES_LIST).getId())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.io())
                    .subscribe(slots ->{
                        DoctorModel.getInstance().prepareSchedule(slots);
                        scheduleListener.onUpdate(DoctorModel.getInstance().getSchedule());
                    }, error->view.displayError(error));
            String titleText = "";
            if (doctor.getSpecialization().getName() != null) titleText += doctor.getSpecialization().getName();
            if (doctor.getClinic().getName() != null) titleText += ", " + doctor.getClinic().getName();
            titleText += "\n" + String.format("%d \u20BD", doctor.getPrice());
            view.initView(titleText,doctor.getShortName(),doctor.getRating(),new PhotoResource(doctor.getUser().getPhoto()),
                    doctor.isOnline(),"Онлайн консультация");

        }

    }

    @Override
    public void onInfoClicked() {

    }

    @Override
    public void onTodayClicked() {

    }

    @Override
    public void onTommorowClicked() {

    }

    @Override
    public void onDayAfterTommorowClicked() {

    }

    @Override
    public void initDoctor(Doctor doc) {
        DoctorModel.getInstance().setDoctor(doc);
    }

    public Doctor getDoctor() {
        return DoctorModel.getInstance().getDoctor();
    }

    @Override
    public void onImmediateClicked() {
        int price = getDoctor().getServices().get(ONLINE_CONSULTATION_POS_IN_SERVICES_LIST).getPrice();
        navigator.startPaymentInfoActivity(security.getPatientId(), getDoctor(),TIMESLOT_LENGTH, price);
        navigator.closeActivity();
    }

    @Override
    public void onItemSelected(Schedule schedule, int position) {
        int price = getDoctor().getServices().get(ONLINE_CONSULTATION_POS_IN_SERVICES_LIST).getPrice();
        Calendar cal = schedule.getReceptionSlot(position).getCalendar();

        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMMMM yyyy  HH:mm");
        String datetime = sdf.format(cal.getTime());
        navigator.startPaymentInfoActivity(security.getPatientId(), getDoctor(),TIMESLOT_LENGTH, price,datetime, schedule.getReceptionSlot(position).getId());
        navigator.closeActivity();
    }

    @Override
    public void onFooterSelected() {
        //Here we are showing the calendar with dates 30 days ahead grayed if there is no schedule that day
        //here we have already set the schedule in Model
        Calendar [] dates = DoctorModel.getInstance().getDistinctCalendarDates();
        if(0!=dates.length) view.showDatePickerDialog(dates);
                else view.displayError("Консультаций на ближайшие даты нет");
    }

    @Override
    public void onDateSelected(int year, int monthOfYear, int dayOfMonth) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR,year);
        cal.set(Calendar.MONTH,monthOfYear);
        cal.set(Calendar.DAY_OF_MONTH,dayOfMonth);
        view.showScheduleDialog(DoctorModel.getInstance().getScheduleForDate(cal));
    }

    @Override
    public void showScheduleDialog(Schedule schedule) {
        view.showScheduleDialog(schedule);
    }

    @Override
    public void setOnUpdateListener(ScheduleListener scheduleListener) {
        this.scheduleListener=scheduleListener;
    }
}
