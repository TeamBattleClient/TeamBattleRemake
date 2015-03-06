package net.minecraft.util;

import java.util.Arrays;
import java.util.IllegalFormatException;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;

public class ChatComponentTranslation extends ChatComponentStyle {
	public static final Pattern stringVariablePattern = Pattern
			.compile("%(?:(\\d+)\\$)?([A-Za-z%]|$)");
	/**
	 * The discrete elements that make up this component. For example, this
	 * would be ["Prefix, ", "FirstArg", "SecondArg", " again ", "SecondArg",
	 * " and ", "FirstArg", " lastly ", "ThirdArg", " and also ", "FirstArg", "
	 * again!"] for "translation.test.complex" (see en-US.lang)
	 */
	List children = Lists.newArrayList();
	private final Object[] formatArgs;
	private final String key;

	private long lastTranslationUpdateTimeInMilliseconds = -1L;
	private final Object syncLock = new Object();

	public ChatComponentTranslation(String p_i45160_1_, Object... p_i45160_2_) {
		key = p_i45160_1_;
		formatArgs = p_i45160_2_;
		final Object[] var3 = p_i45160_2_;
		final int var4 = p_i45160_2_.length;

		for (int var5 = 0; var5 < var4; ++var5) {
			final Object var6 = var3[var5];

			if (var6 instanceof IChatComponent) {
				((IChatComponent) var6).getChatStyle().setParentStyle(
						getChatStyle());
			}
		}
	}

	/**
	 * Creates a copy of this component. Almost a deep copy, except the style is
	 * shallow-copied.
	 */
	@Override
	public ChatComponentTranslation createCopy() {
		final Object[] var1 = new Object[formatArgs.length];

		for (int var2 = 0; var2 < formatArgs.length; ++var2) {
			if (formatArgs[var2] instanceof IChatComponent) {
				var1[var2] = ((IChatComponent) formatArgs[var2]).createCopy();
			} else {
				var1[var2] = formatArgs[var2];
			}
		}

		final ChatComponentTranslation var5 = new ChatComponentTranslation(key,
				var1);
		var5.setChatStyle(getChatStyle().createShallowCopy());
		final Iterator var3 = getSiblings().iterator();

		while (var3.hasNext()) {
			final IChatComponent var4 = (IChatComponent) var3.next();
			var5.appendSibling(var4.createCopy());
		}

		return var5;
	}

	/**
	 * ensures that our children are initialized from the most recent string
	 * translation mapping.
	 */
	synchronized void ensureInitialized() {
		synchronized (syncLock) {
			final long var2 = StatCollector
					.getLastTranslationUpdateTimeInMilliseconds();

			if (var2 == lastTranslationUpdateTimeInMilliseconds)
				return;

			lastTranslationUpdateTimeInMilliseconds = var2;
			children.clear();
		}

		try {
			initializeFromFormat(StatCollector.translateToLocal(key));
		} catch (final ChatComponentTranslationFormatException var6) {
			children.clear();

			try {
				initializeFromFormat(StatCollector.translateToFallback(key));
			} catch (final ChatComponentTranslationFormatException var5) {
				throw var6;
			}
		}
	}

	@Override
	public boolean equals(Object p_equals_1_) {
		if (this == p_equals_1_)
			return true;
		else if (!(p_equals_1_ instanceof ChatComponentTranslation))
			return false;
		else {
			final ChatComponentTranslation var2 = (ChatComponentTranslation) p_equals_1_;
			return Arrays.equals(formatArgs, var2.formatArgs)
					&& key.equals(var2.key) && super.equals(p_equals_1_);
		}
	}

	public Object[] getFormatArgs() {
		return formatArgs;
	}

	private IChatComponent getFormatArgumentAsComponent(int p_150272_1_) {
		if (p_150272_1_ >= formatArgs.length)
			throw new ChatComponentTranslationFormatException(this, p_150272_1_);
		else {
			final Object var2 = formatArgs[p_150272_1_];
			Object var3;

			if (var2 instanceof IChatComponent) {
				var3 = var2;
			} else {
				var3 = new ChatComponentText(var2 == null ? "null"
						: var2.toString());
				((IChatComponent) var3).getChatStyle().setParentStyle(
						getChatStyle());
			}

			return (IChatComponent) var3;
		}
	}

