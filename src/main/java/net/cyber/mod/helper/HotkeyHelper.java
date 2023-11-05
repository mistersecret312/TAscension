package net.cyber.mod.helper;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;

public class HotkeyHelper {
    public static void assignHotkey(ICyberData cyberwareUserData, ItemStack stack, int key)
    {
        removeHotkey(cyberwareUserData, stack);

        cyberwareUserData.addHotkey(key, stack);
        CyberAPI.getCyberwareNBT(stack).putInt("hotkey", key);
    }

    public static void removeHotkey(ICyberData cyberwareUserData, int key)
    {
        ItemStack stack = cyberwareUserData.getHotkey(key);
        removeHotkey(cyberwareUserData, stack);
    }

    public static void removeHotkey(ICyberData cyberwareUserData, ItemStack stack)
    {
        int hotkey = getHotkey(stack);

        if (hotkey != -1)
        {
            cyberwareUserData.removeHotkey(hotkey);
            CyberAPI.getCyberwareNBT(stack).remove("hotkey");
        }
    }

    public static int getHotkey(ItemStack stack)
    {
        if (stack.isEmpty()) return -1;

        CompoundNBT tagCompound = CyberAPI.getCyberwareNBT(stack);
        if (!tagCompound.contains("hotkey"))
        {
            return -1;
        }

        return tagCompound.getInt("hotkey");
    }
}
