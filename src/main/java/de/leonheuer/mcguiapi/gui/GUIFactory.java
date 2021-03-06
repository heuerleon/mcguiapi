package de.leonheuer.mcguiapi.gui;

import de.leonheuer.mcguiapi.exceptions.ForbiddenRowAmountException;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

/**
 * Class that can create new GUIs and register their listeners to a Minecraft Plugin.
 * It is necessary for creating GUIs because every GUI needs their own listener.
 * Since the GUI API is not a standalone plugin that can register listeners to itself, there must be another plugin
 * given to register the listeners to.
 */
@SuppressWarnings("unused")
public class GUIFactory {

    private final JavaPlugin plugin;

    public GUIFactory(@NotNull JavaPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Creates a GUI with the specified amount of rows.
     * @param rows The amount of rows
     * @return The instantiated GUI
     * @throws ForbiddenRowAmountException if the amount of rows is not between 1 and 6
     */
    @NotNull
    public GUI createGUI(int rows) throws ForbiddenRowAmountException {
        GUI gui = new GUI(rows);
        plugin.getServer().getPluginManager().registerEvents(new GUIListener(gui), plugin);
        return gui;
    }

    /**
     * Creates a GUI with the specified amount of rows and title.
     * @param rows The amount of rows
     * @param title The title of the GUI
     * @return The instantiated GUI
     * @throws ForbiddenRowAmountException if the amount of rows is not between 1 and 6
     */
    @NotNull
    public GUI createGUI(int rows, @NotNull String title) throws ForbiddenRowAmountException {
        GUI gui = new GUI(rows, title);
        plugin.getServer().getPluginManager().registerEvents(new GUIListener(gui), plugin);
        return gui;
    }

}
