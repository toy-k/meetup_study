package com.example.meetup_study.upload.announceUpload.domain;

import com.example.meetup_study.announce.domain.Announce;
import com.example.meetup_study.common.domain.BaseEntity;
import com.example.meetup_study.room.domain.Room;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class AnnounceUpload extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "announce_upload_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "announce_id")
    private Announce announce;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "file_path")
    private String filePath;

    //    @Column(name = "file_type")
//    private String fileType;
//
//    @Column(name = "file_size")
//    private Long fileSize;
    public AnnounceUpload(Announce announce, String fileName, String filePath) {
        this.announce = announce;
        this.fileName = fileName;
        this.filePath = filePath;
    }
}
