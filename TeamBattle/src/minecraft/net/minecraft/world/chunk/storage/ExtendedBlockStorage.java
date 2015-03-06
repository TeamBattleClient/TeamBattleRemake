package net.minecraft.world.chunk.storage;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.chunk.NibbleArray;

public class ExtendedBlockStorage {
	/** The NibbleArray containing a block of Block-light data. */
	private NibbleArray blocklightArray;

	/**
	 * Contains the least significant 8 bits of each block ID belonging to this
	 * block storage's parent Chunk.
	 */
	private byte[] blockLSBArray;

	/**
	 * Stores the metadata associated with blocks in this ExtendedBlockStorage.
	 */
	private NibbleArray blockMetadataArray;

	/**
	 * Contains the most significant 4 bits of each block ID belonging to this
	 * block storage's parent Chunk.
	 */
	private NibbleArray blockMSBArray;

	/**
	 * A total count of the number of non-air blocks in this block storage's
	 * Chunk.
	 */
	private int blockRefCount;

	/** The NibbleArray containing a block of Sky-light data. */
	private NibbleArray skylightArray;

	/**
	 * Contains the number of blocks in this block storage's parent chunk that
	 * require random ticking. Used to cull the Chunk from random tick updates
	 * for performance reasons.
	 */
	private int tickRefCount;

	/**
	 * Contains the bottom-most Y block represented by this
	 * ExtendedBlockStorage. Typically a multiple of 16.
	 */
	private final int yBase;

	public ExtendedBlockStorage(int p_i1997_1_, boolean p_i1997_2_) {
		yBase = p_i1997_1_;
		blockLSBArray = new byte[4096];
		blockMetadataArray = new NibbleArray(blockLSBArray.length, 4);
		blocklightArray = new NibbleArray(blockLSBArray.length, 4);

		if (p_i1997_2_) {
			skylightArray = new NibbleArray(blockLSBArray.length, 4);
		}
	}

	public void clearMSBArray() {
		blockMSBArray = null;
	}

	/**
	 * Called by a Chunk to initialize the MSB array if getBlockMSBArray returns
	 * null. Returns the newly-created NibbleArray instance.
	 */
	public NibbleArray createBlockMSBArray() {
		blockMSBArray = new NibbleArray(blockLSBArray.length, 4);
		return blockMSBArray;
	}

	public void func_150818_a(int p_150818_1_, int p_150818_2_,
			int p_150818_3_, Block p_150818_4_) {
		int var5 = blockLSBArray[p_150818_2_ << 8 | p_150818_3_ << 4
				| p_150818_1_] & 255;

		if (blockMSBArray != null) {
			var5 |= blockMSBArray.get(p_150818_1_, p_150818_2_, p_150818_3_) << 8;
		}

		final Block var6 = Block.getBlockById(var5);

		if (var6 != Blocks.air) {
			--blockRefCount;

			if (var6.getTickRandomly()) {
				--tickRefCount;
			}
		}

		if (p_150818_4_ != Blocks.air) {
			++blockRefCount;

			if (p_150818_4_.getTickRandomly()) {
				++tickRefCount;
			}
		}

		final int var7 = Block.getIdFromBlock(p_150818_4_);
		blockLSBArray[p_150818_2_ << 8 | p_150818_3_ << 4 | p_150818_1_] = (byte) (var7 & 255);

		if (var7 > 255) {
			if (blockMSBArray == null) {
				blockMSBArray = new NibbleArray(blockLSBArray.length, 4);
			}

			blockMSBArray.set(p_150818_1_, p_150818_2_, p_150818_3_,
					(var7 & 3840) >> 8);
		} else if (blockMSBArray != null) {
			blockMSBArray.set(p_150818_1_, p_150818_2_, p_150818_3_, 0);
		}
	}

	public Block func_150819_a(int p_150819_1_, int p_150819_2_, int p_150819_3_) {
		int var4 = blockLSBArray[p_150819_2_ << 8 | p_150819_3_ << 4
				| p_150819_1_] & 255;

		if (blockMSBArray != null) {
			var4 |= blockMSBArray.get(p_150819_1_, p_150819_2_, p_150819_3_) << 8;
		}

		return Block.getBlockById(var4);
	}

	/**
	 * Returns the NibbleArray instance containing Block-light data.
	 */
	public NibbleArray getBlocklightArray() {
		return blocklightArray;
	}

	public byte[] getBlockLSBArray() {
		return blockLSBArray;
	}

	/**
	 * Returns the block ID MSB (bits 11..8) array for this storage array's
	 * Chunk.
	 */
	public NibbleArray getBlockMSBArray() {
		return blockMSBArray;
	}

