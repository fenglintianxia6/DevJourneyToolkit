package dev.journey.toolkit.retrofit;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.internal.Util;
import okio.BufferedSink;
import okio.Okio;
import okio.Source;

/**
 * Created by mwp on 2016/1/21.
 */
public class ProgressRequestBody extends RequestBody {
    private static final int SEGMENT_SIZE = 2048;
    private final File file;
    private final ProgressListener listener;
    private final MediaType contentType;

    public ProgressRequestBody(File file, MediaType contentType, ProgressListener listener) {
        this.file = file;
        this.contentType = contentType;
        this.listener = listener;
    }

    @Override
    public long contentLength() throws IOException {
        return file.length();
    }

    @Override
    public MediaType contentType() {
        return contentType;
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        Source source = null;
        try {
            source = Okio.source(file);
            long total = 0;
            long read;

            while ((read = source.read(sink.buffer(), SEGMENT_SIZE)) != -1) {
                total += read;
                sink.flush();
                this.listener.update(total, contentLength(), read == -1);
            }
        } finally {
            Util.closeQuietly(source);
        }
    }
}
