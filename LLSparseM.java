
public class LLSparseM implements SparseM {
	public class MatrixNode {
	    int rIdx, cIdx, val;
	    MatrixNode nextRow, nextCol;       
	    /**constructor with no parameter. */
	    public MatrixNode()  { //Constructor without parameter
	       rIdx = 0;
	       cIdx = 0;
	       val = 0;
	       this.nextCol = null;
	       this.nextRow = null;
	    }       
	    public MatrixNode(int rowIdx, int colIdx, int value)  { //Constructor with three parameter
	       rIdx = rowIdx;
	       cIdx = colIdx;
	       val = value;
	       this.nextCol = null;
	       this.nextRow = null;
	    }
	    public MatrixNode(int rowIdx, int colIdx, int value , MatrixNode colNode, MatrixNode rowNode)  { //Constructor with all parameter
	       rIdx = rowIdx;
	       cIdx = colIdx;
	       val = value;
	       this.nextCol = colNode;
	       this.nextRow = rowNode;
	    } 
	    public String toString(){
	       return "Row ="+rIdx+" Col ="+cIdx+" Value ="+val;
	    }
	}//end of MatrixNode

	int ridx,cidx,val;//node index and value in the node
	int nelements = 0;//#of elements
	int nrows;//#of rows
	int ncols;//#of columns
	LLSparseM south=null;
	LLSparseM east=null;
	LLSparseM head=null;
	LLSparseM tail=null;
	LLSparseM rowNode=null;
	boolean isOk[];
	
	
	public LLSparseM()
	{
		this.south=null;
		this.east=null;
		this.ridx=-1;
		this.cidx=-1;
		this.val=0;
	}

    public LLSparseM(int nr,int nc){
    	this.nrows=nr;
    	this.ncols=nc;
    	isOk=new boolean[nr];
    	for(int i=0;i<nr;i++){
    		LLSparseM tempRowNode=new LLSparseM();
    		if(head == null) 
    		{
    			head=tempRowNode;
    			tail=head;
    		}
    		if(rowNode==null)
    		{
    			rowNode=tempRowNode;
    			tail=rowNode;
    		}
    		else
    		{
    			rowNode.south=tempRowNode;
    			tail=tempRowNode;
    		}
    		for(int j=0;j<nc;j++)
    		{
    			LLSparseM node=new LLSparseM();
    			tail.east=node;
    			tail=node;
    		}
    		rowNode=tempRowNode;
    	}
    }
    //return number of rows
	public int nrows() {
		return nrows;
	}
	//return number of columns
	public int ncols() {
		return ncols;
	}
	//return total number of nonzero elements in the matrix
	public int numElements() {	
		return nelements;
	}
	//return the element at a given entry (ridx, cidx), 
	public int getElement(int ridx,int cidx) {
		LLSparseM rowTemp= this.head;
		while(rowTemp.south != null)
		{
			if(rowTemp.ridx == ridx)
			{
				rowTemp.ridx=ridx;
				break;
			}
			rowTemp = rowTemp.south;
		}
		LLSparseM temp=rowTemp.east;
		while(temp!=null)
		{
			if(temp.cidx==cidx)
			{
				break;
			}
			temp=temp.east;
		}
		if(temp == null){
			return 0;
		}
		return temp.val;
	}
	//set the element at (ridx,cidx) to zero
	public void clearElement(int ridx,int cidx) {
		LLSparseM rowTemp= this.head;
		while(rowTemp.south!=null)
		{
			if(rowTemp.ridx == ridx)
			{
				break;//break while loop after row if found.
			}
			rowTemp = rowTemp.south;
		}
		LLSparseM temp=rowTemp.east;
		while(temp!=null)
		{
			if(temp.cidx == cidx)
			{
				temp.cidx=-1;
				temp.val=0;//
				nelements--;
				break;
			}
			if(temp.cidx ==-1) {
				break;
			}
			temp=temp.east;
		}

	}
	//set the element at (ridx,cidx) to val
	public void setElement(int ridx,int cidx,int val) {
		if(val==0)
		{
			clearElement(ridx,cidx);
			return;
		}
		LLSparseM row_temp= this.head;
		while(row_temp !=null)
		{
			if(row_temp.ridx==-1) 
			{
				row_temp.ridx=ridx;
				break;
			}
			else if(row_temp.ridx == ridx)
			{
				break;
			}
			row_temp = row_temp.south;
		}
		LLSparseM temp=row_temp.east;
		while(temp!=null)
		{
			if(temp.cidx == -1 || temp.cidx == cidx)
			{
				if(temp.cidx == -1)
				{
					nelements++;
					temp.cidx=cidx;
				}
				temp.val= val;
				break;
			}
			temp=temp.east;
		}
	}
	// get indices of non-empty rows, sorted
	public int[] getRowIndices(){
		int [] isnonzero = new int [nrows];
		int index=0;
		int counter = 0;
		LLSparseM currnode= this.head;
		while(currnode != null)
		{
			isnonzero[index] = -1;
			if(currnode.ridx != -1)
			{
				isnonzero[index] = currnode.ridx;
				counter ++;	
			}
			index++;
			currnode=currnode.south;
		}
		int[] ridx = new int[counter];
		int counter2=0;
		
		for(int x : isnonzero)
		{
			if(x != -1)
			{
				ridx[counter2++] = x;
			}
		}
		return ridx;
	}

