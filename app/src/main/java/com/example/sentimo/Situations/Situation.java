package com.example.sentimo.Situations;

/**
 * This class represents the situation associated with a Mood object. A situation
 * is an optional parameter of a Mood and has the options of being "Alone", "With One Person",
 * "With Several People", or "With a Crowd". The situation is chosen by a SituationType
 * that is passed when a new situation is created.
 */
public class Situation {
    String name;

    public Situation() {}

    /**
     * This function takes a SituationType and sets the name of the
     * Situation accordingly. If the situation is not one of the four situations allowed,
     * a runtime exception is thrown.
     * @param situationType
     *  An enum that represents all the possible situations.
     */
    public Situation(SituationType situationType) {
        switch (situationType) {
            case Alone:
                this.name = "Alone";
                break;
            case OnePerson:
                this.name = "One Person";
                break;
            case SeveralPeople:
                this.name = "Several People";
                break;
            case Crowd:
                this.name = "Crowd";
                break;
            default:
                throw new RuntimeException("Unknown SituationType");
        }
    }

    /**
     * This function returns the name of the situation. The name of the situation is
     * decided on the creation of the situation.
     * @return
     *  A string that represents the name of the Situation object.
     */
    public String getName() {
        return this.name;
    }
}
