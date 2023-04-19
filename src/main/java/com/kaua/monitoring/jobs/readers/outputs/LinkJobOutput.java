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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
