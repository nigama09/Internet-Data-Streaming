import java.io.*;
import java.util.*;

public class ProbabilisticBitmap {
	
	//given bitmap size is m
	int m=10000;
	int B[]=new int[m];
	
	double probability=0.1;
	
	//an array for each flow elements
	int flow[];
	Random random = new Random();
	
	ProbabilisticBitmap(int l) {
		
		//creating an array with l number of flow elements, that is the given spread
		flow=new int[l];
		
		// x has to be an integer whose value can be taken as max 
		int x=Integer.MAX_VALUE-1;
		
		//fill with unique random numbers
		fillRandomElements(flow);
		
		//selecting two postive random numbers for hash function
		int hash1=Math.abs(random.nextInt(Integer.MAX_VALUE-1));
		int hash2=Math.abs(random.nextInt(Integer.MAX_VALUE-1));
		
		//make sure both the hashes are not same
		while(hash1==hash2) {
			hash2=Math.abs(random.nextInt(Integer.MAX_VALUE-1));
		}
		
		//just to make sure they are all set to 0
		Arrays.fill(B, 0);
		
		//hash and record
		for(int i = 0;i<l;i++)
        {
			//this is for H'(e)
			int check= flow[i]^hash1;
			
			//sampling condition
			if(check<(x*probability)) {
				
				//this is H(e)
				int index=(flow[i]^hash2)%m;
				B[index]=1;
				
			}
        }
		
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
		
		//u is the number of zeroes in Bitmap
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
		double estimated = (-1)*(1/probability)*m*(Math.log(u/m));
		
		//in case of saturated bitmaps the count value will be '2147483647' as this is the maximum value of an integer
		count=(int)estimated;
		
		
		return count;
		
	}
	
	
	public static void main(String[] args){
		
		//we are taking the given flow spreads as an array for ease of operation
		int[] spreads= {100,1000,10000,100000,1000000};
			
		try {

	    	File outputFile= new File("C:\\Users\\Nigama\\OneDrive\\Desktop\\IDS workspace\\IDSProject4\\ProbablilsticBitmapOutput.txt");
	    	FileOutputStream opt= new FileOutputStream(outputFile);

	        BufferedWriter buff = new BufferedWriter(new OutputStreamWriter(opt));

	        //for each flow spread given we create a bitmap
	        for (int i = 0; i <spreads.length; i++) {
	        	ProbabilisticBitmap pb=new ProbabilisticBitmap(spreads[i]);
	            buff.write("True Flow Spread: "+ spreads[i] + "\t Estimated Flow Spread: "+ pb.estimatedSpread());
	            buff.newLine();
	        }
	    	
	        buff.close();
	
		}
		catch(Exception e) {
			System.out.println("Something went wrong!");
		}

	}

}
