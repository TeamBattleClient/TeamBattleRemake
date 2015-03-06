package net.minecraft.client.resources;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.minecraft.client.resources.data.IMetadataSerializer;
import net.minecraft.util.ResourceLocation;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public class SimpleReloadableResourceManager implements
		IReloadableResourceManager {
	private static final Joiner joinerResourcePacks = Joiner.on(", ");
	private static final Logger logger = LogManager.getLogger();
	private final Map domainResourceManagers = Maps.newHashMap();
	private final List reloadListeners = Lists.newArrayList();
	private final IMetadataSerializer rmMetadataSerializer;
	private final Set setResourceDomains = Sets.newLinkedHashSet();

	public SimpleReloadableResourceManager(IMetadataSerializer p_i1299_1_) {
		rmMetadataSerializer = p_i1299_1_;
	}

	private void clearResources() {
		domainResourceManagers.clear();
		setResourceDomains.clear();
	}

	@Override
	public List getAllResources(ResourceLocation p_135056_1_)
			throws IOException {
		final IResourceManager var2 = (IResourceManager) domainResourceManagers
				.get(p_135056_1_.getResourceDomain());

		if (var2 != null)
			return var2.getAllResources(p_135056_1_);
		else
			throw new FileNotFoundException(p_135056_1_.toString());
	}

	@Override
	public IResource getResource(ResourceLocation p_110536_1_)
			throws IOException {
		final IResourceManager var2 = (IResourceManager) domainResourceManagers
				.get(p_110536_1_.getResourceDomain());

		if (var2 != null)
			return var2.getResource(p_110536_1_);
		else
			throw new FileNotFoundException(p_110536_1_.toString());
	}

	@Override
	public Set getResourceDomains() {
		return setResourceDomains;
	}

	private void notifyReloadListeners() {
		final Iterator var1 = reloadListeners.iterator();

		while (var1.hasNext()) {
			final IResourceManagerReloadListener var2 = (IResourceManagerReloadListener) var1
					.next();
			var2.onResourceManagerReload(this);
		}
	}

	@Override
	public void registerReloadListener(
			IResourceManagerReloadListener p_110542_1_) {
		reloadListeners.add(p_110542_1_);
		p_110542_1_.onResourceManagerReload(this);
	}

	public void reloadResourcePack(IResourcePack p_110545_1_) {
		FallbackResourceManager var4;

		for (final Iterator var2 = p_110545_1_.getResourceDomains().iterator(); var2
				.hasNext(); var4.addResourcePack(p_110545_1_)) {
			final String var3 = (String) var2.next();
			setResourceDomains.add(var3);
			var4 = (FallbackResourceManager) domainResourceManagers.get(var3);

			if (var4 == null) {
				var4 = new FallbackResourceManager(rmMetadataSerializer);
				domainResourceManagers.put(var3, var4);
			}
		}
	}

	@Override
	public void reloadResources(List p_110541_1_) {
		clearResources();
		logger.info("Reloading ResourceManager: "
				+ joinerResourcePacks.join(Iterables.transform(p_110541_1_,
						new Function() {

							public String apply(IResourcePack p_apply_1_) {
								return p_apply_1_.getPackName();
							}

							@Override
							public Object apply(Object p_apply_1_) {
								return this.apply((IResourcePack) p_apply_1_);
							}
						})));
		final Iterator var2 = p_110541_1_.iterator();

		while (var2.hasNext()) {
			final IResourcePack var3 = (IResourcePack) var2.next();
			reloadResourcePack(var3);
		}

		notifyReloadListeners();
	}
}
