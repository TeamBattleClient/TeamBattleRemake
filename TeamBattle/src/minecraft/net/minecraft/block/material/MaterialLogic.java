package net.minecraft.block.material;

public class MaterialLogic extends Material {

	public MaterialLogic(MapColor p_i2112_1_) {
		super(p_i2112_1_);
		setAdventureModeExempt();
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
