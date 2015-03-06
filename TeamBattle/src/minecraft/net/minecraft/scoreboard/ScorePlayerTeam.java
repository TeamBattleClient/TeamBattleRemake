package net.minecraft.scoreboard;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class ScorePlayerTeam extends Team {
	/**
	 * Returns the player name including the color prefixes and suffixes
	 */
	public static String formatPlayerName(Team p_96667_0_, String p_96667_1_) {
		return p_96667_0_ == null ? p_96667_1_ : p_96667_0_
				.func_142053_d(p_96667_1_);
	}

	private boolean allowFriendlyFire = true;

	private boolean canSeeFriendlyInvisibles = true;
	private String colorSuffix = "";
	private final String field_96675_b;
	/** A set of all team member usernames. */
	private final Set membershipSet = new HashSet();
	private String namePrefixSPT = "";
	private String teamNameSPT;

	private final Scoreboard theScoreboard;

	public ScorePlayerTeam(Scoreboard p_i2308_1_, String p_i2308_2_) {
		theScoreboard = p_i2308_1_;
		field_96675_b = p_i2308_2_;
		teamNameSPT = p_i2308_2_;
	}

	@Override
	public String func_142053_d(String p_142053_1_) {
		return getColorPrefix() + p_142053_1_ + getColorSuffix();
	}

	public String func_96669_c() {
		return teamNameSPT;
	}

	@Override
	public boolean func_98297_h() {
		return canSeeFriendlyInvisibles;
	}

	public void func_98298_a(int p_98298_1_) {
		setAllowFriendlyFire((p_98298_1_ & 1) > 0);
		setSeeFriendlyInvisiblesEnabled((p_98298_1_ & 2) > 0);
	}

	public int func_98299_i() {
		int var1 = 0;

		if (getAllowFriendlyFire()) {
			var1 |= 1;
		}

		if (func_98297_h()) {
			var1 |= 2;
		}

		return var1;
	}

	@Override
	public boolean getAllowFriendlyFire() {
		return allowFriendlyFire;
	}

	/**
	 * Returns the color prefix for the player's team name
	 */
	public String getColorPrefix() {
		return namePrefixSPT;
	}

	/**
	 * Returns the color suffix for the player's team name
	 */
	public String getColorSuffix() {
		return colorSuffix;
	}

	public Collection getMembershipCollection() {
		return membershipSet;
	}

	/**
	 * Retrieve the name by which this team is registered in the scoreboard
	 */
	@Override
	public String getRegisteredName() {
		return field_96675_b;
	}

	public void setAllowFriendlyFire(boolean p_96660_1_) {
		allowFriendlyFire = p_96660_1_;
		theScoreboard.func_96538_b(this);
	}

	public void setNamePrefix(String p_96666_1_) {
		if (p_96666_1_ == null)
			throw new IllegalArgumentException("Prefix cannot be null");
		else {
			namePrefixSPT = p_96666_1_;
			theScoreboard.func_96538_b(this);
		}
	}

	public void setNameSuffix(String p_96662_1_) {
		if (p_96662_1_ == null)
			throw new IllegalArgumentException("Suffix cannot be null");
		else {
			colorSuffix = p_96662_1_;
			theScoreboard.func_96538_b(this);
		}
	}

	public void setSeeFriendlyInvisiblesEnabled(boolean p_98300_1_) {
		canSeeFriendlyInvisibles = p_98300_1_;
		theScoreboard.func_96538_b(this);
	}

	public void setTeamName(String p_96664_1_) {
		if (p_96664_1_ == null)
			throw new IllegalArgumentException("Name cannot be null");
		else {
			teamNameSPT = p_96664_1_;
			theScoreboard.func_96538_b(this);
		}
	}
}
