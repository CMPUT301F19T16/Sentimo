package com.example.sentimo;

public class Confident extends Emotion {

    private String name;
    private String colour;
    //private Image emoji;

    public Confident(){
        super("Confident", "#EDF961");
        this.name = "Confident";
        this.colour = "#EDF961";
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
