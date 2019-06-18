/*
 * Copyright 2016 Max Schuster.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package info.colarietosti.demo.app.ui;

import com.vaadin.server.ExternalResource;
import com.vaadin.shared.ApplicationConstants;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * A collection of country flags icons.
 *
 * <p>
 * This class allows for easy use of the
 * "<a href="http://www.famfamfam.com/lab/icons/flags/">Flags</a>" icon set by
 * <a href="mailto:mjames@gmail.com">Mark James</a>.
 * </p>
 *
 * <p>
 * <b>Typical use case:</b><br><br>
 * <code>
 * Button button = new Button();<br>
 * button.setCaption("Finland");<br>
 * button.setIcon(FamFamFlags.FINLAND);<br>
 * </code>
 * </p>
 *
 * <p>
 * <b>ICON LICENSE:</b><br><br>
 * "These icons are public domain, and as such are free for any use (attribution
 * appreciated but not required)."<br><br>
 * <i>(Extracted from: James, Mark: readme.txt
 * http://www.famfamfam.com/lab/icons/flags/famfamfam_flag_icons.zip
 * (2016-03-11)</i>
 * </p>
 *
 * <p>
 * <b>SOME FLAGS ARE MISSING</b>:
 * </p>
 * <ul>
 * <li>AQ - Antarctica</li>
 * <li>BQ - Bonaire, Sint Eustatius And Saba</li>
 * <li>CW - Curaçao</li>
 * <li>GG - Guernsey</li>
 * <li>IM - Isle Of Man</li>
 * <li>JE - Jersey</li>
 * <li>BL - Saint Barthélemy</li>
 * <li>MF - Saint Martin (French Part)</li>
 * <li>SX - Sint Maarten (Dutch Part)</li>
 * <li>SS - South Sudan</li>
 * </ul>
 *
 * @author Max Schuster
 * @see
 * <a href="http://www.iso.org/iso/home/standards/country_codes/country_names_and_code_elements_txt-temp.htm">
 * country_names_and_code_elements_txt-temp.htm</a>
 * @see <a href="http://famfamfam.com">famfamfam.com</a>
 * @see <a href="http://www.famfamfam.com/lab/icons/flags/">FLAG ICONS</a>
 */
public final class FamFamFlags extends ExternalResource {

    /**
     * The path where the icons are located.
     */
    private static final String ICON_PATH
            = ApplicationConstants.VAADIN_PROTOCOL_PREFIX
            + "VAADIN/addons/famfamflags/";

    /**
     * A HashMap for fast country code to FamFamFlags matching.
     */
    private static final Map<String, FamFamFlags> COUNTRY_TO_FLAG
            = new HashMap<String, FamFamFlags>(247);

    /**
     * Flag of "Afghanistan"
     */
    public static final FamFamFlags AFGHANISTAN = self("AF");

    /**
     * Flag of "Åland Islands"
     */
    public static final FamFamFlags ALAND_ISLANDS = self("AX");

    /**
     * Flag of "Albania"
     */
    public static final FamFamFlags ALBANIA = self("AL");

    /**
     * Flag of "Algeria"
     */
    public static final FamFamFlags ALGERIA = self("DZ");

    /**
     * Flag of "American Samoa"
     */
    public static final FamFamFlags AMERICAN_SAMOA = self("AS");

    /**
     * Flag of "Andorra"
     */
    public static final FamFamFlags ANDORRA = self("AD");

    /**
     * Flag of "Angola"
     */
    public static final FamFamFlags ANGOLA = self("AO");

    /**
     * Flag of "Anguilla"
     */
    public static final FamFamFlags ANGUILLA = self("AI");

    /**
     * Flag of "Antigua And Barbuda"
     */
    public static final FamFamFlags ANTIGUA_AND_BARBUDA = self("AG");

    /**
     * Flag of "Argentina"
     */
    public static final FamFamFlags ARGENTINA = self("AR");

    /**
     * Flag of "Armenia"
     */
    public static final FamFamFlags ARMENIA = self("AM");

    /**
     * Flag of "Aruba"
     */
    public static final FamFamFlags ARUBA = self("AW");

    /**
     * Flag of "Australia"
     */
    public static final FamFamFlags AUSTRALIA = self("AU");

    /**
     * Flag of "Austria"
     */
    public static final FamFamFlags AUSTRIA = self("AT");

    /**
     * Flag of "Azerbaijan"
     */
    public static final FamFamFlags AZERBAIJAN = self("AZ");

