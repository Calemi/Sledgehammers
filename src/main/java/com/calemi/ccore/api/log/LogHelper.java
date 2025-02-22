package com.calemi.ccore.api.log;

import com.calemi.ccore.api.string.StringHelper;
import net.minecraft.world.level.Level;

import java.lang.management.ManagementFactory;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Use this class to help log information.
 * Great for debugging!
 */
public class LogHelper {

    /**
     * Use this method to send a message to the console on both the client and server side.
     * This method will also show what side the message came from.
     * NOTE: Will only show up when running in debug mode.
     * @param modName The mod the message is coming from.
     * @param level   Used to determine the side.
     * @param message The message you wish to print.
     */
    public static void logCommon(String modName, Level level, Object message) {
        log(modName, (level.isClientSide() ? "[CLIENT] " : "[SERVER] ") + message);
    }

    /**
     * Use this method to send a simple message to the console.
     * NOTE: Will only show up when running in debug mode.
     * @param modName The mod the message is coming from.
     * @param message The message you wish to print.
     */
    public static void log(String modName, Object message) {
        boolean isDebug = ManagementFactory.getRuntimeMXBean().getInputArguments().toString().indexOf("-agentlib:jdwp") > 0;
        if (isDebug) System.out.println(getFormatterTime() + " " + StringHelper.boxString(modName) + ": " + message);
    }

    private static String getFormatterTime() {
        return StringHelper.boxString(new SimpleDateFormat("HH:mm:ss").format(new Date()));
    }
}