package net.minecraft.util;

import net.minecraft.entity.EntityLivingBase;

public class CombatEntry {
	private final DamageSource damageSrc;
	private final float field_94564_f;
	private final String field_94566_e;
	private final float field_94568_c;

	public CombatEntry(DamageSource p_i1564_1_, int p_i1564_2_,
			float p_i1564_3_, float p_i1564_4_, String p_i1564_5_,
			float p_i1564_6_) {
		damageSrc = p_i1564_1_;
		field_94568_c = p_i1564_4_;
		field_94566_e = p_i1564_5_;
		field_94564_f = p_i1564_6_;
	}

	public IChatComponent func_151522_h() {
		return getDamageSrc().getEntity() == null ? null : getDamageSrc()
				.getEntity().func_145748_c_();
	}

	public boolean func_94559_f() {
		return damageSrc.getEntity() instanceof EntityLivingBase;
	}

	public float func_94561_i() {
		return damageSrc == DamageSource.outOfWorld ? Float.MAX_VALUE
				: field_94564_f;
	}

	public String func_94562_g() {
		return field_94566_e;
	}

	public float func_94563_c() {
		return field_94568_c;
	}

	/**
	 * Get the DamageSource of the CombatEntry instance.
	 */
	public DamageSource getDamageSrc() {
		return damageSrc;
	}
}
