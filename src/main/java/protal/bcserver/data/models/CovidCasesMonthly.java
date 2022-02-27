package protal.bcserver.data.models;

import java.util.List;


public class CovidCasesMonthly {


    private String country;

    private List<CovidCases> cases;

    private int year;


    public CovidCasesMonthly() {
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public List<CovidCases> getCases() {
        return cases;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }


    public String toString(CovidCases cc) {
        return "{" +
                "country='" + country + '\'' +
                ", year=" + year +
                ", case=" + cc +
                '}';
    }

}
