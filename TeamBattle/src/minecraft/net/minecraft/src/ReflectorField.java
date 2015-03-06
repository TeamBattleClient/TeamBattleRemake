package net.minecraft.src;

import java.lang.reflect.Field;

public class ReflectorField {
	private boolean checked = false;
	private ReflectorClass reflectorClass = null;
	private Field targetField = null;
	private String targetFieldName = null;

	public ReflectorField(ReflectorClass reflectorClass, String targetFieldName) {
		this.reflectorClass = reflectorClass;
		this.targetFieldName = targetFieldName;
		getTargetField();
	}

	public boolean exists() {
		return checked ? targetField != null : getTargetField() != null;
	}

	public Field getTargetField() {
		if (checked)
			return targetField;
		else {
			checked = true;
			final Class cls = reflectorClass.getTargetClass();

			if (cls == null)
				return null;
			else {
				try {
					targetField = cls.getDeclaredField(targetFieldName);

					if (!targetField.isAccessible()) {
						targetField.setAccessible(true);
					}
				} catch (final SecurityException var3) {
					var3.printStackTrace();
				} catch (final NoSuchFieldException var4) {
					Config.log("(Reflector) Field not present: "
							+ cls.getName() + "." + targetFieldName);
				}

				return targetField;
			}
		}
	}

	public Object getValue() {
		return Reflector.getFieldValue((Object) null, this);
	}

	public void setValue(Object value) {
		Reflector.setFieldValue((Object) null, this, value);
	}
}
