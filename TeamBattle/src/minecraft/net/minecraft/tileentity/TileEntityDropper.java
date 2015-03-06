package net.minecraft.tileentity;

public class TileEntityDropper extends TileEntityDispenser {

	/**
	 * Returns the name of the inventory
	 */
	@Override
	public String getInventoryName() {
		return isInventoryNameLocalized() ? field_146020_a
				: "container.dropper";
	}
}
