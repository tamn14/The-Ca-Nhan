package com.example.The_Ca_Nhan.Service.Implemment;

import com.example.The_Ca_Nhan.DTO.Request.MediaFileCreateRequest;
import com.example.The_Ca_Nhan.DTO.Request.ProfilesRequest;
import com.example.The_Ca_Nhan.DTO.Request.ProjectRequest;
import com.example.The_Ca_Nhan.DTO.Response.ProjectResponse;
import com.example.The_Ca_Nhan.Entity.MediaFiles;
import com.example.The_Ca_Nhan.Entity.Profiles;
import com.example.The_Ca_Nhan.Entity.Projects;
import com.example.The_Ca_Nhan.Entity.Users;
import com.example.The_Ca_Nhan.Exception.AppException;
import com.example.The_Ca_Nhan.Exception.ErrorCode;
import com.example.The_Ca_Nhan.Mapper.MediaFilesMapper;
import com.example.The_Ca_Nhan.Mapper.ProjectMapper;
import com.example.The_Ca_Nhan.Properties.MediaEntityType;
import com.example.The_Ca_Nhan.Repository.MediaFileRepository;
import com.example.The_Ca_Nhan.Repository.ProjectsRepository;
import com.example.The_Ca_Nhan.Service.Interface.ProjectInterface;
import com.example.The_Ca_Nhan.Service.Interface.S3Interface;
import com.example.The_Ca_Nhan.Util.Extract;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectInterface {
    private final ProjectsRepository projectsRepository ;
    private final ProjectMapper projectMapper ;
    private final Extract extract ;
    private final MediaFilesMapper mediaFilesMapper ;
    private final MediaFileRepository mediaFileRepository ;
    private final S3Interface s3Interface ;


    private Projects getProjectsById(int id ) {
        return projectsRepository.findById(id)
                .orElseThrow(()-> new AppException(ErrorCode.PROJECT_NOT_FOUND)) ;
    }

    private void checkAuthenticated (Users users , Projects projects) {
        if(!(projects.getUsers().getUserName().equals(users.getUserName()))) {
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
    public ProjectResponse insertProject(ProjectRequest request) {
        // lay user trong phien dang nhap
        Users users = extract.getUserInFlowLogin() ;
        // chuyen Project ve entity
        Projects projects = projectMapper.toEntity(request) ;
        projects.setUsers(users);
        Projects projectsInserted = projectsRepository.save(projects) ;
        // kiem tra List Media co rong khong
        // Neu List khong rong co nghia nguoi dung muon them project kem hinh anh
        // lay List<MediaFileCreateRequest>
        List<MediaFileCreateRequest> mediaFileCreateRequests = request.getMediaFiles() ;
        // Tao List<MediaFiles>
        List<MediaFiles> mediaFiles = new ArrayList<>() ;
        if(!checkListMediaIsNull(mediaFileCreateRequests)) {
            mediaFileCreateRequests.forEach(mediaFileCreateRequest -> {
                MediaFiles mediaFilesInsert = createAndSaveMedia(mediaFileCreateRequest, users, projectsInserted.getProjectId());
                mediaFiles.add(mediaFilesInsert) ;
            });
        }
        return projectMapper.toDTO(projects , mediaFiles) ;

    }

    @Override
    @PreAuthorize( "hasRole('USER')")
    public ProjectResponse updateProject(ProjectRequest request, int Id) {
        // kiem tra Project co ton tai khong
        Projects projects = getProjectsById(Id) ;
        // lay user dang dang nhap
        Users users = extract.getUserInFlowLogin() ;
        // kiem tra doan bao user chi duoc cap nhat Education cua chinh user do
        checkAuthenticated(users , projects);

        if(request.getTitle() != null) {
            projects.setTitle(request.getTitle());
        }
        if(request.getDescription() != null) {
            projects.setDescription(request.getDescription());
        }
        projectsRepository.save(projects) ;
        return projectMapper.toDTO(projects) ;
    }

    @Override
    @PreAuthorize( "hasRole('USER')")
    public void deleteProject(int Id) {
        // kiem tra Project co ton tai khong
        Projects projects = getProjectsById(Id) ;
        // lay user dang dang nhap
        Users users = extract.getUserInFlowLogin() ;
        // kiem tra doan bao user chi duoc cap nhat Education cua chinh user do
        checkAuthenticated(users , projects);

        List<MediaFiles> mediaFiles = mediaFileRepository.findByEntityTypeAndEntityId(MediaEntityType.PROJECT , Id ) ;
        mediaFiles.forEach(mediaFilesDelete -> {
            deleteImage(mediaFilesDelete.getLink());
            mediaFileRepository.deleteById(mediaFilesDelete.getMediaId());
        });

        projectsRepository.deleteById(Id);
    }

    @Override
    @PreAuthorize( "hasRole('USER')")
    public List<ProjectResponse> findAll() {
        return projectsRepository.findAll().stream().map(projectMapper :: toDTO).toList() ;
    }

    @Override
    @PreAuthorize( "hasRole('USER')")
    public ProjectResponse findById(int id) {
        // kiem tra Project co ton tai khong
        Projects projects = getProjectsById(id) ;
        // lay user dang dang nhap
        Users users = extract.getUserInFlowLogin() ;
        // kiem tra doan bao user chi duoc cap nhat Education cua chinh user do
        checkAuthenticated(users , projects);
        List<MediaFiles> mediaFiles = mediaFileRepository.findByEntityTypeAndEntityId(MediaEntityType.PROJECT , id ) ;
        return projectMapper.toDTO(projects , mediaFiles) ;
    }
}
