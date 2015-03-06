package net.minecraft.network.play.server;

import java.io.IOException;

import net.minecraft.entity.Entity;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.world.World;

public class S19PacketEntityStatus extends Packet {
	private byte field_149163_b;
	private int field_149164_a;

	public S19PacketEntityStatus() {
	}

	public S19PacketEntityStatus(Entity p_i45192_1_, byte p_i45192_2_) {
		field_149164_a = p_i45192_1_.getEntityId();
		field_149163_b = p_i45192_2_;
	}

	public byte func_149160_c() {
		return field_149163_b;
	}

	public Entity func_149161_a(World p_149161_1_) {
		return p_149161_1_.getEntityByID(field_149164_a);
	}

	@Override
	public void processPacket(INetHandler p_148833_1_) {
		this.processPacket((INetHandlerPlayClient) p_148833_1_);
	}

	public void processPacket(INetHandlerPlayClient p_148833_1_) {
		p_148833_1_.handleEntityStatus(this);
	}

	/**
	 * Reads the raw packet data from the data stream.
	 */
	@Override
	public void readPacketData(PacketBuffer p_148837_1_) throws IOException {
		field_149164_a = p_148837_1_.readInt();
		field_149163_b = p_148837_1_.readByte();
	}

	/**
	 * Writes the raw packet data to the data stream.
	 */
	@Override
	public void writePacketData(PacketBuffer p_148840_1_) throws IOException {
		p_148840_1_.writeInt(field_149164_a);
		p_148840_1_.writeByte(field_149163_b);
	}
}
