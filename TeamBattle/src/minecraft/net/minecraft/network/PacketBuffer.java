package net.minecraft.network;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBufProcessor;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.GatheringByteChannel;
import java.nio.channels.ScatteringByteChannel;
import java.nio.charset.Charset;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTSizeTracker;
import net.minecraft.nbt.NBTTagCompound;

import com.google.common.base.Charsets;

public class PacketBuffer extends ByteBuf {
	/**
	 * Calculates the number of bytes required to fit the supplied int (0-5) if
	 * it were to be read/written using readVarIntFromBuffer or
	 * writeVarIntToBuffer
	 */
	public static int getVarIntSize(int p_150790_0_) {
		return (p_150790_0_ & -128) == 0 ? 1 : (p_150790_0_ & -16384) == 0 ? 2
				: (p_150790_0_ & -2097152) == 0 ? 3
						: (p_150790_0_ & -268435456) == 0 ? 4 : 5;
	}

	private final ByteBuf field_150794_a;

	public PacketBuffer(ByteBuf p_i45154_1_) {
		field_150794_a = p_i45154_1_;
	}

	@Override
	public ByteBufAllocator alloc() {
		return field_150794_a.alloc();
	}

	@Override
	public byte[] array() {
		return field_150794_a.array();
	}

	@Override
	public int arrayOffset() {
		return field_150794_a.arrayOffset();
	}

	@Override
	public int bytesBefore(byte p_bytesBefore_1_) {
		return field_150794_a.bytesBefore(p_bytesBefore_1_);
	}

	@Override
	public int bytesBefore(int p_bytesBefore_1_, byte p_bytesBefore_2_) {
		return field_150794_a.bytesBefore(p_bytesBefore_1_, p_bytesBefore_2_);
	}

	@Override
	public int bytesBefore(int p_bytesBefore_1_, int p_bytesBefore_2_,
			byte p_bytesBefore_3_) {
		return field_150794_a.bytesBefore(p_bytesBefore_1_, p_bytesBefore_2_,
				p_bytesBefore_3_);
	}

	@Override
	public int capacity() {
		return field_150794_a.capacity();
	}

	@Override
	public ByteBuf capacity(int p_capacity_1_) {
		return field_150794_a.capacity(p_capacity_1_);
	}

	@Override
	public ByteBuf clear() {
		return field_150794_a.clear();
	}

	@Override
	public int compareTo(ByteBuf p_compareTo_1_) {
		return field_150794_a.compareTo(p_compareTo_1_);
	}

	@Override
	public ByteBuf copy() {
		return field_150794_a.copy();
	}

	@Override
	public ByteBuf copy(int p_copy_1_, int p_copy_2_) {
		return field_150794_a.copy(p_copy_1_, p_copy_2_);
	}

	@Override
	public ByteBuf discardReadBytes() {
		return field_150794_a.discardReadBytes();
	}

	@Override
	public ByteBuf discardSomeReadBytes() {
		return field_150794_a.discardSomeReadBytes();
	}

	@Override
	public ByteBuf duplicate() {
		return field_150794_a.duplicate();
	}

	@Override
	public ByteBuf ensureWritable(int p_ensureWritable_1_) {
		return field_150794_a.ensureWritable(p_ensureWritable_1_);
	}

	@Override
	public int ensureWritable(int p_ensureWritable_1_,
			boolean p_ensureWritable_2_) {
		return field_150794_a.ensureWritable(p_ensureWritable_1_,
				p_ensureWritable_2_);
	}

	@Override
	public boolean equals(Object p_equals_1_) {
		return field_150794_a.equals(p_equals_1_);
	}

	@Override
	public int forEachByte(ByteBufProcessor p_forEachByte_1_) {
		return field_150794_a.forEachByte(p_forEachByte_1_);
	}

	@Override
	public int forEachByte(int p_forEachByte_1_, int p_forEachByte_2_,
			ByteBufProcessor p_forEachByte_3_) {
		return field_150794_a.forEachByte(p_forEachByte_1_, p_forEachByte_2_,
				p_forEachByte_3_);
	}

	@Override
	public int forEachByteDesc(ByteBufProcessor p_forEachByteDesc_1_) {
		return field_150794_a.forEachByteDesc(p_forEachByteDesc_1_);
	}

