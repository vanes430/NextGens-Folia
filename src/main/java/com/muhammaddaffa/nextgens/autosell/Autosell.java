package com.muhammaddaffa.nextgens.autosell;

import com.muhammaddaffa.mdlib.utils.Common;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;

public class Autosell {

    /**
     * Permissions:
     * nextgens.autosell.inv.(interval in seconds)
     * nextgens.autosell.gens
     */

    public static int getAutosellInventoryInterval(Player player) {
        // Iterate from smallest interval (1 second) to a reasonable maximum (60 seconds)
        for (int i = 1; i <= 60; i++) {
            if (player.hasPermission("nextgens.autosell.inv." + i)) {
                return i; // Return the smallest interval found
            }
        }
        return 0;
    }

    public static boolean hasAutosellInventoryPermission(Player player) {
        // If getAutosellInventoryInterval returns a value > 0, it means the player has an autosell inventory permission
        return getAutosellInventoryInterval(player) > 0;
    }

    public static boolean hasAutosellGensPermission(Player player) {
        return player.hasPermission("nextgens.autosell.gens");
    }

}
