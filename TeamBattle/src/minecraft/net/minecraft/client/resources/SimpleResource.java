package net.minecraft.client.resources;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

import net.minecraft.client.resources.data.IMetadataSection;
import net.minecraft.client.resources.data.IMetadataSerializer;
import net.minecraft.util.ResourceLocation;

import org.apache.commons.io.IOUtils;

import com.google.common.collect.Maps;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class SimpleResource implements IResource {
	private final Map mapMetadataSections = Maps.newHashMap();
	private final InputStream mcmetaInputStream;
	private JsonObject mcmetaJson;
	private boolean mcmetaJsonChecked;
	private final InputStream resourceInputStream;
	private final IMetadataSerializer srMetadataSerializer;
	private final ResourceLocation srResourceLocation;

	public SimpleResource(ResourceLocation p_i1300_1_, InputStream p_i1300_2_,
			InputStream p_i1300_3_, IMetadataSerializer p_i1300_4_) {
		srResourceLocation = p_i1300_1_;
		resourceInputStream = p_i1300_2_;
		mcmetaInputStream = p_i1300_3_;
		srMetadataSerializer = p_i1300_4_;
	}

	@Override
	public boolean equals(Object p_equals_1_) {
		if (this == p_equals_1_)
			return true;
		else if (p_equals_1_ instanceof SimpleResource) {
			final SimpleResource var2 = (SimpleResource) p_equals_1_;
			return srResourceLocation != null ? srResourceLocation
					.equals(var2.srResourceLocation)
					: var2.srResourceLocation == null;
		} else
			return false;
	}

	@Override
	public InputStream getInputStream() {
		return resourceInputStream;
	}

	@Override
	public IMetadataSection getMetadata(String p_110526_1_) {
		if (!hasMetadata())
			return null;
		else {
			if (mcmetaJson == null && !mcmetaJsonChecked) {
				mcmetaJsonChecked = true;
				BufferedReader var2 = null;

				try {
					var2 = new BufferedReader(new InputStreamReader(
							mcmetaInputStream));
					mcmetaJson = new JsonParser().parse(var2).getAsJsonObject();
				} finally {
					IOUtils.closeQuietly(var2);
				}
			}

			IMetadataSection var6 = (IMetadataSection) mapMetadataSections
					.get(p_110526_1_);

			if (var6 == null) {
				var6 = srMetadataSerializer.parseMetadataSection(p_110526_1_,
						mcmetaJson);
			}

			return var6;
		}
	}

	@Override
	public int hashCode() {
		return srResourceLocation == null ? 0 : srResourceLocation.hashCode();
	}

	@Override
	public boolean hasMetadata() {
		return mcmetaInputStream != null;
	}
}
