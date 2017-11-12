/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package testing;

public class KataNN {
    String kata="aa";
    String path;
    String nomer;
    int index;

    public KataNN(String kata, String nomer, String path, int index) {
        this.kata = kata;
        this.path = path;
        this.nomer = nomer;
        this.index = index;
    }

    public String getKata(String kataAsli,String nomer, String path, int index){
        if(this.kata.contains(kataAsli)&&this.nomer.equalsIgnoreCase(nomer)&&this.path.equalsIgnoreCase(path)&&this.index==index){
        return this.kata;
        }
        return kataAsli;
    }
}
