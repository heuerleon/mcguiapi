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
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

/**
 * GUI class that contains items with different click actions.
 * Each item can have another click action, and close actions can be defined.
 * The GUI can also be formatted with a GUIPattern.
 * It can be shown to any player and handles every annoying part about creating GUIs in minecraft.
 */
public class GUI {

    private final Inventory inv;
    private final HashMap<Integer, Consumer<InventoryClickEvent>> clickActions = new HashMap<>();
    private final EnumMap<CloseCause, Consumer<Event>> closeActions = new EnumMap<>(CloseCause.class);
    private final List<Player> viewers = new ArrayList<>();

    // use GUIFactory to create a new GUI
    protected GUI(int rows) throws ForbiddenRowAmountException {
        if (rows < 1 || rows > 6) {
            throw new ForbiddenRowAmountException("Only 1 to 6 rows are allowed, but " + rows + " rows were given.");
        }
        inv = Bukkit.createInventory(null, rows * 9);
    }

    // use GUIFactory to create a new GUI
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
     * Formats the gui with the specified pattern.
     * @param pattern The pattern
     * @return The current GUI instance
     */
    public GUI formatPattern(GUIPattern pattern) {
        int index = pattern.getIndex();
        for (String line : pattern.getLines()) {
            int pos = 0;
            for (char c : line.toCharArray()) {
                if (index > inv.getSize() - 1) {
                    return this;
                }
                if (pos > 8) {
                    break;
                }
                if (pattern.getPatternItems().containsKey(c)) {
                    set(index, pattern.getPatternItems().get(c), event -> event.setCancelled(true));
                    index++;
                    pos++;
                }
            }
            index = index + 8 - pos; // skip missing pattern characters for the line
        }
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
        player.openInventory(inv);
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
    protected EnumMap<CloseCause, Consumer<Event>> getCloseActions() {
        return closeActions;
    }

    // for internal use only
    protected List<Player> getViewers() {
        return viewers;
    }

}
