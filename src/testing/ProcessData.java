    /*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package testing;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import testing.Utils.TypeKalimat;

public class ProcessData {

    Rule rule = new Rule();
    Utils utils = new Utils();
    Map<String, String[]> kategori = new HashMap<String, String[]>();
    public Double alpha = 0.0;
    Double maxCSF = 0.0, minCSF = 0.0;
    Double maxGSF = 0.0, minGSF = 0.0, normGSF = 0.0;
    Double maxLCSF = 0.0, minLCSF = 0.0, normCSF = 0.0;
    String domain = "";

    public ProcessData() {
    }

    public void inisialisasiDomain(String domain){
        this.domain = domain;
        String p = "data/category-" + domain + ".txt";
        utils.readStopword();
        if (domain.equalsIgnoreCase("phone")) {
            kategori = utils.readKategory(p, "apple", "iphone", "3gs", "blackberry", "bb", "curve", "torch", "htc", "inspire", "lg", "optimus", "l7", "motorola", "atrix", "nokia", "lumia", "710", "samsung", "galaxy", "ace", "plus", "sony", "xperia", "u");
        } else if (domain.equalsIgnoreCase("tv")) {
            kategori = utils.readKategory(p, "LG", "42LK450", "Panasonic", "VIERA", "TC-P55VT50", "Samsung", "UN40ES6100", "UN46EH5300", "Sharp", "LC-60LE633U", "Sony", "BRAVIA", "KDL40EX640", "KDL46BX450", "TCL", "L40FHDP60", "Toshiba", "19SLV411U", "Vizio", "E3D420VX");
        } else if (domain.equalsIgnoreCase("camera")) {
            kategori = utils.readKategory(p, "Canon", "EOS", "Rebel", "T3i", "Fujifilm", "FinePix", "S2950", "GE", "Power", "Pro", "X500-WH", "Kodak", "EasyShare", "Z990", "Nikon", "Coolpix", "P90", "D7100", "Olympus", "E-PL2", "Panasonic", "Lumix", "DMC-ZS8", "Pentax", "K20D", "Sony", "Cyber-shot", "DSC-HX200V");
        }
        utils.readKamus(domain);
    }
    
    public void setDefaultLexicon() {
        //Ngisi lexicon dengan kata opini positif
        BufferedReader br = null;
        try {
            //Mendifinisikan kamus kata opini positif
            br = new BufferedReader(new FileReader("data/positive-words.txt"));
            String line;
            while ((line = br.readLine()) != null) {
                StaticVariable.lexicon.put(line, 1.0);
            }
            br.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                br.close();
            } catch (IOException ex) {
                Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        //Ngisi lexicon dengan kata opini negatif
        BufferedReader bl = null;
        try {
            //Mendifinisikan kamus kata opini negatif
            bl = new BufferedReader(new FileReader("data/negative-words.txt"));
            String line;
            while ((line = bl.readLine()) != null) {
                StaticVariable.lexicon.put(line, -1.0);
            }
            bl.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                bl.close();
            } catch (IOException ex) {
                Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public Map<FiturProduk, Double> getFiturProduk(String path) {
        Map<FiturProduk, Double> fitur_produk = new HashMap<FiturProduk, Double>();
        int n = (new File(path).listFiles().length) / 3;

        List<String> data = new ArrayList<String>();
        for (int x = 1; x <= n; x++) {
            data.add(x + "");
        }

        List<ParsedModel> ls = new ArrayList<ParsedModel>();
        for (String string : data) {
            utils.readTagged(path, string);
            ls.addAll(utils.readParsed(path, string));
        }

        // chek semua kata yang menagndung sv dan sn
        for (ParsedModel model : ls) {
            rule.chekSnSv(model);
        }



        int i = 0;
        boolean chek = true;
        while (chek) {
            chek = false;
            for (ParsedModel model : ls) {
                rule.chek(model, fitur_produk);
                if (rule.AdaProsess) {
                    chek = true;
                }
            }

            i++;
        }

        // tampilkan kata lexicon dan bobot
        Iterator it = StaticVariable.lexicon.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pairs = (Map.Entry) it.next();
        }

        return fitur_produk;
    }

    public boolean checkStopword(String kata) {
        boolean ada = false;
        for (int i = 0; i < StaticVariable.stopword.size(); i++) {
            if (kata.equalsIgnoreCase(StaticVariable.stopword.get(i))) {
                ada = true;
                break;
            }
        }
        return ada;
    }

    public HashMap<String, Double> hitungGSF(String path, Map<FiturProduk, Double> fitur_produk, int MinimalKomen) {

        int n = (new File(path).listFiles().length) / 3;

        List<String> data = new ArrayList<String>();
        for (int x = 1; x <= n; x++) {
            data.add(x + "");
        }


        // tampilkan kata FiturProduk dan bobot
        HashMap<String, Double> gsf = new HashMap<String, Double>();
        HashMap<String, Double> gsfSemua = new HashMap<String, Double>();

        Map<FiturProduk, Double> tempAmbil = new HashMap<FiturProduk, Double>();
        Map<FiturProduk, Double> tempBuang = new HashMap<FiturProduk, Double>();
        for (Map.Entry<FiturProduk, Double> entry : fitur_produk.entrySet()) {
            FiturProduk key = entry.getKey();
            Double value = entry.getValue();



            if (StaticVariable.jml_komen_fitur.get(key.nama) >= MinimalKomen) {
                tempAmbil.put(key, value);
            } else {
                tempBuang.put(key, value);
            }
            if (gsfSemua.containsKey(key.nama)) {
                double nilaisebelumnya = gsfSemua.get(key.nama);
                gsfSemua.put(key.nama, nilaisebelumnya + value);
            } else {
                gsfSemua.put(key.nama, value);
            }
        }
        fitur_produk.clear();


        fitur_produk.putAll(tempAmbil);
        for (Map.Entry<FiturProduk, Double> entry : fitur_produk.entrySet()) {
            FiturProduk key = entry.getKey();
            Double value = entry.getValue();
            if (gsf.containsKey(key.nama)) {
                double nilaisebelumnya = gsf.get(key.nama);
                gsf.put(key.nama, nilaisebelumnya + value);
            } else {
                gsf.put(key.nama, value);
            }

        }


        for (String string : data) {
            utils.readJUmlahKalimat(path, string);
        }


        int jumJJSMOST = 0;
        for (int x = 1; x <= n; x++) {
            jumJJSMOST += utils.ChekKalimatConstainOr(path, x + "", "jjs", "most");
        }

        int jumJJRMORE = 0;
        for (int x = 1; x <= n; x++) {
            jumJJRMORE += utils.ChekKalimatConstainOr(path, x + "", "jjr", "more");
        }

        return gsf;
    }

    public Map<String, nilai_gsf_csf> Bandingkan(HashMap<String, Double> gsf, String kata) {

        String katapecah[] = kata.split(" ");
        String kalimatsudah = "";

        //comparative
        List<lexicon_kata_mengandung> lkmCom = new ArrayList<lexicon_kata_mengandung>();
        for (Kalimat kalimat : StaticVariable.kalimatMengandungJJR) {
            String string = kalimat.kalimatString;
            for (String string1 : katapecah) {
                if (string.toLowerCase().contains(string1.toLowerCase())
                        && string != kalimatsudah) {
                    kalimatsudah = string;
                    String perkata[] = kalimatsudah.split(" ");
                    for (int x = 0; x < perkata.length; x++) {
                        String katachek = chekKalimatType(perkata[x], "jjr", false);
                        if (perkata[x].toLowerCase().startsWith("more")) {
                            lkmCom.add(new lexicon_kata_mengandung(chekKalimatType(perkata[x + 1],
                                    null, true), kalimat.dokumen, kalimat.kalimatke, string1));
                        }
                        if (!katachek.equalsIgnoreCase(perkata[x])) {
                            lkmCom.add(new lexicon_kata_mengandung(katachek, kalimat.dokumen,
                                    kalimat.kalimatke, string1));
                        }
                    }
                }
            }
        }
        List<Pasangan_lexicon_fitur> listPenampung = new ArrayList<Pasangan_lexicon_fitur>();
        for (lexicon_kata_mengandung object : lkmCom) {
            int indexProduk = ChekIndexKata(object.dokumen, object.kalimatke, object.KataProduk);
            cariFitur(listPenampung, object.dokumen, object.kalimatke, object.kata, indexProduk,
                    TypeKalimat.Comparative, true);
        }
        Map<String, Double> mapCSf = new HashMap<String, Double>();
        for (Pasangan_lexicon_fitur pasangan_lexicon_fitur : listPenampung) {
            if (mapCSf.containsKey(pasangan_lexicon_fitur.getFitur(domain))) {
                double nilaiSebelumnya = mapCSf.get(pasangan_lexicon_fitur.getFitur(domain));
                mapCSf.put(pasangan_lexicon_fitur.getFitur(domain), nilaiSebelumnya +
                        pasangan_lexicon_fitur.csf);
            } else {
                mapCSf.put(pasangan_lexicon_fitur.getFitur(domain), pasangan_lexicon_fitur.csf);
            }
        }



        //superlative
        List<lexicon_kata_mengandung> lkmSup = new ArrayList<lexicon_kata_mengandung>();
        int indexProdukSup = 0;
        for (Kalimat kalimat : StaticVariable.kalimatMengandungJJS) {
            indexProdukSup = -1;
            String string = kalimat.kalimatString;
            //System.out.println(string);
            for (String string1 : katapecah) {
                if (string.toLowerCase().contains(string1.toLowerCase())) {
                    indexProdukSup = 1;
                }
            }
            kalimatsudah = string;
            String perkata[] = kalimatsudah.split(" ");
            for (int x = 0; x < perkata.length; x++) {
                String katachek = chekKalimatType(perkata[x], "jjs", false);
                if (perkata[x].toLowerCase().startsWith("most")) {
                    lkmSup.add(new lexicon_kata_mengandung(chekKalimatType(perkata[x + 1],
                            null, true), kalimat.dokumen, kalimat.kalimatke));
                }
                if (!katachek.equalsIgnoreCase(perkata[x])) {
                    lkmSup.add(new lexicon_kata_mengandung(katachek, kalimat.dokumen,
                            kalimat.kalimatke));
                }
            }
        }
        List<Pasangan_lexicon_fitur> listSuperlative = new ArrayList<Pasangan_lexicon_fitur>();
        for (lexicon_kata_mengandung object : lkmSup) {
            cariFitur(listSuperlative, object.dokumen, object.kalimatke, object.kata,
                    indexProdukSup, TypeKalimat.Superlative, true);
        }
        for (Pasangan_lexicon_fitur pasangan_lexicon_fitur : listSuperlative) {
            if (mapCSf.containsKey(pasangan_lexicon_fitur.getFitur(domain))) {
                double nilaiSebelumnya = mapCSf.get(pasangan_lexicon_fitur.getFitur(domain));
                mapCSf.put(pasangan_lexicon_fitur.getFitur(domain), nilaiSebelumnya + pasangan_lexicon_fitur.csf);
            } else {
                mapCSf.put(pasangan_lexicon_fitur.getFitur(domain), pasangan_lexicon_fitur.csf);
            }
        }
        Map<String, nilai_gsf_csf> lngc = new HashMap<String, nilai_gsf_csf>();
        for (Map.Entry<String, Double> entry : gsf.entrySet()) {
            String key = entry.getKey();
            Double value = entry.getValue();
            double csf = (mapCSf.get(key.toString()) == null ? 0 : mapCSf.get(key.toString()));
            lngc.put(key.toString(), new nilai_gsf_csf(value, csf));
        }
        return lngc;
    }

    public void hitungMaxMin(Map<String, nilai_gsf_csf> gsfcsf) {
        maxGSF = 0.0;
        minGSF = 0.0;
        for (Map.Entry<String, nilai_gsf_csf> entry : gsfcsf.entrySet()) {
            nilai_gsf_csf value = entry.getValue();
            maxGSF = Math.max(maxGSF, value.gsf);
            minGSF = Math.min(minGSF, value.gsf);
            Double valueCSF = value.csf;
            maxLCSF = Math.max(maxLCSF, valueCSF);
            minLCSF = Math.min(minLCSF, valueCSF);
        }
        maxCSF = Math.max(maxCSF, maxLCSF);
        minCSF = Math.min(minCSF, minLCSF);
    }

    public Map<String, nilai_gsf_csf> normalisasiGSF(Map<String, nilai_gsf_csf> gsfcsf) {
        Map<String, nilai_gsf_csf> hasilNormGSF = new HashMap<String, nilai_gsf_csf>();

        //Normalisasi GSF
        for (Map.Entry<String, nilai_gsf_csf> entry : gsfcsf.entrySet()) {
            String key = entry.getKey();
            nilai_gsf_csf value = entry.getValue();
            Double valueGSF = value.gsf;
            normGSF = (valueGSF - minGSF) / (maxGSF - minGSF);
            value.gsf = normGSF;
            hasilNormGSF.put(key, value);
        }
        return hasilNormGSF;
    }

    public Map<String, Double> normalisasi(Map<String, nilai_gsf_csf> gsfcsf) {
        Map<String, Double> hasilNorm = new HashMap<String, Double>();
        //Normalisasi CSF dan penggabungan nilai
        for (Map.Entry<String, nilai_gsf_csf> entry : gsfcsf.entrySet()) {
            String key = entry.getKey();
            nilai_gsf_csf value = entry.getValue();
            Double valueCSF = value.csf;
            //Normalisasi CSF
            normCSF = (valueCSF - minCSF) / (maxCSF - minCSF);
            //Penggabungan skor GSF dan CSF
            Double skorRek = (alpha * value.gsf) + ((1 - alpha) * normCSF);
            hasilNorm.put(key, skorRek);
        }
        return hasilNorm;
    }

    public List<hasil_compare> BandingkanAkhir(Map<String, Double> prod1, Map<String, Double> prod2) {
        List<hasil_compare> lh = new ArrayList<hasil_compare>();
        for (Map.Entry<String, Double> entry : prod1.entrySet()) {
            String key = entry.getKey();
            Double value1 = entry.getValue();
            if (prod2.containsKey(key)) {
                Double value2 = prod2.get(key);
                lh.add(new hasil_compare(key, value1, value2));
            }
        }
        return lh;
    }

    public static String chekKalimatType(String kata, String type, boolean mostMore) {
        String katapecah[] = kata.split("_");
        if (mostMore) {
            return katapecah[0];
        }
        if (katapecah.length == 0) {
            return kata;
        }
        if (katapecah[1].equalsIgnoreCase(type)) {
            return katapecah[0];
        }
        return kata;
    }

    public static void cariFitur(List<Pasangan_lexicon_fitur> listPenampung, String dokumen, int kalimat, String kata, int indexProduk, TypeKalimat typeKalimat, boolean enenmy) {
        for (Tagged tagged : StaticVariable.tagkata) {
            if (tagged.dokumen.equalsIgnoreCase(dokumen) && tagged.kalimatke == kalimat && tagged.kata.toLowerCase().contains(kata)) {
                String data = tagged.kata;
                data = data.replace("(", ", ");
                String pattern[] = data.trim().split(",");
                if (pattern[0].equalsIgnoreCase("amod") || pattern[0].equalsIgnoreCase("nsubj")) {
                    int s1 = pattern[1].length();
                    int p1 = 0;
                    for (int i = s1 - 1; i >= 0; i--) {
                        if (pattern[1].charAt(i) == '-') {
                            p1 = i;
                            break;
                        }
                    }
                    String kt1 = pattern[1].trim().substring(0, p1 - 1);
                    int ind1 = Integer.parseInt(pattern[1].trim().substring(p1, s1 - 1));
                    String kata1 = kt1;
                    int indexKata1 = ind1;
                    int s = pattern[2].indexOf(")");
                    int p = 0;
                    for (int i = s - 1; i >= 0; i--) {
                        if (pattern[2].charAt(i) == '-') {
                            p = i;
                            break;
                        }
                    }
                    String kt2 = pattern[2].trim().substring(0, p - 1);
                    int ind2 = Integer.parseInt(pattern[2].trim().substring(p, s - 1));
                    String kata2 = kt2;
                    int indexKata2 = ind2;
                    int postiveNegative = 0;
                    String kirikanan = "netral";
                    if (StaticVariable.lexicon.get(kata) != null) {
                        double scoreLexicon = StaticVariable.lexicon.get(kata);
                        if (kata1.equalsIgnoreCase(kata)) {
                            if (typeKalimat == TypeKalimat.Comparative) {
                                if (indexProduk < indexKata1) {
                                    kirikanan = "kiri";
                                    //seblah kiri
                                    if (enenmy == true) {
                                        postiveNegative = -2;
                                    } else {
                                        postiveNegative = 2;
                                    }
                                } else {
                                    kirikanan = "kanan";
                                    //sebalah kanan
                                    if (enenmy == true) {
                                        postiveNegative = 2;
                                    } else {
                                        postiveNegative = -2;
                                    }
                                }
                            }
                            else {
                                if (indexProduk > 0) {
                                    kirikanan = "ada";
                                    postiveNegative = -1;
                                } else if (indexProduk < 0) {
                                    kirikanan = "tidak ada";
                                    postiveNegative = 1;
                                }
                            }
                            listPenampung.add(new Pasangan_lexicon_fitur(dokumen + "-" + kalimat, kata1 + "-" + kata2, scoreLexicon, kirikanan, scoreLexicon * postiveNegative));
                        } else if(kata2.equalsIgnoreCase(kata)) {
                            if (typeKalimat == TypeKalimat.Comparative) {
                                if (indexProduk < indexKata2) {
                                    //seblah kiri
                                    kirikanan = "kiri";
                                    if (enenmy == true) {
                                        postiveNegative = -2;
                                    } else {
                                        postiveNegative = 2;
                                    }
                                } else {
                                    //sebalah kanan
                                    kirikanan = "kanan";
                                    if (enenmy == true) {
                                        postiveNegative = 2;
                                    } else {
                                        postiveNegative = -2;
                                    }
                                }
                            } else {
                                if (indexProduk > 0) {
                                    kirikanan = "ada";
                                    postiveNegative = -1;
                                } else if (indexProduk < 0) {
                                    kirikanan = "tidak ada";
                                    postiveNegative = 1;
                                }
                            }
                            listPenampung.add(new Pasangan_lexicon_fitur(dokumen + "-" + kalimat, kata2 + "-" + kata1, scoreLexicon, kirikanan, (scoreLexicon * postiveNegative)));
                        }
                    }
                }
            }
        }
    }

    public static int ChekIndexKata(String dokumen, int kalimat, String kata) {
        int index = -1;
        kata = kata.toLowerCase();
        for (Tagged tagged : StaticVariable.tagkata) {
            if (tagged.dokumen.equalsIgnoreCase(dokumen) && tagged.kalimatke == kalimat && tagged.kata.toLowerCase().contains(kata)) {
                String data = tagged.kata;
                data = data.replace("(", ", ");
                String pattern[] = data.trim().split(",");
                int s1 = pattern[1].length();
                int p1 = 0;
                for (int i = s1 - 1; i >= 0; i--) {
                    if (pattern[1].charAt(i) == '-') {
                        p1 = i;
                        break;
                    }
                }
                String kt1 = pattern[1].trim().substring(0, p1 - 1);
                int ind1 = Integer.parseInt(pattern[1].trim().substring(p1, s1 - 1));
                String kata1 = kt1;
                int indexKata1 = ind1;
                int s = pattern[2].indexOf(")");
                int p = 0;
                for (int i = s - 1; i >= 0; i--) {
                    if (pattern[2].charAt(i) == '-') {
                        p = i;
                        break;
                    }
                }
                String kt2 = pattern[2].trim().substring(0, p - 1);
                int ind2 = Integer.parseInt(pattern[2].trim().substring(p, s - 1));
                String kata2 = kt2;
                int indexKata2 = ind2;
                if (kata1.equalsIgnoreCase(kata)) {
                    index = indexKata1;
                    break;
                } else {
                    index = indexKata2;
                    break;
                }
            }
        }
        return index;
    }

    public Map<String, nilai_gsf_csf> GabungKategori(Map<String, nilai_gsf_csf> prod1) {
        Map<String, nilai_gsf_csf> hasil = new HashMap<String, nilai_gsf_csf>();
        for (Map.Entry<String, nilai_gsf_csf> entry : prod1.entrySet()) {
            String key = entry.getKey();


            nilai_gsf_csf value = entry.getValue();
            boolean ada = false;
            for (Map.Entry<String, String[]> entrykat : kategori.entrySet()) {
                String keykat = entrykat.getKey();

                String[] valuekat = entrykat.getValue();
                if (Arrays.asList(valuekat).indexOf(key) > -1) {
                    ada = true;

                    if (hasil.containsKey(keykat)) {
                        nilai_gsf_csf valueasli = hasil.get(keykat);
                        hasil.put(keykat, new nilai_gsf_csf(value.gsf + valueasli.gsf, value.csf + valueasli.csf));
                    } else {
                        hasil.put(keykat, value);
                    }
                    break;

                } else {
                    ada = false;
                }
            }
            if (ada == false) {
                hasil.put(key, value);
            }
        }
        return hasil;
    }

    public void crossCheck(Map<String, nilai_gsf_csf> prod1, Map<String, nilai_gsf_csf> prod2) {
        Map<String, nilai_gsf_csf> tempProd1 = new HashMap<String, nilai_gsf_csf>();
        Map<String, nilai_gsf_csf> tempProd2 = new HashMap<String, nilai_gsf_csf>();
        tempProd1.putAll(prod1);
        tempProd2.putAll(prod2);

        for (Map.Entry<String, nilai_gsf_csf> entry : tempProd1.entrySet()) {
            String key = entry.getKey();
            nilai_gsf_csf value = entry.getValue();
            if (prod2.containsKey(key)) {
                nilai_gsf_csf valueAsli = prod2.get(key);
                prod2.put(key, new nilai_gsf_csf(valueAsli.gsf, 
                        valueAsli.csf - value.csf));
            }
        }

        for (Map.Entry<String, nilai_gsf_csf> entry : tempProd2.entrySet()) {
            String key = entry.getKey();
            nilai_gsf_csf value = entry.getValue();
            if (prod1.containsKey(key)) {
                nilai_gsf_csf valueAsli = prod1.get(key);
                prod1.put(key, new nilai_gsf_csf(valueAsli.gsf, valueAsli.csf - value.csf));
            } else {
            }
        }
    }

    public void HitungDF(Map<FiturProduk, Double> fitur) {
        String sPath = "";
        for (Map.Entry<FiturProduk, Double> entry : fitur.entrySet()) {
            FiturProduk key = entry.getKey();
            if (StaticVariable.DF_fitur.get(key.nama) == null) {
                StaticVariable.DF_fitur.put(key.nama, 1);
            } else {
                int nilaiLama = StaticVariable.DF_fitur.get(key.nama);
                if (!sPath.equalsIgnoreCase(key.path)) {
                    StaticVariable.DF_fitur.put(key.nama, nilaiLama + 1);
                }
            }
            sPath = key.path;
        }
    }
}
