/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * MainProgram.java
 *
 * Created on 28 Mei 13, 19:02:25
 */
package GUI;

import java.awt.Component;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.TableView.TableCell;
import testing.FiturProduk;
import testing.ProcessData;
import testing.StaticVariable;
import testing.hasil_compare;
import testing.nilai_gsf_csf;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import jxl.*;
import jxl.write.*;
import similarity.Jaccard;
import similarity.SynonimFeatures;

public class MainProgram extends javax.swing.JFrame {

    /** Creates new form MainProgram */
    String domain = "";
    int N = 0;
    ProcessData proc = new ProcessData();
    HashMap<String, Double> gsf1, gsf2, gsf3, gsf4, gsf5, gsf6, gsf7, gsf8, gsf9, gsf10;
    StaticVariable sv = new StaticVariable();
    List<String> relevan;
    Map<String, Integer[]> frek = new HashMap<String, Integer[]>();
    int b = 0;
    Map<String, Integer[]> dokFrek = new HashMap<String, Integer[]>();
    int c = 0;
    Map<String, Double> logFF = new HashMap<String, Double>();
    Map<String, Double> IDF = new HashMap<String, Double>();
    Map<String, Double> IDF_prob = new HashMap<String, Double>();
    Map<String, String[]> sinonim_features = new HashMap<String, String[]>();

    public MainProgram() {

        initComponents();
    }

    public void matriksFF() {
        for (Map.Entry<String, Integer> entry : sv.jml_komen_fitur.entrySet()) {
            Integer arr[] = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
            String key = entry.getKey();
            Integer value = entry.getValue();
            if (frek.containsKey(key)) {
                Integer temp[] = frek.get(key);
                temp[b] = value;
                frek.put(key, temp);
            } else {
                arr[b] = value;
                frek.put(key, arr);
            }
            sv.jml_komen_fitur.put(key, 0);
        }
        b++;
    }

    public void matriksDF() {
        for (Map.Entry<String, Integer> entry : sv.DF_fitur.entrySet()) {
            Integer arr[] = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
            String key = entry.getKey();
            Integer value = entry.getValue();
            if (dokFrek.containsKey(key)) {
                Integer temp[] = dokFrek.get(key);
                temp[c] = value;
                dokFrek.put(key, temp);
            } else {
                arr[c] = value;
                dokFrek.put(key, arr);
            }
            sv.DF_fitur.put(key, 0);
        }
        c++;
    }

