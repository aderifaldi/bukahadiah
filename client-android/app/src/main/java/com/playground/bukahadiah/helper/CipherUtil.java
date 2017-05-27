package com.playground.bukahadiah.helper;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.radyalabs.irfan.util.AppUtility;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Base64;
import android.util.Log;

public class CipherUtil {

	public static String hash(String key, String plain) {
		String result = "";
		result = result + (key.substring(0, 4));
		result = result + plain;
		result = result + (key.substring(4));

		Log.i("HAE", result);

		return sha256(result);
	}

	public static String sha256(String base) {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] hash = digest.digest(base.getBytes("UTF-8"));
			StringBuffer hexString = new StringBuffer();

			for (int i = 0; i < hash.length; i++) {
				String hex = Integer.toHexString(0xff & hash[i]);
				if (hex.length() == 1)
					hexString.append('0');
				hexString.append(hex);
			}

			return hexString.toString();
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}
	
	public static void getHashForFacebook(Activity act){
		try {
			AppUtility.logD("appsterize", act.getApplicationContext().getPackageName());
			PackageInfo info = act.getPackageManager().getPackageInfo(act.getApplicationContext().getPackageName(), PackageManager.GET_SIGNATURES);
			AppUtility.logD("appsterize", ""+info.signatures.length);
			for (Signature signature : info.signatures) {
				MessageDigest md;
				md = MessageDigest.getInstance("SHA");
				md.update(signature.toByteArray());
				String something = new String(Base64.encode(md.digest(), 0));
				AppUtility.logD("appsterize", something);
			}
		}catch (NameNotFoundException e1) {
			AppUtility.logD("appsterize", e1.toString()); 
		}catch (NoSuchAlgorithmException e) {
			AppUtility.logD("appsterize", e.toString());
		}catch (Exception e){
			AppUtility.logD("appsterize", e.toString());
		}
	}
}