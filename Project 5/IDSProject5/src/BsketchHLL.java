import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class BsketchHLL {

	    //given size of bitmap
		int m=4000;
		int l=128;
		int k=3;
		int A[][]=new int[m][l];

		
		String[] flowIDs;
		HashMap<String,Integer> given;
		HashMap<String,Integer> estimatedVals;
		
		int[] randomHashes;
		Random random = new Random();
		int hash=Math.abs(random.nextInt(Integer.MAX_VALUE-1));

	BsketchHLL(int n,String[] fids){
		
		flowIDs=fids;	
		randomHashes=new int[k];
		fillRandomValues(randomHashes);
		
		int estSpread=0;
		int fSpread=0;
		
		estimatedVals=new HashMap();
		given=new HashMap();
	
		
		for(int i=0;i<n;i++) {
			
			// splits into flowid and its spread size
			String[] pairs= flowIDs[i].split("\\s+");
			fSpread=Integer.parseInt(pairs[1]);;
			given.put(pairs[0],fSpread);
			
			//record all the elements into the physical bitmap;
			record(pairs[0],fSpread);
		}
		
		
	}
	
	public void fillRandomValues(int[] array) {
		
		 //Use sets so that it wont store duplicates
		 Set<Integer> vals=new HashSet<>();
		 for(int i=0;i<array.length;i++) {
		 	//taking the largest possible so that we can get unique values, no chance of repetition, and math.abs to ensure positive numbers
		 	vals.add(Math.abs(random.nextInt(Integer.MAX_VALUE - 1)+1));
		 }

		 //Store it in the array
		 Iterator<Integer> it =vals.iterator();
		 int i=0;
		 while(it.hasNext()) {
		 	array[i]=it.next();	
		 	i++;
		 }
		 	
	 }
	
	public void record(String f,int s) {
		
		//create an array with random numbers as its elements;
		int flowElements[]=new int[s];
		fillRandomValues(flowElements);
		
		for(int i=0;i<flowElements.length;i++) {
			
			for(int j=0;j<k;j++) {
				
				int row=(Math.abs(f.hashCode())^randomHashes[j])%m;
				
				//this is H(e)
				int hashedValue=flowElements[i]^hash;
				int col=hashedValue%l;
				
				//This is geometric hash G'(e)
				int geoHash=G(flowElements[i]);
				
				A[row][col]=Math.max(A[row][col], geoHash);	
			}
		}		
	}

	public int estimatedSpread(String f) {
		
		//the constant alpha value
		double a=((0.7213)/(1+(1.079/l)));
		double estimated=999999.9;
		
		//this is the calculated spread
		int count=0;
		
		//calculating Harmonic sum
				
		for(int i=0;i<k;i++) {
			
			int row=(Math.abs(f.hashCode())^randomHashes[i])%m;
			double harmonic=0;
			double bitVal;
			
			for(int j=0;j<l;j++) {		
				
					bitVal=Math.pow(2, A[row][j]);
					harmonic+=(1/bitVal);				
			}
			
			//formula to calculate estimated spread of a flow
			estimated= Math.min(estimated,(a)*(Math.pow(l, 2))*(1/harmonic)) ;
		}	
		
		count=(int)estimated;		
		return count;	
	}
	
	public int G(int n) {
			
			int count=Integer.numberOfLeadingZeros(n);
	
			return count;
		}

	public List<Map.Entry<String, Integer>> sort() {
		
		//since we cannot sort a map we turn it into an list of map entries
		List<Map.Entry<String, Integer>> l = new ArrayList<>(estimatedVals.entrySet());
		
		//sort in descending order based on values
	    Collections.sort(l, ((Map.Entry<String, Integer> a,
	        Map.Entry<String, Integer> b) -> b.getValue() - a.getValue()));
	    
	    return l;
	}
	
	public static void main(String[] args) {
		
		// TODO Auto-generated method stub
		try(BufferedReader br = new BufferedReader(new FileReader("project5input.txt"))){
			int n = Integer.parseInt(br.readLine());
            String[] fids=new String[n+1];
            
            for(int i=0;i<n;i++) {
	    		fids[i]=br.readLine();
	    	}
            
            BsketchHLL bll=new BsketchHLL(n,fids);
	  
            //Create a new file
	    	File outputFile= new File("OutputBSketchHLL.txt");	
	    	FileOutputStream opt= new FileOutputStream(outputFile);
	    	BufferedWriter buff = new BufferedWriter(new OutputStreamWriter(opt));
	    	
	    	for(int i=0;i<n;i++) {
	    		String flow=(bll.flowIDs[i]).split("\\s+")[0];
	    		
	    		int est=bll.estimatedSpread(flow);
	    		
	    		bll.estimatedVals.put(flow,est);
	    	}
	         
	    	List<Map.Entry<String, Integer>> sortedList=bll.sort();
	    	
	    	for (int i = 0; i <25; i++) {
	    		
	        	 //reassigning to a map entry 
	      
	        	 String f=sortedList.get(i).getKey();
	             buff.write("flowID: "+f +" "+"   estimated value: "+" "+bll.estimatedVals.get(f)+"    actual value: "+bll.given.get(f));
	             buff.newLine();
	         }
	    	
	    	
	    	
	         buff.close();    

		}catch (Exception e) {
            e.printStackTrace();
        }

	}
	
	
}