    public void RunData(Double alpha, int betha) throws IOException, WriteException {
        proc.alpha = alpha;
        int MinimalKomen = betha;
        String path1 = "", path2 = "", path3 = "", path4 = "", path5 = "", path6 = "", path7 = "", path8 = "", path9 = "", path10 = "";
        relevan = sv.kamus;
        if (domain.equalsIgnoreCase("phone")) {
            N = 1610;
            path1 = "data/Data Asli/Phone/LG Optimus L7 P705";
            path2 = "data/Data Asli/Phone/Apple Iphone 3GS";
            path3 = "data/Data Asli/Phone/Sony Xperia U";
            path4 = "data/Data Asli/Phone/Blackberry Curve 9360";
            path5 = "data/Data Asli/Phone/Blackberry Torch 9800";
            path6 = "data/Data Asli/Phone/HTC Inspire";
            path7 = "data/Data Asli/Phone/Motorola Atrix MB860";
            path8 = "data/Data Asli/Phone/Nokia Lumia 710";
            path9 = "data/Data Asli/Phone/Samsung Galaxy Ace Plus S7500";
            path10 = "data/Data Asli/Phone/Google Nexus One";
        } else if (domain.equalsIgnoreCase("tv")) {
            String listKata[] = {"tv", "picture", "sound", "quality", "price", "color", "screen", "size", "speaker"};
            N = 1568;
            path1 = "data/Data Asli/TV/LG 42LK450";
            path2 = "data/Data Asli/TV/Panasonic VIERA TC-P55VT50";
            path3 = "data/Data Asli/TV/Samsung UN40ES6100";
            path4 = "data/Data Asli/TV/Samsung UN46EH5300";
            path5 = "data/Data Asli/TV/Sharp LC-60LE633U";
            path6 = "data/Data Asli/TV/Sony BRAVIA KDL40EX640";
            path7 = "data/Data Asli/TV/Sony BRAVIA KDL46BX450";
            path8 = "data/Data Asli/TV/TCL L40FHDP60";
            path9 = "data/Data Asli/TV/Toshiba 19SLV411U";
            path10 = "data/Data Asli/TV/Vizio E3D420VX";
        } else if (domain.equalsIgnoreCase("camera")) {
            String listKata[] = {"camera", "picture", "price", "battery", "screen", "size", "memory", "performance", "zoom", "photo", "shot", "light", "image", "lense", "resolution", "quality"};
            N = 1670;
            path1 = "data/Data Asli/Camera/Canon EOS Rebel T3i";
            path2 = "data/Data Asli/Camera/Fujifilm FinePix S2950";
            path3 = "data/Data Asli/Camera/GE Power Pro X500-WH";
            path4 = "data/Data Asli/Camera/Kodak EasyShare Max Z990";
            path5 = "data/Data Asli/Camera/Nikon Coolpix P90";
            path6 = "data/Data Asli/Camera/Nikon D7100";
            path7 = "data/Data Asli/Camera/Olympus EPL2";
            path8 = "data/Data Asli/Camera/Panasonic Lumix DMC-ZS8";
            path9 = "data/Data Asli/Camera/Pentax K20D";
            path10 = "data/Data Asli/Camera/Sony Cyber-shot DSC-HX200V";
        }

        proc.setDefaultLexicon();


        Map<FiturProduk, Double> fitur1 = proc.getFiturProduk(path1);
        matriksFF();
        Map<FiturProduk, Double> fitur2 = proc.getFiturProduk(path2);
        matriksFF();
        Map<FiturProduk, Double> fitur3 = proc.getFiturProduk(path3);
        matriksFF();
        Map<FiturProduk, Double> fitur4 = proc.getFiturProduk(path4);
        matriksFF();
        Map<FiturProduk, Double> fitur5 = proc.getFiturProduk(path5);
        matriksFF();
        Map<FiturProduk, Double> fitur6 = proc.getFiturProduk(path6);
        matriksFF();
        Map<FiturProduk, Double> fitur7 = proc.getFiturProduk(path7);
        matriksFF();
        Map<FiturProduk, Double> fitur8 = proc.getFiturProduk(path8);
        matriksFF();
        Map<FiturProduk, Double> fitur9 = proc.getFiturProduk(path9);
        matriksFF();
        Map<FiturProduk, Double> fitur10 = proc.getFiturProduk(path10);
        matriksFF();
        
        

        proc.HitungDF(fitur1);
        matriksDF();
        proc.HitungDF(fitur2);
        matriksDF();
        proc.HitungDF(fitur3);
        matriksDF();
        proc.HitungDF(fitur4);
        matriksDF();
        proc.HitungDF(fitur5);
        matriksDF();
        proc.HitungDF(fitur6);
        matriksDF();
        proc.HitungDF(fitur7);
        matriksDF();
        proc.HitungDF(fitur8);
        matriksDF();
        proc.HitungDF(fitur9);
        matriksDF();
        proc.HitungDF(fitur10);
        matriksDF();
        
        

//============== Tampilkan matriks FF ===========================================================
        WritableWorkbook workbook = Workbook.createWorkbook(new File("Matriks Frekuensi - " + domain + ".xls"));
        WritableSheet sheet = workbook.createSheet("FF", 0);
        int i = 3;
        Label label = new Label(0, 0, "Frekuensi Kemunculan Fitur di Semua Dokumen");
        Label label0 = new Label(0, 2, "FITUR");
        Label label1 = new Label(1, 2, "Produk 1");
        Label label2 = new Label(2, 2, "Produk 2");
        Label label3 = new Label(3, 2, "Produk 3");
        Label label4 = new Label(4, 2, "Produk 4");
        Label label5 = new Label(5, 2, "Produk 5");
        Label label6 = new Label(6, 2, "Produk 6");
        Label label7 = new Label(7, 2, "Produk 7");
        Label label8 = new Label(8, 2, "Produk 8");
        Label label9 = new Label(9, 2, "Produk 9");
        Label label10 = new Label(10, 2, "Produk 10");
        Label label11 = new Label(11, 2, "FF");
        Label label12 = new Label(12, 2, "log(FF)");
        sheet.addCell(label);
        sheet.addCell(label0);
        sheet.addCell(label1);
        sheet.addCell(label2);
        sheet.addCell(label3);
        sheet.addCell(label4);
        sheet.addCell(label5);
        sheet.addCell(label6);
        sheet.addCell(label7);
        sheet.addCell(label8);
        sheet.addCell(label9);
        sheet.addCell(label10);
        sheet.addCell(label11);
        sheet.addCell(label12);
        WritableCellFormat integerFormat = new WritableCellFormat(NumberFormats.INTEGER);
        WritableCellFormat floatFormat = new WritableCellFormat(NumberFormats.FLOAT);
        for (Map.Entry<String, Integer[]> entry : frek.entrySet()) {
            String key = entry.getKey();
            Integer value[] = entry.getValue();
            Label lblFitur = new Label(0, i, key);
            sheet.addCell(lblFitur);
            int FF = 0;
            for (int j = 0; j < value.length; j++) {
                FF = FF + value[j];
                jxl.write.Number lbl1 = new jxl.write.Number(j + 1, i, value[j], integerFormat);
                sheet.addCell(lbl1);
            }
            jxl.write.Number lblFF = new jxl.write.Number(11, i, FF, integerFormat);
            sheet.addCell(lblFF);
            double finalFF = 1 + Math.log10(FF);
            jxl.write.Number lblfinalFF = new jxl.write.Number(12, i, finalFF, floatFormat);
            sheet.addCell(lblfinalFF);
            sv.jml_komen_fitur.put(key, FF);
            logFF.put(key, finalFF);
            i++;
        }
        
        

//============== Tampilkan matriks DF ===========================================================
        WritableSheet sheet1 = workbook.createSheet("ProbDF", 1);
        i = 3;
        Label Dlabel = new Label(0, 0, "Frekuensi Dokumen yang Mengandung FItur");
        Label Dlabel0 = new Label(0, 2, "FITUR");
        Label Dlabel1 = new Label(1, 2, "Produk 1");
        Label Dlabel2 = new Label(2, 2, "Produk 2");
        Label Dlabel3 = new Label(3, 2, "Produk 3");
        Label Dlabel4 = new Label(4, 2, "Produk 4");
        Label Dlabel5 = new Label(5, 2, "Produk 5");
        Label Dlabel6 = new Label(6, 2, "Produk 6");
        Label Dlabel7 = new Label(7, 2, "Produk 7");
        Label Dlabel8 = new Label(8, 2, "Produk 8");
        Label Dlabel9 = new Label(9, 2, "Produk 9");
        Label Dlabel10 = new Label(10, 2, "Produk 10");
        Label Dlabel11 = new Label(11, 2, "df");
        Label Dlabel12 = new Label(12, 2, "1/idf");
        Label Dlabel13 = new Label(13, 2, "1/(idf-prob)");
        sheet1.addCell(Dlabel);
        sheet1.addCell(Dlabel0);
        sheet1.addCell(Dlabel1);
        sheet1.addCell(Dlabel2);
        sheet1.addCell(Dlabel3);
        sheet1.addCell(Dlabel4);
        sheet1.addCell(Dlabel5);
        sheet1.addCell(Dlabel6);
        sheet1.addCell(Dlabel7);
        sheet1.addCell(Dlabel8);
        sheet1.addCell(Dlabel9);
        sheet1.addCell(Dlabel10);
        sheet1.addCell(Dlabel11);
        sheet1.addCell(Dlabel12);
        sheet1.addCell(Dlabel13);
        WritableCellFormat integerFormat1 = new WritableCellFormat(NumberFormats.INTEGER);
        WritableCellFormat floatFormat1 = new WritableCellFormat(NumberFormats.FLOAT);
        for (Map.Entry<String, Integer[]> entry : dokFrek.entrySet()) {
            String key = entry.getKey();
            Integer value[] = entry.getValue();
            Label lblFitur = new Label(0, i, key);
            sheet1.addCell(lblFitur);
            int DF = 0;
            for (int j = 0; j < value.length; j++) {
                DF = DF + value[j];
                jxl.write.Number lbl1 = new jxl.write.Number(j + 1, i, value[j], integerFormat1);
                sheet1.addCell(lbl1);
            }
            double idf = Math.log10(N / DF);
            double p = N - DF;
            double prob = p / DF;
            double probIDF = Math.log10(prob);
            jxl.write.Number lblDF = new jxl.write.Number(11, i, DF, integerFormat1);
            jxl.write.Number lblIDF = new jxl.write.Number(12, i, 1 / idf, floatFormat1);
            jxl.write.Number lblProbDF = new jxl.write.Number(13, i, 1 / probIDF, floatFormat1);
            sheet1.addCell(lblDF);
            sheet1.addCell(lblIDF);
            sheet1.addCell(lblProbDF);
            sv.DF_fitur.put(key, DF);
            IDF.put(key, idf);
            IDF_prob.put(key, probIDF);
            i++;
        }
        
        
//===================Hitung FF.ProbDF======================================================
        WritableSheet sheet2 = workbook.createSheet("Perbandingan", 2);
        i = 3;
        Label Flabel = new Label(0, 0, "Hitung FF.ProbDF");
        Label Flabel0 = new Label(0, 2, "FITUR");
        Label Flabel1 = new Label(1, 2, "FF");
        Label Flabel2 = new Label(2, 2, "log(FF)");
        Label Flabel3 = new Label(3, 2, "1/IDF");
        Label Flabel4 = new Label(4, 2, "FF/IDF");
        Label Flabel5 = new Label(5, 2, "log(FF)/IDF");
        Label Flabel6 = new Label(6, 2, "FF/IDF_prob");
        Label Flabel7 = new Label(7, 2, "log(FF)/IDF_prob");
        sheet2.addCell(Flabel);
        sheet2.addCell(Flabel0);
        sheet2.addCell(Flabel1);
        sheet2.addCell(Flabel2);
        sheet2.addCell(Flabel3);
        sheet2.addCell(Flabel4);
        sheet2.addCell(Flabel5);
        sheet2.addCell(Flabel6);
        sheet2.addCell(Flabel7);
        WritableCellFormat integerFormat2 = new WritableCellFormat(NumberFormats.INTEGER);
        WritableCellFormat floatFormat2 = new WritableCellFormat(NumberFormats.FLOAT);
        for (Map.Entry<String, Double> entry : logFF.entrySet()) {
            String key = entry.getKey();
            Integer sFF = sv.jml_komen_fitur.get(key);
            Double sLogFF = entry.getValue();
            Double sIDF = IDF.get(key);
            Double sIdfProb = IDF_prob.get(key);
            Label Flbl0 = new Label(0, i, key);
            sheet2.addCell(Flbl0);
            jxl.write.Number Flbl1 = new jxl.write.Number(1, i, sFF, integerFormat2);
            jxl.write.Number Flbl2 = new jxl.write.Number(2, i, sLogFF, floatFormat2);
            jxl.write.Number Flbl3 = new jxl.write.Number(3, i, 1 / sIDF, floatFormat2);
            jxl.write.Number Flbl4 = new jxl.write.Number(4, i, sFF / sIDF, floatFormat2);
            jxl.write.Number Flbl5 = new jxl.write.Number(5, i, sLogFF / sIDF, floatFormat2);
            jxl.write.Number Flbl6 = new jxl.write.Number(6, i, sFF / sIdfProb, floatFormat2);
            jxl.write.Number Flbl7 = new jxl.write.Number(7, i, sLogFF / sIdfProb, floatFormat2);
            sv.bobotRelevansi.put(key, sLogFF / sIdfProb);
            sheet2.addCell(Flbl1);
            sheet2.addCell(Flbl2);
            sheet2.addCell(Flbl3);
            sheet2.addCell(Flbl4);
            sheet2.addCell(Flbl5);
            sheet2.addCell(Flbl6);
            sheet2.addCell(Flbl7);
            i++;
        }
        
        

        workbook.write();
        workbook.close();

        System.out.println("amod -> " + sv.ruleAmod);
        System.out.println("nsubj -> " + sv.ruleNsubj);
        System.out.println("prep_with -> " + sv.rulePrepWith);
        System.out.println("conj_and(nn,nn) -> " + sv.ruleConjAndAdj);
        System.out.println("conj_and(adj,adj) -> " + sv.ruleConjAndNoun);
        System.out.println("det_no -> " + sv.ruleNo);
        
        

        gsf1 = proc.hitungGSF(path1, fitur1, MinimalKomen);
        gsf2 = proc.hitungGSF(path2, fitur2, MinimalKomen);
        gsf3 = proc.hitungGSF(path3, fitur3, MinimalKomen);
        gsf4 = proc.hitungGSF(path4, fitur4, MinimalKomen);
        gsf5 = proc.hitungGSF(path5, fitur5, MinimalKomen);
        gsf6 = proc.hitungGSF(path6, fitur6, MinimalKomen);
        gsf7 = proc.hitungGSF(path7, fitur7, MinimalKomen);
        gsf8 = proc.hitungGSF(path8, fitur8, MinimalKomen);
        gsf9 = proc.hitungGSF(path9, fitur9, MinimalKomen);
        gsf10 = proc.hitungGSF(path10, fitur10, MinimalKomen);
        

        System.out.println("Hitung GSF Selesai");
    }

