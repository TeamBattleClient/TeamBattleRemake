package net.minecraft.scoreboard;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Scoreboard {
	/**
	 * Returns 'list' for 0, 'sidebar' for 1, 'belowName for 2, otherwise null.
	 */
	public static String getObjectiveDisplaySlot(int p_96517_0_) {
		switch (p_96517_0_) {
		case 0:
			return "list";

		case 1:
			return "sidebar";

		case 2:
			return "belowName";

		default:
			return null;
		}
	}

	/**
	 * Returns 0 for (case-insensitive) 'list', 1 for 'sidebar', 2 for
	 * 'belowName', otherwise -1.
	 */
	public static int getObjectiveDisplaySlotNumber(String p_96537_0_) {
		return p_96537_0_.equalsIgnoreCase("list") ? 0 : p_96537_0_
				.equalsIgnoreCase("sidebar") ? 1 : p_96537_0_
				.equalsIgnoreCase("belowName") ? 2 : -1;
	}

	private final ScoreObjective[] field_96541_d = new ScoreObjective[3];
	private final Map field_96544_c = new HashMap();

	private final Map scoreObjectiveCriterias = new HashMap();

	/** Map of objective names to ScoreObjective objects. */
	private final Map scoreObjectives = new HashMap();

	/** Map of usernames to ScorePlayerTeam objects. */
	private final Map teamMemberships = new HashMap();

	/** Map of teamnames to ScorePlayerTeam instances */
	private final Map teams = new HashMap();

	public ScoreObjective addScoreObjective(String p_96535_1_,
			IScoreObjectiveCriteria p_96535_2_) {
		ScoreObjective var3 = getObjective(p_96535_1_);

		if (var3 != null)
			throw new IllegalArgumentException("An objective with the name \'"
					+ p_96535_1_ + "\' already exists!");
		else {
			var3 = new ScoreObjective(this, p_96535_1_, p_96535_2_);
			Object var4 = scoreObjectiveCriterias.get(p_96535_2_);

			if (var4 == null) {
				var4 = new ArrayList();
				scoreObjectiveCriterias.put(p_96535_2_, var4);
			}

			((List) var4).add(var3);
			scoreObjectives.put(p_96535_1_, var3);
			func_96522_a(var3);
			return var3;
		}
	}

	/**
	 * Verifies that the given name doesn't already refer to an existing team,
	 * creates it otherwise and broadcasts the addition to all players
	 */
	public ScorePlayerTeam createTeam(String p_96527_1_) {
		ScorePlayerTeam var2 = getTeam(p_96527_1_);

		if (var2 != null)
			throw new IllegalArgumentException("A team with the name \'"
					+ p_96527_1_ + "\' already exists!");
		else {
			var2 = new ScorePlayerTeam(this, p_96527_1_);
			teams.put(p_96527_1_, var2);
			func_96523_a(var2);
			return var2;
		}
	}

	public boolean func_151392_a(String p_151392_1_, String p_151392_2_) {
		if (!teams.containsKey(p_151392_2_))
			return false;
		else {
			final ScorePlayerTeam var3 = getTeam(p_151392_2_);

			if (getPlayersTeam(p_151392_1_) != null) {
				func_96524_g(p_151392_1_);
			}

			teamMemberships.put(p_151392_1_, var3);
			var3.getMembershipCollection().add(p_151392_1_);
			return true;
		}
	}

	public Map func_96510_d(String p_96510_1_) {
		Object var2 = field_96544_c.get(p_96510_1_);

		if (var2 == null) {
			var2 = new HashMap();
		}

		return (Map) var2;
	}

	public void func_96513_c(ScorePlayerTeam p_96513_1_) {
	}

	public void func_96515_c(String p_96515_1_) {
		final Map var2 = (Map) field_96544_c.remove(p_96515_1_);

		if (var2 != null) {
			func_96516_a(p_96515_1_);
		}
	}

	public void func_96516_a(String p_96516_1_) {
	}

	public void func_96519_k(ScoreObjective p_96519_1_) {
		scoreObjectives.remove(p_96519_1_.getName());

		for (int var2 = 0; var2 < 3; ++var2) {
			if (func_96539_a(var2) == p_96519_1_) {
				func_96530_a(var2, (ScoreObjective) null);
			}
		}

		final List var5 = (List) scoreObjectiveCriterias.get(p_96519_1_
				.getCriteria());

		if (var5 != null) {
			var5.remove(p_96519_1_);
		}

		final Iterator var3 = field_96544_c.values().iterator();

		while (var3.hasNext()) {
			final Map var4 = (Map) var3.next();
			var4.remove(p_96519_1_);
		}

		func_96533_c(p_96519_1_);
	}

	public Collection func_96520_a(IScoreObjectiveCriteria p_96520_1_) {
		final Collection var2 = (Collection) scoreObjectiveCriterias
				.get(p_96520_1_);
		return var2 == null ? new ArrayList() : new ArrayList(var2);
	}

	public void func_96522_a(ScoreObjective p_96522_1_) {
	}

	public void func_96523_a(ScorePlayerTeam p_96523_1_) {
	}

	public boolean func_96524_g(String p_96524_1_) {
		final ScorePlayerTeam var2 = getPlayersTeam(p_96524_1_);

		if (var2 != null) {
			removePlayerFromTeam(p_96524_1_, var2);
			return true;
		} else
			return false;
	}

	public Collection func_96528_e() {
		final Collection var1 = field_96544_c.values();
		final ArrayList var2 = new ArrayList();
		final Iterator var3 = var1.iterator();

		while (var3.hasNext()) {
			final Map var4 = (Map) var3.next();
			var2.addAll(var4.values());
		}

		return var2;
	}

	public Score func_96529_a(String p_96529_1_, ScoreObjective p_96529_2_) {
		Object var3 = field_96544_c.get(p_96529_1_);

		if (var3 == null) {
			var3 = new HashMap();
			field_96544_c.put(p_96529_1_, var3);
		}

		Score var4 = (Score) ((Map) var3).get(p_96529_2_);

		if (var4 == null) {
			var4 = new Score(this, p_96529_2_, p_96529_1_);
			((Map) var3).put(p_96529_2_, var4);
		}

		return var4;
	}

	public void func_96530_a(int p_96530_1_, ScoreObjective p_96530_2_) {
		field_96541_d[p_96530_1_] = p_96530_2_;
	}

	public void func_96532_b(ScoreObjective p_96532_1_) {
	}

	public void func_96533_c(ScoreObjective p_96533_1_) {
	}

	public Collection func_96534_i(ScoreObjective p_96534_1_) {
		final ArrayList var2 = new ArrayList();
		final Iterator var3 = field_96544_c.values().iterator();

		while (var3.hasNext()) {
			final Map var4 = (Map) var3.next();
			final Score var5 = (Score) var4.get(p_96534_1_);

			if (var5 != null) {
				var2.add(var5);
			}
		}

		Collections.sort(var2, Score.field_96658_a);
		return var2;
	}

	public void func_96536_a(Score p_96536_1_) {
	}

	public void func_96538_b(ScorePlayerTeam p_96538_1_) {
	}

	public ScoreObjective func_96539_a(int p_96539_1_) {
		return field_96541_d[p_96539_1_];
	}

	/**
	 * Returns a ScoreObjective for the objective name
	 */
	public ScoreObjective getObjective(String p_96518_1_) {
		return (ScoreObjective) scoreObjectives.get(p_96518_1_);
	}

	public Collection getObjectiveNames() {
		return field_96544_c.keySet();
	}

	/**
	 * Gets the ScorePlayerTeam object for the given username.
	 */
	public ScorePlayerTeam getPlayersTeam(String p_96509_1_) {
		return (ScorePlayerTeam) teamMemberships.get(p_96509_1_);
	}

	public Collection getScoreObjectives() {
		return scoreObjectives.values();
	}

	/**
	 * Retrieve the ScorePlayerTeam instance identified by the passed team name
	 */
	public ScorePlayerTeam getTeam(String p_96508_1_) {
		return (ScorePlayerTeam) teams.get(p_96508_1_);
	}

	/**
	 * Retrieve all registered ScorePlayerTeam names
	 */
	public Collection getTeamNames() {
		return teams.keySet();
	}

	/**
	 * Retrieve all registered ScorePlayerTeam instances
	 */
	public Collection getTeams() {
		return teams.values();
	}

	/**
	 * Removes the given username from the given ScorePlayerTeam. If the player
	 * is not on the team then an IllegalStateException is thrown.
	 */
	public void removePlayerFromTeam(String p_96512_1_,
			ScorePlayerTeam p_96512_2_) {
		if (getPlayersTeam(p_96512_1_) != p_96512_2_)
			throw new IllegalStateException(
					"Player is either on another team or not on any team. Cannot remove from team \'"
							+ p_96512_2_.getRegisteredName() + "\'.");
		else {
			teamMemberships.remove(p_96512_1_);
			p_96512_2_.getMembershipCollection().remove(p_96512_1_);
		}
	}

	/**
	 * Removes the team from the scoreboard, updates all player memberships and
	 * broadcasts the deletion to all players
	 */
	public void removeTeam(ScorePlayerTeam p_96511_1_) {
		teams.remove(p_96511_1_.getRegisteredName());
		final Iterator var2 = p_96511_1_.getMembershipCollection().iterator();

		while (var2.hasNext()) {
			final String var3 = (String) var2.next();
			teamMemberships.remove(var3);
		}

		func_96513_c(p_96511_1_);
	}
}
