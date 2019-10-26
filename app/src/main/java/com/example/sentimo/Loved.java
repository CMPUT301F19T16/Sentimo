package com.example.sentimo;

public class Loved extends Emotion {

    private String name;
    private String colour;
    //private Image emoji;

    public Loved(){
        super("Loved", "#FF7373");
        this.name = "Loved";
        this.colour = "#FF7373";
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
