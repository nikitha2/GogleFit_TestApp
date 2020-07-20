package com.nikitha.android.fittestapp;

import com.google.android.gms.fitness.data.Value;

public class DatenStepsPojo implements Comparable< DatenStepsPojo >{
    String date;
    String steps;
    int type;

    public DatenStepsPojo(String date, String steps) {
        this.date = date;
        this.steps = steps;
        this.type=1;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }



    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSteps() {
        return steps;
    }

    public void setSteps(String steps) {
        this.steps = steps;
    }

    @Override
    public String toString() {
        return "DatenStepsPojo [date=" + date + "]";
    }

    @Override
    public int compareTo(DatenStepsPojo o) {
        return this.getDate().compareTo(o.getDate());
    }

}
