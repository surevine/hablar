/**
 * 
 */
package com.calclab.hablar.icons.client;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;

public class GravatarConfig implements AvatarConfig {

	@Override
	public String getUrl(final XmppURI xmppURI) {
		return "http://gravatar.com/avatar/" + new MD5Util().digest(xmppURI.getJID().toString() +"?s=24&d=mm");
	}

	class MD5Util {
		public String hex(byte[] array) {
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < array.length; ++i) {
				sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1, 3));
			}
			return sb.toString();
		}

		public String digest(String message) {
			try {
				MessageDigest md = MessageDigest.getInstance("MD5");
				return hex(md.digest(message.getBytes("CP1252")));
			} catch (NoSuchAlgorithmException e) {
			} catch (UnsupportedEncodingException e) {
			}
			return null;
		}
	}
}
