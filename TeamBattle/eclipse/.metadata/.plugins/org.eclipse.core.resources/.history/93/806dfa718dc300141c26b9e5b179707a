package me.client.hooks;

import me.client.Client;
import me.client.helpers.MinecraftHelper;
import me.client.modules.Module;
import me.client.modules.ModuleManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngame;

public class GuiIngameHook extends GuiIngame implements MinecraftHelper{

	public GuiIngameHook(Minecraft mc) {
		super(mc);
	}
	
	private void renderHackList() {
		int yCount = 10;
		for(Module module : ModuleManager.hacks) {
				Client.instance.tahoma.drawString(module.getListName(), 2, yCount, 0xff004499);
				yCount += 12;
		}
	}
	
	@Override
	public void renderGameOverlay(float var1, boolean var2, int var3, int var4) {
		super.renderGameOverlay(var1, var2, var3, var4);
		
		this.renderHackList();
	}

	@Override
	public boolean isCancelled() {
		return false;
	}

	@Override
	public void setCancelled(boolean cancel) {}

}
