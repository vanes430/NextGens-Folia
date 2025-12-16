package com.muhammaddaffa.nextgens.users;

import com.muhammaddaffa.nextgens.NextGens;
import com.muhammaddaffa.nextgens.users.models.User;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class UserManager {

    private final Map<UUID, User> userMap = new ConcurrentHashMap<>();

    @Nullable
    public User getUser(String name) {
        return userMap.values().stream()
                .filter(user -> user.getName() != null && user.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    @NotNull
    public User getUser(Player player) {
        return getUser(player.getUniqueId());
    }

    @NotNull
    public User getUser(UUID uuid) {
        return userMap.computeIfAbsent(uuid, User::new);
    }

    public Collection<User> getUsers() {
        return userMap.values();
    }

    public List<String> getUsersName() {
        return userMap.values().stream()
                .map(User::getName)
                .collect(Collectors.toList());
    }

    public List<String> getUsersMemberName(Player player) {
        User user = this.getUser(player);
        return user.getMemberNames();
    }

    public List<String> getWhoUserAddedToPlayer(Player player) {
        return this.userMap.values().stream()
                .filter(user -> user.isMember(player.getUniqueId()))
                .map(User::getName)
                .collect(Collectors.toList());
    }

    public void addUser(User user) {
        userMap.put(user.getUniqueId(), user);
    }

    public void removeUser(UUID uuid) {
        userMap.remove(uuid);
    }

    public int getMaxSlot(Player player) {
        int max = 0;
        FileConfiguration config = NextGens.DEFAULT_CONFIG.getConfig();
        if (config.getBoolean("default-max-generator.enabled")) {
            max += config.getInt("default-max-generator.amount");
        }

        // Iterate from a high number down to 1 to find the highest nextgens.max.<amount> permission
        // Assuming a reasonable maximum of 1000, adjust if needed based on server's expected limits
        for (int i = 1000; i >= 1; i--) {
            if (player.hasPermission("nextgens.max." + i)) {
                max = Math.max(max, i); // Take the maximum between current max and the permission-based max
                break; // Found the highest permission, can stop
            }
        }

        int bonusMax = max + this.getUser(player).getBonus();
        int limit = config.getInt("player-generator-limit.limit");
        if (config.getBoolean("player-generator-limit.enabled") && bonusMax > limit) {
            return limit;
        }
        return bonusMax;
    }

}
