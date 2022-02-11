package de.leonheuer.mcguiapi.gui;

import de.leonheuer.mcguiapi.enums.CloseCause;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class GUIListener implements Listener {

    private final GUI gui;

    public GUIListener(GUI gui) {
        this.gui = gui;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        HumanEntity player = event.getWhoClicked();
        if (!(player instanceof Player)) {
            return;
        }
        ItemStack item = event.getCurrentItem();
        if (item == null || item.getType() == Material.AIR) {
            return;
        }
        if (event.getClickedInventory() == event.getWhoClicked().getInventory()) {
            return;
        }

        int slot = event.getSlot();
        if (gui.getViewers().contains(player)) {
            if (gui.getUnStealableSlots().contains(slot)) {
                event.setCancelled(true);
            }
            Consumer<InventoryClickEvent> defaultAction = gui.getDefaultClickAction();
            if (defaultAction != null) {
                defaultAction.accept(event);
            }
            Consumer<InventoryClickEvent> action = gui.getClickActions().get(slot);
            if (action != null) {
                action.accept(event);
            }
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        HumanEntity player = event.getPlayer();
        if (!(player instanceof Player)) {
            return;
        }
        if (!gui.getViewers().contains(player)) {
            return;
        }
        gui.getViewers().remove(player);
        BiConsumer<Event, Player> action = gui.getCloseActions().get(CloseCause.CLOSE);
        if (action != null) {
            action.accept(event, (Player) player);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (!gui.getViewers().contains(event.getPlayer())) {
            return;
        }
        gui.getViewers().remove(event.getPlayer());
        if (gui.getCloseActions().containsKey(CloseCause.QUIT)) {
            gui.getCloseActions().get(CloseCause.QUIT).accept(event, event.getPlayer());
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (!gui.getViewers().contains(event.getPlayer())) {
            return;
        }
        gui.getViewers().remove(event.getEntity());
        if (gui.getCloseActions().containsKey(CloseCause.DEATH)) {
            gui.getCloseActions().get(CloseCause.DEATH).accept(event, event.getPlayer());
        }
    }

}