	// get col indices of a given row
	public int[] getOneRowColIndices(int ridx) {//final
		int[]isnonzero = new int [ncols];
		int index=0;
		int counter=0;
		LLSparseM currnode= this.head;
		while(currnode != null)
		{
			if(currnode.ridx == ridx)
				 break;
			currnode=currnode.south;
		}
		LLSparseM node= currnode.east;
		while(node != null)
		{
			isnonzero[index] = -1;
			if(node.cidx != -1)
			{
				isnonzero[index] = node.cidx;
				counter++;	
			}
			index++;
			node=node.east;
		}
		int[] cidx = new int[counter];
		int counter2=0;
		for(int x : isnonzero)
		{
			if(x != -1)
			{
				cidx[counter2++] = x;
			}
		}
		return cidx;
	}
	// get values of a given row
	public int[] getOneRowValues(int ridx) {
		int [] isnonzero = new int [ncols];
		int index=0;
		int counter = 0;
		LLSparseM row_node= this.head;
		while(row_node != null)
		{
			if(row_node.ridx == ridx)
				 break;
			row_node=row_node.south;
		}
		LLSparseM node= row_node.east;
		while(node != null)
		{
			isnonzero[index] = 0;
			if(node.val != 0)
			{
				isnonzero[index] = node.val;
				counter ++;	
			}
			index++;
			node=node.east;
		}
		int[] values = new int[counter];
		int counter2=0;
		for(int r = 0; r < ncols; ++r){
			if(isnonzero[r] != 0)
				values[counter2++] = isnonzero[r];
		}
		return values;
	}
	
	// this vector + otherV
	// return a new vector storing the result
	public SparseM addition(SparseM otherM){
		if((otherM.nrows()!=nrows)||(otherM.ncols()!=ncols))
			return null;
		SparseM newMat = new LLSparseM(nrows, ncols);
		for(int i=0;i<nrows;i++) {
			for(int j=0;j<ncols;j++) {
				int a = this.getElement(i,j);
				int b = otherM.getElement(i, j);
				if(!(a == 0 && b==0)) {
					newMat.setElement(i,j,a+b);
				}
			}
		}
		return newMat;
	}
	// this matrix - otherM
	// return a new matrix storing the result
	public SparseM subtraction(SparseM otherM){
		if((otherM.nrows()!=nrows)||(otherM.ncols()!=ncols))
			return null;
		SparseM newMat = new LLSparseM(nrows, ncols);
		for(int i=0;i<nrows;i++) {
			for(int j=0;j<ncols;j++) {
				int a = this.getElement(i,j);
				int b = otherM.getElement(i, j);
				/*if(otherM.numElements() == 0){
					newM.setElement(i,j,a);
				}*/
				
				if(!(a == 0 && b==0)) {
					newMat.setElement(i,j,a-b);
				}
			}
		}
		return newMat;
	}
	// this matrix .* with otherM
	// return a new matrix storing the result
	public SparseM multiplication(SparseM otherM){
		if(otherM.numElements()==0){
	    	   return otherM;
	       }
	    if(this.numElements()==0){
	    	   return this;
	    }
		if((otherM.nrows()!=nrows)||(otherM.ncols()!=ncols))
			return null;
		SparseM newMat = new LLSparseM(nrows, ncols);
		for(int i=0;i<nrows;i++) {
			for(int j=0;j<ncols;j++) {
				int a = this.getElement(i,j);
				int b = otherM.getElement(i, j);
				if((a != 0 && b!=0)) {
					newMat.setElement(i,j,a*b);
				}
			}
		}
		return newMat;
	}//multiplication
}
