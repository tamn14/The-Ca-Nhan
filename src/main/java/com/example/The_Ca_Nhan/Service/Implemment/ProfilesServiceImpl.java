package com.example.The_Ca_Nhan.Service.Implemment;

import com.example.The_Ca_Nhan.DTO.Request.MediaFileCreateRequest;
import com.example.The_Ca_Nhan.DTO.Request.ProfilesRequest;
import com.example.The_Ca_Nhan.DTO.Response.ProfilesResponse;
import com.example.The_Ca_Nhan.Entity.Educations;
import com.example.The_Ca_Nhan.Entity.MediaFiles;
import com.example.The_Ca_Nhan.Entity.Profiles;
import com.example.The_Ca_Nhan.Entity.Users;
import com.example.The_Ca_Nhan.Exception.AppException;
import com.example.The_Ca_Nhan.Exception.ErrorCode;
import com.example.The_Ca_Nhan.Mapper.MediaFilesMapper;
import com.example.The_Ca_Nhan.Mapper.ProfilesMapper;
import com.example.The_Ca_Nhan.Properties.MediaEntityType;
import com.example.The_Ca_Nhan.Repository.MediaFileRepository;
import com.example.The_Ca_Nhan.Repository.ProfilesRepository;
import com.example.The_Ca_Nhan.Service.Interface.ProfilesInterface;
import com.example.The_Ca_Nhan.Service.Interface.S3Interface;
import com.example.The_Ca_Nhan.Util.Extract;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ProfilesServiceImpl implements ProfilesInterface {
    private final ProfilesRepository profilesRepository ;
    private final ProfilesMapper profilesMapper  ;
    private final Extract extract ;
    private final MediaFilesMapper mediaFilesMapper ;
    private final MediaFileRepository mediaFileRepository ;
    private final S3Interface s3Interface ;


    private Profiles getProfilesById( int id ) {
        return profilesRepository.findById(id)
                .orElseThrow(()-> new AppException(ErrorCode.PROFILES_NOT_FOUND)) ;
    }

    private void checkAuthenticated (Users users , Profiles profiles) {
        if(!(profiles.getUsers().getUserName().equals(users.getUserName()))) {
            throw new AppException(ErrorCode.UNAUTHENTICATED) ;
        }
    }
    private boolean checkListMediaIsNull(List<MediaFileCreateRequest> requests) {
        return requests == null || requests.isEmpty();
    }

    private String uploadImageAndGetUrl (MultipartFile file) {
        return s3Interface.uploadFile(file);
    }

    private void deleteImage (String url) {
        s3Interface.deleteImage(url);
    }

    private MediaFiles createAndSaveMedia(MediaFileCreateRequest request, Users user, int entityId) {
        String url = uploadImageAndGetUrl(request.getImageUrl());
        MediaFiles media = mediaFilesMapper.toEntity(request);
        media.setUsers(user);
        media.setEntityId(entityId);
        media.setLink(url);
        return mediaFileRepository.save(media);
    }



    @Override
    @PreAuthorize( "hasRole('USER')")
    public ProfilesResponse insertProfiles(ProfilesRequest request) {
        // lay user trong phien dang nhap
        Users users = extract.getUserInFlowLogin() ;
        // chuyen request ve entity '
        Profiles profiles = profilesMapper.toEntity(request) ;
        profiles.setUsers(users);
        Profiles profilesInsertEd = profilesRepository.save(profiles) ;
        // kiem tra List Media co rong khong
        // Neu List khong rong co nghia nguoi dung muon them profiles kem hinh anh
        // lay List<MediaFileCreateRequest>
        List<MediaFileCreateRequest> mediaFileCreateRequests = request.getMediaFiles() ;
        // Tao List<MediaFiles>
        List<MediaFiles> mediaFiles = new ArrayList<>() ;

        if(!checkListMediaIsNull(mediaFileCreateRequests)) {
            mediaFileCreateRequests.forEach(mediaFileCreateRequest -> {
                MediaFiles mediaFilesInsert = createAndSaveMedia(mediaFileCreateRequest, users, profilesInsertEd.getProfileId());
                mediaFiles.add(mediaFilesInsert) ;
            });
        }

        return profilesMapper.toDTO(profiles , mediaFiles);


    }

    @Override
    @PreAuthorize( "hasRole('USER')")
    public ProfilesResponse updateProfiles(ProfilesRequest request, int Id) {
        // kiem tra profiles co ton tai khong
        Profiles profiles = getProfilesById(Id) ;
        // lay user dang dang nhap
        Users users = extract.getUserInFlowLogin() ;
        // kiem tra doan bao user chi duoc cap nhat Education cua chinh user do
        checkAuthenticated(users , profiles);
        // set value cho education
        if(request.getSummary() != null) {
            profiles.setSummary(request.getSummary());
        }
        if(request.getHobby() != null && !request.getHobby().isEmpty()  ) {
            profiles.setHobby(request.getHobby());
        }
        if( request.getWebsite() != null && !request.getWebsite().isEmpty() ) {
            profiles.setWebsite(request.getWebsite());
        }
        profilesRepository.save(profiles);
        return profilesMapper.toDTO(profiles) ;
    }

    @Override
    @PreAuthorize( "hasRole('USER')")
    public void deleteProfiles(int Id) {
        // kiem tra profiles co ton tai khong
        Profiles profiles = getProfilesById(Id) ;
        // lay user dang dang nhap
        Users users = extract.getUserInFlowLogin() ;
        // kiem tra doan bao user chi duoc cap nhat Education cua chinh user do
        checkAuthenticated(users , profiles);
        // delete media cua education
        List<MediaFiles> mediaFiles = mediaFileRepository.findByEntityTypeAndEntityId(MediaEntityType.PROFILES , Id ) ;
        mediaFiles.forEach(mediaFilesDelete -> {
            deleteImage(mediaFilesDelete.getLink());
            mediaFileRepository.deleteById(mediaFilesDelete.getMediaId());
        });
        profilesRepository.deleteById(Id);
    }

    @Override
    @PreAuthorize( "hasRole('USER')")
    public List<ProfilesResponse> findAll() {
        return profilesRepository.findAll().stream().map(profilesMapper :: toDTO).toList() ;
    }

    @Override
    @PreAuthorize( "hasRole('USER')")
    public ProfilesResponse findById(int id) {
        // kiem tra profiles co ton tai khong
        Profiles profiles = getProfilesById(id) ;
        // lay user dang dang nhap
        Users users = extract.getUserInFlowLogin() ;
        // kiem tra doan bao user chi duoc cap nhat Education cua chinh user do
        checkAuthenticated(users , profiles);

        List<MediaFiles> mediaFiles = mediaFileRepository.findByEntityTypeAndEntityId(MediaEntityType.PROFILES , id ) ;

        return profilesMapper.toDTO(profiles , mediaFiles) ;

    }
}
