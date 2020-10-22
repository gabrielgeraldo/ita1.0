package br.com.ita.controle.util;

import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class DescriptografiaAES {
	private static final String ALGORITMO = "AES";
	private final SecretKeySpec chaveSecreta;

	public DescriptografiaAES(String chaveSecreta) {
		this.chaveSecreta = new SecretKeySpec(chaveSecreta.getBytes(), ALGORITMO);
	}

	public String descriptografa(String textoCriptografado) {
		try {
			Cipher cipher = Cipher.getInstance(ALGORITMO);
			cipher.init(Cipher.DECRYPT_MODE, chaveSecreta);
			byte[] decryptedBytes = cipher.doFinal(base64Decode(textoCriptografado));
			return new String(decryptedBytes);
		} catch (Exception e) {
			// throw new RuntimeException("Falha ao descriptografar a mensagem:
			// " + e);
			return null;
		}
	}

	private byte[] base64Decode(String encryptedMessage) {
		return Base64.getDecoder().decode(encryptedMessage);
	}

}
