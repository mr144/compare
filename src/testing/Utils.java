/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package testing;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Utils {

    public static enum TypeKalimat {

        Comparative, Superlative
    };

    public static void readStopword() {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader("data/stopword.txt"));
            String line;
            while ((line = br.readLine()) != null) {
                StaticVariable.stopword.add(line);
            }
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
    }

    public static void readKamus(String domain) {
        BufferedReader br = null;
        try {
            if (domain.equalsIgnoreCase("phone")) {
                br = new BufferedReader(new FileReader("data/kamus-phone.txt"));
            } else if (domain.equalsIgnoreCase("tv")) {
                br = new BufferedReader(new FileReader("data/kamus-tv.txt"));
            } else if (domain.equalsIgnoreCase("camera")) {
                br = new BufferedReader(new FileReader("data/kamus-camera.txt"));
            }
            String line;
            while ((line = br.readLine()) != null) {
                StaticVariable.kamus.add(line);
            }
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
    }

    public void readTagged(String path, String nomer) {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(path + "/" + nomer + "-tagged.txt"));
            String line;
            while ((line = br.readLine()) != null) {
                String kata2[] = line.split(" ");
                for (String kata : kata2) {
                    String type_kata[] = kata.split("_");
                    if (type_kata[1].startsWith("NN")) {
                        if (!StaticVariable.noun.contains(type_kata[0].trim().toLowerCase())) {
                            StaticVariable.noun.add(type_kata[0].trim().toLowerCase());
                        }
                    } else if (type_kata[1].startsWith("JJ")) {
                        if (!StaticVariable.adj.contains(type_kata[0].trim().toLowerCase())) {
                            StaticVariable.adj.add(type_kata[0].trim().toLowerCase());
                        }
                    }
                }
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
    }

    public List<ParsedModel> readParsed(String path, String nomer) {
        List<ParsedModel> ls = new ArrayList<ParsedModel>();

        BufferedReader br = null;
        int index = 0;
        try {
            br = new BufferedReader(new FileReader(path + "/" + nomer + "-parsed.txt"));
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("===")) {
                    index++;
                }

                if (line.indexOf("(") > 0) {
                    StaticVariable.tagkata.add(new Tagged(line, nomer, index));
                    ParsedModel parsedModel = new ParsedModel();
                    parsedModel.kata = line;
                    parsedModel.nomer = nomer;
                    parsedModel.index = index;
                    parsedModel.path = path + "/" + nomer + ".txt";
                    ls.add(parsedModel);
                }

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

        return ls;
    }

    public void readLama(String path, String lama, String baru) {


        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(path + "/" + lama));
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("(ROOT")) {
                    writeToFile(path, "=========", baru);
                } else if (!line.startsWith(" ")) {
                    //System.out.println(line);
                    writeToFile(path, line, baru);
                }
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
    }

    public static String readDokumenString(String path, int kalimatKe) {
        String hasil = "";

        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(path));
            String line;
            int index = 1;
            while ((line = br.readLine()) != null) {
                if (index == kalimatKe) {
                    hasil = line;
                    break;
                }
                index++;
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
        return hasil;
    }

    public void writeToFile(String path, String text, String baru) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(new File(
                    path + "/" + baru), true));
            bw.write(text);
            bw.newLine();
            bw.close();
        } catch (Exception e) {
        }
    }

    public static String removeMinus(String data) {
        return data.split("-")[0];
    }

    public void readJUmlahKalimat(String path, String nomer) {
        BufferedReader br = null;
        int kalimatKe = 0;
        List<Integer> kalsudah = new ArrayList<Integer>();
        try {
            br = new BufferedReader(new FileReader(path + "/" + nomer + "-parsed.txt"));
            String line;
            while ((line = br.readLine()) != null) {
                if (line.contains("=")) {
                    StaticVariable.jumlahKalmat++;
                    kalimatKe++;
                }



                if (line.startsWith("advmod") && !kalsudah.contains(kalimatKe)) {
                    StaticVariable.jumlahKalmatMengandungLexicon++;

                    kalsudah.add(kalimatKe);

                }
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


    }

    public int ChekKalimatConstainOr(String path, String nomor, String... contains) {
        BufferedReader br = null;
        int jumlah = 0;
        String kalimat = "";

        try {
            br = new BufferedReader(new FileReader(path + "/" + nomor + "-tagged.txt"));
            String line;
            int index = 1;
            while ((line = br.readLine()) != null) {
                for (String string : contains) {
                    if (line.toLowerCase().contains(string) && kalimat != line) {
                        kalimat = line;
                        if (string.equalsIgnoreCase("jjr") || string.equalsIgnoreCase("more")) {
                            StaticVariable.kalimatMengandungJJR.add(new Kalimat(line, nomor, index));

                        } else if (string.equalsIgnoreCase("jjs") || string.equalsIgnoreCase("most")) {
                            StaticVariable.kalimatMengandungJJS.add(new Kalimat(line, nomor, index));
                        }
                        jumlah++;
                    }
                }
                index++;
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
        return jumlah;

    }

    public int ChekKalimatConstain(String path, String nomor, String kata1, String kata2) {
        BufferedReader br = null;
        int jumlah = 0;
        String kalimat = "";
        try {
            br = new BufferedReader(new FileReader(path + "/" + nomor + ".txt"));
            String line;
            while ((line = br.readLine()) != null) {


                if (line.toLowerCase().contains(kata1.toLowerCase()) && line.toLowerCase().contains(kata2.toLowerCase())) {
                    kalimat = line;
                    jumlah++;
                }
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
        return jumlah;

    }

    public Map<String, String[]> readKategory(String p, String... phone) {
        String path = p;
        BufferedReader br = null;
        int jumlah = 0;
        String kalimat = "";
        Map<String, String[]> kategory = new HashMap<String, String[]>();
        kategory.put("phone", phone);
        try {
            br = new BufferedReader(new FileReader(path));
            String line;
            while ((line = br.readLine()) != null) {
                String kat[] = line.split("=");
                String isiKategory[] = kat[0].split(",");
                if (kategory.containsKey(kat[1])) {

                    String[] baru = concatArray(kategory.get(kat[1]), isiKategory);
                    kategory.put(kat[1], baru);
                } else {
                    kategory.put(kat[1], isiKategory);
                }
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
        return kategory;

    }

    public static <T> T[] concatArray(T[] first, T[] second) {
        T[] result = Arrays.copyOf(first, first.length + second.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
    }
}
