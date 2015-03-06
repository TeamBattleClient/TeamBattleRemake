package net.minecraft.network.play.server;

import java.io.IOException;

import net.minecraft.entity.Entity;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.world.World;

public class S14PacketEntity extends Packet {
	public static class S15PacketEntityRelMove extends S14PacketEntity {

		public S15PacketEntityRelMove() {
		}

		public S15PacketEntityRelMove(int p_i45203_1_, byte p_i45203_2_,
				byte p_i45203_3_, byte p_i45203_4_) {
			super(p_i45203_1_);
			field_149072_b = p_i45203_2_;
			field_149073_c = p_i45203_3_;
			field_149070_d = p_i45203_4_;
		}

		@Override
		public void processPacket(INetHandler p_148833_1_) {
			super.processPacket((INetHandlerPlayClient) p_148833_1_);
		}

		@Override
		public void readPacketData(PacketBuffer p_148837_1_) throws IOException {
			super.readPacketData(p_148837_1_);
			field_149072_b = p_148837_1_.readByte();
			field_149073_c = p_148837_1_.readByte();
			field_149070_d = p_148837_1_.readByte();
		}

		@Override
		public String serialize() {
			return super.serialize()
					+ String.format(
							", xa=%d, ya=%d, za=%d",
							new Object[] { Byte.valueOf(field_149072_b),
									Byte.valueOf(field_149073_c),
									Byte.valueOf(field_149070_d) });
		}

		@Override
		public void writePacketData(PacketBuffer p_148840_1_)
				throws IOException {
			super.writePacketData(p_148840_1_);
			p_148840_1_.writeByte(field_149072_b);
			p_148840_1_.writeByte(field_149073_c);
			p_148840_1_.writeByte(field_149070_d);
		}
	}

	public static class S16PacketEntityLook extends S14PacketEntity {

		public S16PacketEntityLook() {
			field_149069_g = true;
		}

		public S16PacketEntityLook(int p_i45205_1_, byte p_i45205_2_,
				byte p_i45205_3_) {
			super(p_i45205_1_);
			field_149071_e = p_i45205_2_;
			field_149068_f = p_i45205_3_;
			field_149069_g = true;
		}

		@Override
		public void processPacket(INetHandler p_148833_1_) {
			super.processPacket((INetHandlerPlayClient) p_148833_1_);
		}

		@Override
		public void readPacketData(PacketBuffer p_148837_1_) throws IOException {
			super.readPacketData(p_148837_1_);
			field_149071_e = p_148837_1_.readByte();
			field_149068_f = p_148837_1_.readByte();
		}

		@Override
		public String serialize() {
			return super.serialize()
					+ String.format(
							", yRot=%d, xRot=%d",
							new Object[] { Byte.valueOf(field_149071_e),
									Byte.valueOf(field_149068_f) });
		}

		@Override
		public void writePacketData(PacketBuffer p_148840_1_)
				throws IOException {
			super.writePacketData(p_148840_1_);
			p_148840_1_.writeByte(field_149071_e);
			p_148840_1_.writeByte(field_149068_f);
		}
	}

	public static class S17PacketEntityLookMove extends S14PacketEntity {

		public S17PacketEntityLookMove() {
			field_149069_g = true;
		}

		public S17PacketEntityLookMove(int p_i45204_1_, byte p_i45204_2_,
				byte p_i45204_3_, byte p_i45204_4_, byte p_i45204_5_,
				byte p_i45204_6_) {
			super(p_i45204_1_);
			field_149072_b = p_i45204_2_;
			field_149073_c = p_i45204_3_;
			field_149070_d = p_i45204_4_;
			field_149071_e = p_i45204_5_;
			field_149068_f = p_i45204_6_;
			field_149069_g = true;
		}

		@Override
		public void processPacket(INetHandler p_148833_1_) {
			super.processPacket((INetHandlerPlayClient) p_148833_1_);
		}

		@Override
		public void readPacketData(PacketBuffer p_148837_1_) throws IOException {
			super.readPacketData(p_148837_1_);
			field_149072_b = p_148837_1_.readByte();
			field_149073_c = p_148837_1_.readByte();
			field_149070_d = p_148837_1_.readByte();
			field_149071_e = p_148837_1_.readByte();
			field_149068_f = p_148837_1_.readByte();
		}

		@Override
		public String serialize() {
			return super.serialize()
					+ String.format(
							", xa=%d, ya=%d, za=%d, yRot=%d, xRot=%d",
							new Object[] { Byte.valueOf(field_149072_b),
									Byte.valueOf(field_149073_c),
									Byte.valueOf(field_149070_d),
									Byte.valueOf(field_149071_e),
									Byte.valueOf(field_149068_f) });
		}

		@Override
		public void writePacketData(PacketBuffer p_148840_1_)
				throws IOException {
			super.writePacketData(p_148840_1_);
			p_148840_1_.writeByte(field_149072_b);
			p_148840_1_.writeByte(field_149073_c);
			p_148840_1_.writeByte(field_149070_d);
			p_148840_1_.writeByte(field_149071_e);
			p_148840_1_.writeByte(field_149068_f);
		}
	}

	public byte field_149068_f;
	protected boolean field_149069_g;
	protected byte field_149070_d;
	public byte field_149071_e;

	protected byte field_149072_b;

	protected byte field_149073_c;

	protected int field_149074_a;

	public S14PacketEntity() {
	}

	public S14PacketEntity(int p_i45206_1_) {
		field_149074_a = p_i45206_1_;
	}

	public boolean func_149060_h() {
		return field_149069_g;
	}

	public byte func_149061_d() {
		return field_149073_c;
	}

	public byte func_149062_c() {
		return field_149072_b;
	}

	public byte func_149063_g() {
		return field_149068_f;
	}

	public byte func_149064_e() {
		return field_149070_d;
	}

	public Entity func_149065_a(World p_149065_1_) {
		return p_149065_1_.getEntityByID(field_149074_a);
	}

	public byte func_149066_f() {
		return field_149071_e;
	}

	@Override
	public void processPacket(INetHandler p_148833_1_) {
		this.processPacket((INetHandlerPlayClient) p_148833_1_);
	}

	public void processPacket(INetHandlerPlayClient p_148833_1_) {
		p_148833_1_.handleEntityMovement(this);
	}

	/**
	 * Reads the raw packet data from the data stream.
	 */
	@Override
	public void readPacketData(PacketBuffer p_148837_1_) throws IOException {
		field_149074_a = p_148837_1_.readInt();
	}

	/**
	 * Returns a string formatted as comma separated [field]=[value] values.
	 * Used by Minecraft for logging purposes.
	 */
	@Override
	public String serialize() {
		return String.format("id=%d",
				new Object[] { Integer.valueOf(field_149074_a) });
	}

	@Override
	public String toString() {
		return "Entity_" + super.toString();
	}

	/**
	 * Writes the raw packet data to the data stream.
	 */
	@Override
	public void writePacketData(PacketBuffer p_148840_1_) throws IOException {
		p_148840_1_.writeInt(field_149074_a);
	}
}