    /**
     * Flag of "Bahamas"
     */
    public static final FamFamFlags BAHAMAS = self("BS");

    /**
     * Flag of "Bahrain"
     */
    public static final FamFamFlags BAHRAIN = self("BH");

    /**
     * Flag of "Bangladesh"
     */
    public static final FamFamFlags BANGLADESH = self("BD");

    /**
     * Flag of "Barbados"
     */
    public static final FamFamFlags BARBADOS = self("BB");

    /**
     * Flag of "Belarus"
     */
    public static final FamFamFlags BELARUS = self("BY");

    /**
     * Flag of "Belgium"
     */
    public static final FamFamFlags BELGIUM = self("BE");

    /**
     * Flag of "Belize"
     */
    public static final FamFamFlags BELIZE = self("BZ");

    /**
     * Flag of "Benin"
     */
    public static final FamFamFlags BENIN = self("BJ");

    /**
     * Flag of "Bermuda"
     */
    public static final FamFamFlags BERMUDA = self("BM");

    /**
     * Flag of "Bhutan"
     */
    public static final FamFamFlags BHUTAN = self("BT");

    /**
     * Flag of "Bolivia, Plurinational State Of"
     */
    public static final FamFamFlags BOLIVIA_PLURINATIONAL_STATE_OF = self("BO");

    /**
     * Flag of "Bosnia And Herzegovina"
     */
    public static final FamFamFlags BOSNIA_AND_HERZEGOVINA = self("BA");

    /**
     * Flag of "Botswana"
     */
    public static final FamFamFlags BOTSWANA = self("BW");

    /**
     * Flag of "Bouvet Island"
     */
    public static final FamFamFlags BOUVET_ISLAND = self("BV");

    /**
     * Flag of "Brazil"
     */
    public static final FamFamFlags BRAZIL = self("BR");

    /**
     * Flag of "British Indian Ocean Territory"
     */
    public static final FamFamFlags BRITISH_INDIAN_OCEAN_TERRITORY = self("IO");

    /**
     * Flag of "Brunei Darussalam"
     */
    public static final FamFamFlags BRUNEI_DARUSSALAM = self("BN");

    /**
     * Flag of "Bulgaria"
     */
    public static final FamFamFlags BULGARIA = self("BG");

    /**
     * Flag of "Burkina Faso"
     */
    public static final FamFamFlags BURKINA_FASO = self("BF");

    /**
     * Flag of "Burundi"
     */
    public static final FamFamFlags BURUNDI = self("BI");

    /**
     * Flag of "Cambodia"
     */
    public static final FamFamFlags CAMBODIA = self("KH");

    /**
     * Flag of "Cameroon"
     */
    public static final FamFamFlags CAMEROON = self("CM");

    /**
     * Flag of "Canada"
     */
    public static final FamFamFlags CANADA = self("CA");

    /**
     * Flag of "Cape Verde"
     */
    public static final FamFamFlags CAPE_VERDE = self("CV");

    /**
     * Flag of "Cayman Islands"
     */
    public static final FamFamFlags CAYMAN_ISLANDS = self("KY");

    /**
     * Flag of "Central African Republic"
     */
    public static final FamFamFlags CENTRAL_AFRICAN_REPUBLIC = self("CF");

    /**
     * Flag of "Chad"
     */
    public static final FamFamFlags CHAD = self("TD");

    /**
     * Flag of "Chile"
     */
    public static final FamFamFlags CHILE = self("CL");

    /**
     * Flag of "China"
     */
    public static final FamFamFlags CHINA = self("CN");

    /**
     * Flag of "Christmas Island"
     */
    public static final FamFamFlags CHRISTMAS_ISLAND = self("CX");

    /**
     * Flag of "Cocos (Keeling) Islands"
     */
    public static final FamFamFlags COCOS_KEELING_ISLANDS = self("CC");

    /**
     * Flag of "Colombia"
     */
    public static final FamFamFlags COLOMBIA = self("CO");

    /**
     * Flag of "Comoros"
     */
    public static final FamFamFlags COMOROS = self("KM");

    /**
     * Flag of "Congo"
     */
    public static final FamFamFlags CONGO = self("CG");

    /**
     * Flag of "Congo, The Democratic Republic Of The"
     */
    public static final FamFamFlags CONGO_THE_DEMOCRATIC_REPUBLIC_OF_THE = self("CD");

    /**
     * Flag of "Cook Islands"
     */
    public static final FamFamFlags COOK_ISLANDS = self("CK");

