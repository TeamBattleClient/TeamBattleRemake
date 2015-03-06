package net.minecraft.client.resources;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.minecraft.client.resources.data.IMetadataSerializer;
import net.minecraft.util.ResourceLocation;

import com.google.common.collect.Lists;

public class FallbackResourceManager implements IResourceManager {
	static ResourceLocation getLocationMcmeta(ResourceLocation p_110537_0_) {
		return new ResourceLocation(p_110537_0_.getResourceDomain(),
				p_110537_0_.getResourcePath() + ".mcmeta");
	}

	private final IMetadataSerializer frmMetadataSerializer;

	protected final List resourcePacks = new ArrayList();

	public FallbackResourceManager(IMetadataSerializer p_i1289_1_) {
		frmMetadataSerializer = p_i1289_1_;
	}

	public void addResourcePack(IResourcePack p_110538_1_) {
		resourcePacks.add(p_110538_1_);
	}

	@Override
	public List getAllResources(ResourceLocation p_135056_1_)
			throws IOException {
		final ArrayList var2 = Lists.newArrayList();
		final ResourceLocation var3 = getLocationMcmeta(p_135056_1_);
		final Iterator var4 = resourcePacks.iterator();

		while (var4.hasNext()) {
			final IResourcePack var5 = (IResourcePack) var4.next();

			if (var5.resourceExists(p_135056_1_)) {
				final InputStream var6 = var5.resourceExists(var3) ? var5
						.getInputStream(var3) : null;
				var2.add(new SimpleResource(p_135056_1_, var5
						.getInputStream(p_135056_1_), var6,
						frmMetadataSerializer));
			}
		}

		if (var2.isEmpty())
			throw new FileNotFoundException(p_135056_1_.toString());
		else
			return var2;
	}

	@Override
	public IResource getResource(ResourceLocation p_110536_1_)
			throws IOException {
		IResourcePack var2 = null;
		final ResourceLocation var3 = getLocationMcmeta(p_110536_1_);

		for (int var4 = resourcePacks.size() - 1; var4 >= 0; --var4) {
			final IResourcePack var5 = (IResourcePack) resourcePacks.get(var4);

			if (var2 == null && var5.resourceExists(var3)) {
				var2 = var5;
			}

			if (var5.resourceExists(p_110536_1_)) {
				InputStream var6 = null;

				if (var2 != null) {
					var6 = var2.getInputStream(var3);
				}

				return new SimpleResource(p_110536_1_,
						var5.getInputStream(p_110536_1_), var6,
						frmMetadataSerializer);
			}
		}

		throw new FileNotFoundException(p_110536_1_.toString());
	}

	@Override
	public Set getResourceDomains() {
		return null;
	}
}
