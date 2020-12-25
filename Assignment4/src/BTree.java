import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class BTree {
	
	private BTreeNode root;
	private int t;

	public BTree(String tVal) {
		if (!isLegal(tVal)) {
			throw new IllegalArgumentException("Argument must be a natural number");
		}
		else {
			t = Integer.parseInt(tVal);
			root = new BTreeNode(t);
		}
	}
	
    private static boolean isLegal(String s) { 
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
    
	public String toString() {
		String ans = root.toString(0);
		if(ans.length() >  0) {
			ans = ans.substring(0, ans.length()-1);
		}
		return ans;
	}

	public void createFullTree(String file) {
		Scanner sc = getScanner(file);
		while (sc.hasNextLine()) {
			String badPass = sc.nextLine();
			insert(badPass);
		}
	}
	
	// Insert a given password to the tree
	public void insert(String password) {
		BTreeNode node = root;
		password = password.toLowerCase();
		// if root is full, then tree grows in height
		if (node.getSize() == (2*t - 1)) {
			BTreeNode newNode = new BTreeNode(t); // Allocate memory for new root
			root = newNode;
			newNode.setLeaf(false);
			newNode.setChildrenPointers(node, 0);
			newNode.splitChild(0); // Split the former root
			newNode.insertNonFull(password);
		}
		else {
			// if there is still extra place for another key,
			// insert it to the root
			node.insertNonFull(password);
		}	
	}
	
	public boolean search(String password) {
		return root.search(password.toLowerCase());
	}
	
	public void deleteKeysFromTree(String file) {
		Scanner sc = getScanner(file);
		while (sc.hasNextLine()) {
			String toDelete = sc.nextLine();
			delete(toDelete);
		}
	}
	
	public void delete(String password) {
		root.delete(password.toLowerCase());
	}
	
	public String getSearchTime(String file) {
		Scanner sc = getScanner(file);
		long startTime = System.nanoTime();
		while (sc.hasNextLine()) {
			String requestedPass = sc.nextLine();
			search(requestedPass);
		}
		return "" + (int)(((double)System.nanoTime() - startTime) / 100) / (double)10000;
	}
	
	
	public static Scanner getScanner (String file) {
	    Scanner sc = null;
		try {
			sc = new Scanner(new File(file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} return sc;
	}

}