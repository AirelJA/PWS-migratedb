/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.b.kelompok_8;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author T.U.F GAMING
 */
@Entity
@Table(name = "tiket")
@NamedQueries({
    @NamedQuery(name = "Tiket.findAll", query = "SELECT t FROM Tiket t"),
    @NamedQuery(name = "Tiket.findByIdTiket", query = "SELECT t FROM Tiket t WHERE t.idTiket = :idTiket"),
    @NamedQuery(name = "Tiket.findByGerbong", query = "SELECT t FROM Tiket t WHERE t.gerbong = :gerbong"),
    @NamedQuery(name = "Tiket.findByWaktuAwal", query = "SELECT t FROM Tiket t WHERE t.waktuAwal = :waktuAwal"),
    @NamedQuery(name = "Tiket.findByWaktuAkhir", query = "SELECT t FROM Tiket t WHERE t.waktuAkhir = :waktuAkhir")})
public class Tiket implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_tiket")
    private Integer idTiket;
    @Basic(optional = false)
    @Column(name = "gerbong")
    private Character gerbong;
    @Basic(optional = false)
    @Column(name = "waktu_awal")
    @Temporal(TemporalType.TIME)
    private Date waktuAwal;
    @Basic(optional = false)
    @Column(name = "waktu_akhir")
    @Temporal(TemporalType.TIME)
    private Date waktuAkhir;
    @JoinColumn(name = "id_detailT", referencedColumnName = "id_detailT")
    @ManyToOne(optional = false)
    private Detailt iddetailT;
    @JoinColumn(name = "username", referencedColumnName = "username")
    @ManyToOne(optional = false)
    private Account username;

    public Tiket() {
    }

    public Tiket(Integer idTiket) {
        this.idTiket = idTiket;
    }

    public Tiket(Integer idTiket, Character gerbong, Date waktuAwal, Date waktuAkhir) {
        this.idTiket = idTiket;
        this.gerbong = gerbong;
        this.waktuAwal = waktuAwal;
        this.waktuAkhir = waktuAkhir;
    }

    public Integer getIdTiket() {
        return idTiket;
    }

    public void setIdTiket(Integer idTiket) {
        this.idTiket = idTiket;
    }

    public Character getGerbong() {
        return gerbong;
    }

    public void setGerbong(Character gerbong) {
        this.gerbong = gerbong;
    }

    public Date getWaktuAwal() {
        return waktuAwal;
    }

    public void setWaktuAwal(Date waktuAwal) {
        this.waktuAwal = waktuAwal;
    }

    public Date getWaktuAkhir() {
        return waktuAkhir;
    }

    public void setWaktuAkhir(Date waktuAkhir) {
        this.waktuAkhir = waktuAkhir;
    }

    public Detailt getIddetailT() {
        return iddetailT;
    }

    public void setIddetailT(Detailt iddetailT) {
        this.iddetailT = iddetailT;
    }

    public Account getUsername() {
        return username;
    }

    public void setUsername(Account username) {
        this.username = username;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idTiket != null ? idTiket.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Tiket)) {
            return false;
        }
        Tiket other = (Tiket) object;
        if ((this.idTiket == null && other.idTiket != null) || (this.idTiket != null && !this.idTiket.equals(other.idTiket))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ws.b.kelompok_8.Tiket[ idTiket=" + idTiket + " ]";
    }
    
}
