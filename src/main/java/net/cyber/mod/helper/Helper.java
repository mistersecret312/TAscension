package net.cyber.mod.helper;

import net.cyber.mod.container.BaseContainer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Slot;

public class Helper {

    public static void addPlayerInvContainer(BaseContainer container, PlayerInventory player, int x, int y) {

        //Player Main
        for(int i = 0; i < player.mainInventory.size() - 9; ++i) {
            container.addSlot(new Slot(player, i + 9, x + 8 + (i % 9) * 18, 86 + y + (i / 9) * 18));
        }

        //hotbar
        for(int i = 0; i < 9; ++i) {
            container.addSlot(new Slot(player, i, 8 + x + (i * 18), y + 144));
        }
    }


}
