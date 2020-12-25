public class HashList {
	
	private HashListElement first;
	
	public HashList(HashListElement first) {
		this.first = first;
	}

	public void add(HashListElement element) {
		element.setNext(first);
		first = element;
	}

	public boolean find(int key) {
		boolean output = false;
		HashListElement curr = first;
		while (!output & curr != null) {
			output = (key == curr.getKey());
			curr = curr.next;
		}
		return output;
	}
}