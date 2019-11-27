package com.example.sentimo.Emotions;


import com.example.sentimo.R;


/**
 *  This is a class that represents a specific emotion.
 */
public class Emotion{
    private String colour;
    private String name;
    private int image;

    public Emotion(){}
//    /**
//     * Constructor for the Emotion class.
//     * @param name
//     *      This is the name of the emotion.
//     * @param colour
//     *      This is the associated colour of the emotion.
//     *      It is used for displaying colours in.
//     * @param image
//     *      This is the associated R.drawable.* file associated with the
//     *      emotion. It is used to display a .png file in the app.
//     */
//    private Emotion(String name, String colour, int image){
//        this.name = name;
//        this.colour = colour;
//        this.image = image;
//    }

    public Emotion(EmotionType emotionType) {
        switch (emotionType) {
            case Confident:
                this.name = "Confident";
                this.colour = "#EDF961";
                this.image = R.drawable.confident;
                break;
            case Embarassed:
                this.name = "Embarassed";
                this.colour = "#F33EAB";
                this.image = R.drawable.embarrased;
                break;
            case Happy:
                this.name = "Happy";
                this.colour = "#23E775";
                this.image = R.drawable.happy;
                break;
            case Loved:
                this.name = "Loved";
                this.colour = "#FF7373";
                this.image = R.drawable.loved;
                break;
            case Mad:
                this.name = "Mad";
                this.colour = "#CF542E";
                this.image = R.drawable.mad;
                break;
            case Sad:
                this.name = "Sad";
                this.colour = "#6F8ACF";
                this.image = R.drawable.sad;
                break;
            case Tired:
                this.name = "Tired";
                this.colour = "#182649";
                this.image = R.drawable.tired;
                break;
            case Worried:
                this.name = "Worried";
                this.colour = "#A1D6D0";
                this.image = R.drawable.worried;
                break;
            default:
                throw new RuntimeException("Unknown Emotion type");
        }
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
