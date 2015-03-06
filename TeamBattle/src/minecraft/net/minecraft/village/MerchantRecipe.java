package net.minecraft.village;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class MerchantRecipe {
	/** Item the Villager buys. */
	private ItemStack itemToBuy;

	/** Item the Villager sells. */
	private ItemStack itemToSell;

	/** Maximum times this trade can be used. */
	private int maxTradeUses;

	/** Second Item the Villager buys. */
	private ItemStack secondItemToBuy;

	/**
	 * Saves how much has been tool used when put into to slot to be enchanted.
	 */
	private int toolUses;

	public MerchantRecipe(ItemStack p_i1943_1_, Item p_i1943_2_) {
		this(p_i1943_1_, new ItemStack(p_i1943_2_));
	}

	public MerchantRecipe(ItemStack p_i1942_1_, ItemStack p_i1942_2_) {
		this(p_i1942_1_, (ItemStack) null, p_i1942_2_);
	}

	public MerchantRecipe(ItemStack p_i1941_1_, ItemStack p_i1941_2_,
			ItemStack p_i1941_3_) {
		itemToBuy = p_i1941_1_;
		secondItemToBuy = p_i1941_2_;
		itemToSell = p_i1941_3_;
		maxTradeUses = 7;
	}

	public MerchantRecipe(NBTTagCompound p_i1940_1_) {
		readFromTags(p_i1940_1_);
	}

	public void func_82783_a(int p_82783_1_) {
		maxTradeUses += p_82783_1_;
	}

	public void func_82785_h() {
		toolUses = maxTradeUses;
	}

	/**
	 * Gets the itemToBuy.
	 */
	public ItemStack getItemToBuy() {
		return itemToBuy;
	}

	/**
	 * Gets itemToSell.
	 */
	public ItemStack getItemToSell() {
		return itemToSell;
	}

	/**
	 * Gets secondItemToBuy.
	 */
	public ItemStack getSecondItemToBuy() {
		return secondItemToBuy;
	}

	/**
	 * checks if both the first and second ItemToBuy IDs are the same
	 */
	public boolean hasSameIDsAs(MerchantRecipe p_77393_1_) {
		return itemToBuy.getItem() == p_77393_1_.itemToBuy.getItem()
				&& itemToSell.getItem() == p_77393_1_.itemToSell.getItem() ? secondItemToBuy == null
				&& p_77393_1_.secondItemToBuy == null
				|| secondItemToBuy != null
				&& p_77393_1_.secondItemToBuy != null
				&& secondItemToBuy.getItem() == p_77393_1_.secondItemToBuy
						.getItem()
				: false;
	}

	/**
	 * checks first and second ItemToBuy ID's and count. Calls hasSameIDs
	 */
	public boolean hasSameItemsAs(MerchantRecipe p_77391_1_) {
		return hasSameIDsAs(p_77391_1_)
				&& (itemToBuy.stackSize < p_77391_1_.itemToBuy.stackSize || secondItemToBuy != null
						&& secondItemToBuy.stackSize < p_77391_1_.secondItemToBuy.stackSize);
	}

	/**
	 * Gets if Villager has secondItemToBuy.
	 */
	public boolean hasSecondItemToBuy() {
		return secondItemToBuy != null;
	}

	public void incrementToolUses() {
		++toolUses;
	}

	public boolean isRecipeDisabled() {
		return toolUses >= maxTradeUses;
	}

	public void readFromTags(NBTTagCompound p_77390_1_) {
		final NBTTagCompound var2 = p_77390_1_.getCompoundTag("buy");
		itemToBuy = ItemStack.loadItemStackFromNBT(var2);
		final NBTTagCompound var3 = p_77390_1_.getCompoundTag("sell");
		itemToSell = ItemStack.loadItemStackFromNBT(var3);

		if (p_77390_1_.func_150297_b("buyB", 10)) {
			secondItemToBuy = ItemStack.loadItemStackFromNBT(p_77390_1_
					.getCompoundTag("buyB"));
		}

		if (p_77390_1_.func_150297_b("uses", 99)) {
			toolUses = p_77390_1_.getInteger("uses");
		}

		if (p_77390_1_.func_150297_b("maxUses", 99)) {
			maxTradeUses = p_77390_1_.getInteger("maxUses");
		} else {
			maxTradeUses = 7;
		}
	}

	public NBTTagCompound writeToTags() {
		final NBTTagCompound var1 = new NBTTagCompound();
		var1.setTag("buy", itemToBuy.writeToNBT(new NBTTagCompound()));
		var1.setTag("sell", itemToSell.writeToNBT(new NBTTagCompound()));

		if (secondItemToBuy != null) {
			var1.setTag("buyB",
					secondItemToBuy.writeToNBT(new NBTTagCompound()));
		}

		var1.setInteger("uses", toolUses);
		var1.setInteger("maxUses", maxTradeUses);
		return var1;
	}
}
