package net.minecraft.tileentity;

import net.minecraft.nbt.NBTTagCompound;

public class TileEntityComparator extends TileEntity {
	private int field_145997_a;

	public void func_145995_a(int p_145995_1_) {
		field_145997_a = p_145995_1_;
	}

	public int func_145996_a() {
		return field_145997_a;
	}

	@Override
	public void readFromNBT(NBTTagCompound p_145839_1_) {
		super.readFromNBT(p_145839_1_);
		field_145997_a = p_145839_1_.getInteger("OutputSignal");
	}

	@Override
	public void writeToNBT(NBTTagCompound p_145841_1_) {
		super.writeToNBT(p_145841_1_);
		p_145841_1_.setInteger("OutputSignal", field_145997_a);
	}
}
