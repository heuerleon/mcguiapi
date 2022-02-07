package de.leonheuer.mcguiapi.utils;

import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Builder utility class for easily creating item stacks.
 */
public class ItemBuilder implements Cloneable {

    private final ItemStack itemStack;

    /**
     * Creates a new builder instance for creating an item stack.
     * @param material The material of the item
     * @param amount The amount
     */
    public ItemBuilder(Material material, int amount) {
        itemStack = new ItemStack(material, amount);
    }

    /**
     * Sets the name of the item.
     * @param name The name
     * @return The item builder instance
     */
    public ItemBuilder name(String name) {
        itemStack.editMeta((meta) -> meta.displayName(Component.text(
                ChatColor.translateAlternateColorCodes('&', name)
        )));
        return this;
    }

    /**
     * Sets the description of the item
     * @param description The lines to set as description
     * @return The item builder instance
     */
    public ItemBuilder description(String... description) {
        List<Component> lore = Arrays.stream(description)
                .map(line -> Component.text(
                        ChatColor.translateAlternateColorCodes('&', line)
                )).collect(Collectors.toList());
        itemStack.editMeta((meta) -> meta.lore(lore));
        return this;
    }

    /**
     * Gets the current item stack that has been built by the builder.
     * @return The created item stack
     */
    public ItemStack getResult() {
        return itemStack;
    }

    /**
     * Clones the item builder.
     * @return The cloned instance
     */
    @Override
    public ItemBuilder clone() {
        try {
            return (ItemBuilder) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
