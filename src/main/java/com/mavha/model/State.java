package com.mavha.model;

public enum State {
    CREATED,
    INPROGRESS,
    COMPLETED;

    public String getFormatState(String value) {
        switch (this) {
            case CREATED:
                return "Created";
            case INPROGRESS:
                return "In Progress";
            case COMPLETED:
                return "Complete";
            default:
                throw new AssertionError("Unknown State " + this);
        }
    }

}