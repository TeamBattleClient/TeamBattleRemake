package net.minecraft.tileentity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Facing;

public class TileEntityPiston extends TileEntity {
	private Block field_145869_a;
	private float field_145870_n;
	private final List field_145871_o = new ArrayList();
	private boolean field_145872_l;
	private float field_145873_m;
	private int field_145874_j;
	private boolean field_145875_k;
	private int field_145876_i;

	public TileEntityPiston() {
	}

	public TileEntityPiston(Block p_i45444_1_, int p_i45444_2_,
			int p_i45444_3_, boolean p_i45444_4_, boolean p_i45444_5_) {
		field_145869_a = p_i45444_1_;
		field_145876_i = p_i45444_2_;
		field_145874_j = p_i45444_3_;
		field_145875_k = p_i45444_4_;
		field_145872_l = p_i45444_5_;
	}

	public float func_145859_d(float p_145859_1_) {
		return field_145875_k ? (func_145860_a(p_145859_1_) - 1.0F)
				* Facing.offsetsZForSide[field_145874_j]
				: (1.0F - func_145860_a(p_145859_1_))
						* Facing.offsetsZForSide[field_145874_j];
	}

	public float func_145860_a(float p_145860_1_) {
		if (p_145860_1_ > 1.0F) {
			p_145860_1_ = 1.0F;
		}

		return field_145870_n + (field_145873_m - field_145870_n) * p_145860_1_;
	}

	public Block func_145861_a() {
		return field_145869_a;
	}

	public float func_145862_c(float p_145862_1_) {
		return field_145875_k ? (func_145860_a(p_145862_1_) - 1.0F)
				* Facing.offsetsYForSide[field_145874_j]
				: (1.0F - func_145860_a(p_145862_1_))
						* Facing.offsetsYForSide[field_145874_j];
	}

	private void func_145863_a(float p_145863_1_, float p_145863_2_) {
		if (field_145875_k) {
			p_145863_1_ = 1.0F - p_145863_1_;
		} else {
			--p_145863_1_;
		}

		final AxisAlignedBB var3 = Blocks.piston_extension.func_149964_a(
				worldObj, field_145851_c, field_145848_d, field_145849_e,
				field_145869_a, p_145863_1_, field_145874_j);

		if (var3 != null) {
			final List var4 = worldObj.getEntitiesWithinAABBExcludingEntity(
					(Entity) null, var3);

			if (!var4.isEmpty()) {
				field_145871_o.addAll(var4);
				final Iterator var5 = field_145871_o.iterator();

				while (var5.hasNext()) {
					final Entity var6 = (Entity) var5.next();
					var6.moveEntity(p_145863_2_
							* Facing.offsetsXForSide[field_145874_j],
							p_145863_2_
									* Facing.offsetsYForSide[field_145874_j],
							p_145863_2_
									* Facing.offsetsZForSide[field_145874_j]);
				}

				field_145871_o.clear();
			}
		}
	}

	public int func_145864_c() {
		return field_145874_j;
	}

	public float func_145865_b(float p_145865_1_) {
		return field_145875_k ? (func_145860_a(p_145865_1_) - 1.0F)
				* Facing.offsetsXForSide[field_145874_j]
				: (1.0F - func_145860_a(p_145865_1_))
						* Facing.offsetsXForSide[field_145874_j];
	}

	public void func_145866_f() {
		if (field_145870_n < 1.0F && worldObj != null) {
			field_145870_n = field_145873_m = 1.0F;
			worldObj.removeTileEntity(field_145851_c, field_145848_d,
					field_145849_e);
			invalidate();

			if (worldObj.getBlock(field_145851_c, field_145848_d,
					field_145849_e) == Blocks.piston_extension) {
				worldObj.setBlock(field_145851_c, field_145848_d,
						field_145849_e, field_145869_a, field_145876_i, 3);
				worldObj.func_147460_e(field_145851_c, field_145848_d,
						field_145849_e, field_145869_a);
			}
		}
	}

	public boolean func_145867_d() {
		return field_145872_l;
	}

	public boolean func_145868_b() {
		return field_145875_k;
	}

	@Override
	public int getBlockMetadata() {
		return field_145876_i;
	}

	@Override
	public void readFromNBT(NBTTagCompound p_145839_1_) {
		super.readFromNBT(p_145839_1_);
		field_145869_a = Block.getBlockById(p_145839_1_.getInteger("blockId"));
		field_145876_i = p_145839_1_.getInteger("blockData");
		field_145874_j = p_145839_1_.getInteger("facing");
		field_145870_n = field_145873_m = p_145839_1_.getFloat("progress");
		field_145875_k = p_145839_1_.getBoolean("extending");
	}

	@Override
	public void updateEntity() {
		field_145870_n = field_145873_m;

		if (field_145870_n >= 1.0F) {
			func_145863_a(1.0F, 0.25F);
			worldObj.removeTileEntity(field_145851_c, field_145848_d,
					field_145849_e);
			invalidate();

			if (worldObj.getBlock(field_145851_c, field_145848_d,
					field_145849_e) == Blocks.piston_extension) {
				worldObj.setBlock(field_145851_c, field_145848_d,
						field_145849_e, field_145869_a, field_145876_i, 3);
				worldObj.func_147460_e(field_145851_c, field_145848_d,
						field_145849_e, field_145869_a);
			}
		} else {
			field_145873_m += 0.5F;

			if (field_145873_m >= 1.0F) {
				field_145873_m = 1.0F;
			}

			if (field_145875_k) {
				func_145863_a(field_145873_m, field_145873_m - field_145870_n
						+ 0.0625F);
			}
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound p_145841_1_) {
		super.writeToNBT(p_145841_1_);
		p_145841_1_.setInteger("blockId", Block.getIdFromBlock(field_145869_a));
		p_145841_1_.setInteger("blockData", field_145876_i);
		p_145841_1_.setInteger("facing", field_145874_j);
		p_145841_1_.setFloat("progress", field_145870_n);
		p_145841_1_.setBoolean("extending", field_145875_k);
	}
}
