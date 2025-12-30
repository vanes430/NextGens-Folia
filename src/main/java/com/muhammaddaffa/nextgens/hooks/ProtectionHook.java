package com.muhammaddaffa.nextgens.hooks;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public interface ProtectionHook {

    boolean canAccess(Player player, Block block);

}
