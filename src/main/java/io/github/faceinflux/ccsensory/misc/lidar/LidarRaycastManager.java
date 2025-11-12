package io.github.faceinflux.ccsensory.misc.lidar;

import net.minecraft.util.Tuple;
import org.jspecify.annotations.Nullable;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Queue;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

public final class LidarRaycastManager {
    private static final LinkedBlockingDeque<LidarScanRequest> REQUESTS = new LinkedBlockingDeque<>();
    private static final ArrayList<Integer> IDS = new ArrayList<>();
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
