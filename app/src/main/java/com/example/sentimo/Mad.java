package com.example.sentimo;

public class Mad extends Emotion {

    private String name;
    private String colour;
    //private Image emoji;

    public Mad(){
        super("Mad", "#CF542E");
        this.name = "Mad";
        this.colour = "#CF542E";
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
