/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package testing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StaticVariable {

    public static int ruleAmod=0, ruleNsubj=0, ruleConjAndNoun=0, ruleConjAndAdj=0, ruleNo=0, rulePrepWith=0;
    public static Map<String, Double> lexicon = new HashMap<String, Double>();
    public static Map<SvSn, double[]> svsn = new HashMap<SvSn, double[]>();
    public static Map<String, Integer> jml_komen_fitur = new HashMap<String, Integer>();
    public static Map<String, Integer> DF_fitur = new HashMap<String, Integer>();
    public static List<String> noun = new ArrayList<String>();
    public static List<String> adj = new ArrayList<String>();
    public static List<KataNN> lnn = new ArrayList<KataNN>();
    public static List<ParsedModel> parsedFilter = new ArrayList<ParsedModel>();
    public static int jumlahKalmat = 0;
    public static int jumlahKalmatMengandungLexicon = 0;
    public static List<Kalimat> kalimatMengandungJJR = new ArrayList<Kalimat>();
    public static List<Kalimat> kalimatMengandungJJS = new ArrayList<Kalimat>();
    public static List<Tagged> tagkata = new ArrayList<Tagged>();
    public static List<String> stopword = new ArrayList<String>();
    public static List<String> kamus = new ArrayList<String>();
    public static Map<String, Double> bobotRelevansi = new HashMap<String, Double>();
    public static List<String> fiturNsubj = new ArrayList<String>();
    public static List<String> fiturAmod = new ArrayList<String>();
    public static List<String> fiturPrep = new ArrayList<String>();
    public static List<String> fiturDet = new ArrayList<String>();
    public static List<String> fiturConjAndNoun = new ArrayList<String>();
}

class Kalimat {

    String kalimatString;
    String dokumen;
    int kalimatke;

    public Kalimat(String kalimatString, String dokumen, int kalimatke) {
        this.kalimatString = kalimatString;
        this.dokumen = dokumen;
        this.kalimatke = kalimatke;
    }
    
    
}


class lexicon_kata_mengandung {

    String kata;
    String dokumen;
    int kalimatke;
    String KataProduk;

    public lexicon_kata_mengandung(String kata, String dokumen, int kalimatke,String KataProduk) {
        this.kata = kata;
        this.dokumen = dokumen;
        this.kalimatke = kalimatke;
        this.KataProduk=KataProduk;
    }
    
    public lexicon_kata_mengandung(String kata, String dokumen, int kalimatke) {
        this.kata = kata;
        this.dokumen = dokumen;
        this.kalimatke = kalimatke;
    }
    
    
}


class Tagged {

    String kata;
    String dokumen;
    int kalimatke;

    public Tagged(String kata, String dokumen, int kalimatke) {
        this.kata = kata;
        this.dokumen = dokumen;
        this.kalimatke = kalimatke;
    }
    
    
}


 class Pasangan_lexicon_fitur{
        String kalimat;
        String lexiconFitur;
        double scoreLexion;
        String lokasi;
        double csf;
        String fitur;

        public Pasangan_lexicon_fitur(String kalimat, String lexiconFitur, double scoreLexion, String lokasi, double csf) {
            this.kalimat = kalimat;
            this.lexiconFitur = lexiconFitur;
            this.scoreLexion = scoreLexion;
            this.lokasi = lokasi;
            this.csf = csf;
        }

        @Override
        public String toString() {
            return kalimat+" | "+lexiconFitur+" | "+scoreLexion+" | "+lokasi+" | "+csf; //To change body of generated methods, choose Tools | Templates.
        }

        public String getFitur(String domain) {
            String fitur = lexiconFitur.split("-")[1];
            return fitur.equalsIgnoreCase("it")?domain:fitur;
        }
        
    }