/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package testing;

import java.util.Map;

public class Rule {

    boolean bolehS = true;
    boolean AdaProsess = true;
    int indexKataNN = 0;

    public void chekSnSv(ParsedModel model) {
        String data = model.kata;
        data = data.replace("(", ", ");
        data = data.replace(")", "");
        Boolean prosess = false;


        String pattern[] = data.split(",");
        if (pattern.length < 3) {
            return;
        }
        String type = pattern[0];
        String kataPertama = removeMinus(pattern[1]).trim().toLowerCase();
        String kataKedua = removeMinus(pattern[2]).trim().toLowerCase();


        SvSn svSn = new SvSn(kataPertama, model.nomer);
        if (type.equalsIgnoreCase("advmod")) {
            if (StaticVariable.svsn.containsKey(svSn)) {
                double[] nilaiSvsn = StaticVariable.svsn.get(svSn);

                nilaiSvsn[0] = nilaiSvsn[0] + 0.5;
                StaticVariable.svsn.put(svSn, nilaiSvsn);
            } else {
                double[] nilaiSvsn = new double[2];
                nilaiSvsn[0] = 0.5;
                nilaiSvsn[1] = 1;
                StaticVariable.svsn.put(svSn, nilaiSvsn);
            }
        } else if (type.equalsIgnoreCase("neg")) {
            if (StaticVariable.svsn.containsKey(svSn)) {
                double[] nilaiSvsn = StaticVariable.svsn.get(svSn);
                nilaiSvsn[1] = -1;
                StaticVariable.svsn.put(svSn, nilaiSvsn);
            } else {
                double[] nilaiSvsn = new double[2];
                nilaiSvsn[0] = 0;
                nilaiSvsn[1] = -1;
                StaticVariable.svsn.put(svSn, nilaiSvsn);
            }
        }

    }