    public void RunDataCSF(String produk1, String produk2) throws IOException, WriteException {
        jTable1.setModel(new DefaultTableModel());

        WritableWorkbook workbook = Workbook.createWorkbook(new File("Data/Data Uji/" + domain + "/" + produk1 + " VS " + produk2 + ".xls"));
        WritableSheet sheet = workbook.createSheet("GSF CSF", 0);

        Label label = new Label(0, 0, "Nilai GSF, GSFs (GFS similarity) dan CSF Kedua Produk");
        Label label0 = new Label(0, 2, produk1);
        Label label1 = new Label(0, 3, "FITUR");
        Label label2 = new Label(1, 3, "GSF");
        Label label3 = new Label(2, 3, "GSFs");
        Label label4 = new Label(3, 3, "CSF");
        Label label5 = new Label(5, 2, produk2);
        Label label6 = new Label(5, 3, "FITUR");
        Label label7 = new Label(6, 3, "GSF");
        Label label8 = new Label(7, 3, "GSFs");
        Label label9 = new Label(8, 3, "CSF");

        sheet.addCell(label);
        sheet.addCell(label0);
        sheet.addCell(label1);
        sheet.addCell(label2);
        sheet.addCell(label3);
        sheet.addCell(label4);
        sheet.addCell(label5);
        sheet.addCell(label6);
        sheet.addCell(label7);
        sheet.addCell(label8);
        sheet.addCell(label9);

        Map<String, nilai_gsf_csf> gsfcsf1 = null;
        Map<String, nilai_gsf_csf> gsfcsf2 = null;
        
        HashMap<String, Double> data_produk1 = null;
        HashMap<String, Double> data_produk2 = null;

        if (produk1.contains("Optimus") || produk1.contains("42LK450") || produk1.contains("Canon")) {
            gsfcsf1 = proc.Bandingkan(gsf1, produk2);
            data_produk1 = gsf1;
        } else if (produk1.contains("Iphone") || produk1.contains("VIERA") || produk1.contains("FinePix")) {
            gsfcsf1 = proc.Bandingkan(gsf2, produk2);
            data_produk1 = gsf2;
        } else if (produk1.contains("Xperia") || produk1.contains("UN40ES6100") || produk1.contains("GE Power")) {
            gsfcsf1 = proc.Bandingkan(gsf3, produk2);
            data_produk1 = gsf3;
        } else if (produk1.contains("Curve") || produk1.contains("UN46EH5300") || produk1.contains("Kodak EasyShare")) {
            gsfcsf1 = proc.Bandingkan(gsf4, produk2);
            data_produk1 = gsf4;
        } else if (produk1.contains("Torch") || produk1.contains("Sharp") || produk1.contains("Coolpix")) {
            gsfcsf1 = proc.Bandingkan(gsf5, produk2);
            data_produk1 = gsf5;
        } else if (produk1.contains("HTC") || produk1.contains("KDL40EX640") || produk1.contains("D7100")) {
            gsfcsf1 = proc.Bandingkan(gsf6, produk2);
            data_produk1 = gsf6;
        } else if (produk1.contains("Motorola") || produk1.contains("KDL46BX450") || produk1.contains("Olympus")) {
            gsfcsf1 = proc.Bandingkan(gsf7, produk2);
            data_produk1 = gsf7;
        } else if (produk1.contains("Nokia") || produk1.contains("L40FHDP60") || produk1.contains("Lumix")) {
            gsfcsf1 = proc.Bandingkan(gsf8, produk2);
            data_produk1 = gsf8;
        } else if (produk1.contains("Galaxy") || produk1.contains("19SLV411U") || produk1.contains("Pentax")) {
            gsfcsf1 = proc.Bandingkan(gsf9, produk2);
            data_produk1 = gsf9;
        } else if (produk1.contains("Nexus") || produk1.contains("Vizio") || produk1.contains("DSC-HX200V")) {
            gsfcsf1 = proc.Bandingkan(gsf10, produk2);
            data_produk1 = gsf10;
        }

        if (produk2.contains("Optimus") || produk2.contains("42LK450") || produk2.contains("Canon")) {
            gsfcsf2 = proc.Bandingkan(gsf1, produk1);
            data_produk2 = gsf1;
        } else if (produk2.contains("Iphone") || produk2.contains("VIERA") || produk2.contains("FinePix")) {
            gsfcsf2 = proc.Bandingkan(gsf2, produk1);
            data_produk2 = gsf2;
        } else if (produk2.contains("Xperia") || produk2.contains("UN40ES6100") || produk2.contains("GE Power")) {
            gsfcsf2 = proc.Bandingkan(gsf3, produk1);
            data_produk2 = gsf3;
        } else if (produk2.contains("Curve") || produk2.contains("UN46EH5300") || produk2.contains("Kodak EasyShare")) {
            gsfcsf2 = proc.Bandingkan(gsf4, produk1);
            data_produk2 = gsf4;
        } else if (produk2.contains("Torch") || produk2.contains("Sharp") || produk2.contains("Coolpix")) {
            gsfcsf2 = proc.Bandingkan(gsf5, produk1);
            data_produk2 = gsf5;
        } else if (produk2.contains("HTC") || produk2.contains("KDL40EX640") || produk2.contains("D7100")) {
            gsfcsf2 = proc.Bandingkan(gsf6, produk1);
            data_produk2 = gsf6;
        } else if (produk2.contains("Motorola") || produk2.contains("KDL46BX450") || produk2.contains("Olympus")) {
            gsfcsf2 = proc.Bandingkan(gsf7, produk1);
            data_produk2 = gsf7;
        } else if (produk2.contains("Nokia") || produk2.contains("L40FHDP60") || produk2.contains("Lumix")) {
            gsfcsf2 = proc.Bandingkan(gsf8, produk1);
            data_produk2 = gsf8;
        } else if (produk2.contains("Galaxy") || produk2.contains("19SLV411U") || produk2.contains("Pentax")) {
            gsfcsf2 = proc.Bandingkan(gsf9, produk1);
            data_produk2 = gsf9;
        } else if (produk2.contains("Nexus") || produk2.contains("Vizio") || produk2.contains("DSC-HX200V")) {
            gsfcsf2 = proc.Bandingkan(gsf10, produk1);
            data_produk2 = gsf10;
        }
        
        //baca file synonim-features
        if (domain.equalsIgnoreCase("camera")) {
            sinonim_features = SynonimFeatures.getSynonim("camera");
        }
        else if (domain.equalsIgnoreCase("phone")) {
            sinonim_features = SynonimFeatures.getSynonim("phone");
        }
        else if (domain.equalsIgnoreCase("tv")) {
            sinonim_features = SynonimFeatures.getSynonim("tv");
        }
        
        /*for (Map.Entry entry : sinonim_features.entrySet()) {
            System.out.println(entry.getKey() + ", " + entry.getValue());
        }*/

        //memasukkan fitur yang sama
        proc.crossCheck(gsfcsf1, gsfcsf2);
        Map<String, nilai_gsf_csf> katgsfcsf1 = proc.GabungKategori(gsfcsf1);
        Map<String, nilai_gsf_csf> katgsfcsf2 = proc.GabungKategori(gsfcsf2);

        int j = 4;
        for (Map.Entry<String, nilai_gsf_csf> entry : katgsfcsf1.entrySet()) {
            String key = entry.getKey();
            nilai_gsf_csf value = entry.getValue();
            Label lblFitur = new Label(0, j, key);
            Label lblGSF = new Label(1, j, value.gsf.toString());
            Label lblCSF = new Label(3, j, value.csf.toString());
            sheet.addCell(lblFitur);
            sheet.addCell(lblGSF);
            sheet.addCell(lblCSF);
            j++;
        }

        int k = 4;
        for (Map.Entry<String, nilai_gsf_csf> entry : katgsfcsf2.entrySet()) {
            String key = entry.getKey();
            nilai_gsf_csf value = entry.getValue();
            Label lblFitur = new Label(5, k, key);
            Label lblGSF = new Label(6, k, value.gsf.toString());
            Label lblCSF = new Label(8, k, value.csf.toString());
            sheet.addCell(lblFitur);
            sheet.addCell(lblGSF);
            sheet.addCell(lblCSF);
            k++;
        }
        
        //update kategori berdasarkan sinonim
        Map<String, nilai_gsf_csf> katgsfcsf1_versi2 = new HashMap<String, nilai_gsf_csf>();
        Map<String, nilai_gsf_csf> katgsfcsf2_versi2 = new HashMap<String, nilai_gsf_csf>();
        
        for (Map.Entry<String, nilai_gsf_csf> entry : katgsfcsf1.entrySet()) {
            String fitur = entry.getKey();
            nilai_gsf_csf value = entry.getValue();
            int ketemu = 0;
            
            for (Map.Entry<String, String[]> entry2 : sinonim_features.entrySet()) {
                String fitur_utama   = entry2.getKey();
                String[] arr_fitur     = entry2.getValue();
//                System.out.println("cek fitur "+ fitur_utama);
                //cek apakah penamaan fitur sama dengan di arr_fitur
                for (int i = 0; i < arr_fitur.length; i++) {
//                    System.out.println("isinya "+ arr_fitur[i]+ " ? "+fitur);
                    if(fitur.equals(arr_fitur[i])){
//                        System.out.println("fitur "+ fitur +" sama dengan "+arr_fitur[i] + " menjadi "+fitur_utama);
                        if(katgsfcsf1_versi2.containsKey(fitur_utama)){
                            double gsf_lama = value.gsf;
                            double csf_lama = value.csf;
                            double gsf_ada = katgsfcsf1_versi2.get(fitur_utama).gsf;
                            double csf_ada = katgsfcsf1_versi2.get(fitur_utama).csf;
                            double gsf_baru = gsf_lama + gsf_ada;
                            double csf_baru = csf_lama + csf_ada;
                            nilai_gsf_csf nilai_baru = new nilai_gsf_csf(gsf_baru, csf_baru);                            
                            katgsfcsf1_versi2.put(fitur_utama, nilai_baru);
                        }
                        else{
                            katgsfcsf1_versi2.put(fitur_utama, value);
                        }
                        
                        ketemu = 1;
                        break;
                    }
                }
                
                if(ketemu==1){
                    break;
                }
            }
            
            if(ketemu == 0){ //tidak cocok
                katgsfcsf1_versi2.put(fitur, value);
            }
        }
        
        for (Map.Entry<String, nilai_gsf_csf> entry : katgsfcsf2.entrySet()) {
            String fitur = entry.getKey();
            nilai_gsf_csf value = entry.getValue();
            int ketemu = 0;
            
            for (Map.Entry<String, String[]> entry2 : sinonim_features.entrySet()) {
                String fitur_utama   = entry2.getKey();
                String[] arr_fitur     = entry2.getValue();
//                System.out.println("cek fitur "+ fitur_utama);
                //cek apakah penamaan fitur sama dengan di arr_fitur
                for (int i = 0; i < arr_fitur.length; i++) {
//                    System.out.println("isinya "+ arr_fitur[i]+ " ? "+fitur);
                    if(fitur.equals(arr_fitur[i])){
//                        System.out.println("fitur "+ fitur +" sama dengan "+arr_fitur[i] + " menjadi "+fitur_utama);
                        if(katgsfcsf2_versi2.containsKey(fitur_utama)){
                            double gsf_lama = value.gsf;
                            double csf_lama = value.csf;
                            double gsf_ada = katgsfcsf2_versi2.get(fitur_utama).gsf;
                            double csf_ada = katgsfcsf2_versi2.get(fitur_utama).csf;
                            double gsf_baru = gsf_lama + gsf_ada;
                            double csf_baru = csf_lama + csf_ada;
                            nilai_gsf_csf nilai_baru = new nilai_gsf_csf(gsf_baru, csf_baru);                            
                            katgsfcsf2_versi2.put(fitur_utama, nilai_baru);
                        }
                        else{
                            katgsfcsf2_versi2.put(fitur_utama, value);
                        }
                        
                        ketemu = 1;
                        break;
                    }
                }
                
                if(ketemu==1){
                    break;
                }
            }
            
            if(ketemu == 0){ //tidak cocok
                katgsfcsf2_versi2.put(fitur, value);
            }
        }

        //buat map fitur_sama, dan fitur_beda antara 2 produk
        Map<String, nilai_gsf_csf> fitur_sama1 = new HashMap<String, nilai_gsf_csf>();
        Map<String, nilai_gsf_csf> fitur_sama2 = new HashMap<String, nilai_gsf_csf>();
        Map<String, nilai_gsf_csf> fitur_beda1 = new HashMap<String, nilai_gsf_csf>();
        Map<String, nilai_gsf_csf> fitur_beda2 = new HashMap<String, nilai_gsf_csf>();
        
        for (Map.Entry<String, nilai_gsf_csf> entry : katgsfcsf1_versi2.entrySet()) {
            String fitur_p1 = entry.getKey();
            nilai_gsf_csf value_p1 = entry.getValue();
            int ketemu = 0;
            
            for (Map.Entry<String, nilai_gsf_csf> entry2 : katgsfcsf2_versi2.entrySet()) {
                String fitur_p2 = entry2.getKey();
                nilai_gsf_csf value_p2 = entry2.getValue();
                
                //jika fitur sama maka masuk
                if(fitur_p1.equals(fitur_p2)){
                    fitur_sama1.put(fitur_p1, value_p1);
                    fitur_sama2.put(fitur_p2, value_p2);
                    ketemu = 1;
                    break;
                }
            }
            
            //jika gk ada yang sama maka masuk fitur_beda1
            if(ketemu == 0){
                fitur_beda1.put(fitur_p1, value_p1);
            }
        }
        
        //cari fitur_beda2
        for (Map.Entry<String, nilai_gsf_csf> entry : katgsfcsf2_versi2.entrySet()) {
            String fitur_p2 = entry.getKey();
            nilai_gsf_csf value_p2 = entry.getValue();
            int ketemu = 0;
            
            for (Map.Entry<String, nilai_gsf_csf> entry2 : fitur_sama2.entrySet()) {
                String fitur_s2 = entry2.getKey();
                nilai_gsf_csf value_s2 = entry2.getValue();
                
                //jika fitur sama maka masuk
                if(fitur_p2.equals(fitur_s2)){
                    ketemu = 1;
                    break;
                }
            }
            
            //jika gk ada yang sama maka masuk fitur_beda1
            if(ketemu == 0){
                fitur_beda2.put(fitur_p2, value_p2);
            }
        }
        
//        System.out.println("Print SAMA1");
//        for (Map.Entry entry : fitur_sama1.entrySet()) {
//            System.out.println(entry.getKey() + ", " + entry.getValue());
//        }
//        
//        System.out.println("Print SAMA2");
//        for (Map.Entry entry : fitur_sama2.entrySet()) {
//            System.out.println(entry.getKey() + ", " + entry.getValue());
//        }
//        
//        System.out.println("Print BEDA1");
//        for (Map.Entry entry : fitur_beda1.entrySet()) {
//            System.out.println(entry.getKey() + ", " + entry.getValue());
//        }
//        
//        System.out.println("Print BEDA2");
//        for (Map.Entry entry : fitur_beda2.entrySet()) {
//            System.out.println(entry.getKey() + ", " + entry.getValue());
//        }
        
        Map<String, nilai_gsf_csf> new_fitur_sama1 = fitur_sama1;
        Map<String, nilai_gsf_csf> new_fitur_sama2 = fitur_sama2;
        
        //hitung kemiripan dari fitur_beda1 untuk membentuk new_fitur_sama1
        for (Map.Entry<String, nilai_gsf_csf> entry : fitur_beda1.entrySet()) {
            String fitur1           = entry.getKey();
            nilai_gsf_csf value1    = entry.getValue();
            
            double max_bobot_similar    = 0.0;
            int ketemu                  = 0;
            String key_sama1            = "";
            
            for (Map.Entry<String, nilai_gsf_csf> entry2 : fitur_sama1.entrySet()) {
                String fitur_s1         = entry2.getKey();
                nilai_gsf_csf value_s1  = entry2.getValue();
                //hitung bobot kemiripan dengan jaccard
                Jaccard ja = new Jaccard(3);
                //System.out.println("CEK "+fitur1 + ", "+ fitur_s1);
                double similarity_check = ja.similarity(fitur1, fitur_s1);
                //System.out.println("nilai : "+similarity_check);
                if(similarity_check > max_bobot_similar){ //threshold sementara 0, jika perlu threshold maka masukkan disini
                    max_bobot_similar   = similarity_check;
                    key_sama1           = fitur_s1;
                    ketemu              = 1;
                    //System.out.println("ADA : "+key_sama1 + ", bobot : "+ max_bobot_similar);
                }
            }
            
            //System.out.println("key besar : "+key_sama1 + ", bobot : "+ max_bobot_similar + " KETEMU : "+ketemu);
            //jika ada maka update nilai new_fitur_sama1
            if(ketemu == 1 && new_fitur_sama1.containsKey(key_sama1)){ System.out.println("masuk update : "+key_sama1);
                double gsf_lama = new_fitur_sama1.get(key_sama1).gsf;
                double csf_lama = new_fitur_sama1.get(key_sama1).csf;
                double gsf_baru = gsf_lama + (value1.gsf * max_bobot_similar);
                double csf_baru = csf_lama + (value1.csf * max_bobot_similar);
                nilai_gsf_csf nilai_baru = new nilai_gsf_csf(gsf_baru, csf_baru); 
                new_fitur_sama1.put(key_sama1, nilai_baru);
            }
        }
        
        //hitung kemiripan dari fitur_beda1 untuk membentuk new_fitur_sama1
        for (Map.Entry<String, nilai_gsf_csf> entry : fitur_beda2.entrySet()) {
            String fitur2           = entry.getKey();
            nilai_gsf_csf value2    = entry.getValue();
            
            double max_bobot_similar    = 0.0;
            int ketemu                  = 0;
            String key_sama2            = "";
            
            for (Map.Entry<String, nilai_gsf_csf> entry2 : fitur_sama2.entrySet()) {
                String fitur_s2         = entry2.getKey();
                nilai_gsf_csf value_s2  = entry2.getValue();
                //hitung bobot kemiripan dengan jaccard
                Jaccard ja = new Jaccard(3);
                //System.out.println("CEK "+fitur2 + ", "+ fitur_s2);
                double similarity_check = ja.similarity(fitur2, fitur_s2);
                if(similarity_check > max_bobot_similar){ //threshold sementara 0, jika perlu threshold maka masukkan disini
                    max_bobot_similar   = similarity_check;
                    key_sama2           = fitur_s2;
                    ketemu              = 1;
                }
            }
            
            //jika ada maka update nilai new_fitur_sama1
            if(ketemu == 1 && new_fitur_sama2.containsKey(key_sama2)){
                double gsf_lama = new_fitur_sama2.get(key_sama2).gsf;
                double csf_lama = new_fitur_sama2.get(key_sama2).csf;
                double gsf_baru = gsf_lama + (value2.gsf * max_bobot_similar);
                double csf_baru = csf_lama + (value2.csf * max_bobot_similar);
                nilai_gsf_csf nilai_baru = new nilai_gsf_csf(gsf_baru, csf_baru); 
                new_fitur_sama2.put(key_sama2, nilai_baru);
            }
        }
        
//        System.out.println("Print NILAI1");
//        for (Map.Entry entry : new_fitur_sama1.entrySet()) {
//            System.out.println(entry.getKey() + ", " + entry.getValue());
//        }
//        
//        System.out.println("Print NILAI2");
//        for (Map.Entry entry : new_fitur_sama2.entrySet()) {
//            System.out.println(entry.getKey() + ", " + entry.getValue());
//        }
        
        
        //hitung skor fitur produk 1
        Map<String, Double> skor_fitur1 = new HashMap<String, Double>();
        double my_alpha = proc.alpha;
        double max_gfs1  = 0.0;
        double min_gfs1  = 1000000.0;
        double max_cfs1  = 0.0;
        double min_cfs1  = 1000000.0;
        
        //cari max dan min gfs dan cfs produk 1
        for (Map.Entry<String, nilai_gsf_csf> entry : fitur_sama1.entrySet()) {
            String fitur1           = entry.getKey();
            nilai_gsf_csf value1    = entry.getValue();
            if(value1.gsf > max_gfs1){
                max_gfs1 = value1.gsf;
            }
            if(value1.gsf < min_gfs1){
                min_gfs1 = value1.gsf;
            }
            if(value1.csf > max_cfs1){
                max_cfs1 = value1.csf;
            }
            if(value1.csf < min_cfs1){
                min_cfs1 = value1.csf;
            }
        }
        
        for (Map.Entry<String, nilai_gsf_csf> entry : fitur_sama1.entrySet()) {
            String fitur1           = entry.getKey();
            nilai_gsf_csf value1    = entry.getValue();
            
            Double nilai = (my_alpha*((value1.gsf-min_gfs1)/(max_gfs1-min_gfs1)))+((1-my_alpha)*((value1.csf-min_cfs1)/(max_cfs1-min_cfs1)));
            skor_fitur1.put(fitur1,nilai);
        }
        
        //hitung skor fitur produk 2
        Map<String, Double> skor_fitur2 = new HashMap<String, Double>();
        double max_gfs2  = 0.0;
        double min_gfs2  = 1000000.0;
        double max_cfs2  = 0.0;
        double min_cfs2  = 1000000.0;
        
        //cari max dan min gfs dan cfs produk 1
        for (Map.Entry<String, nilai_gsf_csf> entry : fitur_sama2.entrySet()) {
            String fitur2           = entry.getKey();
            nilai_gsf_csf value2    = entry.getValue();
            if(value2.gsf > max_gfs2){
                max_gfs2 = value2.gsf;
            }
            if(value2.gsf < min_gfs2){
                min_gfs2 = value2.gsf;
            }
            if(value2.csf > max_cfs2){
                max_cfs2 = value2.csf;
            }
            if(value2.csf < min_cfs2){
                min_cfs2 = value2.csf;
            }
        }
        
        for (Map.Entry<String, nilai_gsf_csf> entry : fitur_sama2.entrySet()) {
            String fitur2           = entry.getKey();
            nilai_gsf_csf value2    = entry.getValue();
            
            Double nilai = (my_alpha*((value2.gsf-min_gfs2)/(max_gfs2-min_gfs2)))+((1-my_alpha)*((value2.csf-min_cfs2)/(max_cfs2-min_cfs2)));
            skor_fitur2.put(fitur2,nilai);
        }
        
        System.out.println("Print FITUR 1");
        for (Map.Entry entry : skor_fitur1.entrySet()) {
            System.out.println(entry.getKey() + ", " + entry.getValue());
        }
        
        System.out.println("Print FITUR 2");
        for (Map.Entry entry : skor_fitur2.entrySet()) {
            System.out.println(entry.getKey() + ", " + entry.getValue());
        }
        
        //        System.exit(0);
        //HITUNG SKOR AKHIR PAKE RUMUS BARU
        
        int t = Math.max(j, k) + 3;
        double total_bobot = 0.0;
        double skor_akhir1 = 0.0;
        double skor_akhir2 = 0.0;
        for (Map.Entry<String, Double> entry : skor_fitur1.entrySet()) {
            String get_label1 = entry.getKey();
            Double get_nilai_label1 = entry.getValue();
            
            Label lblF = new Label(0, t, get_label1);
            Label lblS1 = new Label(1, t, "" + get_nilai_label1);
            sheet.addCell(lblF);
            sheet.addCell(lblS1);
            t++;
            total_bobot = total_bobot + sv.bobotRelevansi.get(get_label1);
            skor_akhir1 = skor_akhir1 + (sv.bobotRelevansi.get(get_label1) * get_nilai_label1);
        }
        
        int t2 = Math.max(j, k) + 3;
        for (Map.Entry<String, Double> entry : skor_fitur2.entrySet()) {
            String get_label2 = entry.getKey();
            Double get_nilai_label2 = entry.getValue();
            
            Label lblS2 = new Label(2, t2, "" + get_nilai_label2);
            sheet.addCell(lblS2);
            t2++;
            skor_akhir2 = skor_akhir2 + (sv.bobotRelevansi.get(get_label2) * get_nilai_label2);
        }
        
        Double new_skor_akhir1, new_skor_akhir2;
        
        new_skor_akhir1 = skor_akhir1/total_bobot;
        new_skor_akhir2 = skor_akhir2/total_bobot;
        
        String pp = "";

////======Tampilkan Hasil Perbandingan Skor Rekomendasi===========================
        if (new_skor_akhir1 < new_skor_akhir2) {
            System.out.println("2");
            pp = "2";
        } else if (new_skor_akhir1 > new_skor_akhir2) {
            System.out.println("1");
            pp = "1";
        } else {
            System.out.println("0");
            pp = "tidak ada";
        }
        
        System.out.println("HASILNYA:");
        System.out.println(new_skor_akhir1);
        System.out.println(new_skor_akhir2);

        jLabel13.setText("" + new_skor_akhir1);
        jLabel14.setText("" + new_skor_akhir2);
//        jTable1.setModel(new ModelHasil(lhc));
        
        Label lblakhir = new Label(10, 2, "HASIL AKHIR");
        Label lblakhir1 = new Label(10, 3, "Produk 1");
        Label lblakhir2 = new Label(11, 3, "Produk 2");
        Label lblakhir11 = new Label(10, 4, String.valueOf(new_skor_akhir1));
        Label lblakhir22 = new Label(11, 4, String.valueOf(new_skor_akhir2));
        Label lblakhir3 = new Label(10, 5, "Pemenangnya adalah produk "+pp);
        sheet.addCell(lblakhir);
        sheet.addCell(lblakhir1);
        sheet.addCell(lblakhir2);
        sheet.addCell(lblakhir11);
        sheet.addCell(lblakhir22);
        sheet.addCell(lblakhir3);


        workbook.write();
        workbook.close();

    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jInternalFrame1 = new javax.swing.JInternalFrame();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox();
        jLabel4 = new javax.swing.JLabel();
        jComboBox2 = new javax.swing.JComboBox();
        jButton1 = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jSeparator4 = new javax.swing.JSeparator();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jComboBox3 = new javax.swing.JComboBox();
        jSeparator3 = new javax.swing.JSeparator();
        jSeparator5 = new javax.swing.JSeparator();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();

        jInternalFrame1.setVisible(true);

        javax.swing.GroupLayout jInternalFrame1Layout = new javax.swing.GroupLayout(jInternalFrame1.getContentPane());
        jInternalFrame1.getContentPane().setLayout(jInternalFrame1Layout);
        jInternalFrame1Layout.setHorizontalGroup(
            jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jInternalFrame1Layout.setVerticalGroup(
            jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel1.setText("Aplikasi Perbandingan Produk");

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel2.setText("Berdasarkan Bobot Fitur dan Bobot Kalimat Comparative Pada Teks Opini");

        jLabel3.setText("Pilih Produk Yang Ingin Dibandingkan");

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "<Pilih Produk>" }));

