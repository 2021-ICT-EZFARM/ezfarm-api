package com.ezfarm.ezfarmback.common.farm;

import com.ezfarm.ezfarmback.common.exception.CustomException;
import com.ezfarm.ezfarmback.common.exception.dto.ErrorCode;
import com.ezfarm.ezfarmback.facility.domain.hour.Facility;
import com.ezfarm.ezfarmback.facility.domain.FacilityAvg;
import com.ezfarm.ezfarmback.facility.domain.day.FacilityDayAvg;
import com.ezfarm.ezfarmback.facility.domain.day.FacilityDayAvgRepository;
import com.ezfarm.ezfarmback.facility.domain.month.FacilityMonthAvg;
import com.ezfarm.ezfarmback.facility.domain.month.FacilityMonthAvgRepository;
import com.ezfarm.ezfarmback.facility.domain.week.FacilityWeekAvg;
import com.ezfarm.ezfarmback.facility.domain.week.FacilityWeekAvgRepository;
import com.ezfarm.ezfarmback.farm.domain.Farm;
import com.ezfarm.ezfarmback.farm.domain.FarmRepository;
import com.ezfarm.ezfarmback.farm.domain.enums.CropType;
import com.ezfarm.ezfarmback.farm.domain.enums.FarmGroup;
import com.ezfarm.ezfarmback.farm.domain.enums.FarmType;
import com.ezfarm.ezfarmback.user.domain.User;
import com.ezfarm.ezfarmback.user.domain.UserRepository;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.modelmapper.ModelMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Component
public class PublicFarmParser {

    private final ModelMapper modelMapper;
    private final FarmRepository farmRepository;
    private final FacilityDayAvgRepository facilityDayAvgRepository;
    private final FacilityWeekAvgRepository facilityWeekAvgRepository;
    private final FacilityMonthAvgRepository facilityMonthAvgRepository;
    private final UserRepository userRepository;
    private String serviceKey = "kvkoj/tBF7qWoxvAhz7HsCIG7pgmSGhLhq9W0H84wn3cPDAUwblBLBI6drAyqwaFXVXtn1nJtls9aySwrho1CQ==";

    //작물 : 토마토, 농가 타입 : 비닐에 대한 요청
    public void saveTomatoVinyl() throws Exception {
        Resource resource = new ClassPathResource("csv/farm/tomato/tomato_vinyl.csv");
        saveBestFarms(resource, CropType.TOMATO, FarmType.VINYL);
    }

    public void saveTomatoGlass() throws Exception {
        Resource resource = new ClassPathResource("csv/farm/tomato/tomato_glass.csv");
        saveBestFarms(resource, CropType.TOMATO, FarmType.GLASS);
    }

    public void saveStrawberryVinyl() throws Exception {
        Resource resource = new ClassPathResource("csv/farm/strawberry/strawberry_vinyl.csv");
        saveBestFarms(resource, CropType.STRAWBERRY, FarmType.VINYL);
    }

    public void savePaprikaVinyl() throws Exception {
        Resource resource = new ClassPathResource("csv/farm/paprika/paprika_vinyl.csv");
        saveBestFarms(resource, CropType.PAPRIKA, FarmType.VINYL);
    }

    public void savePaprikaGlass() throws Exception {
        Resource resource = new ClassPathResource("csv/farm/paprika/paprika_glass.csv");
        saveBestFarms(resource, CropType.PAPRIKA, FarmType.GLASS);
    }

    //우수 농가 farm 저장 및 해당 농가에 대한 데이터 저장
    private void saveBestFarms(Resource resource, CropType cropType, FarmType farmType)
        throws Exception {
        RestTemplate rest = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> requestEntity = new HttpEntity<String>(headers);

        log.info("{} {} 농가 데이터 등록 시작 startTime:{}", cropType, farmType, LocalDateTime.now());

        User admin = userRepository.findByEmail("admin@email.com")
            .orElseThrow(() -> new CustomException(ErrorCode.NON_EXISTENT_USER));

        Files.readAllLines(resource.getFile().toPath(), StandardCharsets.UTF_8)
            .stream()
            .forEach(line -> {
                //split[0] : farmId, split[1] : 도(지역), split[2] : 시/군(지역). split[3] : 면적
                String[] split = line.split(",");

                //우수 농가 저장
                Farm bestFarm = Farm.builder()
                    .address(split[1] + " " + split[2])
                    .name("우수 농가 " + split[0])
                    .isMain(false)
                    .cropType(cropType)
                    .farmType(farmType)
                    .area(split[3])
                    .farmGroup(FarmGroup.BEST)
                    .user(admin)
                    .build();
                farmRepository.save(bestFarm);

                //해당 우수 농가의 데이터 저장
                String farmId = split[0];
                List<Facility> facilities = new ArrayList<>();
                ResponseEntity<String> responseEntity = rest.exchange(
                    "http://apis.data.go.kr/1390000/SmartFarmdata/envdatarqst?serviceKey="
                        + serviceKey +
                        "&searchFrmhsCode=" + farmId + "&returnType=json&pageSize=100000",
                    HttpMethod.GET,
                    requestEntity, String.class);
                fromJsonToFacility(facilities, responseEntity.getBody(), bestFarm);
                if (!facilities.isEmpty()) {
                    //facilityRepository.saveAll(facilities);
                    //데이터 통계
                    facilityAveraging(bestFarm, facilities);
                }
            });
        log.info("{} {} 농가 데이터 등록 종료 endTime:{}", cropType, farmType, LocalDateTime.now());

    }

