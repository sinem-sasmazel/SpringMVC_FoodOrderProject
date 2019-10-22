
package restModel;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Product {

    @SerializedName("durum")
    @Expose
    private Boolean durum;
    @SerializedName("mesaj")
    @Expose
    private String mesaj;
    @SerializedName("bilgiler")
    @Expose
    private List<model.Product> bilgiler = null;

    public Boolean getDurum() {
        return durum;
    }

    public void setDurum(Boolean durum) {
        this.durum = durum;
    }

    public String getMesaj() {
        return mesaj;
    }

    public void setMesaj(String mesaj) {
        this.mesaj = mesaj;
    }

    public List<model.Product> getBilgiler() {
        return bilgiler;
    }

    public void setBilgiler(List<model.Product> bilgiler) {
        this.bilgiler = bilgiler;
    }

}
