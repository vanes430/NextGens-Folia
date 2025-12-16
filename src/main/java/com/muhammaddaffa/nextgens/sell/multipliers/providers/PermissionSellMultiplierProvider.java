package com.muhammaddaffa.nextgens.sell.multipliers.providers;

import com.muhammaddaffa.nextgens.sell.multipliers.SellMultiplierProvider;
import com.muhammaddaffa.nextgens.sellwand.models.SellwandData;
import com.muhammaddaffa.nextgens.users.models.User;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;

public class PermissionSellMultiplierProvider implements SellMultiplierProvider {

    @Override
    public double getMultiplier(Player player, User user, SellwandData sellwand) {
        return this.getSellMultiplier(player);
    }

    private double getSellMultiplier(Player player) {
        if (player == null) return 0;

        // Iterate from a maximum reasonable multiplier downwards with a step
        // We assume multipliers can go up to 10.0, with increments of 0.5
        for (double multiplier = 10.0; multiplier >= 0.5; multiplier -= 0.5) {
            // Format the multiplier for the permission string (e.g., 1.5 becomes 1_5)
            String permission = "nextgens.multiplier.sell." + String.valueOf(multiplier).replace('.', '_');
            if (player.hasPermission(permission)) {
                return multiplier;
            }
        }
        return 0.0;
    }

}
