package com.octanepvp.splityosis.octaneworlds.exceptions;

public class InvalidWorldFolderException extends Exception{

    public InvalidWorldFolderException(String path) {
        super("Invalid world folder at '"+path+"'.");
    }
}
