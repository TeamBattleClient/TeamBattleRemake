package net.minecraft.world;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;

import net.minecraft.nbt.NBTTagCompound;

public class GameRules {
	static class Value {
		private boolean valueBoolean;
		private double valueDouble;
		private int valueInteger;
		private String valueString;

		public Value(String par1Str) {
			setValue(par1Str);
		}

		public boolean getGameRuleBooleanValue() {
			return valueBoolean;
		}

		public String getGameRuleStringValue() {
			return valueString;
		}

		public void setValue(String par1Str) {
			valueString = par1Str;

			if (par1Str != null) {
				if (par1Str.equals("false")) {
					valueBoolean = false;
					return;
				}

				if (par1Str.equals("true")) {
					valueBoolean = true;
					return;
				}
			}

			valueBoolean = Boolean.parseBoolean(par1Str);

			try {
				valueInteger = Integer.parseInt(par1Str);
			} catch (final NumberFormatException var4) {
				;
			}

			try {
				valueDouble = Double.parseDouble(par1Str);
			} catch (final NumberFormatException var3) {
				;
			}
		}
	}

	private final TreeMap theGameRules = new TreeMap();

	public GameRules() {
		addGameRule("doFireTick", "true");
		addGameRule("mobGriefing", "true");
		addGameRule("keepInventory", "false");
		addGameRule("doMobSpawning", "true");
		addGameRule("doMobLoot", "true");
		addGameRule("doTileDrops", "true");
		addGameRule("commandBlockOutput", "true");
		addGameRule("naturalRegeneration", "true");
		addGameRule("doDaylightCycle", "true");
	}

	/**
	 * Define a game rule and its default value.
	 */
	public void addGameRule(String par1Str, String par2Str) {
		theGameRules.put(par1Str, new GameRules.Value(par2Str));
	}

	/**
	 * Gets the boolean Game Rule value.
	 */
	public boolean getGameRuleBooleanValue(String par1Str) {
		final GameRules.Value var2 = (GameRules.Value) theGameRules
				.get(par1Str);
		return var2 != null ? var2.getGameRuleBooleanValue() : false;
	}

	/**
	 * Gets the string Game Rule value.
	 */
	public String getGameRuleStringValue(String par1Str) {
		final GameRules.Value var2 = (GameRules.Value) theGameRules
				.get(par1Str);
		return var2 != null ? var2.getGameRuleStringValue() : "";
	}

	/**
	 * Return the defined game rules.
	 */
	public String[] getRules() {
		return (String[]) theGameRules.keySet().toArray(new String[0]);
	}

	/**
	 * Return whether the specified game rule is defined.
	 */
	public boolean hasRule(String par1Str) {
		return theGameRules.containsKey(par1Str);
	}

	/**
	 * Set defined game rules from NBT.
	 */
	public void readGameRulesFromNBT(NBTTagCompound par1NBTTagCompound) {
		final Set var2 = par1NBTTagCompound.func_150296_c();
		final Iterator var3 = var2.iterator();

		while (var3.hasNext()) {
			final String var4 = (String) var3.next();
			final String var6 = par1NBTTagCompound.getString(var4);
			setOrCreateGameRule(var4, var6);
		}
	}

	public void setOrCreateGameRule(String par1Str, String par2Str) {
		final GameRules.Value var3 = (GameRules.Value) theGameRules
				.get(par1Str);

		if (var3 != null) {
			var3.setValue(par2Str);
		} else {
			addGameRule(par1Str, par2Str);
		}
	}

	/**
	 * Return the defined game rules as NBT.
	 */
	public NBTTagCompound writeGameRulesToNBT() {
		final NBTTagCompound var1 = new NBTTagCompound();
		final Iterator var2 = theGameRules.keySet().iterator();

		while (var2.hasNext()) {
			final String var3 = (String) var2.next();
			final GameRules.Value var4 = (GameRules.Value) theGameRules
					.get(var3);
			var1.setString(var3, var4.getGameRuleStringValue());
		}

		return var1;
	}
}
