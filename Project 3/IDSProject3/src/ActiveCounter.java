import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.util.*;

public class ActiveCounter {

	//this is the max value for given size binary number
	  final int max = 65536;
	  int[] counter;
	  
	  //we get number part and exponent part size
	  int number;
	  int exponentPart;
	  
      int finVAl;
	  int currentVal;
	  int overflow;
	  int count;
  
      int exponent;	

  public ActiveCounter(int n, int e) {
	  //initializing an array
	    counter = new int[n + e];
	    number = n;
	    exponentPart = e;
  }
  
public int convert(String k) {
	int ans=Integer.parseInt(k, 2);
	return ans;
}
  public void add() {
	  //we need to increeese by 1 for 1000000 tims
	  
    for (int i = 1; i <= 1000000; i++) {
    	
 // create string to store exponential part of our array
    	
      String ce = "";
      
      for (int j = number; j < counter.length; j++) {
        ce += counter[j];
      }
      //convert it into a number the exponetial part 
      
      int nce = convert(ce);
      
      // this is the xponential part value 
      nce = (int) Math.pow(2, nce);
      
      //this is to determine how many times we increment by 1
      for (int j = 0; j < nce - 1; j++) {
        i++;
      }
      
      
      incrementOne(i);
    }
    
    String exp = "";
    
    for (int i = number; i < counter.length; i++) {
      exp += counter[i];
    }
    exponent = convert(exp);
  }

  public void incrementOne(int temp) {

    StringBuilder sb = new StringBuilder();


    if (Integer.toBinaryString(currentVal + 1).length() > number) {
    	
    	overflow++;
        currentVal = (currentVal + 1) / 2;
        String binary = Integer.toBinaryString(currentVal);
      randomNumberGenerator();
        int k = binary.length() - 1;
        for (int i = number - 1; i >= 0; i--) {
          if (k >= 0)
            counter[i] = binary.charAt(k--) - '0';
          sb.append(counter[i]);
        }
     
        int moveOver = 0, x = 0;
         randomNumberGenerator();
        for (int i = number + exponentPart - 1; i >= number; i--) {
          x = counter[i];
          if (x == 1) {
            counter[i] = 0;
            moveOver = 1;
          } else if (x == 0) {
            counter[i] = 1;
            break;
          }
        }

      
    } 
    else {
    
    	if (overflow > 0) {
            overflow++;
          }
          // calculate the numeric parts 
          String num = "";
          for (int i = 0; i < number; i++)
            num += counter[i];

          int sum = convert(num)+ 1;
          //cahneg this part 
          String sumStr = Integer.toBinaryString(sum);
          currentVal = sum;
          int k = sumStr.length() - 1;
          for (int j = number - 1; j >= 0; j--)
            if (k >= 0)
              counter[j] = Character.getNumericValue(sumStr.charAt(k--));

      
    }

  }
  
	public int randomNumberGenerator(){
        Random random = new Random();
        int x=random.nextInt(Integer.MAX_VALUE - 1);
        return x;
    }
	
	public static void main(String[] args) {
		
      try  {
    
    	  ActiveCounter ac = new ActiveCounter(16, 16);
    	  ac.add();
    	    
	    	//Create a new file
	    	File outputFile= new File("C:\\Users\\Nigama\\OneDrive\\Desktop\\IDS workspace\\IDSProject3\\OutputProg3.txt");
	    	
	    	FileOutputStream opt= new FileOutputStream(outputFile);
	    	
	    	BufferedWriter buff = new BufferedWriter(new OutputStreamWriter(opt));
	        
//	         int s=ac.counter.length;
//	         for (int i = 0; i <s; i++) {
//	        	 buff.write(ac.counter[i]);
//	        	 buff.newLine();
//	         }
	         buff.write("Active Counter value is :  "+(int)(ac.currentVal * Math.pow(2, ac.exponent)));
             buff.newLine();
	         
	         buff.close();
	    	
          
          
      } catch (Exception e) {
          e.printStackTrace();
        }
	}
  
}

