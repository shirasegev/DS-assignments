import java.util.Scanner;

public class BloomFilter extends Contructs {
	
	private int m1;
	boolean[] binaryArr;
	HashFunc[] hashFuncs;
	final static long p = 15486907;

	public BloomFilter(String m1, String file) {
		if (!isLegal(m1)) {
			throw new IllegalArgumentException("Argument must be a natural number");
		}
		else {
			this.m1 = Integer.parseInt(m1);
			// Default values are False
			binaryArr = new boolean[this.m1];
	        hashFuncs = new HashFunc[countLines(file)];
	        int i = 0;
			Scanner sc = getScanner(file);
			while (sc.hasNextLine()) {
				hashFuncs[i] = new HashFunc(sc.nextLine());
				i++;
			}
		}
	}
	
	// Add each line to bloom filter table
	public void updateTable(String file) {
		Scanner sc = getScanner(file);
		while (sc.hasNextLine()) {
			String badPass = sc.nextLine();
			add(badPass);
		}
	}
	
	// Initialize the bloom filter table using hash functions
	private void add (String s) {
		int k = (int)horner(s);
		for (int i = 0; i < hashFuncs.length; i++) {
			int hashVal = (int)(((hashFuncs[i].a*k + hashFuncs[i].b) % p) % m1);
			binaryArr[hashVal] = true;
		}
	}

	// Go throw the input file
	// check if the bloomFilter reject a current password
	// than approve it in hash table
	// calculate the false positive percentage
	public String getFalsePositivePercentage(HashTable hashTable, String file) {
		int rejectCounter = 0; 
		int falsePositiveCounter = 0;
		Scanner sc = getScanner(file);
		while (sc.hasNextLine()) {
			String requestedPass = sc.nextLine();
			int key = (int)horner(requestedPass);
			if (!hashTable.search(key)) {
				rejectCounter++;
				if (!isLegal(key)) {
					falsePositiveCounter++;
				}
			}
		}
		return "" + ((double)falsePositiveCounter / (double)rejectCounter);
	}

	// Count the number of rejected passwords among all given passwords
	public String getRejectedPasswordsAmount(String file) {
		int counter = 0; 
		Scanner sc = getScanner(file);
		while (sc.hasNextLine()) {
			String requestedPass = sc.nextLine();
			if (!isLegal((int)horner(requestedPass))) {
				counter++;
			}
		}
		return "" + counter;
	}
	
	// Check if the password is legal by checking the bloom filter array indexes using hash functions
	private boolean isLegal (int k) {
		boolean illegal = true;
		for (int i = 0; i < hashFuncs.length & illegal; i++) {
			int hashVal = (int)(((hashFuncs[i].a*k + hashFuncs[i].b) % p) % m1);
			illegal = binaryArr[hashVal];
		}
		return !illegal;
	}
	
}