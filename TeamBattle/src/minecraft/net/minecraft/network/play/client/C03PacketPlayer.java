package net.minecraft.network.play.client;

import java.io.IOException;

import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

public class C03PacketPlayer extends Packet {
	public static class C04PacketPlayerPosition extends C03PacketPlayer {

		public C04PacketPlayerPosition() {
			field_149480_h = true;
		}

		public C04PacketPlayerPosition(double p_i45253_1_, double p_i45253_3_,
				double p_i45253_5_, double p_i45253_7_, boolean p_i45253_9_) {
			field_149479_a = p_i45253_1_;
			field_149477_b = p_i45253_3_;
			field_149475_d = p_i45253_5_;
			field_149478_c = p_i45253_7_;
			field_149474_g = p_i45253_9_;
			field_149480_h = true;
		}

		@Override
		public void processPacket(INetHandler p_148833_1_) {
			super.processPacket((INetHandlerPlayServer) p_148833_1_);
		}

		@Override
		public void readPacketData(PacketBuffer p_148837_1_) throws IOException {
			field_149479_a = p_148837_1_.readDouble();
			field_149477_b = p_148837_1_.readDouble();
			field_149475_d = p_148837_1_.readDouble();
			field_149478_c = p_148837_1_.readDouble();
			super.readPacketData(p_148837_1_);
		}

		@Override
		public void writePacketData(PacketBuffer p_148840_1_)
				throws IOException {
			p_148840_1_.writeDouble(field_149479_a);
			p_148840_1_.writeDouble(field_149477_b);
			p_148840_1_.writeDouble(field_149475_d);
			p_148840_1_.writeDouble(field_149478_c);
			super.writePacketData(p_148840_1_);
		}
	}

	public static class C05PacketPlayerLook extends C03PacketPlayer {

		public C05PacketPlayerLook() {
			field_149481_i = true;
		}

		public C05PacketPlayerLook(float p_i45255_1_, float p_i45255_2_,
				boolean p_i45255_3_) {
			field_149476_e = p_i45255_1_;
			field_149473_f = p_i45255_2_;
			field_149474_g = p_i45255_3_;
			field_149481_i = true;
		}

		@Override
		public void processPacket(INetHandler p_148833_1_) {
			super.processPacket((INetHandlerPlayServer) p_148833_1_);
		}

		@Override
		public void readPacketData(PacketBuffer p_148837_1_) throws IOException {
			field_149476_e = p_148837_1_.readFloat();
			field_149473_f = p_148837_1_.readFloat();
			super.readPacketData(p_148837_1_);
		}

		@Override
		public void writePacketData(PacketBuffer p_148840_1_)
				throws IOException {
			p_148840_1_.writeFloat(field_149476_e);
			p_148840_1_.writeFloat(field_149473_f);
			super.writePacketData(p_148840_1_);
		}
	}

	public static class C06PacketPlayerPosLook extends C03PacketPlayer {

		public C06PacketPlayerPosLook() {
			field_149480_h = true;
			field_149481_i = true;
		}

		public C06PacketPlayerPosLook(double p_i45254_1_, double p_i45254_3_,
				double p_i45254_5_, double p_i45254_7_, float p_i45254_9_,
				float p_i45254_10_, boolean p_i45254_11_) {
			field_149479_a = p_i45254_1_;
			field_149477_b = p_i45254_3_;
			field_149475_d = p_i45254_5_;
			field_149478_c = p_i45254_7_;
			field_149476_e = p_i45254_9_;
			field_149473_f = p_i45254_10_;
			field_149474_g = p_i45254_11_;
			field_149481_i = true;
			field_149480_h = true;
		}

		@Override
		public void processPacket(INetHandler p_148833_1_) {
			super.processPacket((INetHandlerPlayServer) p_148833_1_);
		}

		@Override
		public void readPacketData(PacketBuffer p_148837_1_) throws IOException {
			field_149479_a = p_148837_1_.readDouble();
			field_149477_b = p_148837_1_.readDouble();
			field_149475_d = p_148837_1_.readDouble();
			field_149478_c = p_148837_1_.readDouble();
			field_149476_e = p_148837_1_.readFloat();
			field_149473_f = p_148837_1_.readFloat();
			super.readPacketData(p_148837_1_);
		}

		@Override
		public void writePacketData(PacketBuffer p_148840_1_)
				throws IOException {
			p_148840_1_.writeDouble(field_149479_a);
			p_148840_1_.writeDouble(field_149477_b);
			p_148840_1_.writeDouble(field_149475_d);
			p_148840_1_.writeDouble(field_149478_c);
			p_148840_1_.writeFloat(field_149476_e);
			p_148840_1_.writeFloat(field_149473_f);
			super.writePacketData(p_148840_1_);
		}
	}

	public float field_149473_f;
	public boolean field_149474_g;
	public double field_149475_d;
	public float field_149476_e;
	public double field_149477_b;
	public double field_149478_c;

	public double field_149479_a;

	public boolean field_149480_h;

	public boolean field_149481_i;

	public C03PacketPlayer() {
	}

	public C03PacketPlayer(boolean p_i45256_1_) {
		field_149474_g = p_i45256_1_;
	}

	public float func_149462_g() {
		return field_149476_e;
	}

	public boolean func_149463_k() {
		return field_149481_i;
	}

	public double func_149464_c() {
		return field_149479_a;
	}

	public boolean func_149465_i() {
		return field_149474_g;
	}

	public boolean func_149466_j() {
		return field_149480_h;
	}

	public double func_149467_d() {
		return field_149477_b;
	}

	public void func_149469_a(boolean p_149469_1_) {
		field_149480_h = p_149469_1_;
	}

	public float func_149470_h() {
		return field_149473_f;
	}

	public double func_149471_f() {
		return field_149475_d;
	}

	public double func_149472_e() {
		return field_149478_c;
	}

	@Override
	public void processPacket(INetHandler p_148833_1_) {
		this.processPacket((INetHandlerPlayServer) p_148833_1_);
	}

	public void processPacket(INetHandlerPlayServer p_148833_1_) {
		p_148833_1_.processPlayer(this);
	}

	/**
	 * Reads the raw packet data from the data stream.
	 */
	@Override
	public void readPacketData(PacketBuffer p_148837_1_) throws IOException {
		field_149474_g = p_148837_1_.readUnsignedByte() != 0;
	}

	public void setOnGround(boolean onGround) {
		field_149474_g = onGround;
	}

	/**
	 * Writes the raw packet data to the data stream.
	 */
	@Override
	public void writePacketData(PacketBuffer p_148840_1_) throws IOException {
		p_148840_1_.writeByte(field_149474_g ? 1 : 0);
	}
}
