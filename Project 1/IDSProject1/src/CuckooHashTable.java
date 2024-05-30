import java.io.*;
import java.util.*;


public class CuckooHashTable {

	int numTableEntries;
	int numFlows;
	int numHashes;
	int cuckooSteps;
	int[] tableVals;
	int[] randomHashes;
	HashMap<Integer,int[]> cMap=new HashMap<Integer,int[]>();
	
	Random rand=new Random();
	
	CuckooHashTable(int n,int f,int k,int c){
		numTableEntries=n;
		numFlows=f;
		numHashes=k;
		cuckooSteps=c;
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
		for(int i=0;i<numFlows;i++) {
			int f=rand.nextInt(3000);
			int[] hashedArray=hashXOR(f);
			cMap.put(f, hashedArray);
			boolean check=false;
			for(int j=0;j<numHashes;j++) {
				index=hashedArray[j]%numTableEntries;
				if(tableVals[index]==0) {
					tableVals[index]=f;
					c++;
					check=true;
					break;
				}
			}
			if(!check && (recursiveSearch(f,hashedArray,cuckooSteps)!=0)) {
					c++;
				}
			
		}
		System.out.println(c);
		return c;
	}
	public int[] hashXOR(int f) {
		int[] hashedArray=new int[numHashes];
		for(int i: hashedArray) {
			hashedArray[i]=f^randomHashes[i];
		}
		return hashedArray;
	}
	
	
	public int recursiveSearch(int f,int[] hashedArray,int cuckooSteps) {
		//see if we have any steps specified 
		
		if(cuckooSteps>0) {
			//if yes then move on.
		for(int i=0;i<hashedArray.length;i++) {
			int f1=tableVals[hashedArray[i]%numTableEntries];
			//each occupant 
			int k=randomNumberGenerator();
			int[] occupent=cMap.get(f1);
			for(int j=0;j<occupent.length;j++) {
				int p=randomNumberGenerator();
				int index=occupent[j]%numTableEntries;
				if(tableVals[index]==0) {
					tableVals[index]=f1;
					tableVals[hashedArray[i]%numTableEntries]=f;
					return 1;
				}
			}
			//recusion
			 for(int j=0;j<occupent.length;j++) {
				  if(recursiveSearch(f1, occupent, cuckooSteps-1)==0) {
				  return 1;
				  }
				 }
				 
			
		}
	}
		return 0;
	}
	
	
	
	public static void main(String[] args) throws IOException {
    if(args.length==4) {
    	try {
    	int e=Integer.parseInt(args[0]);
    	int f=Integer.parseInt(args[1]);
    	int k=Integer.parseInt(args[2]);
    	int c=Integer.parseInt(args[3]);
    	CuckooHashTable t=new CuckooHashTable(e,f,k,c);
    	
    	File outputFile= new File("C:\\Users\\Nigama\\OneDrive\\Desktop\\IDS workspace\\IDSProject1\\OutputProg2.txt");
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
