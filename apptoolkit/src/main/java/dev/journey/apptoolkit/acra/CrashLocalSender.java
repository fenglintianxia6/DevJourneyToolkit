package dev.journey.apptoolkit.acra;

import android.content.Context;
import android.support.annotation.NonNull;

import org.acra.collector.CrashReportData;
import org.acra.config.ACRAConfiguration;
import org.acra.sender.ReportSender;
import org.acra.sender.ReportSenderException;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import dev.journey.toolkit.util.L;
import dev.journey.toolkit.util.StdFileUtils;
import dev.journey.toolkit.util.TimeUtils;

/**
 * Created by mengweiping on 16/5/14.
 */
public class CrashLocalSender implements ReportSender {
    ACRAConfiguration config;
    ExecutorService executorService;

    public CrashLocalSender(ACRAConfiguration config) {
        this.config = config;
        executorService = Executors.newSingleThreadExecutor();
    }

    @Override
    public void send(@NonNull final Context context, @NonNull final CrashReportData errorContent) throws ReportSenderException {
        L.d("CrashLocalSender", errorContent == null ? null : errorContent.toString());
        if (errorContent == null) {
            return;
        }
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                File dir = StdFileUtils.getDiskCacheDir(context, "Crash");
                File file = new File(dir, TimeUtils.createTimeStamp() + ".txt");
                String crashInfo = errorContent.toString();
                StdFileUtils.bytesToFile(crashInfo.getBytes(), file);
            }
        });
    }
}
