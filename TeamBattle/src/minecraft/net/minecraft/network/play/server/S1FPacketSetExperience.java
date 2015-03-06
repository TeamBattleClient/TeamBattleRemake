package net.minecraft.network.play.server;

import java.io.IOException;

import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class S1FPacketSetExperience extends Packet {
	private int field_149399_b;
	private int field_149400_c;
	private float field_149401_a;

	public S1FPacketSetExperience() {
	}

	public S1FPacketSetExperience(float p_i45222_1_, int p_i45222_2_,
			int p_i45222_3_) {
		field_149401_a = p_i45222_1_;
		field_149399_b = p_i45222_2_;
		field_149400_c = p_i45222_3_;
	}

	public int func_149395_e() {
		return field_149400_c;
	}

	public int func_149396_d() {
		return field_149399_b;
	}

	public float func_149397_c() {
		return field_149401_a;
	}

	@Override
	public void processPacket(INetHandler p_148833_1_) {
		this.processPacket((INetHandlerPlayClient) p_148833_1_);
	}

	public void processPacket(INetHandlerPlayClient p_148833_1_) {
		p_148833_1_.handleSetExperience(this);
	}

	/**
	 * Reads the raw packet data from the data stream.
	 */
	@Override
	public void readPacketData(PacketBuffer p_148837_1_) throws IOException {
		field_149401_a = p_148837_1_.readFloat();
		field_149400_c = p_148837_1_.readShort();
		field_149399_b = p_148837_1_.readShort();
	}

	/**
	 * Writes the raw packet data to the data stream.
	 */
	@Override
	public void writePacketData(PacketBuffer p_148840_1_) throws IOException {
		p_148840_1_.writeFloat(field_149401_a);
		p_148840_1_.writeShort(field_149400_c);
		p_148840_1_.writeShort(field_149399_b);
	}
}
