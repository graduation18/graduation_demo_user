package com.example.fatma.graduation_demo_user.activities.blabla_activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fatma.graduation_demo_user.R;
import com.github.florent37.singledateandtimepicker.SingleDateAndTimePicker;

import java.util.Date;

public class trip_search extends AppCompatActivity {

    private String[] cities = {"الإسماعيلية",
            "فايد, الإسماعيلية",
            "أبو سوير , الإسماعيلية" ,
            "القنطرة , إسماعيلية" ,
            "القنطرة الشرقية , إسماعيلية" ,
            "قصاصين, الإسماعيلية",
            "التل الكبير , إسماعيلية" ,

            "بورسعيد",

            "الجنوب , بورسعيد" ,
            "الزهور , بورسعيد" ,
            "الدواهي , بورسعيد" ,
            "الشرق , بورسعيد" ,
            "المناخ , بورسعيد" ,
            "العرب , بورسعيد" ,
            "غرب , بورسعيد" ,

            "القاهرة" ,

            "مدينة 15 مايو , القاهرة" ,
            "عابدين , القاهرة" ,
            "الدرب الأحمر , القاهرة" ,
            "عين شمس , القاهرة" ,
            "العامرية , القاهرة" ,
            "الأزبكية , القاهرة" ,
            "البساتين , القاهرة" ,
            "الجمالية , القاهرة" ,
            "الخليفة , القاهرة" ,
            "المعادى, القاهرة",
            "المرج , القاهرة" ,
            "المسرة , القاهرة" ,
            "المطرية , القاهرة" ,
            "المقطم , القاهرة" ,
            "الموسكي , القاهرة" ,
            "القاهرة الجديدة 1 , القاهرة" ,
            "القاهرة الجديدة 2 , القاهرة" ,
            "القاهرة الجديدة , القاهرة" ,

            "الوايلي , القاهرة" ,
            "النزهة , القاهرة" ,
            "الشرابية , القاهرة" ,
            "الشروق , القاهرة" ,
            "السجن , القاهرة" ,
            "السلام , القاهرة" ,
            "السيدة زينب , القاهرة" ,
            "التبين , القاهرة" ,
            "الظاهر , القاهرة" ,
            "الزمالك , القاهرة" ,
            "الزاوية الحمراء , القاهرة" ,
            "زيتون , القاهرة" ,
            "باب الشريعة , القاهرة" ,
            "بولاق , القاهرة" ,
            "دار السلام , القاهرة" ,
            "حدائق القبة , القاهرة" ,
            "حلوان , القاهرة" ,
            "مدينة نصر , القاهرة" ,
            "مدينة بدر , القاهرة" ,
            "هليوبوليس , القاهرة" ,
            "القاهرة القديمة , القاهرة" ,
            "منشية ناصر , القاهرة" ,
            "قصر النيل , القاهرة" ,
            "روض الفرج , القاهرة" ,
            "شبرا , القاهرة" ,
            "طره , القاهرة" ,

            "الإسكندرية",

            "باب شرق , الإسكندرية" ,
            "محرم بك , الإسكندرية" ,
            "كرموز , الإسكندرية" ,
            "العطارين , الإسكندرية" ,
            "الرمل , الإسكندرية" ,
            "سيدي جابر , الإسكندرية" ,
            "منيا البصل , الإسكندرية" ,
            "اللبان , الإسكندرية" ,
            "المنشية , الإسكندرية" ,
            "المنتزه , الإسكندرية" ,
            "الجمرك , الإسكندرية" ,
            "العامرية , الإسكندرية" ,
            "الدخيلة , الإسكندرية" ,
            "برج العرب , الإسكندرية" ,

            "البحيرة" ,

            "أبو المطامير , البحيرة" ,
            "أبو حمص , البحيرة" ,
            "الدلنجات , البحيرة" ,
            "المحمودية , البحيرة" ,
            "الرحمانية , البحيرة" ,
            "بدر , البحيرة" ,
            "دمنهور , البحيرة" ,
            "غرب النوبارية , البحيرة" ,
            "حوش عيسى , البحيرة" ,
            "إدكو , البحيرة" ,
            "إيتاي البرود , البحيرة" ,
            "كفر الدوار , البحيرة" ,
            "كوم حمادة , البحيرة" ,
            "رشيد , البحيرة" ,
            "شبراخيت , البحيرة" ,
            "وادي النطرون , البحيرة" ,

            "كفر الشيخ ",

            "البرلس وكفر الشيخ" ,
            "الهمول , كفر الشيخ" ,
            "الرياض , كفر الشيخ" ,
            "بيلا , كفر الشيخ" ,
            "دسوق , كفر الشيخ" ,
            "فوا , كفر الشيخ" ,
            "كفر الشيخ , كفر الشيخ" ,
            "مطوبس , كفر الشيخ" ,
            "قلين , كفر الشيخ" ,
            "سيدي سالم , كفر الشيخ" ,

            "الدقهلية" ,

            "أجا , الدقهلية" ,
            "الجمالية , الدقهلية" ,
            "الكردي , الدقهلية" ,
            "المنصورة , الدقهلية" ,
            "المنصورة 1 , الدقهلية" ,
            "المنصورة 2 , الدقهلية" ,
            "المنزلة , الدقهلية" ,
            "المطرية , الدقهلية" ,
            "السنبلاوين , الدقهلية" ,
            "بني عبيد , الدقهلية" ,
            "بلقاس , الدقهلية" ,
            "دكرنس , الدقهلية" ,
            "جمصة , الدقهلية" ,
            "معلومة دمنه , الدقهلية" ,
            "منية النصر , الدقهلية" ,
            "ميت غمر , الدقهلية" ,
            "ميت سلسيل , الدقهلية" ,
            "نبروه , الدقهلية" ,
            "شربين , الدقهلية" ,
            "طلخا , الدقهلية" ,
            "تيماى الامديد , الدقهلية" ,

            "الجيزة",

            "الدقي , الجيزة" ,
            "بيراميدز , الجيزة" ,
            "العجوزة , الجيزة" ,
            "العياط , الجيزة" ,
            "البدرشين , الجيزة" ,
            "الحوامدية , الجيزة" ,
            "الجيزة , الجيزة" ,
            "العمرانية , الجيزة" ,
            "الواحات البحرية , الجيزة" ,
            "مدينة الشيخ زايد , الجيزة" ,
            "الصف , الجيزة" ,
            "عطفة , الجيزة" ,
            "أوسيم , الجيزة" ,
            "بولاق , الجيزة" ,
            "إمبابة , الجيزة" ,
            "كرداسة , الجيزة" ,

            "الغربية" ,

            "المحلة الكبرى , الغربية" ,
            "السانتا , الغربية" ,
            "بسيون , الغربية" ,
            "كفر الزيات , الغربية" ,
            "كوتور , الغربية" ,
            "سمنود , الغربية" ,
            "طنطا , الغربية" ,
            "زفتا , الغربية" ,

            "مطروح" ,

            "الضبعة , مطروح" ,
            "العلمين , مطروح" ,
            "الحمام , مطروح" ,
            "النجيلة , مطروح" ,
            "الساحل الشمالي , مطروح" ,
            "السلوم , مطروح" ,
            "مرسى مطروح , مطروح" ,
            "سيدي براني , مطروح" ,
            "واحة سيوة , مطروح" ,

            "شمال سيناء" ,

            "العريش 1 , شمال سيناء" ,
            "العريش 2 , شمال سيناء" ,
            "العريش 3 , شمال سيناء" ,
            "العريش 4 , شمال سيناء" ,
            "الحسنة , شمال سيناء" ,
            "الشيخ زويد , شمال سيناء" ,
            "بئر العبد , شمال سيناء" ,
            "نخل , شمال سيناء" ,
            "رفح , شمال سيناء" ,

            "دمياط" ,

            "الزرقاء , دمياط" ,
            "دمياط 1 , دمياط" ,
            "دمياط 2 , دمياط" ,
            "فارسكور , دمياط" ,
            "كفر البطيق , دمياط" ,
            "كفر سعد , دمياط" ,
            "دمياط الجديدة , دمياط" ,
            "رأس البر , دمياط" ,

            "الشرقية",

            "أبو حماد , الشرقية" ,
            "أبو كبير , الشرقية" ,
            "الحسينية , الشرقية" ,
            "الإبراهيمية , الشرقية" ,
            "القنايات , الشرقية" ,
            "القرين , الشرقية" ,
            "الصالحية الجديدة , الشرقية" ,
            "أولاد صقر , الشرقية" ,
            "الزقازيق , الشرقية" ,
            "بلبيس , الشرقية" ,
            "ديار نجم , الشرقية" ,
            "فاقوس , الشرقية" ,
            "هحية , الشرقية" ,
            "كفر صقر , الشرقية" ,
            "مدينة العاشر من رمضان , الشرقية" ,
            "مشتول السوق , الشرقية" ,
            "منيا القمح , الشرقية" ,
            "مينشات أبو عمر , الشرقية" ,
            "سان الهجر , الشرقية" ,

            "بني سويف" ,

            "الفشن , بني سويف" ,
            "الواسطى, بنى سويف",
            "بني سويف , بني سويف" ,
            "ببا , بني سويف" ,
            "احناسيا , بني سويف" ,
            "بني سويف الجديدة , بني سويف" ,
            "ناصر , بني سويف" ,
            "سومستا الأوقاف , بني سويف" ,

            "المنيا" ,

            "أبو قرقاص , المنيا" ,
            "العدوة , المنيا" ,
            "المنيا , المنيا" ,
            "بني مزار , المنيا" ,
            "دير مواس , المنيا" ,
            "المنيا الجديدة , المنيا" ,
            "مغاغة , المنيا" ,
            "ملاويت غرب , المنيا" ,
            "ملوى , المنيا" ,
            "مطاى , المنيا" ,
            "سمالوط , المنيا" ,

            "السويس",

            "عربين , السويس" ,
            "جناين , السويس" ,
            "السويس , السويس" ,
            "عتاقة , السويس" ,
            "فيصل , السويس" ,
            "البحر الاحمر ",
            "الغردقة , البحر الأحمر" ,
            "القصير , البحر الأحمر" ,
            "شلاتين , البحر الأحمر" ,
            "حلايب , البحر الأحمر" ,
            "مرسى علم , البحر الأحمر" ,
            "رأس غارب , البحر الأحمر" ,
            "سفاجا , البحر الأحمر" ,

            "أسيوط",
            "أبنوب , أسيوط" ,
            "أبو تيج , أسيوط" ,
            "البدارى , أسيوط" ,
            "الفتح , أسيوط" ,
            "الغنايم , أسيوط" ,
            "القوصية , أسيوط" ,
            "أسيوط , أسيوط" ,
            "ديروط , أسيوط" ,
            "أسيوط الجديدة , أسيوط" ,
            "منفلوط , أسيوط" ,
            "ساحل سليم , أسيوط" ,
            "صدفة , أسيوط" ,

            "المنوفية" ,
            "الباجور , المنوفية" ,
            "أشمون , المنوفية" ,
            "الشهداء , المنوفية" ,
            "بركة الصباح , المنوفية" ,
            "مدينة السادات , المنوفية" ,
            "منوف , المنوفية" ,
            "قويسنا , المنوفية" ,
            "شبين الكوم , المنوفية" ,
            "سرس الليان , المنوفية" ,
            "تالا , المنوفية" ,

            "القليوبية" ,
            "الخانكة , القليوبية" ,
            "خوسوس , القليوبية" ,
            "القناطر الخيرية , القليوبية" ,
            "العبور , القليوبية" ,
            "بنها , القليوبية" ,
            "كفر شكر , القليوبية" ,
            "قها , القليوبية" ,
            "قليوب , القليوبية" ,
            "شبرا الخيمة , القليوبية" ,
            "شبين القناطر , القليوبية" ,
            "طوخ , القليوبية" ,

            "الفيوم",

            "الفيوم , الفيوم" ,
            "إبشواي , الفيوم" ,
            "Itsa, Faiyum",
            "الفيوم الجديد , الفيوم" ,
            "سنورس , الفيوم" ,
            "طامية , الفيوم" ,
            "يوسف الصديق , الفيوم" ,

            "جنوب سيناء" ,

  "  ابو رديس جنوب سيناء",
"الطور , جنوب سيناء",
        "دهب , جنوب سيناء",
    "نويبع , جنوب سيناء",
"رأس سدر , جنوب سيناء",
        "سانت كاترين , جنوب سيناء",
        "شرم الشيخ , جنوب سيناء",
        "طابا , جنوب سيناء",

        "سوهاج" ,
        "أخميم , سوهاج" ,
        "البلينا , سوهاج" ,
        "الكوثر , سوهاج" ,
        "المراغة , سوهاج" ,
        "المنشا , سوهاج" ,
        "عسيرات سوهاج" ,
        "دار السلام , سوهاج" ,
        "جرجا , سوهاج" ,
        "جهينة غرب , سوهاج" ,
        "جديد أخميم , سوهاج" ,
        "سوهاج الجديدة , سوهاج" ,
        "ساقلته , سوهاج" ,
        "سوهاج , سوهاج" ,
        "طهطا , سوهاج" ,
        "تيما , سوهاج" ,

        "قنا",
        "أبو طشت , قنا" ,
        "الوقف , قنا" ,
        "دشنا , قنا" ,
        "فرشوت , قنا" ,
        "قنا الجديدة , قنا" ,
        "نجع حمادي , قنا" ,
        "نقادة , قنا" ,
        "غيبتو أو كوبتوس , قنا" ,
        "قنا , قنا" ,
        "قوص , قنا" ,

        "الأقصر" ,
        "القرنة , الأقصر" ,
        "الأقصر , الأقصر" ,
        "أرمنت , الأقصر" ,
        "إسنا , الأقصر" ,
        "طيبة , الأقصر" ,

        "أسوان" ,
        "أبو سمبل , أسوان" ,
        "أسوان , أسوان" ,
        "دراو , أسوان" ,
        "إدفو , أسوان" ,
        "كوم أمبو , أسوان" ,
        "أسوان الجديدة , أسوان" ,
        "نيو توشكا , أسوان" ,
        "مدينة ناصر , أسوان" ,

        "الوادي الجديد" ,
        "الخارجة , الوادي الجديد" ,
        "بلاط , الوادي الجديد" ,
        "الداخلة , الوادي الجديد" ,
        "الفرافرة , الوادي الجديد" ,
        "باريس , الوادي الجديد" ,
            "Ismailia",
                    "Fayed,Ismailia",
                    "Abu Suwir,Ismailia",
                    "El Qantara,ismailia",
                    "El Qantara El Sharqiya,ismailia",
                    "Kasaseen,ismailia",
                    "Tell El Kebir,ismailia",

                    "Port Said",

                    "Al-Ganoub,Port Said",
                    "Al-Zohour ,Port Said   ",
                    "Al-Dawahy,Port Said   ",
                    "Al-Sharq,Port Said  ",
                    "Al-Manakh,Port Said  ",
                    "Al-Arab,Port Said  ",
                    "Gharb,Port Said",

                    "Cairo ",

                    "15 May City,Cairo ",
                    "Abdeen,Cairo  ",
                    "El Darb El Ahmar,Cairo ",
                    "Ain Shams,Cairo ",
                    "Amreya,Cairo  ",
                    "Azbakeya,Cairo ",
                    "El Basatin,Cairo ",
                    "El Gamaliya,Cairo ",
                    "El Khalifa,Cairo ",
                    "Maadi,Cairo",
                    "El Marg,Cairo ",
                    "El Masara,Cairo ",
                    "El Matareya,Cairo  ",
                    "El Mokattam,Cairo  ",
                    "El Muski,Cairo  ",
                    "New Cairo 1,Cairo  ",
                    "New Cairo 2,Cairo  ",
                    "New Cairo,Cairo  ",

                    "El Weili,Cairo",
                    "El Nozha,Cairo    ",
                    "El Sharabiya,Cairo    ",
                    "El Shorouk,Cairo    ",
                    "El Segil,Cairo    ",
                    "El Salam,Cairo    ",
                    "El Sayeda Zeinab,Cairo    ",
                    "El Tebbin,Cairo    ",
                    "El Zaher,Cairo    ",
                    "Zamalek,Cairo    ",
                    "El Zawya El Hamra,Cairo    ",
                    "Zeitoun,Cairo    ",
                    "Bab El Sharia,Cairo    ",
                    "Bulaq,Cairo    ",
                    "Dar El Salam,Cairo    ",
                    "Hada'iq El Qobbah,Cairo    ",
                    "Helwan,Cairo    ",
                    "Nasr City,Cairo    ",
                    "Badr City,Cairo    ",
                    "Heliopolis,Cairo    ",
                    "Old Cairo,Cairo    ",
                    "Manshiyat Naser,Cairo    ",
                    "Qasr El Nil,Cairo    ",
                    "Rod El Farag,Cairo    ",
                    "Shubra,Cairo    ",
                    "Tura,Cairo    ",

                    "Alexandria",

                    "Bab sharq,Alexandria  ",
                    "Moharam bek,Alexandria  ",
                    "Karmoz,Alexandria  ",
                    "El Atareen,Alexandria  ",
                    "El Ramel,Alexandria  ",
                    "Sidi Gaber,Alexandria  ",
                    "Menia El Basal,Alexandria  ",
                    "El Lban,Alexandria  ",
                    "El Manshia,Alexandria  ",
                    "El Montazh,Alexandria  ",
                    "El Gomrok,Alexandria  ",
                    "Ameria,Alexandria  ",
                    "El Dekhila,Alexandria  ",
                    "Borg El Arab,Alexandria  ",

                    "Beheira ",

                    "Abu El Matamir,Beheira  ",
                    "Abu Hummus,Beheira  ",
                    "El Delengat,Beheira  ",
                    "Mahmoudiyah,Beheira  ",
                    "Rahmaniya,Beheira  ",
                    "Badr,Beheira  ",
                    "Damanhour,Beheira  ",
                    "West Nubariyah,Beheira  ",
                    "Hosh Essa,Beheira  ",
                    "Edku,Beheira  ",
                    "Itay El Barud,Beheira  ",
                    "Kafr El Dawwar,Beheira  ",
                    "Kom Hamada,Beheira  ",
                    "Rosetta,Beheira  ",
                    "Shubrakhit,Beheira  ",
                    "Natrn Valley,Beheira  ",

                    "Kafr El Sheikh ",

                    "Burullus,Kafr El Sheikh     ",
                    "El Hamool,Kafr El Sheikh     ",
                    "El Reyad,Kafr El Sheikh     ",
                    "Bila,Kafr El Sheikh     ",
                    "Desouk,Kafr El Sheikh     ",
                    "Fuwa,Kafr El Sheikh     ",
                    "Kafr el Sheikh,Kafr El Sheikh     ",
                    "Metoubes,Kafr El Sheikh     ",
                    "Qallin,Kafr El Sheikh     ",
                    " Sidi Salem  ,Kafr El Sheikh     ",

                    "Dakahlia  ",

                    "Aga,Dakahlia    ",
                    "El Gamaliya,Dakahlia    ",
                    "El Kurdy,Dakahlia    ",
                    "El Mansoura,Dakahlia    ",
                    "El Mansoura 1,Dakahlia    ",
                    "El Mansoura 2,Dakahlia    ",
                    "El Manzala,Dakahlia    ",
                    "El Matareya,Dakahlia    ",
                    "El Senbellawein,Dakahlia    ",
                    "Beni Ebeid,Dakahlia    ",
                    "Belqas,Dakahlia    ",
                    "Dikirnis,Dakahlia    ",
                    "Gamasa,Dakahlia    ",
                    "Maḥallat Damanah,Dakahlia    ",
                    "Minyet El Nasr,Dakahlia    ",
                    "Mit Ghamr,Dakahlia    ",
                    "Mit Salsil,Dakahlia    ",
                    "Nabaroh,Dakahlia    ",
                    "Shirbin,Dakahlia    ",
                    "Talkha,Dakahlia    ",
                    "Timay El Imdid,Dakahlia    ",

                    "Giza",

                    "Dokki,Giza     ",
                    "Pyramids,Giza     ",
                    "Agouza,Giza     ",
                    "El Ayyat,Giza     ",
                    "El Badrashein,Giza     ",
                    "El Hawamdeya,Giza     ",

                    "El Omraniya,Giza     ",
                    "El Wahat El Bahariya,Giza     ",
                    "Sheikh Zayed City, Giza ",
                    "El Saff, Giza  ",
                    "Atfeh,Giza   ",
                    "Ossim,Giza ",
                    "Bulaq,Giza  ",
                    "Imbaba,Giza ",
                    "Kerdasa,Giza ",

                    "Gharbia ",

                    "El Mahalla El Kubra,Gharbia ",
                    "El Santa,Gharbia ",
                    "Basyoun,Gharbia ",
                    "Kafr El Zayat,Gharbia ",
                    "Kotoor,Gharbia ",
                    "Samanoud,Gharbia ",
                    "Tanta,Gharbia ",
                    "Zefta,Gharbia ",

                    "Matrouh ",

                    "El Dabaa,Matrouh",
                    "El Alamein,Matrouh",
                    "El Hamam,Matrouh",
                    "El Negaila,Matrouh",
                    "North Coast,Matrouh",
                    "Sallum,Matrouh",
                    "Mersa Matruh,Matrouh",
                    "Sidi Barrani,Matrouh",
                    "Siwa Oasis,Matrouh",

                    "North Sinai",

                    "El Arish 1,North Sinai",
                    "El Arish 2,North Sinai",
                    "El Arish 3,North Sinai",
                    "El Arish 4,North Sinai",
                    "El Hassana,North Sinai",
                    " Sheikh Zuweid,North Sinai",
                    "Bir El Abd,North Sinai",
                    "Nakhl,North Sinai",
                    "Rafah,North Sinai",

                    "Damietta ",

                    "El Zarqa,Damietta ",
                    "Damietta 1,Damietta ",
                    "Damietta 2,Damietta ",
                    "Faraskur,Damietta ",
                    "Kafr El Batik,Damietta ",
                    "Kafr Saad,Damietta ",
                    "New Damietta,Damietta ",
                    "Ras El Bar,Damietta ",
                    "Sharqia",

                    "Abu Hammad,Sharqia   ",
                    "Abu Kebir,Sharqia   ",
                    "El Husseiniya,Sharqia   ",
                    "El Ibrahimiya,Sharqia   ",
                    "El Qanayat,Sharqia   ",
                    "El Qurein,Sharqia   ",
                    "New Salhia,Sharqia   ",
                    "Awlad Saqr,Sharqia   ",
                    "Zagazig,Sharqia   ",
                    "Bilbeis,Sharqia   ",
                    "Diyarb Negm,Sharqia   ",
                    "Faqous,Sharqia   ",
                    "Hihya,Sharqia   ",
                    "Kafr Saqr,Sharqia   ",
                    "10th of Ramadan City,Sharqia   ",
                    "Mashtool El Souk,Sharqia   ",
                    "Minya El Qamh,Sharqia   ",
                    "Minshat Abu Omar,Sharqia   ",
                    "San El Hager,Sharqia   ",

                    "Beni Suef",

                    "El Fashn,Beni Suef",
                    "El Wasta,Beni Suef",
                    "Beni Suef,Beni Suef",
                    "Biba,Beni Suef",
                    "Ihnasiya,Beni Suef",
                    "New Beni Suef,Beni Suef",
                    "Nasser,Beni Suef",
                    "Sumusta El Waqf,Beni Suef",

                    "Minya ",

                    "Abu Qirqas,Minya ",
                    "El Idwa,Minya ",
                    "Minya,Minya ",
                    "Beni Mazar,Minya ",
                    "Deir Mawas,Minya ",
                    "New Minya,Minya ",
                    "Maghagha,Minya ",
                    "Malawit Gharb,Minya ",
                    "Mallawi,Minya ",
                    "Matai,Minya ",
                    "Samalut,Minya ",

                    "Suez",

                    "Arbaeen,Suez ",
                    "Ganayen,Suez ",

                    " Attaka,Suez ",
                    "Faisal,Suez ",
                    "Red Sea ",
                    "Hurghada,Red Sea",
                    "El Qoseir,Red Sea",
                    "Shalateen,Red Sea",
                    "Hala'ib,Red Sea",
                    "Marsa Alam,Red Sea",
                    "Ras Gharib,Red Sea",
                    "Safaga,Red Sea",
                    "Asyut",
                    "Abnub,Asyut",
                    "Abu Tig ,Asyut",
                    "El Badari,Asyut",
                    "El Fath,Asyut",
                    "El Ghanayem,Asyut",
                    "El Qusiya,Asyut",

                    "Dairut,Asyut",
                    "New Asyut,Asyut",
                    "Manfalut,Asyut",
                    "Sahel Selim,Asyut",
                    "Sodfa,Asyut",

                    "Monufia ",
                    "El Bagour,Monufia ",
                    "Ashmoun,Monufia ",
                    "El Shohada,Monufia ",
                    "Birket el Sab,Monufia ",
                    "Sadat City,Monufia ",
                    "Menouf,Monufia ",
                    "Quweisna,Monufia ",
                    "Shibin el Kom,Monufia ",
                    " Sers El Lyan ,Monufia ",
                    "Tala  ,Monufia ",

                    "Qalyubia ",
                    "Khanka,Qalyubia",
                    "Khusus,Qalyubia",
                    "El Qanater El Khayreya,Qalyubia",
                    "El Ubour,Qalyubia",
                    "Banha,Qalyubia",
                    "Kafr Shukr,Qalyubia",
                    "Qaha,Qalyubia",
                    "Qalyub,Qalyubia",
                    "Shubra El Kheima,Qalyubia",
                    "Shibin El Qanater,Qalyubia",
                    "Tukh,Qalyubia",

                    "Faiyum",


                    "Ibsheway,Faiyum",
                    "Itsa,Faiyum",
                    "New Faiyum,Faiyum",
                    "Sinnuris,Faiyum",
                    "Tamiya,Faiyum",
                    "Yousef El Seddik,Faiyum",

                    "South Sinai ",

                    "Abu Radis,South Sinai ",
                    "El Tor,South Sinai ",
                    "Dahab,South Sinai ",
                    "Nuweiba,South Sinai ",
                    "Ras Sidr,South Sinai ",
                    "Saint Catherine,South Sinai ",
                    "Sharm El Sheikh,South Sinai ",
                    "Taba,South Sinai ",

                    "Sohag ",
                    "Akhmim,Sohag ",
                    "El Balyana,Sohag ",
                    "El Kawtar,Sohag ",
                    "El Maragha,Sohag ",
                    "El Munsha,Sohag ",
                    "Aserat,Sohag ",
                    "Dar El Salam,Sohag ",
                    "Girga,Sohag ",
                    "Juhaynah West,Sohag ",
                    "New Akhmim,Sohag ",
                    "New Sohag,Sohag ",
                    "Saqultah,Sohag ",
                    "Sohag,Sohag ",
                    "Tahta,Sohag ",
                    "Tima,Sohag ",

                    "Qena",
                    "Abu Tesht,Qena",
                    "El Waqf,Qena",
                    "Dishna,Qena",
                    "Farshut,Qena",
                    "New Qena,Qena",
                    "Nag Hammadi,Qena",
                    "Naqada,Qena",
                    "Gebtu or Coptos,Qena",

                    "Qus,Qena",

                    "Luxor ",
                    "Qurna,Luxor",

                    "Armant,Luxor",
                    "Esna,Luxor",
                    "Tiba,Luxor",

                    "Aswan ",
                    "Abu Simbel,Aswan",

                    "Dakhla,New Valley",
                    "Farafra,New Valley",
                    "Baris,New Valley",



            "Daraw,Aswan",
            "Edfu,Aswan",
            "Kom Ombo,Aswan",
            "New Aswan,Aswan",
            "New Tushka,Aswan",
            "Nasser City,Aswan",

            "New Valley ",
            "Kharga,New Valley",
            "Balat,New Valley",


};
    private Button find_trip_btn;
    private AutoCompleteTextView search_from;
    private AutoCompleteTextView search_to;
    private SingleDateAndTimePicker singleDateAndTimePicker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_search);
        search_from = (AutoCompleteTextView) findViewById(R.id.search_from);

        find_trip_btn = (Button) findViewById(R.id.find_trip) ;
        search_to = (AutoCompleteTextView) findViewById(R.id.search_to);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
          singleDateAndTimePicker = (SingleDateAndTimePicker) findViewById(R.id.single_day_picker);

        singleDateAndTimePicker.addOnDateChangedListener(new SingleDateAndTimePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(String displayed, Date date) {
                display(displayed);
            }


        });


        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this, android.R.layout.select_dialog_item, cities);


               search_from.setThreshold(1);
        search_to.setThreshold(1);//will start working from first character
        search_from.setAdapter(adapter);
        search_to.setAdapter(adapter);
        search_from.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                return false;
            }
        });
        search_to.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                return false;
            }
        });

        search_from.setOnItemClickListener(itemAutocompleteClickListener);
        search_to.setOnItemClickListener(itemAutocompleteClickListener);

    }




    private void hideSoftKeyboard() {
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }


    private AdapterView.OnItemClickListener itemAutocompleteClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            hideSoftKeyboard();



        }

    };


    private void display(String toDisplay) {


        Toast.makeText(this, toDisplay, Toast.LENGTH_SHORT).show();
    }


    public void find_trip(View view) {
        String from,to;
        long time;
        from = search_from.getText().toString();
        to = search_to.getText().toString();
        time = singleDateAndTimePicker.getDate().getTime();
        if(to.length()>0&& from.length()>0 && time>System.currentTimeMillis()){
            Intent trips=new Intent(trip_search.this, trips.class);
            trips.putExtra("from",from);
            trips.putExtra("to",to);
            trips.putExtra("time",time);
            startActivity(trips);

        }else {
            Toast.makeText(this,"Errrrrrrrr",Toast.LENGTH_LONG).show();
        }

    }
}