/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.b.kelompok_8;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author T.U.F GAMING
 */
@Entity
@Table(name = "detailt")
@NamedQueries({
    @NamedQuery(name = "Detailt.findAll", query = "SELECT d FROM Detailt d"),
    @NamedQuery(name = "Detailt.findByIddetailT", query = "SELECT d FROM Detailt d WHERE d.iddetailT = :iddetailT"),
    @NamedQuery(name = "Detailt.findByKotaAsal", query = "SELECT d FROM Detailt d WHERE d.kotaAsal = :kotaAsal"),
    @NamedQuery(name = "Detailt.findByKotaTujuan", query = "SELECT d FROM Detailt d WHERE d.kotaTujuan = :kotaTujuan"),
    @NamedQuery(name = "Detailt.findByKelas", query = "SELECT d FROM Detailt d WHERE d.kelas = :kelas"),
    @NamedQuery(name = "Detailt.findByWaktuTempuh", query = "SELECT d FROM Detailt d WHERE d.waktuTempuh = :waktuTempuh"),
    @NamedQuery(name = "Detailt.findByHarga", query = "SELECT d FROM Detailt d WHERE d.harga = :harga"),
    @NamedQuery(name = "Detailt.findByTanggal", query = "SELECT d FROM Detailt d WHERE d.tanggal = :tanggal")})
public class Detailt implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_detailT")
    private Integer iddetailT;
    @Basic(optional = false)
    @Column(name = "kota_asal")
    private String kotaAsal;
    @Basic(optional = false)
    @Column(name = "kota_tujuan")
    private String kotaTujuan;
    @Basic(optional = false)
    @Column(name = "kelas")
    private String kelas;
    @Basic(optional = false)
    @Column(name = "waktu_tempuh")
    @Temporal(TemporalType.TIME)
    private Date waktuTempuh;
    @Basic(optional = false)
    @Column(name = "harga")
    private double harga;
    @Basic(optional = false)
    @Column(name = "tanggal")
    @Temporal(TemporalType.DATE)
    private Date tanggal;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "iddetailT")
    private Collection<Tiket> tiketCollection;

    public Detailt() {
    }

    public Detailt(Integer iddetailT) {
        this.iddetailT = iddetailT;
    }

    public Detailt(Integer iddetailT, String kotaAsal, String kotaTujuan, String kelas, Date waktuTempuh, double harga, Date tanggal) {
        this.iddetailT = iddetailT;
        this.kotaAsal = kotaAsal;
        this.kotaTujuan = kotaTujuan;
        this.kelas = kelas;
        this.waktuTempuh = waktuTempuh;
        this.harga = harga;
        this.tanggal = tanggal;
    }

    public Integer getIddetailT() {
        return iddetailT;
    }

    public void setIddetailT(Integer iddetailT) {
        this.iddetailT = iddetailT;
    }

    public String getKotaAsal() {
        return kotaAsal;
    }

    public void setKotaAsal(String kotaAsal) {
        this.kotaAsal = kotaAsal;
    }

    public String getKotaTujuan() {
        return kotaTujuan;
    }

    public void setKotaTujuan(String kotaTujuan) {
        this.kotaTujuan = kotaTujuan;
    }

    public String getKelas() {
        return kelas;
    }

    public void setKelas(String kelas) {
        this.kelas = kelas;
    }

    public Date getWaktuTempuh() {
        return waktuTempuh;
    }

    public void setWaktuTempuh(Date waktuTempuh) {
        this.waktuTempuh = waktuTempuh;
    }

    public double getHarga() {
        return harga;
    }

    public void setHarga(double harga) {
        this.harga = harga;
    }

    public Date getTanggal() {
        return tanggal;
    }

    public void setTanggal(Date tanggal) {
        this.tanggal = tanggal;
    }

    public Collection<Tiket> getTiketCollection() {
        return tiketCollection;
    }

    public void setTiketCollection(Collection<Tiket> tiketCollection) {
        this.tiketCollection = tiketCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (iddetailT != null ? iddetailT.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Detailt)) {
            return false;
        }
        Detailt other = (Detailt) object;
        if ((this.iddetailT == null && other.iddetailT != null) || (this.iddetailT != null && !this.iddetailT.equals(other.iddetailT))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ws.b.kelompok_8.Detailt[ iddetailT=" + iddetailT + " ]";
    }
    
}
