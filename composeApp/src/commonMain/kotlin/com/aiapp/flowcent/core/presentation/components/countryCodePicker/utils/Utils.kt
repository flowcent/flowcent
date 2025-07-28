package com.aiapp.flowcent.core.presentation.components.countryCodePicker.utils

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import com.aiapp.flowcent.core.presentation.components.countryCodePicker.model.CountryDetails
import flowcent.composeapp.generated.resources.Res
import flowcent.composeapp.generated.resources.ad
import flowcent.composeapp.generated.resources.ae
import flowcent.composeapp.generated.resources.af
import flowcent.composeapp.generated.resources.ag
import flowcent.composeapp.generated.resources.ai
import flowcent.composeapp.generated.resources.al
import flowcent.composeapp.generated.resources.am
import flowcent.composeapp.generated.resources.american_samoa
import flowcent.composeapp.generated.resources.ao
import flowcent.composeapp.generated.resources.aq
import flowcent.composeapp.generated.resources.ar
import flowcent.composeapp.generated.resources.at
import flowcent.composeapp.generated.resources.au
import flowcent.composeapp.generated.resources.aw
import flowcent.composeapp.generated.resources.ax
import flowcent.composeapp.generated.resources.az
import flowcent.composeapp.generated.resources.ba
import flowcent.composeapp.generated.resources.bb
import flowcent.composeapp.generated.resources.bd
import flowcent.composeapp.generated.resources.be
import flowcent.composeapp.generated.resources.bf
import flowcent.composeapp.generated.resources.bg
import flowcent.composeapp.generated.resources.bh
import flowcent.composeapp.generated.resources.bi
import flowcent.composeapp.generated.resources.bj
import flowcent.composeapp.generated.resources.bl
import flowcent.composeapp.generated.resources.bm
import flowcent.composeapp.generated.resources.bn
import flowcent.composeapp.generated.resources.bo
import flowcent.composeapp.generated.resources.br
import flowcent.composeapp.generated.resources.bs
import flowcent.composeapp.generated.resources.bt
import flowcent.composeapp.generated.resources.bw
import flowcent.composeapp.generated.resources.by
import flowcent.composeapp.generated.resources.bz
import flowcent.composeapp.generated.resources.ca
import flowcent.composeapp.generated.resources.cc
import flowcent.composeapp.generated.resources.cd
import flowcent.composeapp.generated.resources.cf
import flowcent.composeapp.generated.resources.cg
import flowcent.composeapp.generated.resources.ch
import flowcent.composeapp.generated.resources.ci
import flowcent.composeapp.generated.resources.ck
import flowcent.composeapp.generated.resources.cl
import flowcent.composeapp.generated.resources.cm
import flowcent.composeapp.generated.resources.cn
import flowcent.composeapp.generated.resources.co
import flowcent.composeapp.generated.resources.cr
import flowcent.composeapp.generated.resources.cu
import flowcent.composeapp.generated.resources.cv
import flowcent.composeapp.generated.resources.cw
import flowcent.composeapp.generated.resources.cx
import flowcent.composeapp.generated.resources.cy
import flowcent.composeapp.generated.resources.cz
import flowcent.composeapp.generated.resources.de
import flowcent.composeapp.generated.resources.dj
import flowcent.composeapp.generated.resources.dk
import flowcent.composeapp.generated.resources.dm
import flowcent.composeapp.generated.resources.dz
import flowcent.composeapp.generated.resources.ec
import flowcent.composeapp.generated.resources.ee
import flowcent.composeapp.generated.resources.eg
import flowcent.composeapp.generated.resources.er
import flowcent.composeapp.generated.resources.es
import flowcent.composeapp.generated.resources.et
import flowcent.composeapp.generated.resources.fi
import flowcent.composeapp.generated.resources.fj
import flowcent.composeapp.generated.resources.fk
import flowcent.composeapp.generated.resources.fm
import flowcent.composeapp.generated.resources.fo
import flowcent.composeapp.generated.resources.fr
import flowcent.composeapp.generated.resources.ga
import flowcent.composeapp.generated.resources.gb
import flowcent.composeapp.generated.resources.gd
import flowcent.composeapp.generated.resources.ge
import flowcent.composeapp.generated.resources.gf
import flowcent.composeapp.generated.resources.gh
import flowcent.composeapp.generated.resources.gi
import flowcent.composeapp.generated.resources.gl
import flowcent.composeapp.generated.resources.gm
import flowcent.composeapp.generated.resources.gn
import flowcent.composeapp.generated.resources.gp
import flowcent.composeapp.generated.resources.gq
import flowcent.composeapp.generated.resources.gr
import flowcent.composeapp.generated.resources.gt
import flowcent.composeapp.generated.resources.gu
import flowcent.composeapp.generated.resources.gw
import flowcent.composeapp.generated.resources.gy
import flowcent.composeapp.generated.resources.hk
import flowcent.composeapp.generated.resources.hn
import flowcent.composeapp.generated.resources.hr
import flowcent.composeapp.generated.resources.ht
import flowcent.composeapp.generated.resources.hu
import flowcent.composeapp.generated.resources.ic_do
import flowcent.composeapp.generated.resources.iceland
import flowcent.composeapp.generated.resources.id
import flowcent.composeapp.generated.resources.ie
import flowcent.composeapp.generated.resources.il
import flowcent.composeapp.generated.resources.im
import flowcent.composeapp.generated.resources.india
import flowcent.composeapp.generated.resources.io
import flowcent.composeapp.generated.resources.iq
import flowcent.composeapp.generated.resources.ir
import flowcent.composeapp.generated.resources.it
import flowcent.composeapp.generated.resources.je
import flowcent.composeapp.generated.resources.jm
import flowcent.composeapp.generated.resources.jo
import flowcent.composeapp.generated.resources.jp
import flowcent.composeapp.generated.resources.ke
import flowcent.composeapp.generated.resources.kg
import flowcent.composeapp.generated.resources.kh
import flowcent.composeapp.generated.resources.ki
import flowcent.composeapp.generated.resources.km
import flowcent.composeapp.generated.resources.kn
import flowcent.composeapp.generated.resources.kp
import flowcent.composeapp.generated.resources.kr
import flowcent.composeapp.generated.resources.kw
import flowcent.composeapp.generated.resources.ky
import flowcent.composeapp.generated.resources.kz
import flowcent.composeapp.generated.resources.la
import flowcent.composeapp.generated.resources.lb
import flowcent.composeapp.generated.resources.lc
import flowcent.composeapp.generated.resources.li
import flowcent.composeapp.generated.resources.lk
import flowcent.composeapp.generated.resources.lr
import flowcent.composeapp.generated.resources.ls
import flowcent.composeapp.generated.resources.lt
import flowcent.composeapp.generated.resources.lu
import flowcent.composeapp.generated.resources.lv
import flowcent.composeapp.generated.resources.ly
import flowcent.composeapp.generated.resources.ma
import flowcent.composeapp.generated.resources.mc
import flowcent.composeapp.generated.resources.md
import flowcent.composeapp.generated.resources.me
import flowcent.composeapp.generated.resources.mf
import flowcent.composeapp.generated.resources.mg
import flowcent.composeapp.generated.resources.mh
import flowcent.composeapp.generated.resources.mk
import flowcent.composeapp.generated.resources.ml
import flowcent.composeapp.generated.resources.mm
import flowcent.composeapp.generated.resources.mn
import flowcent.composeapp.generated.resources.mo
import flowcent.composeapp.generated.resources.mp
import flowcent.composeapp.generated.resources.mq
import flowcent.composeapp.generated.resources.mr
import flowcent.composeapp.generated.resources.ms
import flowcent.composeapp.generated.resources.mt
import flowcent.composeapp.generated.resources.mu
import flowcent.composeapp.generated.resources.mv
import flowcent.composeapp.generated.resources.mw
import flowcent.composeapp.generated.resources.mx
import flowcent.composeapp.generated.resources.my
import flowcent.composeapp.generated.resources.mz
import flowcent.composeapp.generated.resources.na
import flowcent.composeapp.generated.resources.nc
import flowcent.composeapp.generated.resources.ne
import flowcent.composeapp.generated.resources.nf
import flowcent.composeapp.generated.resources.ng
import flowcent.composeapp.generated.resources.ni
import flowcent.composeapp.generated.resources.nl
import flowcent.composeapp.generated.resources.no
import flowcent.composeapp.generated.resources.np
import flowcent.composeapp.generated.resources.nr
import flowcent.composeapp.generated.resources.nu
import flowcent.composeapp.generated.resources.nz
import flowcent.composeapp.generated.resources.om
import flowcent.composeapp.generated.resources.pa
import flowcent.composeapp.generated.resources.pe
import flowcent.composeapp.generated.resources.pf
import flowcent.composeapp.generated.resources.pg
import flowcent.composeapp.generated.resources.ph
import flowcent.composeapp.generated.resources.pk
import flowcent.composeapp.generated.resources.pl
import flowcent.composeapp.generated.resources.pm
import flowcent.composeapp.generated.resources.pn
import flowcent.composeapp.generated.resources.pr
import flowcent.composeapp.generated.resources.ps
import flowcent.composeapp.generated.resources.pt
import flowcent.composeapp.generated.resources.pw
import flowcent.composeapp.generated.resources.py
import flowcent.composeapp.generated.resources.qa
import flowcent.composeapp.generated.resources.re
import flowcent.composeapp.generated.resources.ro
import flowcent.composeapp.generated.resources.rs
import flowcent.composeapp.generated.resources.ru
import flowcent.composeapp.generated.resources.rw
import flowcent.composeapp.generated.resources.sa
import flowcent.composeapp.generated.resources.sb
import flowcent.composeapp.generated.resources.sc
import flowcent.composeapp.generated.resources.sd
import flowcent.composeapp.generated.resources.se
import flowcent.composeapp.generated.resources.sg
import flowcent.composeapp.generated.resources.sh
import flowcent.composeapp.generated.resources.si
import flowcent.composeapp.generated.resources.sk
import flowcent.composeapp.generated.resources.sl
import flowcent.composeapp.generated.resources.sm
import flowcent.composeapp.generated.resources.sn
import flowcent.composeapp.generated.resources.so
import flowcent.composeapp.generated.resources.sr
import flowcent.composeapp.generated.resources.ss
import flowcent.composeapp.generated.resources.st
import flowcent.composeapp.generated.resources.sv
import flowcent.composeapp.generated.resources.sx
import flowcent.composeapp.generated.resources.sy
import flowcent.composeapp.generated.resources.sz
import flowcent.composeapp.generated.resources.tc
import flowcent.composeapp.generated.resources.td
import flowcent.composeapp.generated.resources.tg
import flowcent.composeapp.generated.resources.th
import flowcent.composeapp.generated.resources.tj
import flowcent.composeapp.generated.resources.tk
import flowcent.composeapp.generated.resources.tl
import flowcent.composeapp.generated.resources.tm
import flowcent.composeapp.generated.resources.tn
import flowcent.composeapp.generated.resources.to
import flowcent.composeapp.generated.resources.tr
import flowcent.composeapp.generated.resources.tt
import flowcent.composeapp.generated.resources.tv
import flowcent.composeapp.generated.resources.tw
import flowcent.composeapp.generated.resources.tz
import flowcent.composeapp.generated.resources.ua
import flowcent.composeapp.generated.resources.ug
import flowcent.composeapp.generated.resources.us
import flowcent.composeapp.generated.resources.uy
import flowcent.composeapp.generated.resources.uz
import flowcent.composeapp.generated.resources.va
import flowcent.composeapp.generated.resources.vc
import flowcent.composeapp.generated.resources.ve
import flowcent.composeapp.generated.resources.vg
import flowcent.composeapp.generated.resources.vi
import flowcent.composeapp.generated.resources.vn
import flowcent.composeapp.generated.resources.vu
import flowcent.composeapp.generated.resources.wf
import flowcent.composeapp.generated.resources.ws
import flowcent.composeapp.generated.resources.xk
import flowcent.composeapp.generated.resources.ye
import flowcent.composeapp.generated.resources.yt
import flowcent.composeapp.generated.resources.za
import flowcent.composeapp.generated.resources.zm
import flowcent.composeapp.generated.resources.zw

