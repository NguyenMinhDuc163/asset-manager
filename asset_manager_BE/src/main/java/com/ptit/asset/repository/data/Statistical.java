package com.ptit.asset.repository.data;

import com.ptit.asset.domain.enumeration.MaterialStatus;
import com.ptit.asset.domain.enumeration.TimeAllocationType;

import java.time.Instant;

public interface Statistical {

    interface Inventory {
        String getName();
        String getCode();
        String getPlace();
        Double getPrice();// origin price
        TimeAllocationType getTimeAllocationType();
        int getAllocationDuration();
        Double getDepreciationRate();
        Instant getTimeStartDepreciation();
        MaterialStatus getMaterialStatus();
        String getMaterialCheck();
    }

    interface HistoryTransfer {
        String getPlaceFrom();
        String getPlaceTarget();
        String getReason();
        String getTimeAction();
        String getPersonAction();
    }

}