    /**
     * Flag of "Costa Rica"
     */
    public static final FamFamFlags COSTA_RICA = self("CR");

    /**
     * Flag of "Côte D'ivoire"
     */
    public static final FamFamFlags COTE_DIVOIRE = self("CI");

    /**
     * Flag of "Croatia"
     */
    public static final FamFamFlags CROATIA = self("HR");

    /**
     * Flag of "Cuba"
     */
    public static final FamFamFlags CUBA = self("CU");

    /**
     * Flag of "Cyprus"
     */
    public static final FamFamFlags CYPRUS = self("CY");

    /**
     * Flag of "Czech Republic"
     */
    public static final FamFamFlags CZECH_REPUBLIC = self("CZ");

    /**
     * Flag of "Denmark"
     */
    public static final FamFamFlags DENMARK = self("DK");

    /**
     * Flag of "Djibouti"
     */
    public static final FamFamFlags DJIBOUTI = self("DJ");

    /**
     * Flag of "Dominica"
     */
    public static final FamFamFlags DOMINICA = self("DM");

    /**
     * Flag of "Dominican Republic"
     */
    public static final FamFamFlags DOMINICAN_REPUBLIC = self("DO");

    /**
     * Flag of "Ecuador"
     */
    public static final FamFamFlags ECUADOR = self("EC");

    /**
     * Flag of "Egypt"
     */
    public static final FamFamFlags EGYPT = self("EG");

    /**
     * Flag of "El Salvador"
     */
    public static final FamFamFlags EL_SALVADOR = self("SV");

    /**
     * Flag of "Equatorial Guinea"
     */
    public static final FamFamFlags EQUATORIAL_GUINEA = self("GQ");

    /**
     * Flag of "Eritrea"
     */
    public static final FamFamFlags ERITREA = self("ER");

    /**
     * Flag of "Estonia"
     */
    public static final FamFamFlags ESTONIA = self("EE");

    /**
     * Flag of "Ethiopia"
     */
    public static final FamFamFlags ETHIOPIA = self("ET");

    /**
     * Flag of "Falkland Islands (Malvinas)"
     */
    public static final FamFamFlags FALKLAND_ISLANDS_MALVINAS = self("FK");

    /**
     * Flag of "Faroe Islands"
     */
    public static final FamFamFlags FAROE_ISLANDS = self("FO");

    /**
     * Flag of "Fiji"
     */
    public static final FamFamFlags FIJI = self("FJ");

    /**
     * Flag of "Finland"
     */
    public static final FamFamFlags FINLAND = self("FI");

    /**
     * Flag of "France"
     */
    public static final FamFamFlags FRANCE = self("FR");

    /**
     * Flag of "French Guiana"
     */
    public static final FamFamFlags FRENCH_GUIANA = self("GF");

    /**
     * Flag of "French Polynesia"
     */
    public static final FamFamFlags FRENCH_POLYNESIA = self("PF");

    /**
     * Flag of "French Southern Territories"
     */
    public static final FamFamFlags FRENCH_SOUTHERN_TERRITORIES = self("TF");

    /**
     * Flag of "Gabon"
     */
    public static final FamFamFlags GABON = self("GA");

    /**
     * Flag of "Gambia"
     */
    public static final FamFamFlags GAMBIA = self("GM");

    /**
     * Flag of "Georgia"
     */
    public static final FamFamFlags GEORGIA = self("GE");

    /**
     * Flag of "Germany"
     */
    public static final FamFamFlags GERMANY = self("DE");

    /**
     * Flag of "Ghana"
     */
    public static final FamFamFlags GHANA = self("GH");

    /**
     * Flag of "Gibraltar"
     */
    public static final FamFamFlags GIBRALTAR = self("GI");

    /**
     * Flag of "Greece"
     */
    public static final FamFamFlags GREECE = self("GR");

    /**
     * Flag of "Greenland"
     */
    public static final FamFamFlags GREENLAND = self("GL");

    /**
     * Flag of "Grenada"
     */
    public static final FamFamFlags GRENADA = self("GD");

    /**
     * Flag of "Guadeloupe"
     */
    public static final FamFamFlags GUADELOUPE = self("GP");

    /**
     * Flag of "Guam"
     */
    public static final FamFamFlags GUAM = self("GU");

    /**
     * Flag of "Guatemala"
     */
    public static final FamFamFlags GUATEMALA = self("GT");

    /**
     * Flag of "Guinea"
     */
    public static final FamFamFlags GUINEA = self("GN");

