/**
 * 
 */
package com.calclab.hablar.icons.client;

import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;

public class AvatarIcon implements AvatarConfig {

	@Override
	public String getUrl(final XmppURI xmppURI) {
		return "http://2.gravatar.com/avatar/8f0c9789bd69a98b3103bcefa878f1bb?s=66&d=retro";
	}
}
