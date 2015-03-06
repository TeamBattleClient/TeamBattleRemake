package net.minecraft.item;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentDurability;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.event.HoverEvent;
import net.minecraft.init.Items;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.stats.StatList;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

public final class ItemStack {
	public static final DecimalFormat field_111284_a = new DecimalFormat(
			"#.###");

	/**
	 * compares ItemStack argument1 with ItemStack argument2; returns true if
	 * both ItemStacks are equal
	 */
	public static boolean areItemStacksEqual(ItemStack p_77989_0_,
			ItemStack p_77989_1_) {
		return p_77989_0_ == null && p_77989_1_ == null ? true
				: p_77989_0_ != null && p_77989_1_ != null ? p_77989_0_
						.isItemStackEqual(p_77989_1_) : false;
	}

	public static boolean areItemStackTagsEqual(ItemStack p_77970_0_,
			ItemStack p_77970_1_) {
		return p_77970_0_ == null && p_77970_1_ == null ? true
				: p_77970_0_ != null && p_77970_1_ != null ? p_77970_0_.stackTagCompound == null
						&& p_77970_1_.stackTagCompound != null ? false
						: p_77970_0_.stackTagCompound == null
								|| p_77970_0_.stackTagCompound
										.equals(p_77970_1_.stackTagCompound)
						: false;
	}

	/**
	 * Creates a copy of a ItemStack, a null parameters will return a null.
	 */
	public static ItemStack copyItemStack(ItemStack p_77944_0_) {
		return p_77944_0_ == null ? null : p_77944_0_.copy();
	}

	public static ItemStack loadItemStackFromNBT(NBTTagCompound p_77949_0_) {
		final ItemStack var1 = new ItemStack();
		var1.readFromNBT(p_77949_0_);
		return var1.getItem() != null ? var1 : null;
	}

	/**
	 * Number of animation frames to go when receiving an item (by walking into
	 * it, for example).
	 */
	public int animationsToGo;

	private Item field_151002_e;

	/** Damage dealt to the item or number of use. Raise when using items. */
	private int itemDamage;

	/** Item frame this stack is on, or null if not on an item frame. */
	private EntityItemFrame itemFrame;

	/** Size of the stack. */
	public int stackSize;

	/**
	 * A NBTTagMap containing data about an ItemStack. Can only be used for non
	 * stackable items
	 */
	public NBTTagCompound stackTagCompound;

	private ItemStack() {
	}

	public ItemStack(Block p_i1876_1_) {
		this(p_i1876_1_, 1);
	}

	public ItemStack(Block p_i1877_1_, int p_i1877_2_) {
		this(p_i1877_1_, p_i1877_2_, 0);
	}

	public ItemStack(Block p_i1878_1_, int p_i1878_2_, int p_i1878_3_) {
		this(Item.getItemFromBlock(p_i1878_1_), p_i1878_2_, p_i1878_3_);
	}

	public ItemStack(Item p_i1879_1_) {
		this(p_i1879_1_, 1);
	}

	public ItemStack(Item p_i1880_1_, int p_i1880_2_) {
		this(p_i1880_1_, p_i1880_2_, 0);
	}

	public ItemStack(Item p_i1881_1_, int p_i1881_2_, int p_i1881_3_) {
		field_151002_e = p_i1881_1_;
		stackSize = p_i1881_2_;
		itemDamage = p_i1881_3_;

		if (itemDamage < 0) {
			itemDamage = 0;
		}
	}

	/**
	 * Adds an enchantment with a desired level on the ItemStack.
	 */
	public void addEnchantment(Enchantment p_77966_1_, int p_77966_2_) {
		if (stackTagCompound == null) {
			setTagCompound(new NBTTagCompound());
		}

		if (!stackTagCompound.func_150297_b("ench", 9)) {
			stackTagCompound.setTag("ench", new NBTTagList());
		}

		final NBTTagList var3 = stackTagCompound.getTagList("ench", 10);
		final NBTTagCompound var4 = new NBTTagCompound();
		var4.setShort("id", (short) p_77966_1_.effectId);
		var4.setShort("lvl", (byte) p_77966_2_);
		var3.appendTag(var4);
	}