internal object Utils {

    fun searchCountry(
        searchStr: String,
        countriesList: List<CountryDetails>,
    ): List<CountryDetails> {
        return countriesList.filter {
            it.countryName.contains(searchStr, ignoreCase = true)
                    || it.countryPhoneNumberCode.contains(searchStr, ignoreCase = true)
                    || it.countryCode.contains(searchStr, ignoreCase = true)
        }
    }

    fun getCountryFromCountryCode(
        countryCode: String,
        countriesList: List<CountryDetails>
    ): CountryDetails {
        return countriesList.single { it.countryCode == countryCode }
    }

    fun getCountryList(): List<CountryDetails> = listOf(
        CountryDetails(
            countryCode = "ad",
            "+376",
            "Andorra",
            Res.drawable.ad,
        ),
        CountryDetails(
            countryCode = "ae",
            "+971",
            "United Arab Emirates (UAE)",
            Res.drawable.ae,
        ),
        CountryDetails(
            countryCode = "af",
            "+93",
            "Afghanistan",
            Res.drawable.af,
        ),
        CountryDetails(
            countryCode = "ag",
            "+1",
            "Antigua and Barbuda",
            Res.drawable.ag,
        ),
        CountryDetails(
            "ai",
            "+1",
            "Anguilla",
            Res.drawable.ai,
        ),
        CountryDetails(
            "al",
            "+355",
            "Albania",
            Res.drawable.al,
        ),
        CountryDetails(
            "am",
            "+374",
            "Armenia",
            Res.drawable.am,
        ),
        CountryDetails(
            "ao",
            "+244",
            "Angola",
            Res.drawable.ao,
        ),
        CountryDetails(
            "aq",
            "+672",
            "Antarctica",
            Res.drawable.aq,
        ),
        CountryDetails(
            "ar",
            "+54",
            "Argentina",
            Res.drawable.ar,
        ),
        CountryDetails(
            "as",
            "+1",
            "American Samoa",
            Res.drawable.american_samoa
        ),
        CountryDetails(
            "at",
            "+43",
            "Austria",
            Res.drawable.at,
        ),
        CountryDetails(
            "au",
            "+61",
            "Australia",
            Res.drawable.au,
        ),
        CountryDetails(
            "aw",
            "+297",
            "Aruba",
            Res.drawable.aw,
        ),
        CountryDetails(
            "ax",
            "+358",
            "Åland Islands",
            Res.drawable.ax
        ),
        CountryDetails(
            "az",
            "+994",
            "Azerbaijan",
            Res.drawable.az
        ),
        CountryDetails(
            "ba",
            "+387",
            "Bosnia And Herzegovina",
            Res.drawable.ba
        ),
        CountryDetails(
            "bb",
            "+1",
            "Barbados",
            Res.drawable.bb
        ),
        CountryDetails(
            "bd",
            "+880",
            "Bangladesh",
            Res.drawable.bd
        ),
        CountryDetails(
            "be",
            "+32",
            "Belgium",
            Res.drawable.be
        ),
        CountryDetails(
            "bf",
            "+226",
            "Burkina Faso",
            Res.drawable.bf
        ),
        CountryDetails(
            "bg",
            "+359",
            "Bulgaria",
            Res.drawable.bg
        ),
        CountryDetails(
            "bh",
            "+973",
            "Bahrain",
            Res.drawable.bh
        ),
        CountryDetails(
            "bi",
            "+257",
            "Burundi",
            Res.drawable.bi
        ),
        CountryDetails(
            "bj",
            "+229",
            "Benin",
            Res.drawable.bj
        ),
        CountryDetails(
            "bl",
            "+590",
            "Saint Barthélemy",
            Res.drawable.bl
        ),
        CountryDetails(
            "bm",
            "+1",
            "Bermuda",
            Res.drawable.bm
        ),
        CountryDetails(
            "bn",
            "+673",
            "Brunei Darussalam",
            Res.drawable.bn
        ),
        CountryDetails(
            "bo",
            "+591",
            "Bolivia, Plurinational State Of",
            Res.drawable.bo
        ),
        CountryDetails(
            "br",
            "+55",
            "Brazil",
            Res.drawable.br
        ),
        CountryDetails(
            "bs",
            "+1",
            "Bahamas",
            Res.drawable.bs
        ),
        CountryDetails(
            "bt",
            "+975",
            "Bhutan",
            Res.drawable.bt
        ),
        CountryDetails(
            "bw",
            "+267",
            "Botswana",
            Res.drawable.bw
        ),
        CountryDetails(
            "by",
            "+375",
            "Belarus",
            Res.drawable.by
        ),
        CountryDetails(
            "bz",
            "+501",
            "Belize",
            Res.drawable.bz
        ),
        CountryDetails(
            "ca",
            "+1",
            "Canada",
            Res.drawable.ca
        ),
        CountryDetails(
            "cc",
            "+61",
            "Cocos (keeling) Islands",
            Res.drawable.cc
        ),
        CountryDetails(
            "cd",
            "+243",
            "Congo, The Democratic Republic Of The",
            Res.drawable.cd
        ),
        CountryDetails(
            "cf",
            "+236",
            "Central African Republic",
            Res.drawable.cf
        ),
        CountryDetails(
            "cg",
            "+242",
            "Congo",
            Res.drawable.cg
        ),
        CountryDetails(
            "ch",
            "+41",
            "Switzerland",
            Res.drawable.ch
        ),
        CountryDetails(
            "ci",
            "+225",
            "Côte D'ivoire",
            Res.drawable.ci
        ),
        CountryDetails(
            "ck",
            "+682",
            "Cook Islands",
            Res.drawable.ck
        ),
        CountryDetails(
            "cl",
            "+56",
            "Chile",
            Res.drawable.cl
        ),
        CountryDetails(
            "cm",
            "+237",
            "Cameroon",
            Res.drawable.cm
        ),
        CountryDetails(
            "cn",
            "+86",
            "China",
            Res.drawable.cn
        ),
        CountryDetails(
            "co",
            "+57",
            "Colombia",
            Res.drawable.co
        ),
        CountryDetails(
            "cr",
            "+506",
            "Costa Rica",
            Res.drawable.cr
        ),
        CountryDetails(
            "cu",
            "+53",
            "Cuba",
            Res.drawable.cu
        ),
        CountryDetails(
            "cv",
            "+238",
            "Cape Verde",
            Res.drawable.cv
        ),
        CountryDetails(
            "cw",
            "+599",
            "Curaçao",
            Res.drawable.cw
        ),
        CountryDetails(
            "cx",
            "+61",
            "Christmas Island",
            Res.drawable.cx
        ),
        CountryDetails(
            "cy",
            "+357",
            "Cyprus",
            Res.drawable.cy
        ),
        CountryDetails(
            "cz",
            "+420",
            "Czech Republic",
            Res.drawable.cz
        ),
        CountryDetails(
            "de",
            "+49",
            "Germany",
            Res.drawable.de
        ),
        CountryDetails(
            "dj",
            "+253",
            "Djibouti",
            Res.drawable.dj
        ),
        CountryDetails(
            "dk",
            "+45",
            "Denmark",
            Res.drawable.dk
        ),
        CountryDetails(
            "dm",
            "+1",
            "Dominica",
            Res.drawable.dm
        ),
        CountryDetails(
            "do",
            "+1",
            "Dominican Republic",
            Res.drawable.ic_do
        ),
        CountryDetails(
            "dz",
            "+213",
            "Algeria",
            Res.drawable.dz
        ),
        CountryDetails(
            "ec",
            "+593",
            "Ecuador",
            Res.drawable.ec
        ),
        CountryDetails(
            "ee",
            "+372",
            "Estonia",
            Res.drawable.ee
        ),
        CountryDetails(
            "eg",
            "+20",
            "Egypt",
            Res.drawable.eg
        ),
        CountryDetails(
            "er",
            "+291",
            "Eritrea",
            Res.drawable.er
        ),
        CountryDetails(
            "es",
            "+34",
            "Spain",
            Res.drawable.es
        ),
        CountryDetails(
            "et",
            "+251",
            "Ethiopia",
            Res.drawable.et
        ),
        CountryDetails(
            "fi",
            "+358",
            "Finland",
            Res.drawable.fi
        ),
        CountryDetails(
            "fj",
            "+679",
            "Fiji",
            Res.drawable.fj
        ),
        CountryDetails(
            "fk",
            "+500",
            "Falkland Islands (Malvinas)",
            Res.drawable.fk
        ),
        CountryDetails(
            "fm",
            "+691",
            "Micronesia, Federated States Of",
            Res.drawable.fm
        ),
        CountryDetails(
            "fo",
            "+298",
            "Faroe Islands",
            Res.drawable.fo
        ),
        CountryDetails(
            "fr",
            "+33",
            "France",
            Res.drawable.fr
        ),
        CountryDetails(
            "ga",
            "+241",
            "Gabon",
            Res.drawable.ga
        ),
        CountryDetails(
            "gb",
            "+44",
            "United Kingdom",
            Res.drawable.gb
        ),
        CountryDetails(
            "gd",
            "+1",
            "Grenada",
            Res.drawable.gd
        ),
        CountryDetails(
            "ge",
            "+995",
            "Georgia",
            Res.drawable.ge
        ),
        CountryDetails(
            "gf",
            "+594",
            "French Guyana",
            Res.drawable.gf
        ),
        CountryDetails(
            "gh",
            "+233",
            "Ghana",
            Res.drawable.gh
        ),
        CountryDetails(
            "gi",
            "+350",
            "Gibraltar",
            Res.drawable.gi
        ),
        CountryDetails(
            "gl",
            "+299",
            "Greenland",
            Res.drawable.gl
        ),
        CountryDetails(
            "gm",
            "+220",
            "Gambia",
            Res.drawable.gm
        ),
        CountryDetails(
            "gn",
            "+224",
            "Guinea",
            Res.drawable.gn
        ),
        CountryDetails(
            "gp",
            "+450",
            "Guadeloupe",
            Res.drawable.gp
        ),
        CountryDetails(
            "gq",
            "+240",
            "Equatorial Guinea",
            Res.drawable.gq
        ),
        CountryDetails(
            "gr",
            "+30",
            "Greece",
            Res.drawable.gr
        ),
        CountryDetails(
            "gt",
            "+502",
            "Guatemala",
            Res.drawable.gt
        ),
        CountryDetails(
            "gu",
            "+1",
            "Guam",
            Res.drawable.gu
        ),
        CountryDetails(
            "gw",
            "+245",
            "Guinea-Bissau",
            Res.drawable.gw
        ),
        CountryDetails(
            "gy",
            "+592",
            "Guyana",
            Res.drawable.gy
        ),
        CountryDetails(
            "hk",
            "+852",
            "Hong Kong",
            Res.drawable.hk
        ),
        CountryDetails(
            "hn",
            "+504",
            "Honduras",
            Res.drawable.hn
        ),
        CountryDetails(
            "hr",
            "+385",
            "Croatia",
            Res.drawable.hr
        ),
        CountryDetails(
            "ht",
            "+509",
            "Haiti",
            Res.drawable.ht
        ),
        CountryDetails(
            "hu",
            "+36",
            "Hungary",
            Res.drawable.hu
        ),
        CountryDetails(
            "id",
            "+62",
            "Indonesia",
            Res.drawable.id
        ),
        CountryDetails(
            "ie",
            "+353",
            "Ireland",
            Res.drawable.ie
        ),
        CountryDetails(
            "il",
            "+972",
            "Israel",
            Res.drawable.il
        ),
        CountryDetails(
            "im",
            "+44",
            "Isle Of Man",
            Res.drawable.im
        ),
        CountryDetails(
            "is",
            "+354",
            "Iceland",
            Res.drawable.iceland
        ),
        CountryDetails(
            "in",
            "+91",
            "India",
            Res.drawable.india
        ),
        CountryDetails(
            "io",
            "+246",
            "British Indian Ocean Territory",
            Res.drawable.io
        ),
        CountryDetails(
            "iq",
            "+964",
            "Iraq",
            Res.drawable.iq
        ),
        CountryDetails(
            "ir",
            "+98",
            "Iran, Islamic Republic Of",
            Res.drawable.ir
        ),
        CountryDetails(
            "it",
            "+39",
            "Italy",
            Res.drawable.it
        ),
        CountryDetails(
            "je",
            "+44",
            "Jersey",
            Res.drawable.je
        ),
        CountryDetails(
            "jm",
            "+1",
            "Jamaica",
            Res.drawable.jm
        ),
        CountryDetails(
            "jo",
            "+962",
            "Jordan",
            Res.drawable.jo
        ),
        CountryDetails(
            "jp",
            "+81",
            "Japan",
            Res.drawable.jp
        ),
        CountryDetails(
            "ke",
            "+254",
            "Kenya",
            Res.drawable.ke
        ),
        CountryDetails(
            "kg",
            "+996",
            "Kyrgyzstan",
            Res.drawable.kg
        ),
        CountryDetails(
            "kh",
            "+855",
            "Cambodia",
            Res.drawable.kh
        ),
        CountryDetails(
            "ki",
            "+686",
            "Kiribati",
            Res.drawable.ki
        ),
        CountryDetails(
            "km",
            "+269",
            "Comoros",
            Res.drawable.km
        ),
        CountryDetails(
            "kn",
            "+1",
            "Saint Kitts and Nevis",
            Res.drawable.kn
        ),
        CountryDetails(
            "kp",
            "+850",
            "North Korea",
            Res.drawable.kp
        ),
        CountryDetails(
            "kr",
            "+82",
            "South Korea",
            Res.drawable.kr
        ),
        CountryDetails(
            "kw",
            "+965",
            "Kuwait",
            Res.drawable.kw
        ),
        CountryDetails(
            "ky",
            "+1",
            "Cayman Islands",
            Res.drawable.ky
        ),
        CountryDetails(
            "kz",
            "+7",
            "Kazakhstan",
            Res.drawable.kz
        ),
        CountryDetails(
            "la",
            "+856",
            "Lao People's Democratic Republic",
            Res.drawable.la
        ),
        CountryDetails(
            "lb",
            "+961",
            "Lebanon",
            Res.drawable.lb
        ),
        CountryDetails(
            "lc",
            "+1",
            "Saint Lucia",
            Res.drawable.lc
        ),
        CountryDetails(
            "li",
            "+423",
            "Liechtenstein",
            Res.drawable.li
        ),
        CountryDetails(
            "lk",
            "+94",
            "Sri Lanka",
            Res.drawable.lk
        ),
        CountryDetails(
            "lr",
            "+231",
            "Liberia",
            Res.drawable.lr
        ),
        CountryDetails(
            "ls",
            "+266",
            "Lesotho",
            Res.drawable.ls
        ),
        CountryDetails(
            "lt",
            "+370",
            "Lithuania",
            Res.drawable.lt
        ),
        CountryDetails(
            "lu",
            "+352",
            "Luxembourg",
            Res.drawable.lu
        ),
        CountryDetails(
            "lv",
            "+371",
            "Latvia",
            Res.drawable.lv
        ),
        CountryDetails(
            "ly",
            "+218",
            "Libya",
            Res.drawable.ly
        ),
        CountryDetails(
            "ma",
            "+212",
            "Morocco",
            Res.drawable.ma
        ),
        CountryDetails(
            "mc",
            "+377",
            "Monaco",
            Res.drawable.mc
        ),
        CountryDetails(
            "md",
            "+373",
            "Moldova, Republic Of",
            Res.drawable.md
        ),
        CountryDetails(
            "me",
            "+382",
            "Montenegro",
            Res.drawable.me
        ),
        CountryDetails(
            "mf",
            "+590",
            "Saint Martin",
            Res.drawable.mf
        ),
        CountryDetails(
            "mg",
            "+261",
            "Madagascar",
            Res.drawable.mg
        ),
        CountryDetails(
            "mh",
            "+692",
            "Marshall Islands",
            Res.drawable.mh
        ),
        CountryDetails(
            "mk",
            "+389",
            "Macedonia (FYROM)",
            Res.drawable.mk
        ),
        CountryDetails(
            "ml",
            "+223",
            "Mali",
            Res.drawable.ml
        ),
        CountryDetails(
            "mm",
            "+95",
            "Myanmar",
            Res.drawable.mm
        ),
        CountryDetails(
            "mn",
            "+976",
            "Mongolia",
            Res.drawable.mn
        ),
        CountryDetails(
            "mo",
            "+853",
            "Macau",
            Res.drawable.mo
        ),
        CountryDetails(
            "mp",
            "+1",
            "Northern Mariana Islands",
            Res.drawable.mp
        ),
        CountryDetails(
            "mq",
            "+596",
            "Martinique",
            Res.drawable.mq
        ),
        CountryDetails(
            "mr",
            "+222",
            "Mauritania",
            Res.drawable.mr
        ),
        CountryDetails(
            "ms",
            "+1",
            "Montserrat",
            Res.drawable.ms
        ),
        CountryDetails(
            "mt",
            "+356",
            "Malta",
            Res.drawable.mt
        ),
        CountryDetails(
            "mu",
            "+230",
            "Mauritius",
            Res.drawable.mu
        ),
        CountryDetails(
            "mv",
            "+960",
            "Maldives",
            Res.drawable.mv
        ),
        CountryDetails(
            "mw",
            "+265",
            "Malawi",
            Res.drawable.mw
        ),
        CountryDetails(
            "mx",
            "+52",
            "Mexico",
            Res.drawable.mx
        ),
        CountryDetails(
            "my",
            "+60",
            "Malaysia",
            Res.drawable.my
        ),
        CountryDetails(
            "mz",
            "+258",
            "Mozambique",
            Res.drawable.mz
        ),
        CountryDetails(
            "na",
            "+264",
            "Namibia",
            Res.drawable.na
        ),
        CountryDetails(
            "nc",
            "+687",
            "New Caledonia",
            Res.drawable.nc
        ),
        CountryDetails(
            "ne",
            "+227",
            "Niger",
            Res.drawable.ne
        ),
        CountryDetails(
            "nf",
            "+672",
            "Norfolk Islands",
            Res.drawable.nf
        ),
        CountryDetails(
            "ng",
            "+234",
            "Nigeria",
            Res.drawable.ng
        ),
        CountryDetails(
            "ni",
            "+505",
            "Nicaragua",
            Res.drawable.ni
        ),
        CountryDetails(
            "nl",
            "+31",
            "Netherlands",
            Res.drawable.nl
        ),
        CountryDetails(
            "no",
            "+47",
            "Norway",
            Res.drawable.no
        ),
        CountryDetails(
            "np",
            "+977",
            "Nepal",
            Res.drawable.np
        ),
        CountryDetails(
            "nr",
            "+674",
            "Nauru",
            Res.drawable.nr
        ),
        CountryDetails(
            "nu",
            "+683",
            "Niue",
            Res.drawable.nu
        ),
        CountryDetails(
            "nz",
            "+64",
            "New Zealand",
            Res.drawable.nz
        ),
        CountryDetails(
            "om",
            "+968",
            "Oman",
            Res.drawable.om
        ),
        CountryDetails(
            "pa",
            "+507",
            "Panama",
            Res.drawable.pa
        ),
        CountryDetails(
            "pe",
            "+51",
            "Peru",
            Res.drawable.pe
        ),
        CountryDetails(
            "pf",
            "+689",
            "French Polynesia",
            Res.drawable.pf
        ),
        CountryDetails(
            "pg",
            "+675",
            "Papua New Guinea",
            Res.drawable.pg
        ),
        CountryDetails(
            "ph",
            "+63",
            "Philippines",
            Res.drawable.ph
        ),
        CountryDetails(
            "pk",
            "+92",
            "Pakistan",
            Res.drawable.pk
        ),
        CountryDetails(
            "pl",
            "+48",
            "Poland",
            Res.drawable.pl
        ),
        CountryDetails(
            "pm",
            "+508",
            "Saint Pierre And Miquelon",
            Res.drawable.pm
        ),
        CountryDetails(
            "pn",
            "+870",
            "Pitcairn Islands",
            Res.drawable.pn
        ),
        CountryDetails(
            "pr",
            "+1",
            "Puerto Rico",
            Res.drawable.pr
        ),
        CountryDetails(
            "ps",
            "+970",
            "Palestine",
            Res.drawable.ps
        ),
        CountryDetails(
            "pt",
            "+351",
            "Portugal",
            Res.drawable.pt
        ),
        CountryDetails(
            "pw",
            "+680",
            "Palau",
            Res.drawable.pw
        ),
        CountryDetails(
            "py",
            "+595",
            "Paraguay",
            Res.drawable.py
        ),
        CountryDetails(
            "qa",
            "+974",
            "Qatar",
            Res.drawable.qa
        ),
        CountryDetails(
            "re",
            "+262",
            "Réunion",
            Res.drawable.re
        ),
        CountryDetails(
            "ro",
            "+40",
            "Romania",
            Res.drawable.ro
        ),
        CountryDetails(
            "rs",
            "+381",
            "Serbia",
            Res.drawable.rs
        ),
        CountryDetails(
            "ru",
            "+7",
            "Russian Federation",
            Res.drawable.ru
        ),
        CountryDetails(
            "rw",
            "+250",
            "Rwanda",
            Res.drawable.rw
        ),
        CountryDetails(
            "sa",
            "+966",
            "Saudi Arabia",
            Res.drawable.sa
        ),
        CountryDetails(
            "sb",
            "+677",
            "Solomon Islands",
            Res.drawable.sb
        ),
        CountryDetails(
            "sc",
            "+248",
            "Seychelles",
            Res.drawable.sc
        ),
        CountryDetails(
            "sd",
            "+249",
            "Sudan",
            Res.drawable.sd
        ),
        CountryDetails(
            "se",
            "+46",
            "Sweden",
            Res.drawable.se
        ),
        CountryDetails(
            "sg",
            "+65",
            "Singapore",
            Res.drawable.sg
        ),
        CountryDetails(
            "sh",
            "+290",
            "Saint Helena, Ascension And Tristan Da Cunha",
            Res.drawable.sh
        ),
        CountryDetails(
            "si",
            "+386",
            "Slovenia",
            Res.drawable.si
        ),
        CountryDetails(
            "sk",
            "+421",
            "Slovakia",
            Res.drawable.sk
        ),
        CountryDetails(
            "sl",
            "+232",
            "Sierra Leone",
            Res.drawable.sl
        ),
        CountryDetails(
            "sm",
            "+378",
            "San Marino",
            Res.drawable.sm
        ),
        CountryDetails(
            "sn",
            "+221",
            "Senegal",
            Res.drawable.sn
        ),
        CountryDetails(
            "so",
            "+252",
            "Somalia",
            Res.drawable.so
        ),
        CountryDetails(
            "sr",
            "+597",
            "Suriname",
            Res.drawable.sr
        ),
        CountryDetails(
            "ss",
            "+211",
            "South Sudan",
            Res.drawable.ss
        ),
        CountryDetails(
            "st",
            "+239",
            "Sao Tome And Principe",
            Res.drawable.st
        ),
        CountryDetails(
            "sv",
            "+503",
            "El Salvador",
            Res.drawable.sv
        ),
        CountryDetails(
            "sx",
            "+1",
            "Sint Maarten",
            Res.drawable.sx
        ),
        CountryDetails(
            "sy",
            "+963",
            "Syrian Arab Republic",
            Res.drawable.sy
        ),
        CountryDetails(
            "sz",
            "+268",
            "Swaziland",
            Res.drawable.sz
        ),
        CountryDetails(
            "tc",
            "+1",
            "Turks and Caicos Islands",
            Res.drawable.tc
        ),
        CountryDetails(
            "td",
            "+235",
            "Chad",
            Res.drawable.td
        ),
        CountryDetails(
            "tg",
            "+228",
            "Togo",
            Res.drawable.tg
        ),
        CountryDetails(
            "th",
            "+66",
            "Thailand",
            Res.drawable.th
        ),
        CountryDetails(
            "tj",
            "+992",
            "Tajikistan",
            Res.drawable.tj
        ),
        CountryDetails(
            "tk",
            "+690",
            "Tokelau",
            Res.drawable.tk
        ),
        CountryDetails(
            "tl",
            "+670",
            "Timor-leste",
            Res.drawable.tl
        ),
        CountryDetails(
            "tm",
            "+993",
            "Turkmenistan",
            Res.drawable.tm
        ),
        CountryDetails(
            "tn",
            "+216",
            "Tunisia",
            Res.drawable.tn
        ),
        CountryDetails(
            "to",
            "+676",
            "Tonga",
            Res.drawable.to
        ),
        CountryDetails(
            "tr",
            "+90",
            "Turkey",
            Res.drawable.tr
        ),
        CountryDetails(
            "tt",
            "+1",
            "Trinidad &amp; Tobago",
            Res.drawable.tt
        ),
        CountryDetails(
            "tv",
            "+688",
            "Tuvalu",
            Res.drawable.tv
        ),
        CountryDetails(
            "tw",
            "+886",
            "Taiwan",
            Res.drawable.tw
        ),
        CountryDetails(
            "tz",
            "+255",
            "Tanzania, United Republic Of",
            Res.drawable.tz
        ),
        CountryDetails(
            "ua",
            "+380",
            "Ukraine",
            Res.drawable.ua
        ),
        CountryDetails(
            "ug",
            "+256",
            "Uganda",
            Res.drawable.ug
        ),
        CountryDetails(
            "us",
            "+1",
            "United States",
            Res.drawable.us
        ),
        CountryDetails(
            "uy",
            "+598",
            "Uruguay",
            Res.drawable.uy
        ),
        CountryDetails(
            "uz",
            "+998",
            "Uzbekistan",
            Res.drawable.uz
        ),
        CountryDetails(
            "va",
            "+379",
            "Holy See (vatican City State)",
            Res.drawable.va
        ),
        CountryDetails(
            "vc",
            "+1",
            "Saint Vincent &amp; The Grenadines",
            Res.drawable.vc
        ),
        CountryDetails(
            "ve",
            "+58",
            "Venezuela, Bolivarian Republic Of",
            Res.drawable.ve
        ),
        CountryDetails(
            "vg",
            "+1",
            "British Virgin Islands",
            Res.drawable.vg
        ),
        CountryDetails(
            "vi",
            "+1",
            "US Virgin Islands",
            Res.drawable.vi
        ),
        CountryDetails(
            "vn",
            "+84",
            "Vietnam",
            Res.drawable.vn
        ),
        CountryDetails(
            "vu",
            "+678",
            "Vanuatu",
            Res.drawable.vu
        ),
        CountryDetails(
            "wf",
            "+681",
            "Wallis And Futuna",
            Res.drawable.wf
        ),
        CountryDetails(
            "ws",
            "4685",
            "Samoa",
            Res.drawable.ws
        ),
        CountryDetails(
            "xk",
            "+383",
            "Kosovo",
            Res.drawable.xk
        ),
        CountryDetails(
            "ye",
            "+967",
            "Yemen",
            Res.drawable.ye
        ),
        CountryDetails(
            "yt",
            "+262",
            "Mayotte",
            Res.drawable.yt
        ),
        CountryDetails(
            "za",
            "+27",
            "South Africa",
            Res.drawable.za
        ),
        CountryDetails(
            "zm",
            "+260",
            "Zambia",
            Res.drawable.zm
        ),
        CountryDetails(
            "zw",
            "+263",
            "Zimbabwe",
            Res.drawable.zw
        ),
    ).sortedBy { it.countryName }

    fun Modifier.noRippleClickable(
        onClick: () -> Unit
    ) = composed {
        this.clickable(
            indication = null,
            interactionSource = remember { MutableInteractionSource() }
        ) {
            onClick()
        }
    }
}