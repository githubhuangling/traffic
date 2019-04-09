package com.ctf.traffic.service.impl;

import java.util.*;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ctf.traffic.po.Accident;
import com.ctf.traffic.po.AccidentMedia;
import com.ctf.traffic.repository.AccidentMediaRepository;
import com.ctf.traffic.service.AccidentMediaService;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author ramer
 * @Date 6/27/2018
 * @see
 */
@Slf4j
@Service
public class AccidentMediaServiceImpl implements AccidentMediaService{
    // 可能会导致内存溢出
    private Map<Long, List<UploadMediaM>> uploadMediaM = Collections.synchronizedMap(new HashMap<>());
    // key: accidentId val:  UUID
    private Map<Long, List<String>> uploadEncryptM = Collections.synchronizedMap(new HashMap<>());

    @Resource
    private AccidentMediaRepository repository;

    @Transactional
    @Override
    public void delete(Accident accident) {
        repository.deleteByAccident(accident);
    }

    @Transactional
    @Override
    public AccidentMedia saveOrUpdate(AccidentMedia media) {
        return repository.saveAndFlush(media);
    }

    @Override
    public List<AccidentMedia> findByAccidentAndPart(long accidentId, int part) {
        return repository.findByAccidentAndPart(new Accident(accidentId), part);
    }

    @Override
    public List<AccidentMedia> findByAccident(long accidentId) {
        return repository.findByAccident(new Accident(accidentId));
    }

    @Override
    public List<AccidentMedia> findByDrivingLicence(long drivingId) {
        return repository.findByDrivingLicenceId(drivingId);
    }

    @Override
    public String saveUploadMediaM(Long accidentId, Integer mediaPart, String fileUrl) {
        List<UploadMediaM> mediaWithParts = uploadMediaM.get(accidentId);
        // 已有事故对应的图片
        if (mediaWithParts != null && mediaWithParts.size() > 0) {
            UploadMediaM uploadMediaM = new UploadMediaM();
            uploadMediaM.setPart(mediaPart);
            uploadMediaM.getUrls().add(fileUrl);
            // 已有当前部位的图片,直接添加
            int index;
            if ((index = mediaWithParts.indexOf(uploadMediaM)) != -1) {
                mediaWithParts.get(index).getUrls().add(fileUrl);
            } else {
                mediaWithParts.add(uploadMediaM);
            }
        } else {
            uploadMediaM.put(accidentId, new ArrayList() {
                {
                    add(new UploadMediaM() {
                        {
                            setPart(mediaPart);
                            getUrls().add(fileUrl);
                        }
                    });
                }
            });
        }
        return "";
    }

    @Override
    public List<UploadMediaM> getUploadMediaM(Long accidentId) {
        return uploadMediaM.remove(accidentId);
    }

    @Override
    public void initUploadMediaM(long accidentId, String uuid) {
        if (uuid != null) {
            final List<String> uuids = uploadEncryptM.get(accidentId);
            if (uuids != null) {
                if (!uuids.contains(uuid)) {
                    uuids.add(uuid);
                    uploadEncryptM.put(accidentId, uuids);
                }
            }
        } else {
            uploadEncryptM.put(accidentId, new ArrayList<>());
        }
    }

    @Override
    public boolean canUploadMediaM(Long accidentId, String uuid) {
        final List<String> uuids = uploadEncryptM.get(accidentId);
        //todo
        //return uuids != null && uuids.contains(uuid);
        return true;
    }

    @Override
    public void releaseUploadMediaM(Long accidentId) {
        uploadEncryptM.remove(accidentId);
    }

    @Data
    public class UploadMediaM{
        private Integer part;
        private List<String> urls = new ArrayList<>();

        @Override
        public boolean equals(Object o) {
            return this.part == ((UploadMediaM) o).part;
        }

        @Override
        public int hashCode() {
            return Objects.hash(part);
        }
    }
}
