package net.cyber.mod.tags;

import net.minecraft.item.Item;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.cyber.mod.CyberMod;

public class CyberItemTags {
    public static final ResourceLocation SCORCHEDLOG = new ResourceLocation(CyberMod.MOD_ID, "scorched");

    public static ITag.INamedTag<Item> makeItem(ResourceLocation resourceLocation) {
        return ItemTags.makeWrapperTag(resourceLocation.toString()); //makeWrapperTag can be static inited and is aware of tag reloads. Do not use createOptional because that gets loaded too early.
    }

}
