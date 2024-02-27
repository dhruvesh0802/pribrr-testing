package com.pb.service;

import com.pb.dto.CountryDTO;
import com.pb.dto.TimezoneDTO;
import com.pb.model.CountryEntity;
import com.pb.model.TimezoneEntity;
import com.pb.repository.CountryRepository;
import com.pb.repository.TimezoneRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CountryService {
    
    @Autowired
    private CountryRepository countryRepository;
    @Autowired
    private TimezoneRepository timezoneRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Cacheable("Country-cache")
    public List<CountryDTO> getAllCountry() {
        List<CountryEntity> countryEntityList = countryRepository.findAllByIsActiveTrue();
        
        TypeToken<List<CountryDTO>> typeToken = new TypeToken<List<CountryDTO>>() {
        };
        
        List<CountryDTO> countryDTOList = modelMapper.map(countryEntityList, typeToken.getType());
        return countryDTOList;
        
    }

    @CacheEvict(value = "Country-cache" , allEntries = true)
    public void clearcache() {
        System.out.println("Country cache Deleted.............!");
    }

    public List<TimezoneDTO> getAllTimeZone() {
        List<TimezoneEntity> timezoneList = timezoneRepository.findAllByIsActiveTrue();

        TypeToken<List<TimezoneDTO>> typeToken = new TypeToken<List<TimezoneDTO>>() {
        };

        List<TimezoneDTO> timezoneDTOList = modelMapper.map(timezoneList, typeToken.getType());
        return timezoneDTOList;

    }
}
