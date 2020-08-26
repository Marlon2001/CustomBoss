package me.marlon.customboss.Utils;

public class StringBoolean {
    private final String s;
    private final Boolean b;

    public StringBoolean(String s, Boolean b) {
        this.s = s;
        this.b = b;
    }

    public String getString() {
        return this.s;
    }

    public Boolean getBoolean() {
        return this.b;
    }
}
