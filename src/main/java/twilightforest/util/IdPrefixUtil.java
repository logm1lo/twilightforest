package twilightforest.util;

import net.minecraft.resources.ResourceLocation;

import java.util.Locale;

public class IdPrefixUtil {

	private final String prefix;

	public IdPrefixUtil(String prefix) {
		this.prefix = prefix;
	}

	public String stringPrefix(String suffix) {
		return prefix.concat(":").concat(suffix);
	}

	public ResourceLocation prefix(String name) {
		return ResourceLocation.fromNamespaceAndPath(prefix, name.toLowerCase(Locale.ROOT));
	}

	public ResourceLocation modelTexture(String name) {
		return ResourceLocation.fromNamespaceAndPath(prefix, "textures/entity/" + name);
	}

	public ResourceLocation guiTexture(String name) {
		return ResourceLocation.fromNamespaceAndPath(prefix, "textures/gui/" + name);
	}

	public ResourceLocation envTexture(String name) {
		return ResourceLocation.fromNamespaceAndPath(prefix, "textures/environment/" + name);
	}

}
