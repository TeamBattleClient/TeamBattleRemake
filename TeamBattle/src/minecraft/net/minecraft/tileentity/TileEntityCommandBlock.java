package net.minecraft.tileentity;

import io.netty.buffer.ByteBuf;
import net.minecraft.command.server.CommandBlockLogic;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;

public class TileEntityCommandBlock extends TileEntity {
	private final CommandBlockLogic field_145994_a = new CommandBlockLogic() {

		@Override
		public int func_145751_f() {
			return 0;
		}

		@Override
		public void func_145752_a(String p_145752_1_) {
			super.func_145752_a(p_145752_1_);
			TileEntityCommandBlock.this.onInventoryChanged();
		}

		@Override
		public void func_145756_e() {
			TileEntityCommandBlock.this.getWorldObj().func_147471_g(
					TileEntityCommandBlock.this.field_145851_c,
					TileEntityCommandBlock.this.field_145848_d,
					TileEntityCommandBlock.this.field_145849_e);
		}

		@Override
		public void func_145757_a(ByteBuf p_145757_1_) {
			p_145757_1_.writeInt(TileEntityCommandBlock.this.field_145851_c);
			p_145757_1_.writeInt(TileEntityCommandBlock.this.field_145848_d);
			p_145757_1_.writeInt(TileEntityCommandBlock.this.field_145849_e);
		}

		@Override
		public World getEntityWorld() {
			return TileEntityCommandBlock.this.getWorldObj();
		}

		@Override
		public ChunkCoordinates getPlayerCoordinates() {
			return new ChunkCoordinates(
					TileEntityCommandBlock.this.field_145851_c,
					TileEntityCommandBlock.this.field_145848_d,
					TileEntityCommandBlock.this.field_145849_e);
		}
	};

	public CommandBlockLogic func_145993_a() {
		return field_145994_a;
	}

	/**
	 * Overriden in a sign to provide the text.
	 */
	@Override
	public Packet getDescriptionPacket() {
		final NBTTagCompound var1 = new NBTTagCompound();
		writeToNBT(var1);
		return new S35PacketUpdateTileEntity(field_145851_c, field_145848_d,
				field_145849_e, 2, var1);
	}

	@Override
	public void readFromNBT(NBTTagCompound p_145839_1_) {
		super.readFromNBT(p_145839_1_);
		field_145994_a.func_145759_b(p_145839_1_);
	}

	@Override
	public void writeToNBT(NBTTagCompound p_145841_1_) {
		super.writeToNBT(p_145841_1_);
		field_145994_a.func_145758_a(p_145841_1_);
	}
}
