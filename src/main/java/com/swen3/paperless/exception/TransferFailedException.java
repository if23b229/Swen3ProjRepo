package com.swen3.paperless.exception;

public class TransferFailedException extends RuntimeException {
    public TransferFailedException() {
        super("Failed to transfer file");
    }
}
