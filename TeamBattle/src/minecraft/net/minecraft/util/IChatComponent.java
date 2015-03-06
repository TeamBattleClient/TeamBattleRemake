package net.minecraft.util;

import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public interface IChatComponent extends Iterable {
	public static class Serializer implements JsonDeserializer, JsonSerializer {
		private static final Gson field_150700_a;

		static {
			final GsonBuilder var0 = new GsonBuilder();
			var0.registerTypeHierarchyAdapter(IChatComponent.class,
					new IChatComponent.Serializer());
			var0.registerTypeHierarchyAdapter(ChatStyle.class,
					new ChatStyle.Serializer());
			var0.registerTypeAdapterFactory(new EnumTypeAdapterFactory());
			field_150700_a = var0.create();
		}

		public static String func_150696_a(IChatComponent p_150696_0_) {
			return field_150700_a.toJson(p_150696_0_);
		}

		public static IChatComponent func_150699_a(String p_150699_0_) {
			return field_150700_a.fromJson(p_150699_0_, IChatComponent.class);
		}

		@Override
		public IChatComponent deserialize(JsonElement p_deserialize_1_,
				Type p_deserialize_2_,
				JsonDeserializationContext p_deserialize_3_) {
			if (p_deserialize_1_.isJsonPrimitive())
				return new ChatComponentText(p_deserialize_1_.getAsString());
			else if (!p_deserialize_1_.isJsonObject()) {
				if (p_deserialize_1_.isJsonArray()) {
					final JsonArray var11 = p_deserialize_1_.getAsJsonArray();
					IChatComponent var12 = null;
					final Iterator var14 = var11.iterator();

					while (var14.hasNext()) {
						final JsonElement var16 = (JsonElement) var14.next();
						final IChatComponent var17 = deserialize(var16,
								var16.getClass(), p_deserialize_3_);

						if (var12 == null) {
							var12 = var17;
						} else {
							var12.appendSibling(var17);
						}
					}

					return var12;
				} else
					throw new JsonParseException("Don\'t know how to turn "
							+ p_deserialize_1_.toString() + " into a Component");
			} else {
				final JsonObject var4 = p_deserialize_1_.getAsJsonObject();
				Object var5;

				if (var4.has("text")) {
					var5 = new ChatComponentText(var4.get("text").getAsString());
				} else {
					if (!var4.has("translate"))
						throw new JsonParseException("Don\'t know how to turn "
								+ p_deserialize_1_.toString()
								+ " into a Component");

					final String var6 = var4.get("translate").getAsString();

					if (var4.has("with")) {
						final JsonArray var7 = var4.getAsJsonArray("with");
						final Object[] var8 = new Object[var7.size()];

						for (int var9 = 0; var9 < var8.length; ++var9) {
							var8[var9] = deserialize(var7.get(var9),
									p_deserialize_2_, p_deserialize_3_);

							if (var8[var9] instanceof ChatComponentText) {
								final ChatComponentText var10 = (ChatComponentText) var8[var9];

								if (var10.getChatStyle().isEmpty()
										&& var10.getSiblings().isEmpty()) {
									var8[var9] = var10
											.getChatComponentText_TextValue();
								}
							}
						}

						var5 = new ChatComponentTranslation(var6, var8);
					} else {
						var5 = new ChatComponentTranslation(var6, new Object[0]);
					}
				}

				if (var4.has("extra")) {
					final JsonArray var13 = var4.getAsJsonArray("extra");

					if (var13.size() <= 0)
						throw new JsonParseException(
								"Unexpected empty array of components");

					for (int var15 = 0; var15 < var13.size(); ++var15) {
						((IChatComponent) var5).appendSibling(deserialize(
								var13.get(var15), p_deserialize_2_,
								p_deserialize_3_));
					}
				}

				((IChatComponent) var5)
						.setChatStyle((ChatStyle) p_deserialize_3_.deserialize(
								p_deserialize_1_, ChatStyle.class));
				return (IChatComponent) var5;
			}
		}

		private void func_150695_a(ChatStyle p_150695_1_,
				JsonObject p_150695_2_, JsonSerializationContext p_150695_3_) {
			final JsonElement var4 = p_150695_3_.serialize(p_150695_1_);

			if (var4.isJsonObject()) {
				final JsonObject var5 = (JsonObject) var4;
				final Iterator var6 = var5.entrySet().iterator();

				while (var6.hasNext()) {
					final Entry var7 = (Entry) var6.next();
					p_150695_2_.add((String) var7.getKey(),
							(JsonElement) var7.getValue());
				}
			}
		}

		public JsonElement serialize(IChatComponent p_serialize_1_,
				Type p_serialize_2_, JsonSerializationContext p_serialize_3_) {
			if (p_serialize_1_ instanceof ChatComponentText
					&& p_serialize_1_.getChatStyle().isEmpty()
					&& p_serialize_1_.getSiblings().isEmpty())
				return new JsonPrimitive(
						((ChatComponentText) p_serialize_1_)
								.getChatComponentText_TextValue());
			else {
				final JsonObject var4 = new JsonObject();

				if (!p_serialize_1_.getChatStyle().isEmpty()) {
					func_150695_a(p_serialize_1_.getChatStyle(), var4,
							p_serialize_3_);
				}

				if (!p_serialize_1_.getSiblings().isEmpty()) {
					final JsonArray var5 = new JsonArray();
					final Iterator var6 = p_serialize_1_.getSiblings()
							.iterator();

					while (var6.hasNext()) {
						final IChatComponent var7 = (IChatComponent) var6
								.next();
						var5.add(this.serialize(var7, var7.getClass(),
								p_serialize_3_));
					}

					var4.add("extra", var5);
				}

				if (p_serialize_1_ instanceof ChatComponentText) {
					var4.addProperty("text",
							((ChatComponentText) p_serialize_1_)
									.getChatComponentText_TextValue());
				} else {
					if (!(p_serialize_1_ instanceof ChatComponentTranslation))
						throw new IllegalArgumentException(
								"Don\'t know how to serialize "
										+ p_serialize_1_ + " as a Component");

					final ChatComponentTranslation var11 = (ChatComponentTranslation) p_serialize_1_;
					var4.addProperty("translate", var11.getKey());

					if (var11.getFormatArgs() != null
							&& var11.getFormatArgs().length > 0) {
						final JsonArray var12 = new JsonArray();
						final Object[] var13 = var11.getFormatArgs();
						final int var8 = var13.length;

						for (int var9 = 0; var9 < var8; ++var9) {
							final Object var10 = var13[var9];

							if (var10 instanceof IChatComponent) {
								var12.add(this.serialize(
										(IChatComponent) var10,
										var10.getClass(), p_serialize_3_));
							} else {
								var12.add(new JsonPrimitive(String
										.valueOf(var10)));
							}
						}

						var4.add("with", var12);
					}
				}

				return var4;
			}
		}

		@Override
		public JsonElement serialize(Object p_serialize_1_,
				Type p_serialize_2_, JsonSerializationContext p_serialize_3_) {
			return this.serialize((IChatComponent) p_serialize_1_,
					p_serialize_2_, p_serialize_3_);
		}
	}

	/**
	 * Appends the given component to the end of this one.
	 */
	IChatComponent appendSibling(IChatComponent p_150257_1_);

	/**
	 * Appends the given text to the end of this component.
	 */
	IChatComponent appendText(String p_150258_1_);

	/**
	 * Creates a copy of this component. Almost a deep copy, except the style is
	 * shallow-copied.
	 */
	IChatComponent createCopy();

	ChatStyle getChatStyle();

	/**
	 * Gets the text of this component, with formatting codes added for
	 * rendering.
	 */
	String getFormattedText();

	/**
	 * Gets the sibling components of this one.
	 */
	List getSiblings();

	/**
	 * Gets the text of this component, without any special formatting codes
	 * added. TODO: why is this two different methods?
	 */
	String getUnformattedText();

	/**
	 * Gets the text of this component, without any special formatting codes
	 * added, for chat. TODO: why is this two different methods?
	 */
	String getUnformattedTextForChat();

	IChatComponent setChatStyle(ChatStyle p_150255_1_);
}
