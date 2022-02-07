package de.leonheuer.mcguiapi.gui;

import de.leonheuer.mcguiapi.enums.CloseCause;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

public class GUIListener implements Listener {

    private final GUI gui;

    public GUIListener(GUI gui) {
        this.gui = gui;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }
        ItemStack item = event.getCurrentItem();
        if (item == null || item.getType() == Material.AIR) {
            return;
        }
        if (event.getClickedInventory() == event.getWhoClicked().getInventory()) {
            return;
        }
        if (gui.getViewers().contains((Player) event.getWhoClicked())) {
            gui.getClickActions().get(event.getSlot()).accept(event);
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (!(event.getPlayer() instanceof Player)) {
            return;
        }
        if (!gui.getViewers().contains((Player) event.getPlayer())) {
            return;
        }
        gui.getViewers().remove((Player) event.getPlayer());
        if (gui.getCloseActions().containsKey(CloseCause.CLOSE)) {
            gui.getCloseActions().get(CloseCause.CLOSE).accept(event);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (!gui.getViewers().contains(event.getPlayer())) {
            return;
        }
        gui.getViewers().remove(event.getPlayer());
        if (gui.getCloseActions().containsKey(CloseCause.QUIT)) {
            gui.getCloseActions().get(CloseCause.QUIT).accept(event);
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (!gui.getViewers().contains(event.getEntity())) {
            return;
        }
        gui.getViewers().remove(event.getEntity());
        if (gui.getCloseActions().containsKey(CloseCause.DEATH)) {
            gui.getCloseActions().get(CloseCause.DEATH).accept(event);
        }
    }

}