	@Override
	public int forEachByteDesc(int p_forEachByteDesc_1_,
			int p_forEachByteDesc_2_, ByteBufProcessor p_forEachByteDesc_3_) {
		return field_150794_a.forEachByteDesc(p_forEachByteDesc_1_,
				p_forEachByteDesc_2_, p_forEachByteDesc_3_);
	}

	@Override
	public boolean getBoolean(int p_getBoolean_1_) {
		return field_150794_a.getBoolean(p_getBoolean_1_);
	}

	@Override
	public byte getByte(int p_getByte_1_) {
		return field_150794_a.getByte(p_getByte_1_);
	}

	@Override
	public ByteBuf getBytes(int p_getBytes_1_, byte[] p_getBytes_2_) {
		return field_150794_a.getBytes(p_getBytes_1_, p_getBytes_2_);
	}

	@Override
	public ByteBuf getBytes(int p_getBytes_1_, byte[] p_getBytes_2_,
			int p_getBytes_3_, int p_getBytes_4_) {
		return field_150794_a.getBytes(p_getBytes_1_, p_getBytes_2_,
				p_getBytes_3_, p_getBytes_4_);
	}

	@Override
	public ByteBuf getBytes(int p_getBytes_1_, ByteBuf p_getBytes_2_) {
		return field_150794_a.getBytes(p_getBytes_1_, p_getBytes_2_);
	}

	@Override
	public ByteBuf getBytes(int p_getBytes_1_, ByteBuf p_getBytes_2_,
			int p_getBytes_3_) {
		return field_150794_a.getBytes(p_getBytes_1_, p_getBytes_2_,
				p_getBytes_3_);
	}

	@Override
	public ByteBuf getBytes(int p_getBytes_1_, ByteBuf p_getBytes_2_,
			int p_getBytes_3_, int p_getBytes_4_) {
		return field_150794_a.getBytes(p_getBytes_1_, p_getBytes_2_,
				p_getBytes_3_, p_getBytes_4_);
	}

	@Override
	public ByteBuf getBytes(int p_getBytes_1_, ByteBuffer p_getBytes_2_) {
		return field_150794_a.getBytes(p_getBytes_1_, p_getBytes_2_);
	}

	@Override
	public int getBytes(int p_getBytes_1_, GatheringByteChannel p_getBytes_2_,
			int p_getBytes_3_) throws IOException {
		return field_150794_a.getBytes(p_getBytes_1_, p_getBytes_2_,
				p_getBytes_3_);
	}

	@Override
	public ByteBuf getBytes(int p_getBytes_1_, OutputStream p_getBytes_2_,
			int p_getBytes_3_) throws IOException {
		return field_150794_a.getBytes(p_getBytes_1_, p_getBytes_2_,
				p_getBytes_3_);
	}

	@Override
	public char getChar(int p_getChar_1_) {
		return field_150794_a.getChar(p_getChar_1_);
	}

	@Override
	public double getDouble(int p_getDouble_1_) {
		return field_150794_a.getDouble(p_getDouble_1_);
	}

	@Override
	public float getFloat(int p_getFloat_1_) {
		return field_150794_a.getFloat(p_getFloat_1_);
	}

	@Override
	public int getInt(int p_getInt_1_) {
		return field_150794_a.getInt(p_getInt_1_);
	}

	@Override
	public long getLong(int p_getLong_1_) {
		return field_150794_a.getLong(p_getLong_1_);
	}

	@Override
	public int getMedium(int p_getMedium_1_) {
		return field_150794_a.getMedium(p_getMedium_1_);
	}

	@Override
	public short getShort(int p_getShort_1_) {
		return field_150794_a.getShort(p_getShort_1_);
	}

	@Override
	public short getUnsignedByte(int p_getUnsignedByte_1_) {
		return field_150794_a.getUnsignedByte(p_getUnsignedByte_1_);
	}

	@Override
	public long getUnsignedInt(int p_getUnsignedInt_1_) {
		return field_150794_a.getUnsignedInt(p_getUnsignedInt_1_);
	}

