/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package similarity;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author mr144 memetakan fitur yang sama kedalam array berdasarkan kamus kata sinonim
 */
public class SynonimFeatures {
    public static Map<String, String[]> getSynonim(String p) throws IOException{
        String path = "data/synonim-features-"+ p + ".txt";
        BufferedReader br = null;
        int jumlah = 0;
        String kalimat = "";
        Map<String, String[]> myFitur = new HashMap<String, String[]>();
        
        br = new BufferedReader(new FileReader(path));
        String line;
        while ((line = br.readLine()) != null) {
            String isi[] = line.split(",");
            myFitur.put(isi[0], isi);
        }
        br.close();
        
        return myFitur;
    }
}
