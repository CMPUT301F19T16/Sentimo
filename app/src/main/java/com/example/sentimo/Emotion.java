package com.example.sentimo;

public abstract class Emotion {
    private String colour;
    private String name;
    //private Image emotion;

    public Emotion(String name, String colour){
        this.name = name;
        this.colour = colour;
    }

    public abstract String getColour();
    public abstract String getName();

}
