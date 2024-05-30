import java.io.*;
import java.util.*;

public class VirtualBitmap {
	
	//given size of bitmap
	int m=500000;
	int B[]=new int[m];
	int v_b=0;
	
	//given size of virtual bitmap
	int l=500;
	
	String[] flowIDs;
	//an array for each flow elements
	int flow[];
	
	
	int[] randomHashes;
	Random random = new Random();
	int hash=Math.abs(random.nextInt(Integer.MAX_VALUE-1));
	
	HashMap<String,Integer> given;
	HashMap<String,Integer> estimatedVals;
	
	VirtualBitmap(int n,String[] fids){
		
		flowIDs=fids;	
		randomHashes=new int[l];
		fillRandomValues(randomHashes);
		int estSpread=0;
		int fSpread=0;
		estimatedVals=new HashMap();
		given=new HashMap();
		
		
		//just to make sure they are all set to 0
		Arrays.fill(B, 0);
		
		for(int i=0;i<n;i++) {
			
			// splits into flowid and its spread size
			String[] pairs= flowIDs[i].split("\\s+");
			
			fSpread=Integer.parseInt(pairs[1]);;
			given.put(pairs[0],fSpread);
			
			//record all the elements into the physical bitmap;
			record(pairs[0],fSpread);
		}
		
		//count number of zeroes;
		for(int i=0;i<m;i++) {
			if(B[i]==0) {
				v_b++;
			}
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
			
			//hash1
			int index=(flowElements[i]^hash)%l;
			
			//hash2 and then record in physical bitmap
			B[(Math.abs(f.hashCode())^randomHashes[index])%m]=1;
		}
		
	}

			
	
	public int query(String flow) {
		int v_f=0;
		int f=Math.abs(flow.hashCode());
		
		for(int i=0;i<l;i++) {
			if(B[(f^randomHashes[i])%m]==0) {
				v_f++;
			}
		}
        double Vf=(double)v_f/ l;
        double Vb=(double)v_b/ m;
        
        int estimate=(int)(Math.abs((l*Math.log(Vb))-(l*Math.log(Vf))));
        return estimate;
	}
	
	public static void main(String[] args) {
		
		// TODO Auto-generated method stub
		try(BufferedReader br = new BufferedReader(new FileReader("project5input.txt"))){
			int n = Integer.parseInt(br.readLine());
            String[] fids=new String[n+1];
            
            for(int i=0;i<n;i++) {
	    		fids[i]=br.readLine();
	    	}
            
            VirtualBitmap v=new VirtualBitmap(n,fids);
	  
            //Create a new file
	    	File outputFile= new File("PlotPoints.txt");	
	    	FileOutputStream opt= new FileOutputStream(outputFile);
	    	BufferedWriter buff = new BufferedWriter(new OutputStreamWriter(opt));
	    	
	    	for(int i=0;i<n;i++) {
	    		String flow=(v.flowIDs[i]).split("\\s+")[0];
	    		
	    		int est=v.query(flow);
	    		
	    		v.estimatedVals.put(flow,est);
	    		
	    		buff.write(v.given.get(flow)+" "+v.estimatedVals.get(flow));
	            buff.newLine();
	    	}
	         
	         buff.close();    

		}catch (Exception e) {
            e.printStackTrace();
        }

	}

}
