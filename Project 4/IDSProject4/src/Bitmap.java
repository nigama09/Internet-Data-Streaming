import java.io.*;
import java.util.*;

public class Bitmap {
	
	//given bitmap size is m
	int m=10000;
	int B[]=new int[m];
	
	//an array for each flow elements
	int flow[];
	Random random = new Random();
	
	Bitmap(int l) {
		
		//creating an array with l number of flow elements, that is the given spread  
		flow=new int[l];
		
		
		//fill with unique random numbers
		fillRandomElements(flow);
		
		//selecting a random positive value		
		int hash=Math.abs(random.nextInt(Integer.MAX_VALUE-1));
		
		//just to make sure they are all set to 0
		Arrays.fill(B, 0);
		
		//hashing and recording bits to 1 
		for(int i = 0;i<l;i++)
        {
			int index= (flow[i]^hash)%m;
			B[index]=1;
        }
		
	}
		
	public void fillRandomElements(int[] flow) {
			
		 //Use sets so that it wont store duplicates
		 Set<Integer> vals=new HashSet<>();
		 for(int i=0;i<flow.length;i++) {
		 	//taking the largest possible so that we can get unique values, no chance of repetition, and math.abs to ensure positive numbers
		 	vals.add(Math.abs(random.nextInt(Integer.MAX_VALUE - 1)+1));
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
		
		//u is the number of zeroes in Bitmap which is a integer
		// for sake of convenience we take it as double at the beginning because later we will be dividing it with m an integer to get the ratio 
		double u=0;
		
		//this is the calculated spread
		int count=0;
		
		//calculating number of zeroes in Bitmap
		for(int i=0;i<m;i++) {
			if(B[i]==0) {
				u++;
			}
		}
		
        // sometimes we have the case of saturated bitmaps, where number of zeroes will be 0 that is, u=0.
		//in case of saturated bitmaps our log value will give infinity.
		
		//formula to calculate estimated spread of a flow
		double estimated = (-1)*m*(Math.log(u/m));
		
		//in case of saturated bitmaps the count value will be '2147483647' as this is the maximum value of an integer
		count=(int)estimated;
		
		
		return count;
		
	}
	
	
	public static void main(String[] args){
		
		//we are taking the given flow spreads as an array for ease of operation
		int[] spreads= {100,1000,10000,100000,1000000};
	
		try {

	    	File outputFile= new File("C:\\Users\\Nigama\\OneDrive\\Desktop\\IDS workspace\\IDSProject4\\BitmapOutput.txt");
	    	FileOutputStream opt= new FileOutputStream(outputFile);

	        BufferedWriter buff = new BufferedWriter(new OutputStreamWriter(opt));

	      //for each flow spread given we create a bitmap
	        for (int i = 0; i <spreads.length; i++) {
	        	Bitmap b=new Bitmap(spreads[i]);
	            buff.write("True Flow Spread: "+ spreads[i] + "\t Estimated Flow Spread: "+ b.estimatedSpread());
	            buff.newLine();
	        }
	    	
	        buff.close();
	
		}
		catch(Exception e) {
			System.out.println("Something went wrong!");
		}

	}

}