    public void chek(ParsedModel model, Map<FiturProduk, Double> fitur_produk) {
        String data = model.kata;
        data = data.replace("(", ", ");
        data = data.replace(")", "");
        Boolean prosess = false;

        String pattern[] = data.split(",");
        String type = pattern[0];
        if (pattern.length < 3) {
            return;
        }
        String kataPertama = removeMinus(pattern[1]).trim().toLowerCase();
        String kataKedua = removeMinus(pattern[2]).trim().toLowerCase();

        if (type.equalsIgnoreCase("nn")) {
            if (model.flag) {
                return;
            }

            String kataNN = kataKedua + " " + kataPertama;
            indexKataNN = model.index;
            prosess = true;
            model.flag = true;
            StaticVariable.lnn.add(new KataNN(kataNN, model.nomer, model.path, model.index));

        }


        if (type.equalsIgnoreCase("amod") && prosess == false) {
            if (model.flag) {
                return;
            }
            ReturnData returnData = chekNounAdj(kataPertama, kataKedua);
            if (returnData.hasil) {
                kataPertama = returnData.kata1;
                kataKedua = returnData.kata2;

                FiturProduk fiturProduk = new FiturProduk(kataPertama, model.nomer,
                        model.path, model.index);
                if (fitur_produk.containsKey(fiturProduk)) {
                    if (!StaticVariable.lexicon.containsKey(kataKedua)) {
                        double nilaidef = polaritasProduk(fitur_produk.get(fiturProduk));
                        StaticVariable.lexicon.put(kataKedua, nilaidef);
                        double nilaisebelumnya = fitur_produk.get(fiturProduk);
                        fitur_produk.put(fiturProduk, nilaisebelumnya
                                + polaritasProdukSvSn(kataKedua, model.nomer));
                        addJmlFitur(fiturProduk.nama);
                        prosess = true;
                        model.flag = true;
                    }
                } else if (StaticVariable.lexicon.containsKey(kataKedua)) {
                    if (!fitur_produk.containsKey(fiturProduk)) {
                        fitur_produk.put(fiturProduk, polaritasProdukSvSn(kataKedua,
                                model.nomer));
                        addJmlFitur(fiturProduk.nama);
                        prosess = true;
                        model.flag = true;
                    } else {
                        double nilaisebelumnya = fitur_produk.get(fiturProduk);
                        fitur_produk.put(fiturProduk, nilaisebelumnya
                                + polaritasProdukSvSn(kataKedua, model.nomer));
                        addJmlFitur(fiturProduk.nama);
                        prosess = true;
                        model.flag = true;
                    }
                    //==================================================
                    StaticVariable.ruleAmod++;
                    if (!StaticVariable.fiturAmod.contains(kataPertama)) {
                        StaticVariable.fiturAmod.add(kataPertama);
                    }
                    //==================================================
                }
            }
        }

        if ((type.equalsIgnoreCase("nsubj") || type.equalsIgnoreCase("prep_with")) && prosess == false) {
            if (model.flag) {
                return;
            }
            ReturnData returnData = chekAdjNoun(kataPertama, kataKedua);
            if (returnData.hasil) {
                kataPertama = returnData.kata1;
                kataKedua = returnData.kata2;

                if (kataKedua.equalsIgnoreCase("i")) {
                } else {
                    FiturProduk fiturProduk = new FiturProduk(kataKedua, model.nomer,
                            model.path, model.index);
                    if (fitur_produk.containsKey(fiturProduk)) {
                        if (!StaticVariable.lexicon.containsKey(kataPertama)) {
                            double nilaidef = polaritasProduk(fitur_produk.get(fiturProduk));
                            StaticVariable.lexicon.put(kataPertama, nilaidef);
                            double nilaisebelumnya = fitur_produk.get(fiturProduk);
                            fitur_produk.put(fiturProduk, nilaisebelumnya
                                    + polaritasProdukSvSn(kataPertama,
                                    model.nomer));
                            addJmlFitur(fiturProduk.nama);
                            prosess = true;
                            model.flag = true;
                        }
                    } else if (StaticVariable.lexicon.containsKey(kataPertama)) {
                        if (!fitur_produk.containsKey(fiturProduk)) {
                            fitur_produk.put(fiturProduk, polaritasProdukSvSn(kataPertama,
                                    model.nomer));
                            addJmlFitur(fiturProduk.nama);
                            prosess = true;
                            model.flag = true;
                        } else {
                            double nilaisebelumnya = fitur_produk.get(fiturProduk);
                            fitur_produk.put(fiturProduk, nilaisebelumnya
                                    + polaritasProdukSvSn(kataPertama,
                                    model.nomer));
                            addJmlFitur(fiturProduk.nama);
                            prosess = true;
                            model.flag = true;
                        }
                        //======================================================
                        if (type.equalsIgnoreCase("nsubj")) {
                            StaticVariable.ruleNsubj++;
                            if (!StaticVariable.fiturNsubj.contains(kataKedua)) {
                                StaticVariable.fiturNsubj.add(kataKedua);
                            }
                        } else if (type.equalsIgnoreCase("prep_with")) {
                            StaticVariable.rulePrepWith++;
                            if (!StaticVariable.fiturPrep.contains(kataKedua)) {
                                StaticVariable.fiturPrep.add(kataKedua);
                            }
                        }
                        //======================================================
                    }
                }
            }
        }
        if (type.equalsIgnoreCase("conj_and") && prosess == false) {
            if (model.flag) {
                return;
            }
            ReturnData returnData = chekAdjAdj(kataPertama, kataKedua);
            if (returnData.hasil) {
                StaticVariable.ruleConjAndAdj++;
                kataPertama = returnData.kata1;
                kataKedua = returnData.kata2;
                if (StaticVariable.lexicon.containsKey(kataPertama)) {
                    if (!StaticVariable.lexicon.containsKey(kataKedua)) {
                        StaticVariable.lexicon.put(kataKedua,
                                StaticVariable.lexicon.get(kataPertama));
                        StaticVariable.parsedFilter.add(model);
                        prosess = true;
                        model.flag = true;
                    }
                } else if (StaticVariable.lexicon.containsKey(kataKedua)) {
                    if (!StaticVariable.lexicon.containsKey(kataPertama)) {
                        StaticVariable.lexicon.put(kataPertama,
                                StaticVariable.lexicon.get(kataKedua));
                        StaticVariable.parsedFilter.add(model);
                        prosess = true;
                        model.flag = true;
                    }
                }
            }
        }

        if (type.equalsIgnoreCase("conj_and") && prosess == false) {
            if (model.flag) {
                return;
            }
            ReturnData returnData = chekNounNoun(kataPertama, kataKedua);
            if (returnData.hasil) {
                kataPertama = returnData.kata1;
                kataKedua = returnData.kata2;

                FiturProduk fiturProduk1 = new FiturProduk(kataPertama, model.nomer, model.path, model.index);
                FiturProduk fiturProduk2 = new FiturProduk(kataKedua, model.nomer, model.path, model.index);
                if (fitur_produk.containsKey(fiturProduk1)) {
                    if (!fitur_produk.containsKey(fiturProduk2)) {
                        double nilaidef = polaritasProduk(fitur_produk.get(fiturProduk1));
                        fitur_produk.put(fiturProduk2, nilaidef);
                        addJmlFitur(fiturProduk2.nama);
                        prosess = true;
                        model.flag = true;
                    } else {
                        double nilaidef = polaritasProduk(fitur_produk.get(fiturProduk1));
                        double nilaisebelumnya = fitur_produk.get(fiturProduk2);
                        fitur_produk.put(fiturProduk2, nilaisebelumnya + nilaidef);
                        addJmlFitur(fiturProduk2.nama);
                        prosess = true;
                        model.flag = true;
                    }
                    //================================================
                    StaticVariable.ruleConjAndNoun++;
                    if (!StaticVariable.fiturConjAndNoun.contains(kataPertama)) {
                        StaticVariable.fiturConjAndNoun.add(kataPertama);
                    }
                    //================================================
                } else if (fitur_produk.containsKey(fiturProduk2)) {
                    if (!fitur_produk.containsKey(fiturProduk1)) {
                        double nilaidef = polaritasProduk(fitur_produk.get(fiturProduk2));
                        fitur_produk.put(fiturProduk1, nilaidef);
                        addJmlFitur(fiturProduk1.nama);
                        prosess = true;
                        model.flag = true;
                    } else {
                        double nilaidef = polaritasProduk(fitur_produk.get(fiturProduk2));
                        double nilaisebelumnya = fitur_produk.get(fiturProduk1);
                        fitur_produk.put(fiturProduk1, nilaisebelumnya + nilaidef);
                        addJmlFitur(fiturProduk1.nama);
                        prosess = true;
                        model.flag = true;
                    }
                    //================================================
                    StaticVariable.ruleConjAndNoun++;
                    if (!StaticVariable.fiturConjAndNoun.contains(kataKedua)) {
                        StaticVariable.fiturConjAndNoun.add(kataKedua);
                    }
                    //================================================
                }
            }
        }

        if (type.equalsIgnoreCase("det") && prosess == false) {
            if (model.flag) {
                return;
            }

            ReturnData returnData = chekNoNoun(kataPertama, kataKedua);

            if (returnData != null && returnData.hasil) {

                kataPertama = returnData.kata1;
                kataKedua = returnData.kata2;
                FiturProduk fiturProduk = new FiturProduk(kataPertama, model.nomer,
                        model.path, model.index);
                if (!fitur_produk.containsKey(fiturProduk)) {
                    if (kataPertama.equalsIgnoreCase("problem") || kataPertama.equalsIgnoreCase("complaint")) {
                    } else {
                        fitur_produk.put(fiturProduk, -1.0);
                        addJmlFitur(fiturProduk.nama);
                    }
                    prosess = true;
                    model.flag = true;
                } else {
                    double nilaisebelumnya = fitur_produk.get(fiturProduk);
                    if (kataPertama.equalsIgnoreCase("problem") || kataPertama.equalsIgnoreCase("complaint")) {
                    } else {
                        fitur_produk.put(fiturProduk, nilaisebelumnya - 1.0);
                        addJmlFitur(fiturProduk.nama);
                    }
                    prosess = true;
                    model.flag = true;
                }
                //=======================================
                StaticVariable.ruleNo++;
                if (!StaticVariable.fiturDet.contains(kataPertama)) {
                    StaticVariable.fiturDet.add(kataPertama);
                }
                //=======================================
            }
        }

        AdaProsess = prosess;

    }

