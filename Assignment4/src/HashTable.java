import java.util.Scanner;

public class HashTable extends Contructs {
	
	private int m2;
	private HashList[] hashArr;
	final static long p = 15486907;

	public HashTable(String m2) {
		if (!isLegal(m2)) {
			throw new IllegalArgumentException("Argument must be a natural number");
		}
		else {
			this.m2 = Integer.parseInt(m2);
			hashArr = new HashList[this.m2];
		}
	}
	
	private int hashFunction(int key) {
		return key % m2;
	}

	// Go throw the input file and add the Horner value of each line to the hash table
	public void updateTable(String file) {
		Scanner sc = getScanner(file);
		while (sc.hasNextLine()) {
			String badPass = sc.nextLine();
			add((int)horner(badPass));
		}
	}

	// Add key to the hash table with chaining
	private void add(int key) {
		HashListElement newElement = new HashListElement(key, null);
		int index = hashFunction(key);
		if (hashArr[index] == null) {
			hashArr[index] = new HashList(newElement);
		}
		else {
			hashArr[index].add(newElement);
		}
	}

	// Calculate search time in mili sec.
	public String getSearchTime(String file) {
		Scanner sc = getScanner(file);
		long startTime = System.nanoTime();
		while (sc.hasNextLine()) {
			String requestedPass = sc.nextLine();
			search((int)horner(requestedPass));
		}
		return "" + (int)(((double)System.nanoTime() - startTime) / 100) / (double)10000;
	}
	
	// Search key in the hash list
	public boolean search (int key) {
		boolean output = false;
		int index = hashFunction(key);
		if (hashArr[index] != null) {
			output = hashArr[index].find(key);
		}
		return output;
	}
}