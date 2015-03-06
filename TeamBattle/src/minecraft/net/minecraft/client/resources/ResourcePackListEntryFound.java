package net.minecraft.client.resources;

import net.minecraft.client.gui.GuiScreenResourcePacks;

public class ResourcePackListEntryFound extends ResourcePackListEntry {
	private final ResourcePackRepository.Entry field_148319_c;

	public ResourcePackListEntryFound(GuiScreenResourcePacks p_i45053_1_,
			ResourcePackRepository.Entry p_i45053_2_) {
		super(p_i45053_1_);
		field_148319_c = p_i45053_2_;
	}

	@Override
	protected String func_148311_a() {
		return field_148319_c.getTexturePackDescription();
	}

	@Override
	protected String func_148312_b() {
		return field_148319_c.getResourcePackName();
	}

	@Override
	protected void func_148313_c() {
		field_148319_c.bindTexturePackIcon(field_148317_a.getTextureManager());
	}

	public ResourcePackRepository.Entry func_148318_i() {
		return field_148319_c;
	}
}
