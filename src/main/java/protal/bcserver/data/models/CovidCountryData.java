package protal.bcserver.data.models;

import protal.bcserver.clockchain.block;

import javax.persistence.*;


@Entity
@Table(name = "covid_test")
public class CovidCountryData {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(name = "date")
    private String dt;
    @Column(name = "timestamp")
    private int ts;
    @Column(name = "location")
    private String location;
    @Column(name = "deaths")
    private int deaths;
    @Column(name = "recovered")
    private int recovered;
    @Column(name = "confirmed")
    private int confirmed;
    @Column(name = "active")
    private int active;

    @Transient
    @OneToOne(mappedBy = "ccd")
    private block blk;

    public CovidCountryData() {

    }

    public CovidCountryData(String dt, int ts, String location, int deaths, int recovered, int confirmed, int active) {

        this.dt = dt;
        this.ts = ts;
        this.location = location;
        this.deaths = deaths;
        this.recovered = recovered;
        this.confirmed = confirmed;
        this.active = active;
    }

    public int getTs() {
        return ts;
    }

    public void setTs(int ts) {
        this.ts = ts;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDt() {
        return dt;
    }

    public void setDt(String dt) {
        this.dt = dt;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getDeaths() {
        return deaths;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    public int getRecovered() {
        return recovered;
    }

    public void setRecovered(int recovered) {
        this.recovered = recovered;
    }

    public int getConfirmed() {
        return confirmed;
    }

    public void setConfirmed(int confirmed) {
        this.confirmed = confirmed;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return
                '{' +
                        "dt='" + dt + '\'' +
                        ", ts=" + ts +
                        ", location='" + location + '\'' +
                        ", deaths=" + deaths +
                        ", recovered=" + recovered +
                        ", confirmed=" + confirmed +
                        ", active=" + active +
                        '}';
    }


}
