import java.util.*;


 // کلاس مربوط به ساده‌سازی تابع منطقی به روش Quine–McCluskey

public class QMSimplifierFull {

    // متغیرهای قابل استفاده (تا 6 متغیر)
    static char[] VARIABLES = {'A', 'B', 'C', 'D', 'E', 'F'};


      //دریافت ایمپلیکنت اصلی (Prime Implicants) از لیست مینترم‌ها

    public static List<String> getPrimeImplicants(List<String> minterms) {
        List<String> current = new ArrayList<>(minterms); // لیست فعلی از ترم‌ها
        List<String> primeImplicants = new ArrayList<>(); // لیست نهایی ترم‌ها
        boolean changed;

        // حلقه تا زمانی که ترکیب جدیدی حاصل می‌شود
        do {
            List<String> next = new ArrayList<>(); // ترم‌های ترکیب‌ شده
            List<String> used = new ArrayList<>(); // ترم‌هایی که استفاده شده‌اند
            changed = false;

            // بررسی هر دو ترم برای ترکیب‌ شدن
            for (int i = 0; i < current.size(); i++) {
                for (int j = i + 1; j < current.size(); j++) {
                    String a = current.get(i);
                    String b = current.get(j);

                    int diffPos = getDifferencePosition(a, b); // بررسی تفاوت یک ‌بیتی
                    if (diffPos != -1) {
                        // ترکیب دو ترم با تفاوت در یک موقعیت
                        String combined = a.substring(0, diffPos) + "-" + a.substring(diffPos + 1);
                        if (!next.contains(combined)) {
                            next.add(combined);
                        }
                        used.add(a);
                        used.add(b);
                        changed = true;
                    }
                }
            }

            // اضافه کردن ترم‌هایی که ترکیب نشده‌اند
            for (String term : current) {
                if (!used.contains(term) && !primeImplicants.contains(term)) {
                    primeImplicants.add(term);
                }
            }

            current = next;

        } while (changed);

        return primeImplicants;
    }


     // پیدا کردن موقعیتی که دو رشته باینری دقیقاً یک بیت با هم تفاوت دارند
    static int getDifferencePosition(String a, String b) {
        int pos = -1;
        for (int i = 0; i < a.length(); i++) {
            if (a.charAt(i) != b.charAt(i)) {
                if (pos != -1) return -1; // بیشتر از یک تفاوت → نمی‌توان ترکیب کرد
                pos = i;
            }
        }
        return pos;
    }


     // تبدیل رشته باینری (با "-") به عبارت منطقی بر اساس تعداد متغیر

    public static String binaryToLiteral(String bin, int numVars) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < numVars; i++) {
            if (bin.charAt(i) == '-') continue;
            sb.append(bin.charAt(i) == '1' ? VARIABLES[i] : VARIABLES[i] + "'");
        }
        return sb.toString();
    }
}