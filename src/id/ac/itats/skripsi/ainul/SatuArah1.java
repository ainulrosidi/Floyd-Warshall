package id.ac.itats.skripsi.ainul;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table Satu_Arah1.
 */
public class SatuArah1 {

    private Long idSatuArah;
    private String satuArah;
    private String idNodePeta;
    private String latitude;
    private String longitude;
    private String namaJalan;

    public SatuArah1() {
    }

    public SatuArah1(Long idSatuArah) {
        this.idSatuArah = idSatuArah;
    }

    public SatuArah1(Long idSatuArah, String satuArah, String idNodePeta, String latitude, String longitude, String namaJalan) {
        this.idSatuArah = idSatuArah;
        this.satuArah = satuArah;
        this.idNodePeta = idNodePeta;
        this.latitude = latitude;
        this.longitude = longitude;
        this.namaJalan = namaJalan;
    }

    public Long getIdSatuArah() {
        return idSatuArah;
    }

    public void setIdSatuArah(Long idSatuArah) {
        this.idSatuArah = idSatuArah;
    }

    public String getSatuArah() {
        return satuArah;
    }

    public void setSatuArah(String satuArah) {
        this.satuArah = satuArah;
    }

    public String getIdNodePeta() {
        return idNodePeta;
    }

    public void setIdNodePeta(String idNodePeta) {
        this.idNodePeta = idNodePeta;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getNamaJalan() {
        return namaJalan;
    }

    public void setNamaJalan(String namaJalan) {
        this.namaJalan = namaJalan;
    }

}
