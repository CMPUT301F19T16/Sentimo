package com.example.sentimo;

import java.io.Serializable;
import java.util.Objects;

public class Mood implements Serializable, Comparable {

    private TimeFormatter time;
    private Emotion emotion;
    private String reason;
    private Situation situation;
    private Boolean locationPermission;
    //private Location location
    //private Photo photo;

    public Mood(){}

    public Mood(TimeFormatter time, Emotion emotion, String reason,
                Situation situation, Boolean locationPermission){
        this.time = time;
        this.emotion = emotion;
        this.reason = reason;
        this.situation = situation;
        this.locationPermission = locationPermission;
    }
    public TimeFormatter getTime() {
        return this.time;
    }

    public void setTime(TimeFormatter time) {
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
        return  time.equals(mood.time) &&
                emotion.equals(mood.emotion) &&
                Objects.equals(reason, mood.reason) &&
                Objects.equals(situation, mood.situation);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + (time == null ? 0 : time.hashCode());
        hash = 31 * hash + (emotion.getColour() == null ? 0 : emotion.getColour().hashCode());
        hash = 31 * hash + (emotion.getName() == null ? 0 : emotion.getName().hashCode());
        hash = 31 * hash + (reason == null ? 0 : reason.hashCode());
        if (situation != null)
            hash = 31 * hash + (situation.getName() == null ? 0 : situation.getName().hashCode());
        return hash;
    }

    @Override
    public int compareTo(Object o) {
        return this.time.getTime().compareTo(((Mood) o).getTime().getTime());
    }
}
