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
    private static final HashMap<Integer, LidarScanResult> RESULTS = new HashMap<>(); // FIXME Clear out old results if the computer awaiting them nopes out
    private static final LidarRaycastThread THREAD = new LidarRaycastThread();

    static {
        THREAD.start();
    }

    public static int queueScan(LidarScanRequest data) {
        int id = 0;
        while (IDS.contains(id)) {id++;} // Iterate up until free id found.

        data.id = id;
        IDS.add(id);
        REQUESTS.add(data);

        return id;
    }

    public static LidarScanRequest dequeueRequest() throws InterruptedException {
        return REQUESTS.takeFirst();
    }

    public static synchronized boolean isReady(int id) {
        return RESULTS.containsKey(id);
    }

    public static synchronized LidarScanResult pullResult(int id) {
        if (!isReady(id)) {
            throw new NullPointerException("Scan result not ready.");
        }

        LidarScanResult result = RESULTS.remove(id); // Removing
        IDS.remove(id);
        return result;
    }

    public static synchronized void pushResult(int id, LidarScanResult result) {
        RESULTS.put(id, result);
    }
}