    public String removeMinus(String data) {
        String datapecah[] = data.split("-");
        String hasil = "";
        for (int i = 0; i < datapecah.length - 1; i++) {
            if (i > 0) {
                hasil += "-" + datapecah[i];
            } else {
                hasil += datapecah[i];
            }
        }

        return hasil;
    }

    public ReturnData chekNounAdj(String kata1, String kata2) {
        if (bolehS) {
            if (kata1.endsWith("s")) {
                kata1 = kata1.substring(0, kata1.length() - 1);
            }
            if (kata2.endsWith("s")) {
                kata2 = kata2.substring(0, kata2.length() - 1);
            }

            if (StaticVariable.noun.contains(kata1) && StaticVariable.adj.contains(kata2)) {
                return new ReturnData(true, kata1, kata2);
            }

        }
        if (StaticVariable.noun.contains(kata1) && StaticVariable.adj.contains(kata2)) {
            return new ReturnData(true, kata1, kata2);
        }
        return new ReturnData(false, kata1, kata2);
    }

    public ReturnData chekAdjNoun(String kata1, String kata2) {

        if (bolehS) {
            if (kata1.endsWith("s")) {
                kata1 = kata1.substring(0, kata1.length() - 1);
            }
            if (kata2.endsWith("s")) {
                kata2 = kata2.substring(0, kata2.length() - 1);
            }

            if (StaticVariable.adj.contains(kata1) && StaticVariable.noun.contains(kata2)) {
                return new ReturnData(true, kata1, kata2);
            }

        }

        if (StaticVariable.adj.contains(kata1) && StaticVariable.noun.contains(kata2)) {
            return new ReturnData(true, kata1, kata2);
        }

        return new ReturnData(false, kata1, kata2);
    }

