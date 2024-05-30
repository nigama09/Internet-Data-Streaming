
import java.util.*;
import java.io.*;

public class CountingBloomFilter {
	
	int numElements;
	int numElementsToRemove;
    int numElementsToAdd;
    int numBits;
    int numHashes;
	int[] filter;
    int[]randomHashes;
    
    Random rand=new Random();
    
    
    
    CountingBloomFilter(int e, int r,int a,int b, int k) {
        numElements = e;
		numElementsToRemove=r;
		numElementsToAdd=a;
        numBits = b;
        numHashes=k;
        filter = new int[b];
        randomHashes = new int[k];
        fillRandomHashes(randomHashes);
        
    }
    
    
	  public void fillRandomHashes(int[] randomHashes) {
			
	  	//Use sets so that it wont store duplicates
	  	Set<Integer> vals=new HashSet<>();
	  	for(int j=0;j<numHashes;j++) {
	  		//taking the largest possible so that we can get unique values, no chance of repetition
	  		vals.add(rand.nextInt(Integer.MAX_VALUE - 1)+1);
	  	}

	  	//Store it in the random hashes array
	  	Iterator<Integer> it =vals.iterator();
	  	int i=0;
	  	while(it.hasNext()) {
	  		randomHashes[i]=it.next();
	  		//System.out.println(randomHashes[i]);
	  		i++;
	  	}
	  	
	  }
    
    
	
    //filling the two given sets A 
    public int fillBloomFilter(){
    
    	//create the given set 
       HashMap<Integer, int[]> setA = new HashMap<>();
     //Inorder to ensure that we fill in unique random elements in our sets we use this set to keep track of random elements
       Set<Integer> initialElementsSet = new HashSet<>();
       
       fillSets(setA,initialElementsSet);
       encode(setA);
	   //perform the operations
	   removeElements(setA);
	   addElements(setA);
	   //final lookup
       int count = lookup(setA,initialElementsSet);

       return count;
    }
    
    
    //Generating random values for A 
    public void fillSets(HashMap<Integer, int[]> m,  Set<Integer> initialElementsSet) {
    	//Initialize an element
    	int e=rand.nextInt(Integer.MAX_VALUE - 1);
    	//generate 1000 unique random numbers
        for(int i = 0; i < numElements; i++) {
            
            
            //We keep track of values so that we have unique elements
            
            //while(initialElementsSet.contains(e))
            while(m.containsKey(e)) {
            	e=rand.nextInt(Integer.MAX_VALUE - 1);
            }
            
            initialElementsSet.add(e);
            
            //generate hash values for element
            int[] hashVals = hashXOR(e);
            m.put(e, hashVals);
            
        }
    }

    //Encode all Bits of the seen value to 1
    public void encode(HashMap<Integer, int[]> m) {
    	
    	  for(int[] hashVals :m.values()) {
//    		  int[] toMark= hashVals;
    		  //take all the hashes of the element and mark all those indexes as 1 if we store.
    		  for(int i: hashVals) {
    			  filter[i%numBits]+=1;
    		  }
    	  }
    	
    }

	public void removeElements(HashMap<Integer,int[]>m){
	        int count=0;
			for(int[] hashVals :m.values()) {
    		  //int[] toMark= hashVals;
    		  //take all the hashes of the element and mark all those indexes as 1 if we store.
    		  for(int i: hashVals) {
    			  filter[i%numBits]-=1;
    		  }
			  count++;
			  if(count==numElementsToRemove){
					break;
			  }
    	    }
	}
    
	public void addElements(HashMap<Integer,int[]>m){
	    
			
			//Initialize an element
    	int e=rand.nextInt(Integer.MAX_VALUE - 1);
    	//generate 1000 unique random numbers
    	int count=numElementsToAdd;
        while(count!=0) {
            
            
            //We keep track of values so that we have unique elements
            //while(initialElementsSet.contains(e))
            while(m.containsKey(e)) {
            	e=rand.nextInt(Integer.MAX_VALUE - 1);
            }
            
            
            //generate hash values for element
            int[] hashVals = hashXOR(e);
            m.put(e, hashVals);
			for(int j=0;j<hashVals.length;j++){
				filter[j%numBits]+=1;
			}
            count--;
        }
			
			
	}
	
	
    //Check if the value exists in the table
    public int lookup(Map<Integer, int[]> m, Set<Integer> initialElementsSet){
    	
        int count = numElements;
        
        for(int e :initialElementsSet){
  		  int[] toMark= m.get(e);
  		  for(int i: toMark) {
  			  //if any one of the index says 0 then it is not marked hence remove it
  			  if(filter[i%numBits]==0) {
  				  count--;
  				  break;
  				  
  			  }
  		  }
  	  }
        return count;
        
    }

  

    
    public int[] hashXOR(int flowEntry) {
    	
		//use XOR for each entry
		int[] hashedArray=new int[numHashes];
		for(int i=0;i<numHashes;i++) {
			hashedArray[i]=flowEntry ^ randomHashes[i];
		}
		return hashedArray;
	}
    
    
	public static void main(String[] args) throws IOException {
	    if(args.length==5) {
	    	try {
	    		//take command line arguments one by one.
	    	int e=Integer.parseInt(args[0]);
	    	int r=Integer.parseInt(args[1]);
	    	int a=Integer.parseInt(args[2]);
	    	int b=Integer.parseInt(args[3]);
	    	int k=Integer.parseInt(args[4]);
	    	
	    	CountingBloomFilter cbf=new CountingBloomFilter(e,r,a,b,k);
	    	
	    	//Create a new file
	    	File outputFile= new File("C:\\Users\\Nigama\\OneDrive\\Desktop\\IDS workspace\\IDSProject2\\OutputProg2.txt");
	    	
	    	//read into one file from another
	    	FileOutputStream opt= new FileOutputStream(outputFile);

	        BufferedWriter buff = new BufferedWriter(new OutputStreamWriter(opt));
	         
	        int result=cbf.fillBloomFilter() ;
	        buff.write("The number of elements of A after we remove and add are: " + Integer.toString(result));
	        buff.newLine();
	        buff.close();
	    	
	    	}
	    	
	    	catch(Exception e) {
	    		System.out.println("Invalid type");
	    	}
						
		}

	}
    
    
    
    
}