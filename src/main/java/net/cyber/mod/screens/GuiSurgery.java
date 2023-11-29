package net.cyber.mod.screens;

import com.google.common.graph.Network;
import com.google.common.util.concurrent.AtomicDouble;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.cyber.mod.CyberMod;
import net.cyber.mod.cap.CyberCapabilities;
import net.cyber.mod.container.ContainerSurgery;
import net.cyber.mod.network.CyberNetwork;
import net.cyber.mod.network.packets.ApplyButtonMessage;
import net.cyber.mod.tileentity.TileEntitySurgery;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextComponent;


public class GuiSurgery extends ContainerScreen<ContainerSurgery>
{
	private final TileEntitySurgery tile;
	private final PlayerEntity player;
	public static final ResourceLocation TEXTURE = new ResourceLocation(CyberMod.MOD_ID, "textures/gui/surgery_gui.png");

	public GuiSurgery(ContainerSurgery container, PlayerInventory inventory, ITextComponent ex)
	{
		super(container, inventory, ex);

		this.player = inventory.player;
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
		int texWidth = 218;
		int textHeight = 223;
		this.minecraft.getTextureManager().bindTexture(TEXTURE);
		blit(matrixStack, width / 2 - texWidth / 2, height / 2 - textHeight / 2, 0, 0, texWidth, textHeight);

		blit(matrixStack, 3,  7, 238, 7, 3, 118);
		blit(matrixStack, 214, 7, 27, 7, 3,  (int) (118 * getPercentage()));
	}


	@Override
	public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		super.render(matrixStack, mouseX, mouseY, partialTicks);
		this.renderHoveredTooltip(matrixStack, mouseX, mouseY);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(MatrixStack matrixStack, int x, int y) {
		//Remove super class call so we don't show our container title
	}

	public double getPercentage(){
		AtomicDouble percent = new AtomicDouble();
		this.player.getCapability(CyberCapabilities.CYBERWARE_CAPABILITY).ifPresent(cap -> {
			percent.set((cap.getEssence()/cap.getMaxEssence()));
		});
		return percent.get();
	}

	public void addModeButton(int x, int y) {
		this.addButton(new Button(width / 2 - x, height / 2 - y, 17, 9, new StringTextComponent("Apply"), but -> {
			player.getCapability(CyberCapabilities.CYBERWARE_CAPABILITY).ifPresent(cap -> {
				cap.setAllCyberware(this.container.getInventory());
			});
		}));
	}


}