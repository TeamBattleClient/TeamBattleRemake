package me.client.hooks;

import me.client.Client;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.ResourceLocation;

public class FontRendererHook extends FontRenderer{

	public FontRendererHook(GameSettings par1GameSettings, ResourceLocation par2ResourceLocation, TextureManager par3TextureManager, boolean par4) {
		super(par1GameSettings, par2ResourceLocation, par3TextureManager, par4);
	}
	
	@Override
    public int drawStringWithShadow(String par1Str, int x, int y, int color) {
        Client.instance.tahoma.drawStringWithShadow(par1Str, x - 2, y, color);
        return x + Client.instance.tahoma.getStringWidth(par1Str) + 1;
	}

	@Override
    public int drawString(String par1Str, int x, int y, int color)
    {
        Client.instance.tahoma.drawString(par1Str, x - 2, y, color);
        return x + Client.instance.tahoma.getStringWidth(par1Str) + 1;
    }

}
