package net.minecraft.network.play.server;

import java.io.IOException;

import net.minecraft.block.Block;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class S24PacketBlockAction extends Packet {
	private Block field_148871_f;
	private int field_148872_d;
	private int field_148873_e;
	private int field_148874_b;
	private int field_148875_c;
	private int field_148876_a;

	public S24PacketBlockAction() {
	}

	public S24PacketBlockAction(int p_i45176_1_, int p_i45176_2_,
			int p_i45176_3_, Block p_i45176_4_, int p_i45176_5_, int p_i45176_6_) {
		field_148876_a = p_i45176_1_;
		field_148874_b = p_i45176_2_;
		field_148875_c = p_i45176_3_;
		field_148872_d = p_i45176_5_;
		field_148873_e = p_i45176_6_;
		field_148871_f = p_i45176_4_;
	}

	public int func_148864_h() {
		return field_148873_e;
	}

	public int func_148865_f() {
		return field_148875_c;
	}

	public int func_148866_e() {
		return field_148874_b;
	}

	public int func_148867_d() {
		return field_148876_a;
	}

	public Block func_148868_c() {
		return field_148871_f;
	}

	public int func_148869_g() {
		return field_148872_d;
	}

	@Override
	public void processPacket(INetHandler p_148833_1_) {
		this.processPacket((INetHandlerPlayClient) p_148833_1_);
	}

	public void processPacket(INetHandlerPlayClient p_148833_1_) {
		p_148833_1_.handleBlockAction(this);
	}

	/**
	 * Reads the raw packet data from the data stream.
	 */
	@Override
	public void readPacketData(PacketBuffer p_148837_1_) throws IOException {
		field_148876_a = p_148837_1_.readInt();
		field_148874_b = p_148837_1_.readShort();
		field_148875_c = p_148837_1_.readInt();
		field_148872_d = p_148837_1_.readUnsignedByte();
		field_148873_e = p_148837_1_.readUnsignedByte();
		field_148871_f = Block
				.getBlockById(p_148837_1_.readVarIntFromBuffer() & 4095);
	}

	/**
	 * Writes the raw packet data to the data stream.
	 */
	@Override
	public void writePacketData(PacketBuffer p_148840_1_) throws IOException {
		p_148840_1_.writeInt(field_148876_a);
		p_148840_1_.writeShort(field_148874_b);
		p_148840_1_.writeInt(field_148875_c);
		p_148840_1_.writeByte(field_148872_d);
		p_148840_1_.writeByte(field_148873_e);
		p_148840_1_
				.writeVarIntToBuffer(Block.getIdFromBlock(field_148871_f) & 4095);
	}
}
