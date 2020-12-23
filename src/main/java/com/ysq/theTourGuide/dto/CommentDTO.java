package com.ysq.theTourGuide.dto;

import com.ysq.theTourGuide.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDTO {


    private String avatar_url;
    private String nickname;
    private boolean isLike;
    private Comment comment;
    private String timeFormHere;
}
