package com.example.sentimo;

import java.util.Objects;

public class Mood {

    private String date;
    private String time;
    private Emotion emotion;
    private String reason;
    private String situation;
    //private Location location
    //private Photo photo;

    public Mood(String date, String time, Emotion emotion, String reason,
                String situation){
        this.date = date;
        this.time = time;
        this.emotion = emotion;
        this.reason = reason;
        this.situation = situation;
    }

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
        return Objects.hash(date, time, emotion, reason, situation);
    }
}
