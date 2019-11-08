package com.example.sentimo.Emotions;


import com.example.sentimo.R;

/**
 *  This is a class that represents a specific emotion.
 */
public class Emotion {
    private String colour;
    private String name;
    private int image;

    /**
     * Constructor for the Emotion class.
     * @param name
     *      This is the name of the emotion.
     * @param colour
     *      This is the associated colour of the emotion.
     *      It is used for displaying colours in.
     * @param image
     *      This is the associated R.drawable.* file associated with the
     *      emotion. It is used to display a .png file in the app.
     */
    public Emotion(String name, String colour, int image){
        this.name = name;
        this.colour = colour;
        this.image = image;
    }

    /**
     * Get function that returns a string that represents the colour
     * of the emotion.
     * @return
     *      Returns the colour.
     */
    public String getColour(){
        return this.colour;
    }

    /**
     * Get function that returns a string that represents the name
     * of the emotion.
     * @return
     *      Returns the name.
     */
    public String getName(){
        return this.name;
    }

    /**
     * Get function that returns a integer that represents the
     * R.drawable file of the emotion.
     * @return
     *      Returns the R.drawable file.
     */
    public int getImage() { return this.image; }

}
