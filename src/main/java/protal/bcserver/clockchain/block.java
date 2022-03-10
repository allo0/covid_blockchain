package protal.bcserver.clockchain;

import protal.bcserver.data.models.CovidCases;
import protal.bcserver.data.models.CovidCountryData;

import javax.persistence.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Entity
@Table(name = "blocks")
public class block {
    @Transient
    private boolean block_ready; //check if the block is ready

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private int id;

    @Column(name = "hash")
    private String hash;
    private String previousHash;
    @Column(name = "data")
    private String data;
    @Column(name = "timestamp")
    private long timeStamp;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "covidcountry_id")
    private CovidCountryData ccd;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "covidcasesmonthly_id", referencedColumnName = "id")
    private CovidCases ccms;


    @Transient
    private Integer prefix;
    @Transient
    private int nonce;


    public block(String previousHash, String data, long timeStamp) {
        this.previousHash = previousHash;
        this.data = data;
        this.timeStamp = timeStamp;
        hash = calculateBlockHash(nonce);
    }

    public block() {

    }

    public CovidCases getCc() {
        return ccms;
    }

    public void setCc(CovidCases ccms) {
        this.ccms = ccms;
    }

    public CovidCountryData getCcd() {
        return ccd;
    }

    public void setCcd(CovidCountryData ccd) {
        this.ccd = ccd;
    }

    synchronized boolean isBlock_ready() {
        return block_ready;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public void mineBlock(int prefix) {
        block_ready = false;

        /**
         * Initiate a thread pool, and indicate the threaded code block
         */
        ExecutorService executor = Executors.newFixedThreadPool(5);  //Using 5 threads.
        Runnable mineTask = () -> {
            String prefixString = new String(new char[prefix]).replace('\0', '0');
            // Each thread has its own nonce and hash
            int thread_nonce = this.nonce;
            String thread_hash = this.hash;
            while (!thread_hash.substring(0, prefix).equals(prefixString) && !block_ready) {

                thread_nonce++;
                thread_hash = calculateBlockHash(thread_nonce);
            }
            block_ready = true;

            // One thread each time can access and set the "global" nonce and hash
            synchronized (this) {
                if (block_ready && thread_hash.substring(0, prefix).equals(prefixString)) {
                    this.nonce = thread_nonce;
                    this.hash = thread_hash;

                    executor.shutdown();
                }
            }

        };

        executor.execute(mineTask);
        // Call a synchronized function that checks if the block is ready
        while (!isBlock_ready()) {
        }
    }

    public String getHash() {
        return hash;
    }

    public String getPreviousHash() {
        return previousHash;
    }

    public String calculateBlockHash(int nonce) {
        String dataToHash = previousHash + timeStamp +
                nonce + data;

        MessageDigest digest = null;
        byte[] bytes = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
            bytes = digest.digest(dataToHash.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        StringBuffer buffer = new StringBuffer();
        for (byte b : bytes)
            buffer.append(String.format("%02x", b));


        return buffer.toString();
    }

    public String calculateBlockHash() {
        String dataToHash = previousHash + timeStamp +
                nonce + data;
        MessageDigest digest = null;
        byte[] bytes = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
            bytes = digest.digest(dataToHash.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        StringBuffer buffer = new StringBuffer();
        for (byte b : bytes)
            buffer.append(String.format("%02x", b));

        return buffer.toString();
    }


}
