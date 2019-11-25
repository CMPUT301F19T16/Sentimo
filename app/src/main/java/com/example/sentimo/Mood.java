package com.example.sentimo;

import android.location.Location;
import android.net.Uri;
import android.util.Log;

import com.example.sentimo.Emotions.Emotion;
import com.example.sentimo.Situations.Situation;

import java.io.Serializable;
import java.util.Objects;

/**
 * A class to contain information about a mood.
 * TODO: Implement Location and Photo.
 */
public class Mood implements Serializable, Comparable{

    private TimeFormatter time;
    private Emotion emotion;
    private String reason;
    private Situation situation;
    private Double longitude;
    private Double latitude;
    private String onlinePath;
    private String username;

    /**
     * Constructor for a null Mood object
     */
    public Mood(){
        time = null;
        emotion = null;
        reason = null;
        situation = null;
        longitude = null;
        latitude = null;
        onlinePath = null;
    }

    /**
     * The constructor to create a copy of a mood object
     * @param mood the Mood to be copied
     */
    public Mood(Mood mood) {
        time = mood.getTime();
        emotion = mood.getEmotion();
        reason = mood.getReason();
        situation = mood.getSituation();
        longitude = mood.getLongitude();
        latitude = mood.getLatitude();
        onlinePath = mood.getOnlinePath();
    }

    /**
     * The constructor to create a mood object
     * @param time the time of the Mood as a TimeFormatter object
     * @param emotion the emotion of the mood as an Emotion object
     * @param reason the text reason for the mood as a String (optional)
     * @param situation the situation of the mood as a Situation object (optional)
     * @param longitude the longitude of a Mood, as a Double object (optional)
     * @param latitude the latitude of a Mood, as a Double object (optional)
     * @param onlinePath the database path to a cloud stored image (optional)
     */
    public Mood(TimeFormatter time, Emotion emotion, String reason,
                Situation situation, Double longitude, Double latitude, String onlinePath){
        this.time = time;
        this.emotion = emotion;
        this.reason = reason;
        this.situation = situation;
        this.longitude = longitude;
        this.latitude = latitude;
        this.onlinePath = onlinePath;
    }

    /**
     * Get the time of the mood
     * @return time as a TimeFormatter object
     */
    public TimeFormatter getTime() {
        return this.time;
    }

    /**
     * Set the time of a mood
     * @param time a TimeFormatter object
     */
    public void setTime(TimeFormatter time) {
        this.time = time;
    }

    /**
     * Get the emotion for this mood
     * @return an Emotion object
     */
    public Emotion getEmotion() {
        return emotion;
    }

    /**
     * Set the emotion of this mood
     * @param emotion an Emotion object
     */
    public void setEmotion(Emotion emotion) {
        this.emotion = emotion;
    }

    /**
     * Get the reason for this mood
     * @return the reason as a string
     */
    public String getReason() {
        return reason;
    }

    /**
     * Set the reason for this mood
     * @param reason the reason as a string
     */
    public void setReason(String reason) {
        this.reason = reason;
    }

    /**
     * Get the situation for this mood
     * @return a Situation object
     */
    public Situation getSituation() {
        return situation;
    }

    /**
     * Set the situation for this mood
     * @param situation the Situation object
     */
    public void setSituation(Situation situation) {
        this.situation = situation;
    }


    /**
     * Get the longitude for this mood
     * @return a Double object representing longitude in degrees
     */
    public Double getLongitude() {
        return longitude;
    }

    /**
     * A setter for longitude and latitude together, since both are required if either is present
     * @param longitude
     * @param latitude
     */
    public void setLongitudeLatitude(double longitude, double latitude) {
        this.longitude = new Double(longitude);
        this.latitude = new Double(latitude);
    }

    /**
     * Get the latitude for this mood
     * @return a Double object representing latitude in degrees
     */
    public Double getLatitude() {
        return latitude;
    }

    /**
     * Get the database path for an image associated with a Mood
     * @return the Firebase cloud storage path for the image associated with the Mood
     */
    public String getOnlinePath() {
        return onlinePath;
    }

    /**
     * Set the database path for an image associated with a Mood
     * @param onlinePath the new Firebase cloud storage path for the image associated with the Mood
     */
    public void setOnlinePath(String onlinePath) {
        this.onlinePath = onlinePath;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Mood mood = (Mood) o;
        return  time.equals(mood.time) &&
                emotion.equals(mood.emotion) &&
                Objects.equals(reason, mood.reason) &&
                Objects.equals(situation, mood.situation) && Objects.equals(longitude, mood.longitude)
                && Objects.equals(latitude, mood.latitude) && Objects.equals(onlinePath, mood.getOnlinePath());
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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
        if (longitude != null && latitude != null) {
            hash = 31 * hash + (longitude.hashCode());
            hash = 31 * hash + (longitude.hashCode());
        }
        if (onlinePath != null) {
            hash = 31 * hash + (onlinePath.hashCode());
        }
        return hash;
    }

    @Override
    public int compareTo(Object o) {
        return this.time.getTime().compareTo(((Mood) o).getTime().getTime());
    }

}
