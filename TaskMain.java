import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import org.json.simple.JSONObject;

/*
 * Used hash Map to get Fast Speed
 * Create a freshworks folder at local Disk-D for default location
 * D:\freshworks\dataStore.txt
 */

public  class TaskMain 
{
    public static void main(String[] args) throws IOException,InputMismatchException
    {
        //initialized new hashMap;
        HashMap<String,JSONObject> map = new HashMap<String,JSONObject>();
        Scanner sc = new Scanner(System.in);
      
        
        char ch;
        try
        {
            do {
                //clearing the terminal every time of new operation begins
                cls();
                System.out.println("Operations:");
                System.out.println("-----------");
                System.out.println("1.Create/Add");
                System.out.println("2.Read");
                System.out.println("3.Delete");
                System.out.println("4.Exit");
                System.out.println("Enter Option :");

                int op =sc.nextInt();
                String key="";
                String json_key="",json_value="";
                JSONObject obj = new JSONObject();
                int op1=4,tiktok;
                String filepath="D:\\freshworks\\dataStore.txt";
                //default filepath-D:\\freshworks\\dataStore.txt

                if(op!=4)
                {
                //getting location/filepath
                System.out.println("Location");
                System.out.println("---------");
                System.out.println("1.New  2.default");
                op1=sc.nextInt();

                //clear map for every new operation
                map.clear();
                System.out.println("Enter key:");
                key=sc.next().toUpperCase();
                }

                switch(op)
                {
                    case 1:
                        if(op1==1){
                            //optional file path
                            System.out.println("Enter filepath with filename(.txt):");
                            filepath=sc.next();
                            
                            try
                            {
                                File myObj = new File(filepath);

                                //Used to check if file already exists or not
                                if (myObj.createNewFile()){
                                    System.out.println("");
                                System.out.println("File created: " + myObj.getName());
                                } else {
                                    System.out.println("");
                                System.out.println("File is existing..");
                                }
                            } catch (IOException e) {
                                System.out.println("An error occurred-Please give correct location");
                                break;
                            }
                        }

                        final String fname = filepath;
                        final String keyy=key;
                        File file=new File(filepath);

                        //checking the file size (<1GB?)
                        if(file.exists())
                        {
                            double bytes=file.length();
                            //Converting bytes into KiloBytes
                            double kilobytes=bytes/1024;
                            //1GB = 1048576KB
                            if(kilobytes>=1048576)
                            {
                                System.out.println("File memory exceeds.."+kilobytes);
                                System.out.println("Create a new file/Delete the keys");
                                break;
                            }
                        }

                        //calling toread function to store all keys in HashMap
                        toread(map, filepath);

                        //Checking whether the key is less than 32 chars or not
                        if(key.length()>=32)
                        {
                            System.out.println("Key size exceeds maximum size..");
                            break;
                        }
                        //To check if the key already exists or not
                        if(map.containsKey(key)){
                            System.out.println("KEY already exists. Duplicate keys not allowed..");
                            break;
                        }            
                        sc.nextLine();
                        System.out.println("Enter the JSON key:");
                        json_key=sc.nextLine().toUpperCase();

                        System.out.println("Enter the JSON Value:");
                        json_value=sc.nextLine().toUpperCase();

                        System.out.println("Enter TimeToLive:");
                        tiktok = sc.nextInt();
                        sc.nextLine();

                        //setting the timer(TTL) for each key
                        if(tiktok!=0)
                        {
                            Timer timer = new Timer();
                            long timeout = 1000*tiktok;
                            timer.schedule(new TimerTask(){
                                public void run()
                                {
                                    doaction(map,keyy);
                                    towrite(map,fname);
                                }
                            }, timeout);
                        }
                        obj.put(json_key,json_value);

                        ByteArrayOutputStream ostream = new ByteArrayOutputStream ();
                        ObjectOutputStream obStream = new ObjectOutputStream(ostream);
                        obStream.writeObject(obj.toString());
                        byte[] rawObject = ostream.toByteArray();
                        ostream.close();
                        int size = rawObject.length;
                        
                        //Checking the size of object(<=16kb?)
                        if(size>16*1024)
                        {
                            System.out.println("Object Limit Exceeded.! Try again");
                            break;
                        }
                        map.put(key,obj);

                        //if all conditions are true then write all key & values to the dataStore
                        towrite(map,filepath);
                        break;

                    case 2:
                        if(op1==1){
                            System.out.println("Enter filepath with filename(.txt):");
                            filepath=sc.next();
                            try {
                                File myObj = new File(filepath);
                                if (myObj.createNewFile()) {
                                    System.out.println("");
                                System.out.println("File created: " + myObj.getName());
                                } else {
                                    System.out.println("");
                                System.out.println("File is existing..");
                                }
                            } catch (IOException e) {
                                System.out.println("An error occurred-Please give correct location");
                                break;
                            }
                        }
                        toread(map,filepath);

                        //Displaying the value from HaspMap to the given key by user 
                        if(map.containsKey(key))
                        {
                            System.out.println("");
                            System.out.println("The json object is "+map.get(key));
                        }
                        else{
                            System.out.println("");
                            System.out.println("Key not found!");
                        }
                        break;
                    
                    case 3:
                        if(op1==1){
                            System.out.println("Enter filepath with filename(.txt):");
                            filepath=sc.next();
                            try {
                                File myObj = new File(filepath);
                                if (myObj.createNewFile()) {
                                    System.out.println("");
                                System.out.println("File created: " + myObj.getName());
                                } else {
                                    System.out.println("");
                                System.out.println("File is existing..");
                                }
                            } catch (IOException e) {
                                System.out.println("An error occurred-Please give correct location");
                                break;
                            }
                        }
                        toread(map, filepath);
                        if(map.containsKey(key))
                        {
                            map.remove(key);
                            System.out.println("");
                            System.out.println("Successfully deleted..");
                        }
                        else
                        {
                            System.out.println("");
                            System.out.println("Key not found!");
                        }
                        towrite(map,filepath);
                        break;

                    case 4:
                        System.exit(0);
                    default:
                        System.out.println("Invalid option..");
                        break;
                }
            
                System.out.println("");
                System.out.println("To Continue:Press-y/n:");
                ch = sc.next().charAt(0);
            } while ((ch == 'y') ? true : false);
            cls();
        System.exit(0);
        }
        catch(Exception e)
        {
            System.out.println("Mismatch Found. Run again!");
            System.exit(0);
        }
    }

