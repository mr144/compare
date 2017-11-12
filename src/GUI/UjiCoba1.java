/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

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
import java.util.Arrays;
import jxl.*;
import jxl.write.*;
import testing.Utils;

public class UjiCoba1 {
    Utils utils = new Utils();
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
            path1 = "data/Data Asli/phone/LG Optimus L7 P705";
            path2 = "data/Data Asli/phone/Apple Iphone 3GS";
            path3 = "data/Data Asli/phone/Sony Xperia U";
            path4 = "data/Data Asli/phone/Blackberry Curve 9360";
            path5 = "data/Data Asli/phone/Blackberry Torch 9800";
            path6 = "data/Data Asli/phone/HTC Inspire";
            path7 = "data/Data Asli/phone/Motorola Atrix MB860";
            path8 = "data/Data Asli/phone/Nokia Lumia 710";
            path9 = "data/Data Asli/phone/Samsung Galaxy Ace Plus S7500";
            path10 = "data/Data Asli/phone/Google Nexus One";
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
        for (Map.Entry<FiturProduk, Double> entry : fitur2.entrySet()) {
            String key = entry.getKey().nama;
            String path = entry.getKey().path;
            int index = entry.getKey().index;
            String hasil = Utils.readDokumenString(path, index);
            System.out.println(key + " -> " + hasil);
        }
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
        WritableWorkbook workbook = Workbook.createWorkbook(new File("Data/Data Uji/" + domain + "/" + produk1 + " VS " + produk2 + ".xls"));
        WritableSheet sheet = workbook.createSheet("GSF CSF", 0);

        Label label = new Label(0, 0, "Nilai GSF CSF Kedua Produk");
        Label label0 = new Label(0, 2, produk1);
        Label label1 = new Label(0, 3, "FITUR");
        Label label2 = new Label(1, 3, "GSF");
        Label label3 = new Label(2, 3, "CSF");
        Label label4 = new Label(4, 2, produk2);
        Label label5 = new Label(4, 3, "FITUR");
        Label label6 = new Label(5, 3, "GSF");
        Label label7 = new Label(6, 3, "CSF");

        sheet.addCell(label);
        sheet.addCell(label0);
        sheet.addCell(label1);
        sheet.addCell(label2);
        sheet.addCell(label3);
        sheet.addCell(label4);
        sheet.addCell(label5);
        sheet.addCell(label6);
        sheet.addCell(label7);

        Map<String, nilai_gsf_csf> gsfcsf1 = null;
        Map<String, nilai_gsf_csf> gsfcsf2 = null;

        if (produk1.contains("Optimus") || produk1.contains("42LK450") || produk1.contains("Canon")) {
            gsfcsf1 = proc.Bandingkan(gsf1, produk2);
        } else if (produk1.contains("Iphone") || produk1.contains("VIERA") || produk1.contains("FinePix")) {
            gsfcsf1 = proc.Bandingkan(gsf2, produk2);
        } else if (produk1.contains("Xperia") || produk1.contains("UN40ES6100") || produk1.contains("GE Power")) {
            gsfcsf1 = proc.Bandingkan(gsf3, produk2);
        } else if (produk1.contains("Curve") || produk1.contains("UN46EH5300") || produk1.contains("Kodak EasyShare")) {
            gsfcsf1 = proc.Bandingkan(gsf4, produk2);
        } else if (produk1.contains("Torch") || produk1.contains("Sharp") || produk1.contains("Coolpix")) {
            gsfcsf1 = proc.Bandingkan(gsf5, produk2);
        } else if (produk1.contains("HTC") || produk1.contains("KDL40EX640") || produk1.contains("D7100")) {
            gsfcsf1 = proc.Bandingkan(gsf6, produk2);
        } else if (produk1.contains("Motorola") || produk1.contains("KDL46BX450") || produk1.contains("Olympus")) {
            gsfcsf1 = proc.Bandingkan(gsf7, produk2);
        } else if (produk1.contains("Nokia") || produk1.contains("L40FHDP60") || produk1.contains("Lumix")) {
            gsfcsf1 = proc.Bandingkan(gsf8, produk2);
        } else if (produk1.contains("Galaxy") || produk1.contains("19SLV411U") || produk1.contains("Pentax")) {
            gsfcsf1 = proc.Bandingkan(gsf9, produk2);
        } else if (produk1.contains("Nexus") || produk1.contains("Vizio") || produk1.contains("DSC-HX200V")) {
            gsfcsf1 = proc.Bandingkan(gsf10, produk2);
        }