	@Override
	public int getUnsignedMedium(int p_getUnsignedMedium_1_) {
		return field_150794_a.getUnsignedMedium(p_getUnsignedMedium_1_);
	}

	@Override
	public int getUnsignedShort(int p_getUnsignedShort_1_) {
		return field_150794_a.getUnsignedShort(p_getUnsignedShort_1_);
	}

	@Override
	public boolean hasArray() {
		return field_150794_a.hasArray();
	}

	@Override
	public int hashCode() {
		return field_150794_a.hashCode();
	}

	@Override
	public boolean hasMemoryAddress() {
		return field_150794_a.hasMemoryAddress();
	}

	@Override
	public int indexOf(int p_indexOf_1_, int p_indexOf_2_, byte p_indexOf_3_) {
		return field_150794_a.indexOf(p_indexOf_1_, p_indexOf_2_, p_indexOf_3_);
	}

	@Override
	public ByteBuffer internalNioBuffer(int p_internalNioBuffer_1_,
			int p_internalNioBuffer_2_) {
		return field_150794_a.internalNioBuffer(p_internalNioBuffer_1_,
				p_internalNioBuffer_2_);
	}

	@Override
	public boolean isDirect() {
		return field_150794_a.isDirect();
	}

	@Override
	public boolean isReadable() {
		return field_150794_a.isReadable();
	}

	@Override
	public boolean isReadable(int p_isReadable_1_) {
		return field_150794_a.isReadable(p_isReadable_1_);
	}

	@Override
	public boolean isWritable() {
		return field_150794_a.isWritable();
	}

	@Override
	public boolean isWritable(int p_isWritable_1_) {
		return field_150794_a.isWritable(p_isWritable_1_);
	}

	@Override
	public ByteBuf markReaderIndex() {
		return field_150794_a.markReaderIndex();
	}

	@Override
	public ByteBuf markWriterIndex() {
		return field_150794_a.markWriterIndex();
	}

	@Override
	public int maxCapacity() {
		return field_150794_a.maxCapacity();
	}

	@Override
	public int maxWritableBytes() {
		return field_150794_a.maxWritableBytes();
	}

	@Override
	public long memoryAddress() {
		return field_150794_a.memoryAddress();
	}

	@Override
	public ByteBuffer nioBuffer() {
		return field_150794_a.nioBuffer();
	}

	@Override
	public ByteBuffer nioBuffer(int p_nioBuffer_1_, int p_nioBuffer_2_) {
		return field_150794_a.nioBuffer(p_nioBuffer_1_, p_nioBuffer_2_);
	}

	@Override
	public int nioBufferCount() {
		return field_150794_a.nioBufferCount();
	}

	@Override
	public ByteBuffer[] nioBuffers() {
		return field_150794_a.nioBuffers();
	}

	@Override
	public ByteBuffer[] nioBuffers(int p_nioBuffers_1_, int p_nioBuffers_2_) {
		return field_150794_a.nioBuffers(p_nioBuffers_1_, p_nioBuffers_2_);
	}

	@Override
	public ByteOrder order() {
		return field_150794_a.order();
	}

	@Override
	public ByteBuf order(ByteOrder p_order_1_) {
		return field_150794_a.order(p_order_1_);
	}

	@Override
	public int readableBytes() {
		return field_150794_a.readableBytes();
	}

	@Override
	public boolean readBoolean() {
		return field_150794_a.readBoolean();
	}

	@Override
	public byte readByte() {
		return field_150794_a.readByte();
	}

	@Override
	public ByteBuf readBytes(byte[] p_readBytes_1_) {
		return field_150794_a.readBytes(p_readBytes_1_);
	}

	@Override
	public ByteBuf readBytes(byte[] p_readBytes_1_, int p_readBytes_2_,
			int p_readBytes_3_) {
		return field_150794_a.readBytes(p_readBytes_1_, p_readBytes_2_,
				p_readBytes_3_);
	}

	@Override
	public ByteBuf readBytes(ByteBuf p_readBytes_1_) {
		return field_150794_a.readBytes(p_readBytes_1_);
	}

	@Override
	public ByteBuf readBytes(ByteBuf p_readBytes_1_, int p_readBytes_2_) {
		return field_150794_a.readBytes(p_readBytes_1_, p_readBytes_2_);
	}

