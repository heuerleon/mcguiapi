package de.leonheuer.mcguiapi.gui;

import de.leonheuer.mcguiapi.enums.CloseCause;
import de.leonheuer.mcguiapi.exceptions.ForbiddenRowAmountException;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

public class GUI implements Cloneable {

    private final Inventory inv;
    private final HashMap<Integer, Consumer<InventoryClickEvent>> clickActions = new HashMap<>();
    private final HashMap<CloseCause, Consumer<Event>> closeActions = new HashMap<>();
    private final List<Player> viewers = new ArrayList<>();

    // Use the GUIFactory to create new GUIs
    protected GUI(int rows) throws ForbiddenRowAmountException {
        if (rows < 1 || rows > 6) {
            throw new ForbiddenRowAmountException("Only 1 to 6 rows are allowed, but " + rows + " rows were given.");
        }
        inv = Bukkit.createInventory(null, rows * 9);
    }

    // Use the GUIFactory to create new GUIs
    protected GUI(int rows, @NotNull String title) throws ForbiddenRowAmountException {
        if (rows < 1 || rows > 6) {
            throw new ForbiddenRowAmountException("Only 1 to 6 rows are allowed, but " + rows + " rows were given.");
        }
        inv = Bukkit.createInventory(null, rows * 9, Component.text(title));
    }

    /**
     * Sets an item at the given slot and registers an action to be executed when the item is clicked.
     * @param row Row of the GUI slot
     * @param column Column of the GUI slot
     * @param itemStack Item to be set
     * @param action Action to be executed when
     * @return The current GUI instance
     */
    @NotNull
    public GUI set(int row, int column, @NotNull ItemStack itemStack, @NotNull Consumer<InventoryClickEvent> action) {
        int index = row * column - 1;
        inv.setItem(index, itemStack);
        clickActions.put(index, action);
        return this;
    }

    /**
     * Sets an item at the given slot and registers an action to be executed when the item is clicked.
     * @param index Index of the GUI slot
     * @param itemStack Item to be set
     * @param action Action to be executed when
     * @return The current GUI instance
     */
    @NotNull
    public GUI set(int index, @NotNull ItemStack itemStack, @NotNull Consumer<InventoryClickEvent> action) {
        inv.setItem(index, itemStack);
        clickActions.put(index, action);
        return this;
    }

    /**
     * Removes the item at the specified slot from the inventory.
     * @param row Row of the GUI slot
     * @param column Column of the GUI slot
     * @return The current GUI instance
     */
    @NotNull
    public GUI remove(int row, int column) {
        int index = row * column - 1;
        inv.setItem(index, null);
        clickActions.remove(index);
        return this;
    }

    /**
     * Sets an action to perform when the specified cause leads to closing the GUI.
     * @param cause Cause for the GUI to close
     * @param action Action to perform
     * @return The current GUI instance
     */
    public GUI setCloseAction(@NotNull CloseCause cause, @NotNull Consumer<Event> action) {
        closeActions.put(cause, action);
        return this;
    }

    /**
     * Removes the specified close action.
     * @param cause Close action to remove
     * @return The current GUI instance
     */
    public GUI removeCloseAction(@NotNull CloseCause cause) {
        closeActions.remove(cause);
        return this;
    }

    /**
     * Resets all contents and actions of the GUI.
     * @return The current GUI instance
     */
    public GUI reset() {
        inv.clear();
        clickActions.clear();
        closeActions.clear();
        return this;
    }

    /**
     * Removes the item at the specified slot from the inventory.
     * @param index Index of the GUI slot
     * @return The current GUI instance
     */
    @NotNull
    public GUI remove(int index) {
        inv.setItem(index, null);
        clickActions.remove(index);
        return this;
    }

    /**
     * Shows the GUI to the specified player.
     * @param player The player to show the GUI to
     */
    public void show(@NotNull Player player) {
        player.openInventory(clone().getInv()); // clone inventory because viewer could modify it
        viewers.add(player);
    }

    /**
     * Gets an unmodifiable list of players currently looking at the GUI.
     * @return The list of viewers
     */
    @NotNull
    public List<Player> getViewersList() {
        return viewers.stream().toList();
    }

    // for internal use only
    protected Inventory getInv() {
        return inv;
    }

    // for internal use only
    protected HashMap<Integer, Consumer<InventoryClickEvent>> getClickActions() {
        return clickActions;
    }

    // for internal use only
    protected HashMap<CloseCause, Consumer<Event>> getCloseActions() {
        return closeActions;
    }

    // for internal use only
    protected List<Player> getViewers() {
        return viewers;
    }

    /**
     * Clones the GUI, including the items and their click actions.
     * @return The cloned GUI instance
     */
    @Override
    public GUI clone() {
        try {
            return (GUI) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
