
import java.util.*;
import java.io.*;

public class CountMin {

	
	int numFlows;
	//numCounters is nothing but number of Counter Arrays
	int numCounters;
	int countersSize;
	String[] flowIDs;
	
	int[][] counterArray;
	int[] randomHashes;
	
	Random rand=new Random();
	
	HashMap<String,Integer> estSizePairs;
	HashMap<String,Integer> actSizePairs;
	HashMap<String,Integer> calError;
	
	
	CountMin(int n,int k,int w,String[] fids){
		numFlows=n;
		numCounters=k;
		countersSize=w;
		flowIDs=fids;
		
		counterArray=new int[k][w];
		randomHashes=new int[k];
		fillRandomHashes(randomHashes);
		
		estSizePairs=new HashMap();
		calError=new HashMap();
		actSizePairs=new HashMap();
		for(int i=0;i<n;i++) {
			String[] pairs= fids[i].split("\\s+");
			// splits into flowid and its size
			int fSize=Integer.parseInt(pairs[1]);
			actSizePairs.put(pairs[0],fSize);
			record(pairs[0],fSize);
		}
		
		
		
	}
	
	public int randomNumberGenerator(){
        Random random = new Random();
        int x=random.nextInt(Integer.MAX_VALUE - 1);
        return x;
    }
	
	public void fillRandomHashes(int[] randomHashes) {
		
		//Use sets so that it wont store duplicates
		Set<Integer> vals=new HashSet<>();
		for(int j=0;j<numCounters;j++) {
			vals.add(rand.nextInt(3000));
		}
		
		Iterator<Integer> it =vals.iterator();
		int i=0;
		while(it.hasNext()) {
			randomHashes[i]=it.next();
			//System.out.println(randomHashes[i]);
			i++;
		}
	}
	
	public int[] hashXOR(int f) {
		
		//use XOR for each entry
		int[] hashedArray=new int[numCounters];
		for(int i: hashedArray) {
			hashedArray[i]=Math.abs(f^randomHashes[i]);
		}
		return hashedArray;
	}
	
	public void record(String fID,int flowSize) {
		//hash into each counter
//		int[] hashedArray=hashXOR(fID.hashCode());
		for(int i=0;i<numCounters;i++) {
		
			for(int j=0;j<flowSize;j++) {
				int index=Math.abs(fID.hashCode()^randomHashes[i])%countersSize;
				counterArray[i][index]+=1;
			}
			
		}
	}
	
	public void query(String fID) {
		//int[] hashedArray=hashXOR(fID.hashCode());
		int index;
		int result=Integer.MAX_VALUE;
		for(int i=0;i<numCounters;i++) {
			index=Math.abs(fID.hashCode()^randomHashes[i])%countersSize;
			result=Math.min(result, counterArray[i][index]);
		}
		
		estSizePairs.put(fID,result);
		int error=result-actSizePairs.get(fID);
		calError.put(fID,error);
		
	}
	public double getAvgError() {
		double sum=0.0;
        for(int i :calError.values()) {
       	 sum+=i;
        }
        return (sum/calError.size());
	}
	
	
	public List<Map.Entry<String, Integer>> sort() {
		//since we cannot sort a map we turn it into an list of map entries
		List<Map.Entry<String, Integer>> l = new ArrayList<>(estSizePairs.entrySet());
		
		//sort in descending order based on values
	    Collections.sort(l, ((Map.Entry<String, Integer> a,
	        Map.Entry<String, Integer> b) -> b.getValue() - a.getValue()));
	    
	    return l;
	}
	
	
	public static void main(String[] args) {
		
        try (BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\Nigama\\OneDrive\\Desktop\\IDS workspace\\IDSProject3\\project3input.txt"))) {
            int n = Integer.parseInt(br.readLine());
            String[] fids=new String[n+1];
            
            for(int p=0;p<n;p++) {
	    		fids[p]=br.readLine();
	    	}
//	    	int k=Integer.parseInt(args[0]);
//	    	int w=Integer.parseInt(args[1]);
//            System.out.println(fids[0]);
//            System.out.println(fids[n-1]);
            
	    	int k=3;
	    	int w=3000;
	    	
	    	CountMin cm=new CountMin(n,k,w,fids);
	    	
	    	//Create a new file
	    	File outputFile= new File("C:\\Users\\Nigama\\OneDrive\\Desktop\\IDS workspace\\IDSProject3\\OutputProg1.txt");
	    	
	    	FileOutputStream opt= new FileOutputStream(outputFile);
	    	
	    	BufferedWriter buff = new BufferedWriter(new OutputStreamWriter(opt));
	    	
	    	for(int i=0;i<n;i++) {
	    		cm.query((cm.flowIDs[i]).split("\\s+")[0]);
	    	}
	    	
	    	double averageError=cm.getAvgError();
	         buff.write("Average Error = "+averageError);
	         buff.newLine();
	         
	         List<Map.Entry<String, Integer>> sortedList=cm.sort();
	         
	         for (int i = 0; i <100; i++) {
	        	 //reassigning to a map entry 
	      
	        	 String f=sortedList.get(i).getKey();
	             buff.write("flowID: "+f +" "+"   estimated value: "+" "+cm.estSizePairs.get(f)+"    actual value: "+cm.actSizePairs.get(f));
	             buff.newLine();
	         }
	         
	         buff.close();
	    	
            
            
        } catch (Exception e) {
            e.printStackTrace();
          }
	}
        
	
}	