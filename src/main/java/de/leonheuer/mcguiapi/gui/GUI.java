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
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * GUI class that contains items with different click actions.
 * Each item can have another click action, and close actions can be defined.
 * The GUI can also be formatted with a GUIPattern.
 * It can be shown to any player and handles every annoying part about creating GUIs in minecraft.
 */
@SuppressWarnings("unused")
public class GUI {

    private final Inventory inv;
    private final HashMap<Integer, Consumer<InventoryClickEvent>> clickActions = new HashMap<>();
    private final EnumMap<CloseCause, BiConsumer<Event, Player>> closeActions = new EnumMap<>(CloseCause.class);
    private final List<Player> viewers = new ArrayList<>();
    private final List<Integer> unStealableSlots = new ArrayList<>();
    private Consumer<InventoryClickEvent> defaultClickAction = null;
    private Consumer<Event> defaultCloseAction = null;

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
     *  Also sets whether the item can be stolen, i.e. taken out of the GUI.
     * @param row Row of the GUI slot
     * @param column Column of the GUI slot
     * @param itemStack Item to be set
     * @param stealable Whether the item should be stealable
     * @param action Action to be executed when
     * @return The current GUI instance
     */
    @NotNull
    public GUI setItem(int row, int column, @NotNull ItemStack itemStack, boolean stealable,
                       @NotNull Consumer<InventoryClickEvent> action
    ) {
        return setItem(row * column - 1, itemStack, stealable, action);
    }

