package utils;

import java.time.Instant;

public class UnixSeconds {
    private final long seconds;
    private UnixSeconds(long seconds) {
        this.seconds = seconds;
    }

    public static UnixSeconds now() {
        return new UnixSeconds(Instant.now().getEpochSecond());
    }

    public static UnixSeconds from(long seconds) {
        return new UnixSeconds(seconds);
    }

    public long toLong() {
        return seconds;
    }

    public UnixSeconds plusSeconds(long seconds) {
        return new UnixSeconds(this.seconds + seconds);
    }

    public UnixSeconds minusSeconds(long seconds) {
        return new UnixSeconds(this.seconds - seconds);
    }

    public boolean isBefore(UnixSeconds other) {
        return this.seconds < other.seconds;
    }

    public boolean isAfter(UnixSeconds other) {
        return this.seconds > other.seconds;
    }

    public long secondsUntil(UnixSeconds other) {
        return other.seconds - this.seconds;
    }

    @Override
    public String toString() {
        return "Unix Seconds : " + seconds;
    }
}
