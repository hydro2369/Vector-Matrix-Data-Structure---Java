
public class LLSparseVec implements SparseVec {
   public class ArrayNode{
		int [] data;
		ArrayNode next;
		
		//constructor with 0 parameter:
		public ArrayNode(){
			data = new int[2];
			this.data[0] = 0; //data[0] is the index
			this.data[1] = 0; //data[1] is the value of the node
			this.next = null;
		}
		//constructor with 2 parameters
		public ArrayNode(int idx, int val){
			data = new int[2];
			this.data[0] = idx;
			this.data[1] = val;
			this.next =null;
		}
		//constructor with 3 parameters
		public ArrayNode (int idx, int val, ArrayNode n){
			data = new int[2];
			this.data[0] = idx;
			this.data[1] = val;
			this.next = n;
		}
		public String toString(){
			return data.toString();
		}
		
	}	
   private ArrayNode first = new ArrayNode();
   private ArrayNode last = first;
   private int length = 0;
   private int nonZero = 0;
   private int MaxIdx = -1;
   
   public void printOut(){ // for Testing LLS
      ArrayNode p = new ArrayNode();
      p=first.next;
      while(p!=null)
      {
         System.out.println(p.data[0]+" "+p.data[1]);
         p=p.next;
      }
      
   }
    public int getNonZero() {
       return nonZero;
    }
    public LLSparseVec(int len){
       length=len;
       return;
    }

	public int getLength() {
		return length;
	}

	public int numElements() {
		return nonZero;
	}

	public int getElement(int idx) {
       if (length==0 || nonZero==0 || idx>length) return 0;
       ArrayNode p = first.next; 
       while(p!=null){
          if (p.data[0]==idx) {
             return p.data[1];
          }
          p=p.next;
       }   
       return 0;
	}

	public void clearElement(int idx) {
	   if (length==0 || nonZero==0 || idx>length) return;
	   
	   ArrayNode preNode = first;
	   ArrayNode p = first.next; //Set a pointer to know where to do the delete.
	   while(p!=null){
	      if (p.data[0]==idx) {
	         preNode.next=p.next; 
	         --nonZero;
	         break;
	      }
	      preNode=p;
	      p=p.next;
	   }      	   
	}

	public void setElement(int idx, int val) {
	   	if (length==nonZero){
	   	   System.out.println("Out of Bounds, Can't accept new data...");
	   	   return;
	   	}
	   	if(val==0)
		{
			clearElement(idx);
			return;
		}
	   	   ArrayNode Node = new ArrayNode(idx, val); 
	   	if (MaxIdx<idx){ // check add the new Node to the back
           last.next = Node;
           last = Node;
           ++nonZero;
           MaxIdx=idx;
        }
	   	else if (MaxIdx>idx){ // check add the new before the MaxIdx
	   	   ArrayNode preNode = new ArrayNode();
           ArrayNode p = new ArrayNode();
           preNode = first;
	   	   p = first.next;

		   while(p!=null){ 
		      if (p.data[0]>idx) {  
		         preNode.next = Node;
		         Node.next = p;
		         ++nonZero;
		         return;
		      }
		      else if (p.data[0]==idx){
		         p.data[1] = val;
		         return;
		      }

		      preNode=p;
		      p=p.next;
		      
		   }   
		}
	   	else{
	   	   last.data[1]=val; 
	   	}
	   	//this.printOut(); // Print all node
	}// End setElement;

	public int[] getAllIndices() {
		int [] idx;
		idx = new int[nonZero];
	    ArrayNode p = first.next; 
	    int i=0;
	    while(p!=null){
	       idx[i]=p.data[0];
	       ++i;
	       p=p.next;
	       }           		
		return idx;
	}

	public int[] getAllValues() {
       int [] val;
       val = new int[nonZero];
       ArrayNode p = first.next; //Set a pointer to know where to do the delete.
       int i=0;
       while(p!=null){
          val[i]=p.data[1];
          ++i;
          p=p.next;
          }                
       return val;
   }

