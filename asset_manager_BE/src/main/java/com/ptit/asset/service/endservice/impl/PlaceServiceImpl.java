package com.ptit.asset.service.endservice.impl;

import com.ptit.asset.domain.Place;
import com.ptit.asset.dto.request.FetchPagePlaceRequestDTO;
import com.ptit.asset.dto.request.PlaceCreateRequestDTO;
import com.ptit.asset.dto.request.PlaceUpdateRequestDTO;
import com.ptit.asset.service.endservice.PlaceService;
import com.ptit.asset.service.manager.PlaceManagement;
import io.vavr.control.Try;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlaceServiceImpl implements PlaceService {

    @Autowired
    private PlaceManagement placeManagement;

    @Override
    public Try<Place> getOne(Long id) {
        return placeManagement.getOne(id);
    }

    @Override
    public Try<Place> create(PlaceCreateRequestDTO dto) {
        return placeManagement.create(dto);
    }

    @Override
    public Try<Place> update(PlaceUpdateRequestDTO dto) {
        return placeManagement.update(dto);
    }

    @Override
    public Try<Boolean> delete(Long id) {
        return placeManagement.delete(id);
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - //


    @Override
    public List<Place> fetchAll() {
        return placeManagement.fetchAll();
    }

    @Override
    public Page<Place> fetchPage(FetchPagePlaceRequestDTO dto) {
        return placeManagement.fetchPage(dto);
    }
}
