package protal.bcserver.data.models;

import javax.persistence.*;
import java.util.Objects;

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

    public CovidCountryData() {

    }

    public CovidCountryData(String dt, int ts, String location, int deaths, int recovered, int confirmed, int active) {
//        this.id = id; //long id,
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
                        "  id=" + id +
                        ", dt='" + dt + '\'' +
                        ", ts=" + ts +
                        ", location='" + location + '\'' +
                        ", deaths=" + deaths +
                        ", recovered=" + recovered +
                        ", confirmed=" + confirmed +
                        ", active=" + active +
                        '}';
    }

    @Entity
    @Table(name = "data", schema = "blockchain_covid")
    public static class DataEntity {
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Id
        @Column(name = "id")
        private int id;
        @Basic
        @Column(name = "location")
        private String location;
        @Basic
        @Column(name = "confirmed")
        private Integer confirmed;
        @Basic
        @Column(name = "deaths")
        private Integer deaths;
        @Basic
        @Column(name = "recovered")
        private Integer recovered;
        @Basic
        @Column(name = "active")
        private Integer active;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public Integer getConfirmed() {
            return confirmed;
        }

        public void setConfirmed(Integer confirmed) {
            this.confirmed = confirmed;
        }

        public Integer getDeaths() {
            return deaths;
        }

        public void setDeaths(Integer deaths) {
            this.deaths = deaths;
        }

        public Integer getRecovered() {
            return recovered;
        }

        public void setRecovered(Integer recovered) {
            this.recovered = recovered;
        }

        public Integer getActive() {
            return active;
        }

        public void setActive(Integer active) {
            this.active = active;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            DataEntity that = (DataEntity) o;
            return id == that.id && Objects.equals(location, that.location) && Objects.equals(confirmed, that.confirmed) && Objects.equals(deaths, that.deaths) && Objects.equals(recovered, that.recovered) && Objects.equals(active, that.active);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id, location, confirmed, deaths, recovered, active);
        }
    }
}
