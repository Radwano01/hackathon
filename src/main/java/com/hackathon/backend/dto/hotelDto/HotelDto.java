package com.hackathon.backend.dto.hotelDto;


import com.hackathon.backend.entities.hotel.hotelFeatures.HotelFeaturesEntity;
import com.hackathon.backend.entities.hotel.hotelFeatures.RoomFeaturesEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class HotelDto {

    private long id;
    private String hotelName;
    private String mainImage;
    private String title;
    private String description;
    private int hotelRoomsCount;
    private String address;
    private float rate;

    private RoomDetailsDto roomDetails;
    private List<HotelFeaturesEntity> hotelFeatures;
    private List<RoomFeaturesEntity> roomFeatures;

    public HotelDto(long id, String hotelName,
                    String mainImage, String title,
                    String description, String address, float rate) {
        this.id = id;
        this.hotelName = hotelName;
        this.mainImage = mainImage;
        this.title = title;
        this.description = description;
        this.address = address;
        this.rate = rate;
    }

    public HotelDto(long id, String hotelName,
                    String mainImage, String title,
                    String description, int hotelRoomsCount,
                    String address, float rate) {
        this.id = id;
        this.hotelName = hotelName;
        this.mainImage = mainImage;
        this.title = title;
        this.description = description;
        this.hotelRoomsCount = hotelRoomsCount;
        this.address = address;
        this.rate = rate;
    }

    public HotelDto(long id, String hotelName,
                    String address, float rate,
                    RoomDetailsDto roomDetailsDto,
                    List<HotelFeaturesEntity> hotelFeatures,
                    List<RoomFeaturesEntity> roomFeatures) {
        this.id = id;
        this.hotelName = hotelName;
        this.address = address;
        this.rate = rate;
        this.roomDetails = roomDetailsDto;
        this.hotelFeatures = hotelFeatures;
        this.roomFeatures = roomFeatures;
    }

}
