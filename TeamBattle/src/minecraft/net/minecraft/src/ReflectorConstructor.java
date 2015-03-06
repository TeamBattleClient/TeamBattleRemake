package net.minecraft.src;

import java.lang.reflect.Constructor;

public class ReflectorConstructor {
	private static Constructor findConstructor(Class cls, Class[] paramTypes) {
		final Constructor[] cs = cls.getDeclaredConstructors();

		for (final Constructor c : cs) {
			final Class[] types = c.getParameterTypes();

			if (Reflector.matchesTypes(paramTypes, types))
				return c;
		}

		return null;
	}

	private boolean checked = false;
	private Class[] parameterTypes = null;
	private ReflectorClass reflectorClass = null;

	private Constructor targetConstructor = null;

	public ReflectorConstructor(ReflectorClass reflectorClass,
			Class[] parameterTypes) {
		this.reflectorClass = reflectorClass;
		this.parameterTypes = parameterTypes;
		getTargetConstructor();
	}

	public void deactivate() {
		checked = true;
		targetConstructor = null;
	}

	public boolean exists() {
		return checked ? targetConstructor != null
				: getTargetConstructor() != null;
	}

	public Constructor getTargetConstructor() {
		if (checked)
			return targetConstructor;
		else {
			checked = true;
			final Class cls = reflectorClass.getTargetClass();

			if (cls == null)
				return null;
			else {
				targetConstructor = findConstructor(cls, parameterTypes);

				if (targetConstructor == null) {
					Config.dbg("(Reflector) Constructor not present: "
							+ cls.getName() + ", params: "
							+ Config.arrayToString(parameterTypes));
				}

				if (targetConstructor != null
						&& !targetConstructor.isAccessible()) {
					targetConstructor.setAccessible(true);
				}

				return targetConstructor;
			}
		}
	}
}
