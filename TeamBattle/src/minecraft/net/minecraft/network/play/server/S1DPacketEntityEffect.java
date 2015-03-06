package net.minecraft.network.play.server;

import java.io.IOException;

import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.potion.PotionEffect;

public class S1DPacketEntityEffect extends Packet {
	private short field_149431_d;
	private byte field_149432_b;
	private byte field_149433_c;
	private int field_149434_a;

	public S1DPacketEntityEffect() {
	}

	public S1DPacketEntityEffect(int p_i45237_1_, PotionEffect p_i45237_2_) {
		field_149434_a = p_i45237_1_;
		field_149432_b = (byte) (p_i45237_2_.getPotionID() & 255);
		field_149433_c = (byte) (p_i45237_2_.getAmplifier() & 255);

		if (p_i45237_2_.getDuration() > 32767) {
			field_149431_d = 32767;
		} else {
			field_149431_d = (short) p_i45237_2_.getDuration();
		}
	}

	public short func_149425_g() {
		return field_149431_d;
	}

	public int func_149426_d() {
		return field_149434_a;
	}

	public byte func_149427_e() {
		return field_149432_b;
	}

	public byte func_149428_f() {
		return field_149433_c;
	}

	public boolean func_149429_c() {
		return field_149431_d == 32767;
	}

	@Override
	public void processPacket(INetHandler p_148833_1_) {
		this.processPacket((INetHandlerPlayClient) p_148833_1_);
	}

	public void processPacket(INetHandlerPlayClient p_148833_1_) {
		p_148833_1_.handleEntityEffect(this);
	}

	/**
	 * Reads the raw packet data from the data stream.
	 */
	@Override
	public void readPacketData(PacketBuffer p_148837_1_) throws IOException {
		field_149434_a = p_148837_1_.readInt();
		field_149432_b = p_148837_1_.readByte();
		field_149433_c = p_148837_1_.readByte();
		field_149431_d = p_148837_1_.readShort();
	}

	/**
	 * Writes the raw packet data to the data stream.
	 */
	@Override
	public void writePacketData(PacketBuffer p_148840_1_) throws IOException {
		p_148840_1_.writeInt(field_149434_a);
		p_148840_1_.writeByte(field_149432_b);
		p_148840_1_.writeByte(field_149433_c);
		p_148840_1_.writeShort(field_149431_d);
	}
}
