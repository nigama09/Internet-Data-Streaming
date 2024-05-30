
import java.util.*;
import java.io.*;

public class CodedBloomFilter {
	
	int numSets;
	int numElements;
	int numFilters;
    int numBits;
    int numHashes;
	int[] filter;
    int[]randomHashes;

    Random rand=new Random();
    //we initialize the size so that we can set it to the number of filters later
    int size=0;
    // we take a global map to keep count bloom filter
    HashMap<Integer, int[]> c = new HashMap<>();
    
    
    
    CodedBloomFilter(int s, int e,int f,int b, int k) {
    	numSets=s;
    	numElements=e;
    	numFilters=f;
        numBits = b;
        numHashes=k;
        
        randomHashes = new int[k];
        fillRandomHashes(randomHashes);
        
        //we will be setting the code for our bloom filter
        setCode();
    }
    
    public int randomNumberGenerator(){
        Random random = new Random();
        int x=random.nextInt(Integer.MAX_VALUE - 1);
        return x;
  
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
    
	  //set the code for our bloom filter
	  public void setCode() {
		  
		  //assign a random number to the size to have a large number
		  size=randomNumberGenerator();
		  
		  //apply the core logic of coded bloom filter probability
		  size = (int)Math.ceil(Math.log(numSets+1) / Math.log(2));
		  
		  // reassign the number of filters to the computed values
		  numFilters = size;
		  
		  //store the value in our coded map 
	        for(int i = 0; i < size; i++) {
	            c.put(i,new int[numBits]);
	        }
	        
	  }
    
	
    //filling the sets
    public int fillBloomFilter(){
    	
    	int count = 0;
    	// create a ist of 7 sets that well be using 
    	List<HashMap<Integer, int[]>> sets = new LinkedList<>();
    	//take a set that stores alll values
        HashMap<Integer, int[]> totalSet = new HashMap<>();
        
        //fill all these set values and add it to the list of sets
        for(int i = 0; i < numSets; i++) {
            HashMap<Integer, int[]> m = new HashMap<>();
            fillSets(m, totalSet);
            sets.add(m);
        }
        
        //this will help in looking for the elemnts if they are encoded or not
        HashMap<Integer, String> checker = new HashMap<>();
        
        //this will be used to look up all the sets with the code that we generate
        for(int i = 0; i < sets.size(); i++) {
            String binCode = String.format("%" + size + "s", Integer.toBinaryString(i+1)).replaceAll(" ", "0");
            for(int k = 0; k < binCode.length(); k++) {
                if(binCode.charAt(k) == '1') {
                    encode(sets.get(i), k, checker, binCode);
                }
            }
        }
        

        //this is used for looking up 
        for(HashMap.Entry<Integer, int[]> e : totalSet.entrySet()) {
        	
        	//use a stringbuilder to add the bits
            StringBuilder sb = new StringBuilder();
            
            //decide for each filter if the bit is 1 we map it
            for(int i = 0; i < numFilters; i++) {
                int counterHashes = 0;
                int[] filter = c.get(i);
                int[] hashVals = e.getValue();
                for(int j = 0; j < hashVals.length; j++) {
                    if(filter[hashVals[j] % numBits] == 1) {
                        counterHashes++;
                    }
                }
                
                
                if(counterHashes != randomHashes.length) {
                    sb.append(0);
                } else {
                    sb.append(1);
                }
            }

            if(checker.containsKey(e.getKey())) {
            	if(checker.get(e.getKey()).equals(sb.toString())) {
            	     count++;
            	}
           
            }
        }
        return count;
    	
       
    }
    
    
    //Generating random values for A and B
    public void fillSets(HashMap<Integer, int[]> m1, HashMap<Integer, int[]> m2) {
    	//Initialize an element
    	int e = 0;
    	//generate 1000 unique random numbers
        for(int i = 0; i < numElements; i++) {
            e=rand.nextInt(Integer.MAX_VALUE - 1);
            
            //We keep track of values so that we have unique elements
            
            //while(initialElementsSet.contains(e))
            while(m2.containsKey(e)) {
            	e=rand.nextInt(Integer.MAX_VALUE - 1);
            }
            
            
            
            //generate hash values for element
            int[] hashVals = hashXOR(e);
            m1.put(e, hashVals);
            m2.put(e, hashVals);
            
        }
    }

    //Encode all Bits of the seen value to 1
    public void encode(HashMap<Integer, int[]> m1, int index, HashMap<Integer, String> check, String coded) {
    	
    	int[] bitMapArray = c.get(index);
        for(HashMap.Entry<Integer, int[]> e : m1.entrySet()) {
            check.put(e.getKey(), coded);
            int[] hashVals = e.getValue();
            for (int toMark : hashVals) {
            	//update the bit map to 1 
                bitMapArray[toMark % numBits] = 1;
            }
        }
        int randel=randomNumberGenerator();
        c.replace(index, bitMapArray);

    	
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
	    	int s=Integer.parseInt(args[0]);
	    	int e=Integer.parseInt(args[1]);
	    	int f=Integer.parseInt(args[2]);
	    	int b=Integer.parseInt(args[3]);
	    	int k=Integer.parseInt(args[4]);
	    	
	    	CodedBloomFilter cbf=new CodedBloomFilter(s,e,f,b,k);
	    	
	    	//Create a new file
	    	File outputFile= new File("C:\\Users\\Nigama\\OneDrive\\Desktop\\IDS workspace\\IDSProject2\\OutputProg3.txt");
	    	
	    	//read into one file from another
	    	FileOutputStream opt= new FileOutputStream(outputFile);

	        BufferedWriter buff = new BufferedWriter(new OutputStreamWriter(opt));
	         
	        int result=cbf.fillBloomFilter() ;
	        buff.write("The number of elements whose lookup values are correct are: " + Integer.toString(result));
	        buff.newLine();
	        buff.close();
	    	
	    	}
	    	
	    	catch(Exception e) {
	    		System.out.println("Invalid type");
	    	}
						
		}

	}
    
    
    
    
}