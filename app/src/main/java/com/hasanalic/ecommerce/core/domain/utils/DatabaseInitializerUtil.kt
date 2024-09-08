package com.hasanalic.ecommerce.core.domain.utils

import com.hasanalic.ecommerce.feature_home.data.local.entity.ProductEntity
import com.hasanalic.ecommerce.feature_product_detail.data.local.entity.ReviewEntity

object DatabaseInitializerUtil {
    fun getProductEntityArray(): Array<ProductEntity> {
        return arrayOf(
            ProductEntity(
                "Elektronik",
                "https://cdn.dsmcdn.com/ty376/product/media/images/20220330/19/79045113/430653100/2/2_org_zoom.jpg",
                "Samsung",
                "Samsung Galaxy A35 5G Akıllı Cep Telefon. 8 GB RAM. 256 GB Hafıza, Mavi (Samsung Türkiye Garantili)",
                14749,
                0,
                4.5,
                "144",
                "369875214520",
                "B Kargo",
                "Kazanç Shop",
                "8.7"
            ),
            ProductEntity(
                "Elektronik",
                "https://www.deercase.com/image/cache/catalog/0/0-1-CST/15-promax-beyaz-1-600x600.jpg",
                "Apple",
                "iPhone 15 Pro Max Beyaz 256 GB 8 GB Ram Akıllı Telefon (Apple Türkiye Garantili)",
                78900,
                0,
                4.8,
                "1852",
                "128555662211",
                "B Kargo",
                "Kazanç Shop",
                "8.7"
            ),
            ProductEntity(
                "Bilgisayar",
                "https://www.stuff.tv/wp-content/uploads/sites/2/2023/01/macbook-pro-2023-15.jpg",
                "Apple",
                "Apple MacBook Pro M3 Pro - Ekran Kartı: NVIDIA 320M, Ram: 18GB 512GB SSD macOS 14 inch Taşınabilir Bilgisayar Uzay Siyahı MRX33TU/A",
                52000,
                0,
                4.7,
                "597",
                "548702369851",
                "A Kargo",
                "Pandora Ticaret",
                "9.1"
            ),
            ProductEntity(
                "Bilgisayar",
                "https://cdn.akakce.com/z/lenovo/lenovo-ideapad-3-82rk00hptx-i5-1235u-8-gb-256-gb-ssd-iris-xe-graphics-15-6-full-hd-notebook.jpg",
                "Lenovo",
                "Lenovo Thinkpad E15 Gen4 - İşlemci: Intel Core I7 1255U, Ram: 32 GB, Ekran Kartı: Intel Iris X, 512 GB SSD 15.6 inch Fhd Windows 11 Pro Taşınabilir Bilgisayar",
                35650,
                0,
                4.6,
                "1288",
                "010101010101",
                "A Kargo",
                "Pandora Ticaret",
                "9.1"
            ),
            ProductEntity(
                "Bahçe",
                "https://m.media-amazon.com/images/I/71osNV6jaaL._AC_SY879_.jpg",
                "Green Life",
                "MİNERAL Katkılı Perlitli Bitki Toprağı fideleme Saksı Toprağı İç Dış Mekan Perlitli Toprak 50 LT.",
                239,
                0,
                4.1,
                "112",
                "845555955551",
                "B Kargo",
                "Bizim Bahçe",
                "7.9"
            ),
            ProductEntity(
                "Bebek",
                "https://m.media-amazon.com/images/I/61hOnMZBPmL._AC_SX679_.jpg",
                "Bebelac",
                "Bebelac 1 Bebek Sütü 800 g 0-6 Ay",
                254,
                50,
                4.7,
                "37",
                "214785369102",
                "A Kargo",
                "Bebelac Store",
                "8.9"
            ),
            ProductEntity(
                "Bebek",
                "https://m.media-amazon.com/images/I/71chEQjy-JL._AC_SX679_.jpg",
                "Prima",
                "Bebek Bezi Premium Care 6 Beden 93 Adet Junior Aylık Fırsat Paketi",
                989,
                50,
                4.6,
                "676",
                "777777777711",
                "D Kargo",
                "Prima Store",
                "9.1"
            ),
            ProductEntity(
                "Ev",
                "https://m.media-amazon.com/images/I/61X0bWwRaIL._AC_SY879_.jpg",
                "Philips",
                "Philips XB9185/07 TriActive Ultra LED Başlık ve PowerCyclone 10 Teknolojisi ile Premium Toz Torbasız Elektrikli Süpürge",
                12775,
                0,
                4.5,
                "1841",
                "785632014580",
                "D Kargo",
                "Philips Store",
                "9.2"
            ),
            ProductEntity(
                "Ev",
                "https://ekinlerelaletleri.com/image/catalog/product/bosch-eea206-komursuz-sarjli-matkap-88v-12ah.jpg",
                "DeWalt",
                "Dewalt Akülü Matkap / Akülü Matkap (18V, 1,5Ah, Fırçasız, Kompakt, Üniversal Kullanılabilir, Bütünleşmiş Led-Lamba, Dâhil Olanlar2 Akü, Sistem-Hızlı Şarj Cihazı Ve T Stak Kutu) Dcd777S2T",
                7500,
                0,
                3.6,
                "1444",
                "120365874951",
                "B Kargo",
                "Paxel",
                "8.1"
            ),
            ProductEntity(
                "Evcil Hayvan",
                "https://images.migrosone.com/sanalmarket/product/41991817/41991817-e7ceb2-1650x1650.jpg",
                "Hill's Science",
                "Kuru Kedi Maması - Tavuk Aromalı",
                99,
                90,
                4.7,
                "25",
                "785632014589",
                "B Kargo",
                "Sevimli Shop",
                "9.7"
            ),
            ProductEntity(
                "Gıda ve İçecek",
                "https://m.media-amazon.com/images/I/618qlbuXW8S.__AC_SX300_SY300_QL70_ML2_.jpg",
                "Eti",
                "Eti Karam %70 Kakaolu Bitter Çikolata 60 g x 6 Adet",
                162,
                0,
                4.5,
                "30",
                "444455558888",
                "B Kargo",
                "Eti",
                "9.6"
            ),
            ProductEntity(
                "Gıda ve İçecek",
                "https://m.media-amazon.com/images/I/61PIAPpJY5L.__AC_SX300_SY300_QL70_ML2_.jpg",
                "Erikli",
                "Erikli Su 1.5 Litre, Avantaj Paket (6 x 1.5l)",
                169,
                95,
                4.5,
                "37",
                "715586363980",
                "C Kargo",
                "Erikli",
                "8.9"
            ),
            ProductEntity(
                "Kitaplar",
                "https://m.media-amazon.com/images/I/51MVdrFt-xL._SY342_.jpg",
                "Metis",
                "Yüzüklerin Efendisi: (Tek Cilt) Yüzük Kardeşliği - İki Kule - Kralın Dönüşü - J. R. R. Tolkien kitap",
                468,
                50,
                4.8,
                "566",
                "787870087878",
                "A Kargo",
                "Esnaf Kitapçılık",
                "9.5"
            ),
            ProductEntity(
                "Kitaplar",
                "https://m.media-amazon.com/images/I/51+K-hv4c+L._SY342_.jpg",
                "Domingo",
                "Geleceğini Keşfedenler: Dijital Çağın Biyografisi - Walter Isaacson kitap",
                143,
                25,
                4.5,
                "14",
                "787878787878",
                "A Kargo",
                "Esnaf Kitapçılık",
                "9.5"
            ),
            ProductEntity(
                "Kitaplar",
                "https://m.media-amazon.com/images/I/61FrV5sju7L._AC_UF894,1000_QL80_.jpg",
                "İthaki",
                "Pürdikkat: Odaklanma Becerimizi Nasıl Yitirdik - Cal Newport kitap",
                88,
                90,
                4.5,
                "60",
                "102365874951",
                "A Kargo",
                "Esnaf Kitapçılık",
                "9.5"
            ),
            ProductEntity(
                "Kişisel Bakım",
                "https://images.migrosone.com/macrocenter/product/35409711/35409711-024788.jpg",
                "L'Oreal",
                "Kırışık Karşıtı Gece Yüz Bakım Kremi 50 ml, Saf Q10 ve Kreatin, 24 Saat Nemlendirme, 7 Günde Cilt Sıkılaştırıcı Etki",
                262,
                0,
                4.8,
                "150",
                "951463870215",
                "A Kargo",
                "Gizmo",
                "7.8"
            ),
            ProductEntity(
                "Moda",
                "https://m.media-amazon.com/images/I/51tU4RgMA+L._AC_SX679_.jpg",
                "YYNUDA",
                "Kadın Deri Ceket Fermuarlı Rüzgar Geçirmez Dik Yaka Motorcu Ceket İlkbahar Sonbahar Kısa Ceket, Siyah, XL",
                1578,
                50,
                3.6,
                "22",
                "555555555555",
                "C Kargo",
                "Zara",
                "9.6"
            ),
            ProductEntity(
                "Müzik",
                "https://m.media-amazon.com/images/I/61qRTQoOoML._AC_SX425_.jpg",
                "Cort",
                "Cort Elektro Akustik Gitar AF510E-BKS",
                6976,
                50,
                4.5,
                "8",
                "444444444444",
                "B Kargo",
                "R&Red",
                "9.4"
            ),
            ProductEntity(
                "Oyuncak",
                "https://m.media-amazon.com/images/I/71-U9Tq8Y+L._AC_SX425_.jpg",
                "LEGO",
                "LEGO Creator Muhteşem Dinozorlar 31058-7 Yaş ve Üzeri Dinozorları Seven Çocuklar için Yaratıcı Oyuncak Yapım Seti (174 Parça)",
                379,
                0,
                4.7,
                "92",
                "548702369851",
                "A Kargo",
                "Railroad",
                "9.2"
            ),
            ProductEntity(
                "Spor",
                "https://akn-barcin.a-cdn.akinoncloud.com/products/2022/09/20/994169/38f4b86a-6fac-4828-907d-1d0df2b0ab75.jpg",
                "Nike",
                "Nike Erkek Air Monarch IV fitness spor ayakkabısı",
                2400,
                0,
                4.4,
                "2348",
                "333333333333",
                "B Kargo",
                "Synergy",
                "8.9"
            ),
            ProductEntity(
                "Elektronik",
                "https://m.media-amazon.com/images/I/61hm+Vm1fML._AC_SX466_.jpg",
                "Xiaomi",
                "Redmi Note 13 Pro 8 GB RAM 256 GB ROM, Cep Telefonu, Orman Yeşili",
                15199,
                50,
                4.2,
                "51",
                "111122211119",
                "B Kargo",
                "Kazanç Shop",
                "8.7"
            ),
            ProductEntity(
                "Bilgisayar",
                "https://m.media-amazon.com/images/I/61xtEhKa4xL._AC_SY355_.jpg",
                "HP Victus",
                "HP Victus Gaming 15 Dizüstü Bilgisayar, 15.6'' FHD 144 Hz IPS 9 Ms, İşlemci: AMD Ryzen 5 5600H, 16 GB Ram, Ekran Kartı: NVIDIA GeForce RTX 3050, 512 GB SSD, FreeDOS, Performans Mavisi, 71T73EA",
                24599,
                0,
                4.5,
                "141",
                "888888888888",
                "A Kargo",
                "Pandora Ticaret",
                "9.1"
            ),
        )
    }

