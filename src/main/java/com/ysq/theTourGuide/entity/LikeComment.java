package com.ysq.theTourGuide.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Table(name = "like_comment")
@AllArgsConstructor
@NoArgsConstructor
public class LikeComment  implements Serializable {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tourist_id")
    private Long touristId;

    @Column(name = "comment_id")
    private Long commentId;

    public static final String ID = "id";

    public static final String TOURIST_ID = "touristId";

    public static final String COMMENT_ID = "commentId";

    public LikeComment(Long touristId,Long commentId){
        this.touristId = touristId;
        this.commentId = commentId;
    }

}