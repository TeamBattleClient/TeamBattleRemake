package net.minecraft.network.play.server;

import java.io.IOException;

import net.minecraft.block.Block;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.world.World;

public class S23PacketBlockChange extends Packet {
	private Block field_148883_d;
	private int field_148884_e;
	private int field_148885_b;
	private int field_148886_c;
	private int field_148887_a;

	public S23PacketBlockChange() {
	}

	public S23PacketBlockChange(int p_i45177_1_, int p_i45177_2_,
			int p_i45177_3_, World p_i45177_4_) {
		field_148887_a = p_i45177_1_;
		field_148885_b = p_i45177_2_;
		field_148886_c = p_i45177_3_;
		field_148883_d = p_i45177_4_.getBlock(p_i45177_1_, p_i45177_2_,
				p_i45177_3_);
		field_148884_e = p_i45177_4_.getBlockMetadata(p_i45177_1_, p_i45177_2_,
				p_i45177_3_);
	}

	public int func_148877_f() {
		return field_148886_c;
	}

	public int func_148878_e() {
		return field_148885_b;
	}

	public int func_148879_d() {
		return field_148887_a;
	}

	public Block func_148880_c() {
		return field_148883_d;
	}

	public int func_148881_g() {
		return field_148884_e;
	}

	@Override
	public void processPacket(INetHandler p_148833_1_) {
		this.processPacket((INetHandlerPlayClient) p_148833_1_);
	}

	public void processPacket(INetHandlerPlayClient p_148833_1_) {
		p_148833_1_.handleBlockChange(this);
	}

	/**
	 * Reads the raw packet data from the data stream.
	 */
	@Override
	public void readPacketData(PacketBuffer p_148837_1_) throws IOException {
		field_148887_a = p_148837_1_.readInt();
		field_148885_b = p_148837_1_.readUnsignedByte();
		field_148886_c = p_148837_1_.readInt();
		field_148883_d = Block.getBlockById(p_148837_1_.readVarIntFromBuffer());
		field_148884_e = p_148837_1_.readUnsignedByte();
	}

	/**
	 * Returns a string formatted as comma separated [field]=[value] values.
	 * Used by Minecraft for logging purposes.
	 */
	@Override
	public String serialize() {
		return String.format(
				"type=%d, data=%d, x=%d, y=%d, z=%d",
				new Object[] {
						Integer.valueOf(Block.getIdFromBlock(field_148883_d)),
						Integer.valueOf(field_148884_e),
						Integer.valueOf(field_148887_a),
						Integer.valueOf(field_148885_b),
						Integer.valueOf(field_148886_c) });
	}

	/**
	 * Writes the raw packet data to the data stream.
	 */
	@Override
	public void writePacketData(PacketBuffer p_148840_1_) throws IOException {
		p_148840_1_.writeInt(field_148887_a);
		p_148840_1_.writeByte(field_148885_b);
		p_148840_1_.writeInt(field_148886_c);
		p_148840_1_.writeVarIntToBuffer(Block.getIdFromBlock(field_148883_d));
		p_148840_1_.writeByte(field_148884_e);
	}
}
