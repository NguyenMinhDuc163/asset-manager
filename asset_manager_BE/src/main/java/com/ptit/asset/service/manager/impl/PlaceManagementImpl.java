package com.ptit.asset.service.manager.impl;

import com.ptit.asset.domain.Place;
import com.ptit.asset.dto.request.FetchPagePlaceRequestDTO;
import com.ptit.asset.dto.request.PlaceCreateRequestDTO;
import com.ptit.asset.dto.request.PlaceUpdateRequestDTO;
import com.ptit.asset.repository.CampusRepository;
import com.ptit.asset.repository.DepartmentRepository;
import com.ptit.asset.repository.PlaceRepository;
import com.ptit.asset.repository.TypePlaceRepository;
import com.ptit.asset.service.manager.PlaceManagement;
import com.ptit.asset.service.manager.mapper.CentralMapper;
import io.vavr.control.Try;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlaceManagementImpl implements PlaceManagement {

    @Autowired
    private CentralMapper centralMapper;
    @Autowired
    private PlaceRepository placeRepository;
    @Autowired
    TypePlaceRepository typePlaceRepository;
    @Autowired
    private CampusRepository campusRepository;
    @Autowired
    private DepartmentRepository departmentRepository;


    @Override
    public Try<Place> getOne(Long id) {
        return Try.of(() -> placeRepository.findById(id).get())
            .orElse(() -> Try.failure(new Exception("Failure when get place by id: "+id)));
    }

    @Override
    public Try<Place> create(PlaceCreateRequestDTO dto) {
        val typePlace = typePlaceRepository.findById(dto.getEmbedded().getTypePlaceId());
        if (!typePlace.isPresent()){
            return Try.failure(new Exception("Failure when find typePlace by id: "+dto.getEmbedded().getTypePlaceId()));
        }
        val campus = campusRepository.findById(dto.getEmbedded().getCampusId());
        if (!campus.isPresent()){
            return Try.failure(new Exception("Failure when find campus by id: "+dto.getEmbedded().getCampusId()));
        }
        // this result is opt cause place can't belong to any department
//        val department = departmentRepository.findById(dto.getEmbedded().getDepartmentId());

        val place = centralMapper.toPlace(dto,typePlace.get(),campus.get(),
                (dto.getEmbedded().getDepartmentId()!= null) ?
//                departmentRepository.findById(dto.getEmbedded().getDepartmentId()).get()
                departmentRepository.findById(dto.getEmbedded().getDepartmentId()).get()
                :
                null);

        return Try.of(() -> placeRepository.save(place))
            .orElse(() -> Try.failure(new Exception("Failure when save place")));

    }

    @Override
    public Try<Place> update(PlaceUpdateRequestDTO dto) {
        val placeResult = placeRepository.findById(dto.getId());
        if (!placeResult.isPresent()){
            return Try.failure(new Exception("Failure when find place to update with id: "+dto.getId()));
        }

        val placeUpdate = centralMapper.toPlaceUpdate(placeResult.get(), dto);

        // relationship space
        if (dto.getEmbedded() != null){

            val typePlaceId = dto.getEmbedded().getTypePlaceId();
            if (typePlaceId != null && !typePlaceId.equals(placeUpdate.getTypePlace().getId())){
                val typePlace = typePlaceRepository.findById(typePlaceId);
                typePlace.ifPresent(placeUpdate::setTypePlace);
            }

            val campusId = dto.getEmbedded().getCampusId();
            if (campusId != null && !campusId.equals(placeUpdate.getCampus().getId())){
                val campus = campusRepository.findById(campusId);
                campus.ifPresent(placeUpdate::setCampus);
            }

            val departmentId = dto.getEmbedded().getDepartmentId();
            if (departmentId != null){
                val department = departmentRepository.findById(departmentId);
                department.ifPresent(placeUpdate::setDepartment);
            }
        }
        // relationship space

        return Try.of(() -> placeRepository.save(placeUpdate))
            .orElse(() -> Try.failure(new Exception("Failure when update place")));
    }

    @Override
    public Try<Boolean> delete(Long id) {
        return Try.of(() -> {
            placeRepository.deleteById(id);
            return true;
        }).orElse(() -> Try.failure(new Exception("Failure when delete place by id: "+ id)));
    }


    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - //
    @Override
    public List<Place> fetchAll() {
        return placeRepository.findAll();
    }

    @Override
    public Page<Place> fetchPage(FetchPagePlaceRequestDTO dto) {
        Pageable pageable = PageRequest.of(dto.getPage(),dto.getSize());
        return placeRepository.findAll(pageable);
    }
}
