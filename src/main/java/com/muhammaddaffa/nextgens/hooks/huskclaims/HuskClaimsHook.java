package com.muhammaddaffa.nextgens.hooks.huskclaims;

import com.muhammaddaffa.nextgens.hooks.ProtectionHook;
import net.william278.huskclaims.api.BukkitHuskClaimsAPI;
import net.william278.huskclaims.libraries.cloplib.operation.OperationType;
import net.william278.huskclaims.position.Position;
import net.william278.huskclaims.user.OnlineUser;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class HuskClaimsHook implements ProtectionHook {

    private final BukkitHuskClaimsAPI huskClaimsAPI;

    public HuskClaimsHook() {
        this.huskClaimsAPI = BukkitHuskClaimsAPI.getInstance();
    }

    @Override
    public boolean canAccess(Player player, Block block) {
        OnlineUser user = huskClaimsAPI.getOnlineUser(player);
        Position position = huskClaimsAPI.getPosition(block.getLocation());
        return huskClaimsAPI.isOperationAllowed(user, OperationType.CONTAINER_OPEN, position);
    }
}
