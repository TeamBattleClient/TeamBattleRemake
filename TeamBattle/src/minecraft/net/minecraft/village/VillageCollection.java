package net.minecraft.village;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.block.BlockDoor;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;

public class VillageCollection extends WorldSavedData {
	private final List newDoors = new ArrayList();

	private int tickCounter;
	private final List villageList = new ArrayList();
	/**
	 * This is a black hole. You can add data to this list through a public
	 * interface, but you can't query that information in any way and it's not
	 * used internally either.
	 */
	private final List villagerPositionsList = new ArrayList();
	private World worldObj;

	public VillageCollection(String p_i1677_1_) {
		super(p_i1677_1_);
	}

	public VillageCollection(World p_i1678_1_) {
		super("villages");
		worldObj = p_i1678_1_;
		markDirty();
	}

	private void addDoorToNewListIfAppropriate(int p_75542_1_, int p_75542_2_,
			int p_75542_3_) {
		final int var4 = ((BlockDoor) Blocks.wooden_door).func_150013_e(
				worldObj, p_75542_1_, p_75542_2_, p_75542_3_);
		int var5;
		int var6;

		if (var4 != 0 && var4 != 2) {
			var5 = 0;

			for (var6 = -5; var6 < 0; ++var6) {
				if (worldObj.canBlockSeeTheSky(p_75542_1_, p_75542_2_,
						p_75542_3_ + var6)) {
					--var5;
				}
			}

			for (var6 = 1; var6 <= 5; ++var6) {
				if (worldObj.canBlockSeeTheSky(p_75542_1_, p_75542_2_,
						p_75542_3_ + var6)) {
					++var5;
				}
			}

			if (var5 != 0) {
				newDoors.add(new VillageDoorInfo(p_75542_1_, p_75542_2_,
						p_75542_3_, 0, var5 > 0 ? -2 : 2, tickCounter));
			}
		} else {
			var5 = 0;

			for (var6 = -5; var6 < 0; ++var6) {
				if (worldObj.canBlockSeeTheSky(p_75542_1_ + var6, p_75542_2_,
						p_75542_3_)) {
					--var5;
				}
			}

			for (var6 = 1; var6 <= 5; ++var6) {
				if (worldObj.canBlockSeeTheSky(p_75542_1_ + var6, p_75542_2_,
						p_75542_3_)) {
					++var5;
				}
			}

			if (var5 != 0) {
				newDoors.add(new VillageDoorInfo(p_75542_1_, p_75542_2_,
						p_75542_3_, var5 > 0 ? -2 : 2, 0, tickCounter));
			}
		}
	}

	private void addNewDoorsToVillageOrCreateVillage() {
		int var1 = 0;

		while (var1 < newDoors.size()) {
			final VillageDoorInfo var2 = (VillageDoorInfo) newDoors.get(var1);
			boolean var3 = false;
			final Iterator var4 = villageList.iterator();

			while (true) {
				if (var4.hasNext()) {
					final Village var5 = (Village) var4.next();
					final int var6 = (int) var5.getCenter().getDistanceSquared(
							var2.posX, var2.posY, var2.posZ);
					final int var7 = 32 + var5.getVillageRadius();

					if (var6 > var7 * var7) {
						continue;
					}

					var5.addVillageDoorInfo(var2);
					var3 = true;
				}

				if (!var3) {
					final Village var8 = new Village(worldObj);
					var8.addVillageDoorInfo(var2);
					villageList.add(var8);
					markDirty();
				}

				++var1;
				break;
			}
		}

		newDoors.clear();
	}

	private void addUnassignedWoodenDoorsAroundToNewDoorsList(
			ChunkCoordinates p_75546_1_) {
		final byte var2 = 16;
		final byte var3 = 4;
		final byte var4 = 16;

		for (int var5 = p_75546_1_.posX - var2; var5 < p_75546_1_.posX + var2; ++var5) {
			for (int var6 = p_75546_1_.posY - var3; var6 < p_75546_1_.posY
					+ var3; ++var6) {
				for (int var7 = p_75546_1_.posZ - var4; var7 < p_75546_1_.posZ
						+ var4; ++var7) {
					if (isWoodenDoorAt(var5, var6, var7)) {
						final VillageDoorInfo var8 = getVillageDoorAt(var5,
								var6, var7);

						if (var8 == null) {
							addDoorToNewListIfAppropriate(var5, var6, var7);
						} else {
							var8.lastActivityTimestamp = tickCounter;
						}
					}
				}
			}
		}
	}

	/**
	 * This is a black hole. You can add data to this list through a public
	 * interface, but you can't query that information in any way and it's not
	 * used internally either.
	 */
	public void addVillagerPosition(int p_75551_1_, int p_75551_2_,
			int p_75551_3_) {
		if (villagerPositionsList.size() <= 64) {
			if (!isVillagerPositionPresent(p_75551_1_, p_75551_2_, p_75551_3_)) {
				villagerPositionsList.add(new ChunkCoordinates(p_75551_1_,
						p_75551_2_, p_75551_3_));
			}
		}
	}

