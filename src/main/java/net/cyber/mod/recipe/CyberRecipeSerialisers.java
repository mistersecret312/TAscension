package net.cyber.mod.recipe;

import net.cyber.mod.CyberMod;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class CyberRecipeSerialisers {
	
		public static final DeferredRegister<IRecipeSerializer<?>> RECIPE_SERIALISERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, CyberMod.MOD_ID);
	
	//public static final RegistryObject<IRecipeSerializer<?>> ADVQUANTISCOPE_SERIALISER = RECIPE_SERIALISERS.register("advquantiscope", AdvWeldRecipe.AdvWeldRecipeSerializer::new);
	//public static final ResourceLocation ADVWELD_RECIPE_TYPE_LOC = new ResourceLocation(CyberMod.MOD_ID, "advquantiscope");
	//public static final IRecipeType<AdvWeldRecipe> ADVWELD_RECIPE_TYPE = IRecipeType.register(ADVWELD_RECIPE_TYPE_LOC.toString());

}
