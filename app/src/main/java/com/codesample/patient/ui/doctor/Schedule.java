package com.codesample.newpatient.ui.doctor;

import com.codesample.newpatient.network.pojo.ReceptionSlot;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Acer on 26.06.2017.
 */

public class Schedule {

    public String title;
    public String startTime;
    public String endTime;
    public List<String> namedtimeslots= new ArrayList<>();
    public List<ReceptionSlot> timeslots= new ArrayList<>();

    public Schedule(String t, String s, String s1, List<String> today) {
        title=t;
        startTime=s;
        endTime=s1;
        namedtimeslots=today;
    }

    public Schedule() {
    }

    public String[] getNamedTimeSlots() {
        return namedtimeslots.toArray(new String[0]);
    }


    public void addSlot(String name, String time, ReceptionSlot receptionSlot) {
        //Warning! time is SORTED!!!!!!!!!!!!!!!!!!!!
        title = name;
        if(null==startTime) {
            startTime=time;
            endTime=time;
        } else {
            endTime=time;
        }
        namedtimeslots.add(name + " "+time);
        timeslots.add(receptionSlot);
    }

    public ReceptionSlot getReceptionSlot(int i){
        return timeslots.get(i);
    }

    public int size() {
        return timeslots.size();
    }
}
