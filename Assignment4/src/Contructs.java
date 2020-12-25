import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public abstract class Contructs {
	
	final static long p = 15486907;

	public long horner(String password) {
		long ans = 0;
		for (int i = 0; i < password.length(); i++) {
			ans = (password.charAt(i) + ans * 256) % p;
		}
		return ans;
	}

	public Scanner getScanner(String file) {
	    Scanner sc = null;
		try {
			sc = new Scanner(new File(file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return sc;
	}

	public int countLines(String file) {
		Scanner sc = getScanner(file);
		int counter = 0;
		while (sc.hasNextLine()) {
			sc.nextLine();
			counter++;
		}
		return counter;
	}
	
    public static boolean isLegal(String s) { 
    	boolean valid = true;
    	if (s == null || s.length() == 0 || (s.length() > 1 && s.charAt(0) == '0')) {
    		valid = false;
    	}
    	for (int i = 0; valid && i < s.length(); i = i + 1) {
    		if (s.charAt(i) < '0' || s.charAt(i) > '9') {
    			valid = false;
    		}
    	}
    	return valid;
    }
}