    /**
     * Flag of "Guinea-Bissau"
     */
    public static final FamFamFlags GUINEABISSAU = self("GW");

    /**
     * Flag of "Guyana"
     */
    public static final FamFamFlags GUYANA = self("GY");

    /**
     * Flag of "Haiti"
     */
    public static final FamFamFlags HAITI = self("HT");

    /**
     * Flag of "Heard Island And Mcdonald Islands"
     */
    public static final FamFamFlags HEARD_ISLAND_AND_MCDONALD_ISLANDS = self("HM");

    /**
     * Flag of "Holy See (Vatican City State)"
     */
    public static final FamFamFlags HOLY_SEE_VATICAN_CITY_STATE = self("VA");

    /**
     * Flag of "Honduras"
     */
    public static final FamFamFlags HONDURAS = self("HN");

    /**
     * Flag of "Hong Kong"
     */
    public static final FamFamFlags HONG_KONG = self("HK");

    /**
     * Flag of "Hungary"
     */
    public static final FamFamFlags HUNGARY = self("HU");

    /**
     * Flag of "Iceland"
     */
    public static final FamFamFlags ICELAND = self("IS");

    /**
     * Flag of "India"
     */
    public static final FamFamFlags INDIA = self("IN");

    /**
     * Flag of "Indonesia"
     */
    public static final FamFamFlags INDONESIA = self("ID");

    /**
     * Flag of "Iran, Islamic Republic Of"
     */
    public static final FamFamFlags IRAN_ISLAMIC_REPUBLIC_OF = self("IR");

    /**
     * Flag of "Iraq"
     */
    public static final FamFamFlags IRAQ = self("IQ");

    /**
     * Flag of "Ireland"
     */
    public static final FamFamFlags IRELAND = self("IE");

    /**
     * Flag of "Israel"
     */
    public static final FamFamFlags ISRAEL = self("IL");

    /**
     * Flag of "Italy"
     */
    public static final FamFamFlags ITALY = self("IT");

    /**
     * Flag of "Jamaica"
     */
    public static final FamFamFlags JAMAICA = self("JM");

    /**
     * Flag of "Japan"
     */
    public static final FamFamFlags JAPAN = self("JP");

    /**
     * Flag of "Jordan"
     */
    public static final FamFamFlags JORDAN = self("JO");

    /**
     * Flag of "Kazakhstan"
     */
    public static final FamFamFlags KAZAKHSTAN = self("KZ");

    /**
     * Flag of "Kenya"
     */
    public static final FamFamFlags KENYA = self("KE");

    /**
     * Flag of "Kiribati"
     */
    public static final FamFamFlags KIRIBATI = self("KI");

    /**
     * Flag of "Korea, Democratic People's Republic Of"
     */
    public static final FamFamFlags KOREA_DEMOCRATIC_PEOPLES_REPUBLIC_OF = self("KP");

    /**
     * Flag of "Korea, Republic Of"
     */
    public static final FamFamFlags KOREA_REPUBLIC_OF = self("KR");

    /**
     * Flag of "Kuwait"
     */
    public static final FamFamFlags KUWAIT = self("KW");

    /**
     * Flag of "Kyrgyzstan"
     */
    public static final FamFamFlags KYRGYZSTAN = self("KG");

    /**
     * Flag of "Lao People's Democratic Republic"
     */
    public static final FamFamFlags LAO_PEOPLES_DEMOCRATIC_REPUBLIC = self("LA");

    /**
     * Flag of "Latvia"
     */
    public static final FamFamFlags LATVIA = self("LV");

    /**
     * Flag of "Lebanon"
     */
    public static final FamFamFlags LEBANON = self("LB");

    /**
     * Flag of "Lesotho"
     */
    public static final FamFamFlags LESOTHO = self("LS");

    /**
     * Flag of "Liberia"
     */
    public static final FamFamFlags LIBERIA = self("LR");

    /**
     * Flag of "Libya"
     */
    public static final FamFamFlags LIBYA = self("LY");

    /**
     * Flag of "Liechtenstein"
     */
    public static final FamFamFlags LIECHTENSTEIN = self("LI");

    /**
     * Flag of "Lithuania"
     */
    public static final FamFamFlags LITHUANIA = self("LT");

    /**
     * Flag of "Luxembourg"
     */
    public static final FamFamFlags LUXEMBOURG = self("LU");

    /**
     * Flag of "Macao"
     */
    public static final FamFamFlags MACAO = self("MO");

