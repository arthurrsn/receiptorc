package com.receiptorc.infrastructure.exceptions;

public class NotFoundException extends RuntimeException {
    public NotFoundException () {super("Not found exception.");}
    public NotFoundException (String message) { super(message); }
}
