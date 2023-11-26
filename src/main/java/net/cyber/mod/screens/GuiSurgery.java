package net.cyber.mod.screens;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.cyber.mod.CyberMod;
import net.cyber.mod.container.ContainerSurgery;
import net.cyber.mod.tileentity.TileEntitySurgery;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;


public class GuiSurgery extends ContainerScreen<ContainerSurgery>
{
	private final TileEntitySurgery tile;
	public static final ResourceLocation TEXTURE = new ResourceLocation(CyberMod.MOD_ID, "textures/gui/surgery_gui.png");

	public GuiSurgery(ContainerSurgery container, PlayerInventory inventory, ITextComponent ex)
	{
		super(container, inventory, ex);

		this.tile = container.getBlockEntity();
		this.ySize = 222;
	}

		
	@Override
	public void init()
	{
		super.init();
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY) {
		this.renderBackground(matrixStack);
		int texWidth = 176;
		int textHeight = 216;
		this.minecraft.getTextureManager().bindTexture(TEXTURE);
		blit(matrixStack, width / 2 - texWidth / 2, height / 2 - textHeight / 2, 0, 0, texWidth, textHeight);
	}

	@Override
	public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		super.render(matrixStack, mouseX, mouseY, partialTicks);
		this.renderHoveredTooltip(matrixStack, mouseX, mouseY);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(MatrixStack matrixStack, int x, int y) {
		//Remove super class call so we don't show our container title
		this.font.drawText(matrixStack, this.playerInventory.getDisplayName(), (float)this.playerInventoryTitleX, (float)this.playerInventoryTitleY+25, 4210752);
	}

}