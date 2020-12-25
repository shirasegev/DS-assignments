public class FloorsArrayList implements DynamicSet {
    
    private int size;
    //We keep the value of the highest array in the list,
    //to save runTime during search
    private int maxArrSize;
    //First and last fields will hold the value of Infinity and -Infinity
    private FloorsArrayLink first;
    private FloorsArrayLink last;
    
	public FloorsArrayList(int N){
        size = 0;
        maxArrSize = 0;
        first = new FloorsArrayLink(Double.NEGATIVE_INFINITY, N);        
        last = new FloorsArrayLink(Double.POSITIVE_INFINITY, N);
        //Initialize first and last (+-Infinity) links to the height of the given N
        for (int i = 1; i <= N; i = i + 1) {
        	 first.setNext(i, last);
        	 last.setPrev(i, first);
        }  
    }

    public int getSize(){
        return size;
    }
    
    public FloorsArrayLink lookup(double key) {
    	FloorsArrayLink link = find(key);
    	//Make sure the link we got from find function is what we were looking for 
    	if (link.getKey() == key) {
    		return link;
    	}
    	//Otherwise, it probably do not exist.
    	return null;
    }
    
    //Assisting function for lookup, and insert
    public FloorsArrayLink find(double key) {
    	int i = maxArrSize;
    	FloorsArrayLink curr = first;
    	boolean found = false;
    	while (i > 1 & !found) {
    		i = i - 1;
    		while (curr.getNext(i).getKey() <= key & !found) {
    			curr = curr.getNext(i);
    			if (curr.getKey() == key) {
    				found = true;
    			}
    		}
    	}
        return curr;
    }
	
    public void insert(double key, int arrSize) {
    	//Update the field that holds the value of maxArrSize, if needed
    	if (arrSize > maxArrSize) {
    		maxArrSize = arrSize;
    	}
    	FloorsArrayLink toAdd = new FloorsArrayLink(key, arrSize);
    	FloorsArrayLink link = find(key);
    	//Update pointers,
    	//while ensuring the new link will be updated throughout all its array
    	FloorsArrayLink next = link.getNext(1);
    	for (int i = 1; i <= toAdd.getArrSize(); i++) {
    		while (next.getArrSize() < i) {
    			next = next.getNext(next.getArrSize());
    		}
    		link = next.getPrev(i);
			toAdd.setPrev(i, link);
			toAdd.setNext(i, next);
			link.setNext(i, toAdd);
			next.setPrev(i, toAdd);
    	}
    	//A new link is added to the list
    	size = size + 1;
    }

    public void remove(FloorsArrayLink toRemove) {
    	//Update max array size in case toRemove is the link
    	//with the max array size in the list
    	if (toRemove.getArrSize() == maxArrSize) {
    		int i = maxArrSize;
    		while (i > 0 && toRemove.getNext(i) == last && toRemove.getPrev(i) == first) {
    			i--;
    		}
    		maxArrSize = i;
    	}
    	FloorsArrayLink prev;
    	FloorsArrayLink next;
    	//Update pointers at each floor at the surrounding links.
    	for (int i = 1; i <= toRemove.getArrSize(); i++) {
    		next = toRemove.getNext(i);
    		prev = toRemove.getPrev(i);
    		prev.setNext(i, next);
    		next.setPrev(i, prev);
    	}
    	//Update size field after removing a link
    	size = size - 1;
    }

    public double successor(FloorsArrayLink link) {
        return link.getNext(1).getKey();
    }

    public double predecessor(FloorsArrayLink link) {        
        return link.getPrev(1).getKey();
    }

    public double minimum() {
        return successor(first);
    }

    public double maximum() {
        return predecessor(last);
    }
    
}