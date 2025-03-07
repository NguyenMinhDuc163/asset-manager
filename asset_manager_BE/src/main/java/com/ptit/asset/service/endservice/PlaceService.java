package com.ptit.asset.service.endservice;

import com.ptit.asset.domain.Place;
import com.ptit.asset.dto.request.FetchPagePlaceRequestDTO;
import com.ptit.asset.dto.request.PlaceCreateRequestDTO;
import com.ptit.asset.dto.request.PlaceUpdateRequestDTO;
import io.vavr.control.Try;
import org.springframework.data.domain.Page;

import java.util.List;

public interface PlaceService {

    Try<Place> getOne(Long id);

    Try<Place> create(PlaceCreateRequestDTO dto);

    Try<Place> update(PlaceUpdateRequestDTO dto);

    Try<Boolean> delete(Long id);

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - //
    List<Place> fetchAll();

    Page<Place> fetchPage(FetchPagePlaceRequestDTO dto);

}
