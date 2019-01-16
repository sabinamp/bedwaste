package ch.fhnw.bedwaste;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import android.arch.lifecycle.ViewModel;
import android.graphics.Movie;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.Date;

import ch.fhnw.bedwaste.model.AvailabilityResult;
import ch.fhnw.bedwaste.model.CurrencyCode;
import ch.fhnw.bedwaste.model.HotelDescriptiveInfo;
import ch.fhnw.bedwaste.model.HotelInfo;
import ch.fhnw.bedwaste.model.HotelInfoPosition;

public class WelcomeViewModel extends ViewModel {
    static final LatLng HOTTINGEN= new LatLng(47.3697905658882, 8.55352004819906);
    static final LatLng PLATZHIRSCH = new LatLng(47.3735057616661, 8.5440319776535);
    static final LatLng HEMLHAUS= new LatLng(47.369158397978, 8.54404538869858);
    static final LatLng VILLETTE= new LatLng(47.3682, 8.5453);




    static String hottingenJsonInput="{\n" +
            "    \"hotelId\": \"00I5846a022h291r\",\n" +
            "    \"hotelName\": \"Hotel Hottingen\",\n" +
            "    \"areaUnitOfMeasureCode\": 14,\n" +
            "    \"distanceUnitOfMeasureCode\": 2,\n" +
            "    \"timeZone\": \"Europe/Zurich\",\n" +
            "    \"weightUnitOfMeasureCode\": 16,\n" +
            "    \"currencyCode\": \"CHF\",\n" +
            "    \"hotelInfo\": {\n" +
            "        \"whenBuilt\": 2011,\n" +
            "        \"position\": {\n" +
            "            \"altitude\": 440,\n" +
            "            \"latitude\": 47.3697905658882,\n" +
            "            \"longitude\": 8.55352004819906\n" +
            "        },\n" +
            "        \"descriptions\": {\n" +
            "            \"multimediadescriptions\": [\n" +
            "                {\n" +
            "                    \"images\": [\n" +
            "                        {\n" +
            "                            \"sort\": 1,\n" +
            "                            \"imageId\": \"00L55eej8eh46e5r\",\n" +
            "                            \"imageUrl\": \"./hotel_hottingen1.jpg\",\n" +
            "                            \"imageWidth\": 350,\n" +
            "                            \"imageHeight\": 150,\n" +
            "                            \"dimensionCategory\": \"Mobile\"\n" +
            "                        },\n" +
            "                        {\n" +
            "                            \"sort\": 2,\n" +
            "                            \"imageId\": \"00L55gej8ah46e5r\",\n" +
            "                            \"imageUrl\": \"./Hottingen.jpg\",\n" +
            "                            \"imageWidth\": 350,\n" +
            "                            \"imageHeight\": 150,\n" +
            "                            \"dimensionCategory\": \"Mobile\"\n" +
            "                        }\n" +
            "                    ],\n" +
            "                    \"texts\": [\n" +
            "                        [\n" +
            "                            {\n" +
            "                                \"detailsCode\": 2,\n" +
            "                                \"text\": \"Hotel Hottingen ist ein Haus mit modernem Design in der Altstadt von Zürich\"\n" +
            "                            },\n" +
            "                            {\n" +
            "                                \"detailsCode\": 71,\n" +
            "                                \"text\": \"This is the restaurant description\"\n" +
            "                            }\n" +
            "                        ]\n" +
            "                    ]\n" +
            "                }\n" +
            "            ]\n" +
            "        },\n" +
            "        \"services\": [\n" +
            "            [\n" +
            "                {\n" +
            "                    \"code\": 5,\n" +
            "                    \"codeDetails\": \"Air conditioning\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"code\": 54,\n" +
            "                    \"codeDetails\": \"Indoor pool\"\n" +
            "                }\n" +
            "            ]\n" +
            "        ]\n" +
            "    },\n" +
            "    \"affiliationInfo\": {\n" +
            "        \"awards\": [\n" +
            "            {\n" +
            "                \"provider\": \"HotelAssociation\",\n" +
            "                \"rating\": 2\n" +
            "            },\n" +
            "            {\n" +
            "                \"provider\": \"TripAdvisor\",\n" +
            "                \"rating\": 4.4\n" +
            "            }\n" +
            "        ]\n" +
            "    },\n" +
            "    \"facilityInfo\": {\n" +
            "        \"guestRooms\": [\n" +
            "            {\n" +
            "                \"id\": \"00U5846d022f2911\",\n" +
            "                \"roomTypeName\": \"Junior Suite\",\n" +
            "                \"minOccupancy\": 1,\n" +
            "                \"maxOccupancy\": 2,\n" +
            "                \"maxChildOccupancy\": 1,\n" +
            "                \"maxAdultOccupancy\": 2,\n" +
            "                \"typeRoom\": {\n" +
            "                    \"standardOccupancy\": 1,\n" +
            "                    \"standardNumBeds\": 1,\n" +
            "                    \"maxCribs\": 1,\n" +
            "                    \"maxRollaways\": 0,\n" +
            "                    \"composite\": false,\n" +
            "                    \"quantity\": 26\n" +
            "                },\n" +
            "                \"amenities\": [\n" +
            "                    {\n" +
            "                        \"roomAmenityCode\": 2,\n" +
            "                        \"codeDetails\": \"Air conditioning\"\n" +
            "                    },\n" +
            "                    {\n" +
            "                        \"roomAmenityCode\": 55,\n" +
            "                        \"codeDetails\": \"Iron\"\n" +
            "                    }\n" +
            "                ],\n" +
            "                \"multimediaDescriptions\": {\n" +
            "                    \"images\": [\n" +
            "                        {\n" +
            "                            \"imageId\": \"00U5846e022f2915\",\n" +
            "                            \"imageUrl\": \"\",\n" +
            "                            \"imageWidth\": 1920,\n" +
            "                            \"imageHeight\": 1080,\n" +
            "                            \"dimensionCategory\": \"Desktop\"\n" +
            "                        },\n" +
            "                        {\n" +
            "                            \"imageId\": \"00U5846e022e4567\",\n" +
            "                            \"imageUrl\": \"\",\n" +
            "                            \"imageWidth\": 720,\n" +
            "                            \"imageHeight\": 480,\n" +
            "                            \"dimensionCategory\": \"Mobile\"\n" +
            "                        },\n" +
            "                        {\n" +
            "                            \"imageId\": \"00U5846e022f2916\",\n" +
            "                            \"imageUrl\": \"\",\n" +
            "                            \"imageWidth\": 1920,\n" +
            "                            \"imageHeight\": 1080,\n" +
            "                            \"dimensionCategory\": \"Desktop\"\n" +
            "                        }\n" +
            "                    ]\n" +
            "                }\n" +
            "            },\n" +
            "            {\n" +
            "                \"id\": \"00U5846e022f2912\",\n" +
            "                \"roomTypeName\": \"Double deluxe\",\n" +
            "                \"minOccupancy\": 1,\n" +
            "                \"maxOccupancy\": 3,\n" +
            "                \"maxChildOccupancy\": 2,\n" +
            "                \"maxAdultOccupancy\": 3,\n" +
            "                \"typeRoom\": {\n" +
            "                    \"standardOccupancy\": 2,\n" +
            "                    \"standardNumBeds\": 1,\n" +
            "                    \"maxCribs\": 1,\n" +
            "                    \"maxRollaways\": 0,\n" +
            "                    \"composite\": false,\n" +
            "                    \"quantity\": 14\n" +
            "                }\n" +
            "            }\n" +
            "        ]\n" +
            "    },\n" +
            "    \"contactInfos\": [\n" +
            "        {\n" +
            "            \"companyName\": \"Hotel Hottingen\",\n" +
            "            \"addresses\": [\n" +
            "                {\n" +
            "                    \"streetNmbr\": \"31\",\n" +
            "                    \"addressLine\": \"Hottingerstrasse\",\n" +
            "                    \"cityName\": \"Zürich\",\n" +
            "                    \"postalCode\": \"8032\",\n" +
            "                    \"stateProv\": \"Zürich\",\n" +
            "                    \"countryName\": \"Schweiz\"\n" +
            "                }\n" +
            "            ],\n" +
            "            \"phones\": [\n" +
            "                {\n" +
            "                    \"phoneTechType\": 1,\n" +
            "                    \"countryAccessCode\": 41,\n" +
            "                    \"phoneNumber\": \"0442561919\"\n" +
            "                }\n" +
            "            ],\n" +
            "            \"emails\": [\n" +
            "                {\n" +
            "                    \"email\": \"info@hotelhottingen.ch\"\n" +
            "                }\n" +
            "            ]\n" +
            "        }\n" +
            "    ],\n" +
            "    \"policies\": {\n" +
            "        \"policyInfo\": {\n" +
            "            \"minChildAge\": 2,\n" +
            "            \"maxChildAge\": 12\n" +
            "        }\n" +
            "    }\n" +
            "}";

 /*   private static HotelDescriptiveInfo outputHotelHottingen = new Gson().fromJson(hottingenJsonInput, HotelDescriptiveInfo.class);

   public static String getHottingenHotelDescription(){
        return outputHotelHottingen.getFacilityInfo().getGuestRooms().toString();
    }

    public static LatLng getHottingenHotelPosition(){
        HotelInfoPosition loc= outputHotelHottingen.getHotelInfo().getPosition();

        return new LatLng(loc.getLatitude().doubleValue(),loc.getLongitude().doubleValue());
    }*/

}
