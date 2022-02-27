package protal.bcserver.data.models;

import protal.bcserver.clockchain.block;

import javax.persistence.*;

@Entity
@Table(name = "covid_cases")
public class CovidCases {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(name = "deaths")
    private int deaths;
    @Column(name = "recovered")
    private int recovered;
    @Column(name = "confirmed")
    private int confirmed;
    @Column(name = "month")
    private int month;
    @Column(name = "year")
    private int year;
    @Column(name = "country")
    private String country;

    @Transient
    @OneToOne(mappedBy = "ccms", fetch = FetchType.LAZY)
    private block blk;

    public CovidCases(int deaths, int recovered, int confirmed, int month, int year, String country) {
        this.deaths = deaths;
        this.recovered = recovered;
        this.confirmed = confirmed;
        this.month = month;
        this.country = country;
        this.year = year;
    }

    public CovidCases() {
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public String toString() {
        return "{" +
                "deaths=" + deaths +
                ", recovered=" + recovered +
                ", confirmed=" + confirmed +
                ", month=" + month +
                ", year=" + year +
                ", country='" + country + '\'' +
                '}';
    }


    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getDeaths() {
        return deaths;
    }

    public void setDeaths(int value) {
        this.deaths = value;
    }

    public int getRecovered() {
        return recovered;
    }

    public void setRecovered(int value) {
        this.recovered = value;
    }

    public int getConfirmed() {
        return confirmed;
    }

    public void setConfirmed(int value) {
        this.confirmed = value;
    }
}
