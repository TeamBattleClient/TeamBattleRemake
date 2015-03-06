package net.minecraft.util;

public class Util {

	public static enum EnumOS {
		LINUX("LINUX", 0), OSX("OSX", 3), SOLARIS("SOLARIS", 1), UNKNOWN(
				"UNKNOWN", 4), WINDOWS("WINDOWS", 2);

		private EnumOS(String p_i1357_1_, int p_i1357_2_) {
		}
	}

	public static Util.EnumOS getOSType() {
		final String var0 = System.getProperty("os.name").toLowerCase();
		return var0.contains("win") ? Util.EnumOS.WINDOWS : var0
				.contains("mac") ? Util.EnumOS.OSX
				: var0.contains("solaris") ? Util.EnumOS.SOLARIS : var0
						.contains("sunos") ? Util.EnumOS.SOLARIS : var0
						.contains("linux") ? Util.EnumOS.LINUX : var0
						.contains("unix") ? Util.EnumOS.LINUX
						: Util.EnumOS.UNKNOWN;
	}
}
