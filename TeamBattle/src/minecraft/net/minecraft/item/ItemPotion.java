package net.minecraft.item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.init.Items;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionHelper;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import com.google.common.collect.HashMultimap;

public class ItemPotion extends Item {
	private static final Map field_77835_b = new LinkedHashMap();

	public static IIcon func_94589_d(String p_94589_0_) {
		return p_94589_0_.equals("bottle_drinkable") ? Items.potionitem.field_94590_d
				: p_94589_0_.equals("bottle_splash") ? Items.potionitem.field_94591_c
						: p_94589_0_.equals("overlay") ? Items.potionitem.field_94592_ct
								: null;
	}

	/**
	 * returns wether or not a potion is a throwable splash potion based on
	 * damage value
	 */
	public static boolean isSplash(int p_77831_0_) {
		return (p_77831_0_ & 16384) != 0;
	}

	/**
	 * Contains a map from integers to the list of potion effects that potions
	 * with that damage value confer (to prevent recalculating it).
	 */
	private final HashMap effectCache = new HashMap();
	private IIcon field_94590_d;

	private IIcon field_94591_c;

	private IIcon field_94592_ct;

	public ItemPotion() {
		setMaxStackSize(1);
		setHasSubtypes(true);
		setMaxDamage(0);
		setCreativeTab(CreativeTabs.tabBrewing);
	}

	/**
	 * allows items to add custom lines of information to the mouseover
	 * description
	 */
	@Override
	public void addInformation(ItemStack p_77624_1_, EntityPlayer p_77624_2_,
			List p_77624_3_, boolean p_77624_4_) {
		if (p_77624_1_.getItemDamage() != 0) {
			final List var5 = Items.potionitem.getEffects(p_77624_1_);
			final HashMultimap var6 = HashMultimap.create();
			Iterator var16;

			if (var5 != null && !var5.isEmpty()) {
				var16 = var5.iterator();

				while (var16.hasNext()) {
					final PotionEffect var8 = (PotionEffect) var16.next();
					String var9 = StatCollector.translateToLocal(
							var8.getEffectName()).trim();
					final Potion var10 = Potion.potionTypes[var8.getPotionID()];
					final Map var11 = var10.func_111186_k();

					if (var11 != null && var11.size() > 0) {
						final Iterator var12 = var11.entrySet().iterator();

						while (var12.hasNext()) {
							final Entry var13 = (Entry) var12.next();
							final AttributeModifier var14 = (AttributeModifier) var13
									.getValue();
							final AttributeModifier var15 = new AttributeModifier(
									var14.getName(), var10.func_111183_a(
											var8.getAmplifier(), var14),
									var14.getOperation());
							var6.put(((IAttribute) var13.getKey())
									.getAttributeUnlocalizedName(), var15);
						}
					}

					if (var8.getAmplifier() > 0) {
						var9 = var9
								+ " "
								+ StatCollector
										.translateToLocal(
												"potion.potency."
														+ var8.getAmplifier())
										.trim();
					}

					if (var8.getDuration() > 20) {
						var9 = var9 + " (" + Potion.getDurationString(var8)
								+ ")";
					}

					if (var10.isBadEffect()) {
						p_77624_3_.add(EnumChatFormatting.RED + var9);
					} else {
						p_77624_3_.add(EnumChatFormatting.GRAY + var9);
					}
				}
			} else {
				final String var7 = StatCollector.translateToLocal(
						"potion.empty").trim();
				p_77624_3_.add(EnumChatFormatting.GRAY + var7);
			}

			if (!var6.isEmpty()) {
				p_77624_3_.add("");
				p_77624_3_.add(EnumChatFormatting.DARK_PURPLE
						+ StatCollector
								.translateToLocal("potion.effects.whenDrank"));
				var16 = var6.entries().iterator();

				while (var16.hasNext()) {
					final Entry var17 = (Entry) var16.next();
					final AttributeModifier var18 = (AttributeModifier) var17
							.getValue();
					final double var19 = var18.getAmount();
					double var20;

					if (var18.getOperation() != 1 && var18.getOperation() != 2) {
						var20 = var18.getAmount();
					} else {
						var20 = var18.getAmount() * 100.0D;
					}

					if (var19 > 0.0D) {
						p_77624_3_
								.add(EnumChatFormatting.BLUE
										+ StatCollector.translateToLocalFormatted(
												"attribute.modifier.plus."
														+ var18.getOperation(),
												new Object[] {
														ItemStack.field_111284_a
																.format(var20),
														StatCollector
																.translateToLocal("attribute.name."
																		+ (String) var17
																				.getKey()) }));
					} else if (var19 < 0.0D) {
						var20 *= -1.0D;
						p_77624_3_
								.add(EnumChatFormatting.RED
										+ StatCollector.translateToLocalFormatted(
												"attribute.modifier.take."
														+ var18.getOperation(),
												new Object[] {
														ItemStack.field_111284_a
																.format(var20),
														StatCollector
																.translateToLocal("attribute.name."
																		+ (String) var17
																				.getKey()) }));
					}
				}
			}
		}
	}

