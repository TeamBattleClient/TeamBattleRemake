package net.minecraft.client.resources;

import java.awt.image.BufferedImage;
import java.io.Closeable;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreenWorking;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.data.IMetadataSerializer;
import net.minecraft.client.resources.data.PackMetadataSection;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.HttpUtil;
import net.minecraft.util.ResourceLocation;

import org.apache.commons.io.IOUtils;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class ResourcePackRepository {
	public class Entry {
		private ResourceLocation locationTexturePackIcon;
		private PackMetadataSection rePackMetadataSection;
		private IResourcePack reResourcePack;
		private final File resourcePackFile;
		private BufferedImage texturePackIcon;

		private Entry(File p_i1295_2_) {
			resourcePackFile = p_i1295_2_;
		}

		Entry(File p_i1296_2_, Object p_i1296_3_) {
			this(p_i1296_2_);
		}

		public void bindTexturePackIcon(TextureManager p_110518_1_) {
			if (locationTexturePackIcon == null) {
				locationTexturePackIcon = p_110518_1_
						.getDynamicTextureLocation("texturepackicon",
								new DynamicTexture(texturePackIcon));
			}

			p_110518_1_.bindTexture(locationTexturePackIcon);
		}

		public void closeResourcePack() {
			if (reResourcePack instanceof Closeable) {
				IOUtils.closeQuietly((Closeable) reResourcePack);
			}
		}

		@Override
		public boolean equals(Object p_equals_1_) {
			return this == p_equals_1_ ? true
					: p_equals_1_ instanceof ResourcePackRepository.Entry ? toString()
							.equals(p_equals_1_.toString()) : false;
		}

		public IResourcePack getResourcePack() {
			return reResourcePack;
		}

		public String getResourcePackName() {
			return reResourcePack.getPackName();
		}

		public String getTexturePackDescription() {
			return rePackMetadataSection == null ? EnumChatFormatting.RED
					+ "Invalid pack.mcmeta (or missing \'pack\' section)"
					: rePackMetadataSection.func_152805_a().getFormattedText();
		}

		@Override
		public int hashCode() {
			return toString().hashCode();
		}

		@Override
		public String toString() {
			return String.format(
					"%s:%s:%d",
					new Object[] { resourcePackFile.getName(),
							resourcePackFile.isDirectory() ? "folder" : "zip",
							Long.valueOf(resourcePackFile.lastModified()) });
		}

		public void updateResourcePack() throws IOException {
			reResourcePack = resourcePackFile.isDirectory() ? new FolderResourcePack(
					resourcePackFile) : new FileResourcePack(resourcePackFile);
			rePackMetadataSection = (PackMetadataSection) reResourcePack
					.getPackMetadata(rprMetadataSerializer, "pack");

			try {
				texturePackIcon = reResourcePack.getPackImage();
			} catch (final IOException var2) {
				;
			}

			if (texturePackIcon == null) {
				texturePackIcon = rprDefaultResourcePack.getPackImage();
			}

			closeResourcePack();
		}
	}

	protected static final FileFilter resourcePackFilter = new FileFilter() {

		@Override
		public boolean accept(File p_accept_1_) {
			final boolean var2 = p_accept_1_.isFile()
					&& p_accept_1_.getName().endsWith(".zip");
			final boolean var3 = p_accept_1_.isDirectory()
					&& new File(p_accept_1_, "pack.mcmeta").isFile();
			return var2 || var3;
		}
	};
	private final File dirResourcepacks;
	private IResourcePack field_148532_f;
	private boolean field_148533_g;
	private final File field_148534_e;
	private final List repositoryEntries = Lists.newArrayList();
	private List repositoryEntriesAll = Lists.newArrayList();
	public final IResourcePack rprDefaultResourcePack;

	public final IMetadataSerializer rprMetadataSerializer;

	public ResourcePackRepository(File p_i45101_1_, File p_i45101_2_,
			IResourcePack p_i45101_3_, IMetadataSerializer p_i45101_4_,
			GameSettings p_i45101_5_) {
		dirResourcepacks = p_i45101_1_;
		field_148534_e = p_i45101_2_;
		rprDefaultResourcePack = p_i45101_3_;
		rprMetadataSerializer = p_i45101_4_;
		fixDirResourcepacks();
		updateRepositoryEntriesAll();
		final Iterator var6 = p_i45101_5_.resourcePacks.iterator();

		while (var6.hasNext()) {
			final String var7 = (String) var6.next();
			final Iterator var8 = repositoryEntriesAll.iterator();

			while (var8.hasNext()) {
				final ResourcePackRepository.Entry var9 = (ResourcePackRepository.Entry) var8
						.next();

				if (var9.getResourcePackName().equals(var7)) {
					repositoryEntries.add(var9);
					break;
				}
			}
		}
	}

	private void fixDirResourcepacks() {
		if (!dirResourcepacks.isDirectory()) {
			dirResourcepacks.delete();
			dirResourcepacks.mkdirs();
		}
	}

	public void func_148526_a(String p_148526_1_) {
		String var2 = p_148526_1_.substring(p_148526_1_.lastIndexOf("/") + 1);

		if (var2.contains("?")) {
			var2 = var2.substring(0, var2.indexOf("?"));
		}

		if (var2.endsWith(".zip")) {
			final File var3 = new File(field_148534_e, var2.replaceAll("\\W",
					""));
			func_148529_f();
			func_148528_a(p_148526_1_, var3);
		}
	}

	public void func_148527_a(List p_148527_1_) {
		repositoryEntries.clear();
		repositoryEntries.addAll(p_148527_1_);
	}

	private void func_148528_a(String p_148528_1_, File p_148528_2_) {
		final HashMap var3 = Maps.newHashMap();
		final GuiScreenWorking var4 = new GuiScreenWorking();
		var3.put("X-Minecraft-Username", Minecraft.getMinecraft().getSession()
				.getUsername());
		var3.put("X-Minecraft-UUID", Minecraft.getMinecraft().getSession()
				.getPlayerID());
		var3.put("X-Minecraft-Version", "1.7.10");
		field_148533_g = true;
		Minecraft.getMinecraft().displayGuiScreen(var4);
		HttpUtil.func_151223_a(p_148528_2_, p_148528_1_,
				new HttpUtil.DownloadListener() {

					@Override
					public void func_148522_a(File p_148522_1_) {
						if (field_148533_g) {
							field_148533_g = false;
							field_148532_f = new FileResourcePack(p_148522_1_);
							Minecraft.getMinecraft().scheduleResourcesRefresh();
						}
					}
				}, var3, 52428800, var4, Minecraft.getMinecraft().getProxy());
	}

	public void func_148529_f() {
		field_148532_f = null;
		field_148533_g = false;
	}

	public IResourcePack func_148530_e() {
		return field_148532_f;
	}

	public File getDirResourcepacks() {
		return dirResourcepacks;
	}

	public List getRepositoryEntries() {
		return ImmutableList.copyOf(repositoryEntries);
	}

	public List getRepositoryEntriesAll() {
		return ImmutableList.copyOf(repositoryEntriesAll);
	}

	private List getResourcePackFiles() {
		return dirResourcepacks.isDirectory() ? Arrays.asList(dirResourcepacks
				.listFiles(resourcePackFilter)) : Collections.emptyList();
	}

	public void updateRepositoryEntriesAll() {
		final ArrayList var1 = Lists.newArrayList();
		Iterator var2 = getResourcePackFiles().iterator();

		while (var2.hasNext()) {
			final File var3 = (File) var2.next();
			final ResourcePackRepository.Entry var4 = new ResourcePackRepository.Entry(
					var3, null);

			if (!repositoryEntriesAll.contains(var4)) {
				try {
					var4.updateResourcePack();
					var1.add(var4);
				} catch (final Exception var6) {
					var1.remove(var4);
				}
			} else {
				final int var5 = repositoryEntriesAll.indexOf(var4);

				if (var5 > -1 && var5 < repositoryEntriesAll.size()) {
					var1.add(repositoryEntriesAll.get(var5));
				}
			}
		}

		repositoryEntriesAll.removeAll(var1);
		var2 = repositoryEntriesAll.iterator();

		while (var2.hasNext()) {
			final ResourcePackRepository.Entry var7 = (ResourcePackRepository.Entry) var2
					.next();
			var7.closeResourcePack();
		}

		repositoryEntriesAll = var1;
	}
}
