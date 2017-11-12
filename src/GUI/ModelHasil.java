/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import testing.hasil_compare;

public class ModelHasil extends AbstractTableModel {

    List<hasil_compare> lhc = new ArrayList<hasil_compare>();

    public ModelHasil(List<hasil_compare> lhc) {
        this.lhc = lhc;
    }

    @Override
    public int getRowCount() {
        return lhc.size();
    }

    @Override
    public int getColumnCount() {
        return 3;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case 0:
                return lhc.get(rowIndex).getKategory();
            case 1:
                return lhc.get(rowIndex).getProduk1();
            case 2:
                return lhc.get(rowIndex).getProduk2();
            default:
                return "";
        }
    }

    @Override
    public String getColumnName(int column) {
        switch (column) {
            case 0:
                return "Fitur";
            case 1:
                return "Produk 1";
            case 2:
                return "Produk 2";
            default:
                return "";
        }
    }


}
