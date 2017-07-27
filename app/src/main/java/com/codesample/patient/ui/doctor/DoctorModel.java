package com.codesample.newpatient.ui.doctor;

import com.codesample.newpatient.network.pojo.Doctor;
import com.codesample.newpatient.network.pojo.ReceptionSlot;
import com.codesample.newpatient.utils.ISO8601;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import rx.Observable;

/**
 * Created by Acer on 27.06.2017.
 */

public class DoctorModel {
    private static final int FIRST_SERVICE_FOR_DOCTOR = 0;
    static DoctorModel instance;
    List<Schedule> schedule;
    List<ReceptionSlot> mSlotsCache= new ArrayList<>();
    Set<String> mDistinctDatesCached = new HashSet<>();
    Doctor doctor;


    public DoctorModel() {
        schedule= new ArrayList<>();
    }

    public static DoctorModel getInstance() {
        if(null==instance) instance=new DoctorModel();
        return instance;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public String getServiceName(){
        if(null==doctor.getServices()) return "No services";
        else return doctor.getServices().get(FIRST_SERVICE_FOR_DOCTOR).getName();
    }

    public Integer getServiceId(){
        if(null==doctor.getServices()) return null;
        else return doctor.getServices().get(FIRST_SERVICE_FOR_DOCTOR).getId();
    }


    public List<Schedule> getSchedule() {
        return schedule;
    }

    public void prepareSchedule(List<ReceptionSlot> slots) {
        //we are caching the slots to get schedules later if calendar will be needed
        mSlotsCache = slots;
        mDistinctDatesCached.clear();
        schedule.clear();
        if(null==slots || 0==slots.size()) return;
        //Preparing calendar dates for sorting outof ISO8xxx
        for(ReceptionSlot r:slots) mDistinctDatesCached.add(r.createCalendarDate());
        //Sorting the slots based on calendar dates inside slots
        Collections.sort(slots, new Comparator<ReceptionSlot>() {
            @Override
            public int compare(ReceptionSlot t0, ReceptionSlot t1) {
                return (int) (t0.getCalendar().getTimeInMillis()-t1.getCalendar().getTimeInMillis());
            }
        });
        //getting through the list once again and filling the three schedules and the special schedule of other slots
        // fill today's slots
        Calendar day = Calendar.getInstance();
        addSchedule("Сегодня", day, slots);
        day.add(Calendar.DATE,1);
        addSchedule("Завтра", day, slots);
        day.add(Calendar.DATE,1);
        addSchedule("Послезавтра", day, slots);
    }

    void addSchedule(String name, Calendar day, List<ReceptionSlot> slots){
        //Using Rxjava filter
        Schedule today_s = new Schedule();
        Observable.from(slots)
                .filter(receptionSlot -> receptionSlot.isTheDate(day))
                .subscribe(receptionSlot -> {
                    today_s.addSlot(name,receptionSlot.getTime(), receptionSlot);
                });
        if(today_s.size()>0) schedule.add(today_s);
    }

    Calendar[] getDistinctCalendarDates(){
        Calendar [] res = new Calendar[mDistinctDatesCached.size()];
        int i=0;
       for(String date:mDistinctDatesCached){
           res[i++]= ISO8601.toCalendar(date);
       }
       return res;
    }

    public Schedule getScheduleForDate(Calendar cal) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMMMMM-yyyy");
        String datename = sdf.format(cal.getTime());
        Schedule schedule = new Schedule();
        Observable.from(mSlotsCache)
                .filter(receptionSlot -> receptionSlot.isTheDate(cal))
                .subscribe(receptionSlot -> {
                    schedule.addSlot(datename,receptionSlot.getTime(), receptionSlot);
                });
        return schedule;
    }
}
