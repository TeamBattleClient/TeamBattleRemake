package net.minecraft.tileentity;

import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.world.World;

public class TileEntityMobSpawner extends TileEntity {
	private final MobSpawnerBaseLogic field_145882_a = new MobSpawnerBaseLogic() {

		@Override
		public void func_98267_a(int p_98267_1_) {
			TileEntityMobSpawner.this.worldObj.func_147452_c(
					TileEntityMobSpawner.this.field_145851_c,
					TileEntityMobSpawner.this.field_145848_d,
					TileEntityMobSpawner.this.field_145849_e,
					Blocks.mob_spawner, p_98267_1_, 0);
		}

		@Override
		public World getSpawnerWorld() {
			return TileEntityMobSpawner.this.worldObj;
		}

		@Override
		public int getSpawnerX() {
			return TileEntityMobSpawner.this.field_145851_c;
		}

		@Override
		public int getSpawnerY() {
			return TileEntityMobSpawner.this.field_145848_d;
		}

		@Override
		public int getSpawnerZ() {
			return TileEntityMobSpawner.this.field_145849_e;
		}

		@Override
		public void setRandomMinecart(
				MobSpawnerBaseLogic.WeightedRandomMinecart p_98277_1_) {
			super.setRandomMinecart(p_98277_1_);

			if (getSpawnerWorld() != null) {
				getSpawnerWorld().func_147471_g(
						TileEntityMobSpawner.this.field_145851_c,
						TileEntityMobSpawner.this.field_145848_d,
						TileEntityMobSpawner.this.field_145849_e);
			}
		}
	};

	public MobSpawnerBaseLogic func_145881_a() {
		return field_145882_a;
	}

	/**
	 * Overriden in a sign to provide the text.
	 */
	@Override
	public Packet getDescriptionPacket() {
		final NBTTagCompound var1 = new NBTTagCompound();
		writeToNBT(var1);
		var1.removeTag("SpawnPotentials");
		return new S35PacketUpdateTileEntity(field_145851_c, field_145848_d,
				field_145849_e, 1, var1);
	}

	@Override
	public void readFromNBT(NBTTagCompound p_145839_1_) {
		super.readFromNBT(p_145839_1_);
		field_145882_a.readFromNBT(p_145839_1_);
	}

	@Override
	public boolean receiveClientEvent(int p_145842_1_, int p_145842_2_) {
		return field_145882_a.setDelayToMin(p_145842_1_) ? true : super
				.receiveClientEvent(p_145842_1_, p_145842_2_);
	}

	@Override
	public void updateEntity() {
		field_145882_a.updateSpawner();
		super.updateEntity();
	}

	@Override
	public void writeToNBT(NBTTagCompound p_145841_1_) {
		super.writeToNBT(p_145841_1_);
		field_145882_a.writeToNBT(p_145841_1_);
	}
}
