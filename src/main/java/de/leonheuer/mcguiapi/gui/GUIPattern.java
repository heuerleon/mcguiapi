package de.leonheuer.mcguiapi.gui;

import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Builder class for a GUI pattern. Patterns contain lines of characters, each one standing for a defined item.
 * This can be useful for formatting a GUI with placeholders, for example.
 */
public class GUIPattern {

    private final List<String> lines;
    private final HashMap<Character, ItemStack> patternItems = new HashMap<>();
    private int index = 0;

    // use static method to instantiate
    private GUIPattern(List<String> lines) {
        this.lines = lines;
    }

    /**
     * Creates a new GUIPattern with the specified lines of patterns.
     * @param pattern The lines of patterns - should always be 9 characters long
     * @return The current GUIPattern instance
     */
    public static GUIPattern ofPattern(String... pattern) {
        return new GUIPattern(Arrays.stream(pattern).toList());
    }

    /**
     * Defines what item a character in the pattern should be replaced with.
     * @param identifier The character to replace
     * @param item The item to set in place
     * @return The current GUIPattern instance
     */
    public GUIPattern withMaterial(char identifier, ItemStack item) {
        patternItems.put(identifier, item);
        return this;
    }

    /**
     * The line in the GUI to start the pattern at
     * @param line The line
     * @return The current GUIPattern instance
     */
    public GUIPattern startAtLine(int line) {
        index = (line - 1) * 9;
        return this;
    }

    // for internal use only
    protected List<String> getLines() {
        return lines;
    }

    // for internal use only
    protected HashMap<Character, ItemStack> getPatternItems() {
        return patternItems;
    }

    // for internal use only
    protected int getIndex() {
        return index;
    }
}
