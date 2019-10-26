package com.example.sentimo;

public class Embarrassed extends Emotion{

    private String name;
    private String colour;
    //private Image emoji;

    public Embarrassed(){
        super("Embarrassed", "#F33EAB");
        this.name = "Embarrassed";
        this.colour = "#F33EAB";
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
