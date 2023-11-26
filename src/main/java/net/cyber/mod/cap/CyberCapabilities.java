package net.cyber.mod.cap;

import net.cyber.mod.helper.ICyberData;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

public class CyberCapabilities  {

    @CapabilityInject(ICyberUser.class)
    public static final Capability<ICyberUser> CYBERWARE_CAPABILITY = null;


}
