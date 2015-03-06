package net.minecraft.src;

public class NaturalProperties {
	public boolean flip = false;
	public int rotation = 1;

	public NaturalProperties(String type) {
		if (type.equals("4")) {
			rotation = 4;
		} else if (type.equals("2")) {
			rotation = 2;
		} else if (type.equals("F")) {
			flip = true;
		} else if (type.equals("4F")) {
			rotation = 4;
			flip = true;
		} else if (type.equals("2F")) {
			rotation = 2;
			flip = true;
		} else {
			Config.warn("NaturalTextures: Unknown type: " + type);
		}
	}

	public boolean isValid() {
		return rotation != 2 && rotation != 4 ? flip : true;
	}
}