    //데이터 통계
    private void facilityAveraging(Farm bestFarm, List<Facility> facilities) {
        //1시간마다 측정된 농가 데이터를 측정 일짜 순으로 정렬(오름차순)
        facilities.sort(new Comparator<Facility>() {
            @Override
            public int compare(Facility f1, Facility f2) {
                if (f1.getMeasureDate().isEqual(f2.getMeasureDate())) {
                    return 0;
                } else if (f1.getMeasureDate().isBefore(f2.getMeasureDate())) {
                    return -1;
                } else {
                    return 1;
                }
            }
        });

        WeekFields weekFields = WeekFields.of(Locale.KOREA);
        int day = facilities.get(0).getMeasureDate().get(weekFields.dayOfWeek());
        int week = facilities.get(0).getMeasureDate().get(weekFields.weekOfWeekBasedYear());
        int month = facilities.get(0).getMeasureDate().getMonthValue();
        int year = facilities.get(0).getMeasureDate().getYear();

        //일 평균 데이터를 저장할 객체
        FacilityAvg tmpFacilityDay = new FacilityAvg();

        //주 평균 데이터를 저장할 객체
        FacilityAvg tmpFacilityWeek = new FacilityAvg();

        //월 평균 데이터를 저장할 객체
        FacilityAvg tmpFacilityMonth = new FacilityAvg();

        int hours = 0;
        int days = 0;
        int weeks = 0;

        //facility : 1시간별 농가 데이터 객체
        for (Facility facility : facilities) {

            //일별 농가 데이터 평균 구함
            if (day != facility.getMeasureDate().get(weekFields.dayOfWeek())) {
                day = facility.getMeasureDate().get(weekFields.dayOfWeek());
                tmpFacilityDay.average(hours);
                tmpFacilityWeek.sum(tmpFacilityDay);

                //일 통계 데이터 저장
                facilityDayAvgRepository.save(
                    FacilityDayAvg.builder().farm(bestFarm).facilityAvg(tmpFacilityDay)
                        .measureDate(facility.getMeasureDate()).build());
                tmpFacilityDay = new FacilityAvg();
                tmpFacilityDay.sum(facility);
                hours = 0;
                days++;
            }

            //주별 농가 데이터 평균 구함
            if (week != facility.getMeasureDate().get(weekFields.weekOfWeekBasedYear())) {
                week = facility.getMeasureDate().get(weekFields.weekOfWeekBasedYear());
                tmpFacilityWeek.average(days);
                tmpFacilityMonth.sum(tmpFacilityWeek);

                facilityWeekAvgRepository
                    .save(FacilityWeekAvg.builder().farm(bestFarm).facilityAvg(tmpFacilityWeek)
                        .measureDate(facility.getMeasureDate()).build());
                tmpFacilityWeek = new FacilityAvg();
                tmpFacilityWeek.sum(facility);
                weeks++;
                days = 0;
            }

            //월별 농가 데이터 평균 구함
            if (month != facility.getMeasureDate().getMonthValue()) {
                month = facility.getMeasureDate().getMonthValue();
                tmpFacilityMonth.average(weeks);

                facilityMonthAvgRepository
                    .save(FacilityMonthAvg.builder().farm(bestFarm).facilityAvg(tmpFacilityMonth)
                        .measureDate(facility.getMeasureDate()).build());
                tmpFacilityMonth = new FacilityAvg();
                tmpFacilityMonth.sum(facility);
                weeks = 0;
            }

            tmpFacilityDay.sum(facility);
            hours++;
        }
        ;
    }

    //공공데이터로 읽어온 json을 FacilityRequestDto 객체로 파싱
    public void fromJsonToFacility(List<Facility> facilities, String result, Farm bestFarm) {
        JSONObject jObject = new JSONObject(result);

    /*
      json 구조
      {"response" : {"body" : {"items" : [{facility}, {facility}, {facility}] } } }
     */
        jObject = (JSONObject) jObject.get("response");
        jObject = (JSONObject) jObject.get("body");
        jObject = (JSONObject) jObject.get("items");
        JSONArray items = jObject.getJSONArray("item");
        for (int i = 0; i < items.length(); i++) {
            JSONObject itemJson = (JSONObject) items.get(i);
            PublicFarmRequest facilityRequestDto = PublicFarmRequest.of(itemJson, bestFarm);
            facilities.add(modelMapper.map(facilityRequestDto, Facility.class));
        }
    }
}
