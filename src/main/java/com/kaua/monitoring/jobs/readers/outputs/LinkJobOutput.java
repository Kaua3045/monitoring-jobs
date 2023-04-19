package com.kaua.monitoring.jobs.readers.outputs;

import java.io.Serializable;

public class LinkJobOutput implements Serializable {

    private String id;
    private String url;

    public LinkJobOutput() {}

    public LinkJobOutput(final String id, final String url) {
        this.id = id;
        this.url = url;
    }
}
