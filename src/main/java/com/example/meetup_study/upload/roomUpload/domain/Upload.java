package com.example.meetup_study.upload.roomUpload.domain;

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
public class Upload extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "upload_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private Room room;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "file_path")
    private String filePath;

//    @Column(name = "file_type")
//    private String fileType;
//
//    @Column(name = "file_size")
//    private Long fileSize;
public Upload(Room room, String fileName, String filePath) {
    this.room = room;
    this.fileName = fileName;
    this.filePath = filePath;
}



}