	@Override
	public ByteBuf readBytes(ByteBuf p_readBytes_1_, int p_readBytes_2_,
			int p_readBytes_3_) {
		return field_150794_a.readBytes(p_readBytes_1_, p_readBytes_2_,
				p_readBytes_3_);
	}

	@Override
	public ByteBuf readBytes(ByteBuffer p_readBytes_1_) {
		return field_150794_a.readBytes(p_readBytes_1_);
	}

	@Override
	public int readBytes(GatheringByteChannel p_readBytes_1_, int p_readBytes_2_)
			throws IOException {
		return field_150794_a.readBytes(p_readBytes_1_, p_readBytes_2_);
	}

	@Override
	public ByteBuf readBytes(int p_readBytes_1_) {
		return field_150794_a.readBytes(p_readBytes_1_);
	}

	@Override
	public ByteBuf readBytes(OutputStream p_readBytes_1_, int p_readBytes_2_)
			throws IOException {
		return field_150794_a.readBytes(p_readBytes_1_, p_readBytes_2_);
	}

	@Override
	public char readChar() {
		return field_150794_a.readChar();
	}

	@Override
	public double readDouble() {
		return field_150794_a.readDouble();
	}

	@Override
	public int readerIndex() {
		return field_150794_a.readerIndex();
	}

	@Override
	public ByteBuf readerIndex(int p_readerIndex_1_) {
		return field_150794_a.readerIndex(p_readerIndex_1_);
	}

	@Override
	public float readFloat() {
		return field_150794_a.readFloat();
	}

	@Override
	public int readInt() {
		return field_150794_a.readInt();
	}

	/**
	 * Reads an ItemStack from this buffer
	 */
	public ItemStack readItemStackFromBuffer() throws IOException {
		ItemStack var1 = null;
		final short var2 = readShort();

		if (var2 >= 0) {
			final byte var3 = readByte();
			final short var4 = readShort();
			var1 = new ItemStack(Item.getItemById(var2), var3, var4);
			var1.stackTagCompound = readNBTTagCompoundFromBuffer();
		}

		return var1;
	}

	@Override
	public long readLong() {
		return field_150794_a.readLong();
	}

	@Override
	public int readMedium() {
		return field_150794_a.readMedium();
	}

	/**
	 * Reads a compressed NBTTagCompound from this buffer
	 */
	public NBTTagCompound readNBTTagCompoundFromBuffer() throws IOException {
		final short var1 = readShort();

		if (var1 < 0)
			return null;
		else {
			final byte[] var2 = new byte[var1];
			this.readBytes(var2);
			return CompressedStreamTools.func_152457_a(var2,
					new NBTSizeTracker(2097152L));
		}
	}

	@Override
	public short readShort() {
		return field_150794_a.readShort();
	}

	@Override
	public ByteBuf readSlice(int p_readSlice_1_) {
		return field_150794_a.readSlice(p_readSlice_1_);
	}

	/**
	 * Reads a string from this buffer. Expected parameter is maximum allowed
	 * string length. Will throw IOException if string length exceeds this
	 * value!
	 */
	public String readStringFromBuffer(int p_150789_1_) throws IOException {
		final int var2 = readVarIntFromBuffer();

		if (var2 > p_150789_1_ * 4)
			throw new IOException(
					"The received encoded string buffer length is longer than maximum allowed ("
							+ var2 + " > " + p_150789_1_ * 4 + ")");
		else if (var2 < 0)
			throw new IOException(
					"The received encoded string buffer length is less than zero! Weird string!");
		else {
			final String var3 = new String(this.readBytes(var2).array(),
					Charsets.UTF_8);

			if (var3.length() > p_150789_1_)
				throw new IOException(
						"The received string length is longer than maximum allowed ("
								+ var2 + " > " + p_150789_1_ + ")");
			else
				return var3;
		}
	}

	@Override
	public short readUnsignedByte() {
		return field_150794_a.readUnsignedByte();
	}

	@Override
	public long readUnsignedInt() {
		return field_150794_a.readUnsignedInt();
	}

	@Override
	public int readUnsignedMedium() {
		return field_150794_a.readUnsignedMedium();
	}

