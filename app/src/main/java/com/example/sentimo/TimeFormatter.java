package com.example.sentimo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

/**
 * A class for parsing a calendar date and time from user provided text fields
 */
public class TimeFormatter {
    private Date time;

    public TimeFormatter() {
        this.time = time;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    /**
     * Parses date and time from strings to a Date
     * @param date User provided calendar date string
     * @param time User provided time string
     * @throws ParseException
     */
    public void setTimeFormat(String date, String time) throws ParseException {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy hh:mm", Locale.CANADA);
        cal.setTime(Objects.requireNonNull(df.parse(date + " " + time)));
        this.time = new Date(cal.getTimeInMillis());
    }

    public String getTimeString() {
        SimpleDateFormat df = new SimpleDateFormat("hh:mm", Locale.CANADA);
        Date cal = new Date();
        cal.setTime(this.time.getTime());
        final String format = df.format(cal);
        return format;
    }

    public String getDateString() {
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.CANADA);
        Date cal = new Date();
        cal.setTime(this.time.getTime());
        final String format = df.format(cal);
        return format;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TimeFormatter that = (TimeFormatter) o;
        String timeString = getTimeString();
        String dateString = getDateString();
        return timeString.equals(((TimeFormatter) o).getTimeString()) &&
                dateString.equals(((TimeFormatter) o).getDateString());
    }

    @Override
    public int hashCode() {
        String timeString = getTimeString();
        String dateString = getDateString();
        int hash = 7;
        hash = 31 * hash + (timeString == null ? 0 : timeString.hashCode());
        hash = 31 * hash + (dateString == null ? 0 : dateString.hashCode());
        return hash;
    }

}
