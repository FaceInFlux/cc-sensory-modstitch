package io.github.faceinflux.ccsensory.misc.lidar;

import java.util.concurrent.LinkedBlockingDeque;

public final class LidarRaycastManager {
    private static final LinkedBlockingDeque<LidarScanRequest> REQUESTS = new LinkedBlockingDeque<>();
    private static final LidarRaycastThread THREAD = new LidarRaycastThread();

    static {
        THREAD.start();
    }

    public static void queueScan(LidarScanRequest data) {
        REQUESTS.add(data);
    }

    public static LidarScanRequest dequeueRequest() throws InterruptedException {
        return REQUESTS.takeFirst();
    }
}
