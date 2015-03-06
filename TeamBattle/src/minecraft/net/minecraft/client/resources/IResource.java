package net.minecraft.client.resources;

import java.io.InputStream;

import net.minecraft.client.resources.data.IMetadataSection;

public interface IResource {
	InputStream getInputStream();

	IMetadataSection getMetadata(String p_110526_1_);

	boolean hasMetadata();
}