	@Override
	public int readUnsignedShort() {
		return field_150794_a.readUnsignedShort();
	}

	/**
	 * Reads a compressed int from the buffer. To do so it maximally reads 5
	 * byte-sized chunks whose most significant bit dictates whether another
	 * byte should be read.
	 */
	public int readVarIntFromBuffer() {
		int var1 = 0;
		int var2 = 0;
		byte var3;

		do {
			var3 = readByte();
			var1 |= (var3 & 127) << var2++ * 7;

			if (var2 > 5)
				throw new RuntimeException("VarInt too big");
		} while ((var3 & 128) == 128);

		return var1;
	}

	@Override
	public int refCnt() {
		return field_150794_a.refCnt();
	}

	@Override
	public boolean release() {
		return field_150794_a.release();
	}

	@Override
	public boolean release(int p_release_1_) {
		return field_150794_a.release(p_release_1_);
	}

	@Override
	public ByteBuf resetReaderIndex() {
		return field_150794_a.resetReaderIndex();
	}

	@Override
	public ByteBuf resetWriterIndex() {
		return field_150794_a.resetWriterIndex();
	}

	@Override
	public ByteBuf retain() {
		return field_150794_a.retain();
	}

	@Override
	public ByteBuf retain(int p_retain_1_) {
		return field_150794_a.retain(p_retain_1_);
	}

	@Override
	public ByteBuf setBoolean(int p_setBoolean_1_, boolean p_setBoolean_2_) {
		return field_150794_a.setBoolean(p_setBoolean_1_, p_setBoolean_2_);
	}

	@Override
	public ByteBuf setByte(int p_setByte_1_, int p_setByte_2_) {
		return field_150794_a.setByte(p_setByte_1_, p_setByte_2_);
	}

	@Override
	public ByteBuf setBytes(int p_setBytes_1_, byte[] p_setBytes_2_) {
		return field_150794_a.setBytes(p_setBytes_1_, p_setBytes_2_);
	}

	@Override
	public ByteBuf setBytes(int p_setBytes_1_, byte[] p_setBytes_2_,
			int p_setBytes_3_, int p_setBytes_4_) {
		return field_150794_a.setBytes(p_setBytes_1_, p_setBytes_2_,
				p_setBytes_3_, p_setBytes_4_);
	}

	@Override
	public ByteBuf setBytes(int p_setBytes_1_, ByteBuf p_setBytes_2_) {
		return field_150794_a.setBytes(p_setBytes_1_, p_setBytes_2_);
	}

	@Override
	public ByteBuf setBytes(int p_setBytes_1_, ByteBuf p_setBytes_2_,
			int p_setBytes_3_) {
		return field_150794_a.setBytes(p_setBytes_1_, p_setBytes_2_,
				p_setBytes_3_);
	}

	@Override
	public ByteBuf setBytes(int p_setBytes_1_, ByteBuf p_setBytes_2_,
			int p_setBytes_3_, int p_setBytes_4_) {
		return field_150794_a.setBytes(p_setBytes_1_, p_setBytes_2_,
				p_setBytes_3_, p_setBytes_4_);
	}

	@Override
	public ByteBuf setBytes(int p_setBytes_1_, ByteBuffer p_setBytes_2_) {
		return field_150794_a.setBytes(p_setBytes_1_, p_setBytes_2_);
	}

	@Override
	public int setBytes(int p_setBytes_1_, InputStream p_setBytes_2_,
			int p_setBytes_3_) throws IOException {
		return field_150794_a.setBytes(p_setBytes_1_, p_setBytes_2_,
				p_setBytes_3_);
	}

	@Override
	public int setBytes(int p_setBytes_1_, ScatteringByteChannel p_setBytes_2_,
			int p_setBytes_3_) throws IOException {
		return field_150794_a.setBytes(p_setBytes_1_, p_setBytes_2_,
				p_setBytes_3_);
	}

	@Override
	public ByteBuf setChar(int p_setChar_1_, int p_setChar_2_) {
		return field_150794_a.setChar(p_setChar_1_, p_setChar_2_);
	}

	@Override
	public ByteBuf setDouble(int p_setDouble_1_, double p_setDouble_2_) {
		return field_150794_a.setDouble(p_setDouble_1_, p_setDouble_2_);
	}