	/**
	 * Gets the saved Block-light value in the extended block storage structure.
	 */
	public int getExtBlocklightValue(int p_76674_1_, int p_76674_2_,
			int p_76674_3_) {
		return blocklightArray.get(p_76674_1_, p_76674_2_, p_76674_3_);
	}

	/**
	 * Returns the metadata associated with the block at the given coordinates
	 * in this ExtendedBlockStorage.
	 */
	public int getExtBlockMetadata(int p_76665_1_, int p_76665_2_,
			int p_76665_3_) {
		return blockMetadataArray.get(p_76665_1_, p_76665_2_, p_76665_3_);
	}

	/**
	 * Gets the saved Sky-light value in the extended block storage structure.
	 */
	public int getExtSkylightValue(int p_76670_1_, int p_76670_2_,
			int p_76670_3_) {
		return skylightArray.get(p_76670_1_, p_76670_2_, p_76670_3_);
	}

	public NibbleArray getMetadataArray() {
		return blockMetadataArray;
	}

	/**
	 * Returns whether or not this block storage's Chunk will require random
	 * ticking, used to avoid looping through random block ticks when there are
	 * no blocks that would randomly tick.
	 */
	public boolean getNeedsRandomTick() {
		return tickRefCount > 0;
	}

	/**
	 * Returns the NibbleArray instance containing Sky-light data.
	 */
	public NibbleArray getSkylightArray() {
		return skylightArray;
	}

	/**
	 * Returns the Y location of this ExtendedBlockStorage.
	 */
	public int getYLocation() {
		return yBase;
	}

	/**
	 * Returns whether or not this block storage's Chunk is fully empty, based
	 * on its internal reference count.
	 */
	public boolean isEmpty() {
		return blockRefCount == 0;
	}

	public void removeInvalidBlocks() {
		blockRefCount = 0;
		tickRefCount = 0;

		for (int var1 = 0; var1 < 16; ++var1) {
			for (int var2 = 0; var2 < 16; ++var2) {
				for (int var3 = 0; var3 < 16; ++var3) {
					final Block var4 = func_150819_a(var1, var2, var3);

					if (var4 != Blocks.air) {
						++blockRefCount;

						if (var4.getTickRandomly()) {
							++tickRefCount;
						}
					}
				}
			}
		}
	}

	/**
	 * Sets the NibbleArray instance used for Block-light values in this
	 * particular storage block.
	 */
	public void setBlocklightArray(NibbleArray p_76659_1_) {
		blocklightArray = p_76659_1_;
	}

	/**
	 * Sets the array of block ID least significant bits for this
	 * ExtendedBlockStorage.
	 */
	public void setBlockLSBArray(byte[] p_76664_1_) {
		blockLSBArray = p_76664_1_;
	}

	/**
	 * Sets the NibbleArray of block metadata (blockMetadataArray) for this
	 * ExtendedBlockStorage.
	 */
	public void setBlockMetadataArray(NibbleArray p_76668_1_) {
		blockMetadataArray = p_76668_1_;
	}

	/**
	 * Sets the array of blockID most significant bits (blockMSBArray) for this
	 * ExtendedBlockStorage.
	 */
	public void setBlockMSBArray(NibbleArray p_76673_1_) {
		blockMSBArray = p_76673_1_;
	}

	/**
	 * Sets the saved Block-light value in the extended block storage structure.
	 */
	public void setExtBlocklightValue(int p_76677_1_, int p_76677_2_,
			int p_76677_3_, int p_76677_4_) {
		blocklightArray.set(p_76677_1_, p_76677_2_, p_76677_3_, p_76677_4_);
	}

	/**
	 * Sets the metadata of the Block at the given coordinates in this
	 * ExtendedBlockStorage to the given metadata.
	 */
	public void setExtBlockMetadata(int p_76654_1_, int p_76654_2_,
			int p_76654_3_, int p_76654_4_) {
		blockMetadataArray.set(p_76654_1_, p_76654_2_, p_76654_3_, p_76654_4_);
	}

	/**
	 * Sets the saved Sky-light value in the extended block storage structure.
	 */
	public void setExtSkylightValue(int p_76657_1_, int p_76657_2_,
			int p_76657_3_, int p_76657_4_) {
		skylightArray.set(p_76657_1_, p_76657_2_, p_76657_3_, p_76657_4_);
	}

	/**
	 * Sets the NibbleArray instance used for Sky-light values in this
	 * particular storage block.
	 */
	public void setSkylightArray(NibbleArray p_76666_1_) {
		skylightArray = p_76666_1_;
	}
}
