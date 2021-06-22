package com.malaab.ya.action.actionyamalaab.admin.exceptions;

import java.io.IOException;


public class NoConnectivityException extends IOException {

    @Override
    public String getMessage() {
        return "No Connectivity exception";
    }
}