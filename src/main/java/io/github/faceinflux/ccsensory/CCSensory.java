package io.github.faceinflux.ccsensory;

import com.mojang.logging.LogUtils;
import org.slf4j.Logger;

public class CCSensory {
    public static final String ID = "ccsensory";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static void initialize() {
        LOGGER.info("Hello from MyMod!");
    }
}
