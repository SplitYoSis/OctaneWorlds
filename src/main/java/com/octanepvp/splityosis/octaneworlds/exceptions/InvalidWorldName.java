package com.octanepvp.splityosis.octaneworlds.exceptions;

public class InvalidWorldName extends Exception{

    public InvalidWorldName(String name) {
        super("A World with the name '"+name+"' already exists.");
    }
}
