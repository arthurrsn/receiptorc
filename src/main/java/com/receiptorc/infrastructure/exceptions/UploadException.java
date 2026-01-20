package com.receiptorc.infrastructure.exceptions;

public class UploadException extends RuntimeException{
    public UploadException () {super("The file arrived in system but had a problem to upload it.");}
    public UploadException (String message) { super(message); }
}
