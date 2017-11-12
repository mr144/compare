/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package testing;

public class FiturProduk {
    public String nama;
    public String nomerDokumen;
    public String path;
    public int index;

    public FiturProduk(){

    }
    
    public FiturProduk(String nama, String nomerDokumen,String path,int index) {

         this.nama = nama;
         for (KataNN object : StaticVariable.lnn) {
          
        String hasil= object.getKata(nama, nomerDokumen, path, index);
            if(!hasil.equalsIgnoreCase(nama)){
                this.nama=hasil;
              
                break;
            }
        }
     //   
        this.nomerDokumen = nomerDokumen;
        this.path=path;
        this.index=index;
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
        final FiturProduk other = (FiturProduk) obj;
        if ((this.nama == null) ? (other.nama != null) : !this.nama.equals(other.nama)) {
            return false;
        }
        if ((this.nomerDokumen == null) ? (other.nomerDokumen != null) : !this.nomerDokumen.equals(other.nomerDokumen)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return this.nama+" dokuemen ke : "+nomerDokumen +" index ke "+index+" Path="+path; //To change body of generated methods, choose Tools | Templates.
    }
    
    
 

 
    
    
    
}