    /**
     * Sets an item at the given slot and registers an action to be executed when the item is clicked.
     *  Also sets whether the item can be stolen, i.e. taken out of the GUI.
     * @param index Index of the GUI slot
     * @param itemStack Item to be set
     * @param stealable Whether the item should be stealable
     * @param action Action to be executed when
     * @return The current GUI instance
     */
    @NotNull
    public GUI setItem(int index, @NotNull ItemStack itemStack, boolean stealable,
                       @NotNull Consumer<InventoryClickEvent> action
    ) {
        inv.setItem(index, itemStack);
        clickActions.put(index, action);
        return this;
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
    public GUI setItem(int row, int column, @NotNull ItemStack itemStack, @NotNull Consumer<InventoryClickEvent> action) {
        return setItem(row * column - 1, itemStack, false, action);
    }

    /**
     * Sets an item at the given slot and registers an action to be executed when the item is clicked.
     * @param index Index of the GUI slot
     * @param itemStack Item to be set
     * @param action Action to be executed when
     * @return The current GUI instance
     */
    @NotNull
    public GUI setItem(int index, @NotNull ItemStack itemStack, @NotNull Consumer<InventoryClickEvent> action) {
        return setItem(index, itemStack, false, action);
    }

    /**
     * Sets an item at the given slot. Also sets whether the item can be stolen, i.e. taken out of the GUI.
     * @param row Row of the GUI slot
     * @param column Column of the GUI slot
     * @param itemStack Item to be set
     * @param stealable Whether the item should be stealable
     * @return The current GUI instance
     */
    @NotNull
    public GUI setItem(int row, int column, @NotNull ItemStack itemStack, boolean stealable) {
        return setItem(row * column - 1, itemStack, stealable);
    }

    /**
     * Sets an item at the given slot. Also sets whether the item can be stolen, i.e. taken out of the GUI.
     * @param index Index of the GUI slot
     * @param itemStack Item to be set
     * @param stealable Whether the item should be stealable
     * @return The current GUI instance
     */
    @NotNull
    public GUI setItem(int index, @NotNull ItemStack itemStack, boolean stealable) {
        inv.setItem(index, itemStack);
        if (!stealable && !unStealableSlots.contains(index)) {
            unStealableSlots.add(index);
        }
        return this;
    }

    /**
     * Sets an item at the given slot.
     * @param row Row of the GUI slot
     * @param column Column of the GUI slot
     * @param itemStack Item to be set
     * @return The current GUI instance
     */
    @NotNull
    public GUI setItem(int row, int column, @NotNull ItemStack itemStack) {
        return setItem(row * column - 1, itemStack);
    }

    /**
     * Sets an item at the given slot.
     * @param index Index of the GUI slot
     * @param itemStack Item to be set
     * @return The current GUI instance
     */
    @NotNull
    public GUI setItem(int index, @NotNull ItemStack itemStack) {
        return setItem(index, itemStack, false);
    }

    /**
     * Removes the item at the specified slot from the inventory. This will also remove the click action of the slot.
     * @param row Row of the GUI slot
     * @param column Column of the GUI slot
     * @return The current GUI instance
     */
    @NotNull
    public GUI removeItem(int row, int column) {
        return removeItem(row * column - 1);
    }

    /**
     * Removes the item at the specified slot from the inventory. This will also remove the click action of the slot.
     * @param index Index of the GUI slot
     * @return The current GUI instance
     */
    @NotNull
    public GUI removeItem(int index) {
        inv.setItem(index, null);
        clickActions.remove(index);
        unStealableSlots.remove(index);
        return this;
    }

    /**
     * Sets whether the slot of the GUI should allow items to be stolen from.
     * @param row Row of the GUI slot
     * @param column Column of the GUI slot
     * @param stealable Whether the item should be stealable
     * @return The current GUI instance
     */
    public GUI setStealable(int row, int column, boolean stealable) {
        return setStealable(row * column - 1, stealable);
    }

    /**
     * Sets whether the slot of the GUI should allow items to be stolen from.
     * @param index Index of the GUI slot
     * @param stealable Whether the item should be stealable
     * @return The current GUI instance
     */
    public GUI setStealable(int index, boolean stealable) {
        if (stealable) {
            unStealableSlots.remove(index);
            return this;
        }
        if (!unStealableSlots.contains(index)) {
            unStealableSlots.add(index);
        }
        return this;
    }

    /**
     * Sets an action to perform when clicking the specified slot.
     * @param row Row of the GUI slot
     * @param column Column of the GUI slot
     * @param action Action to perform
     * @return The current GUI instance
     */
    public GUI setClickAction(int row, int column, @NotNull Consumer<InventoryClickEvent> action) {
        return setClickAction(row * column - 1, action);
    }

    /**
     * Sets an action to perform when clicking the specified slot.
     * @param index Index of the GUI slot
     * @param action Action to perform
     * @return The current GUI instance
     */
    public GUI setClickAction(int index, @NotNull Consumer<InventoryClickEvent> action) {
        clickActions.put(index, action);
        return this;
    }

    /**
     * Sets an action that will always be performed when something in the GUI is clicked.
     * @param defaultClickAction Action to perform
     * @return The current GUI instance
     */
    public GUI setDefaultClickAction(@Nullable Consumer<InventoryClickEvent> defaultClickAction) {
        this.defaultClickAction = defaultClickAction;
        return this;
    }

    /**
     * Removes the click action of the specified slot.
     * @param row Row of the GUI slot
     * @param column Column of the GUI slot
     * @return The current GUI instance
     */
    public GUI removeClickAction(int row, int column) {
        return removeClickAction(row * column - 1);
    }

    /**
     * Removes the click action of the specified slot.
     * @param index Index of the GUI slot
     * @return The current GUI instance
     */
    public GUI removeClickAction(int index) {
        clickActions.remove(index);
        return this;
    }

    /**
     * Sets an action to perform when the specified cause leads to closing the GUI.
     * @param cause Cause for the GUI to close
     * @param action Action to perform
     * @return The current GUI instance
     */
    public GUI setCloseAction(@NotNull CloseCause cause, @NotNull BiConsumer<Event, Player> action) {
        closeActions.put(cause, action);
        return this;
    }

    /**
     * Sets an action that will always be performed when the GUI is closed.
     * @param defaultCloseAction Action to perform
     * @return The current GUI instance
     */
    public GUI setDefaultCloseAction(@Nullable Consumer<Event> defaultCloseAction) {
        this.defaultCloseAction = defaultCloseAction;
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
                    setItem(index, pattern.getPatternItems().get(c), false);
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
        unStealableSlots.clear();
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
    protected EnumMap<CloseCause, BiConsumer<Event, Player>> getCloseActions() {
        return closeActions;
    }

    // for internal use only
    protected List<Player> getViewers() {
        return viewers;
    }

    // for internal use only
    protected List<Integer> getUnStealableSlots() {
        return unStealableSlots;
    }

    // for internal use only
    @Nullable
    protected Consumer<InventoryClickEvent> getDefaultClickAction() {
        return defaultClickAction;
    }

    // for internal use only
    @Nullable
    protected Consumer<Event> getDefaultCloseAction() {
        return defaultCloseAction;
    }
}