	public String getKey() {
		return key;
	}

	/**
	 * Gets the text of this component, without any special formatting codes
	 * added, for chat. TODO: why is this two different methods?
	 */
	@Override
	public String getUnformattedTextForChat() {
		ensureInitialized();
		final StringBuilder var1 = new StringBuilder();
		final Iterator var2 = children.iterator();

		while (var2.hasNext()) {
			final IChatComponent var3 = (IChatComponent) var2.next();
			var1.append(var3.getUnformattedTextForChat());
		}

		return var1.toString();
	}

	@Override
	public int hashCode() {
		int var1 = super.hashCode();
		var1 = 31 * var1 + key.hashCode();
		var1 = 31 * var1 + Arrays.hashCode(formatArgs);
		return var1;
	}

	/**
	 * initializes our children from a format string, using the format args to
	 * fill in the placeholder variables.
	 */
	protected void initializeFromFormat(String p_150269_1_) {
		final Matcher var3 = stringVariablePattern.matcher(p_150269_1_);
		int var4 = 0;
		int var5 = 0;

		try {
			int var7;

			for (; var3.find(var5); var5 = var7) {
				final int var6 = var3.start();
				var7 = var3.end();

				if (var6 > var5) {
					final ChatComponentText var8 = new ChatComponentText(
							String.format(p_150269_1_.substring(var5, var6),
									new Object[0]));
					var8.getChatStyle().setParentStyle(getChatStyle());
					children.add(var8);
				}

				final String var14 = var3.group(2);
				final String var9 = p_150269_1_.substring(var6, var7);

				if ("%".equals(var14) && "%%".equals(var9)) {
					final ChatComponentText var15 = new ChatComponentText("%");
					var15.getChatStyle().setParentStyle(getChatStyle());
					children.add(var15);
				} else {
					if (!"s".equals(var14))
						throw new ChatComponentTranslationFormatException(this,
								"Unsupported format: \'" + var9 + "\'");

					final String var10 = var3.group(1);
					final int var11 = var10 != null ? Integer.parseInt(var10) - 1
							: var4++;
					children.add(getFormatArgumentAsComponent(var11));
				}
			}

			if (var5 < p_150269_1_.length()) {
				final ChatComponentText var13 = new ChatComponentText(
						String.format(p_150269_1_.substring(var5),
								new Object[0]));
				var13.getChatStyle().setParentStyle(getChatStyle());
				children.add(var13);
			}
		} catch (final IllegalFormatException var12) {
			throw new ChatComponentTranslationFormatException(this, var12);
		}
	}

	@Override
	public Iterator iterator() {
		ensureInitialized();
		return Iterators.concat(createDeepCopyIterator(children),
				createDeepCopyIterator(siblings));
	}

	@Override
	public IChatComponent setChatStyle(ChatStyle p_150255_1_) {
		super.setChatStyle(p_150255_1_);
		final Object[] var2 = formatArgs;
		final int var3 = var2.length;

		for (int var4 = 0; var4 < var3; ++var4) {
			final Object var5 = var2[var4];

			if (var5 instanceof IChatComponent) {
				((IChatComponent) var5).getChatStyle().setParentStyle(
						getChatStyle());
			}
		}

		if (lastTranslationUpdateTimeInMilliseconds > -1L) {
			final Iterator var6 = children.iterator();

			while (var6.hasNext()) {
				final IChatComponent var7 = (IChatComponent) var6.next();
				var7.getChatStyle().setParentStyle(p_150255_1_);
			}
		}

		return this;
	}

	@Override
	public String toString() {
		return "TranslatableComponent{key=\'" + key + '\'' + ", args="
				+ Arrays.toString(formatArgs) + ", siblings=" + siblings
				+ ", style=" + getChatStyle() + '}';
	}
}