	/**
	 * Attempts to damage the ItemStack with par1 amount of damage, If the
	 * ItemStack has the Unbreaking enchantment there is a chance for each point
	 * of damage to be negated. Returns true if it takes more damage than
	 * getMaxDamage(). Returns false otherwise or if the ItemStack can't be
	 * damaged or if all points of damage are negated.
	 */
	public boolean attemptDamageItem(int p_96631_1_, Random p_96631_2_) {
		if (!isItemStackDamageable())
			return false;
		else {
			if (p_96631_1_ > 0) {
				final int var3 = EnchantmentHelper.getEnchantmentLevel(
						Enchantment.unbreaking.effectId, this);
				int var4 = 0;

				for (int var5 = 0; var3 > 0 && var5 < p_96631_1_; ++var5) {
					if (EnchantmentDurability.negateDamage(this, var3,
							p_96631_2_)) {
						++var4;
					}
				}

				p_96631_1_ -= var4;

				if (p_96631_1_ <= 0)
					return false;
			}

			itemDamage += p_96631_1_;
			return itemDamage > getMaxDamage();
		}
	}

	public boolean canEditBlocks() {
		return getItem().canItemEditBlocks();
	}

	/**
	 * Returns a new stack with the same properties.
	 */
	public ItemStack copy() {
		final ItemStack var1 = new ItemStack(field_151002_e, stackSize,
				itemDamage);

		if (stackTagCompound != null) {
			var1.stackTagCompound = (NBTTagCompound) stackTagCompound.copy();
		}

		return var1;
	}

	/**
	 * Damages the item in the ItemStack
	 */
	public void damageItem(int p_77972_1_, EntityLivingBase p_77972_2_) {
		if (!(p_77972_2_ instanceof EntityPlayer)
				|| !((EntityPlayer) p_77972_2_).capabilities.isCreativeMode) {
			if (isItemStackDamageable()) {
				if (attemptDamageItem(p_77972_1_, p_77972_2_.getRNG())) {
					p_77972_2_.renderBrokenItemStack(this);
					--stackSize;

					if (p_77972_2_ instanceof EntityPlayer) {
						final EntityPlayer var3 = (EntityPlayer) p_77972_2_;
						var3.addStat(StatList.objectBreakStats[Item
								.getIdFromItem(field_151002_e)], 1);

						if (stackSize == 0 && getItem() instanceof ItemBow) {
							var3.destroyCurrentEquippedItem();
						}
					}

					if (stackSize < 0) {
						stackSize = 0;
					}

					itemDamage = 0;
				}
			}
		}
	}

	public void func_135074_t() {
		if (stackTagCompound != null) {
			if (stackTagCompound.func_150297_b("display", 10)) {
				final NBTTagCompound var1 = stackTagCompound
						.getCompoundTag("display");
				var1.removeTag("Name");

				if (var1.hasNoTags()) {
					stackTagCompound.removeTag("display");

					if (stackTagCompound.hasNoTags()) {
						setTagCompound((NBTTagCompound) null);
					}
				}
			}
		}
	}

	public void func_150996_a(Item p_150996_1_) {
		field_151002_e = p_150996_1_;
	}

	public float func_150997_a(Block p_150997_1_) {
		return getItem().func_150893_a(this, p_150997_1_);
	}

	public boolean func_150998_b(Block p_150998_1_) {
		return field_151002_e.func_150897_b(p_150998_1_);
	}

	public void func_150999_a(World p_150999_1_, Block p_150999_2_,
			int p_150999_3_, int p_150999_4_, int p_150999_5_,
			EntityPlayer p_150999_6_) {
		final boolean var7 = field_151002_e
				.onBlockDestroyed(this, p_150999_1_, p_150999_2_, p_150999_3_,
						p_150999_4_, p_150999_5_, p_150999_6_);

		if (var7) {
			p_150999_6_
					.addStat(StatList.objectUseStats[Item
							.getIdFromItem(field_151002_e)], 1);
		}
	}

	public IChatComponent func_151000_E() {
		final IChatComponent var1 = new ChatComponentText("[").appendText(
				getDisplayName()).appendText("]");

		if (field_151002_e != null) {
			final NBTTagCompound var2 = new NBTTagCompound();
			writeToNBT(var2);
			var1.getChatStyle().setChatHoverEvent(
					new HoverEvent(HoverEvent.Action.SHOW_ITEM,
							new ChatComponentText(var2.toString())));
			var1.getChatStyle().setColor(getRarity().rarityColor);
		}

		return var1;
	}

