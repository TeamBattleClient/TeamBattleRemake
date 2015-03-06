package net.minecraft.src;

import java.util.List;

import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;

public class WrUpdates {
	private static IWrUpdater wrUpdater = null;

	public static void clearAllUpdates() {
		if (wrUpdater != null) {
			wrUpdater.clearAllUpdates();
		}
	}

	public static void finishCurrentUpdate() {
		if (wrUpdater != null) {
			wrUpdater.finishCurrentUpdate();
		}
	}

	public static IWrUpdater getWrUpdater() {
		return wrUpdater;
	}

	public static boolean hasWrUpdater() {
		return wrUpdater != null;
	}

	public static WorldRenderer makeWorldRenderer(World worldObj,
			List tileEntities, int x, int y, int z, int glRenderListBase) {
		return wrUpdater == null ? new WorldRenderer(worldObj, tileEntities, x,
				y, z, glRenderListBase) : wrUpdater.makeWorldRenderer(worldObj,
				tileEntities, x, y, z, glRenderListBase);
	}

	public static void pauseBackgroundUpdates() {
		if (wrUpdater != null) {
			wrUpdater.pauseBackgroundUpdates();
		}
	}

	public static void postRender() {
		if (wrUpdater != null) {
			wrUpdater.postRender();
		}
	}

	public static void preRender(RenderGlobal rg, EntityLivingBase player) {
		if (wrUpdater != null) {
			wrUpdater.preRender(rg, player);
		}
	}

	public static void resumeBackgroundUpdates() {
		if (wrUpdater != null) {
			wrUpdater.resumeBackgroundUpdates();
		}
	}

	public static void setWrUpdater(IWrUpdater updater) {
		if (wrUpdater != null) {
			wrUpdater.terminate();
		}

		wrUpdater = updater;

		if (wrUpdater != null) {
			try {
				wrUpdater.initialize();
			} catch (final Exception var2) {
				wrUpdater = null;
				var2.printStackTrace();
			}
		}
	}

	public static boolean updateRenderers(RenderGlobal rg,
			EntityLivingBase entityliving, boolean flag) {
		try {
			return wrUpdater.updateRenderers(rg, entityliving, flag);
		} catch (final Exception var4) {
			var4.printStackTrace();
			setWrUpdater((IWrUpdater) null);
			return false;
		}
	}
}