	private void dropOldestVillagerPosition() {
		if (!villagerPositionsList.isEmpty()) {
			addUnassignedWoodenDoorsAroundToNewDoorsList((ChunkCoordinates) villagerPositionsList
					.remove(0));
		}
	}

	/**
	 * Finds the nearest village, but only the given coordinates are withing
	 * it's bounding box plus the given the distance.
	 */
	public Village findNearestVillage(int p_75550_1_, int p_75550_2_,
			int p_75550_3_, int p_75550_4_) {
		Village var5 = null;
		float var6 = Float.MAX_VALUE;
		final Iterator var7 = villageList.iterator();

		while (var7.hasNext()) {
			final Village var8 = (Village) var7.next();
			final float var9 = var8.getCenter().getDistanceSquared(p_75550_1_,
					p_75550_2_, p_75550_3_);

			if (var9 < var6) {
				final float var10 = p_75550_4_ + var8.getVillageRadius();

				if (var9 <= var10 * var10) {
					var5 = var8;
					var6 = var9;
				}
			}
		}

		return var5;
	}

	public void func_82566_a(World p_82566_1_) {
		worldObj = p_82566_1_;
		final Iterator var2 = villageList.iterator();

		while (var2.hasNext()) {
			final Village var3 = (Village) var2.next();
			var3.func_82691_a(p_82566_1_);
		}
	}

	private VillageDoorInfo getVillageDoorAt(int p_75547_1_, int p_75547_2_,
			int p_75547_3_) {
		Iterator var4 = newDoors.iterator();
		VillageDoorInfo var5;

		do {
			if (!var4.hasNext()) {
				var4 = villageList.iterator();
				VillageDoorInfo var6;

				do {
					if (!var4.hasNext())
						return null;

					final Village var7 = (Village) var4.next();
					var6 = var7.getVillageDoorAt(p_75547_1_, p_75547_2_,
							p_75547_3_);
				} while (var6 == null);

				return var6;
			}

			var5 = (VillageDoorInfo) var4.next();
		} while (var5.posX != p_75547_1_ || var5.posZ != p_75547_3_
				|| Math.abs(var5.posY - p_75547_2_) > 1);

		return var5;
	}

	/**
	 * Get a list of villages.
	 */
	public List getVillageList() {
		return villageList;
	}

	private boolean isVillagerPositionPresent(int p_75548_1_, int p_75548_2_,
			int p_75548_3_) {
		final Iterator var4 = villagerPositionsList.iterator();
		ChunkCoordinates var5;

		do {
			if (!var4.hasNext())
				return false;

			var5 = (ChunkCoordinates) var4.next();
		} while (var5.posX != p_75548_1_ || var5.posY != p_75548_2_
				|| var5.posZ != p_75548_3_);

		return true;
	}

	private boolean isWoodenDoorAt(int p_75541_1_, int p_75541_2_,
			int p_75541_3_) {
		return worldObj.getBlock(p_75541_1_, p_75541_2_, p_75541_3_) == Blocks.wooden_door;
	}

	/**
	 * reads in data from the NBTTagCompound into this MapDataBase
	 */
	@Override
	public void readFromNBT(NBTTagCompound p_76184_1_) {
		tickCounter = p_76184_1_.getInteger("Tick");
		final NBTTagList var2 = p_76184_1_.getTagList("Villages", 10);

		for (int var3 = 0; var3 < var2.tagCount(); ++var3) {
			final NBTTagCompound var4 = var2.getCompoundTagAt(var3);
			final Village var5 = new Village();
			var5.readVillageDataFromNBT(var4);
			villageList.add(var5);
		}
	}

	private void removeAnnihilatedVillages() {
		final Iterator var1 = villageList.iterator();

		while (var1.hasNext()) {
			final Village var2 = (Village) var1.next();

			if (var2.isAnnihilated()) {
				var1.remove();
				markDirty();
			}
		}
	}

	/**
	 * Runs a single tick for the village collection
	 */
	public void tick() {
		++tickCounter;
		final Iterator var1 = villageList.iterator();

		while (var1.hasNext()) {
			final Village var2 = (Village) var1.next();
			var2.tick(tickCounter);
		}

		removeAnnihilatedVillages();
		dropOldestVillagerPosition();
		addNewDoorsToVillageOrCreateVillage();

		if (tickCounter % 400 == 0) {
			markDirty();
		}
	}

	/**
	 * write data to NBTTagCompound from this MapDataBase, similar to Entities
	 * and TileEntities
	 */
	@Override
	public void writeToNBT(NBTTagCompound p_76187_1_) {
		p_76187_1_.setInteger("Tick", tickCounter);
		final NBTTagList var2 = new NBTTagList();
		final Iterator var3 = villageList.iterator();

		while (var3.hasNext()) {
			final Village var4 = (Village) var3.next();
			final NBTTagCompound var5 = new NBTTagCompound();
			var4.writeVillageDataToNBT(var5);
			var2.appendTag(var5);
		}

		p_76187_1_.setTag("Villages", var2);
	}
}
