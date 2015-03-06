package net.minecraft.client.resources.data;

import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumTypeAdapterFactory;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.IRegistry;
import net.minecraft.util.RegistrySimple;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

public class IMetadataSerializer {
	class Registration {
		final Class field_110500_b;
		final IMetadataSectionSerializer field_110502_a;

		private Registration(IMetadataSectionSerializer p_i1305_2_,
				Class p_i1305_3_) {
			field_110502_a = p_i1305_2_;
			field_110500_b = p_i1305_3_;
		}

		Registration(IMetadataSectionSerializer p_i1306_2_, Class p_i1306_3_,
				Object p_i1306_4_) {
			this(p_i1306_2_, p_i1306_3_);
		}
	}

	/**
	 * Cached Gson instance. Set to null when more sections are registered, and
	 * then re-created from the builder.
	 */
	private Gson gson;

	private final GsonBuilder gsonBuilder = new GsonBuilder();

	private final IRegistry metadataSectionSerializerRegistry = new RegistrySimple();

	public IMetadataSerializer() {
		gsonBuilder.registerTypeHierarchyAdapter(IChatComponent.class,
				new IChatComponent.Serializer());
		gsonBuilder.registerTypeHierarchyAdapter(ChatStyle.class,
				new ChatStyle.Serializer());
		gsonBuilder.registerTypeAdapterFactory(new EnumTypeAdapterFactory());
	}

	/**
	 * Returns a Gson instance with type adapters registered for metadata
	 * sections.
	 */
	private Gson getGson() {
		if (gson == null) {
			gson = gsonBuilder.create();
		}

		return gson;
	}

	public IMetadataSection parseMetadataSection(String p_110503_1_,
			JsonObject p_110503_2_) {
		if (p_110503_1_ == null)
			throw new IllegalArgumentException(
					"Metadata section name cannot be null");
		else if (!p_110503_2_.has(p_110503_1_))
			return null;
		else if (!p_110503_2_.get(p_110503_1_).isJsonObject())
			throw new IllegalArgumentException("Invalid metadata for \'"
					+ p_110503_1_ + "\' - expected object, found "
					+ p_110503_2_.get(p_110503_1_));
		else {
			final IMetadataSerializer.Registration var3 = (IMetadataSerializer.Registration) metadataSectionSerializerRegistry
					.getObject(p_110503_1_);

			if (var3 == null)
				throw new IllegalArgumentException(
						"Don\'t know how to handle metadata section \'"
								+ p_110503_1_ + "\'");
			else
				return (IMetadataSection) getGson().fromJson(
						p_110503_2_.getAsJsonObject(p_110503_1_),
						var3.field_110500_b);
		}
	}

	public void registerMetadataSectionType(
			IMetadataSectionSerializer p_110504_1_, Class p_110504_2_) {
		metadataSectionSerializerRegistry.putObject(p_110504_1_
				.getSectionName(), new IMetadataSerializer.Registration(
				p_110504_1_, p_110504_2_, null));
		gsonBuilder.registerTypeAdapter(p_110504_2_, p_110504_1_);
		gson = null;
	}
}
