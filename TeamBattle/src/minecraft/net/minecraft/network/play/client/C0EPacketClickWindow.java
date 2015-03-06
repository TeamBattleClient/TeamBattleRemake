package net.minecraft.network.play.client;

import java.io.IOException;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

public class C0EPacketClickWindow extends Packet {
	private int field_149549_f;
	private short field_149550_d;
	private ItemStack field_149551_e;
	private int field_149552_b;
	private int field_149553_c;
	private int field_149554_a;

	public C0EPacketClickWindow() {
	}

	public C0EPacketClickWindow(int p_i45246_1_, int p_i45246_2_,
			int p_i45246_3_, int p_i45246_4_, ItemStack p_i45246_5_,
			short p_i45246_6_) {
		field_149554_a = p_i45246_1_;
		field_149552_b = p_i45246_2_;
		field_149553_c = p_i45246_3_;
		field_149551_e = p_i45246_5_ != null ? p_i45246_5_.copy() : null;
		field_149550_d = p_i45246_6_;
		field_149549_f = p_i45246_4_;
	}

	public int func_149542_h() {
		return field_149549_f;
	}

	public int func_149543_e() {
		return field_149553_c;
	}

	public int func_149544_d() {
		return field_149552_b;
	}

	public ItemStack func_149546_g() {
		return field_149551_e;
	}

	public short func_149547_f() {
		return field_149550_d;
	}

	public int func_149548_c() {
		return field_149554_a;
	}

	@Override
	public void processPacket(INetHandler p_148833_1_) {
		this.processPacket((INetHandlerPlayServer) p_148833_1_);
	}

	public void processPacket(INetHandlerPlayServer p_148833_1_) {
		p_148833_1_.processClickWindow(this);
	}

	/**
	 * Reads the raw packet data from the data stream.
	 */
	@Override
	public void readPacketData(PacketBuffer p_148837_1_) throws IOException {
		field_149554_a = p_148837_1_.readByte();
		field_149552_b = p_148837_1_.readShort();
		field_149553_c = p_148837_1_.readByte();
		field_149550_d = p_148837_1_.readShort();
		field_149549_f = p_148837_1_.readByte();
		field_149551_e = p_148837_1_.readItemStackFromBuffer();
	}

	/**
	 * Returns a string formatted as comma separated [field]=[value] values.
	 * Used by Minecraft for logging purposes.
	 */
	@Override
	public String serialize() {
		return field_149551_e != null ? String
				.format("id=%d, slot=%d, button=%d, type=%d, itemid=%d, itemcount=%d, itemaux=%d",
						new Object[] {
								Integer.valueOf(field_149554_a),
								Integer.valueOf(field_149552_b),
								Integer.valueOf(field_149553_c),
								Integer.valueOf(field_149549_f),
								Integer.valueOf(Item
										.getIdFromItem(field_149551_e.getItem())),
								Integer.valueOf(field_149551_e.stackSize),
								Integer.valueOf(field_149551_e.getItemDamage()) })
				: String.format(
						"id=%d, slot=%d, button=%d, type=%d, itemid=-1",
						new Object[] { Integer.valueOf(field_149554_a),
								Integer.valueOf(field_149552_b),
								Integer.valueOf(field_149553_c),
								Integer.valueOf(field_149549_f) });
	}

	/**
	 * Writes the raw packet data to the data stream.
	 */
	@Override
	public void writePacketData(PacketBuffer p_148840_1_) throws IOException {
		p_148840_1_.writeByte(field_149554_a);
		p_148840_1_.writeShort(field_149552_b);
		p_148840_1_.writeByte(field_149553_c);
		p_148840_1_.writeShort(field_149550_d);
		p_148840_1_.writeByte(field_149549_f);
		p_148840_1_.writeItemStackToBuffer(field_149551_e);
	}
}
