package tp4768;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class Main {

	public static void main(String[] args) {
		
		try {
			//create new users
			User bob = new User();
			User alice = new User();
			
			//generate users' key pair
			bob.user_key_pair = RSA.generate();
			alice.user_key_pair = RSA.generate();
			
			//save each others pub key
			bob.saved_pub_key = alice.user_key_pair.getPublic();
			alice.saved_pub_key = bob.user_key_pair.getPublic();
			
			//verification
			byte[] encrypted = RSA.encrypt("Verification message", bob.user_key_pair.getPrivate());
			printer(Base64.getEncoder().encodeToString(encrypted),1);
			String decrypted = RSA.decrypt(encrypted, alice.saved_pub_key);
			printer(decrypted + "--> OK", 2);
			
			//Bob uses alices pub key and vice versa
			encrypted = RSA.encrypt("Hey alice how are you", bob.saved_pub_key);
			printer(Base64.getEncoder().encodeToString(encrypted),1);
			decrypted = RSA.decrypt(encrypted, alice.user_key_pair.getPrivate());
			printer(decrypted + "---> OK", 2);
			
			//Double encryption
			encrypted = RSA.encrypt("This is a double encrypted and signed message can you read it?", bob.user_key_pair.getPrivate());
			encrypted = RSA.encrypt(encrypted, bob.saved_pub_key,true);	//no padding
			printer(Base64.getEncoder().encodeToString(encrypted),1);
			decrypted = RSA.decrypt(encrypted,alice.user_key_pair.getPrivate(),alice.saved_pub_key);
			printer(decrypted + "---> OK", 2);
			
			
			
		} catch (InvalidKeyException | BadPaddingException | IllegalBlockSizeException | NoSuchPaddingException
				| NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
 	
		

	}
	//function that prints to the debug out
	//@param message -> the message to be printed
	//@param who -> 1 for bob 2 for alice
	public static void printer(String message,int who)
	{
		if(who == 1)
		{
			System.out.println("\n[BOB] -> " + message);
		}
		else
		{
			System.err.println("\n[ALICE] -> " + message);
		}
	}
	

}
