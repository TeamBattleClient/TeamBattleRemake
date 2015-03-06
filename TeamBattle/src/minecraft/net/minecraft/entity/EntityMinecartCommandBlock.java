package net.minecraft.entity;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.command.server.CommandBlockLogic;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityMinecartCommandBlock extends EntityMinecart {
	private int field_145823_b = 0;
	private final CommandBlockLogic field_145824_a = new CommandBlockLogic() {

		@Override
		public int func_145751_f() {
			return 1;
		}

		@Override
		public void func_145756_e() {
			EntityMinecartCommandBlock.this.getDataWatcher().updateObject(23,
					func_145753_i());
			EntityMinecartCommandBlock.this.getDataWatcher().updateObject(24,
					IChatComponent.Serializer.func_150696_a(func_145749_h()));
		}

		@Override
		public void func_145757_a(ByteBuf p_145757_1_) {
			p_145757_1_.writeInt(EntityMinecartCommandBlock.this.getEntityId());
		}

		@Override
		public World getEntityWorld() {
			return EntityMinecartCommandBlock.this.worldObj;
		}

		@Override
		public ChunkCoordinates getPlayerCoordinates() {
			return new ChunkCoordinates(
					MathHelper
							.floor_double(EntityMinecartCommandBlock.this.posX),
					MathHelper
							.floor_double(EntityMinecartCommandBlock.this.posY + 0.5D),
					MathHelper
							.floor_double(EntityMinecartCommandBlock.this.posZ));
		}
	};

	public EntityMinecartCommandBlock(World p_i45321_1_) {
		super(p_i45321_1_);
	}

	public EntityMinecartCommandBlock(World p_i45322_1_, double p_i45322_2_,
			double p_i45322_4_, double p_i45322_6_) {
		super(p_i45322_1_, p_i45322_2_, p_i45322_4_, p_i45322_6_);
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		getDataWatcher().addObject(23, "");
		getDataWatcher().addObject(24, "");
	}

	@Override
	public void func_145781_i(int p_145781_1_) {
		super.func_145781_i(p_145781_1_);

		if (p_145781_1_ == 24) {
			try {
				field_145824_a.func_145750_b(IChatComponent.Serializer
						.func_150699_a(getDataWatcher()
								.getWatchableObjectString(24)));
			} catch (final Throwable var3) {
				;
			}
		} else if (p_145781_1_ == 23) {
			field_145824_a.func_145752_a(getDataWatcher()
					.getWatchableObjectString(23));
		}
	}

	@Override
	public Block func_145817_o() {
		return Blocks.command_block;
	}

	public CommandBlockLogic func_145822_e() {
		return field_145824_a;
	}

	@Override
	public int getMinecartType() {
		return 6;
	}

	/**
	 * First layer of player interaction
	 */
	@Override
	public boolean interactFirst(EntityPlayer p_130002_1_) {
		if (worldObj.isClient) {
			p_130002_1_.func_146095_a(func_145822_e());
		}

		return super.interactFirst(p_130002_1_);
	}

	/**
	 * Called every tick the minecart is on an activator rail. Args: x, y, z, is
	 * the rail receiving power
	 */
	@Override
	public void onActivatorRailPass(int p_96095_1_, int p_96095_2_,
			int p_96095_3_, boolean p_96095_4_) {
		if (p_96095_4_ && ticksExisted - field_145823_b >= 4) {
			func_145822_e().func_145755_a(worldObj);
			field_145823_b = ticksExisted;
		}
	}

	/**
	 * (abstract) Protected helper method to read subclass entity data from NBT.
	 */
	@Override
	protected void readEntityFromNBT(NBTTagCompound p_70037_1_) {
		super.readEntityFromNBT(p_70037_1_);
		field_145824_a.func_145759_b(p_70037_1_);
		getDataWatcher().updateObject(23, func_145822_e().func_145753_i());
		getDataWatcher().updateObject(
				24,
				IChatComponent.Serializer.func_150696_a(func_145822_e()
						.func_145749_h()));
	}

	/**
	 * (abstract) Protected helper method to write subclass entity data to NBT.
	 */
	@Override
	protected void writeEntityToNBT(NBTTagCompound p_70014_1_) {
		super.writeEntityToNBT(p_70014_1_);
		field_145824_a.func_145758_a(p_70014_1_);
	}
}
