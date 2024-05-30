import java.io.*;
import java.util.*;


public class MultiHashingTable {

	int numTableEntries;
	int numFlows;
	int numHashes;
	int[] tableVals;
	int[] randomHashes;
	Random rand=new Random();
	
	MultiHashingTable(int n,int f,int k){
		numTableEntries=n;
		numFlows=f;
		numHashes=k;
		tableVals=new int[n];
		randomHashes=new int[k];
		fillRandomHashes(randomHashes);
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
	
	public int fillHash(){
		int c=0;
		int index=0;
		int k=randomNumberGenerator();
		for(int i=0;i<numFlows;i++) {
			int f=rand.nextInt(3000);
			int[] hashedArray=hashXOR(f);
			for(int j=0;j<numHashes;j++) {
				index=hashedArray[j]%numTableEntries;
				if(tableVals[index]==0) {
					tableVals[index]=f;
					c++;
					break;
				}
			}
		}
		System.out.println(c);
		return c;
	}
	
	public int[] hashXOR(int f) {
		//use XOR for each entry
		int[] hashedArray=new int[numHashes];
		for(int i: hashedArray) {
			hashedArray[i]=f^randomHashes[i];
		}
		return hashedArray;
	}
	
	public static void main(String[] args) throws IOException {
    if(args.length==3) {
    	try {
    		//take command line arguments one by one.
    	int e=Integer.parseInt(args[0]);
    	int f=Integer.parseInt(args[1]);
    	int k=Integer.parseInt(args[2]);
    	
    	MultiHashingTable t=new MultiHashingTable(e,f,k);
    	
    	//Create a new file
    	File outputFile= new File("C:\\Users\\Nigama\\OneDrive\\Desktop\\IDS workspace\\IDSProject1\\OutputProg1.txt");
    	
    	//read into one file from another
    	FileOutputStream opt= new FileOutputStream(outputFile);

         BufferedWriter buff = new BufferedWriter(new OutputStreamWriter(opt));
         buff.write(Integer.toString(t.fillHash()));
         buff.newLine();
         
         for (int i = 0; i < t.tableVals.length; i++) {
             buff.write(Integer.toString(t.tableVals[i]));
             buff.newLine();
         }
    	
         buff.close();
    	
    }
    catch(Exception e) {
    	System.out.println("Invalid type");
    }
		
		
	}

}
}
