
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class Main {

	public static void main(String[] args) {
		String action = args[0];
		String fileName = args[1];
		System.out.println(action + ":" + fileName);
		
		if (action.equals("d")) {
			try {
				byte[] bFile = readBytesFromFile("C:\\Users\\Dmscherr\\Documents\\ChaosBane\\" + fileName + ".ynm");
				String data = decrypt(bFile, fileName);
				Path path = Paths.get("C:\\Users\\Dmscherr\\Documents\\ChaosBane\\" + fileName + "-decrypt.xml");
				Files.write(path, data.getBytes());
				System.out.println("Done");
			} catch (IOException e) {
				e.printStackTrace();
			} 		
		} else if(action.equals("e")) {
			try {
				String xmlData = new String (readBytesFromFile("C:\\Users\\Dmscherr\\Documents\\ChaosBane\\" + fileName + "-decrypt.xml"));
				byte[] data = encrypt(xmlData, fileName);
				Path path = Paths.get("C:\\Users\\Dmscherr\\Documents\\ChaosBane\\" + fileName + "-1.ynm");
				Files.write(path, data);
				System.out.println("Done");
			} catch (IOException e) {
				e.printStackTrace();
			} 	
		} else {
			System.out.println("I don't know what I am doing.");
		}			
	}

	public static String decrypt(byte[] savegameData, String savegameName) {
		try {
			Cipher rc4 = Cipher.getInstance("RC4");
			rc4.init(Cipher.DECRYPT_MODE, makeKey(savegameName));
			byte[] decrypted = rc4.doFinal(savegameData);
			return new String(decrypted);
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException
				| BadPaddingException e) {
			throw new RuntimeException(e);
		}
	}

	public static byte[] encrypt(String savegameData, String savegameName) {
		try {
			Cipher rc4 = Cipher.getInstance("RC4");
			rc4.init(Cipher.ENCRYPT_MODE, makeKey(savegameName));
			return rc4.doFinal(savegameData.getBytes());
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException
				| BadPaddingException e) {
			throw new RuntimeException(e);
		}
	}

	private static SecretKeySpec makeKey(String savegameName) {
		byte[] secret = (savegameName.toLowerCase() + "EKO_SECRET:7373730!").getBytes();
		return new SecretKeySpec(secret, 0, secret.length, "RC4");
	}
	
	private static byte[] readBytesFromFile(String filePath) {

        FileInputStream fileInputStream = null;
        byte[] bytesArray = null;

        try {

            File file = new File(filePath);
            bytesArray = new byte[(int) file.length()];

            //read file into bytes[]
            fileInputStream = new FileInputStream(file);
            fileInputStream.read(bytesArray);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }

        return bytesArray;

    }
}