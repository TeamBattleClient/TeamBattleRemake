package net.minecraft.scoreboard;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface IScoreObjectiveCriteria {
	Map field_96643_a = new HashMap();
	IScoreObjectiveCriteria deathCount = new ScoreDummyCriteria("deathCount");
	IScoreObjectiveCriteria field_96641_b = new ScoreDummyCriteria("dummy");
	IScoreObjectiveCriteria health = new ScoreHealthCriteria("health");
	IScoreObjectiveCriteria playerKillCount = new ScoreDummyCriteria(
			"playerKillCount");
	IScoreObjectiveCriteria totalKillCount = new ScoreDummyCriteria(
			"totalKillCount");

	int func_96635_a(List p_96635_1_);

	String func_96636_a();

	boolean isReadOnly();
}
