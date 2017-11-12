/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package testing;

public class nilai_gsf_csf {

    public Double gsf;
    public Double csf;

    public nilai_gsf_csf(double gsf, double csf) {

        this.gsf = gsf;
        this.csf = csf;
    }

    @Override
    public String toString() {
        return gsf + " " + csf;
    }
}
