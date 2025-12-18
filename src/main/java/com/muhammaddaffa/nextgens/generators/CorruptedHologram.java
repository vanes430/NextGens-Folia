package com.muhammaddaffa.nextgens.generators;

import com.muhammaddaffa.mdlib.utils.Common;
import com.muhammaddaffa.mdlib.utils.LocationUtils;
import com.muhammaddaffa.nextgens.NextGens;
import com.muhammaddaffa.nextgens.generators.hologram.FancyHologramsHook;
import com.muhammaddaffa.nextgens.utils.FoliaHelper;
import com.muhammaddaffa.nextgens.utils.Settings;
import eu.decentsoftware.holograms.api.DHAPI;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import me.filoghost.holographicdisplays.api.HolographicDisplaysAPI;
import me.filoghost.holographicdisplays.api.Position;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.List;

public class CorruptedHologram {

    private final ActiveGenerator active;
    private final Location hologramLocation;
    private final String name;
    private final HologramProvider provider;

    private enum HologramProvider {
        FANCY_HOLOGRAMS,
        DECENT_HOLOGRAMS,
        HOLOGRAPHIC_DISPLAYS,
        NONE
    }

    public CorruptedHologram(ActiveGenerator active) {
        this.active = active;
        this.hologramLocation = active.getLocation().clone().add(0.5, Settings.CORRUPTION_HOLOGRAM_HEIGHT, 0.5);
        this.name = this.getCleanNames(LocationUtils.serialize(this.hologramLocation));
        
        if (Bukkit.getPluginManager().isPluginEnabled("FancyHolograms")) {
            this.provider = HologramProvider.FANCY_HOLOGRAMS;
        } else if (Bukkit.getPluginManager().isPluginEnabled("DecentHolograms")) {
            this.provider = HologramProvider.DECENT_HOLOGRAMS;
        } else if (Bukkit.getPluginManager().isPluginEnabled("HolographicDisplays")) {
            this.provider = HologramProvider.HOLOGRAPHIC_DISPLAYS;
        } else {
            this.provider = HologramProvider.NONE;
        }
    }

    public void spawn() {
        // get the hologram lines
        List<String> lines = Common.color(Settings.CORRUPTION_HOLOGRAM_LINES);

        switch (provider) {
            case FANCY_HOLOGRAMS -> FoliaHelper.runAtLocation(this.hologramLocation, () -> {
                FancyHologramsHook.spawn(this.name, this.hologramLocation, lines);
            });
            case DECENT_HOLOGRAMS -> {
                // destroy the hologram if exists
                if (DHAPI.getHologram(this.name) != null) {
                    return;
                }
                DHAPI.createHologram(this.name, this.hologramLocation, lines);
            }
            case HOLOGRAPHIC_DISPLAYS -> FoliaHelper.runAtLocation(this.hologramLocation, () -> {
                // get the holographic api
                HolographicDisplaysAPI api = HolographicDisplaysAPI.get(NextGens.getInstance());
                // get the hologram position
                Position position = Position.of(this.hologramLocation);
                // spawn the hologram at the location
                me.filoghost.holographicdisplays.api.hologram.Hologram hologram = api.createHologram(position);
                lines.forEach(line -> hologram.getLines().appendText(line));
            });
        }
    }

    public void destroy() {
        switch (provider) {
            case FANCY_HOLOGRAMS -> FoliaHelper.runAtLocation(this.hologramLocation, () -> {
                FancyHologramsHook.destroy(this.name);
            });
            case DECENT_HOLOGRAMS -> {
                Hologram hologram = DHAPI.getHologram(this.name);
                // destroy the hologram if exists
                if (hologram != null) {
                    hologram.destroy();
                }
            }
            case HOLOGRAPHIC_DISPLAYS -> FoliaHelper.runAtLocation(this.hologramLocation, () -> {
                // get the holographic api
                HolographicDisplaysAPI api = HolographicDisplaysAPI.get(NextGens.getInstance());
                // get the position of the hologram
                Position position = Position.of(this.hologramLocation);
                // get the hologram based on the position
                for (me.filoghost.holographicdisplays.api.hologram.Hologram hologram : api.getHolograms()) {
                    if (hologram.getPosition().toLocation().equals(position.toLocation())) {
                        hologram.delete();
                    }
                }
            });
        }
    }

    private String getCleanNames(String text) {
        return text.replace(",", "")
                .replace(".", "")
                .replace(";", "")
                .replace("-", "_");
    }

}
