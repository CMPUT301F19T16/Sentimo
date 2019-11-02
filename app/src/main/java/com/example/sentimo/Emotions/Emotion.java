package com.example.sentimo.Emotions;


public class Emotion {
    private String colour;
    private String name;
    //private Image emotion;

    public Emotion() {}

    public Emotion(String name, String colour){
        this.name = name;
        this.colour = colour;
    }

    public String getColour(){
        return this.colour;
    }
    public String getName(){
        return this.name;
    }

}
