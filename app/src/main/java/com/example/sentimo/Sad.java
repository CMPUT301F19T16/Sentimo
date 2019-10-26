package com.example.sentimo;

public class Sad extends Emotion{

    private String name;
    private String colour;
    //private Image emoji;

    public Sad(){
        super("Sad", "#6F8ACF");
        this.name = "Sad";
        this.colour = "#6F8ACF";
    }

    @Override
    public String getColour() {
        return this.colour;
    }

    @Override
    public String getName() {
        return this.name;
    }
}
