/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package testing;

public class SvSn {
    String nama;
    String dokumen;

    public SvSn(String nama, String dokumen) {
        this.nama = nama;
        this.dokumen = dokumen;
    }

    
    @Override
    public int hashCode() {
        int hash = 7;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SvSn other = (SvSn) obj;
        if ((this.nama == null) ? (other.nama != null) : !this.nama.equals(other.nama)) {
            return false;
        }
        if ((this.dokumen == null) ? (other.dokumen != null) : !this.dokumen.equals(other.dokumen)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return nama+" dokumen ke "+dokumen; //To change body of generated methods, choose Tools | Templates.
    }
  
   
    
    
}
