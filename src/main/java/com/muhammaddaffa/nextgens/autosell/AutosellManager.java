package com.muhammaddaffa.nextgens.autosell;

import com.muhammaddaffa.nextgens.NextGens;
import com.muhammaddaffa.nextgens.sell.SellManager;
import com.muhammaddaffa.nextgens.users.models.User;
import com.muhammaddaffa.nextgens.users.UserManager;
import com.muhammaddaffa.nextgens.utils.FoliaHelper;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public record AutosellManager(
        UserManager userManager
) {

    public void startTask() {
        FoliaHelper.runSyncTimer(() -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                FoliaHelper.runAtEntity(player, () -> {
                    User user = this.userManager.getUser(player);
                    // check if player has autosell inventory permission
                    if (!user.isToggleInventoryAutoSell()) return;
                    // get the interval
                    int sellInterval = NextGens.DEFAULT_CONFIG.getConfig().getInt("autosell.inventory-interval");
                    // check if sell interval is equals or greater than user interval
                    if (sellInterval >= user.getInterval()) {
                        // sell the inventory
                        NextGens.getInstance().getSellManager().performSell(player, null, true, player.getInventory());
                        // set interval back to 0
                        user.setInterval(0);
                        return;
                    }
                    // update the player interval
                    user.updateInterval(1);
                });
            }
        }, 1L, 20L);
    }

}