    //Used Synchronized for every Method Such that multiple threads
    //cls()-function is used to clear the terminal
    public synchronized static void cls()
    {
        try
        {	
            new ProcessBuilder("cmd","/c","cls").inheritIO().start().waitFor();
        }catch(Exception E)
            {
                System.out.println(E);
            }
    }

    public synchronized static void doaction(HashMap<String, JSONObject> map,String key) {
        map.remove(key);
    }

    //toread-function is used to write all keys and values from HaspMap to dataStore
    public synchronized static void towrite(HashMap<String, JSONObject> map, String filepath) {
        try {
            File fileTwo=new File(filepath);
            FileOutputStream fos=new FileOutputStream(fileTwo);
            PrintWriter pw=new PrintWriter(fos);
    
            for(Map.Entry<String,JSONObject> m :map.entrySet()){
                pw.println(m.getKey()+":"+m.getValue());
            }
            pw.flush();
            pw.close();
            fos.close();
        } catch(Exception e) {}

    }

    //toread-function is used to read all keys and values from dataStore and storing in map
    public synchronized static void toread(HashMap<String, JSONObject> map, String filepath) 
    {
        File toRead=new File(filepath);
        if(toRead.length()!=0)
        {
            try
            {
                FileInputStream fis=new FileInputStream(toRead);
                Scanner sc=new Scanner(fis);
                String currentLine;
                while(sc.hasNextLine()) {
                    currentLine=sc.nextLine();    
                    String[] arr = currentLine.split(":", 2); 
                    StringBuilder key = new StringBuilder("");      
                    JSONObject obj = new JSONObject();
                    String nkey=arr[0];
                    int i;
                    for (i = 0; (Character)arr[1].charAt(i)!=':'; i++) {
                        if(Character.isUpperCase(arr[1].charAt(i))|| arr[1].charAt(i)==' '){   
                            key.append(arr[1].charAt(i)); 
                        }
                    }
                    int j;
                    StringBuilder value = new StringBuilder(""); 
                    for (j=i; j < arr[1].length(); j++) {
            
                        if(Character.isUpperCase(arr[1].charAt(j))||arr[1].charAt(i)==' '){    
                            value.append(arr[1].charAt(j));
                        }
                    }
                    obj.put(key,value);
                    map.put(nkey,obj);
                }
                fis.close();

                //print All data in MAP
                //for(Map.Entry<String,JSONObject> m :map.entrySet()) {
                //   System.out.println(m.getKey()+":"+m.getValue());
                //}
            }catch(Exception e) {}
        }
    }
}
