package dev.journey.toolkit.retrofit;

/**
 * Created by mwp on 2016/1/21.
 */
public interface ProgressListener {
    void update(long bytesRead, long contentLength, boolean done);
}
