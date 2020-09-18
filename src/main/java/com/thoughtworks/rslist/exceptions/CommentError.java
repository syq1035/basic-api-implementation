package com.thoughtworks.rslist.exceptions;

import lombok.Getter;
import lombok.Setter;

public class CommentError {
    private String error;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
