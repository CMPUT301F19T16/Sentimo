package com.example.sentimo;

import com.example.sentimo.Emotions.Emotion;
import com.example.sentimo.Situations.Situation;

import java.io.Serializable;
import java.util.Objects;

/**
 * A class to contain information about a mood.
 * TODO: Implement Location and Photo.
 */
public class Mood implements Serializable, Comparable {

    private TimeFormatter time;
    private Emotion emotion;
    private String reason;
    private Situation situation;
    private Boolean locationPermission;
    //private Location location
    //private Photo photo;

    public Mood(){}

    /**
     * Create a mood object
     * @param time the time of the mood as a TimeFormatter object
     * @param emotion the emotion of the mood as a Emotion object
     * @param reason the reason for the mood as a String
     * @param situation the situation of the mood as a Situation object
     * @param locationPermission whether the location permission has been granted as a Boolean
     */
    public Mood(TimeFormatter time, Emotion emotion, String reason,
                Situation situation, Boolean locationPermission){
        this.time = time;
        this.emotion = emotion;
        this.reason = reason;
        this.situation = situation;
        this.locationPermission = locationPermission;
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
     * Get the location permission for this mood
     * @return true if the mood has location permission granted, false if not
     */
    public Boolean getLocationPermission() {return locationPermission;}

    /**
     * Set the location permission for this mood
     * @param locationPermission true to grant the mood location permission, false to deny
     */
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
