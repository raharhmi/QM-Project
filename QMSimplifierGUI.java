import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;


 // کلاس رابط گرافیکی کاربر برای ساده‌سازی مدار منطقی با روش QM

public class QMSimplifierGUI {


    //  راه‌اندازی و نمایش رابط گرافیکی برنامه

    static void createAndShowGUI() {
        // ایجاد پنجره اصلی
        JFrame frame = new JFrame("ساده‌سازی مدار منطقی - روش QM");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 500);
        frame.setLayout(new BorderLayout(10, 10));
        frame.setLocationRelativeTo(null); // نمایش وسط صفحه

        //  پنل ورودی (شمال صفحه)
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridBagLayout()); // استفاده از GridBag برای چیدمان دقیق
        inputPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20)); // حاشیه اطراف پنل

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10); // فاصله بین آیتم‌ها

        // لیبل تعداد متغیرها
        JLabel varLabel = new JLabel("تعداد متغیرها (2 تا 6):");
        varLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));

        // انتخاب‌ گر تعداد متغیرها (Drop-down)
        JComboBox<Integer> varCount = new JComboBox<>(new Integer[]{2, 3, 4, 5, 6});
        varCount.setFont(new Font("Tahoma", Font.PLAIN, 16));

        // لیبل ورودی مینترم‌ها
        JLabel mintermLabel = new JLabel("مینترم‌ها (جداشده با کاما، مثل 0,1,2):");
        mintermLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));

        // فیلد ورودی مینترم‌ها
        JTextField mintermsField = new JTextField();
        mintermsField.setFont(new Font("Tahoma", Font.PLAIN, 16));

        // قرار دادن اجزا در inputPanel با موقعیت مشخص
        gbc.gridx = 0; gbc.gridy = 0;
        inputPanel.add(varLabel, gbc);
        gbc.gridx = 1;
        inputPanel.add(varCount, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        inputPanel.add(mintermLabel, gbc);
        gbc.gridx = 1;
        inputPanel.add(mintermsField, gbc);

        // دکمه ساده‌سازی مرکز صفحه
        JButton simplifyButton = new JButton("ساده‌سازی کن");
        simplifyButton.setFont(new Font("Tahoma", Font.BOLD, 18));
        simplifyButton.setBackground(new Color(0x4CAF50)); // رنگ سبز
        simplifyButton.setForeground(Color.WHITE);         // متن سفید
        simplifyButton.setFocusPainted(false);             // بدون حاشیه فوکوس
        simplifyButton.setPreferredSize(new Dimension(200, 45));

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(simplifyButton);

        //  ناحیه نمایش خروجی (جنوب صفحه)
        JTextArea resultArea = new JTextArea(3, 30);
        resultArea.setFont(new Font("Monospaced", Font.BOLD, 20)); // فونت خوانا و منوپس
        resultArea.setLineWrap(true);
        resultArea.setWrapStyleWord(true);
        resultArea.setEditable(false);
        resultArea.setForeground(Color.BLUE);     // رنگ آبی برای متن
        resultArea.setBackground(new Color(245, 245, 245));         // پس‌زمینه خاکستری روشن
        resultArea.setBorder(BorderFactory.createTitledBorder("عبارت ساده‌شده"));

        JPanel resultPanel = new JPanel(new BorderLayout());
        resultPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
        resultPanel.add(resultArea, BorderLayout.CENTER);

        //      عملکرد دکمه ساده‌سازی
        simplifyButton.addActionListener(e -> {
            int numVars = (int) varCount.getSelectedItem();  // گرفتن تعداد متغیر
            String[] parts = mintermsField.getText().split(","); // جدا کردن مینترم‌ها از متن ورودی
            List<String> minterms = new ArrayList<>();
            try {
                // تبدیل مینترم‌ها به رشته باینری با صفرهای پیشوندی
                for (String part : parts) {
                    int m = Integer.parseInt(part.trim());
                    if (m < 0 || m >= (1 << numVars)) continue; // بررسی خارج از محدوده

                    String bin = String.format("%" + numVars + "s", Integer.toBinaryString(m))
                            .replace(' ', '0'); // تولید باینری با padding
                    minterms.add(bin);
                }

                // اجرای الگوریتم QM و دریافت ترم‌های ساده‌شده
                List<String> simplified = QMSimplifierFull.getPrimeImplicants(minterms);
                StringBuilder result = new StringBuilder();

                // تبدیل ترم‌های باینری به فرمت منطقی و اتصال آن‌ها با +
                for (int i = 0; i < simplified.size(); i++) {
                    result.append(QMSimplifierFull.binaryToLiteral(simplified.get(i), numVars));
                    if (i < simplified.size() - 1)
                        result.append(" + ");
                }

                // نمایش نتیجه در بخش خروجی
                resultArea.setText(result.toString());

            } catch (Exception ex) {
                resultArea.setText("خطا در ورودی‌ها!"); // نمایش پیام خطا در صورت استثناء
            }
        });

        //  اضافه کردن پنل‌ها به فریم
        frame.add(inputPanel, BorderLayout.NORTH);
        frame.add(buttonPanel, BorderLayout.CENTER);
        frame.add(resultPanel, BorderLayout.SOUTH);

        // نمایش فریم
        frame.setVisible(true);
    }
}