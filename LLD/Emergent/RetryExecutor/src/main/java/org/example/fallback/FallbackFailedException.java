package org.example;

import java.util.List;

public class FallbackFailedException extends Exception{
    public FallbackFailedException(List<Exception> exceptionList) {

    }
}