	public int getColorFromDamage(int p_77620_1_) {
		return PotionHelper.func_77915_a(p_77620_1_, false);
	}

	@Override
	public int getColorFromItemStack(ItemStack p_82790_1_, int p_82790_2_) {
		return p_82790_2_ > 0 ? 16777215 : getColorFromDamage(p_82790_1_
				.getItemDamage());
	}

	/**
	 * Returns a list of effects for the specified potion damage value.
	 */
	public List getEffects(int p_77834_1_) {
		List var2 = (List) effectCache.get(Integer.valueOf(p_77834_1_));

		if (var2 == null) {
			var2 = PotionHelper.getPotionEffects(p_77834_1_, false);
			effectCache.put(Integer.valueOf(p_77834_1_), var2);
		}

		return var2;
	}

	/**
	 * Returns a list of potion effects for the specified itemstack.
	 */
	public List getEffects(ItemStack p_77832_1_) {
		if (p_77832_1_.hasTagCompound()
				&& p_77832_1_.getTagCompound().func_150297_b(
						"CustomPotionEffects", 9)) {
			final ArrayList var7 = new ArrayList();
			final NBTTagList var3 = p_77832_1_.getTagCompound().getTagList(
					"CustomPotionEffects", 10);

			for (int var4 = 0; var4 < var3.tagCount(); ++var4) {
				final NBTTagCompound var5 = var3.getCompoundTagAt(var4);
				final PotionEffect var6 = PotionEffect
						.readCustomPotionEffectFromNBT(var5);

				if (var6 != null) {
					var7.add(var6);
				}
			}

			return var7;
		} else {
			List var2 = (List) effectCache.get(Integer.valueOf(p_77832_1_
					.getItemDamage()));

			if (var2 == null) {
				var2 = PotionHelper.getPotionEffects(
						p_77832_1_.getItemDamage(), false);
				effectCache.put(Integer.valueOf(p_77832_1_.getItemDamage()),
						var2);
			}

			return var2;
		}
	}

	/**
	 * Gets an icon index based on an item's damage value
	 */
	@Override
	public IIcon getIconFromDamage(int p_77617_1_) {
		return isSplash(p_77617_1_) ? field_94591_c : field_94590_d;
	}

	/**
	 * Gets an icon index based on an item's damage value and the given render
	 * pass
	 */
	@Override
	public IIcon getIconFromDamageForRenderPass(int p_77618_1_, int p_77618_2_) {
		return p_77618_2_ == 0 ? field_94592_ct : super
				.getIconFromDamageForRenderPass(p_77618_1_, p_77618_2_);
	}

	@Override
	public String getItemStackDisplayName(ItemStack p_77653_1_) {
		if (p_77653_1_.getItemDamage() == 0)
			return StatCollector.translateToLocal("item.emptyPotion.name")
					.trim();
		else {
			String var2 = "";

			if (isSplash(p_77653_1_.getItemDamage())) {
				var2 = StatCollector.translateToLocal("potion.prefix.grenade")
						.trim() + " ";
			}

			final List var3 = Items.potionitem.getEffects(p_77653_1_);
			String var4;

			if (var3 != null && !var3.isEmpty()) {
				var4 = ((PotionEffect) var3.get(0)).getEffectName();
				var4 = var4 + ".postfix";
				return var2 + StatCollector.translateToLocal(var4).trim();
			} else {
				var4 = PotionHelper.func_77905_c(p_77653_1_.getItemDamage());
				return StatCollector.translateToLocal(var4).trim() + " "
						+ super.getItemStackDisplayName(p_77653_1_);
			}
		}
	}

	/**
	 * returns the action that specifies what animation to play when the items
	 * is being used
	 */
	@Override
	public EnumAction getItemUseAction(ItemStack p_77661_1_) {
		return EnumAction.drink;
	}

	/**
	 * How long it takes to use or consume an item
	 */
	@Override
	public int getMaxItemUseDuration(ItemStack p_77626_1_) {
		return 32;
	}

