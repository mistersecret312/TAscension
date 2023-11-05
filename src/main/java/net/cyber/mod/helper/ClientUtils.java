package net.cyber.mod.helper;

import net.cyber.mod.CyberMod;
import net.cyber.mod.network.CyberNetwork;
import net.cyber.mod.network.packets.TriggerActiveAbilityPacket;
import net.cyber.mod.screens.CyberConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.client.gui.GuiUtils;

import java.util.HashMap;
import java.util.List;

public class ClientUtils {



    private static final float TEXTURE_SCALE = 1.0F / 256;
    public static void drawTexturedModalRect(int x, int y, int textureX, int textureY, int width, int height)
    {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        bufferBuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferBuilder.pos(x        , y + height, 0.0F).tex((textureX        ) * TEXTURE_SCALE, (textureY + height) * TEXTURE_SCALE).endVertex();
        bufferBuilder.pos(x + width, y + height, 0.0F).tex((textureX + width) * TEXTURE_SCALE, (textureY + height) * TEXTURE_SCALE).endVertex();
        bufferBuilder.pos(x + width, y         , 0.0F).tex((textureX + width) * TEXTURE_SCALE, (textureY         ) * TEXTURE_SCALE).endVertex();
        bufferBuilder.pos(x        , y         , 0.0F).tex((textureX        ) * TEXTURE_SCALE, (textureY         ) * TEXTURE_SCALE).endVertex();
        tessellator.draw();
    }

    private static HashMap<String, ResourceLocation> textures = new HashMap<>();

    public static void bindTexture(String string)
    {
        if (!textures.containsKey(string))
        {
            textures.put(string, new ResourceLocation(string));
            CyberMod.LOGGER.info("Registering new ResourceLocation: " + string);
        }
        Minecraft.getInstance().getTextureManager().bindTexture(textures.get(string));
    }


    public static void useActiveItemClient(Entity entity, ItemStack stack)
    {
        CyberNetwork.sendToServer(new TriggerActiveAbilityPacket(stack.getItem()));
        CyberAPI.useActiveItem(entity, stack);
    }
}
