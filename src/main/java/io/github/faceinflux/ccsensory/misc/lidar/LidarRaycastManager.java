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
    private static final HashMap<Integer, LidarScanResult> RESULTS = new HashMap<>();
    private static final LidarRaycastThread THREAD = new LidarRaycastThread();

    static {
        THREAD.start();
    }

    public static synchronized int queueScan(LidarScanRequest data) {
        int id = Integer.MIN_VALUE; // Overkill, but no harm in doing this I don't think
        while (!IDS.contains(id)) {id++;} // Iterate up until free id found.

        data.id = id;
        IDS.add(id);
        REQUESTS.add(data);

        THREAD.notify(); // Wake up the thread if it was waiting for smth to be queued

        return id;
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

    public static synchronized @Nullable LidarScanRequest dequeueRequest() {
        return REQUESTS.pollFirst();
    }

    public static synchronized void pushResult(int id, LidarScanResult result) {
        RESULTS.put(id, result);
    }
}
