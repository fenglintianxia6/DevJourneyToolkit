package dev.journey.toolkit.util;

import java.io.File;

import dev.journey.toolkit.retrofit.ProgressListener;

/**
 * Created by mwp on 2016/2/25.
 */
public interface FileDownloadListener extends ProgressListener {
    void onDownloadComplete(File f);

    void onFailure(Throwable throwable);

    void onStart();
}
