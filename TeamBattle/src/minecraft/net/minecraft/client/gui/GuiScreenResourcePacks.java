package net.minecraft.client.gui;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.resources.ResourcePackListEntry;
import net.minecraft.client.resources.ResourcePackListEntryDefault;
import net.minecraft.client.resources.ResourcePackListEntryFound;
import net.minecraft.client.resources.ResourcePackRepository;
import net.minecraft.util.Util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.Sys;

import com.google.common.collect.Lists;

public class GuiScreenResourcePacks extends GuiScreen {
	private static final Logger logger = LogManager.getLogger();
	private final GuiScreen field_146965_f;
	private List field_146966_g;
	private GuiResourcePackSelected field_146967_r;
	private List field_146969_h;
	private GuiResourcePackAvailable field_146970_i;

	public GuiScreenResourcePacks(GuiScreen p_i45050_1_) {
		field_146965_f = p_i45050_1_;
	}

	@Override
	protected void actionPerformed(GuiButton p_146284_1_) {
		if (p_146284_1_.enabled) {
			if (p_146284_1_.id == 2) {
				final File var2 = mc.getResourcePackRepository()
						.getDirResourcepacks();
				final String var3 = var2.getAbsolutePath();

				if (Util.getOSType() == Util.EnumOS.OSX) {
					try {
						logger.info(var3);
						Runtime.getRuntime().exec(
								new String[] { "/usr/bin/open", var3 });
						return;
					} catch (final IOException var9) {
						logger.error("Couldn\'t open file", var9);
					}
				} else if (Util.getOSType() == Util.EnumOS.WINDOWS) {
					final String var4 = String.format(
							"cmd.exe /C start \"Open file\" \"%s\"",
							new Object[] { var3 });

					try {
						Runtime.getRuntime().exec(var4);
						return;
					} catch (final IOException var8) {
						logger.error("Couldn\'t open file", var8);
					}
				}

				boolean var12 = false;

				try {
					final Class var5 = Class.forName("java.awt.Desktop");
					final Object var6 = var5.getMethod("getDesktop",
							new Class[0]).invoke((Object) null, new Object[0]);
					var5.getMethod("browse", new Class[] { URI.class }).invoke(
							var6, new Object[] { var2.toURI() });
				} catch (final Throwable var7) {
					logger.error("Couldn\'t open link", var7);
					var12 = true;
				}

				if (var12) {
					logger.info("Opening via system class!");
					Sys.openURL("file://" + var3);
				}
			} else if (p_146284_1_.id == 1) {
				final ArrayList var10 = Lists.newArrayList();
				Iterator var11 = field_146969_h.iterator();

				while (var11.hasNext()) {
					final ResourcePackListEntry var13 = (ResourcePackListEntry) var11
							.next();

					if (var13 instanceof ResourcePackListEntryFound) {
						var10.add(((ResourcePackListEntryFound) var13)
								.func_148318_i());
					}
				}

				Collections.reverse(var10);
				mc.getResourcePackRepository().func_148527_a(var10);
				mc.gameSettings.resourcePacks.clear();
				var11 = var10.iterator();

				while (var11.hasNext()) {
					final ResourcePackRepository.Entry var14 = (ResourcePackRepository.Entry) var11
							.next();
					mc.gameSettings.resourcePacks.add(var14
							.getResourcePackName());
				}

				mc.gameSettings.saveOptions();
				mc.refreshResources();
				mc.displayGuiScreen(field_146965_f);
			}
		}
	}

	/**
	 * Draws the screen and all the components in it.
	 */
	@Override
	public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_) {
		func_146278_c(0);
		field_146970_i.func_148128_a(p_73863_1_, p_73863_2_, p_73863_3_);
		field_146967_r.func_148128_a(p_73863_1_, p_73863_2_, p_73863_3_);
		drawCenteredString(fontRendererObj,
				I18n.format("resourcePack.title", new Object[0]), width / 2,
				16, 16777215);
		drawCenteredString(fontRendererObj,
				I18n.format("resourcePack.folderInfo", new Object[0]),
				width / 2 - 77, height - 26, 8421504);
		super.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);
	}

	public boolean func_146961_a(ResourcePackListEntry p_146961_1_) {
		return field_146969_h.contains(p_146961_1_);
	}

	public List func_146962_b(ResourcePackListEntry p_146962_1_) {
		return func_146961_a(p_146962_1_) ? field_146969_h : field_146966_g;
	}

	public List func_146963_h() {
		return field_146969_h;
	}

	public List func_146964_g() {
		return field_146966_g;
	}

	/**
	 * Adds the buttons (and other controls) to the screen in question.
	 */
	@Override
	public void initGui() {
		buttons.add(new GuiOptionButton(2, width / 2 - 154, height - 48, I18n
				.format("resourcePack.openFolder", new Object[0])));
		buttons.add(new GuiOptionButton(1, width / 2 + 4, height - 48, I18n
				.format("gui.done", new Object[0])));
		field_146966_g = new ArrayList();
		field_146969_h = new ArrayList();
		final ResourcePackRepository var1 = mc.getResourcePackRepository();
		var1.updateRepositoryEntriesAll();
		final ArrayList var2 = Lists.newArrayList(var1
				.getRepositoryEntriesAll());
		var2.removeAll(var1.getRepositoryEntries());
		Iterator var3 = var2.iterator();
		ResourcePackRepository.Entry var4;

		while (var3.hasNext()) {
			var4 = (ResourcePackRepository.Entry) var3.next();
			field_146966_g.add(new ResourcePackListEntryFound(this, var4));
		}

		var3 = Lists.reverse(var1.getRepositoryEntries()).iterator();

		while (var3.hasNext()) {
			var4 = (ResourcePackRepository.Entry) var3.next();
			field_146969_h.add(new ResourcePackListEntryFound(this, var4));
		}

		field_146969_h.add(new ResourcePackListEntryDefault(this));
		field_146970_i = new GuiResourcePackAvailable(mc, 200, height,
				field_146966_g);
		field_146970_i.func_148140_g(width / 2 - 4 - 200);
		field_146970_i.func_148134_d(7, 8);
		field_146967_r = new GuiResourcePackSelected(mc, 200, height,
				field_146969_h);
		field_146967_r.func_148140_g(width / 2 + 4);
		field_146967_r.func_148134_d(7, 8);
	}

	/**
	 * Called when the mouse is clicked.
	 */
	@Override
	protected void mouseClicked(int p_73864_1_, int p_73864_2_, int p_73864_3_) {
		super.mouseClicked(p_73864_1_, p_73864_2_, p_73864_3_);
		field_146970_i.func_148179_a(p_73864_1_, p_73864_2_, p_73864_3_);
		field_146967_r.func_148179_a(p_73864_1_, p_73864_2_, p_73864_3_);
	}

	@Override
	protected void mouseMovedOrUp(int p_146286_1_, int p_146286_2_,
			int p_146286_3_) {
		super.mouseMovedOrUp(p_146286_1_, p_146286_2_, p_146286_3_);
	}
}
