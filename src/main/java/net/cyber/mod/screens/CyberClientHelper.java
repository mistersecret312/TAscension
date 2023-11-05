package net.cyber.mod.screens;

import net.minecraft.client.Minecraft;

public class CyberClientHelper {

    public static void openGUI(int guiId) {
        switch (guiId) {
            case CyberConstants.Gui.SURERY:
                Minecraft.getInstance();
        }
    }
}
