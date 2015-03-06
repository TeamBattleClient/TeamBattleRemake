package net.minecraft.network.play.server;

import java.io.IOException;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class S35PacketUpdateTileEntity extends Packet {
	private int field_148859_d;
	private NBTTagCompound field_148860_e;
	private int field_148861_b;
	private int field_148862_c;
	private int field_148863_a;

	public S35PacketUpdateTileEntity() {
	}

	public S35PacketUpdateTileEntity(int p_i45175_1_, int p_i45175_2_,
			int p_i45175_3_, int p_i45175_4_, NBTTagCompound p_i45175_5_) {
		field_148863_a = p_i45175_1_;
		field_148861_b = p_i45175_2_;
		field_148862_c = p_i45175_3_;
		field_148859_d = p_i45175_4_;
		field_148860_e = p_i45175_5_;
	}

	public int func_148853_f() {
		return field_148859_d;
	}

	public int func_148854_e() {
		return field_148862_c;
	}

	public int func_148855_d() {
		return field_148861_b;
	}

	public int func_148856_c() {
		return field_148863_a;
	}

	public NBTTagCompound func_148857_g() {
		return field_148860_e;
	}

	@Override
	public void processPacket(INetHandler p_148833_1_) {
		this.processPacket((INetHandlerPlayClient) p_148833_1_);
	}

	public void processPacket(INetHandlerPlayClient p_148833_1_) {
		p_148833_1_.handleUpdateTileEntity(this);
	}

	/**
	 * Reads the raw packet data from the data stream.
	 */
	@Override
	public void readPacketData(PacketBuffer p_148837_1_) throws IOException {
		field_148863_a = p_148837_1_.readInt();
		field_148861_b = p_148837_1_.readShort();
		field_148862_c = p_148837_1_.readInt();
		field_148859_d = p_148837_1_.readUnsignedByte();
		field_148860_e = p_148837_1_.readNBTTagCompoundFromBuffer();
	}

	/**
	 * Writes the raw packet data to the data stream.
	 */
	@Override
	public void writePacketData(PacketBuffer p_148840_1_) throws IOException {
		p_148840_1_.writeInt(field_148863_a);
		p_148840_1_.writeShort(field_148861_b);
		p_148840_1_.writeInt(field_148862_c);
		p_148840_1_.writeByte((byte) field_148859_d);
		p_148840_1_.writeNBTTagCompoundToBuffer(field_148860_e);
	}
}
