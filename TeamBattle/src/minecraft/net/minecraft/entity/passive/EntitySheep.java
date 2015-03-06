package net.minecraft.entity.passive;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIEatGrass;
import net.minecraft.entity.ai.EntityAIFollowParent;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMate;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntitySheep extends EntityAnimal {
	/**
	 * Holds the RGB table of the sheep colors - in OpenGL glColor3f values -
	 * used to render the sheep colored fleece.
	 */
	public static final float[][] fleeceColorTable = new float[][] {
			{ 1.0F, 1.0F, 1.0F }, { 0.85F, 0.5F, 0.2F }, { 0.7F, 0.3F, 0.85F },
			{ 0.4F, 0.6F, 0.85F }, { 0.9F, 0.9F, 0.2F }, { 0.5F, 0.8F, 0.1F },
			{ 0.95F, 0.5F, 0.65F }, { 0.3F, 0.3F, 0.3F }, { 0.6F, 0.6F, 0.6F },
			{ 0.3F, 0.5F, 0.6F }, { 0.5F, 0.25F, 0.7F }, { 0.2F, 0.3F, 0.7F },
			{ 0.4F, 0.3F, 0.2F }, { 0.4F, 0.5F, 0.2F }, { 0.6F, 0.2F, 0.2F },
			{ 0.1F, 0.1F, 0.1F } };

	/**
	 * This method is called when a sheep spawns in the world to select the
	 * color of sheep fleece.
	 */
	public static int getRandomFleeceColor(Random p_70895_0_) {
		final int var1 = p_70895_0_.nextInt(100);
		return var1 < 5 ? 15 : var1 < 10 ? 7 : var1 < 15 ? 8 : var1 < 18 ? 12
				: p_70895_0_.nextInt(500) == 0 ? 6 : 0;
	}

	private final EntityAIEatGrass field_146087_bs = new EntityAIEatGrass(this);
	private final InventoryCrafting field_90016_e = new InventoryCrafting(
			new Container() {

				@Override
				public boolean canInteractWith(EntityPlayer p_75145_1_) {
					return false;
				}
			}, 2, 1);

	/**
	 * Used to control movement as well as wool regrowth. Set to 40 on
	 * handleHealthUpdate and counts down with each tick.
	 */
	private int sheepTimer;

	public EntitySheep(World p_i1691_1_) {
		super(p_i1691_1_);
		setSize(0.9F, 1.3F);
		getNavigator().setAvoidsWater(true);
		tasks.addTask(0, new EntityAISwimming(this));
		tasks.addTask(1, new EntityAIPanic(this, 1.25D));
		tasks.addTask(2, new EntityAIMate(this, 1.0D));
		tasks.addTask(3, new EntityAITempt(this, 1.1D, Items.wheat, false));
		tasks.addTask(4, new EntityAIFollowParent(this, 1.1D));
		tasks.addTask(5, field_146087_bs);
		tasks.addTask(6, new EntityAIWander(this, 1.0D));
		tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class,
				6.0F));
		tasks.addTask(8, new EntityAILookIdle(this));
		field_90016_e.setInventorySlotContents(0,
				new ItemStack(Items.dye, 1, 0));
		field_90016_e.setInventorySlotContents(1,
				new ItemStack(Items.dye, 1, 0));
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.maxHealth)
				.setBaseValue(8.0D);
		getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(
				0.23000000417232513D);
	}

	@Override
	public EntitySheep createChild(EntityAgeable p_90011_1_) {
		final EntitySheep var2 = (EntitySheep) p_90011_1_;
		final EntitySheep var3 = new EntitySheep(worldObj);
		final int var4 = func_90014_a(this, var2);
		var3.setFleeceColor(15 - var4);
		return var3;
	}

	/**
	 * Drop 0-2 items of this living's type
	 */
	@Override
	protected void dropFewItems(boolean p_70628_1_, int p_70628_2_) {
		if (!getSheared()) {
			entityDropItem(new ItemStack(Item.getItemFromBlock(Blocks.wool), 1,
					getFleeceColor()), 0.0F);
		}
	}

	/**
	 * This function applies the benefits of growing back wool and faster
	 * growing up to the acting entity. (This function is used in the
	 * AIEatGrass)
	 */
	@Override
	public void eatGrassBonus() {
		setSheared(false);

		if (isChild()) {
			addGrowth(60);
		}
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		dataWatcher.addObject(16, new Byte((byte) 0));
	}

	@Override
	protected void func_145780_a(int p_145780_1_, int p_145780_2_,
			int p_145780_3_, Block p_145780_4_) {
		playSound("mob.sheep.step", 0.15F, 1.0F);
	}

	@Override
	protected Item func_146068_u() {
		return Item.getItemFromBlock(Blocks.wool);
	}

	public float func_70890_k(float p_70890_1_) {
		if (sheepTimer > 4 && sheepTimer <= 36) {
			final float var2 = (sheepTimer - 4 - p_70890_1_) / 32.0F;
			return (float) Math.PI / 5F + (float) Math.PI * 7F / 100F
					* MathHelper.sin(var2 * 28.7F);
		} else
			return sheepTimer > 0 ? (float) Math.PI / 5F : rotationPitch
					/ (180F / (float) Math.PI);
	}

	public float func_70894_j(float p_70894_1_) {
		return sheepTimer <= 0 ? 0.0F
				: sheepTimer >= 4 && sheepTimer <= 36 ? 1.0F
						: sheepTimer < 4 ? (sheepTimer - p_70894_1_) / 4.0F
								: -(sheepTimer - 40 - p_70894_1_) / 4.0F;
	}

	private int func_90013_b(EntityAnimal p_90013_1_) {
		return 15 - ((EntitySheep) p_90013_1_).getFleeceColor();
	}

	private int func_90014_a(EntityAnimal p_90014_1_, EntityAnimal p_90014_2_) {
		final int var3 = func_90013_b(p_90014_1_);
		final int var4 = func_90013_b(p_90014_2_);
		field_90016_e.getStackInSlot(0).setItemDamage(var3);
		field_90016_e.getStackInSlot(1).setItemDamage(var4);
		final ItemStack var5 = CraftingManager.getInstance()
				.findMatchingRecipe(field_90016_e,
						((EntitySheep) p_90014_1_).worldObj);
		int var6;

		if (var5 != null && var5.getItem() == Items.dye) {
			var6 = var5.getItemDamage();
		} else {
			var6 = worldObj.rand.nextBoolean() ? var3 : var4;
		}

		return var6;
	}

	/**
	 * Returns the sound this mob makes on death.
	 */
	@Override
	protected String getDeathSound() {
		return "mob.sheep.say";
	}

	public int getFleeceColor() {
		return dataWatcher.getWatchableObjectByte(16) & 15;
	}

	/**
	 * Returns the sound this mob makes when it is hurt.
	 */
	@Override
	protected String getHurtSound() {
		return "mob.sheep.say";
	}

	/**
	 * Returns the sound this mob makes while it's alive.
	 */
	@Override
	protected String getLivingSound() {
		return "mob.sheep.say";
	}

	/**
	 * returns true if a sheeps wool has been sheared
	 */
	public boolean getSheared() {
		return (dataWatcher.getWatchableObjectByte(16) & 16) != 0;
	}

	@Override
	public void handleHealthUpdate(byte p_70103_1_) {
		if (p_70103_1_ == 10) {
			sheepTimer = 40;
		} else {
			super.handleHealthUpdate(p_70103_1_);
		}
	}

	/**
	 * Called when a player interacts with a mob. e.g. gets milk from a cow,
	 * gets into the saddle on a pig.
	 */
	@Override
	public boolean interact(EntityPlayer p_70085_1_) {
		final ItemStack var2 = p_70085_1_.inventory.getCurrentItem();

		if (var2 != null && var2.getItem() == Items.shears && !getSheared()
				&& !isChild()) {
			if (!worldObj.isClient) {
				setSheared(true);
				final int var3 = 1 + rand.nextInt(3);

				for (int var4 = 0; var4 < var3; ++var4) {
					final EntityItem var5 = entityDropItem(
							new ItemStack(Item.getItemFromBlock(Blocks.wool),
									1, getFleeceColor()), 1.0F);
					var5.motionY += rand.nextFloat() * 0.05F;
					var5.motionX += (rand.nextFloat() - rand.nextFloat()) * 0.1F;
					var5.motionZ += (rand.nextFloat() - rand.nextFloat()) * 0.1F;
				}
			}

			var2.damageItem(1, p_70085_1_);
			playSound("mob.sheep.shear", 1.0F, 1.0F);
		}

		return super.interact(p_70085_1_);
	}

	/**
	 * Returns true if the newer Entity AI code should be run
	 */
	@Override
	protected boolean isAIEnabled() {
		return true;
	}

	/**
	 * Called frequently so the entity can update its state every tick as
	 * required. For example, zombies and skeletons use this to react to
	 * sunlight and start to burn.
	 */
	@Override
	public void onLivingUpdate() {
		if (worldObj.isClient) {
			sheepTimer = Math.max(0, sheepTimer - 1);
		}

		super.onLivingUpdate();
	}

	@Override
	public IEntityLivingData onSpawnWithEgg(IEntityLivingData p_110161_1_) {
		p_110161_1_ = super.onSpawnWithEgg(p_110161_1_);
		setFleeceColor(getRandomFleeceColor(worldObj.rand));
		return p_110161_1_;
	}

	/**
	 * (abstract) Protected helper method to read subclass entity data from NBT.
	 */
	@Override
	public void readEntityFromNBT(NBTTagCompound p_70037_1_) {
		super.readEntityFromNBT(p_70037_1_);
		setSheared(p_70037_1_.getBoolean("Sheared"));
		setFleeceColor(p_70037_1_.getByte("Color"));
	}

	public void setFleeceColor(int p_70891_1_) {
		final byte var2 = dataWatcher.getWatchableObjectByte(16);
		dataWatcher.updateObject(16,
				Byte.valueOf((byte) (var2 & 240 | p_70891_1_ & 15)));
	}

	/**
	 * make a sheep sheared if set to true
	 */
	public void setSheared(boolean p_70893_1_) {
		final byte var2 = dataWatcher.getWatchableObjectByte(16);

		if (p_70893_1_) {
			dataWatcher.updateObject(16, Byte.valueOf((byte) (var2 | 16)));
		} else {
			dataWatcher.updateObject(16, Byte.valueOf((byte) (var2 & -17)));
		}
	}

	@Override
	protected void updateAITasks() {
		sheepTimer = field_146087_bs.func_151499_f();
		super.updateAITasks();
	}

	/**
	 * (abstract) Protected helper method to write subclass entity data to NBT.
	 */
	@Override
	public void writeEntityToNBT(NBTTagCompound p_70014_1_) {
		super.writeEntityToNBT(p_70014_1_);
		p_70014_1_.setBoolean("Sheared", getSheared());
		p_70014_1_.setByte("Color", (byte) getFleeceColor());
	}
}
