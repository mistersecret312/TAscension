package net.cyber.mod.cap;

import net.cyber.mod.helper.ICyberData;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

public class CyberCapabilities  {


    //@CapabilityInject(IQuant.class)
    //public static final Capability<IQuant> QUANT_CAPABILITY = null;

    @CapabilityInject(ICyberData.class)
    public static final Capability<ICyberData> CYBERWARE_CAPABILITY = null;


}
