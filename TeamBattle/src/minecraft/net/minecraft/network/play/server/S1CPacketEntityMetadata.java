package net.minecraft.network.play.server;

import java.io.IOException;
import java.util.List;

import net.minecraft.entity.DataWatcher;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class S1CPacketEntityMetadata extends Packet {
	private List field_149378_b;
	private int field_149379_a;

	public S1CPacketEntityMetadata() {
	}

	public S1CPacketEntityMetadata(int p_i45217_1_, DataWatcher p_i45217_2_,
			boolean p_i45217_3_) {
		field_149379_a = p_i45217_1_;

		if (p_i45217_3_) {
			field_149378_b = p_i45217_2_.getAllWatched();
		} else {
			field_149378_b = p_i45217_2_.getChanged();
		}
	}

	public int func_149375_d() {
		return field_149379_a;
	}

	public List func_149376_c() {
		return field_149378_b;
	}

	@Override
	public void processPacket(INetHandler p_148833_1_) {
		this.processPacket((INetHandlerPlayClient) p_148833_1_);
	}

	public void processPacket(INetHandlerPlayClient p_148833_1_) {
		p_148833_1_.handleEntityMetadata(this);
	}

	/**
	 * Reads the raw packet data from the data stream.
	 */
	@Override
	public void readPacketData(PacketBuffer p_148837_1_) throws IOException {
		field_149379_a = p_148837_1_.readInt();
		field_149378_b = DataWatcher
				.readWatchedListFromPacketBuffer(p_148837_1_);
	}

	/**
	 * Writes the raw packet data to the data stream.
	 */
	@Override
	public void writePacketData(PacketBuffer p_148840_1_) throws IOException {
		p_148840_1_.writeInt(field_149379_a);
		DataWatcher.writeWatchedListToPacketBuffer(field_149378_b, p_148840_1_);
	}
}
