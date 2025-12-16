package com.muhammaddaffa.nextgens.utils;

import com.muhammaddaffa.nextgens.NextGens;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.TimeUnit;

public class FoliaHelper {

    private static boolean isFolia = false;
    private static JavaPlugin plugin;

    public static void setup(NextGens nextGens) {
        plugin = nextGens;
        try {
            Class.forName("io.papermc.paper.threadedregions.RegionizedServer");
            isFolia = true;
        } catch (ClassNotFoundException e) {
            isFolia = false;
        }
    }

    public static void runAtLocation(Location location, Runnable runnable) {
        if (isFolia) {
            Bukkit.getRegionScheduler().execute(plugin, location, runnable);
        } else {
            Bukkit.getScheduler().runTask(plugin, runnable);
        }
    }

    public static void runAtLocationLater(Location location, Runnable runnable, long delay) {
        if (isFolia) {
            Bukkit.getRegionScheduler().runDelayed(plugin, location, (task) -> runnable.run(), delay);
        } else {
            Bukkit.getScheduler().runTaskLater(plugin, runnable, delay);
        }
    }

    public static void runAtEntity(Entity entity, Runnable runnable) {
        if (isFolia) {
            entity.getScheduler().run(plugin, (task) -> runnable.run(), null);
        } else {
            Bukkit.getScheduler().runTask(plugin, runnable);
        }
    }

    public static void runLater(Runnable runnable, long delay) {
        if (isFolia) {
            Bukkit.getGlobalRegionScheduler().runDelayed(plugin, (task) -> runnable.run(), delay);
        } else {
            Bukkit.getScheduler().runTaskLater(plugin, runnable, delay);
        }
    }

    public static void runAsync(Runnable runnable) {
        if (isFolia) {
            Bukkit.getAsyncScheduler().runNow(plugin, (task) -> runnable.run());
        } else {
            Bukkit.getScheduler().runTaskAsynchronously(plugin, runnable);
        }
    }

    public static void runAsyncLater(Runnable runnable, long delay) {
        if (isFolia) {
            Bukkit.getAsyncScheduler().runDelayed(plugin, (task) -> runnable.run(), delay * 50, TimeUnit.MILLISECONDS);
        } else {
            Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, runnable, delay);
        }
    }

    public static Object runAsyncTimer(Runnable runnable, long delay, long period) {
        if (isFolia) {
            return Bukkit.getAsyncScheduler().runAtFixedRate(plugin, (task) -> runnable.run(), delay * 50, period * 50, TimeUnit.MILLISECONDS);
        } else {
            return Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, runnable, delay, period);
        }
    }

    public static void runSync(Runnable runnable) {
        if (isFolia) {
            Bukkit.getGlobalRegionScheduler().execute(plugin, runnable);
        } else {
            Bukkit.getScheduler().runTask(plugin, runnable);
        }
    }

    public static Object runSyncTimer(Runnable runnable, long delay, long period) {
        if (isFolia) {
            return Bukkit.getGlobalRegionScheduler().runAtFixedRate(plugin, (task) -> runnable.run(), delay, period);
        } else {
            return Bukkit.getScheduler().runTaskTimer(plugin, runnable, delay, period);
        }
    }

    public static void cancel(Object task) {
        if (isFolia) {
            try {
                task.getClass().getMethod("cancel").invoke(task);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            if (task instanceof org.bukkit.scheduler.BukkitTask) {
                ((org.bukkit.scheduler.BukkitTask) task).cancel();
            }
        }
    }
}
