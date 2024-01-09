package com.bootProject.service;

import com.bootProject.common.code.ErrorCode;
import com.bootProject.common.exception.BusinessException;
import com.bootProject.dto.ItemDTO;
import com.bootProject.entity.Item;
import com.bootProject.entity.SaveFile;
import com.bootProject.repository.file.FileRepository;
import com.bootProject.repository.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final FileRepository fileRepository;

    @Value("${com.upload.path}")
    private String uploadPath;


    @Transactional
    public void saveItem(List<MultipartFile> multipartFile, Item item) throws Exception{
        item = itemRepository.save(item);
        if(null != multipartFile) {
            List<SaveFile> fileList = uploadFile(multipartFile, item);
            fileRepository.saveAll(fileList);
        }
    }

    /* 전체 아이템 정보 */
    public List<Item> getAllItem() {
        return itemRepository.findListAll();
    }

    /* 최근 추가된 책 조회 */
    public List<Item> getRecentRegisteredItem() {
        List<Item> itemList = new ArrayList<Item>();
        try {
            itemList = itemRepository.findRecentRegisteredItem();
        } catch(Exception e) {
            log.debug("최근 추가된 책 조회 에러 발생 ");
            log.error(e.getMessage());
        }
        return itemList;
    }

    /* 아이템 상세 정보 */
    public ItemDTO findItemInfo(long id) {
        ItemDTO itemById = itemRepository.findItemById(id);
        return itemById;
    }

    @Transactional
    public void deleteItem (List<Long> itemList, List<Long> fileList) {
        try {
            if(null != fileList && 0 < fileList.size()) {
                List<SaveFile> deleteFileList = fileRepository.findAllById(fileList);
                deleteFileList.forEach(file -> {
                    try {
                        String srcFileName = URLDecoder.decode(file.getStoredFileName(), "UTF-8");
                        File deleteFile = new File(uploadPath + File.separator + srcFileName);
                        boolean result = deleteFile.delete();
                        if(!result) { throw new BusinessException(ErrorCode.FILE_DELETE_ERROR, "파일 삭제 실패"); }
                    } catch (UnsupportedEncodingException e) {
                        log.error("파일명 decode 에러 발생");
                    } catch (BusinessException e) {
                        log.error("파일 삭제 실패");
                    }
                });
                fileRepository.deleteAllByIdInBatch(fileList);
            }

            if(null != itemList && 0 < itemList.size()) {
                itemRepository.deleteAllByIdInBatch(itemList);
            }
        } catch(Exception e) {
            log.error("상품 삭제 중 에러 발생");
        }
    }

    /* 파일 저장  */
    @Transactional
    public List<SaveFile> uploadFile(List<MultipartFile> multipartFile, Item item) throws Exception {

        List<SaveFile> fileList = new ArrayList<>();
        // 파일명을 업로드 한 날짜로 변환하여 저장
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter =
                DateTimeFormatter.ofPattern("yyyyMMdd");
        String current_date = now.format(dateTimeFormatter);

        // 프로젝트 디렉터리 내의 저장을 위한 절대 경로 설정
        // 경로 구분자 File.separator 사용
        String absolutePath = new File(uploadPath).getCanonicalPath(); //+ File.separator + File.separator;
        File newFile = new File(absolutePath);
        if (!newFile.exists()) {
            boolean wasSuccessful = newFile.mkdirs();
            if (!wasSuccessful) { log.error("file: was not successful"); }
        }

        // 다중 파일 처리
        for (MultipartFile file : multipartFile) {
            // 파일의 확장자 추출
            String originalFileExtension = "";
            String contentType = file.getContentType();

            // 확장자명이 존재할 때
            if (ObjectUtils.isEmpty(contentType)) {
                break;
            } else {
                if (contentType.contains("image/jpeg")) {
                    originalFileExtension = ".jpg";
                } else if (contentType.contains("image/png")) {
                    originalFileExtension = ".png";
                } else {    //다른 확장자면 처리 x
                    break;
                }
            }

            String newFileName = "";
            newFileName = System.nanoTime() + originalFileExtension;

            SaveFile saveFile = SaveFile.builder()
                    .item(item)
                    .originFileName( file.getOriginalFilename() )
                    .storedFileName( newFileName )
                    .fileSize( file.getSize() )
                    .build();

            fileList.add(saveFile);

            newFile = new File(absolutePath + File.separator + newFileName);
            file.transferTo(newFile);

            newFile.setWritable(true);
            newFile.setReadable(true);
        }

        return fileList;
    }

}

