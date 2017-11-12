/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package testing;

public class hasil_compare {
    String kategory;
    double produk1;
    double produk2;

    public String getKategory() {
        return kategory;
    }

    public double getProduk1() {
        return produk1;
    }

    public double getProduk2() {
        return produk2;
    }



    
    public hasil_compare(String kategory, double produk1, double produk2) {
        this.kategory = kategory;
        this.produk1 = produk1;
        this.produk2 = produk2;
    }

    @Override
    public String toString() {
        return kategory+" | "+produk1+" | "+produk2;
    }


}

