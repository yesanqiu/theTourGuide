package com.ysq.theTourGuide.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Table(name = "administrator_authority")
public class AdministratorAuthority implements Serializable {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    /**
     * 添加管理员权限
     */
    @Column(name = "add_administrator")
    private Boolean addAdministrator;

    /**
     * 添加子管理员权限
     */
    @Column(name = "add_child_admistrator")
    private Boolean addChildAdmistrator;

    /**
     * 管理导游信息
     */
    @Column(name = "manage_guide")
    private Boolean manageGuide;

    /**
     * 管理视频信息
     */
    @Column(name = "manage_video")
    private Boolean manageVideo;

    /**
     * 管理预约信息
     */
    @Column(name = "manage_order")
    private Boolean manageOrder;

    /**
     * 管理景区信息
     */
    @Column(name = "manage_scenic")
    private Boolean manageScenic;

    /**
     * 发布通知
     */
    private Boolean inform;

    public static final String ID = "id";

    public static final String ADD_ADMINISTRATOR = "addAdministrator";

    public static final String ADD_CHILD_ADMISTRATOR = "addChildAdmistrator";

    public static final String MANAGE_GUIDE = "manageGuide";

    public static final String MANAGE_VIDEO = "manageVideo";

    public static final String MANAGE_ORDER = "manageOrder";

    public static final String MANAGE_SCENIC = "manageScenic";

    public static final String INFORM = "inform";
}