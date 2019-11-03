package com.example.sentimo.Emotions;


import com.example.sentimo.R;

public class Emotion {
    private String colour;
    private String name;
    private int image;

    public Emotion() {}

    public Emotion(String name, String colour, int image){
        this.name = name;
        this.colour = colour;
        this.image = image;
    }

    public String getColour(){
        return this.colour;
    }
    public String getName(){
        return this.name;
    }
    public int getImage() { return this.image; }

}
