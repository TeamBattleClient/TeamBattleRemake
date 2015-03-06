package net.minecraft.network.play.server;

import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.world.World;

public class S0APacketUseBed extends Packet {
	private int field_149094_d;
	private int field_149095_b;
	private int field_149096_c;
	private int field_149097_a;

	public S0APacketUseBed() {
	}

	public S0APacketUseBed(EntityPlayer p_i45210_1_, int p_i45210_2_,
			int p_i45210_3_, int p_i45210_4_) {
		field_149095_b = p_i45210_2_;
		field_149096_c = p_i45210_3_;
		field_149094_d = p_i45210_4_;
		field_149097_a = p_i45210_1_.getEntityId();
	}

	public int func_149089_e() {
		return field_149094_d;
	}

	public int func_149090_d() {
		return field_149096_c;
	}

	public EntityPlayer func_149091_a(World p_149091_1_) {
		return (EntityPlayer) p_149091_1_.getEntityByID(field_149097_a);
	}

	public int func_149092_c() {
		return field_149095_b;
	}

	@Override
	public void processPacket(INetHandler p_148833_1_) {
		this.processPacket((INetHandlerPlayClient) p_148833_1_);
	}

	public void processPacket(INetHandlerPlayClient p_148833_1_) {
		p_148833_1_.handleUseBed(this);
	}

	/**
	 * Reads the raw packet data from the data stream.
	 */
	@Override
	public void readPacketData(PacketBuffer p_148837_1_) throws IOException {
		field_149097_a = p_148837_1_.readInt();
		field_149095_b = p_148837_1_.readInt();
		field_149096_c = p_148837_1_.readByte();
		field_149094_d = p_148837_1_.readInt();
	}

	/**
	 * Writes the raw packet data to the data stream.
	 */
	@Override
	public void writePacketData(PacketBuffer p_148840_1_) throws IOException {
		p_148840_1_.writeInt(field_149097_a);
		p_148840_1_.writeInt(field_149095_b);
		p_148840_1_.writeByte(field_149096_c);
		p_148840_1_.writeInt(field_149094_d);
	}
}
