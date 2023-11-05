package net.cyber.mod.events;

import net.cyber.mod.helper.ICyberData;
import net.minecraft.entity.Entity;
import net.minecraftforge.event.entity.EntityEvent;

import javax.annotation.Nonnull;

public class CyberwareUpdateEvent extends EntityEvent {

    private final Entity entityLivingBase;
    private final ICyberData cyberwareUserData;

    public CyberwareUpdateEvent(@Nonnull Entity entityLivingBase, @Nonnull ICyberData cyberwareUserData)
    {
        super(entityLivingBase);
        this.entityLivingBase = entityLivingBase;
        this.cyberwareUserData = cyberwareUserData;
    }

    @Nonnull
    public Entity getEntityLiving()
    {
        return entityLivingBase;
    }

    @Nonnull
    public ICyberData getCyberwareUserData()
    {
        return cyberwareUserData;
    }

}
