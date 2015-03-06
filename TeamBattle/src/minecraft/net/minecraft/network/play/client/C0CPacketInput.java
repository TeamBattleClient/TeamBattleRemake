package net.minecraft.network.play.client;

import java.io.IOException;

import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

public class C0CPacketInput extends Packet {
	private boolean field_149621_d;
	private float field_149622_b;
	private boolean field_149623_c;
	private float field_149624_a;

	public C0CPacketInput() {
	}

	public C0CPacketInput(float p_i45261_1_, float p_i45261_2_,
			boolean p_i45261_3_, boolean p_i45261_4_) {
		field_149624_a = p_i45261_1_;
		field_149622_b = p_i45261_2_;
		field_149623_c = p_i45261_3_;
		field_149621_d = p_i45261_4_;
	}

	public float func_149616_d() {
		return field_149622_b;
	}

	public boolean func_149617_f() {
		return field_149621_d;
	}

	public boolean func_149618_e() {
		return field_149623_c;
	}

	public float func_149620_c() {
		return field_149624_a;
	}

	@Override
	public void processPacket(INetHandler p_148833_1_) {
		this.processPacket((INetHandlerPlayServer) p_148833_1_);
	}

	public void processPacket(INetHandlerPlayServer p_148833_1_) {
		p_148833_1_.processInput(this);
	}

	/**
	 * Reads the raw packet data from the data stream.
	 */
	@Override
	public void readPacketData(PacketBuffer p_148837_1_) throws IOException {
		field_149624_a = p_148837_1_.readFloat();
		field_149622_b = p_148837_1_.readFloat();
		field_149623_c = p_148837_1_.readBoolean();
		field_149621_d = p_148837_1_.readBoolean();
	}

	/**
	 * Writes the raw packet data to the data stream.
	 */
	@Override
	public void writePacketData(PacketBuffer p_148840_1_) throws IOException {
		p_148840_1_.writeFloat(field_149624_a);
		p_148840_1_.writeFloat(field_149622_b);
		p_148840_1_.writeBoolean(field_149623_c);
		p_148840_1_.writeBoolean(field_149621_d);
	}
}
