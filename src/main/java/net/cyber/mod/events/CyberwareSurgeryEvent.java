package net.cyber.mod.events;

import net.cyber.mod.config.CyberConfigs;
import net.minecraft.entity.Entity;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.items.ItemStackHandler;

public class CyberwareSurgeryEvent extends EntityEvent {
    public CyberwareSurgeryEvent(Entity entity) {
        super(entity);
    }

    @Cancelable
    public static class Pre extends CyberwareSurgeryEvent
    {
        public ItemStackHandler inventoryActual;
        public ItemStackHandler inventoryTarget;

        public Pre(Entity entityLivingBase, ItemStackHandler inventoryActual, ItemStackHandler inventoryTarget)
        {
            super(entityLivingBase);

            this.inventoryActual = new ItemStackHandler(120);
            this.inventoryActual.deserializeNBT(inventoryActual.serializeNBT());
            this.inventoryTarget = new ItemStackHandler(120);
            this.inventoryTarget.deserializeNBT(inventoryTarget.serializeNBT());


        }

        public ItemStackHandler getActualCyberwares()
        {
            return inventoryActual;
        }

        public ItemStackHandler getTargetCyberwares()
        {
            return inventoryTarget;
        }
    }

    /**
     * Fired when the Surgery Chamber finishes the process of altering an entities installed Cyberware
     */
    public static class Post extends CyberwareSurgeryEvent
    {
        public Post(Entity entityLivingBase)
        {
            super(entityLivingBase);
        }
    }
}
