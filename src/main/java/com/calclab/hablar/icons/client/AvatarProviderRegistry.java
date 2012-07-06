package com.calclab.hablar.icons.client;

import java.util.HashMap;
import java.util.Map;

public class AvatarProviderRegistry {

	private final Map<String, AvatarConfig> providers;
	
	public AvatarProviderRegistry() {
		providers = new HashMap<String, AvatarConfig>(2);
		
		put("gravatar", new GravatarConfig());
	}
	
	public void put(final String name, final AvatarConfig provider) {
		providers.put(name.toLowerCase(), provider);
	}
	
	public AvatarConfig get(final String name) {
		return name == null ? null : providers.get(name.toLowerCase());
	}
}