        jLabel4.setText("VS");

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "<Pilih Produk>" }));

        jButton1.setFont(new java.awt.Font("Tahoma", 1, 11));
        jButton1.setText("COMPARE");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel5.setText("Produk 1");

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel6.setText("Produk 2");

        jLabel7.setText("Set Constant Variable :");

        jLabel8.setText("Alpha");

        jLabel9.setText("Threshold");

        jButton2.setText("Ekstrak Fitur Dan Hitung Nilai GSF");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel10.setText("Skor Fitur   :");

        jLabel11.setText("Produk 1");

        jLabel12.setText("Produk 2");

        jLabel13.setText("0");

        jLabel14.setText("0");

        jLabel15.setText("Pilih Domain Produk ");

        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "<Pilih Domain>", "smartphone", "camera", "tv" }));
        jComboBox3.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox3ItemStateChanged(evt);
            }
        });
        jComboBox3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox3ActionPerformed(evt);
            }
        });

        jLabel16.setIcon(new javax.swing.ImageIcon(getClass().getResource("/GUI/logoITS.png"))); // NOI18N

        jLabel17.setFont(new java.awt.Font("Tahoma", 2, 11)); // NOI18N
        jLabel17.setText("Oleh : Yufis Azhar, Agus Zainal Arifin, Diana Purwitasari");

        jLabel18.setFont(new java.awt.Font("Tahoma", 2, 11)); // NOI18N
        jLabel18.setText("Program Magister T. Informatika Institut Teknologi Sepuluh Nopember Surabaya 2013");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(layout.createSequentialGroup()
                                                .addGap(5, 5, 5)
                                                .addComponent(jLabel15)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addComponent(jSeparator4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 784, Short.MAX_VALUE))
                                        .addGap(3, 3, 3))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(8, 8, 8)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jSeparator5, javax.swing.GroupLayout.DEFAULT_SIZE, 779, Short.MAX_VALUE)
                                            .addComponent(jSeparator2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 779, Short.MAX_VALUE)
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(jLabel7)
                                                .addGap(296, 296, 296))
                                            .addGroup(layout.createSequentialGroup()
                                                .addGap(18, 18, 18)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(jLabel9)
                                                    .addComponent(jLabel8))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                                    .addComponent(jTextField2)
                                                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGap(18, 18, 18)
                                                .addComponent(jButton2))
                                            .addComponent(jSeparator3, javax.swing.GroupLayout.DEFAULT_SIZE, 779, Short.MAX_VALUE)
                                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 779, Short.MAX_VALUE)
                                            .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                    .addGroup(layout.createSequentialGroup()
                                                        .addComponent(jLabel3)
                                                        .addGap(18, 18, 18)
                                                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                    .addComponent(jLabel5))
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addGroup(layout.createSequentialGroup()
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                        .addComponent(jLabel4)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                        .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                        .addGap(97, 97, 97)
                                                        .addComponent(jLabel6)))
                                                .addGap(18, 18, 18)
                                                .addComponent(jButton1)))))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel16)
                                .addGap(31, 31, 31)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(149, 149, 149)
                                        .addComponent(jLabel1))
                                    .addComponent(jLabel2)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(87, 87, 87)
                                        .addComponent(jLabel17))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(11, 11, 11)
                                        .addComponent(jLabel18))))))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(160, 160, 160)
                                .addComponent(jLabel11))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(76, 76, 76)
                                .addComponent(jLabel10)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel13)))
                        .addGap(66, 66, 66)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel14)
                            .addComponent(jLabel12))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addComponent(jLabel1)
                        .addGap(5, 5, 5)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel17)
                        .addGap(2, 2, 2)
                        .addComponent(jLabel18)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(42, 42, 42)
                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel15)
                            .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(2, 2, 2)
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel9)
                            .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton2))
                        .addGap(13, 13, 13)
                        .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(jLabel5))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4)
                            .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 237, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(jLabel12))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14))
                .addContainerGap(19, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
//        jTable1.setModel(new DefaultTableModel());
        String produk1 = jComboBox1.getSelectedItem().toString();
        String produk2 = jComboBox2.getSelectedItem().toString();
        try {
            RunDataCSF(produk1, produk2);
        } catch (IOException exception) {
        } catch (WriteException we) {
        }
        //Tampilkan Hasil Perbandingan
//        jLabel13.setText("" + skor1);
//        jLabel14.setText("" + skor2);
//        jTable1.setModel(new ModelHasil(lhc));
        //jTable1.invalidate();
    }//GEN-LAST:event_jButton1ActionPerformed

    public Component getTableCellRendererComponent(JTable table) {

        return this;
    }

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        try {
            RunData(Double.parseDouble(jTextField1.getText().toString()), Integer.parseInt(jTextField2.getText().toString()));
        } catch (IOException e) {
        } catch (WriteException we) {
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jComboBox3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox3ActionPerformed

    private void jComboBox3ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox3ItemStateChanged
        // TODO add your handling code here:
        jComboBox1.removeAllItems();
        jComboBox2.removeAllItems();
        String[] smartphone = {"Google Nexus", "Apple Iphone 3GS", "Blackberry Curve 9360", "Blackberry Torch 9380", "HTC Inspire", "LG Optimus L7", "Motorola Atrix MB860", "Nokia Lumia 710", "Samsung Galaxy Ace Plus", "Sony Xperia U."};
        String[] camera = {"Nikon D7100", "Olympus E-PL2", "Sony Cyber-shot DSC-HX200V", "Panasonic Lumix DMC-ZS8", "Canon EOS Rebel T3i", "Kodak EasyShare Max Z990", "Pentax K20D", "Fujifilm FinePix S2950", "GE Power Pro X500-WH", "Nikon Coolpix P90"};
        String[] tv = {"Panasonic VIERA TC-P55VT50", "Vizio E3D420VX", "TCL L40FHDP60", "Samsung UN46EH5300", "Sony BRAVIA KDL40EX640", "Sharp LC-60LE633U", "LG 42LK450", "Samsung UN40ES6100", "Sony BRAVIA KDL46BX450", "Toshiba 19SLV411U"};

        if (jComboBox3.getSelectedItem().equals("smartphone")) {
            domain = "phone";
            jTextField1.setText("0.2");
            jTextField2.setText("10"); //80
            for (int i = 0; i < smartphone.length; i++) {
                jComboBox1.addItem(smartphone[i]);
                jComboBox2.addItem(smartphone[i]);
            }
        } else if (jComboBox3.getSelectedItem().equals("camera")) {
            domain = "camera";
            jTextField1.setText("0.4");
            jTextField2.setText("10"); //90
            for (int i = 0; i < camera.length; i++) {
                jComboBox1.addItem(camera[i]);
                jComboBox2.addItem(camera[i]);
            }
        } else if (jComboBox3.getSelectedItem().equals("tv")) {
            domain = "tv";
            jTextField1.setText("0.2");
            jTextField2.setText("10"); //100
            for (int i = 0; i < tv.length; i++) {
                jComboBox1.addItem(tv[i]);
                jComboBox2.addItem(tv[i]);
            }
        }
    }//GEN-LAST:event_jComboBox3ItemStateChanged

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new MainProgram().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JComboBox jComboBox2;
    private javax.swing.JComboBox jComboBox3;
    private javax.swing.JInternalFrame jInternalFrame1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    // End of variables declaration//GEN-END:variables
}
