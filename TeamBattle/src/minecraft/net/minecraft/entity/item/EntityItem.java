package net.minecraft.entity.item;

import java.util.Iterator;

import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.AchievementList;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EntityItem extends Entity {
	private static final Logger logger = LogManager.getLogger();

	/**
	 * The age of this EntityItem (used to animate it up and down as well as
	 * expire it)
	 */
	public int age;
	public int delayBeforeCanPickup;

	private String field_145801_f;
	private String field_145802_g;
	/** The health of this EntityItem. (For example, damage for tools) */
	private int health;

	/** The EntityItem's random initial float height. */
	public float hoverStart;

	public EntityItem(World p_i1711_1_) {
		super(p_i1711_1_);
		health = 5;
		hoverStart = (float) (Math.random() * Math.PI * 2.0D);
		setSize(0.25F, 0.25F);
		yOffset = height / 2.0F;
	}

	public EntityItem(World p_i1709_1_, double p_i1709_2_, double p_i1709_4_,
			double p_i1709_6_) {
		super(p_i1709_1_);
		health = 5;
		hoverStart = (float) (Math.random() * Math.PI * 2.0D);
		setSize(0.25F, 0.25F);
		yOffset = height / 2.0F;
		setPosition(p_i1709_2_, p_i1709_4_, p_i1709_6_);
		rotationYaw = (float) (Math.random() * 360.0D);
		motionX = (float) (Math.random() * 0.20000000298023224D - 0.10000000149011612D);
		motionY = 0.20000000298023224D;
		motionZ = (float) (Math.random() * 0.20000000298023224D - 0.10000000149011612D);
	}

	public EntityItem(World p_i1710_1_, double p_i1710_2_, double p_i1710_4_,
			double p_i1710_6_, ItemStack p_i1710_8_) {
		this(p_i1710_1_, p_i1710_2_, p_i1710_4_, p_i1710_6_);
		setEntityItemStack(p_i1710_8_);
	}

	/**
	 * Called when the entity is attacked.
	 */
	@Override
	public boolean attackEntityFrom(DamageSource p_70097_1_, float p_70097_2_) {
		if (isEntityInvulnerable())
			return false;
		else if (getEntityItem() != null
				&& getEntityItem().getItem() == Items.nether_star
				&& p_70097_1_.isExplosion())
			return false;
		else {
			setBeenAttacked();
			health = (int) (health - p_70097_2_);

			if (health <= 0) {
				setDead();
			}

			return false;
		}
	}

	/**
	 * If returns false, the item will not inflict any damage against entities.
	 */
	@Override
	public boolean canAttackWithItem() {
		return false;
	}

	/**
	 * returns if this entity triggers Block.onEntityWalking on the blocks they
	 * walk on. used for spiders and wolves to prevent them from trampling crops
	 */
	@Override
	protected boolean canTriggerWalking() {
		return false;
	}

	/**
	 * Tries to merge this item with the item passed as the parameter. Returns
	 * true if successful. Either this item or the other item will be removed
	 * from the world.
	 */
	public boolean combineItems(EntityItem p_70289_1_) {
		if (p_70289_1_ == this)
			return false;
		else if (p_70289_1_.isEntityAlive() && isEntityAlive()) {
			final ItemStack var2 = getEntityItem();
			final ItemStack var3 = p_70289_1_.getEntityItem();

			if (var3.getItem() != var2.getItem())
				return false;
			else if (var3.hasTagCompound() ^ var2.hasTagCompound())
				return false;
			else if (var3.hasTagCompound()
					&& !var3.getTagCompound().equals(var2.getTagCompound()))
				return false;
			else if (var3.getItem() == null)
				return false;
			else if (var3.getItem().getHasSubtypes()
					&& var3.getItemDamage() != var2.getItemDamage())
				return false;
			else if (var3.stackSize < var2.stackSize)
				return p_70289_1_.combineItems(this);
			else if (var3.stackSize + var2.stackSize > var3.getMaxStackSize())
				return false;
			else {
				var3.stackSize += var2.stackSize;
				p_70289_1_.delayBeforeCanPickup = Math.max(
						p_70289_1_.delayBeforeCanPickup, delayBeforeCanPickup);
				p_70289_1_.age = Math.min(p_70289_1_.age, age);
				p_70289_1_.setEntityItemStack(var3);
				setDead();
				return true;
			}
		} else
			return false;
	}

	/**
	 * Will deal the specified amount of damage to the entity if the entity
	 * isn't immune to fire damage. Args: amountDamage
	 */
	@Override
	protected void dealFireDamage(int p_70081_1_) {
		attackEntityFrom(DamageSource.inFire, p_70081_1_);
	}

	@Override
	protected void entityInit() {
		getDataWatcher().addObjectByDataType(10, 5);
	}

	public void func_145797_a(String p_145797_1_) {
		field_145802_g = p_145797_1_;
	}

	public String func_145798_i() {
		return field_145802_g;
	}

	public void func_145799_b(String p_145799_1_) {
		field_145801_f = p_145799_1_;
	}

	public String func_145800_j() {
		return field_145801_f;
	}

	/**
	 * Gets the name of this command sender (usually username, but possibly
	 * "Rcon")
	 */
	@Override
	public String getCommandSenderName() {
		return StatCollector.translateToLocal("item."
				+ getEntityItem().getUnlocalizedName());
	}

	/**
	 * Returns the ItemStack corresponding to the Entity (Note: if no item
	 * exists, will log an error but still return an ItemStack containing
	 * Block.stone)
	 */
	public ItemStack getEntityItem() {
		final ItemStack var1 = getDataWatcher().getWatchableObjectItemStack(10);
		return var1 == null ? new ItemStack(Blocks.stone) : var1;
	}

	/**
	 * Returns if this entity is in water and will end up adding the waters
	 * velocity to the entity
	 */
	@Override
	public boolean handleWaterMovement() {
		return worldObj.handleMaterialAcceleration(boundingBox, Material.water,
				this);
	}

	/**
	 * Called by a player entity when they collide with an entity
	 */
	@Override
	public void onCollideWithPlayer(EntityPlayer p_70100_1_) {
		if (!worldObj.isClient) {
			final ItemStack var2 = getEntityItem();
			final int var3 = var2.stackSize;

			if (delayBeforeCanPickup == 0
					&& (field_145802_g == null || 6000 - age <= 200 || field_145802_g
							.equals(p_70100_1_.getCommandSenderName()))
					&& p_70100_1_.inventory.addItemStackToInventory(var2)) {
				if (var2.getItem() == Item.getItemFromBlock(Blocks.log)) {
					p_70100_1_.triggerAchievement(AchievementList.mineWood);
				}

				if (var2.getItem() == Item.getItemFromBlock(Blocks.log2)) {
					p_70100_1_.triggerAchievement(AchievementList.mineWood);
				}

				if (var2.getItem() == Items.leather) {
					p_70100_1_.triggerAchievement(AchievementList.killCow);
				}

				if (var2.getItem() == Items.diamond) {
					p_70100_1_.triggerAchievement(AchievementList.diamonds);
				}

				if (var2.getItem() == Items.blaze_rod) {
					p_70100_1_.triggerAchievement(AchievementList.blazeRod);
				}

				if (var2.getItem() == Items.diamond && func_145800_j() != null) {
					final EntityPlayer var4 = worldObj
							.getPlayerEntityByName(func_145800_j());

					if (var4 != null && var4 != p_70100_1_) {
						var4.triggerAchievement(AchievementList.field_150966_x);
					}
				}

				worldObj.playSoundAtEntity(
						p_70100_1_,
						"random.pop",
						0.2F,
						((rand.nextFloat() - rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
				p_70100_1_.onItemPickup(this, var3);

				if (var2.stackSize <= 0) {
					setDead();
				}
			}
		}
	}

	/**
	 * Called to update the entity's position/logic.
	 */
	@Override
	public void onUpdate() {
		if (getEntityItem() == null) {
			setDead();
		} else {
			super.onUpdate();

			if (delayBeforeCanPickup > 0) {
				--delayBeforeCanPickup;
			}

			prevPosX = posX;
			prevPosY = posY;
			prevPosZ = posZ;
			motionY -= 0.03999999910593033D;
			noClip = func_145771_j(posX,
					(boundingBox.minY + boundingBox.maxY) / 2.0D, posZ);
			moveEntity(motionX, motionY, motionZ);
			final boolean var1 = (int) prevPosX != (int) posX
					|| (int) prevPosY != (int) posY
					|| (int) prevPosZ != (int) posZ;

			if (var1 || ticksExisted % 25 == 0) {
				if (worldObj.getBlock(MathHelper.floor_double(posX),
						MathHelper.floor_double(posY),
						MathHelper.floor_double(posZ)).getMaterial() == Material.lava) {
					motionY = 0.20000000298023224D;
					motionX = (rand.nextFloat() - rand.nextFloat()) * 0.2F;
					motionZ = (rand.nextFloat() - rand.nextFloat()) * 0.2F;
					playSound("random.fizz", 0.4F,
							2.0F + rand.nextFloat() * 0.4F);
				}

				if (!worldObj.isClient) {
					searchForOtherItemsNearby();
				}
			}

			float var2 = 0.98F;

			if (onGround) {
				var2 = worldObj.getBlock(MathHelper.floor_double(posX),
						MathHelper.floor_double(boundingBox.minY) - 1,
						MathHelper.floor_double(posZ)).slipperiness * 0.98F;
			}

			motionX *= var2;
			motionY *= 0.9800000190734863D;
			motionZ *= var2;

			if (onGround) {
				motionY *= -0.5D;
			}

			++age;

			if (!worldObj.isClient && age >= 6000) {
				setDead();
			}
		}
	}

	/**
	 * (abstract) Protected helper method to read subclass entity data from NBT.
	 */
	@Override
	public void readEntityFromNBT(NBTTagCompound p_70037_1_) {
		health = p_70037_1_.getShort("Health") & 255;
		age = p_70037_1_.getShort("Age");

		if (p_70037_1_.hasKey("Owner")) {
			field_145802_g = p_70037_1_.getString("Owner");
		}

		if (p_70037_1_.hasKey("Thrower")) {
			field_145801_f = p_70037_1_.getString("Thrower");
		}

		final NBTTagCompound var2 = p_70037_1_.getCompoundTag("Item");
		setEntityItemStack(ItemStack.loadItemStackFromNBT(var2));

		if (getEntityItem() == null) {
			setDead();
		}
	}

	/**
	 * Looks for other itemstacks nearby and tries to stack them together
	 */
	private void searchForOtherItemsNearby() {
		final Iterator var1 = worldObj.getEntitiesWithinAABB(EntityItem.class,
				boundingBox.expand(0.5D, 0.0D, 0.5D)).iterator();

		while (var1.hasNext()) {
			final EntityItem var2 = (EntityItem) var1.next();
			combineItems(var2);
		}
	}

	/**
	 * sets the age of the item so that it'll despawn one minute after it has
	 * been dropped (instead of five). Used when items are dropped from players
	 * in creative mode
	 */
	public void setAgeToCreativeDespawnTime() {
		age = 4800;
	}

	/**
	 * Sets the ItemStack for this entity
	 */
	public void setEntityItemStack(ItemStack p_92058_1_) {
		getDataWatcher().updateObject(10, p_92058_1_);
		getDataWatcher().setObjectWatched(10);
	}

	/**
	 * Teleports the entity to another dimension. Params: Dimension number to
	 * teleport to
	 */
	@Override
	public void travelToDimension(int p_71027_1_) {
		super.travelToDimension(p_71027_1_);

		if (!worldObj.isClient) {
			searchForOtherItemsNearby();
		}
	}

	/**
	 * (abstract) Protected helper method to write subclass entity data to NBT.
	 */
	@Override
	public void writeEntityToNBT(NBTTagCompound p_70014_1_) {
		p_70014_1_.setShort("Health", (byte) health);
		p_70014_1_.setShort("Age", (short) age);

		if (func_145800_j() != null) {
			p_70014_1_.setString("Thrower", field_145801_f);
		}

		if (func_145798_i() != null) {
			p_70014_1_.setString("Owner", field_145802_g);
		}

		if (getEntityItem() != null) {
			p_70014_1_.setTag("Item",
					getEntityItem().writeToNBT(new NBTTagCompound()));
		}
	}
}
