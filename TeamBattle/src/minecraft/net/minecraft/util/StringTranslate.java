package net.minecraft.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.IllegalFormatException;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;

import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;

public class StringTranslate {
	/**
	 * A Splitter that splits a string on the first "=". For example, "a=b=c"
	 * would split into ["a", "b=c"].
	 */
	private static final Splitter equalSignSplitter = Splitter.on('=').limit(2);
	/**
	 * Pattern that matches numeric variable placeholders in a resource string,
	 * such as "%d", "%3$d", "%.2f"
	 */
	private static final Pattern numericVariablePattern = Pattern
			.compile("%(\\d+\\$)?[\\d\\.]*[df]");

	/** Is the private singleton instance of StringTranslate. */
	private static StringTranslate instance = new StringTranslate();

	/**
	 * Return the StringTranslate singleton instance
	 */
	static StringTranslate getInstance() {
		return instance;
	}

	/**
	 * Replaces all the current instance's translations with the ones that are
	 * passed in.
	 */
	public static synchronized void replaceWith(Map p_135063_0_) {
		instance.languageList.clear();
		instance.languageList.putAll(p_135063_0_);
		instance.lastUpdateTimeInMilliseconds = System.currentTimeMillis();
	}

	private final Map languageList = Maps.newHashMap();

	/**
	 * The time, in milliseconds since epoch, that this instance was last
	 * updated
	 */
	private long lastUpdateTimeInMilliseconds;

	public StringTranslate() {
		try {
			final InputStream var1 = StringTranslate.class
					.getResourceAsStream("/assets/minecraft/lang/en_US.lang");
			final Iterator var2 = IOUtils.readLines(var1, Charsets.UTF_8)
					.iterator();

			while (var2.hasNext()) {
				final String var3 = (String) var2.next();

				if (!var3.isEmpty() && var3.charAt(0) != 35) {
					final String[] var4 = Iterables.toArray(
							equalSignSplitter.split(var3), String.class);

					if (var4 != null && var4.length == 2) {
						final String var5 = var4[0];
						final String var6 = numericVariablePattern.matcher(
								var4[1]).replaceAll("%$1s");
						languageList.put(var5, var6);
					}
				}
			}

			lastUpdateTimeInMilliseconds = System.currentTimeMillis();
		} catch (final IOException var7) {
			;
		}
	}

	public synchronized boolean containsTranslateKey(String p_94520_1_) {
		return languageList.containsKey(p_94520_1_);
	}

	/**
	 * Gets the time, in milliseconds since epoch, that this instance was last
	 * updated
	 */
	public long getLastUpdateTimeInMilliseconds() {
		return lastUpdateTimeInMilliseconds;
	}

	/**
	 * Translate a key to current language.
	 */
	public synchronized String translateKey(String p_74805_1_) {
		return tryTranslateKey(p_74805_1_);
	}

	/**
	 * Translate a key to current language applying String.format()
	 */
	public synchronized String translateKeyFormat(String p_74803_1_,
			Object... p_74803_2_) {
		final String var3 = tryTranslateKey(p_74803_1_);

		try {
			return String.format(var3, p_74803_2_);
		} catch (final IllegalFormatException var5) {
			return "Format error: " + var3;
		}
	}

	/**
	 * Tries to look up a translation for the given key; spits back the key if
	 * no result was found.
	 */
	private String tryTranslateKey(String p_135064_1_) {
		final String var2 = (String) languageList.get(p_135064_1_);
		return var2 == null ? p_135064_1_ : var2;
	}
}
