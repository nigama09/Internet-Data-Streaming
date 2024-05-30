import java.io.*;
import java.util.*;

public class HyperLogLog {
	
	//given bitmap size is m
	int m=256;
	int B[]=new int[m];
	
	//each register containing HLL is of size 5
	int registerSize=5;

	//an array for each flow elements
	int flow[];
	Random random = new Random();
	
	HyperLogLog(int l) {
		
		//creating an array with l number of flow elements, with given flow spread
		flow=new int[l];
		
		
		//fill with unique random numbers
		fillRandomElements(flow);
		
		int hash=Math.abs(random.nextInt(Integer.MAX_VALUE-1));
		
		
		//just to make sure they are all set to 0
		Arrays.fill(B, 0);
		
		//hash and record
		for(int i = 0;i<l;i++)
        {
			//this is H(e)
			int hashedValue=flow[i]^hash;
			
			//This is geometric hash G'(e)
			int geoHash=G(flow[i]);
			
			int index=hashedValue%m;
			B[index]=Math.max(B[index], geoHash);		
        }
		
	}
	public int G(int n) {
		
		int count=Integer.numberOfLeadingZeros(n);

		return count;
	}
	
	public void fillRandomElements(int[] flow) {
		
		 //Use sets so that it wont store duplicates
		 Set<Integer> vals=new HashSet<>();
		 for(int i=0;i<flow.length;i++) {
		 	//taking the largest possible so that we can get unique values, no chance of repetition, and math.abs to ensure positive numbers
		 	vals.add(Math.abs(random.nextInt(Integer.MAX_VALUE-1))+1);
		 }

		 //Store it in the array
		 Iterator<Integer> it =vals.iterator();
		 int i=0;
		 while(it.hasNext()) {
		 	flow[i]=it.next();	
		 	i++;
		 }
		 	
	 }
	
	public int estimatedSpread() {
		
		//the constant alpha value
		double a=((0.7213)/(1+(1.079/m)));
		
		//this is the calculated spread
		int count=0;
		
		//calculating Harmonic sum
		double harmonic=0;
		double bitVal;
		for(int i=0;i<m;i++) {
			bitVal=Math.pow(2, B[i]);
			harmonic+=(1/bitVal);
		}
		
		//formula to calculate estimated spread of a flow
		double estimated = (a)*(Math.pow(m, 2))*(1/harmonic);
		
		count=(int)estimated;
		
		
		return count;
		
	}
	
	
	public static void main(String[] args){
		

		int[] spreads= {1000,10000,100000,1000000};
			
		
		try {

	    	File outputFile= new File("C:\\Users\\Nigama\\OneDrive\\Desktop\\IDS workspace\\IDSProject4\\HLLOutput.txt");
	    	FileOutputStream opt= new FileOutputStream(outputFile);

	        BufferedWriter buff = new BufferedWriter(new OutputStreamWriter(opt));

	        for (int i = 0; i <spreads.length; i++) {
	        	HyperLogLog hll=new HyperLogLog(spreads[i]);
	            buff.write("True Flow Spread: "+ spreads[i] + "\t Estimated Flow Spread: "+ hll.estimatedSpread());
	            buff.newLine();
	        }
	    	
	        buff.close();
	
		}
		catch(Exception e) {
			System.out.println("Something went wrong!");
		}

	}

}
