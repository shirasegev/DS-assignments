public class FloorsArrayLink {
   
	private double key;
    private int arrSize;
    private FloorsArrayLink[] pointersNext;
    private FloorsArrayLink[] pointersPrev;
    
    public FloorsArrayLink(double key, int arrSize) {
        this.key = key;
        this.arrSize = arrSize;
        pointersNext = new FloorsArrayLink[arrSize];
        pointersPrev = new FloorsArrayLink[arrSize];
    }

    public double getKey() {
        return key;
    }

    public FloorsArrayLink getNext(int i) {
    	if (i >= 1 & i <= arrSize) {
    		return pointersNext[i-1];
        }
        return null;
    }
	
    public FloorsArrayLink getPrev(int i) {
    	if (i >= 1 & i <= arrSize) {
    		return pointersPrev[i-1];
        }
        return null;
    }
	
    public void setNext(int i, FloorsArrayLink next) {
    	if (i >= 1 & i <= arrSize) {
    		pointersNext[i-1] = next;
        }
    }
	
    public void setPrev(int i, FloorsArrayLink prev) {
    	if (i >= 1 & i <= arrSize) {
    		pointersPrev[i-1] = prev;
        }
    }

    public int getArrSize(){
        return arrSize;
    }
}