	/**
	 * This returns the sub items
	 */
	@Override
	public void getSubItems(Item p_150895_1_, CreativeTabs p_150895_2_,
			List p_150895_3_) {
		super.getSubItems(p_150895_1_, p_150895_2_, p_150895_3_);
		int var5;

		if (field_77835_b.isEmpty()) {
			for (int var4 = 0; var4 <= 15; ++var4) {
				for (var5 = 0; var5 <= 1; ++var5) {
					int var6;

					if (var5 == 0) {
						var6 = var4 | 8192;
					} else {
						var6 = var4 | 16384;
					}

					for (int var7 = 0; var7 <= 2; ++var7) {
						int var8 = var6;

						if (var7 != 0) {
							if (var7 == 1) {
								var8 = var6 | 32;
							} else if (var7 == 2) {
								var8 = var6 | 64;
							}
						}

						final List var9 = PotionHelper.getPotionEffects(var8,
								false);

						if (var9 != null && !var9.isEmpty()) {
							field_77835_b.put(var9, Integer.valueOf(var8));
						}
					}
				}
			}
		}

		final Iterator var10 = field_77835_b.values().iterator();

		while (var10.hasNext()) {
			var5 = ((Integer) var10.next()).intValue();
			p_150895_3_.add(new ItemStack(p_150895_1_, 1, var5));
		}
	}

	@Override
	public boolean hasEffect(ItemStack p_77636_1_) {
		final List var2 = this.getEffects(p_77636_1_);
		return var2 != null && !var2.isEmpty();
	}

	public boolean isEffectInstant(int p_77833_1_) {
		final List var2 = this.getEffects(p_77833_1_);

		if (var2 != null && !var2.isEmpty()) {
			final Iterator var3 = var2.iterator();
			PotionEffect var4;

			do {
				if (!var3.hasNext())
					return false;

				var4 = (PotionEffect) var3.next();
			} while (!Potion.potionTypes[var4.getPotionID()].isInstant());

			return true;
		} else
			return false;
	}

	@Override
	public ItemStack onEaten(ItemStack p_77654_1_, World p_77654_2_,
			EntityPlayer p_77654_3_) {
		if (!p_77654_3_.capabilities.isCreativeMode) {
			--p_77654_1_.stackSize;
		}

		if (!p_77654_2_.isClient) {
			final List var4 = this.getEffects(p_77654_1_);

			if (var4 != null) {
				final Iterator var5 = var4.iterator();

				while (var5.hasNext()) {
					final PotionEffect var6 = (PotionEffect) var5.next();
					p_77654_3_.addPotionEffect(new PotionEffect(var6));
				}
			}
		}

		if (!p_77654_3_.capabilities.isCreativeMode) {
			if (p_77654_1_.stackSize <= 0)
				return new ItemStack(Items.glass_bottle);

			p_77654_3_.inventory.addItemStackToInventory(new ItemStack(
					Items.glass_bottle));
		}

		return p_77654_1_;
	}

	/**
	 * Called whenever this item is equipped and the right mouse button is
	 * pressed. Args: itemStack, world, entityPlayer
	 */
	@Override
	public ItemStack onItemRightClick(ItemStack p_77659_1_, World p_77659_2_,
			EntityPlayer p_77659_3_) {
		if (isSplash(p_77659_1_.getItemDamage())) {
			if (!p_77659_3_.capabilities.isCreativeMode) {
				--p_77659_1_.stackSize;
			}

			p_77659_2_.playSoundAtEntity(p_77659_3_, "random.bow", 0.5F,
					0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));

			if (!p_77659_2_.isClient) {
				p_77659_2_.spawnEntityInWorld(new EntityPotion(p_77659_2_,
						p_77659_3_, p_77659_1_));
			}

			return p_77659_1_;
		} else {
			p_77659_3_.setItemInUse(p_77659_1_,
					getMaxItemUseDuration(p_77659_1_));
			return p_77659_1_;
		}
	}

	/**
	 * Callback for item usage. If the item does something special on right
	 * clicking, he will have one of those. Return True if something happen and
	 * false if it don't. This is for ITEMS, not BLOCKS
	 */
	@Override
	public boolean onItemUse(ItemStack p_77648_1_, EntityPlayer p_77648_2_,
			World p_77648_3_, int p_77648_4_, int p_77648_5_, int p_77648_6_,
			int p_77648_7_, float p_77648_8_, float p_77648_9_,
			float p_77648_10_) {
		return false;
	}

	@Override
	public void registerIcons(IIconRegister p_94581_1_) {
		field_94590_d = p_94581_1_.registerIcon(getIconString() + "_"
				+ "bottle_drinkable");
		field_94591_c = p_94581_1_.registerIcon(getIconString() + "_"
				+ "bottle_splash");
		field_94592_ct = p_94581_1_.registerIcon(getIconString() + "_"
				+ "overlay");
	}

	@Override
	public boolean requiresMultipleRenderPasses() {
		return true;
	}
}
