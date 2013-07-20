package analysis;


import java.text.*;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;


public final class ISO8601CanonicalDateFormat extends SimpleDateFormat {
    private static final Locale CANONICAL_LOCALE = Locale.US;
//    private static final TimeZone CANONICAL_TZ = TimeZones.UTC;
    private NumberFormat millisParser = NumberFormat.getIntegerInstance(CANONICAL_LOCALE);
    protected NumberFormat millisFormat = new DecimalFormat(".###", new DecimalFormatSymbols(CANONICAL_LOCALE));
    private static final ThreadLocalDateFormat THREAD_LOCAL = new ThreadLocalDateFormat(new ISO8601CanonicalDateFormat());

    public static DateFormat get() {
        return THREAD_LOCAL.get();
    }

    private ISO8601CanonicalDateFormat() {
        super("yyyy-MM-dd'T'HH:mm:ss'Z'");
//        this.setTimeZone(CANONICAL_TZ);
    }

    @Override
    public Date parse(String i, ParsePosition p) {
        /* delegate to SimpleDateFormat for easy stuff */
        Date d = super.parse(i, p);
//        int milliIndex = p.getIndex();
//        /* worry aboutthe milliseconds ourselves */
//        if (null != d &&
//                -1 == p.getErrorIndex() &&
//                milliIndex + 1 < i.length() &&
//                '.' == i.charAt(milliIndex)) {
//            p.setIndex(++milliIndex); // NOTE: ++ to chomp '.'
//            Number millis = millisParser.parse(i, p);
//            if (-1 == p.getErrorIndex()) {
//                int endIndex = p.getIndex();
//                d = new Date(d.getTime()
//                        + (long) (millis.doubleValue() *
//                        Math.pow(10, (3 - endIndex + milliIndex))));
//            }
//        }
        return d;
    }

    @Override
    public StringBuffer format(Date d, StringBuffer toAppendTo,
                               FieldPosition pos) {
        /* delegate to SimpleDateFormat for easy stuff */
        super.format(d, toAppendTo, pos);
        /* worry aboutthe milliseconds ourselves */
//        long millis = d.getTime() % 1000l;
//        if (0l == millis) {
//            return toAppendTo;
//        }
//        int posBegin = toAppendTo.length();
//        toAppendTo.append(millisFormat.format(millis / 1000d));
//        if (DateFormat.MILLISECOND_FIELD == pos.getField()) {
//            pos.setBeginIndex(posBegin);
//            pos.setEndIndex(toAppendTo.length());
//        }
        return toAppendTo;
    }

    @Override
    public Object clone() {
        ISO8601CanonicalDateFormat c
                = (ISO8601CanonicalDateFormat) super.clone();
//        c.millisParser = NumberFormat.getIntegerInstance(CANONICAL_LOCALE);
//        c.millisFormat = new DecimalFormat(".###",
//                new DecimalFormatSymbols(CANONICAL_LOCALE));
        return c;
    }


    private static class ThreadLocalDateFormat extends ThreadLocal<DateFormat> {
        DateFormat proto;

        public ThreadLocalDateFormat(DateFormat d) {
            super();
            proto = d;
        }

        @Override
        protected DateFormat initialValue() {
            return (DateFormat) proto.clone();
        }
    }
}