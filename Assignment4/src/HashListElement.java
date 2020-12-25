public class HashListElement {
	
	HashListElement next;
	int key;
	
	public HashListElement(int key, HashListElement next) {
		this.next = next;
		this.key = key;
	}
	
	public void setNext (HashListElement next) {
		this.next = next;
	}
	
	public int getKey() {
		return key;
	}
}