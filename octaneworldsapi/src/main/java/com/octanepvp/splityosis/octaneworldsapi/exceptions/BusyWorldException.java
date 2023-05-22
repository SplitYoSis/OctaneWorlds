package com.octanepvp.splityosis.octaneworldsapi.exceptions;

public class BusyWorldException extends Exception{

    public BusyWorldException(String worldName) {
        super("Cant run task on world '"+worldName+"', The world is currently under a different task");
    }
}