	/**
	 * Gets the attribute modifiers for this ItemStack.\nWill check for an NBT
	 * tag list containing modifiers for the stack.
	 */
	public Multimap getAttributeModifiers() {
		Object var1;

		if (hasTagCompound()
				&& stackTagCompound.func_150297_b("AttributeModifiers", 9)) {
			var1 = HashMultimap.create();
			final NBTTagList var2 = stackTagCompound.getTagList(
					"AttributeModifiers", 10);

			for (int var3 = 0; var3 < var2.tagCount(); ++var3) {
				final NBTTagCompound var4 = var2.getCompoundTagAt(var3);
				final AttributeModifier var5 = SharedMonsterAttributes
						.readAttributeModifierFromNBT(var4);

				if (var5.getID().getLeastSignificantBits() != 0L
						&& var5.getID().getMostSignificantBits() != 0L) {
					((Multimap) var1)
							.put(var4.getString("AttributeName"), var5);
				}
			}
		} else {
			var1 = getItem().getItemAttributeModifiers();
		}

		return (Multimap) var1;
	}

	/**
	 * returns the display name of the itemstack
	 */
	public String getDisplayName() {
		String var1 = getItem().getItemStackDisplayName(this);

		if (stackTagCompound != null
				&& stackTagCompound.func_150297_b("display", 10)) {
			final NBTTagCompound var2 = stackTagCompound
					.getCompoundTag("display");

			if (var2.func_150297_b("Name", 8)) {
				var1 = var2.getString("Name");
			}
		}

		return var1;
	}

	public NBTTagList getEnchantmentTagList() {
		return stackTagCompound == null ? null : stackTagCompound.getTagList(
				"ench", 10);
	}

	public boolean getHasSubtypes() {
		return field_151002_e.getHasSubtypes();
	}

	/**
	 * Returns the icon index of the current stack.
	 */
	public IIcon getIconIndex() {
		return getItem().getIconIndex(this);
	}

	/**
	 * Returns the object corresponding to the stack.
	 */
	public Item getItem() {
		return field_151002_e;
	}

	/**
	 * gets the damage of an itemstack
	 */
	public int getItemDamage() {
		return itemDamage;
	}

	/**
	 * gets the damage of an itemstack, for displaying purposes
	 */
	public int getItemDamageForDisplay() {
		return itemDamage;
	}

	/**
	 * Return the item frame this stack is on. Returns null if not on an item
	 * frame.
	 */
	public EntityItemFrame getItemFrame() {
		return itemFrame;
	}

	public int getItemSpriteNumber() {
		return getItem().getSpriteNumber();
	}

	public EnumAction getItemUseAction() {
		return getItem().getItemUseAction(this);
	}

	/**
	 * Returns the max damage an item in the stack can take.
	 */
	public int getMaxDamage() {
		return field_151002_e.getMaxDamage();
	}

	public int getMaxItemUseDuration() {
		return getItem().getMaxItemUseDuration(this);
	}

	/**
	 * Returns maximum size of the stack.
	 */
	public int getMaxStackSize() {
		return getItem().getItemStackLimit();
	}

	public EnumRarity getRarity() {
		return getItem().getRarity(this);
	}

	/**
	 * Get this stack's repair cost, or 0 if no repair cost is defined.
	 */
	public int getRepairCost() {
		return hasTagCompound()
				&& stackTagCompound.func_150297_b("RepairCost", 3) ? stackTagCompound
				.getInteger("RepairCost") : 0;
	}

	/**
	 * Returns the NBTTagCompound of the ItemStack.
	 */
	public NBTTagCompound getTagCompound() {
		return stackTagCompound;
	}

