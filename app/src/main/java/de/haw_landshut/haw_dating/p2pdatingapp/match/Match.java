package de.haw_landshut.haw_dating.p2pdatingapp.match;

/**
 * Created by s-gheldd on 19.06.16.
 */
public class Match {
    final private String uuid;
    final private String date;
    final private String secret;
    final private boolean own;

    public Match(String uuid, String date, String secret, boolean own) {
        this.uuid = uuid;
        this.date = date;
        this.secret = secret;
        this.own = own;
    }

    public String getUuid() {
        return uuid;
    }

    public String getDate() {
        return date;
    }

    public String getSecret() {
        return secret;
    }

    public boolean isOwn() {
        return own;
    }
}
