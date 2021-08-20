package com.neu360.x_lab.laboratorio.Globales;

import java.util.Calendar;

public  class VariablesGlobales {

    Calendar calendar = Calendar.getInstance();


    public int getANIO_ACTUAL() {
        return calendar.get(Calendar.YEAR);
    }


    public int getMES_ACTUAL() {
        return calendar.get(Calendar.YEAR);
    }


    public int getDIA_ACTUAL() {
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

}