    /**
     * Flag of "Macedonia, The Former Yugoslav Republic Of"
     */
    public static final FamFamFlags MACEDONIA_THE_FORMER_YUGOSLAV_REPUBLIC_OF = self("MK");

    /**
     * Flag of "Madagascar"
     */
    public static final FamFamFlags MADAGASCAR = self("MG");

    /**
     * Flag of "Malawi"
     */
    public static final FamFamFlags MALAWI = self("MW");

    /**
     * Flag of "Malaysia"
     */
    public static final FamFamFlags MALAYSIA = self("MY");

    /**
     * Flag of "Maldives"
     */
    public static final FamFamFlags MALDIVES = self("MV");

    /**
     * Flag of "Mali"
     */
    public static final FamFamFlags MALI = self("ML");

    /**
     * Flag of "Malta"
     */
    public static final FamFamFlags MALTA = self("MT");

    /**
     * Flag of "Marshall Islands"
     */
    public static final FamFamFlags MARSHALL_ISLANDS = self("MH");

    /**
     * Flag of "Martinique"
     */
    public static final FamFamFlags MARTINIQUE = self("MQ");

    /**
     * Flag of "Mauritania"
     */
    public static final FamFamFlags MAURITANIA = self("MR");

    /**
     * Flag of "Mauritius"
     */
    public static final FamFamFlags MAURITIUS = self("MU");

    /**
     * Flag of "Mayotte"
     */
    public static final FamFamFlags MAYOTTE = self("YT");

    /**
     * Flag of "Mexico"
     */
    public static final FamFamFlags MEXICO = self("MX");

    /**
     * Flag of "Micronesia, Federated States Of"
     */
    public static final FamFamFlags MICRONESIA_FEDERATED_STATES_OF = self("FM");

    /**
     * Flag of "Moldova, Republic Of"
     */
    public static final FamFamFlags MOLDOVA_REPUBLIC_OF = self("MD");

    /**
     * Flag of "Monaco"
     */
    public static final FamFamFlags MONACO = self("MC");

    /**
     * Flag of "Mongolia"
     */
    public static final FamFamFlags MONGOLIA = self("MN");

    /**
     * Flag of "Montenegro"
     */
    public static final FamFamFlags MONTENEGRO = self("ME");

    /**
     * Flag of "Montserrat"
     */
    public static final FamFamFlags MONTSERRAT = self("MS");

    /**
     * Flag of "Morocco"
     */
    public static final FamFamFlags MOROCCO = self("MA");

    /**
     * Flag of "Mozambique"
     */
    public static final FamFamFlags MOZAMBIQUE = self("MZ");

    /**
     * Flag of "Myanmar"
     */
    public static final FamFamFlags MYANMAR = self("MM");

    /**
     * Flag of "Namibia"
     */
    public static final FamFamFlags NAMIBIA = self("NA");

    /**
     * Flag of "Nauru"
     */
    public static final FamFamFlags NAURU = self("NR");

    /**
     * Flag of "Nepal"
     */
    public static final FamFamFlags NEPAL = self("NP");

    /**
     * Flag of "Netherlands"
     */
    public static final FamFamFlags NETHERLANDS = self("NL");

    /**
     * Flag of "New Caledonia"
     */
    public static final FamFamFlags NEW_CALEDONIA = self("NC");

    /**
     * Flag of "New Zealand"
     */
    public static final FamFamFlags NEW_ZEALAND = self("NZ");

    /**
     * Flag of "Nicaragua"
     */
    public static final FamFamFlags NICARAGUA = self("NI");

    /**
     * Flag of "Niger"
     */
    public static final FamFamFlags NIGER = self("NE");

    /**
     * Flag of "Nigeria"
     */
    public static final FamFamFlags NIGERIA = self("NG");

    /**
     * Flag of "Niue"
     */
    public static final FamFamFlags NIUE = self("NU");

    /**
     * Flag of "Norfolk Island"
     */
    public static final FamFamFlags NORFOLK_ISLAND = self("NF");

    /**
     * Flag of "Northern Mariana Islands"
     */
    public static final FamFamFlags NORTHERN_MARIANA_ISLANDS = self("MP");

    /**
     * Flag of "Norway"
     */
    public static final FamFamFlags NORWAY = self("NO");

    /**
     * Flag of "Oman"
     */
    public static final FamFamFlags OMAN = self("OM");

    /**
     * Flag of "Pakistan"
     */
    public static final FamFamFlags PAKISTAN = self("PK");

    /**
     * Flag of "Palau"
     */
    public static final FamFamFlags PALAU = self("PW");

