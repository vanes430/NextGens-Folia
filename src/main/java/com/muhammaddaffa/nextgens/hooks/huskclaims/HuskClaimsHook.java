package com.muhammaddaffa.nextgens.hooks.huskclaims;

import com.muhammaddaffa.nextgens.hooks.ProtectionHook;
import net.william278.huskclaims.api.BukkitHuskClaimsAPI;
import net.william278.huskclaims.libraries.cloplib.operation.OperationType;
import net.william278.huskclaims.position.Position;
import net.william278.huskclaims.user.OnlineUser;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class HuskClaimsHook implements ProtectionHook {

    private final BukkitHuskClaimsAPI huskClaimsAPI;

    public HuskClaimsHook() {
        this.huskClaimsAPI = BukkitHuskClaimsAPI.getInstance();
    }

    private static BukkitHuskClaimsAPI getAPI() {
        return BukkitHuskClaimsAPI.getInstance();
    }

    public static boolean canInteract(Player player, Location location) {
        return checkPermission(player, location);
    }

    public static boolean canBreak(Player player, Location location) {
        return checkPermission(player, location);
    }

    public static boolean canPlace(Player player, Location location) {
        return checkPermission(player, location);
    }

    private static boolean checkPermission(Player player, Location location) {
        Position position = getAPI().getPosition(location);

        // Izinkan jika tidak ada claim di lokasi tersebut (Wilderness)
        if (getAPI().getClaimAt(position).isEmpty()) {
            return true;
        }

        // Izinkan jika pemain adalah pemilik (Owner) claim
        boolean isOwner = getAPI().getClaimOwnerAt(position)
                .map(owner -> owner.getUuid().equals(player.getUniqueId()))
                .orElse(false);

        if (isOwner) {
            return true;
        }

        // Cek apakah pemain memiliki akses CONTAINER_OPEN di dalam claim tersebut
        OnlineUser user = getAPI().getOnlineUser(player);
        return getAPI().isOperationAllowed(user, OperationType.CONTAINER_OPEN, position);
    }

    @Override
    public boolean canAccess(Player player, Block block) {
        return checkPermission(player, block.getLocation());
    }

}