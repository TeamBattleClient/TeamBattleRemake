package net.minecraft.src;

import java.lang.reflect.Method;

public class ReflectorMethod {
	private boolean checked;
	private ReflectorClass reflectorClass;
	private Method targetMethod;
	private String targetMethodName;
	private Class[] targetMethodParameterTypes;

	public ReflectorMethod(ReflectorClass reflectorClass,
			String targetMethodName) {
		this(reflectorClass, targetMethodName, (Class[]) null);
	}

	public ReflectorMethod(ReflectorClass reflectorClass,
			String targetMethodName, Class[] targetMethodParameterTypes) {
		this.reflectorClass = null;
		this.targetMethodName = null;
		this.targetMethodParameterTypes = null;
		checked = false;
		targetMethod = null;
		this.reflectorClass = reflectorClass;
		this.targetMethodName = targetMethodName;
		this.targetMethodParameterTypes = targetMethodParameterTypes;
		getTargetMethod();
	}

	public void deactivate() {
		checked = true;
		targetMethod = null;
	}

	public boolean exists() {
		return checked ? targetMethod != null : getTargetMethod() != null;
	}

	public Class getReturnType() {
		final Method tm = getTargetMethod();
		return tm == null ? null : tm.getReturnType();
	}

	public Method getTargetMethod() {
		if (checked)
			return targetMethod;
		else {
			checked = true;
			final Class cls = reflectorClass.getTargetClass();

			if (cls == null)
				return null;
			else {
				final Method[] ms = cls.getDeclaredMethods();
				int i = 0;
				Method m;

				while (true) {
					if (i >= ms.length) {
						Config.log("(Reflector) Method not pesent: "
								+ cls.getName() + "." + targetMethodName);
						return null;
					}

					m = ms[i];

					if (m.getName().equals(targetMethodName)) {
						if (targetMethodParameterTypes == null) {
							break;
						}

						final Class[] types = m.getParameterTypes();

						if (Reflector.matchesTypes(targetMethodParameterTypes,
								types)) {
							break;
						}
					}

					++i;
				}

				targetMethod = m;

				if (!targetMethod.isAccessible()) {
					targetMethod.setAccessible(true);
				}

				return targetMethod;
			}
		}
	}
}
