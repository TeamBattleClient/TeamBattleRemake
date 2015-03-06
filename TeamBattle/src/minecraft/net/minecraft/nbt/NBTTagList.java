package net.minecraft.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class NBTTagList extends NBTBase {
	/** The array list containing the tags encapsulated in this list. */
	private List tagList = new ArrayList();

	/**
	 * The type byte for the tags in the list - they must all be of the same
	 * type.
	 */
	private byte tagType = 0;

	/**
	 * Adds the provided tag to the end of the list. There is no check to verify
	 * this tag is of the same type as any previous tag.
	 */
	public void appendTag(NBTBase p_74742_1_) {
		if (tagType == 0) {
			tagType = p_74742_1_.getId();
		} else if (tagType != p_74742_1_.getId()) {
			System.err
					.println("WARNING: Adding mismatching tag types to tag list");
			return;
		}

		tagList.add(p_74742_1_);
	}

	/**
	 * Creates a clone of the tag.
	 */
	@Override
	public NBTBase copy() {
		final NBTTagList var1 = new NBTTagList();
		var1.tagType = tagType;
		final Iterator var2 = tagList.iterator();

		while (var2.hasNext()) {
			final NBTBase var3 = (NBTBase) var2.next();
			final NBTBase var4 = var3.copy();
			var1.tagList.add(var4);
		}

		return var1;
	}

	@Override
	public boolean equals(Object p_equals_1_) {
		if (super.equals(p_equals_1_)) {
			final NBTTagList var2 = (NBTTagList) p_equals_1_;

			if (tagType == var2.tagType)
				return tagList.equals(var2.tagList);
		}

		return false;
	}

	public int func_150303_d() {
		return tagType;
	}

	public void func_150304_a(int p_150304_1_, NBTBase p_150304_2_) {
		if (p_150304_1_ >= 0 && p_150304_1_ < tagList.size()) {
			if (tagType == 0) {
				tagType = p_150304_2_.getId();
			} else if (tagType != p_150304_2_.getId()) {
				System.err
						.println("WARNING: Adding mismatching tag types to tag list");
				return;
			}

			tagList.set(p_150304_1_, p_150304_2_);
		} else {
			System.err
					.println("WARNING: index out of bounds to set tag in tag list");
		}
	}

	public int[] func_150306_c(int p_150306_1_) {
		if (p_150306_1_ >= 0 && p_150306_1_ < tagList.size()) {
			final NBTBase var2 = (NBTBase) tagList.get(p_150306_1_);
			return var2.getId() == 11 ? ((NBTTagIntArray) var2).func_150302_c()
					: new int[0];
		} else
			return new int[0];
	}

	public float func_150308_e(int p_150308_1_) {
		if (p_150308_1_ >= 0 && p_150308_1_ < tagList.size()) {
			final NBTBase var2 = (NBTBase) tagList.get(p_150308_1_);
			return var2.getId() == 5 ? ((NBTTagFloat) var2).func_150288_h()
					: 0.0F;
		} else
			return 0.0F;
	}

	public double func_150309_d(int p_150309_1_) {
		if (p_150309_1_ >= 0 && p_150309_1_ < tagList.size()) {
			final NBTBase var2 = (NBTBase) tagList.get(p_150309_1_);
			return var2.getId() == 6 ? ((NBTTagDouble) var2).func_150286_g()
					: 0.0D;
		} else
			return 0.0D;
	}

	@Override
	void func_152446_a(DataInput p_152446_1_, int p_152446_2_,
			NBTSizeTracker p_152446_3_) throws IOException {
		if (p_152446_2_ > 512)
			throw new RuntimeException(
					"Tried to read NBT tag with too high complexity, depth > 512");
		else {
			p_152446_3_.func_152450_a(8L);
			tagType = p_152446_1_.readByte();
			final int var4 = p_152446_1_.readInt();
			tagList = new ArrayList();

			for (int var5 = 0; var5 < var4; ++var5) {
				final NBTBase var6 = NBTBase.func_150284_a(tagType);
				var6.func_152446_a(p_152446_1_, p_152446_2_ + 1, p_152446_3_);
				tagList.add(var6);
			}
		}
	}

	/**
	 * Retrieves the NBTTagCompound at the specified index in the list
	 */
	public NBTTagCompound getCompoundTagAt(int p_150305_1_) {
		if (p_150305_1_ >= 0 && p_150305_1_ < tagList.size()) {
			final NBTBase var2 = (NBTBase) tagList.get(p_150305_1_);
			return var2.getId() == 10 ? (NBTTagCompound) var2
					: new NBTTagCompound();
		} else
			return new NBTTagCompound();
	}

	/**
	 * Gets the type byte for the tag.
	 */
	@Override
	public byte getId() {
		return (byte) 9;
	}

	/**
	 * Retrieves the tag String value at the specified index in the list
	 */
	public String getStringTagAt(int p_150307_1_) {
		if (p_150307_1_ >= 0 && p_150307_1_ < tagList.size()) {
			final NBTBase var2 = (NBTBase) tagList.get(p_150307_1_);
			return var2.getId() == 8 ? var2.func_150285_a_() : var2.toString();
		} else
			return "";
	}

	@Override
	public int hashCode() {
		return super.hashCode() ^ tagList.hashCode();
	}

	/**
	 * Removes a tag at the given index.
	 */
	public NBTBase removeTag(int p_74744_1_) {
		return (NBTBase) tagList.remove(p_74744_1_);
	}

	/**
	 * Returns the number of tags in the list.
	 */
	public int tagCount() {
		return tagList.size();
	}

	@Override
	public String toString() {
		String var1 = "[";
		int var2 = 0;

		for (final Iterator var3 = tagList.iterator(); var3.hasNext(); ++var2) {
			final NBTBase var4 = (NBTBase) var3.next();
			var1 = var1 + "" + var2 + ':' + var4 + ',';
		}

		return var1 + "]";
	}

	/**
	 * Write the actual data contents of the tag, implemented in NBT extension
	 * classes
	 */
	@Override
	void write(DataOutput p_74734_1_) throws IOException {
		if (!tagList.isEmpty()) {
			tagType = ((NBTBase) tagList.get(0)).getId();
		} else {
			tagType = 0;
		}

		p_74734_1_.writeByte(tagType);
		p_74734_1_.writeInt(tagList.size());

		for (int var2 = 0; var2 < tagList.size(); ++var2) {
			((NBTBase) tagList.get(var2)).write(p_74734_1_);
		}
	}
}
