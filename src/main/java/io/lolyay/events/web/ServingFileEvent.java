package io.lolyay.events.web;

import com.sun.net.httpserver.HttpExchange;
import io.lolyay.eventbus.Event;

public class ServingFileEvent extends Event {
    private final String path;
    private final HttpExchange context;

    public ServingFileEvent(String path, HttpExchange context) {
        this.path = path;
        this.context = context;
    }

    public String getPath() {
        return path;
    }

    public HttpExchange getContext() {
        return context;
    }

}
