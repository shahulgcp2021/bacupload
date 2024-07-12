package com.bacuti.repository;


import com.bacuti.domain.FileUploadStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FileUploadStatusRepository extends JpaRepository<FileUploadStatus, Long> {
    /**
     * Gets File details by name.
     *
     * @param fileName
     * @return FileUploadStatus as optional.
     */
    Optional<FileUploadStatus> findFirstByFileNameOrderByLastModifiedDateDesc(String fileName);

  }
