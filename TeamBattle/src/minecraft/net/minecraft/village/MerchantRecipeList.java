package net.minecraft.village;

import java.io.IOException;
import java.util.ArrayList;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.PacketBuffer;

public class MerchantRecipeList extends ArrayList {

	public static MerchantRecipeList func_151390_b(PacketBuffer p_151390_0_)
			throws IOException {
		final MerchantRecipeList var1 = new MerchantRecipeList();
		final int var2 = p_151390_0_.readByte() & 255;

		for (int var3 = 0; var3 < var2; ++var3) {
			final ItemStack var4 = p_151390_0_.readItemStackFromBuffer();
			final ItemStack var5 = p_151390_0_.readItemStackFromBuffer();
			ItemStack var6 = null;

			if (p_151390_0_.readBoolean()) {
				var6 = p_151390_0_.readItemStackFromBuffer();
			}

			final boolean var7 = p_151390_0_.readBoolean();
			final MerchantRecipe var8 = new MerchantRecipe(var4, var6, var5);

			if (var7) {
				var8.func_82785_h();
			}

			var1.add(var8);
		}

		return var1;
	}

	public MerchantRecipeList() {
	}

	public MerchantRecipeList(NBTTagCompound p_i1944_1_) {
		readRecipiesFromTags(p_i1944_1_);
	}

	/**
	 * checks if there is a recipie for the same ingredients already on the
	 * list, and replaces it. otherwise, adds it
	 */
	public void addToListWithCheck(MerchantRecipe p_77205_1_) {
		for (int var2 = 0; var2 < size(); ++var2) {
			final MerchantRecipe var3 = (MerchantRecipe) get(var2);

			if (p_77205_1_.hasSameIDsAs(var3)) {
				if (p_77205_1_.hasSameItemsAs(var3)) {
					set(var2, p_77205_1_);
				}

				return;
			}
		}

		this.add(p_77205_1_);
	}

	/**
	 * can par1,par2 be used to in crafting recipe par3
	 */
	public MerchantRecipe canRecipeBeUsed(ItemStack p_77203_1_,
			ItemStack p_77203_2_, int p_77203_3_) {
		if (p_77203_3_ > 0 && p_77203_3_ < size()) {
			final MerchantRecipe var6 = (MerchantRecipe) get(p_77203_3_);
			return p_77203_1_.getItem() == var6.getItemToBuy().getItem()
					&& (p_77203_2_ == null && !var6.hasSecondItemToBuy() || var6
							.hasSecondItemToBuy()
							&& p_77203_2_ != null
							&& var6.getSecondItemToBuy().getItem() == p_77203_2_
									.getItem())
					&& p_77203_1_.stackSize >= var6.getItemToBuy().stackSize
					&& (!var6.hasSecondItemToBuy() || p_77203_2_.stackSize >= var6
							.getSecondItemToBuy().stackSize) ? var6 : null;
		} else {
			for (int var4 = 0; var4 < size(); ++var4) {
				final MerchantRecipe var5 = (MerchantRecipe) get(var4);

				if (p_77203_1_.getItem() == var5.getItemToBuy().getItem()
						&& p_77203_1_.stackSize >= var5.getItemToBuy().stackSize
						&& (!var5.hasSecondItemToBuy() && p_77203_2_ == null || var5
								.hasSecondItemToBuy()
								&& p_77203_2_ != null
								&& var5.getSecondItemToBuy().getItem() == p_77203_2_
										.getItem()
								&& p_77203_2_.stackSize >= var5
										.getSecondItemToBuy().stackSize))
					return var5;
			}

			return null;
		}
	}

	public void func_151391_a(PacketBuffer p_151391_1_) throws IOException {
		p_151391_1_.writeByte((byte) (size() & 255));

		for (int var2 = 0; var2 < size(); ++var2) {
			final MerchantRecipe var3 = (MerchantRecipe) get(var2);
			p_151391_1_.writeItemStackToBuffer(var3.getItemToBuy());
			p_151391_1_.writeItemStackToBuffer(var3.getItemToSell());
			final ItemStack var4 = var3.getSecondItemToBuy();
			p_151391_1_.writeBoolean(var4 != null);

			if (var4 != null) {
				p_151391_1_.writeItemStackToBuffer(var4);
			}

			p_151391_1_.writeBoolean(var3.isRecipeDisabled());
		}
	}

	public NBTTagCompound getRecipiesAsTags() {
		final NBTTagCompound var1 = new NBTTagCompound();
		final NBTTagList var2 = new NBTTagList();

		for (int var3 = 0; var3 < size(); ++var3) {
			final MerchantRecipe var4 = (MerchantRecipe) get(var3);
			var2.appendTag(var4.writeToTags());
		}

		var1.setTag("Recipes", var2);
		return var1;
	}

	public void readRecipiesFromTags(NBTTagCompound p_77201_1_) {
		final NBTTagList var2 = p_77201_1_.getTagList("Recipes", 10);

		for (int var3 = 0; var3 < var2.tagCount(); ++var3) {
			final NBTTagCompound var4 = var2.getCompoundTagAt(var3);
			this.add(new MerchantRecipe(var4));
		}
	}
}