    fun getReviewEntityArray(): Array<ReviewEntity> {
        return arrayOf(
            // 1
            ReviewEntity(
                reviewProductId = "1",
                reviewName = "Batu",
                reviewProfilePhoto = "https://randomuser.me/api/portraits/men/68.jpg",
                reviewDate = "2024-03-06",
                reviewTitle = "Çok İyi Telefon",
                reviewContent = "Ürün gerçekten mükemmel. Hem performansı hem de kamera kalitesi beni çok etkiledi. Herkese tavsiye ederim.",
                reviewProductPhoto = "",
                reviewStar = 4
            ),
            ReviewEntity(
                reviewProductId = "1",
                reviewName = "Tuğba Kiraz",
                reviewProfilePhoto = "https://randomuser.me/api/portraits/women/50.jpg",
                reviewDate = "2024-03-06",
                reviewTitle = "Harika Ürün",
                reviewContent = "Telefonun özellikleri çok iyi. Hızlı kargo ve güvenilir satıcı. Teşekkür ederim!",
                reviewProductPhoto = "",
                reviewStar = 5
            ),
            ReviewEntity(
                reviewProductId = "1",
                reviewName = "gökhan",
                reviewProfilePhoto = "https://static.vecteezy.com/system/resources/thumbnails/009/292/244/small/default-avatar-icon-of-social-media-user-vector.jpg",
                reviewDate = "2024-03-06",
                reviewTitle = "Kötü Deneyim",
                reviewContent = "Ürünü aldığımda paketlemede sorun vardı. Kargo firması çok yavaştı ve ürünüm hasar görmüştü. İlgilenilmesini beklerdim.",
                reviewProductPhoto = "https://image-us.samsung.com/SamsungUS/support/solutions/mobile/phones/galaxy-s/s20/PH_GS_S20_Cracked-Ink-blots-bleeding-screen.png",
                reviewStar = 2
            ),

            // 2
            ReviewEntity(
                reviewProductId = "2",
                reviewName = "Ayşe",
                reviewProfilePhoto = "https://randomuser.me/api/portraits/women/50.jpg",
                reviewDate = "2024-04-15",
                reviewTitle = "Harika bir ürün!",
                reviewContent = "Ürünü aldığımdan beri çok memnunum. Kaliteli malzeme ve hızlı teslimat. Kesinlikle tavsiye ederim.",
                reviewProductPhoto = "link",
                reviewStar = 5
            ),
            ReviewEntity(
                reviewProductId = "2",
                reviewName = "Ali",
                reviewProfilePhoto = "https://static.vecteezy.com/system/resources/thumbnails/009/292/244/small/default-avatar-icon-of-social-media-user-vector.jpg",
                reviewDate = "2024-04-18",
                reviewTitle = "Beklediğim gibi değil",
                reviewContent = "Ürün resimdeki gibi değil. Kalitesiz malzeme ve eksik parçalar var. Dezavantajlı bir alışveriş deneyimi yaşadım.",
                reviewProductPhoto = "link",
                reviewStar = 2
            ),

            // 3
            ReviewEntity(
                reviewProductId = "3",
                reviewName = "Ayşe",
                reviewProfilePhoto = "https://randomuser.me/api/portraits/women/50.jpg",
                reviewDate = "2024-04-15",
                reviewTitle = "Harika bir ürün!",
                reviewContent = "Ürünü aldığımdan beri çok memnunum. Kaliteli malzeme ve hızlı teslimat. Kesinlikle tavsiye ederim.",
                reviewProductPhoto = "link",
                reviewStar = 5
            ),
            ReviewEntity(
                reviewProductId = "3",
                reviewName = "Ali",
                reviewProfilePhoto = "https://static.vecteezy.com/system/resources/thumbnails/009/292/244/small/default-avatar-icon-of-social-media-user-vector.jpg",
                reviewDate = "2024-04-18",
                reviewTitle = "Beklediğim gibi değil",
                reviewContent = "Ürün resimdeki gibi değil. Kalitesiz malzeme ve eksik parçalar var. Dezavantajlı bir alışveriş deneyimi yaşadım.",
                reviewProductPhoto = "link",
                reviewStar = 2
            ),

            // 4
            ReviewEntity(
                reviewProductId = "4",
                reviewName = "Ayşe",
                reviewProfilePhoto = "https://randomuser.me/api/portraits/women/50.jpg",
                reviewDate = "2024-04-15",
                reviewTitle = "Harika bir ürün!",
                reviewContent = "Ürünü aldığımdan beri çok memnunum. Kaliteli malzeme ve hızlı teslimat. Kesinlikle tavsiye ederim.",
                reviewProductPhoto = "link",
                reviewStar = 5
            ),
            ReviewEntity(
                reviewProductId = "4",
                reviewName = "Ali",
                reviewProfilePhoto = "https://static.vecteezy.com/system/resources/thumbnails/009/292/244/small/default-avatar-icon-of-social-media-user-vector.jpg",
                reviewDate = "2024-04-18",
                reviewTitle = "Beklediğim gibi değil",
                reviewContent = "Ürün resimdeki gibi değil. Kalitesiz malzeme ve eksik parçalar var. Dezavantajlı bir alışveriş deneyimi yaşadım.",
                reviewProductPhoto = "link",
                reviewStar = 2
            ),

            // 7
            ReviewEntity(
                reviewProductId = "7",
                reviewName = "Ayşe",
                reviewProfilePhoto = "https://randomuser.me/api/portraits/women/50.jpg",
                reviewDate = "2024-04-15",
                reviewTitle = "Harika bir ürün!",
                reviewContent = "Ürünü aldığımdan beri çok memnunum. Kaliteli malzeme ve hızlı teslimat. Kesinlikle tavsiye ederim.",
                reviewProductPhoto = "link",
                reviewStar = 5
            ),
            ReviewEntity(
                reviewProductId = "7",
                reviewName = "Ali",
                reviewProfilePhoto = "https://static.vecteezy.com/system/resources/thumbnails/009/292/244/small/default-avatar-icon-of-social-media-user-vector.jpg",
                reviewDate = "2024-04-18",
                reviewTitle = "Beklediğim gibi değil",
                reviewContent = "Ürün resimdeki gibi değil. Kalitesiz malzeme ve eksik parçalar var. Dezavantajlı bir alışveriş deneyimi yaşadım.",
                reviewProductPhoto = "link",
                reviewStar = 2
            ),

            // 8
            ReviewEntity(
                reviewProductId = "8",
                reviewName = "Ayşe",
                reviewProfilePhoto = "https://randomuser.me/api/portraits/women/50.jpg",
                reviewDate = "2024-04-15",
                reviewTitle = "Harika bir ürün!",
                reviewContent = "Ürünü aldığımdan beri çok memnunum. Kaliteli malzeme ve hızlı teslimat. Kesinlikle tavsiye ederim.",
                reviewProductPhoto = "link",
                reviewStar = 5
            ),
            ReviewEntity(
                reviewProductId = "8",
                reviewName = "Ali",
                reviewProfilePhoto = "https://static.vecteezy.com/system/resources/thumbnails/009/292/244/small/default-avatar-icon-of-social-media-user-vector.jpg",
                reviewDate = "2024-04-18",
                reviewTitle = "Beklediğim gibi değil",
                reviewContent = "Ürün resimdeki gibi değil. Kalitesiz malzeme ve eksik parçalar var. Dezavantajlı bir alışveriş deneyimi yaşadım.",
                reviewProductPhoto = "link",
                reviewStar = 2
            ),

            // 9
            ReviewEntity(
                reviewProductId = "9",
                reviewName = "Ayşe",
                reviewProfilePhoto = "https://randomuser.me/api/portraits/women/50.jpg",
                reviewDate = "2024-04-15",
                reviewTitle = "Harika bir ürün!",
                reviewContent = "Ürünü aldığımdan beri çok memnunum. Kaliteli malzeme ve hızlı teslimat. Kesinlikle tavsiye ederim.",
                reviewProductPhoto = "link",
                reviewStar = 5
            ),
            ReviewEntity(
                reviewProductId = "9",
                reviewName = "Ali",
                reviewProfilePhoto = "https://static.vecteezy.com/system/resources/thumbnails/009/292/244/small/default-avatar-icon-of-social-media-user-vector.jpg",
                reviewDate = "2024-04-18",
                reviewTitle = "Beklediğim gibi değil",
                reviewContent = "Ürün resimdeki gibi değil. Kalitesiz malzeme ve eksik parçalar var. Dezavantajlı bir alışveriş deneyimi yaşadım.",
                reviewProductPhoto = "link",
                reviewStar = 2
            ),

            // 10
            ReviewEntity(
                reviewProductId = "10",
                reviewName = "Ayşe",
                reviewProfilePhoto = "https://randomuser.me/api/portraits/women/50.jpg",
                reviewDate = "2024-04-15",
                reviewTitle = "Harika bir ürün!",
                reviewContent = "Ürünü aldığımdan beri çok memnunum. Kaliteli malzeme ve hızlı teslimat. Kesinlikle tavsiye ederim.",
                reviewProductPhoto = "link",
                reviewStar = 5
            ),
            ReviewEntity(
                reviewProductId = "10",
                reviewName = "Ali",
                reviewProfilePhoto = "https://static.vecteezy.com/system/resources/thumbnails/009/292/244/small/default-avatar-icon-of-social-media-user-vector.jpg",
                reviewDate = "2024-04-18",
                reviewTitle = "Beklediğim gibi değil",
                reviewContent = "Ürün resimdeki gibi değil. Kalitesiz malzeme ve eksik parçalar var. Dezavantajlı bir alışveriş deneyimi yaşadım.",
                reviewProductPhoto = "link",
                reviewStar = 2
            ),

            // 11
            ReviewEntity(
                reviewProductId = "11",
                reviewName = "Ayşe",
                reviewProfilePhoto = "https://randomuser.me/api/portraits/women/50.jpg",
                reviewDate = "2024-04-15",
                reviewTitle = "Harika bir ürün!",
                reviewContent = "Ürünü aldığımdan beri çok memnunum. Kaliteli malzeme ve hızlı teslimat. Kesinlikle tavsiye ederim.",
                reviewProductPhoto = "link",
                reviewStar = 5
            ),
            ReviewEntity(
                reviewProductId = "11",
                reviewName = "Ali",
                reviewProfilePhoto = "https://static.vecteezy.com/system/resources/thumbnails/009/292/244/small/default-avatar-icon-of-social-media-user-vector.jpg",
                reviewDate = "2024-04-18",
                reviewTitle = "Beklediğim gibi değil",
                reviewContent = "Ürün resimdeki gibi değil. Kalitesiz malzeme ve eksik parçalar var. Dezavantajlı bir alışveriş deneyimi yaşadım.",
                reviewProductPhoto = "link",
                reviewStar = 2
            ),

            // 12
            ReviewEntity(
                reviewProductId = "12",
                reviewName = "Ayşe",
                reviewProfilePhoto = "https://randomuser.me/api/portraits/women/50.jpg",
                reviewDate = "2024-04-15",
                reviewTitle = "Harika bir ürün!",
                reviewContent = "Ürünü aldığımdan beri çok memnunum. Kaliteli malzeme ve hızlı teslimat. Kesinlikle tavsiye ederim.",
                reviewProductPhoto = "link",
                reviewStar = 5
            ),
            ReviewEntity(
                reviewProductId = "12",
                reviewName = "Ali",
                reviewProfilePhoto = "https://static.vecteezy.com/system/resources/thumbnails/009/292/244/small/default-avatar-icon-of-social-media-user-vector.jpg",
                reviewDate = "2024-04-18",
                reviewTitle = "Beklediğim gibi değil",
                reviewContent = "Ürün resimdeki gibi değil. Kalitesiz malzeme ve eksik parçalar var. Dezavantajlı bir alışveriş deneyimi yaşadım.",
                reviewProductPhoto = "link",
                reviewStar = 2
            ),

            // 13
            ReviewEntity(
                reviewProductId = "13",
                reviewName = "Ayşe",
                reviewProfilePhoto = "https://randomuser.me/api/portraits/women/50.jpg",
                reviewDate = "2024-04-15",
                reviewTitle = "Harika bir ürün!",
                reviewContent = "Ürünü aldığımdan beri çok memnunum. Kaliteli malzeme ve hızlı teslimat. Kesinlikle tavsiye ederim.",
                reviewProductPhoto = "link",
                reviewStar = 5
            ),
            ReviewEntity(
                reviewProductId = "13",
                reviewName = "Ali",
                reviewProfilePhoto = "https://static.vecteezy.com/system/resources/thumbnails/009/292/244/small/default-avatar-icon-of-social-media-user-vector.jpg",
                reviewDate = "2024-04-18",
                reviewTitle = "Beklediğim gibi değil",
                reviewContent = "Ürün resimdeki gibi değil. Kalitesiz malzeme ve eksik parçalar var. Dezavantajlı bir alışveriş deneyimi yaşadım.",
                reviewProductPhoto = "link",
                reviewStar = 2
            ),

            // 14
            ReviewEntity(
                reviewProductId = "14",
                reviewName = "Ayşe",
                reviewProfilePhoto = "https://randomuser.me/api/portraits/women/50.jpg",
                reviewDate = "2024-04-15",
                reviewTitle = "Harika bir ürün!",
                reviewContent = "Ürünü aldığımdan beri çok memnunum. Kaliteli malzeme ve hızlı teslimat. Kesinlikle tavsiye ederim.",
                reviewProductPhoto = "link",
                reviewStar = 5
            ),
            ReviewEntity(
                reviewProductId = "14",
                reviewName = "Ali",
                reviewProfilePhoto = "https://static.vecteezy.com/system/resources/thumbnails/009/292/244/small/default-avatar-icon-of-social-media-user-vector.jpg",
                reviewDate = "2024-04-18",
                reviewTitle = "Beklediğim gibi değil",
                reviewContent = "Ürün resimdeki gibi değil. Kalitesiz malzeme ve eksik parçalar var. Dezavantajlı bir alışveriş deneyimi yaşadım.",
                reviewProductPhoto = "link",
                reviewStar = 2
            ),

            // 15
            ReviewEntity(
                reviewProductId = "15",
                reviewName = "Ayşe",
                reviewProfilePhoto = "https://randomuser.me/api/portraits/women/50.jpg",
                reviewDate = "2024-04-15",
                reviewTitle = "Harika bir ürün!",
                reviewContent = "Ürünü aldığımdan beri çok memnunum. Kaliteli malzeme ve hızlı teslimat. Kesinlikle tavsiye ederim.",
                reviewProductPhoto = "link",
                reviewStar = 5
            ),
            ReviewEntity(
                reviewProductId = "15",
                reviewName = "Ali",
                reviewProfilePhoto = "https://static.vecteezy.com/system/resources/thumbnails/009/292/244/small/default-avatar-icon-of-social-media-user-vector.jpg",
                reviewDate = "2024-04-18",
                reviewTitle = "Beklediğim gibi değil",
                reviewContent = "Ürün resimdeki gibi değil. Kalitesiz malzeme ve eksik parçalar var. Dezavantajlı bir alışveriş deneyimi yaşadım.",
                reviewProductPhoto = "link",
                reviewStar = 2
            ),

            // 16
            ReviewEntity(
                reviewProductId = "16",
                reviewName = "Ayşe",
                reviewProfilePhoto = "https://randomuser.me/api/portraits/women/50.jpg",
                reviewDate = "2024-04-15",
                reviewTitle = "Harika bir ürün!",
                reviewContent = "Ürünü aldığımdan beri çok memnunum. Kaliteli malzeme ve hızlı teslimat. Kesinlikle tavsiye ederim.",
                reviewProductPhoto = "link",
                reviewStar = 5
            ),
            ReviewEntity(
                reviewProductId = "16",
                reviewName = "Ali",
                reviewProfilePhoto = "https://static.vecteezy.com/system/resources/thumbnails/009/292/244/small/default-avatar-icon-of-social-media-user-vector.jpg",
                reviewDate = "2024-04-18",
                reviewTitle = "Beklediğim gibi değil",
                reviewContent = "Ürün resimdeki gibi değil. Kalitesiz malzeme ve eksik parçalar var. Dezavantajlı bir alışveriş deneyimi yaşadım.",
                reviewProductPhoto = "link",
                reviewStar = 2
            ),

            // 17
            ReviewEntity(
                reviewProductId = "17",
                reviewName = "Ayşe",
                reviewProfilePhoto = "https://randomuser.me/api/portraits/women/50.jpg",
                reviewDate = "2024-04-15",
                reviewTitle = "Harika bir ürün!",
                reviewContent = "Ürünü aldığımdan beri çok memnunum. Kaliteli malzeme ve hızlı teslimat. Kesinlikle tavsiye ederim.",
                reviewProductPhoto = "link",
                reviewStar = 5
            ),
            ReviewEntity(
                reviewProductId = "17",
                reviewName = "Ali",
                reviewProfilePhoto = "https://static.vecteezy.com/system/resources/thumbnails/009/292/244/small/default-avatar-icon-of-social-media-user-vector.jpg",
                reviewDate = "2024-04-18",
                reviewTitle = "Beklediğim gibi değil",
                reviewContent = "Ürün resimdeki gibi değil. Kalitesiz malzeme ve eksik parçalar var. Dezavantajlı bir alışveriş deneyimi yaşadım.",
                reviewProductPhoto = "link",
                reviewStar = 2
            ),

            // 18
            ReviewEntity(
                reviewProductId = "18",
                reviewName = "Ayşe",
                reviewProfilePhoto = "https://randomuser.me/api/portraits/women/50.jpg",
                reviewDate = "2024-04-15",
                reviewTitle = "Harika bir ürün!",
                reviewContent = "Ürünü aldığımdan beri çok memnunum. Kaliteli malzeme ve hızlı teslimat. Kesinlikle tavsiye ederim.",
                reviewProductPhoto = "link",
                reviewStar = 5
            ),
            ReviewEntity(
                reviewProductId = "18",
                reviewName = "Ali",
                reviewProfilePhoto = "https://static.vecteezy.com/system/resources/thumbnails/009/292/244/small/default-avatar-icon-of-social-media-user-vector.jpg",
                reviewDate = "2024-04-18",
                reviewTitle = "Beklediğim gibi değil",
                reviewContent = "Ürün resimdeki gibi değil. Kalitesiz malzeme ve eksik parçalar var. Dezavantajlı bir alışveriş deneyimi yaşadım.",
                reviewProductPhoto = "link",
                reviewStar = 2
            ),

            // 19
            ReviewEntity(
                reviewProductId = "19",
                reviewName = "Ayşe",
                reviewProfilePhoto = "https://randomuser.me/api/portraits/women/50.jpg",
                reviewDate = "2024-04-15",
                reviewTitle = "Harika bir ürün!",
                reviewContent = "Ürünü aldığımdan beri çok memnunum. Kaliteli malzeme ve hızlı teslimat. Kesinlikle tavsiye ederim.",
                reviewProductPhoto = "link",
                reviewStar = 5
            ),
            ReviewEntity(
                reviewProductId = "19",
                reviewName = "Ali",
                reviewProfilePhoto = "https://static.vecteezy.com/system/resources/thumbnails/009/292/244/small/default-avatar-icon-of-social-media-user-vector.jpg",
                reviewDate = "2024-04-18",
                reviewTitle = "Beklediğim gibi değil",
                reviewContent = "Ürün resimdeki gibi değil. Kalitesiz malzeme ve eksik parçalar var. Dezavantajlı bir alışveriş deneyimi yaşadım.",
                reviewProductPhoto = "link",
                reviewStar = 2
            ),

            // 20
            ReviewEntity(
                reviewProductId = "20",
                reviewName = "Ayşe",
                reviewProfilePhoto = "https://randomuser.me/api/portraits/women/50.jpg",
                reviewDate = "2024-04-15",
                reviewTitle = "Harika bir ürün!",
                reviewContent = "Ürünü aldığımdan beri çok memnunum. Kaliteli malzeme ve hızlı teslimat. Kesinlikle tavsiye ederim.",
                reviewProductPhoto = "link",
                reviewStar = 5
            ),
            ReviewEntity(
                reviewProductId = "20",
                reviewName = "Ali",
                reviewProfilePhoto = "https://static.vecteezy.com/system/resources/thumbnails/009/292/244/small/default-avatar-icon-of-social-media-user-vector.jpg",
                reviewDate = "2024-04-18",
                reviewTitle = "Beklediğim gibi değil",
                reviewContent = "Ürün resimdeki gibi değil. Kalitesiz malzeme ve eksik parçalar var. Dezavantajlı bir alışveriş deneyimi yaşadım.",
                reviewProductPhoto = "link",
                reviewStar = 2
            ),

            // 21
            ReviewEntity(
                reviewProductId = "21",
                reviewName = "Ayşe",
                reviewProfilePhoto = "https://randomuser.me/api/portraits/women/50.jpg",
                reviewDate = "2024-04-15",
                reviewTitle = "Harika bir ürün!",
                reviewContent = "Ürünü aldığımdan beri çok memnunum. Kaliteli malzeme ve hızlı teslimat. Kesinlikle tavsiye ederim.",
                reviewProductPhoto = "link",
                reviewStar = 5
            ),
            ReviewEntity(
                reviewProductId = "21",
                reviewName = "Ali",
                reviewProfilePhoto = "https://static.vecteezy.com/system/resources/thumbnails/009/292/244/small/default-avatar-icon-of-social-media-user-vector.jpg",
                reviewDate = "2024-04-18",
                reviewTitle = "Beklediğim gibi değil",
                reviewContent = "Ürün resimdeki gibi değil. Kalitesiz malzeme ve eksik parçalar var. Dezavantajlı bir alışveriş deneyimi yaşadım.",
                reviewProductPhoto = "link",
                reviewStar = 2
            ),

            // 22
            ReviewEntity(
                reviewProductId = "22",
                reviewName = "Ayşe",
                reviewProfilePhoto = "https://randomuser.me/api/portraits/women/50.jpg",
                reviewDate = "2024-04-15",
                reviewTitle = "Harika bir ürün!",
                reviewContent = "Ürünü aldığımdan beri çok memnunum. Kaliteli malzeme ve hızlı teslimat. Kesinlikle tavsiye ederim.",
                reviewProductPhoto = "link",
                reviewStar = 5
            ),
            ReviewEntity(
                reviewProductId = "22",
                reviewName = "Ali",
                reviewProfilePhoto = "https://static.vecteezy.com/system/resources/thumbnails/009/292/244/small/default-avatar-icon-of-social-media-user-vector.jpg",
                reviewDate = "2024-04-18",
                reviewTitle = "Beklediğim gibi değil",
                reviewContent = "Ürün resimdeki gibi değil. Kalitesiz malzeme ve eksik parçalar var. Dezavantajlı bir alışveriş deneyimi yaşadım.",
                reviewProductPhoto = "link",
                reviewStar = 2
            ),

            )
    }
}