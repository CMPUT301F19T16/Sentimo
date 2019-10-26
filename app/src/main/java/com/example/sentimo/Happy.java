package com.example.sentimo;

public class Happy extends Emotion {

    private String name;
    private String colour;
    //private Image emoji;

    public Happy(){
        super("Happy", "#23E775");
        this.name = "Happy";
        this.colour = "#23E775";
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