    public ReturnData chekAdjAdj(String kata1, String kata2) {

        if (bolehS) {
            if (kata1.endsWith("s")) {
                kata1 = kata1.substring(0, kata1.length() - 1);
            }
            if (kata2.endsWith("s")) {
                kata2 = kata2.substring(0, kata2.length() - 1);
            }

            if (StaticVariable.adj.contains(kata1) && StaticVariable.adj.contains(kata2)) {
                return new ReturnData(true, kata1, kata2);
            }

        }

        if (StaticVariable.adj.contains(kata1) && StaticVariable.adj.contains(kata2)) {
            return new ReturnData(true, kata1, kata2);
        }
        return new ReturnData(false, kata1, kata2);
    }

    public ReturnData chekNounNoun(String kata1, String kata2) {
        if (bolehS) {
            if (kata1.endsWith("s")) {
                kata1 = kata1.substring(0, kata1.length() - 1);
            }
            if (kata2.endsWith("s")) {
                kata2 = kata2.substring(0, kata2.length() - 1);
            }

            if (StaticVariable.noun.contains(kata1) && StaticVariable.noun.contains(kata2)) {
                return new ReturnData(true, kata1, kata2);
            }

        }

        if (StaticVariable.noun.contains(kata1) && StaticVariable.noun.contains(kata2)) {
            return new ReturnData(true, kata1, kata2);
        }
        return new ReturnData(false, kata1, kata2);
    }

    public ReturnData chekNoNoun(String kata1, String kata2) {
        if (bolehS) {
            if (kata1.endsWith("s")) {
                kata1 = kata1.substring(0, kata1.length() - 1);
            }
            if (kata2.endsWith("s")) {
                kata2 = kata2.substring(0, kata2.length() - 1);
            }


            if (kata2.equalsIgnoreCase("no") && StaticVariable.noun.contains(kata1)) {
                return new ReturnData(true, kata1, kata2);
            } else {
                return null;
            }

        }

        if (kata1.equalsIgnoreCase("no") && StaticVariable.noun.contains(kata2)) {
            return new ReturnData(true, kata1, kata2);
        }

        if (kata2.equalsIgnoreCase("no") && StaticVariable.noun.contains(kata1)) {
            return new ReturnData(true, kata2, kata1);
        }


        return new ReturnData(false, kata1, kata2);
    }

    class ReturnData {

        public ReturnData(boolean hasil, String kata1, String kata2) {
            this.hasil = hasil;
            this.kata1 = kata1;
            this.kata2 = kata2;
        }
        boolean hasil;
        String kata1;
        String kata2;
    }

    public double polaritasProduk(double nilai) {
        double nilaidef = 0;
        if (nilai < 0) {
            nilaidef = -1;
        } else if (nilai > 0) {
            nilaidef = 1;
        }
        return nilaidef;

    }

    public void addJmlFitur(String kata) {
        if (StaticVariable.jml_komen_fitur.get(kata) == null) {
            StaticVariable.jml_komen_fitur.put(kata, 1);
        } else {
            int nilaiLama = StaticVariable.jml_komen_fitur.get(kata);
            StaticVariable.jml_komen_fitur.put(kata, nilaiLama + 1);
        }
    }

    public double polaritasProdukSvSn(String kata, String dokumen) {
        if (StaticVariable.lexicon.get(kata) == null) {
            return 0;
        }
        double pol_o = StaticVariable.lexicon.get(kata);
        SvSn svSn = new SvSn(kata, dokumen);

        if (StaticVariable.svsn.containsKey(svSn)) {
            double[] nilaiSvsn = StaticVariable.svsn.get(svSn);

            double nilaidef = (pol_o + (pol_o * nilaiSvsn[0])) * nilaiSvsn[1];

            return nilaidef;


        }


        return pol_o;

    }
}
