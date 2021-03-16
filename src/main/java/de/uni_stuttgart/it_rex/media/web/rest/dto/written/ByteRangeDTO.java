package de.uni_stuttgart.it_rex.media.web.rest.dto.written;

public class ByteRangeDTO {

    /**
     * Constructor.
     *
     * @param theStart the start
     * @param theEnd   the end
     */
    public ByteRangeDTO(final long theStart, final long theEnd) {
        this.start = theStart;
        this.end = theEnd;
        this.length = calculateLength(theStart, theEnd);
    }

    /**
     * The start of the byte range.
     */
    private long start;

    /**
     * The end of the byte range.
     */
    private long end;

    /**
     * The length of the byte range.
     */
    private long length;

    /**
     * Getter.
     *
     * @return the start of the byte range.
     */
    public long getStart() {
        return start;
    }

    /**
     * Setter.
     *
     * @param newStart the start of the byte range.
     */
    public void setStart(final long newStart) {
        this.start = newStart;
        this.length = calculateLength(this.start, this.end);
    }

    /**
     * Getter.
     *
     * @return the end of the byte range.
     */
    public long getEnd() {
        return end;
    }

    /**
     * Setter.
     *
     * @param newEnd the end of the byte range.
     */
    public void setEnd(final long newEnd) {
        this.end = newEnd;
        this.length = calculateLength(this.start, this.end);
    }

    /**
     * Getter.
     *
     * @return The length of the byte range.
     */
    public long getLength() {
        return length;
    }

    private static long calculateLength(final long start, final long end) {
        return end - start + 1;
    }
}