	@Override
	public ByteBuf setFloat(int p_setFloat_1_, float p_setFloat_2_) {
		return field_150794_a.setFloat(p_setFloat_1_, p_setFloat_2_);
	}

	@Override
	public ByteBuf setIndex(int p_setIndex_1_, int p_setIndex_2_) {
		return field_150794_a.setIndex(p_setIndex_1_, p_setIndex_2_);
	}

	@Override
	public ByteBuf setInt(int p_setInt_1_, int p_setInt_2_) {
		return field_150794_a.setInt(p_setInt_1_, p_setInt_2_);
	}

	@Override
	public ByteBuf setLong(int p_setLong_1_, long p_setLong_2_) {
		return field_150794_a.setLong(p_setLong_1_, p_setLong_2_);
	}

	@Override
	public ByteBuf setMedium(int p_setMedium_1_, int p_setMedium_2_) {
		return field_150794_a.setMedium(p_setMedium_1_, p_setMedium_2_);
	}

	@Override
	public ByteBuf setShort(int p_setShort_1_, int p_setShort_2_) {
		return field_150794_a.setShort(p_setShort_1_, p_setShort_2_);
	}

	@Override
	public ByteBuf setZero(int p_setZero_1_, int p_setZero_2_) {
		return field_150794_a.setZero(p_setZero_1_, p_setZero_2_);
	}

	@Override
	public ByteBuf skipBytes(int p_skipBytes_1_) {
		return field_150794_a.skipBytes(p_skipBytes_1_);
	}

	@Override
	public ByteBuf slice() {
		return field_150794_a.slice();
	}

	@Override
	public ByteBuf slice(int p_slice_1_, int p_slice_2_) {
		return field_150794_a.slice(p_slice_1_, p_slice_2_);
	}

	@Override
	public String toString() {
		return field_150794_a.toString();
	}

	@Override
	public String toString(Charset p_toString_1_) {
		return field_150794_a.toString(p_toString_1_);
	}

	@Override
	public String toString(int p_toString_1_, int p_toString_2_,
			Charset p_toString_3_) {
		return field_150794_a.toString(p_toString_1_, p_toString_2_,
				p_toString_3_);
	}

	@Override
	public ByteBuf unwrap() {
		return field_150794_a.unwrap();
	}

	@Override
	public int writableBytes() {
		return field_150794_a.writableBytes();
	}

	@Override
	public ByteBuf writeBoolean(boolean p_writeBoolean_1_) {
		return field_150794_a.writeBoolean(p_writeBoolean_1_);
	}

	@Override
	public ByteBuf writeByte(int p_writeByte_1_) {
		return field_150794_a.writeByte(p_writeByte_1_);
	}

	@Override
	public ByteBuf writeBytes(byte[] p_writeBytes_1_) {
		return field_150794_a.writeBytes(p_writeBytes_1_);
	}

	@Override
	public ByteBuf writeBytes(byte[] p_writeBytes_1_, int p_writeBytes_2_,
			int p_writeBytes_3_) {
		return field_150794_a.writeBytes(p_writeBytes_1_, p_writeBytes_2_,
				p_writeBytes_3_);
	}

	@Override
	public ByteBuf writeBytes(ByteBuf p_writeBytes_1_) {
		return field_150794_a.writeBytes(p_writeBytes_1_);
	}

	@Override
	public ByteBuf writeBytes(ByteBuf p_writeBytes_1_, int p_writeBytes_2_) {
		return field_150794_a.writeBytes(p_writeBytes_1_, p_writeBytes_2_);
	}

	@Override
	public ByteBuf writeBytes(ByteBuf p_writeBytes_1_, int p_writeBytes_2_,
			int p_writeBytes_3_) {
		return field_150794_a.writeBytes(p_writeBytes_1_, p_writeBytes_2_,
				p_writeBytes_3_);
	}

	@Override
	public ByteBuf writeBytes(ByteBuffer p_writeBytes_1_) {
		return field_150794_a.writeBytes(p_writeBytes_1_);
	}

