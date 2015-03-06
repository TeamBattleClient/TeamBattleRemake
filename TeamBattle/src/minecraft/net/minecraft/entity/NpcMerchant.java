package net.minecraft.entity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryMerchant;
import net.minecraft.item.ItemStack;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;

public class NpcMerchant implements IMerchant {
	/** This merchant's current player customer. */
	private final EntityPlayer customer;

	/** The MerchantRecipeList instance. */
	private MerchantRecipeList recipeList;

	/** Instance of Merchants Inventory. */
	private final InventoryMerchant theMerchantInventory;

	public NpcMerchant(EntityPlayer p_i1746_1_) {
		customer = p_i1746_1_;
		theMerchantInventory = new InventoryMerchant(p_i1746_1_, this);
	}

	@Override
	public void func_110297_a_(ItemStack p_110297_1_) {
	}

	@Override
	public EntityPlayer getCustomer() {
		return customer;
	}

	@Override
	public MerchantRecipeList getRecipes(EntityPlayer p_70934_1_) {
		return recipeList;
	}

	@Override
	public void setCustomer(EntityPlayer p_70932_1_) {
	}

	@Override
	public void setRecipes(MerchantRecipeList p_70930_1_) {
		recipeList = p_70930_1_;
	}

	@Override
	public void useRecipe(MerchantRecipe p_70933_1_) {
	}
}
