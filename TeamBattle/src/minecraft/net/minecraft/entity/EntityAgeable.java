package net.minecraft.entity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public abstract class EntityAgeable extends EntityCreature {
	private float field_98056_d = -1.0F;
	private float field_98057_e;

	public EntityAgeable(World p_i1578_1_) {
		super(p_i1578_1_);
	}

	/**
	 * "Adds the value of the parameter times 20 to the age of this entity. If
	 * the entity is an adult (if the entity's age is greater than 0), it will
	 * have no effect."
	 */
	public void addGrowth(int p_110195_1_) {
		int var2 = getGrowingAge();
		var2 += p_110195_1_ * 20;

		if (var2 > 0) {
			var2 = 0;
		}

		setGrowingAge(var2);
	}

	public abstract EntityAgeable createChild(EntityAgeable p_90011_1_);

	@Override
	protected void entityInit() {
		super.entityInit();
		dataWatcher.addObject(12, new Integer(0));
	}

	/**
	 * The age value may be negative or positive or zero. If it's negative, it
	 * get's incremented on each tick, if it's positive, it get's decremented
	 * each tick. Don't confuse this with EntityLiving.getAge. With a negative
	 * value the Entity is considered a child.
	 */
	public int getGrowingAge() {
		return dataWatcher.getWatchableObjectInt(12);
	}

	/**
	 * Called when a player interacts with a mob. e.g. gets milk from a cow,
	 * gets into the saddle on a pig.
	 */
	@Override
	public boolean interact(EntityPlayer p_70085_1_) {
		final ItemStack var2 = p_70085_1_.inventory.getCurrentItem();

		if (var2 != null && var2.getItem() == Items.spawn_egg) {
			if (!worldObj.isClient) {
				final Class var3 = EntityList.getClassFromID(var2
						.getItemDamage());

				if (var3 != null && var3.isAssignableFrom(this.getClass())) {
					final EntityAgeable var4 = createChild(this);

					if (var4 != null) {
						var4.setGrowingAge(-24000);
						var4.setLocationAndAngles(posX, posY, posZ, 0.0F, 0.0F);
						worldObj.spawnEntityInWorld(var4);

						if (var2.hasDisplayName()) {
							var4.setCustomNameTag(var2.getDisplayName());
						}

						if (!p_70085_1_.capabilities.isCreativeMode) {
							--var2.stackSize;

							if (var2.stackSize <= 0) {
								p_70085_1_.inventory.setInventorySlotContents(
										p_70085_1_.inventory.currentItem,
										(ItemStack) null);
							}
						}
					}
				}
			}

			return true;
		} else
			return false;
	}

	/**
	 * If Animal, checks if the age timer is negative
	 */
	@Override
	public boolean isChild() {
		return getGrowingAge() < 0;
	}

	/**
	 * Called frequently so the entity can update its state every tick as
	 * required. For example, zombies and skeletons use this to react to
	 * sunlight and start to burn.
	 */
	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();

		if (worldObj.isClient) {
			setScaleForAge(isChild());
		} else {
			int var1 = getGrowingAge();

			if (var1 < 0) {
				++var1;
				setGrowingAge(var1);
			} else if (var1 > 0) {
				--var1;
				setGrowingAge(var1);
			}
		}
	}

	/**
	 * (abstract) Protected helper method to read subclass entity data from NBT.
	 */
	@Override
	public void readEntityFromNBT(NBTTagCompound p_70037_1_) {
		super.readEntityFromNBT(p_70037_1_);
		setGrowingAge(p_70037_1_.getInteger("Age"));
	}

	/**
	 * The age value may be negative or positive or zero. If it's negative, it
	 * get's incremented on each tick, if it's positive, it get's decremented
	 * each tick. With a negative value the Entity is considered a child.
	 */
	public void setGrowingAge(int p_70873_1_) {
		dataWatcher.updateObject(12, Integer.valueOf(p_70873_1_));
		setScaleForAge(isChild());
	}

	protected final void setScale(float p_98055_1_) {
		super.setSize(field_98056_d * p_98055_1_, field_98057_e * p_98055_1_);
	}

	/**
	 * "Sets the scale for an ageable entity according to the boolean parameter, which says if it's a child."
	 */
	public void setScaleForAge(boolean p_98054_1_) {
		setScale(p_98054_1_ ? 0.5F : 1.0F);
	}

	/**
	 * Sets the width and height of the entity. Args: width, height
	 */
	@Override
	protected final void setSize(float p_70105_1_, float p_70105_2_) {
		final boolean var3 = field_98056_d > 0.0F;
		field_98056_d = p_70105_1_;
		field_98057_e = p_70105_2_;

		if (!var3) {
			setScale(1.0F);
		}
	}

	/**
	 * (abstract) Protected helper method to write subclass entity data to NBT.
	 */
	@Override
	public void writeEntityToNBT(NBTTagCompound p_70014_1_) {
		super.writeEntityToNBT(p_70014_1_);
		p_70014_1_.setInteger("Age", getGrowingAge());
	}
}
