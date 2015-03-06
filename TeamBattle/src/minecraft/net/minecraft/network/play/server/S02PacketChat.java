package net.minecraft.network.play.server;

import java.io.IOException;

import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.IChatComponent;

public class S02PacketChat extends Packet {
	private boolean field_148918_b;
	private IChatComponent field_148919_a;

	public S02PacketChat() {
		field_148918_b = true;
	}

	public S02PacketChat(IChatComponent p_i45179_1_) {
		this(p_i45179_1_, true);
	}

	public S02PacketChat(IChatComponent p_i45180_1_, boolean p_i45180_2_) {
		field_148918_b = true;
		field_148919_a = p_i45180_1_;
		field_148918_b = p_i45180_2_;
	}

	public IChatComponent func_148915_c() {
		return field_148919_a;
	}

	public boolean func_148916_d() {
		return field_148918_b;
	}

	@Override
	public void processPacket(INetHandler p_148833_1_) {
		this.processPacket((INetHandlerPlayClient) p_148833_1_);
	}

	public void processPacket(INetHandlerPlayClient p_148833_1_) {
		p_148833_1_.handleChat(this);
	}

	/**
	 * Reads the raw packet data from the data stream.
	 */
	@Override
	public void readPacketData(PacketBuffer p_148837_1_) throws IOException {
		field_148919_a = IChatComponent.Serializer.func_150699_a(p_148837_1_
				.readStringFromBuffer(32767));
	}

	/**
	 * Returns a string formatted as comma separated [field]=[value] values.
	 * Used by Minecraft for logging purposes.
	 */
	@Override
	public String serialize() {
		return String.format("message=\'%s\'", new Object[] { field_148919_a });
	}

	/**
	 * Writes the raw packet data to the data stream.
	 */
	@Override
	public void writePacketData(PacketBuffer p_148840_1_) throws IOException {
		p_148840_1_.writeStringToBuffer(IChatComponent.Serializer
				.func_150696_a(field_148919_a));
	}
}