        if (produk2.contains("Optimus") || produk2.contains("42LK450") || produk2.contains("Canon")) {
            gsfcsf2 = proc.Bandingkan(gsf1, produk1);
        } else if (produk2.contains("Iphone") || produk2.contains("VIERA") || produk2.contains("FinePix")) {
            gsfcsf2 = proc.Bandingkan(gsf2, produk1);
        } else if (produk2.contains("Xperia") || produk2.contains("UN40ES6100") || produk2.contains("GE Power")) {
            gsfcsf2 = proc.Bandingkan(gsf3, produk1);
        } else if (produk2.contains("Curve") || produk2.contains("UN46EH5300") || produk2.contains("Kodak EasyShare")) {
            gsfcsf2 = proc.Bandingkan(gsf4, produk1);
        } else if (produk2.contains("Torch") || produk2.contains("Sharp") || produk2.contains("Coolpix")) {
            gsfcsf2 = proc.Bandingkan(gsf5, produk1);
        } else if (produk2.contains("HTC") || produk2.contains("KDL40EX640") || produk2.contains("D7100")) {
            gsfcsf2 = proc.Bandingkan(gsf6, produk1);
        } else if (produk2.contains("Motorola") || produk2.contains("KDL46BX450") || produk2.contains("Olympus")) {
            gsfcsf2 = proc.Bandingkan(gsf7, produk1);
        } else if (produk2.contains("Nokia") || produk2.contains("L40FHDP60") || produk2.contains("Lumix")) {
            gsfcsf2 = proc.Bandingkan(gsf8, produk1);
        } else if (produk2.contains("Galaxy") || produk2.contains("19SLV411U") || produk2.contains("Pentax")) {
            gsfcsf2 = proc.Bandingkan(gsf9, produk1);
        } else if (produk2.contains("Nexus") || produk2.contains("Vizio") || produk2.contains("DSC-HX200V")) {
            gsfcsf2 = proc.Bandingkan(gsf10, produk1);
        }

        proc.crossCheck(gsfcsf1, gsfcsf2);
        Map<String, nilai_gsf_csf> katgsfcsf1 = proc.GabungKategori(gsfcsf1);
        Map<String, nilai_gsf_csf> katgsfcsf2 = proc.GabungKategori(gsfcsf2);
        
        int j = 4;
        for (Map.Entry<String, nilai_gsf_csf> entry : katgsfcsf1.entrySet()) {
            String key = entry.getKey();
            nilai_gsf_csf value = entry.getValue();
            Label lblFitur = new Label(0, j, key);
            Label lblGSF = new Label(1, j, value.gsf.toString());
            Label lblCSF = new Label(2, j, value.csf.toString());
            sheet.addCell(lblFitur);
            sheet.addCell(lblGSF);
            sheet.addCell(lblCSF);
            j++;
        }

        int k = 4;
        for (Map.Entry<String, nilai_gsf_csf> entry : katgsfcsf2.entrySet()) {
            String key = entry.getKey();
            nilai_gsf_csf value = entry.getValue();
            Label lblFitur = new Label(4, k, key);
            Label lblGSF = new Label(5, k, value.gsf.toString());
            Label lblCSF = new Label(6, k, value.csf.toString());
            sheet.addCell(lblFitur);
            sheet.addCell(lblGSF);
            sheet.addCell(lblCSF);
            k++;
        }

//=== Proses Normalisasi Dan Penggabungan Skor Fitur Produk ====================
//====== Normalisasi Skor GSF ==================================================
        proc.hitungMaxMin(katgsfcsf1);
        katgsfcsf1 = proc.normalisasiGSF(katgsfcsf1);
        proc.hitungMaxMin(katgsfcsf2);
        katgsfcsf2 = proc.normalisasiGSF(katgsfcsf2);
//====== Normalisasi Skor CSF ==================================================
        Map<String, Double> norm1 = proc.normalisasi(katgsfcsf1);
        Map<String, Double> norm2 = proc.normalisasi(katgsfcsf2);
//====== Proses Perbandingan Skor Kedua Fitur ==================================
        List<hasil_compare> lhc = proc.BandingkanAkhir(norm1, norm2);
//=============================== End ==========================================