    /**
     * Flag of "Palestine, State Of"
     */
    public static final FamFamFlags PALESTINE_STATE_OF = self("PS");

    /**
     * Flag of "Panama"
     */
    public static final FamFamFlags PANAMA = self("PA");

    /**
     * Flag of "Papua New Guinea"
     */
    public static final FamFamFlags PAPUA_NEW_GUINEA = self("PG");

    /**
     * Flag of "Paraguay"
     */
    public static final FamFamFlags PARAGUAY = self("PY");

    /**
     * Flag of "Peru"
     */
    public static final FamFamFlags PERU = self("PE");

    /**
     * Flag of "Philippines"
     */
    public static final FamFamFlags PHILIPPINES = self("PH");

    /**
     * Flag of "Pitcairn"
     */
    public static final FamFamFlags PITCAIRN = self("PN");

    /**
     * Flag of "Poland"
     */
    public static final FamFamFlags POLAND = self("PL");

    /**
     * Flag of "Portugal"
     */
    public static final FamFamFlags PORTUGAL = self("PT");

    /**
     * Flag of "Puerto Rico"
     */
    public static final FamFamFlags PUERTO_RICO = self("PR");

    /**
     * Flag of "Qatar"
     */
    public static final FamFamFlags QATAR = self("QA");

    /**
     * Flag of "Réunion"
     */
    public static final FamFamFlags REUNION = self("RE");

    /**
     * Flag of "Romania"
     */
    public static final FamFamFlags ROMANIA = self("RO");

    /**
     * Flag of "Russian Federation"
     */
    public static final FamFamFlags RUSSIAN_FEDERATION = self("RU");

    /**
     * Flag of "Rwanda"
     */
    public static final FamFamFlags RWANDA = self("RW");

    /**
     * Flag of "Saint Helena, Ascension And Tristan Da Cunha"
     */
    public static final FamFamFlags SAINT_HELENA_ASCENSION_AND_TRISTAN_DA_CUNHA = self("SH");

    /**
     * Flag of "Saint Kitts And Nevis"
     */
    public static final FamFamFlags SAINT_KITTS_AND_NEVIS = self("KN");

    /**
     * Flag of "Saint Lucia"
     */
    public static final FamFamFlags SAINT_LUCIA = self("LC");

    /**
     * Flag of "Saint Pierre And Miquelon"
     */
    public static final FamFamFlags SAINT_PIERRE_AND_MIQUELON = self("PM");

    /**
     * Flag of "Saint Vincent And The Grenadines"
     */
    public static final FamFamFlags SAINT_VINCENT_AND_THE_GRENADINES = self("VC");

    /**
     * Flag of "Samoa"
     */
    public static final FamFamFlags SAMOA = self("WS");

    /**
     * Flag of "San Marino"
     */
    public static final FamFamFlags SAN_MARINO = self("SM");

    /**
     * Flag of "Sao Tome And Principe"
     */
    public static final FamFamFlags SAO_TOME_AND_PRINCIPE = self("ST");

    /**
     * Flag of "Saudi Arabia"
     */
    public static final FamFamFlags SAUDI_ARABIA = self("SA");

    /**
     * Flag of "Senegal"
     */
    public static final FamFamFlags SENEGAL = self("SN");

    /**
     * Flag of "Serbia"
     */
    public static final FamFamFlags SERBIA = self("RS");

    /**
     * Flag of "Seychelles"
     */
    public static final FamFamFlags SEYCHELLES = self("SC");

    /**
     * Flag of "Sierra Leone"
     */
    public static final FamFamFlags SIERRA_LEONE = self("SL");

    /**
     * Flag of "Singapore"
     */
    public static final FamFamFlags SINGAPORE = self("SG");

    /**
     * Flag of "Slovakia"
     */
    public static final FamFamFlags SLOVAKIA = self("SK");

    /**
     * Flag of "Slovenia"
     */
    public static final FamFamFlags SLOVENIA = self("SI");

    /**
     * Flag of "Solomon Islands"
     */
    public static final FamFamFlags SOLOMON_ISLANDS = self("SB");

    /**
     * Flag of "Somalia"
     */
    public static final FamFamFlags SOMALIA = self("SO");

    /**
     * Flag of "South Africa"
     */
    public static final FamFamFlags SOUTH_AFRICA = self("ZA");

    /**
     * Flag of "South Georgia And The South Sandwich Islands"
     */
    public static final FamFamFlags SOUTH_GEORGIA_AND_THE_SOUTH_SANDWICH_ISLANDS = self("GS");