	public SparseVec addition(SparseVec otherV){
	   if (otherV.getLength()!=this.length) return null;
	   LLSparseVec newVec = new LLSparseVec(this.length);
	   int [] othIdx = otherV.getAllIndices();
	   int [] othVal = otherV.getAllValues();
	   int [] thisIdx = this.getAllIndices();
	   int [] thisVal = this.getAllValues();

//For test outcome
/*	   for(int i=0; i<othIdx.length; i++ ){
	      System.out.println("Array - o"+othIdx[i]+ " " + othVal[i]);
	   }
       for(int i=0; i<thisIdx.length; i++ ){
          System.out.println("Array - t"+thisIdx[i]+ " " + thisVal[i]);
       }
*/
	   int i=0, j=0;  // Use two pointer to read the Array
	   while (i!=othIdx.length || j!=thisIdx.length)
	   {
	      if (othIdx[i] > thisIdx[j]){
	    	 newVec.setElement(thisIdx[j], thisVal[j]);
	         ++j;
	      }
	      else if(othIdx[i] < thisIdx[j]){
	    	 newVec.setElement(othIdx[i], othVal[i]);
	         ++i;    
	      }
	      else { //equal
	         if (othVal[i]+thisVal[j]!=0){  //  avoid result equal "0";
	        	newVec.setElement(othIdx[i],othVal[i]+thisVal[j]);
	         }
	         ++i;
	         ++j;       
	      }
	   }	   
	   return newVec;
	}

	public SparseVec subtraction(SparseVec otherV) {
       if (otherV.getLength()!= this.getLength()) return null;
       LLSparseVec newVec = new LLSparseVec(this.getLength());
       int [] othIdx = otherV.getAllIndices();
       int [] othVal = otherV.getAllValues();
       int [] thisIdx = this.getAllIndices();
       int [] thisVal = this.getAllValues();
       if(otherV.numElements() == 0){
    	   for(int i = 0; i<thisIdx.length;i++){
    		   newVec.setElement(thisIdx[i],thisVal[i]);
    	   }
    	   return newVec;
       }
       if(this.numElements() == 0){
    	   for(int i =0; i< othIdx.length;i++){
    		   newVec.setElement(othIdx[i],0 - othVal[i]);
    	   }
    	   return newVec;
       }
       int i=0, j=0;
/*     for(int i=0; i<othIdx.length; i++ ){
          System.out.println("Array - o"+othIdx[i]+ " " + othVal[i]);
       }
       for(int i=0; i<thisIdx.length; i++ ){
          System.out.println("Array - t"+thisIdx[i]+ " " + thisVal[i]);
       }
*/
       
       while (i!=othIdx.length || j!=thisIdx.length)
       {
          if (othIdx[i] > thisIdx[j]){
        	 newVec.setElement(thisIdx[j], thisVal[j]);
             ++j;
          }
          else if(othIdx[i] < thisIdx[j]){
        	 newVec.setElement(othIdx[i], 0 - othVal[i]);
             ++i;    
          }
          else { //equal
             if (othVal[i]!=thisVal[j]){  //  avoid result equal "0";
            	newVec.setElement(othIdx[i],thisVal[i]-othVal[j]);
             }
             ++i;
             ++j;       
          }
       }       
       return newVec;
	}

	public SparseVec multiplication(SparseVec otherV) {
       if (otherV.getLength()!=this.length) return null;
       if(otherV.numElements() == 0){
    	   return otherV;
       }
       if(this.numElements() == 0){
    	   return this;
       }
       LLSparseVec newVec = new LLSparseVec(this.length);
       int [] othIdx = otherV.getAllIndices();
       int [] othVal = otherV.getAllValues();
       int [] thisIdx = this.getAllIndices();
       int [] thisVal = this.getAllValues();
       
       
/*     for(int i=0; i<othIdx.length; i++ ){
          System.out.println("Array - o"+othIdx[i]+ " " + othVal[i]);
       }
       for(int i=0; i<thisIdx.length; i++ ){
          System.out.println("Array - t"+thisIdx[i]+ " " + thisVal[i]);
       }
*/
       int i=0, j=0;
       while (i!=othIdx.length || j!=thisIdx.length)
       {
          if (othIdx[i] > thisIdx[j]){
            // add2V.setElement(thisIdx[j], thisVal[j]*0);
        	  //do nothing because anything times 0 = 0
             ++j;
          }
          else if(othIdx[i] < thisIdx[j]){
        	//do nothing because anything times 0 = 0
             //add2V.setElement(othIdx[i], othVal[i]*0);
             ++i;    
          }
          else { //equal
        	 newVec.setElement(othIdx[i],othVal[i]*thisVal[j]);
             ++i;
             ++j;       
          }
       }       
       return newVec;
	}

}
