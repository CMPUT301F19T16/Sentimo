package com.example.sentimo;

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


}
