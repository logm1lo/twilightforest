package twilightforest.util;

public final class Vec2i {
	public static final Vec2i ZERO = new Vec2i(0, 0);

	public int x, z;

	public Vec2i() {
	}

	public Vec2i(int x, int z) {
		this.x = x;
		this.z = z;
	}
}
