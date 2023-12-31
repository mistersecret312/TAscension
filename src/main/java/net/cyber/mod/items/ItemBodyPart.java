package net.cyber.mod.items;

import net.cyber.mod.helper.CyberPartEnum;
import net.cyber.mod.helper.CyberPartType;
import net.cyber.mod.helper.ICyberPart;

public class ItemBodyPart extends ItemCyberware implements ICyberPart {

    public ItemBodyPart(Properties properties, CyberPartEnum part) {
        super(properties);
        this.cat = part;
        this.type = CyberPartType.MAIN;
    }
}
