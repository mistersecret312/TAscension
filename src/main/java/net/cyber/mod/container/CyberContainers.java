package net.cyber.mod.container;

import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.cyber.mod.CyberMod;

public class CyberContainers {
    public static final DeferredRegister<ContainerType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, CyberMod.MOD_ID);

    public static final RegistryObject<ContainerType<ContainerSurgery>> SURGEON = CONTAINERS.register("surgery", () -> registerContainer(ContainerSurgery::new));


    public static <T extends Container> ContainerType<T> registerContainer(IContainerFactory<T> fact){
        ContainerType<T> type = new ContainerType<T>(fact);
        return type;
    }
}
