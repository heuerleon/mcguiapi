package de.leonheuer.mcguiapi.utils;

import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

/**
 * Builder utility class for easily creating item stacks.
 */
@SuppressWarnings({"unused", "UnusedReturnValue"})
public class ItemBuilder {

    private final ItemStack itemStack;

    // use static method to instantiate a pattern
    ItemBuilder(Material material) {
        itemStack = new ItemStack(material);
    }

    ItemBuilder(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    /**
     * Creates a new builder instance for creating an item stack.
     * @param material The material of the item
     * @return The item builder instance
     */
    @Contract("_ -> new")
    public static @NotNull ItemBuilder of(Material material) {
        return new ItemBuilder(material);
    }

    /**
     * Creates a new builder instance for creating an item stack.
     * @param itemStack The item stack to begin with
     * @return The item builder instance
     */
    @Contract(value = "_ -> new", pure = true)
    public static @NotNull ItemBuilder of(ItemStack itemStack) {
        return new ItemBuilder(itemStack);
    }

    /**
     * Sets the amount of the item.
     * @param amount The amount
     * @return The item builder instance
     */
    public ItemBuilder amount(int amount) {
        itemStack.setAmount(amount);
        return this;
    }

    /**
     * Sets the name of the item.
     * @param name The name
     * @return The item builder instance
     */
    public ItemBuilder name(String name) {
        itemStack.editMeta(meta -> meta.displayName(Component.text(
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
                .map(line -> (Component) Component.text(
                        ChatColor.translateAlternateColorCodes('&', line)
                )).toList();
        itemStack.editMeta(meta -> meta.lore(lore));
        return this;
    }

    /**
     * Enchants the item with the given enchantment and level.
     * @param enchantment The enchantment
     * @param level The level
     * @return The item builder instance
     */
    public ItemBuilder enchant(Enchantment enchantment, int level) {
        itemStack.addUnsafeEnchantment(enchantment, level);
        return this;
    }

    /**
     * Adds all given item flags to the item.
     * @param flags The item flags
     * @return The item builder instance
     */
    public ItemBuilder addFlags(ItemFlag... flags) {
        itemStack.addItemFlags(flags);
        return this;
    }

    /**
     * Makes the item glow.
     * @return The item builder instance
     */
    public ItemBuilder glowing() {
        if (itemStack.getType() == Material.FISHING_ROD) {
            enchant(Enchantment.DEPTH_STRIDER, 1);
        } else {
            enchant(Enchantment.LURE, 1);
        }
        addFlags(ItemFlag.HIDE_ENCHANTS);
        return this;
    }

    /**
     * Gets the current item stack that has been built by the builder.
     * @return The created item stack
     */
    public ItemStack asItem() {
        return itemStack;
    }

}
