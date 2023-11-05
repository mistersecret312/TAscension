package net.cyber.mod.items;

import net.cyber.mod.CyberMod;
import net.cyber.mod.blocks.CyberBlocks;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class CyberItemGroups {
     public static ItemGroup CYBERMOD = new ItemGroup(CyberMod.MOD_ID + ".cybermod") {
         @Override
          public ItemStack createIcon() {
             return new ItemStack(CyberItems.CYBERARML.get());
         }
     };
}