	/**
	 * Return a list of strings containing information about the item
	 */
	public List getTooltip(EntityPlayer p_82840_1_, boolean p_82840_2_) {
		final ArrayList var3 = new ArrayList();
		String var4 = getDisplayName();

		if (hasDisplayName()) {
			var4 = EnumChatFormatting.ITALIC + var4 + EnumChatFormatting.RESET;
		}

		int var6;

		if (p_82840_2_) {
			String var5 = "";

			if (var4.length() > 0) {
				var4 = var4 + " (";
				var5 = ")";
			}

			var6 = Item.getIdFromItem(field_151002_e);

			if (getHasSubtypes()) {
				var4 = var4
						+ String.format(
								"#%04d/%d%s",
								new Object[] { Integer.valueOf(var6),
										Integer.valueOf(itemDamage), var5 });
			} else {
				var4 = var4
						+ String.format("#%04d%s",
								new Object[] { Integer.valueOf(var6), var5 });
			}
		} else if (!hasDisplayName() && field_151002_e == Items.filled_map) {
			var4 = var4 + " #" + itemDamage;
		}

		var3.add(var4);
		field_151002_e.addInformation(this, p_82840_1_, var3, p_82840_2_);

		if (hasTagCompound()) {
			final NBTTagList var13 = getEnchantmentTagList();

			if (var13 != null) {
				for (var6 = 0; var6 < var13.tagCount(); ++var6) {
					final short var7 = var13.getCompoundTagAt(var6).getShort(
							"id");
					final short var8 = var13.getCompoundTagAt(var6).getShort(
							"lvl");

					if (Enchantment.enchantmentsList[var7] != null) {
						var3.add(Enchantment.enchantmentsList[var7]
								.getTranslatedName(var8));
					}
				}
			}

			if (stackTagCompound.func_150297_b("display", 10)) {
				final NBTTagCompound var15 = stackTagCompound
						.getCompoundTag("display");

				if (var15.func_150297_b("color", 3)) {
					if (p_82840_2_) {
						var3.add("Color: #"
								+ Integer
										.toHexString(var15.getInteger("color"))
										.toUpperCase());
					} else {
						var3.add(EnumChatFormatting.ITALIC
								+ StatCollector.translateToLocal("item.dyed"));
					}
				}

				if (var15.func_150299_b("Lore") == 9) {
					final NBTTagList var17 = var15.getTagList("Lore", 8);

					if (var17.tagCount() > 0) {
						for (int var19 = 0; var19 < var17.tagCount(); ++var19) {
							var3.add(EnumChatFormatting.DARK_PURPLE + ""
									+ EnumChatFormatting.ITALIC
									+ var17.getStringTagAt(var19));
						}
					}
				}
			}
		}

		final Multimap var14 = getAttributeModifiers();

		if (!var14.isEmpty()) {
			var3.add("");
			final Iterator var16 = var14.entries().iterator();

			while (var16.hasNext()) {
				final Entry var18 = (Entry) var16.next();
				final AttributeModifier var20 = (AttributeModifier) var18
						.getValue();
				double var9 = var20.getAmount();

				if (var20.getID() == Item.field_111210_e) {
					var9 += EnchantmentHelper.func_152377_a(this,
							EnumCreatureAttribute.UNDEFINED);
				}

				double var11;

				if (var20.getOperation() != 1 && var20.getOperation() != 2) {
					var11 = var9;
				} else {
					var11 = var9 * 100.0D;
				}

				if (var9 > 0.0D) {
					var3.add(EnumChatFormatting.BLUE
							+ StatCollector.translateToLocalFormatted(
									"attribute.modifier.plus."
											+ var20.getOperation(),
									new Object[] {
											field_111284_a.format(var11),
											StatCollector
													.translateToLocal("attribute.name."
															+ (String) var18
																	.getKey()) }));
				} else if (var9 < 0.0D) {
					var11 *= -1.0D;
					var3.add(EnumChatFormatting.RED
							+ StatCollector.translateToLocalFormatted(
									"attribute.modifier.take."
											+ var20.getOperation(),
									new Object[] {
											field_111284_a.format(var11),
											StatCollector
													.translateToLocal("attribute.name."
															+ (String) var18
																	.getKey()) }));
				}
			}
		}

		if (hasTagCompound() && getTagCompound().getBoolean("Unbreakable")) {
			var3.add(EnumChatFormatting.BLUE
					+ StatCollector.translateToLocal("item.unbreakable"));
		}

		if (p_82840_2_ && isItemDamaged()) {
			var3.add("Durability: "
					+ (getMaxDamage() - getItemDamageForDisplay()) + " / "
					+ getMaxDamage());
		}

		return var3;
	}

	public String getUnlocalizedName() {
		return field_151002_e.getUnlocalizedName(this);
	}

	/**
	 * Returns true if the itemstack has a display name
	 */
	public boolean hasDisplayName() {
		return stackTagCompound == null ? false : !stackTagCompound
				.func_150297_b("display", 10) ? false : stackTagCompound
				.getCompoundTag("display").func_150297_b("Name", 8);
	}

	public boolean hasEffect() {
		return getItem().hasEffect(this);
	}

