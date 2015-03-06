package net.minecraft.block.material;

public class MaterialPortal extends Material {

	public MaterialPortal(MapColor p_i2118_1_) {
		super(p_i2118_1_);
	}

	/**
	 * Returns if this material is considered solid or not
	 */
	@Override
	public boolean blocksMovement() {
		return false;
	}

	/**
	 * Will prevent grass from growing on dirt underneath and kill any grass
	 * below it if it returns true
	 */
	@Override
	public boolean getCanBlockGrass() {
		return false;
	}

	@Override
	public boolean isSolid() {
		return false;
	}
}
