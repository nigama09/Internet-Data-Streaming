
import java.util.*;
import java.io.*;

public class BloomFilter {
	
	int numElements;
    int numBits;
    int numHashes;
    int[] bitMapArray;
    int[]randomHashes;
    
    Random rand=new Random();
    
    //Inorder to ensure that we fill in unique random elements in our sets we use this set to keep track of random elements
    Set<Integer> uniqueSetElements = new HashSet<>();
    
    BloomFilter(int e, int b, int k) {
        numElements = e;
        numBits = b;
        numHashes=k;
        bitMapArray = new int[b];
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
    
    
    //filling the two given sets A and B
    public int[] fillBloomFilter(){
    	
       HashMap<Integer, int[]> setA = new HashMap<>();
       fillSets(setA, uniqueSetElements);
       encode(setA);
       int count1 = lookup(setA);
       
       HashMap<Integer, int[]> setB = new HashMap<>();
       fillSets(setB, uniqueSetElements);
       int count2 = lookup(setB);
       
       int result[]= {count1,count2};
       return result;
    }
    
    
    //Generating random values for A and B
    public void fillSets(HashMap<Integer, int[]> m, Set<Integer> uniqueSetElements) {
    	//Initialize an element
    	int e=rand.nextInt(Integer.MAX_VALUE - 1);
    	//generate 1000 unique random numbers
        for(int i = 0; i < numElements; i++) {
            //We keep track of values so that we have unique elements
            while(uniqueSetElements.contains(e)) {
            	e=rand.nextInt(Integer.MAX_VALUE - 1);
            }
            
            uniqueSetElements.add(e);
            
            //generate hash values for element
            int[] hashVals = hashXOR(e);
            m.put(e, hashVals);
            
        }
    }

    //Encode all Bits of the seen value to 1
    public void encode(HashMap<Integer, int[]> m) {
    	
    	  for(int[] hashVals :m.values()) {
    		  //int[] toMark= hashVals;
    		  //take all the hashes of the element and mark all those indexes as 1 if we store.
    		  for(int i: hashVals) {
    			  bitMapArray[i%numBits]=1;
    		  }
    	  }
    	
    }

    
    //Check if the value exists in the table
    public int lookup(Map<Integer, int[]> m){
    	
        int count = numElements;
        
        for(int[] hashVals :m.values()) {
  		 // int[] toMark= hashVals;
  		  for(int i: hashVals) {
  			  //if any one of the index says 0 then it is not marked hence remove it
  			  if(bitMapArray[i%numBits]==0) {
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
	    if(args.length==3) {
	    	try {
	    		//take command line arguments one by one.
	    	int e=Integer.parseInt(args[0]);
	    	int b=Integer.parseInt(args[1]);
	    	int k=Integer.parseInt(args[2]);
	    	
	    	BloomFilter bf=new BloomFilter(e,b,k);
	    	
	    	//Create a new file
	    	File outputFile= new File("C:\\Users\\Nigama\\OneDrive\\Desktop\\IDS workspace\\IDSProject2\\OutputProg1.txt");
	    	
	    	//read into one file from another
	    	FileOutputStream opt= new FileOutputStream(outputFile);

	        BufferedWriter buff = new BufferedWriter(new OutputStreamWriter(opt));
	         
	        int[] counts = bf.fillBloomFilter();
	        buff.write("The number of elements of A are: " + Integer.toString(counts[0]));
	        buff.newLine();
	        buff.write("The number of elements of B are: " + Integer.toString(counts[1]));
	        buff.close();
	    	
	    	}
	    	
	    	catch(Exception e) {
	    		System.out.println("Invalid type");
	    	}
						
		}

	}
    
    
    
    
}