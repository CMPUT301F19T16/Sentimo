package com.example.sentimo;

import com.example.sentimo.Emotions.Emotion;
import com.example.sentimo.Situations.Situation;

import java.io.Serializable;
import java.util.Objects;

public class Mood implements Serializable {

    private String date;
    private String time;
    private Emotion emotion;
    private String reason;
    private Situation situation;
    private Boolean locationPermission;
    //private Location location
    //private Photo photo;

    public Mood(){}

    public Mood(String date, String time, Emotion emotion, String reason,
                Situation situation, Boolean locationPermission){
        this.date = date;
        this.time = time;
        this.emotion = emotion;
        this.reason = reason;
        this.situation = situation;
        this.locationPermission = locationPermission;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Emotion getEmotion() {
        return emotion;
    }

    public void setEmotion(Emotion emotion) {
        this.emotion = emotion;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Situation getSituation() {
        return situation;
    }

    public void setSituation(Situation situation) {
        this.situation = situation;
    }

    public Boolean getLocationPermission() {return locationPermission;}

    public void setLocationPermission(Boolean locationPermission){this.locationPermission = locationPermission;}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Mood mood = (Mood) o;
        return date.equals(mood.date) &&
                time.equals(mood.time) &&
                emotion.equals(mood.emotion) &&
                Objects.equals(reason, mood.reason) &&
                Objects.equals(situation, mood.situation);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + (date == null ? 0 : date.hashCode());
        hash = 31 * hash + (time == null ? 0 : time.hashCode());
        hash = 31 * hash + (emotion.getColour() == null ? 0 : emotion.getColour().hashCode());
        hash = 31 * hash + (emotion.getName() == null ? 0 : emotion.getName().hashCode());
        hash = 31 * hash + (reason == null ? 0 : reason.hashCode());
        hash = 31 * hash + (situation == null ? 0 : situation.hashCode());
        return hash;
    }
}
