package qeeka.jake.imagesteganography.constants;

public class AdminConstant {
    public static final String ENCRYPTION_TYPE = "md5";//加密类型
    public static final Integer ENCRYPTION_TIMES = 2;//加密次数
    public static final Integer ADMIN_ROLE_SUPER_ID = 1;//超级管理员
    public static final Integer ADMIN_ROLE_MIDDLE_ID = 2;//中级管理员
    public static final Integer ADMIN_ROLE_LOW_ID = 3;//低级管理员
    public static final Integer ADMIN_STATUS_PASS = 1;//通过
    public static final Integer ADMIN_STATUS_NOPASS = 0;//不通过(软删除)
}