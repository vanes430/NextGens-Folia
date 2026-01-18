package com.muhammaddaffa.nextgens.hooks.huskclaims;

import com.muhammaddaffa.nextgens.hooks.ProtectionHook;
import net.william278.huskclaims.api.BukkitHuskClaimsAPI;
import net.william278.huskclaims.claim.Claim;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class HuskClaimsHook implements ProtectionHook {

    private final BukkitHuskClaimsAPI huskClaimsAPI;

    public HuskClaimsHook() {
        this.huskClaimsAPI = BukkitHuskClaimsAPI.getInstance();
    }

    @Override
    public boolean canAccess(Player player, Block block) {
        Claim claim = huskClaimsAPI.getClaimAt(huskClaimsAPI.getPosition(block.getLocation())).orElse(null);
        if (claim == null) return true;

        UUID ownerId = claim.getOwner().orElse(null);
        return ownerId != null && ownerId.equals(player.getUniqueId());
    }
}