	/**
	 * Returns true if the ItemStack has an NBTTagCompound. Currently used to
	 * store enchantments.
	 */
	public boolean hasTagCompound() {
		return stackTagCompound != null;
	}

	/**
	 * Calls the corresponding fct in di
	 */
	public void hitEntity(EntityLivingBase p_77961_1_, EntityPlayer p_77961_2_) {
		final boolean var3 = field_151002_e.hitEntity(this, p_77961_1_,
				p_77961_2_);

		if (var3) {
			p_77961_2_
					.addStat(StatList.objectUseStats[Item
							.getIdFromItem(field_151002_e)], 1);
		}
	}

	public boolean interactWithEntity(EntityPlayer p_111282_1_,
			EntityLivingBase p_111282_2_) {
		return field_151002_e.itemInteractionForEntity(this, p_111282_1_,
				p_111282_2_);
	}

	/**
	 * returns true when a damageable item is damaged
	 */
	public boolean isItemDamaged() {
		return isItemStackDamageable() && itemDamage > 0;
	}

	/**
	 * True if it is a tool and has no enchantments to begin with
	 */
	public boolean isItemEnchantable() {
		return !getItem().isItemTool(this) ? false : !isItemEnchanted();
	}

	/**
	 * True if the item has enchantment data
	 */
	public boolean isItemEnchanted() {
		return stackTagCompound != null
				&& stackTagCompound.func_150297_b("ench", 9);
	}

	/**
	 * compares ItemStack argument to the instance ItemStack; returns true if
	 * the Items contained in both ItemStacks are equal
	 */
	public boolean isItemEqual(ItemStack p_77969_1_) {
		return field_151002_e == p_77969_1_.field_151002_e
				&& itemDamage == p_77969_1_.itemDamage;
	}

	/**
	 * true if this itemStack is damageable
	 */
	public boolean isItemStackDamageable() {
		if(field_151002_e.getMaxDamage() == -1) return false;
		return field_151002_e.getMaxDamage() <= 0 ? false : !hasTagCompound()
				|| !getTagCompound().getBoolean("Unbreakable");
	}

	/**
	 * compares ItemStack argument to the instance ItemStack; returns true if
	 * both ItemStacks are equal
	 */
	private boolean isItemStackEqual(ItemStack p_77959_1_) {
		return stackSize != p_77959_1_.stackSize ? false
				: field_151002_e != p_77959_1_.field_151002_e ? false
						: itemDamage != p_77959_1_.itemDamage ? false
								: stackTagCompound == null
										&& p_77959_1_.stackTagCompound != null ? false
										: stackTagCompound == null
												|| stackTagCompound
														.equals(p_77959_1_.stackTagCompound);
	}

	/**
	 * Return whether this stack is on an item frame.
	 */
	public boolean isOnItemFrame() {
		return itemFrame != null;
	}

	/**
	 * Returns true if the ItemStack can hold 2 or more units of the item.
	 */
	public boolean isStackable() {
		return getMaxStackSize() > 1
				&& (!isItemStackDamageable() || !isItemDamaged());
	}

	public void onCrafting(World p_77980_1_, EntityPlayer p_77980_2_,
			int p_77980_3_) {
		p_77980_2_.addStat(
				StatList.objectCraftStats[Item.getIdFromItem(field_151002_e)],
				p_77980_3_);
		field_151002_e.onCreated(this, p_77980_1_, p_77980_2_);
	}

	public ItemStack onFoodEaten(World p_77950_1_, EntityPlayer p_77950_2_) {
		return getItem().onEaten(this, p_77950_1_, p_77950_2_);
	}

	/**
	 * Called when the player releases the use item button. Args: world,
	 * entityplayer, itemInUseCount
	 */
	public void onPlayerStoppedUsing(World p_77974_1_, EntityPlayer p_77974_2_,
			int p_77974_3_) {
		getItem()
				.onPlayerStoppedUsing(this, p_77974_1_, p_77974_2_, p_77974_3_);
	}

	/**
	 * Read the stack fields from a NBT object.
	 */
	public void readFromNBT(NBTTagCompound p_77963_1_) {
		field_151002_e = Item.getItemById(p_77963_1_.getShort("id"));
		stackSize = p_77963_1_.getByte("Count");
		itemDamage = p_77963_1_.getShort("Damage");

		if (itemDamage < 0) {
			itemDamage = 0;
		}

		if (p_77963_1_.func_150297_b("tag", 10)) {
			stackTagCompound = p_77963_1_.getCompoundTag("tag");
		}
	}

