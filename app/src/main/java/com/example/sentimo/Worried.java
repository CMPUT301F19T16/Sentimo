package com.example.sentimo;

public class Worried extends Emotion {

    private String name;
    private String colour;
    //private Image emoji;

    public Worried(){
        super("Worried", "#A1D6D0");
        this.name = "Worried";
        this.colour = "#A1D6D0";
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
