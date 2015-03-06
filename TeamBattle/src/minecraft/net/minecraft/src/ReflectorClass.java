package net.minecraft.src;

public class ReflectorClass {
	private boolean checked = false;
	private Class targetClass = null;
	private String targetClassName = null;

	public ReflectorClass(Class targetClass) {
		this.targetClass = targetClass;
		targetClassName = targetClass.getName();
		checked = true;
	}

	public ReflectorClass(String targetClassName) {
		this.targetClassName = targetClassName;
		getTargetClass();
	}

	public boolean exists() {
		return getTargetClass() != null;
	}

	public Class getTargetClass() {
		if (checked)
			return targetClass;
		else {
			checked = true;

			try {
				targetClass = Class.forName(targetClassName);
			} catch (final ClassNotFoundException var2) {
				Config.log("(Reflector) Class not present: " + targetClassName);
			} catch (final Throwable var3) {
				var3.printStackTrace();
			}

			return targetClass;
		}
	}

	public String getTargetClassName() {
		return targetClassName;
	}
}
