package com.muhammaddaffa.nextgens.hooks.griefprevention;

import com.muhammaddaffa.nextgens.hooks.ProtectionHook;
import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import me.ryanhamshire.GriefPrevention.PlayerData;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class GPHook implements ProtectionHook {

    @Override
    public boolean canAccess(Player player, Block block) {
        PlayerData playerData = GriefPrevention.instance.dataStore.getPlayerData(player.getUniqueId());
        Claim claim = GriefPrevention.instance.dataStore.getClaimAt(block.getLocation(), true, playerData.lastClaim);

        if (claim == null) {
            return true;
        }

        return claim.allowAccess(player) == null;
    }

}