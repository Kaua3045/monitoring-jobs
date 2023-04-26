package com.kaua.monitoring.jobs.readers.outputs;

import java.io.Serializable;

public class LinkJobOutput implements Serializable {

    private String id;
    private String url;
    private String linkExecution;

    public LinkJobOutput() {}

    public LinkJobOutput(final String id, final String url, final String linkExecution) {
        this.id = id;
        this.url = url;
        this.linkExecution = linkExecution;
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

    public String getLinkExecution() {
        return linkExecution;
    }

    public void setLinkExecution(String linkExecution) {
        this.linkExecution = linkExecution;
    }
}
