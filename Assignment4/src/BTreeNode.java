public class BTreeNode {
	
	private int t;
	private int size;
	private String[] keysArr;
	private boolean leaf;
	private BTreeNode[] childrenPointers;
	boolean isNewRoot = false;
	
	public BTreeNode(int t) {
		this.t = t;
		size = 0;
		keysArr = new String[2*t-1];
		leaf = true;
		// left child of the key at index i will be at index i as well
		// right child of the key at index i will be at index i+1
		childrenPointers = new BTreeNode[2*t];
	}
	
	public String toString(int depth) {
		String ans = "";
		int i;
		// Go throw all first n children
		for (i = 0; i < size; i++) {
			// If not leaf, recursively call its' children in index i
			// and add 1 do the depth
			if (!this.leaf) {
				ans = ans + childrenPointers[i].toString(depth + 1);
			}
			ans = ans + keysArr[i] + "_" + depth + ",";
		}
		// recursively print the node rooted in last children
		// and add 1 do the depth
		if (!leaf) {
			ans = ans + childrenPointers[i].toString(depth + 1);
		}
		return ans;
	}
	
	// Given a password, check if its' a part of the tree
	public boolean search(String password) {
		int i = 0;
		while (i < size - 1 && password.compareTo(keysArr[i]) > 0) {
			i++;
		}
		if (i < size - 1 && password.compareTo(keysArr[i]) == 0) {
			return true;
		}
		// If couldn't find it by getting to a leaf, than its not in the tree
		else if (leaf) {
			return false;
		}
		return childrenPointers[i].search(password);
	}
	
	// Split the child in the given index
	public void splitChild(int index) {
		BTreeNode toSplit = childrenPointers[index];
		BTreeNode newNode = new BTreeNode(t); // Allocate a new node to store t-1 keys
		newNode.leaf = toSplit.leaf; newNode.size = t - 1;
		// Transport t-1 keys to the new node
		for (int j = 0; j < t - 1; j++) {
			newNode.keysArr[j] = toSplit.keysArr[j+t];
		}
		// Transport t childrenPointers
		if (!toSplit.leaf) {
			for (int j = 0; j < t; j++) {
				newNode.childrenPointers[j] = toSplit.childrenPointers[j+t];
			}
		}
		// Transport fathers' pointers
		for (int j = size; j >= index+1; j--) {
			childrenPointers[j+1] = childrenPointers[j];
		}
		childrenPointers[index + 1] = newNode;
		// Transport the keys in the parent to the left
		for (int j = size - 1; j >=index; j--) keysArr[j+1] = keysArr[j];
		// Copy the middle key
		keysArr[index] = toSplit.keysArr[t-1];
		size++;	toSplit.size = t-1;
	}
	
	// Insert key to a node with less than 2t-1 keys
	public void insertNonFull(String password) {
		int i = size;
		if (leaf) {
			// Find where to insert and make place
			while (i > 0 && password.compareTo(keysArr[i-1]) < 0) {
				keysArr[i] = keysArr[i-1];
				i--;
			} 
			keysArr[i] = password; 
			size++;
		} 
		else {
			// Find in which child keep the searching
			while (i > 0 && password.compareTo(keysArr[i-1]) < 0) {
				i--;
			}
			// If the suited node is full, split it
			if (childrenPointers[i].size == 2*t - 1) {
				splitChild(i);
				if (password.compareTo(keysArr[i]) > 0) {
					i++;
				}
			} 
			childrenPointers[i].insertNonFull(password);
		}
	}
	
	public void delete (String password) {
		// Find the first index that the key in it is greater than the password
		int index = findIndex(password);
		if (index < size && keysArr[index].compareTo(password) == 0) { // The password is in this node 
			if(leaf) {
				deleteFromLeaf(index);
			}
			else {
				deleteFromNonLeaf(password, index);							
			}
		}
		else if (!leaf) {
			if (keysArr[index].compareTo(password) < 0) { // The password is greater than the key in this index
				deleteFromSubTree(password, index + 1);
			}
			else { // Same for left subtree
				deleteFromSubTree(password, index);
			}
		}
	}

	public int findIndex(String password) { 
	    int index = 0; 
	    while (index + 1 < size && keysArr[index].compareTo(password) < 0) {
	    	index++;
	    }
	    return index; 
	}
	
	private void deleteFromSubTree(String password, int index) {
		if (childrenPointers[index].size < t) { // Check if node has at least t keys
			prepereNode(index);
			if (isNewRoot) {
				delete(password);
			} 
			else {
				childrenPointers[index].delete(password); // Delete recursively			
			}
		} 
		else {
			childrenPointers[index].delete(password);
		}
	}
	
	// Delete key and move other keys
	public void deleteFromLeaf(int index) {
		for (int i = index; i < size - 1; i++) { 
			keysArr[i] = keysArr[i+1];
		}
		size--;
	}
	
	private void prepereNode(int index) {		
		// If left brother has at least t keys, borrow a key from it
		if (index >= 1 && childrenPointers[index-1].size >= t) {						
			shiftFromLeft(index);
		} else if (index + 1 <= size && childrenPointers[index+1].size >= t) { // If right brother has at least t keys, borrow a key from it
			shiftFromRight(index);
		}
		// merge last child
		else if (index == size && childrenPointers[index-1].size == t - 1) {
			merge(index-1);
		}
		// merge two inner children
		else if (childrenPointers[index+1].size == t - 1) { 
			merge(index);
		} 
	}

	public void deleteFromNonLeaf(String password, int index) {
		BTreeNode node1 = childrenPointers[index];
		BTreeNode node2 = childrenPointers[index+1];
		// if node1 has at least t keys, replace password with its' predecessor
		if (node1.size >= t) { 
			BTreeNode pred = node1.predecessor(password);
			keysArr[index] = pred.keysArr[pred.size-1];
			pred.keysArr[pred.size-1] = password;
			node1.delete(password);
		}
		// if node2 has at least t keys, replace password with its' successor
		else if (node2.size >= t){
			BTreeNode suc = node2.successor(password);
			keysArr[index] = suc.keysArr[0];
			suc.keysArr[0] = password;
			node2.delete(password);	
		}
		else {
			merge(index);
			delete(password);
		}
	}

	public void shiftFromLeft(int index) {
		BTreeNode node = childrenPointers[index];
		BTreeNode brother = childrenPointers[index-1];
		// Swap last brother key with fathers' key in index
		String toFather = brother.keysArr[brother.size-1]; 
		String father = keysArr[index-1];
		keysArr[index-1] = toFather;		
		// make place for the new key by moving all key to index+1
		for (int i = node.size-1; i >= 0; i--) {
			node.keysArr[i+1] = node.keysArr[i];
		}
		// Organize node's children pointers
		if (!node.leaf) {
			for (int i = node.size-1; i >= 0; i--) {
				node.childrenPointers[i+1] = node.childrenPointers[i];
			}	
		}
		// move the last child of brother to be the first child of node
		if (!brother.leaf) {
			node.childrenPointers[0] = brother.childrenPointers[brother.size];
		}
		node.keysArr[0] = father; 
		node.size ++;
		brother.size--;
	}
	
	public void shiftFromRight(int index) {
		BTreeNode node = childrenPointers[index];
		BTreeNode brother = childrenPointers[index+1];
		// Swap first brother key with fathers' key in index
		String toFather = childrenPointers[index+1].keysArr[0];
		String father = keysArr[index];
		node.keysArr[node.size] = father;
		keysArr[index] = toFather;
		// transport other keys & delete the key
		if (!brother.leaf) {
			node.childrenPointers[node.size+1] = brother.childrenPointers[0];
		} brother.size--;
		// Organize brothers' keys array
		for (int i = 0; i < brother.size; i++) { 
			brother.keysArr[i] = brother.keysArr[i+1];
		}
		// Organize brothers' children pointers
		for (int i = 0; i <= brother.size; i++) {
			brother.childrenPointers[i] = brother.childrenPointers[i+1];
		} node.size++;
	} 
	
	// Merge two nodes with t-1 keys
	public void merge(int index) {
		BTreeNode node = childrenPointers[index]; BTreeNode brother = childrenPointers[index+1];
		// Put fathers' key in its child
		node.keysArr[node.size] = keysArr[index];
		// Check if merged with root (and created a new root by it)
		boolean root = size == 1;
		if (size != 1) mergeRoot(index);
		for (int i = 0; i < t - 1; i++) { // Transfer keys from brother
			node.keysArr[node.size+1+i] = brother.keysArr[i]; // 
		}		
		if (!brother.leaf) { // Transfer brothers' children pointers
			for (int i = 0; i < t; i++)
				node.childrenPointers[size+1+i] = brother.childrenPointers[i];
		}
		for (int i = index + 1; i < size + 1; i++) { // Organize father pointers
			childrenPointers[i] = childrenPointers[i+1];
		}
		childrenPointers[index].size = 2*t-1; // Update size
		if (root) updateRoot(node);
	}
	
	private void mergeRoot(int index) {
		size--;	
		for (int i = index; i < size; i++) // Organize father keys
			keysArr[i] = keysArr[i+1];
	}
	
	// Update root fields in case we merged it
	private void updateRoot(BTreeNode node) {
		 this.keysArr = node.keysArr;
		 this.childrenPointers = node.childrenPointers;
		 this.size = node.size;
		 this.leaf = node.leaf;
		 isNewRoot = true;
	}

	public BTreeNode predecessor(String password) {
		if (!leaf) {
			 return childrenPointers[size].predecessor(password);
		}
		return this;		
	}
	
	public BTreeNode successor(String password) {
		if (!leaf) {
			 return childrenPointers[0].successor(password);
		}
		return this;		
	}
	
	public int getSize() {
		return size;
	}
	
	public void setLeaf(boolean leaf) {
		this.leaf = leaf;
	}
	
	public void setChildrenPointers(BTreeNode node, int i) {
		this.childrenPointers[i] = node;
	}	
}