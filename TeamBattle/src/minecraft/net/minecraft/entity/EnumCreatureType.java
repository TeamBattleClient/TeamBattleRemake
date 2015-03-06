package net.minecraft.entity;

import net.minecraft.block.material.Material;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.EntityAmbientCreature;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityWaterMob;

public enum EnumCreatureType {
	ambient(EntityAmbientCreature.class, 15, Material.air, true, false), creature(
			EntityAnimal.class, 10, Material.air, true, true), monster(
			IMob.class, 70, Material.air, false, false), waterCreature(
			EntityWaterMob.class, 5, Material.water, true, false);

	/**
	 * The root class of creatures associated with this EnumCreatureType (IMobs
	 * for aggressive creatures, EntityAnimals for friendly ones)
	 */
	private final Class creatureClass;
	private final Material creatureMaterial;
	/** Whether this creature type is an animal. */
	private final boolean isAnimal;

	/** A flag indicating whether this creature type is peaceful. */
	private final boolean isPeacefulCreature;

	private final int maxNumberOfCreature;

	private EnumCreatureType(Class p_i1596_3_, int p_i1596_4_,
			Material p_i1596_5_, boolean p_i1596_6_, boolean p_i1596_7_) {
		creatureClass = p_i1596_3_;
		maxNumberOfCreature = p_i1596_4_;
		creatureMaterial = p_i1596_5_;
		isPeacefulCreature = p_i1596_6_;
		isAnimal = p_i1596_7_;
	}

	/**
	 * Return whether this creature type is an animal.
	 */
	public boolean getAnimal() {
		return isAnimal;
	}

	public Class getCreatureClass() {
		return creatureClass;
	}

	public Material getCreatureMaterial() {
		return creatureMaterial;
	}

	public int getMaxNumberOfCreature() {
		return maxNumberOfCreature;
	}

	/**
	 * Gets whether or not this creature type is peaceful.
	 */
	public boolean getPeacefulCreature() {
		return isPeacefulCreature;
	}
}
