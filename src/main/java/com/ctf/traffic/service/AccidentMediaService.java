package com.ctf.traffic.service;

import java.util.List;

import com.ctf.traffic.po.Accident;
import com.ctf.traffic.po.AccidentMedia;
import com.ctf.traffic.service.impl.AccidentMediaServiceImpl.UploadMediaM;

/**
 * @author ramer
 * @Date 6/27/2018
 * @see
 */
public interface AccidentMediaService{
    void delete(Accident accident);

    AccidentMedia saveOrUpdate(AccidentMedia media);

    /**
     * 通过事故和车损部位获取事故照片.
     * @param accidentId
     * @param part
     * @return
     */
    List<AccidentMedia> findByAccidentAndPart(long accidentId, int part);

    /**
     * 通过事故获取事故照片.
     * @param accidentId
     * @return
     */
    List<AccidentMedia> findByAccident(long accidentId);

    /**
     * 通过行驶证获取事故照片.
     * @param drivingId
     * @return
     */
    List<AccidentMedia> findByDrivingLicence(long drivingId);

    String saveUploadMediaM(Long accidentId, Integer mediaPart, String fileUrl);

    List<UploadMediaM> getUploadMediaM(Long accidentId);

    void initUploadMediaM(long accidentId, String uuid);

    void releaseUploadMediaM(Long accidentId);

    boolean canUploadMediaM(Long id, String uuid);
}
