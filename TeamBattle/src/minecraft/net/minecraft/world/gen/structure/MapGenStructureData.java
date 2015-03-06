package net.minecraft.world.gen.structure;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.WorldSavedData;

public class MapGenStructureData extends WorldSavedData {
	public static String func_143042_b(int p_143042_0_, int p_143042_1_) {
		return "[" + p_143042_0_ + "," + p_143042_1_ + "]";
	}

	private NBTTagCompound field_143044_a = new NBTTagCompound();

	public MapGenStructureData(String p_i43001_1_) {
		super(p_i43001_1_);
	}

	public NBTTagCompound func_143041_a() {
		return field_143044_a;
	}

	public void func_143043_a(NBTTagCompound p_143043_1_, int p_143043_2_,
			int p_143043_3_) {
		field_143044_a.setTag(func_143042_b(p_143043_2_, p_143043_3_),
				p_143043_1_);
	}

	/**
	 * reads in data from the NBTTagCompound into this MapDataBase
	 */
	@Override
	public void readFromNBT(NBTTagCompound p_76184_1_) {
		field_143044_a = p_76184_1_.getCompoundTag("Features");
	}

	/**
	 * write data to NBTTagCompound from this MapDataBase, similar to Entities
	 * and TileEntities
	 */
	@Override
	public void writeToNBT(NBTTagCompound p_76187_1_) {
		p_76187_1_.setTag("Features", field_143044_a);
	}
}
