package com.mindex.challenge.data;

import java.util.ArrayList;
import java.util.List;

public class ErrorResponse {
    private String message;
    private List<String> details;

    public ErrorResponse(String message){
        this.details = new ArrayList<>();
        this.message = message;
    }

    public void addDetail(String detail){
        details.add(detail);
    }

    public String getMessage() {
        return message;
    }

    public List<String> getDetails() {
        return details;
    }
}