        int t = Math.max(j, k) + 3;
        for (int v = 0; v < lhc.size(); v++) {
            Label lblF = new Label(0, t, lhc.get(v).getKategory());
            Label lblS1 = new Label(1, t, "" + lhc.get(v).getProduk1());
            Label lblS2 = new Label(2, t, "" + lhc.get(v).getProduk2());
            sheet.addCell(lblF);
            sheet.addCell(lblS1);
            sheet.addCell(lblS2);
            t++;
        }

        workbook.write();
        workbook.close();

        double skor1 = 0.0, skor2 = 0.0, avgSkor1 = 0.0, avgSkor2 = 0.0;
//===Metode Hitung Rekomendasi Dg Melihat Jumlah Fitur Yang Unggul==============
//======Jika Semua fitur diasumsikan memiliki bobot yang sama===================
//        for (int i = 0; i < lhc.size(); i++) {
//            if (lhc.get(i).getProduk1() > lhc.get(i).getProduk2()) {
//                skor1++;
//            } else {
//                skor2++;
//            }
//        }
//======Jika fitur2 yang relevan diberi bobot yang lebih besar==================
        for (int x = 0; x < lhc.size(); x++) {
            double skor = 0;
            for (int y = 0; y < relevan.size(); y++) {
                if (lhc.get(x).getKategory().contains(relevan.get(y))) {
                    skor = sv.bobotRelevansi.get(lhc.get(x).getKategory());
                }
            }
            if (lhc.get(x).getProduk1() > lhc.get(x).getProduk2()) {
                skor1 = skor1 + skor;
            } else if (lhc.get(x).getProduk1() < lhc.get(x).getProduk2()){
                skor2 = skor2 + skor;
            }
        }
//        System.out.println("Produk1 = "+produk1);
//        System.out.println("Produk2 = "+produk2);
//        System.out.println("Skor1 = "+skor1);
//        System.out.println("Skor2 = "+skor2);
////======Tampilkan Hasil Perbandingan Skor Rekomendasi===========================
        if (skor1 < skor2) {
            System.out.println("2");
        } else if (skor1 > skor2) {
            System.out.println("1");
        } else {
            System.out.println("0");
        }
        //System.out.println("Skor 1 = "+skor1+"  ||  Skor 2 = "+skor2);
//============================== End ===========================================

//===Metode Hitung Rekomendasi Produk Berdasarkan Nilai Average Semua Fitur=====
//======Jika Semua fitur diasumsikan memiliki bobot yang sama===================
//        for (int i = 0; i < lhc.size(); i++) {
//            skor1 = skor1 + lhc.get(i).getProduk1();
//            skor2 = skor2 + lhc.get(i).getProduk2();
//        }
//        avgSkor1 = skor1 / lhc.size();
//        avgSkor2 = skor2 / lhc.size();
//======Jika fitur "phone" punya bobot lebih tinggi=============================
//        double bf = 0.5;
//        double f1 = 0.0, f2 = 0.0;
//        for (int i = 0; i < lhc.size(); i++) {
//            if (lhc.get(i).getKategory().equalsIgnoreCase("phone")) {
//                f1 = lhc.get(i).getProduk1();
//                f2 = lhc.get(i).getProduk2();
//            } else {
//                skor1 = skor1 + lhc.get(i).getProduk1();
//                skor2 = skor2 + lhc.get(i).getProduk2();
//            }
//        }
//        avgSkor1 = (bf*f1)+((1-bf)*(skor1/(lhc.size()-1)));
//        avgSkor2 = (bf*f2)+((1-bf)*(skor2/(lhc.size()-1)));
//======Tampilkan Hasil Perbandingan Skor Rekomendasi===========================
//        if (avgSkor1 < avgSkor2) {
//            System.out.println("2");
//        } else if (avgSkor1 > avgSkor2) {
//            System.out.println("1");
//        } else {
//            System.out.println("0");
//        }
//============================== End ===========================================
    }

    public void Uji(int t) throws IOException, WriteException {
            domain = "phone";
            proc.inisialisasiDomain(domain);

            Double alpha = 0.2;
            int threshold = t;
            RunData(alpha, threshold);

//            System.out.println("=================================amod=======================================");
//            for(int i=0;i<sv.fiturAmod.size();i++){
//                System.out.println(sv.fiturAmod.get(i));
//            }
//            System.out.println("================================nsubj=======================================");
//            for(int i=0;i<sv.fiturNsubj.size();i++){
//                System.out.println(sv.fiturNsubj.get(i));
//            }
//            System.out.println("============================conj_and(noun)==================================");
//            for(int i=0;i<sv.fiturConjAndNoun.size();i++){
//                System.out.println(sv.fiturConjAndNoun.get(i));
//            }
//            System.out.println("==============================prep_with=====================================");
//            for(int i=0;i<sv.fiturPrep.size();i++){
//                System.out.println(sv.fiturPrep.get(i));
//            }
//            System.out.println("==================================det=======================================");
//            for(int i=0;i<sv.fiturDet.size();i++){
//                System.out.println(sv.fiturDet.get(i));
//            }

            // <PRODUK phone> //
//            String produk1[] = {"Google Nexus", "Apple Iphone 3GS", "Blackberry Curve 9360", "Blackberry Torch 9380", "HTC Inspire", "LG Optimus L7", "Motorola Atrix MB860", "Nokia Lumia 710", "Samsung Galaxy Ace Plus", "Sony Xperia U."};
//            String produk2[] = {"Google Nexus", "Apple Iphone 3GS", "Blackberry Curve 9360", "Blackberry Torch 9380", "HTC Inspire", "LG Optimus L7", "Motorola Atrix MB860", "Nokia Lumia 710", "Samsung Galaxy Ace Plus", "Sony Xperia U."};

            // <PRODUK CAMERA> //
//        String produk1[] = {"Nikon D7100", "Olympus E-PL2", "Sony Cyber-shot DSC-HX200V", "Panasonic Lumix DMC-ZS8", "Canon EOS Rebel T3i", "Kodak EasyShare Max Z990", "Pentax K20D", "Fujifilm FinePix S2950", "GE Power Pro X500-WH", "Nikon Coolpix P90"};
//        String produk2[] = {"Nikon D7100", "Olympus E-PL2", "Sony Cyber-shot DSC-HX200V", "Panasonic Lumix DMC-ZS8", "Canon EOS Rebel T3i", "Kodak EasyShare Max Z990", "Pentax K20D", "Fujifilm FinePix S2950", "GE Power Pro X500-WH", "Nikon Coolpix P90"};

            // <PRODUK TV> //
//        String produk1[] = {"Panasonic VIERA TC-P55VT50", "Vizio E3D420VX", "TCL L40FHDP60", "Samsung UN46EH5300", "Sony BRAVIA KDL40EX640", "Sharp LC-60LE633U", "LG 42LK450", "Samsung UN40ES6100", "Sony BRAVIA KDL46BX450", "Toshiba 19SLV411U"};
//        String produk2[] = {"Panasonic VIERA TC-P55VT50", "Vizio E3D420VX", "TCL L40FHDP60", "Samsung UN46EH5300", "Sony BRAVIA KDL40EX640", "Sharp LC-60LE633U", "LG 42LK450", "Samsung UN40ES6100", "Sony BRAVIA KDL46BX450", "Toshiba 19SLV411U"};

//            System.out.println("");
//            System.out.println("Threshold = "+t);

//            for (int i = 0; i < 10; i++) {
//                for (int j = i + 1; j < 10; j++) {
//                    RunDataCSF(produk1[i], produk2[j]);
//                }
//            }
//        RunDataCSF("Apple Iphone 3GS","Google Nexus");
//        RunDataCSF("Google Nexus","HTC Inspire");
//        System.out.println("======");
//        RunDataCSF("HTC Inspire", "Google Nexus");
        
    }

    public static void main(String[] args) throws IOException, WriteException {
        int t = 80;
        new UjiCoba1().Uji(t);
    }
}