	@Override
	public int writeBytes(InputStream p_writeBytes_1_, int p_writeBytes_2_)
			throws IOException {
		return field_150794_a.writeBytes(p_writeBytes_1_, p_writeBytes_2_);
	}

	@Override
	public int writeBytes(ScatteringByteChannel p_writeBytes_1_,
			int p_writeBytes_2_) throws IOException {
		return field_150794_a.writeBytes(p_writeBytes_1_, p_writeBytes_2_);
	}

	@Override
	public ByteBuf writeChar(int p_writeChar_1_) {
		return field_150794_a.writeChar(p_writeChar_1_);
	}

	@Override
	public ByteBuf writeDouble(double p_writeDouble_1_) {
		return field_150794_a.writeDouble(p_writeDouble_1_);
	}

	@Override
	public ByteBuf writeFloat(float p_writeFloat_1_) {
		return field_150794_a.writeFloat(p_writeFloat_1_);
	}

	@Override
	public ByteBuf writeInt(int p_writeInt_1_) {
		return field_150794_a.writeInt(p_writeInt_1_);
	}

	/**
	 * Writes the ItemStack's ID (short), then size (byte), then damage. (short)
	 */
	public void writeItemStackToBuffer(ItemStack p_150788_1_)
			throws IOException {
		if (p_150788_1_ == null) {
			writeShort(-1);
		} else {
			writeShort(Item.getIdFromItem(p_150788_1_.getItem()));
			writeByte(p_150788_1_.stackSize);
			writeShort(p_150788_1_.getItemDamage());
			NBTTagCompound var2 = null;

			if (p_150788_1_.getItem().isDamageable()
					|| p_150788_1_.getItem().getShareTag()) {
				var2 = p_150788_1_.stackTagCompound;
			}

			writeNBTTagCompoundToBuffer(var2);
		}
	}

	@Override
	public ByteBuf writeLong(long p_writeLong_1_) {
		return field_150794_a.writeLong(p_writeLong_1_);
	}

	@Override
	public ByteBuf writeMedium(int p_writeMedium_1_) {
		return field_150794_a.writeMedium(p_writeMedium_1_);
	}

	/**
	 * Writes a compressed NBTTagCompound to this buffer
	 */
	public void writeNBTTagCompoundToBuffer(NBTTagCompound p_150786_1_)
			throws IOException {
		if (p_150786_1_ == null) {
			writeShort(-1);
		} else {
			final byte[] var2 = CompressedStreamTools.compress(p_150786_1_);
			writeShort((short) var2.length);
			this.writeBytes(var2);
		}
	}

	@Override
	public int writerIndex() {
		return field_150794_a.writerIndex();
	}

	@Override
	public ByteBuf writerIndex(int p_writerIndex_1_) {
		return field_150794_a.writerIndex(p_writerIndex_1_);
	}

	@Override
	public ByteBuf writeShort(int p_writeShort_1_) {
		return field_150794_a.writeShort(p_writeShort_1_);
	}

	/**
	 * Writes a (UTF-8 encoded) String to this buffer. Will throw IOException if
	 * String length exceeds 32767 bytes
	 */
	public void writeStringToBuffer(String p_150785_1_) throws IOException {
		final byte[] var2 = p_150785_1_.getBytes(Charsets.UTF_8);

		if (var2.length > 32767)
			throw new IOException("String too big (was " + p_150785_1_.length()
					+ " bytes encoded, max " + 32767 + ")");
		else {
			writeVarIntToBuffer(var2.length);
			this.writeBytes(var2);
		}
	}

	/**
	 * Writes a compressed int to the buffer. The smallest number of bytes to
	 * fit the passed int will be written. Of each such byte only 7 bits will be
	 * used to describe the actual value since its most significant bit dictates
	 * whether the next byte is part of that same int. Micro-optimization for
	 * int values that are expected to have values below 128.
	 */
	public void writeVarIntToBuffer(int p_150787_1_) {
		while ((p_150787_1_ & -128) != 0) {
			writeByte(p_150787_1_ & 127 | 128);
			p_150787_1_ >>>= 7;
		}

		writeByte(p_150787_1_);
	}

	@Override
	public ByteBuf writeZero(int p_writeZero_1_) {
		return field_150794_a.writeZero(p_writeZero_1_);
	}
}
