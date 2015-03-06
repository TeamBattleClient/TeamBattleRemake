package net.minecraft.client.resources;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

public class FileResourcePack extends AbstractResourcePack implements Closeable {
	public static final Splitter entryNameSplitter = Splitter.on('/')
			.omitEmptyStrings().limit(3);
	private ZipFile resourcePackZipFile;

	public FileResourcePack(File p_i1290_1_) {
		super(p_i1290_1_);
	}

	@Override
	public void close() throws IOException {
		if (resourcePackZipFile != null) {
			resourcePackZipFile.close();
			resourcePackZipFile = null;
		}
	}

	@Override
	protected void finalize() throws Throwable {
		close();
		super.finalize();
	}

	@Override
	protected InputStream getInputStreamByName(String p_110591_1_)
			throws IOException {
		final ZipFile var2 = getResourcePackZipFile();
		final ZipEntry var3 = var2.getEntry(p_110591_1_);

		if (var3 == null)
			throw new ResourcePackFileNotFoundException(resourcePackFile,
					p_110591_1_);
		else
			return var2.getInputStream(var3);
	}

	@Override
	public Set getResourceDomains() {
		ZipFile var1;

		try {
			var1 = getResourcePackZipFile();
		} catch (final IOException var8) {
			return Collections.emptySet();
		}

		final Enumeration var2 = var1.entries();
		final HashSet var3 = Sets.newHashSet();

		while (var2.hasMoreElements()) {
			final ZipEntry var4 = (ZipEntry) var2.nextElement();
			final String var5 = var4.getName();

			if (var5.startsWith("assets/")) {
				final ArrayList var6 = Lists.newArrayList(entryNameSplitter
						.split(var5));

				if (var6.size() > 1) {
					final String var7 = (String) var6.get(1);

					if (!var7.equals(var7.toLowerCase())) {
						logNameNotLowercase(var7);
					} else {
						var3.add(var7);
					}
				}
			}
		}

		return var3;
	}

	private ZipFile getResourcePackZipFile() throws IOException {
		if (resourcePackZipFile == null) {
			resourcePackZipFile = new ZipFile(resourcePackFile);
		}

		return resourcePackZipFile;
	}

	@Override
	public boolean hasResourceName(String p_110593_1_) {
		try {
			return getResourcePackZipFile().getEntry(p_110593_1_) != null;
		} catch (final IOException var3) {
			return false;
		}
	}
}
