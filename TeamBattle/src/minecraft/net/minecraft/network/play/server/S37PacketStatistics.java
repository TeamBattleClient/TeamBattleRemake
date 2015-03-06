package net.minecraft.network.play.server;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatList;

import com.google.common.collect.Maps;

public class S37PacketStatistics extends Packet {
	private Map field_148976_a;

	public S37PacketStatistics() {
	}

	public S37PacketStatistics(Map p_i45173_1_) {
		field_148976_a = p_i45173_1_;
	}

	public Map func_148974_c() {
		return field_148976_a;
	}

	@Override
	public void processPacket(INetHandler p_148833_1_) {
		this.processPacket((INetHandlerPlayClient) p_148833_1_);
	}

	public void processPacket(INetHandlerPlayClient p_148833_1_) {
		p_148833_1_.handleStatistics(this);
	}

	/**
	 * Reads the raw packet data from the data stream.
	 */
	@Override
	public void readPacketData(PacketBuffer p_148837_1_) throws IOException {
		final int var2 = p_148837_1_.readVarIntFromBuffer();
		field_148976_a = Maps.newHashMap();

		for (int var3 = 0; var3 < var2; ++var3) {
			final StatBase var4 = StatList.func_151177_a(p_148837_1_
					.readStringFromBuffer(32767));
			final int var5 = p_148837_1_.readVarIntFromBuffer();

			if (var4 != null) {
				field_148976_a.put(var4, Integer.valueOf(var5));
			}
		}
	}

	/**
	 * Returns a string formatted as comma separated [field]=[value] values.
	 * Used by Minecraft for logging purposes.
	 */
	@Override
	public String serialize() {
		return String.format("count=%d",
				new Object[] { Integer.valueOf(field_148976_a.size()) });
	}

	/**
	 * Writes the raw packet data to the data stream.
	 */
	@Override
	public void writePacketData(PacketBuffer p_148840_1_) throws IOException {
		p_148840_1_.writeVarIntToBuffer(field_148976_a.size());
		final Iterator var2 = field_148976_a.entrySet().iterator();

		while (var2.hasNext()) {
			final Entry var3 = (Entry) var2.next();
			p_148840_1_.writeStringToBuffer(((StatBase) var3.getKey()).statId);
			p_148840_1_.writeVarIntToBuffer(((Integer) var3.getValue())
					.intValue());
		}
	}
}
