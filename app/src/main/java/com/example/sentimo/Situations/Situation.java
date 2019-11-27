package com.example.sentimo.Situations;

public class Situation {
    String name;

    public Situation() {}

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

    public String getName() {
        return this.name;
    }
}
