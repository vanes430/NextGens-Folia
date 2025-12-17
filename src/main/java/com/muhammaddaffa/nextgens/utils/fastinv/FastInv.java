package com.muhammaddaffa.nextgens.utils.fastinv;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class FastInv implements InventoryHolder {

    private final Map<Integer, Consumer<InventoryClickEvent>> itemHandlers = new HashMap<>();
    private final Inventory inventory;

    private Consumer<InventoryOpenEvent> openHandler;
    private Consumer<InventoryCloseEvent> closeHandler;
    private Consumer<InventoryClickEvent> clickHandler;

    public FastInv(int size) {
        this(size, InventoryType.CHEST.getDefaultTitle());
    }

    public FastInv(int size, String title) {
        this(size, InventoryType.CHEST, title);
    }

    public FastInv(InventoryType type) {
        this(type, type.getDefaultTitle());
    }

    public FastInv(InventoryType type, String title) {
        this(0, type, title);
    }

    private FastInv(int size, InventoryType type, String title) {
        if (type == InventoryType.CHEST && size > 0) {
            this.inventory = Bukkit.createInventory(this, size, title);
        } else {
            this.inventory = Bukkit.createInventory(this, type, title);
        }
    }

    public void onOpen(InventoryOpenEvent event) {
        if (openHandler != null) {
            openHandler.accept(event);
        }
    }

    public void onClose(InventoryCloseEvent event) {
        if (closeHandler != null) {
            closeHandler.accept(event);
        }
        // Ensure we handle internal cleanup if necessary, but Manager handles removal on close event
    }

    public void onClick(InventoryClickEvent event) {
        if (clickHandler != null) {
            clickHandler.accept(event);
        }

        Consumer<InventoryClickEvent> itemHandler = itemHandlers.get(event.getRawSlot());
        if (itemHandler != null) {
            itemHandler.accept(event);
        }
    }

    public void open(Player player) {
        player.openInventory(inventory);
        FastInvManager.add(this);
    }

    public void setItem(int slot, ItemStack item) {
        setItem(slot, item, null);
    }

    public void setItem(int slot, ItemStack item, Consumer<InventoryClickEvent> handler) {
        inventory.setItem(slot, item);
        if (handler != null) {
            itemHandlers.put(slot, handler);
        } else {
            itemHandlers.remove(slot);
        }
    }

    public void setItems(int[] slots, ItemStack item) {
        setItems(slots, item, null);
    }

    public void setItems(int[] slots, ItemStack item, Consumer<InventoryClickEvent> handler) {
        for (int slot : slots) {
            setItem(slot, item, handler);
        }
    }

    public void setItems(List<Integer> slots, ItemStack item) {
        setItems(slots, item, null);
    }

    public void setItems(List<Integer> slots, ItemStack item, Consumer<InventoryClickEvent> handler) {
        if (slots == null) return;
        for (int slot : slots) {
            setItem(slot, item, handler);
        }
    }

    public void removeItem(int slot) {
        inventory.clear(slot);
        itemHandlers.remove(slot);
    }
    
    public void clearItems() {
        inventory.clear();
        itemHandlers.clear();
    }

    public void addItem(ItemStack item) {
        addItem(item, null);
    }

    public void addItem(ItemStack item, Consumer<InventoryClickEvent> handler) {
        int slot = inventory.firstEmpty();
        if (slot != -1) {
            setItem(slot, item, handler);
        }
    }

    public ItemStack getItem(int slot) {
        return inventory.getItem(slot);
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    public void setOpenHandler(Consumer<InventoryOpenEvent> openHandler) {
        this.openHandler = openHandler;
    }

    public void addOpenHandler(Consumer<InventoryOpenEvent> openHandler) {
        this.openHandler = openHandler;
    }

    public void setCloseHandler(Consumer<InventoryCloseEvent> closeHandler) {
        this.closeHandler = closeHandler;
    }

    public void addCloseHandler(Consumer<InventoryCloseEvent> closeHandler) {
        this.closeHandler = closeHandler;
    }

    public void setClickHandler(Consumer<InventoryClickEvent> clickHandler) {
        this.clickHandler = clickHandler;
    }
}
