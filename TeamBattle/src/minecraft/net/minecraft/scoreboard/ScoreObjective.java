package net.minecraft.scoreboard;

public class ScoreObjective {
	private String displayName;
	private final String name;

	/** The ScoreObjectiveCriteria for this objetive */
	private final IScoreObjectiveCriteria objectiveCriteria;
	private final Scoreboard theScoreboard;

	public ScoreObjective(Scoreboard p_i2307_1_, String p_i2307_2_,
			IScoreObjectiveCriteria p_i2307_3_) {
		theScoreboard = p_i2307_1_;
		name = p_i2307_2_;
		objectiveCriteria = p_i2307_3_;
		displayName = p_i2307_2_;
	}

	public IScoreObjectiveCriteria getCriteria() {
		return objectiveCriteria;
	}

	public String getDisplayName() {
		return displayName;
	}

	public String getName() {
		return name;
	}

	public Scoreboard getScoreboard() {
		return theScoreboard;
	}

	public void setDisplayName(String p_96681_1_) {
		displayName = p_96681_1_;
		theScoreboard.func_96532_b(this);
	}
}
