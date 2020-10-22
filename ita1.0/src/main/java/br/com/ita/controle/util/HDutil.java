package br.com.ita.controle.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

public class HDutil {
	
	public static String getSerialNumber() throws IOException, InterruptedException {
	    ProcessBuilder pb = new ProcessBuilder("wmic", "diskdrive", 
	            "get", "serialnumber");
	    Process process = pb.start();
	    process.waitFor();
	    String serialNumber = "";
	    try (BufferedReader br = new BufferedReader(new InputStreamReader(
	            process.getInputStream()))) {
	        for (String line = br.readLine(); line != null; line = br.readLine()) {
	            if (line.length() < 1 || line.startsWith("SerialNumber")) {
	                continue;
	            }
	            serialNumber = line;
	            break;
	        }
	    }
	    return serialNumber;
	}
	
	public static String getSerialNumber2() {
        String result = "";
        try {
            File file = File.createTempFile("realhowto",".vbs");
            file.deleteOnExit();
            FileWriter fw = new java.io.FileWriter(file);

            String vbs = "Set objFSO = CreateObject(\"Scripting.FileSystemObject\")\n"
                        +"Set colDrives = objFSO.Drives\n"
                        +"Set objDrive = colDrives.item(\"" + "C" + "\")\n"
                        +"Wscript.Echo objDrive.SerialNumber";  // see note
            fw.write(vbs);
            fw.close();
            Process p = Runtime.getRuntime().exec("cscript //NoLogo " + file.getPath());
            BufferedReader input =
              new BufferedReader
                (new InputStreamReader(p.getInputStream()));
            String line;
            while ((line = input.readLine()) != null) {
               result += line;
            }
            input.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return result.trim();
    }
	
}
