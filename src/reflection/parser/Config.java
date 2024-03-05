package reflection.parser;

import java.util.Arrays;

public class Config {
    private String profile;
    private long timeout;

    private String[] titleFonts;

    public String[] getTitleFonts() {
        return titleFonts;
    }

    public void setTitleFonts(String[] titleFonts) {
        this.titleFonts = titleFonts;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    @Override
    public String toString() {
        return "Config{" +
                "profile='" + profile + '\'' +
                ", timeout=" + timeout +
                ", titleFonts=" + Arrays.toString(titleFonts) +
                '}';
    }
}
