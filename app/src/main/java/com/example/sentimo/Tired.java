package com.example.sentimo;

public class Tired extends Emotion {

    private String name;
    private String colour;
    //private Image emoji;

    public Tired(){
        super("Tired", "#182649");
        this.name = "Tired";
        this.colour = "#182649";
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
