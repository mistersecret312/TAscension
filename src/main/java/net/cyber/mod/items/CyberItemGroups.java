package net.cyber.mod.items;

import net.cyber.mod.CyberMod;
import net.cyber.mod.blocks.CyberBlocks;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class CyberItemGroups {
     public static ItemGroup CYBERNETIC = new ItemGroup(CyberMod.MOD_ID + ".cybernetic") {
         @Override
          public ItemStack createIcon() {
             return new ItemStack(CyberBlocks.SURGEON.get());
         }
     };

     public static ItemGroup ORGANIC = new ItemGroup(CyberMod.MOD_ID + ".organic") {

         @Override
         public ItemStack createIcon(){
             return new ItemStack(CyberItems.NORMALBRAIN.get());
         }
     };
}