	/**
	 * Sets the item damage of the ItemStack.
	 */
	public void setItemDamage(int p_77964_1_) {
		itemDamage = p_77964_1_;

		if (itemDamage < 0) {
			itemDamage = 0;
		}
	}

	/**
	 * Set the item frame this stack is on.
	 */
	public void setItemFrame(EntityItemFrame p_82842_1_) {
		itemFrame = p_82842_1_;
	}

	/**
	 * Set this stack's repair cost.
	 */
	public void setRepairCost(int p_82841_1_) {
		if (!hasTagCompound()) {
			stackTagCompound = new NBTTagCompound();
		}

		stackTagCompound.setInteger("RepairCost", p_82841_1_);
	}

	public ItemStack setStackDisplayName(String p_151001_1_) {
		if (stackTagCompound == null) {
			stackTagCompound = new NBTTagCompound();
		}

		if (!stackTagCompound.func_150297_b("display", 10)) {
			stackTagCompound.setTag("display", new NBTTagCompound());
		}

		stackTagCompound.getCompoundTag("display").setString("Name",
				p_151001_1_);
		return this;
	}

	/**
	 * Assigns a NBTTagCompound to the ItemStack, minecraft validates that only
	 * non-stackable items can have it.
	 */
	public void setTagCompound(NBTTagCompound p_77982_1_) {
		stackTagCompound = p_77982_1_;
	}

	public void setTagInfo(String p_77983_1_, NBTBase p_77983_2_) {
		if (stackTagCompound == null) {
			setTagCompound(new NBTTagCompound());
		}

		stackTagCompound.setTag(p_77983_1_, p_77983_2_);
	}

	/**
	 * Remove the argument from the stack size. Return a new stack object with
	 * argument size.
	 */
	public ItemStack splitStack(int p_77979_1_) {
		final ItemStack var2 = new ItemStack(field_151002_e, p_77979_1_,
				itemDamage);

		if (stackTagCompound != null) {
			var2.stackTagCompound = (NBTTagCompound) stackTagCompound.copy();
		}

		stackSize -= p_77979_1_;
		return var2;
	}

	@Override
	public String toString() {
		return stackSize + "x" + field_151002_e.getUnlocalizedName() + "@"
				+ itemDamage;
	}

	public boolean tryPlaceItemIntoWorld(EntityPlayer p_77943_1_,
			World p_77943_2_, int p_77943_3_, int p_77943_4_, int p_77943_5_,
			int p_77943_6_, float p_77943_7_, float p_77943_8_, float p_77943_9_) {
		final boolean var10 = getItem().onItemUse(this, p_77943_1_, p_77943_2_,
				p_77943_3_, p_77943_4_, p_77943_5_, p_77943_6_, p_77943_7_,
				p_77943_8_, p_77943_9_);

		if (var10) {
			p_77943_1_
					.addStat(StatList.objectUseStats[Item
							.getIdFromItem(field_151002_e)], 1);
		}

		return var10;
	}

	/**
	 * Called each tick as long the ItemStack in on player inventory. Used to
	 * progress the pickup animation and update maps.
	 */
	public void updateAnimation(World p_77945_1_, Entity p_77945_2_,
			int p_77945_3_, boolean p_77945_4_) {
		if (animationsToGo > 0) {
			--animationsToGo;
		}

		field_151002_e.onUpdate(this, p_77945_1_, p_77945_2_, p_77945_3_,
				p_77945_4_);
	}

	/**
	 * Called whenever this item stack is equipped and right clicked. Returns
	 * the new item stack to put in the position where this item is. Args:
	 * world, player
	 */
	public ItemStack useItemRightClick(World p_77957_1_, EntityPlayer p_77957_2_) {
		return getItem().onItemRightClick(this, p_77957_1_, p_77957_2_);
	}

	/**
	 * Write the stack fields to a NBT object. Return the new NBT object.
	 */
	public NBTTagCompound writeToNBT(NBTTagCompound p_77955_1_) {
		p_77955_1_.setShort("id", (short) Item.getIdFromItem(field_151002_e));
		p_77955_1_.setByte("Count", (byte) stackSize);
		p_77955_1_.setShort("Damage", (short) itemDamage);

		if (stackTagCompound != null) {
			p_77955_1_.setTag("tag", stackTagCompound);
		}

		return p_77955_1_;
	}
}