    /**
     * Flag of "Spain"
     */
    public static final FamFamFlags SPAIN = self("ES");

    /**
     * Flag of "Sri Lanka"
     */
    public static final FamFamFlags SRI_LANKA = self("LK");

    /**
     * Flag of "Sudan"
     */
    public static final FamFamFlags SUDAN = self("SD");

    /**
     * Flag of "Suriname"
     */
    public static final FamFamFlags SURINAME = self("SR");

    /**
     * Flag of "Svalbard And Jan Mayen"
     */
    public static final FamFamFlags SVALBARD_AND_JAN_MAYEN = self("SJ");

    /**
     * Flag of "Swaziland"
     */
    public static final FamFamFlags SWAZILAND = self("SZ");

    /**
     * Flag of "Sweden"
     */
    public static final FamFamFlags SWEDEN = self("SE");

    /**
     * Flag of "Switzerland"
     */
    public static final FamFamFlags SWITZERLAND = self("CH");

    /**
     * Flag of "Syrian Arab Republic"
     */
    public static final FamFamFlags SYRIAN_ARAB_REPUBLIC = self("SY");

    /**
     * Flag of "Taiwan, Province Of China"
     */
    public static final FamFamFlags TAIWAN_PROVINCE_OF_CHINA = self("TW");

    /**
     * Flag of "Tajikistan"
     */
    public static final FamFamFlags TAJIKISTAN = self("TJ");

    /**
     * Flag of "Tanzania, United Republic Of"
     */
    public static final FamFamFlags TANZANIA_UNITED_REPUBLIC_OF = self("TZ");

    /**
     * Flag of "Thailand"
     */
    public static final FamFamFlags THAILAND = self("TH");

    /**
     * Flag of "Timor-Leste"
     */
    public static final FamFamFlags TIMORLESTE = self("TL");

    /**
     * Flag of "Togo"
     */
    public static final FamFamFlags TOGO = self("TG");

    /**
     * Flag of "Tokelau"
     */
    public static final FamFamFlags TOKELAU = self("TK");

    /**
     * Flag of "Tonga"
     */
    public static final FamFamFlags TONGA = self("TO");

    /**
     * Flag of "Trinidad And Tobago"
     */
    public static final FamFamFlags TRINIDAD_AND_TOBAGO = self("TT");

    /**
     * Flag of "Tunisia"
     */
    public static final FamFamFlags TUNISIA = self("TN");

    /**
     * Flag of "Turkey"
     */
    public static final FamFamFlags TURKEY = self("TR");

    /**
     * Flag of "Turkmenistan"
     */
    public static final FamFamFlags TURKMENISTAN = self("TM");

    /**
     * Flag of "Turks And Caicos Islands"
     */
    public static final FamFamFlags TURKS_AND_CAICOS_ISLANDS = self("TC");

    /**
     * Flag of "Tuvalu"
     */
    public static final FamFamFlags TUVALU = self("TV");

    /**
     * Flag of "Uganda"
     */
    public static final FamFamFlags UGANDA = self("UG");

    /**
     * Flag of "Ukraine"
     */
    public static final FamFamFlags UKRAINE = self("UA");

    /**
     * Flag of "United Arab Emirates"
     */
    public static final FamFamFlags UNITED_ARAB_EMIRATES = self("AE");

    /**
     * Flag of "United Kingdom"
     */
    public static final FamFamFlags UNITED_KINGDOM = self("GB");

    /**
     * Flag of "United States"
     */
    public static final FamFamFlags UNITED_STATES = self("US");

    /**
     * Flag of "United States Minor Outlying Islands"
     */
    public static final FamFamFlags UNITED_STATES_MINOR_OUTLYING_ISLANDS = self("UM");

    /**
     * Flag of "Uruguay"
     */
    public static final FamFamFlags URUGUAY = self("UY");

    /**
     * Flag of "Uzbekistan"
     */
    public static final FamFamFlags UZBEKISTAN = self("UZ");

    /**
     * Flag of "Vanuatu"
     */
    public static final FamFamFlags VANUATU = self("VU");

    /**
     * Flag of "Venezuela, Bolivarian Republic Of"
     */
    public static final FamFamFlags VENEZUELA_BOLIVARIAN_REPUBLIC_OF = self("VE");

    /**
     * Flag of "Viet Nam"
     */
    public static final FamFamFlags VIET_NAM = self("VN");

