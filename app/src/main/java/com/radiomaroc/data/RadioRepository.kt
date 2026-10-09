package com.radiomaroc.data

object RadioRepository {

    val moroccanStations: List<RadioStation> = listOf(
        RadioStation(
            id = "abir",
            name = "Radio Abir",
            city = "المغرب",
            category = "Tarab Live",
            streamUrl = "https://stream.zeno.fm/bqdbb6hd0neuv",
            logoUrl = "https://e.top4top.io/p_37111e2wc1.jpg"
        ),
        RadioStation(
            id = "aswat",
            name = "Radio Aswat",
            city = "المغرب",
            category = "News Direct",
            streamUrl = "https://aswat.ice.infomaniak.ch/aswat-high.mp3",
            logoUrl = "https://h.top4top.io/p_3711b4ywo1.jpg"
        ),
        RadioStation(
            id = "mcd",
            name = "MCD RADIO",
            city = "المغرب",
            category = "Live Stream",
            streamUrl = "https://montecarlodoualiya128k.ice.infomaniak.ch/mc-doualiya.mp3",
            logoUrl = "https://j.top4top.io/p_3898yy2lm0.png"
        ),
        RadioStation(
            id = "plus",
            name = "Radio plus casablanca",
            city = "المغرب",
            category = "Sports Live",
            streamUrl = "https://hosting.studioradiomedia.fr:3040/stream",
            logoUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRqrgJ3J0gBi2EPF7p7VBHpyPpOA0wp2uo-JbQJtqHv1A&s=10"
        ),
        RadioStation(
            id = "med",
            name = "Med radio",
            city = "المغرب",
            category = "Music Live",
            streamUrl = "https://medradio.ice.infomaniak.ch/medradio-128.mp3",
            logoUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRy6cT5jVYaYD_AWmy5BygDWY3mjva8hJduxPXbW4t3PA&s=10"
        ),
        RadioStation(
            id = "atlantic",
            name = "Atlantic Radio",
            city = "المغرب",
            category = "Economic",
            streamUrl = "https://atlantic-sonic.nindohost.net:9300/stream",
            logoUrl = "https://f.top4top.io/p_3711q65ph0.jpg"
        ),
        RadioStation(
            id = "medina",
            name = "Medina FM",
            city = "المغرب",
            category = "Heritage",
            streamUrl = "https://listen.radioking.com/radio/710810/stream/776366",
            logoUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTIhlyp5zFNNXe73R15ayZJE9fwgXMTGAwOfdIMlHYscQ&s=10"
        ),
        RadioStation(
            id = "roqiah",
            name = "الرقية الشرعية",
            city = "المغرب",
            category = "Quran",
            streamUrl = "https://qurango.net/radio/roqiah",
            logoUrl = "https://k.top4top.io/p_37110rm440.jpg"
        ),
        RadioStation(
            id = "quran",
            name = "القرآن الكريم",
            city = "المغرب",
            category = "Quran 24/7",
            streamUrl = "https://backup.qurango.net/radio/shaik_abu_bakr_al_shatri",
            logoUrl = "https://l.top4top.io/p_3711mmrw80.jpg"
        ),
        RadioStation(
            id = "fatwa1",
            name = "الفتاوى",
            city = "المغرب",
            category = "Quran 24/7",
            streamUrl = "https://backup.qurango.net/radio/fatwa/",
            logoUrl = "https://fatwa-qa.com/qa-theme/Legacy/images/favicons/apple-touch-icon.png"
        ),
        RadioStation(
            id = "fatwa2",
            name = "الفتاوى",
            city = "المغرب",
            category = "Quran 24/7",
            streamUrl = "https://backup.qurango.net/radio/alaikhtiarat_alfiqhayh_bin_baz/",
            logoUrl = "https://m.media-amazon.com/images/I/71XZ85qFWyL.png"
        ),
        RadioStation(
            id = "seerah",
            name = "السيرة والقصص",
            city = "المغرب",
            category = "Quran 24/7",
            streamUrl = "https://radio.garden/api/ara/content/listen/GQxvGBNK/channel.mp3?hl=fr&1788448157457",
            logoUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT7dy-UF1K0R-DdRtBifULl0c4HRZsd45YLbGlZ43cfUrJ96Jasf46rgySt&s=10"
        ),
        RadioStation(
            id = "shuraim",
            name = "القرآن شريم",
            city = "المغرب",
            category = "Quran 24/7",
            streamUrl = "https://backup.qurango.net/radio/saud_alshuraim/",
            logoUrl = "https://l.top4top.io/p_3711mmrw80.jpg"
        ),
    )

    val cities: List<String> = listOf("الكل") + moroccanStations.map { it.city }.distinct()
}
