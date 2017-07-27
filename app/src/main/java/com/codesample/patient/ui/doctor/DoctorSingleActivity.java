package com.codesample.newpatient.ui.doctor;

import android.content.DialogInterface;
import android.os.Bundle;


import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.codesample.newpatient.R;
import com.codesample.newpatient.application.ErrorHandler;
import com.codesample.newpatient.application.PatientApplication;
import com.codesample.newpatient.navigator.AndroidNavigator;
import com.codesample.newpatient.navigator.NavigatorActivity;
import com.codesample.newpatient.network.pojo.Doctor;
import com.codesample.newpatient.utils.PhotoResource;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Calendar;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DoctorSingleActivity extends NavigatorActivity implements  DoctorContract.View{
    private AlertDialog scheduleDialog;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.iv_doc_avatar)
    ImageView avatar;

    @BindView(R.id.tv_doc_title)
    TextView title;

    @BindView(R.id.tv_doc_rate)
    RatingBar rate;

    @BindView(R.id.tv_doc_online)
    TextView online;

    @BindView(R.id.action_indicator)
    ImageView action_indicator;

    @BindView(R.id.btDocStartConsultationNow)
    Button btDocStartConsultationNow;
    @OnClick(R.id.btDocStartConsultationNow)
    void startConsultationNow(){
        presenter.onImmediateClicked();
    }


    @BindView(R.id.tabs)
    TabLayout tabs;

    @BindView(R.id.viewPager)
    ViewPager viewPager;

    @Inject
    ErrorHandler errorHandler;

    private DoctorContract.ViewActionsListener presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PatientApplication.getComponent().inject(this);
        setContentView(R.layout.activity_doctor_single);
        ButterKnife.bind(this);
        presenter = new DoctorPresenter((DoctorContract.View) this, AndroidNavigator.newInstance((AppCompatActivity)this));
        if(null!= getIntent() && null!=getIntent().getStringExtra(DoctorContract.EXTRA_DOCTOR)){
            Doctor doctor = (Doctor) new Gson().fromJson(getIntent().getStringExtra(DoctorContract.EXTRA_DOCTOR),Doctor.class);
            presenter.initDoctor(doctor);
        }
        presenter.startPresenting();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.stopPresenting();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void initView(String tit, String fio, float rating, PhotoResource photo, boolean isOnline, String tabName) {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(fio);
        title.setText(tit);

        rate.setRating(rating);

        if (photo != null) {
            photo.display(this, avatar);
        }

        if (isOnline) {
            online.setVisibility(View.VISIBLE);
            btDocStartConsultationNow.setVisibility(View.VISIBLE);
        } else {
            online.setVisibility(View.INVISIBLE);
            btDocStartConsultationNow.setVisibility(View.INVISIBLE);
        }
        tabs.setupWithViewPager(viewPager);
        viewPager.setAdapter(new TabsAdapter(this, getSupportFragmentManager()));

    }

    @Override
    public void showSelectionDialog(String title, String[] names) {

    }

    @Override
    public void showCityDialog(String title, String[] names) {

    }

    @Override
    public void showClinicDialog(String title, String[] names) {

    }

    @Override
    public void notifyDataSetChanged() {

    }

    @Override
    public void displayError(String s) {
        errorHandler.handle(this,s);
    }

    @Override
    public void displayError(Throwable t) {
        errorHandler.handle(this,t);
    }

    @Override
    public void showScheduleDialog(final Schedule schedule) {
        if (scheduleDialog != null) scheduleDialog.dismiss();
        scheduleDialog = new AlertDialog.Builder(this)
                .setTitle(R.string.doctor_info_choose_consult_datetime)
                .setItems(schedule.getNamedTimeSlots(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        DoctorPresenter.getInstance().onItemSelected(schedule,i);
                    }
                })
                .show();
    }

    @Override
    public void showDatePickerDialog(Calendar[] days){
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                        DoctorPresenter.getInstance().onDateSelected(year, monthOfYear, dayOfMonth);
                    }
                },
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        dpd. setSelectableDays(days);
        dpd.show(getFragmentManager(), "Datepickerdialog");
    }

}