    /**
     * Flag of "Virgin Islands, British"
     */
    public static final FamFamFlags VIRGIN_ISLANDS_BRITISH = self("VG");

    /**
     * Flag of "Virgin Islands, U.s."
     */
    public static final FamFamFlags VIRGIN_ISLANDS_US = self("VI");

    /**
     * Flag of "Wallis And Futuna"
     */
    public static final FamFamFlags WALLIS_AND_FUTUNA = self("WF");

    /**
     * Flag of "Western Sahara"
     */
    public static final FamFamFlags WESTERN_SAHARA = self("EH");

    /**
     * Flag of "Yemen"
     */
    public static final FamFamFlags YEMEN = self("YE");

    /**
     * Flag of "Zambia"
     */
    public static final FamFamFlags ZAMBIA = self("ZM");

    /**
     * Flag of "Zimbabwe"
     */
    public static final FamFamFlags ZIMBABWE = self("ZW");

    // ========================
    // Addtional flags:
    // ========================
    /**
     * Flag of "AN" (?)
     */
    public static final FamFamFlags AN = self("AN");

    /**
     * Flag of "Catalonia"
     */
    public static final FamFamFlags CATALONIA = self("CATALONIA");

    /**
     * Flag of "CS" (?)
     */
    public static final FamFamFlags CS = self("CS");

    /**
     * Flag of "England"
     */
    public static final FamFamFlags ENGLAND = self("ENGLAND");

    /**
     * Flag of "European Union"
     */
    public static final FamFamFlags EUROPEANUNION = self("EUROPEANUNION");

    /**
     * Flag of "FAMFAMFAM"
     */
    public static final FamFamFlags FAM = self("FAM");

    /**
     * Flag of "Scotland"
     */
    public static final FamFamFlags SCOTLAND = self("SCOTLAND");

    /**
     * Flag of "Wales"
     */
    public static final FamFamFlags WALES = self("WALES");

    /**
     * The MIME type of this flag
     */
    private final String MIMEType;

    /**
     * The flags country code
     */
    private final String country;

    /**
     * Creates a new {@link FamFamFlags} instance.
     *
     * @param country The country code (usually an uppercase ISO 3166 2-letter
     * code)
     */
    private FamFamFlags(String country) {
        super(getIconUrl(country), null);
        this.country = country;
        this.MIMEType = "image/png";
        COUNTRY_TO_FLAG.put(country, this);
    }

    /**
     * A utility method to make the creation of {@link FamFamFlags} instance
     * less verbose.
     * 
     * @param country The country code (usually an uppercase ISO 3166 2-letter
     * code)
     * @return The new {@link FamFamFlags} instance
     */
    private static FamFamFlags self(String country) {
        return new FamFamFlags(country);
    }

    /**
     * Gets the icon url for the given country code.
     *
     * @param country The country code (usually an uppercase ISO 3166 2-letter
     * code)
     * @return The icons url
     */
    private static String getIconUrl(String country) {
        return ICON_PATH + country.toLowerCase(Locale.US) + ".png";
    }

    /**
     * Returns the {@link FamFamFlags} instance for the given country code or
     * {@code null} if no matching instance was found.
     *
     * @param country The country code (usually an uppercase ISO 3166 2-letter
     * code)
     * @return The {@link FamFamFlags} instance or {@code null}
     * @see Locale#getCountry()
     */
    public static FamFamFlags fromCountry(String country) {
        return COUNTRY_TO_FLAG.get(country);
    }

    /**
     * Returns the {@link FamFamFlags} instance for the given locale or
     * {@code null} if no matching instance was found.
     *
     * @param locale The Locale
     * @return The {@link FamFamFlags} instance or {@code null}
     */
    public static FamFamFlags fromLocale(Locale locale) {
        return fromCountry(locale.getCountry());
    }

    /**
     * Returns the country or region code of this icon (usually an uppercase ISO
     * 3166 2-letter code)
     *
     * @return The country or region code (usually an uppercase ISO 3166 2-
     * letter code)
     * @see Locale#getCountry()
     */
    public String getCountry() {
        return country;
    }

    /*
     * Override the original getMIMEType() method to return our own mimetype.
     */
    @Override
    public String getMIMEType() {
        return MIMEType;
    }

    /**
     * <b>THIS METHOD MUST NOT BE USED!</b>
     *
     * @param mimeType The new MIME type
     * @deprecated
     * @throws UnsupportedOperationException On every method call!
     */
    @Deprecated
    @Override
    public void setMIMEType(String mimeType) throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

}
