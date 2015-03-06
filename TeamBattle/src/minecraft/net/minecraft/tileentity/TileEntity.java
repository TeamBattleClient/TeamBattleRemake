package net.minecraft.tileentity;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockJukebox;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.world.World;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TileEntity {
	/**
	 * A HashMap storing the classes and mapping to the string names (reverse of
	 * nameToClassMap).
	 */
	private static Map classToNameMap = new HashMap();

	private static final Logger logger = LogManager.getLogger();

	/**
	 * A HashMap storing string names of classes mapping to the actual
	 * java.lang.Class type.
	 */
	private static Map nameToClassMap = new HashMap();

	static {
		func_145826_a(TileEntityFurnace.class, "Furnace");
		func_145826_a(TileEntityChest.class, "Chest");
		func_145826_a(TileEntityEnderChest.class, "EnderChest");
		func_145826_a(BlockJukebox.TileEntityJukebox.class, "RecordPlayer");
		func_145826_a(TileEntityDispenser.class, "Trap");
		func_145826_a(TileEntityDropper.class, "Dropper");
		func_145826_a(TileEntitySign.class, "Sign");
		func_145826_a(TileEntityMobSpawner.class, "MobSpawner");
		func_145826_a(TileEntityNote.class, "Music");
		func_145826_a(TileEntityPiston.class, "Piston");
		func_145826_a(TileEntityBrewingStand.class, "Cauldron");
		func_145826_a(TileEntityEnchantmentTable.class, "EnchantTable");
		func_145826_a(TileEntityEndPortal.class, "Airportal");
		func_145826_a(TileEntityCommandBlock.class, "Control");
		func_145826_a(TileEntityBeacon.class, "Beacon");
		func_145826_a(TileEntitySkull.class, "Skull");
		func_145826_a(TileEntityDaylightDetector.class, "DLDetector");
		func_145826_a(TileEntityHopper.class, "Hopper");
		func_145826_a(TileEntityComparator.class, "Comparator");
		func_145826_a(TileEntityFlowerPot.class, "FlowerPot");
	}

	/**
	 * Creates a new entity and loads its data from the specified NBT.
	 */
	public static TileEntity createAndLoadEntity(NBTTagCompound p_145827_0_) {
		TileEntity var1 = null;

		try {
			final Class var2 = (Class) nameToClassMap.get(p_145827_0_
					.getString("id"));

			if (var2 != null) {
				var1 = (TileEntity) var2.newInstance();
			}
		} catch (final Exception var3) {
			var3.printStackTrace();
		}

		if (var1 != null) {
			var1.readFromNBT(p_145827_0_);
		} else {
			logger.warn("Skipping BlockEntity with id "
					+ p_145827_0_.getString("id"));
		}

		return var1;
	}

	private static void func_145826_a(Class p_145826_0_, String p_145826_1_) {
		if (nameToClassMap.containsKey(p_145826_1_))
			throw new IllegalArgumentException("Duplicate id: " + p_145826_1_);
		else {
			nameToClassMap.put(p_145826_1_, p_145826_0_);
			classToNameMap.put(p_145826_0_, p_145826_1_);
		}
	}

	public int blockMetadata = -1;
	/** the Block type that this TileEntity is contained within */
	public Block blockType;
	public int field_145848_d;

	public int field_145849_e;

	public int field_145851_c;

	protected boolean tileEntityInvalid;

	/** the instance of the world the tile entity is in. */
	protected World worldObj;

	public void func_145828_a(CrashReportCategory p_145828_1_) {
		p_145828_1_.addCrashSectionCallable("Name", new Callable() {

			@Override
			public String call() {
				return (String) TileEntity.classToNameMap.get(TileEntity.this
						.getClass())
						+ " // "
						+ TileEntity.this.getClass().getCanonicalName();
			}
		});
		CrashReportCategory.func_147153_a(p_145828_1_, field_145851_c,
				field_145848_d, field_145849_e, getBlockType(),
				getBlockMetadata());
		p_145828_1_.addCrashSectionCallable("Actual block type",
				new Callable() {

					@Override
					public String call() {
						final int var1 = Block.getIdFromBlock(worldObj
								.getBlock(field_145851_c, field_145848_d,
										field_145849_e));

						try {
							return String.format("ID #%d (%s // %s)",
									new Object[] {
											Integer.valueOf(var1),
											Block.getBlockById(var1)
													.getUnlocalizedName(),
											Block.getBlockById(var1).getClass()
													.getCanonicalName() });
						} catch (final Throwable var3) {
							return "ID #" + var1;
						}
					}
				});
		p_145828_1_.addCrashSectionCallable("Actual block data value",
				new Callable() {

					@Override
					public String call() {
						final int var1 = worldObj.getBlockMetadata(
								field_145851_c, field_145848_d, field_145849_e);

						if (var1 < 0)
							return "Unknown? (Got " + var1 + ")";
						else {
							final String var2 = String
									.format("%4s",
											new Object[] { Integer
													.toBinaryString(var1) })
									.replace(" ", "0");
							return String
									.format("%1$d / 0x%1$X / 0b%2$s",
											new Object[] {
													Integer.valueOf(var1), var2 });
						}
					}
				});
	}

	public int getBlockMetadata() {
		if (blockMetadata == -1) {
			blockMetadata = worldObj.getBlockMetadata(field_145851_c,
					field_145848_d, field_145849_e);
		}

		return blockMetadata;
	}

	/**
	 * Gets the block type at the location of this entity (client-only).
	 */
	public Block getBlockType() {
		if (blockType == null) {
			blockType = worldObj.getBlock(field_145851_c, field_145848_d,
					field_145849_e);
		}

		return blockType;
	}

	/**
	 * Overriden in a sign to provide the text.
	 */
	public Packet getDescriptionPacket() {
		return null;
	}

	/**
	 * Returns the square of the distance between this entity and the passed in
	 * coordinates.
	 */
	public double getDistanceFrom(double p_145835_1_, double p_145835_3_,
			double p_145835_5_) {
		final double var7 = field_145851_c + 0.5D - p_145835_1_;
		final double var9 = field_145848_d + 0.5D - p_145835_3_;
		final double var11 = field_145849_e + 0.5D - p_145835_5_;
		return var7 * var7 + var9 * var9 + var11 * var11;
	}

	public double getMaxRenderDistanceSquared() {
		return 4096.0D;
	}

	/**
	 * Returns the worldObj for this tileEntity.
	 */
	public World getWorldObj() {
		return worldObj;
	}

	/**
	 * Returns true if the worldObj isn't null.
	 */
	public boolean hasWorldObj() {
		return worldObj != null;
	}

	/**
	 * invalidates a tile entity
	 */
	public void invalidate() {
		tileEntityInvalid = true;
	}

	public boolean isInvalid() {
		return tileEntityInvalid;
	}

	/**
	 * Called when an the contents of an Inventory change, usually
	 */
	public void onInventoryChanged() {
		if (worldObj != null) {
			blockMetadata = worldObj.getBlockMetadata(field_145851_c,
					field_145848_d, field_145849_e);
			worldObj.func_147476_b(field_145851_c, field_145848_d,
					field_145849_e, this);

			if (getBlockType() != Blocks.air) {
				worldObj.func_147453_f(field_145851_c, field_145848_d,
						field_145849_e, getBlockType());
			}
		}
	}

	public void readFromNBT(NBTTagCompound p_145839_1_) {
		field_145851_c = p_145839_1_.getInteger("x");
		field_145848_d = p_145839_1_.getInteger("y");
		field_145849_e = p_145839_1_.getInteger("z");
	}

	public boolean receiveClientEvent(int p_145842_1_, int p_145842_2_) {
		return false;
	}

	/**
	 * Sets the worldObj for this tileEntity.
	 */
	public void setWorldObj(World p_145834_1_) {
		worldObj = p_145834_1_;
	}

	public void updateContainingBlockInfo() {
		blockType = null;
		blockMetadata = -1;
	}

	public void updateEntity() {
	}

	/**
	 * validates a tile entity
	 */
	public void validate() {
		tileEntityInvalid = false;
	}

	public void writeToNBT(NBTTagCompound p_145841_1_) {
		final String var2 = (String) classToNameMap.get(this.getClass());

		if (var2 == null)
			throw new RuntimeException(this.getClass()
					+ " is missing a mapping! This is a bug!");
		else {
			p_145841_1_.setString("id", var2);
			p_145841_1_.setInteger("x", field_145851_c);
			p_145841_1_.setInteger("y", field_145848_d);
			p_145841_1_.setInteger("z", field_145849_e);
		}
	}
}
