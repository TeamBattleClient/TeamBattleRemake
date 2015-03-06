package net.minecraft.network.play.server;

import java.io.IOException;

import net.minecraft.entity.Entity;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class S12PacketEntityVelocity extends Packet {
	private int field_149414_d;
	private int field_149415_b;
	private int field_149416_c;
	private int field_149417_a;

	public S12PacketEntityVelocity() {
	}

	public S12PacketEntityVelocity(Entity p_i45219_1_) {
		this(p_i45219_1_.getEntityId(), p_i45219_1_.motionX,
				p_i45219_1_.motionY, p_i45219_1_.motionZ);
	}

	public S12PacketEntityVelocity(int p_i45220_1_, double p_i45220_2_,
			double p_i45220_4_, double p_i45220_6_) {
		field_149417_a = p_i45220_1_;
		final double var8 = 3.9D;

		if (p_i45220_2_ < -var8) {
			p_i45220_2_ = -var8;
		}

		if (p_i45220_4_ < -var8) {
			p_i45220_4_ = -var8;
		}

		if (p_i45220_6_ < -var8) {
			p_i45220_6_ = -var8;
		}

		if (p_i45220_2_ > var8) {
			p_i45220_2_ = var8;
		}

		if (p_i45220_4_ > var8) {
			p_i45220_4_ = var8;
		}

		if (p_i45220_6_ > var8) {
			p_i45220_6_ = var8;
		}

		field_149415_b = (int) (p_i45220_2_ * 8000.0D);
		field_149416_c = (int) (p_i45220_4_ * 8000.0D);
		field_149414_d = (int) (p_i45220_6_ * 8000.0D);
	}

	public int func_149409_f() {
		return field_149414_d;
	}

	public int func_149410_e() {
		return field_149416_c;
	}

	public int func_149411_d() {
		return field_149415_b;
	}

	public int func_149412_c() {
		return field_149417_a;
	}

	@Override
	public void processPacket(INetHandler p_148833_1_) {
		this.processPacket((INetHandlerPlayClient) p_148833_1_);
	}

	public void processPacket(INetHandlerPlayClient p_148833_1_) {
		p_148833_1_.handleEntityVelocity(this);
	}

	/**
	 * Reads the raw packet data from the data stream.
	 */
	@Override
	public void readPacketData(PacketBuffer p_148837_1_) throws IOException {
		field_149417_a = p_148837_1_.readInt();
		field_149415_b = p_148837_1_.readShort();
		field_149416_c = p_148837_1_.readShort();
		field_149414_d = p_148837_1_.readShort();
	}

	/**
	 * Returns a string formatted as comma separated [field]=[value] values.
	 * Used by Minecraft for logging purposes.
	 */
	@Override
	public String serialize() {
		return String.format(
				"id=%d, x=%.2f, y=%.2f, z=%.2f",
				new Object[] { Integer.valueOf(field_149417_a),
						Float.valueOf(field_149415_b / 8000.0F),
						Float.valueOf(field_149416_c / 8000.0F),
						Float.valueOf(field_149414_d / 8000.0F) });
	}

	/**
	 * Writes the raw packet data to the data stream.
	 */
	@Override
	public void writePacketData(PacketBuffer p_148840_1_) throws IOException {
		p_148840_1_.writeInt(field_149417_a);
		p_148840_1_.writeShort(field_149415_b);
		p_148840_1_.writeShort(field_149416_c);
		p_148840_1_.writeShort(field_149414_d);
	}
}
