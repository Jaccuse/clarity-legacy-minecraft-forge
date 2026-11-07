package com.readablef3;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;

import java.util.ArrayList;
import java.util.List;

public class ClarityConfigGui extends GuiConfig {

    public ClarityConfigGui(GuiScreen parentScreen) {
        super(
                parentScreen,
                getConfigElements(),
                Clarity.MODID,
                false,
                false,
                "clarity settings"
        );
    }

    private static List<IConfigElement> getConfigElements() {
        List<IConfigElement> elements = new ArrayList<IConfigElement>();

        elements.add(new ConfigElement(
                ClarityConfig.config.getCategory(ClarityConfig.CATEGORY_GENERAL)
        ));

        elements.add(new ConfigElement(
                ClarityConfig.config.getCategory(ClarityConfig.CATEGORY_LINES)
        ));

        return elements;
    }
}