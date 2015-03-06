package net.minecraft.client.resources;

import java.io.IOException;

import net.minecraft.client.gui.GuiScreenResourcePacks;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.data.PackMetadataSection;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.JsonParseException;

public class ResourcePackListEntryDefault extends ResourcePackListEntry {
	private static final Logger logger = LogManager.getLogger();
	private final IResourcePack field_148320_d;
	private final ResourceLocation field_148321_e;

	public ResourcePackListEntryDefault(GuiScreenResourcePacks p_i45052_1_) {
		super(p_i45052_1_);
		field_148320_d = field_148317_a.getResourcePackRepository().rprDefaultResourcePack;
		DynamicTexture var2;

		try {
			var2 = new DynamicTexture(field_148320_d.getPackImage());
		} catch (final IOException var4) {
			var2 = TextureUtil.missingTexture;
		}

		field_148321_e = field_148317_a.getTextureManager()
				.getDynamicTextureLocation("texturepackicon", var2);
	}

	@Override
	protected boolean func_148307_h() {
		return false;
	}

	@Override
	protected boolean func_148308_f() {
		return false;
	}

	@Override
	protected boolean func_148309_e() {
		return false;
	}

	@Override
	protected boolean func_148310_d() {
		return false;
	}

	@Override
	protected String func_148311_a() {
		try {
			final PackMetadataSection var1 = (PackMetadataSection) field_148320_d
					.getPackMetadata(
							field_148317_a.getResourcePackRepository().rprMetadataSerializer,
							"pack");

			if (var1 != null)
				return var1.func_152805_a().getFormattedText();
		} catch (final JsonParseException var2) {
			logger.error("Couldn\'t load metadata info", var2);
		} catch (final IOException var3) {
			logger.error("Couldn\'t load metadata info", var3);
		}

		return EnumChatFormatting.RED + "Missing " + "pack.mcmeta" + " :(";
	}

	@Override
	protected String func_148312_b() {
		return "Default";
	}

	@Override
	protected void func_148313_c() {
		field_148317_a.getTextureManager().bindTexture(field_148321_e);
	}

	@Override
	protected boolean func_148314_g() {
		return false;
	}
}
