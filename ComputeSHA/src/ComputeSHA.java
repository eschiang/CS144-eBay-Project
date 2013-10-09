 import java.awt.Rectangle;
import java.io.*;
import java.security.MessageDigest;

public class ComputeSHA {

   public static byte[] createChecksum(String filename) throws Exception {
       InputStream fis =  new FileInputStream(filename);

       byte[] buffer = new byte[1024];
       MessageDigest complete = MessageDigest.getInstance("SHA-1");
       int numRead;

       do {
           numRead = fis.read(buffer);
           if (numRead > 0) {
               complete.update(buffer, 0, numRead);
           }
       } while (numRead != -1);

       fis.close();
       return complete.digest();
   }

   // see this How-to for a faster way to convert
   // a byte array to a HEX string
   public static String getMD5Checksum(String filename) throws Exception {
       byte[] b = createChecksum(filename);
       String result = "";

       for (int i=0; i < b.length; i++) {
           result += Integer.toString( ( b[i] & 0xff ) + 0x100, 16).substring( 1 );
       }
       return result;
   }

   public static void main(String args[]) {
	  
	  
	   
	   try {
		  ///////////////////Probably don't need it. But just in case if we do the following code reads the stuff
		  ////////////////// from a text file and stores it into a string
		   BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\Meet\\Desktop\\test.txt"));
		   String s="";int ctr=0;String final1="";
		   while((s=br.readLine())!=null)
		   {
			   System.out.print(ctr);
			   System.out.println(s);
			   if(s!=null)
			   final1+=s;
			   
		   }
           System.out.println(final1);
           //////////////////////////////////////////////////////////////////////////////
           //////////////////////////////////////////////////////////////////////////////
           
           /////////////////Following is a pre defined function that calculates the md5 of a file/files content(not sure)
           System.out.println(getMD5Checksum("sample.txt"));
           ////////////////////////And we are done
          
       }
       catch (Exception e) {
           e.printStackTrace();
       }
   }
}