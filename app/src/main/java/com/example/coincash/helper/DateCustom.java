package com.example.coincash.helper;

import java.text.SimpleDateFormat;

public class DateCustom {
    public static String dateAtual() {
        long date = System.currentTimeMillis();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String dateString = simpleDateFormat.format(date);
        return dateString;
    }

    public static String monthAndYear(String date) {

        String returnDate[] = date.split("/");
        String day = returnDate[0];
        String month = returnDate[1];
        String year = returnDate[2];
        String monthYear = month + year;
        return monthYear;
    }
}
