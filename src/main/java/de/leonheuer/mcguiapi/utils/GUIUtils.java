package de.leonheuer.mcguiapi.utils;

public class GUIUtils {

    public static int calculateIndex(int row, int column) {
        return (row - 1) * 9 + column - 1;
    }

}
