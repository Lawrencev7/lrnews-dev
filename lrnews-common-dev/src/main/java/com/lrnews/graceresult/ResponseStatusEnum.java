package com.lrnews.graceresult;

public enum ResponseStatusEnum {

    SUCCESS(200, true, "Success！"),
    REGISTER_SUCCESS(200, true, "Register new user success"),
    FAILED(500, false, "Failed！"),

    // 50x
    ILLEGAL_ARGUMENT(500, false, "Illegal argument"),
    USER_NOT_LOGIN(501, false, "User is not login"),
    TICKET_INVALID(502, false, "会话失效，请重新登录！"),
    NO_AUTH(503, false, "Access denied"),
    MOBILE_ERROR(504, false, "短信发送失败，请稍后重试！"),
    SMS_OVER_FREQUENT_ERROR(505, false, "Too frequent, please retry later."),
    VERIFY_CODE_INCORRECT(506, false, "Verify code is not match"),
    VERIFY_CODE_EXPIRED(507, false, "The verify code is expired"),
    FILE_UPLOAD_NULL_ERROR(510, false, "Upload a null file"),
    FILE_UPLOAD_FAILD(511, false, "文件上传失败！"),
    FILE_FORMATTER_FAILD(512, false, "文件图片格式不支持！"),
    FILE_MAX_SIZE_ERROR(513, false, "仅支持500kb大小以下的图片上传！"),
    FILE_NOT_EXIST_ERROR(514, false, "你所查看的文件不存在！"),
    USER_STATUS_ERROR(515, false, "Error with user status parameter"),
    USER_NOT_EXIST_ERROR(516, false, "用户不存在！"),

    USER_FROZEN(521, false, "用户已被冻结，请联系管理员！"),
    USER_UPDATE_ERROR(522, false, "用户信息更新失败，请联系管理员！"),
    USER_INACTIVE_ERROR(523, false, "User is still inactive, please modify user info and activate your account"),

    // 自定义系统级别异常 54x
    SYSTEM_INDEX_OUT_OF_BOUNDS(541, false, "系统错误，数组越界！"),
    SYSTEM_ARITHMETIC_BY_ZERO(542, false, "系统错误，无法除零！"),
    SYSTEM_NULL_POINTER(543, false, "系统错误，空指针！"),
    SYSTEM_NUMBER_FORMAT(544, false, "系统错误，数字转换异常！"),
    SYSTEM_PARSE(545, false, "系统错误，解析异常！"),
    SYSTEM_IO(546, false, "系统错误，IO输入输出异常！"),
    SYSTEM_FILE_NOT_FOUND(547, false, "系统错误，文件未找到！"),
    SYSTEM_CLASS_CAST(548, false, "系统错误，类型强制转换错误！"),
    SYSTEM_PARSER_ERROR(549, false, "系统错误，解析出错！"),
    SYSTEM_DATE_PARSER_ERROR(550, false, "系统错误，日期解析出错！"),

    // admin 管理系统 56x
    ADMIN_USERNAME_NULL_ERROR(561, false, "Admin username can not be null"),
    ADMIN_USERNAME_EXIST_ERROR(562, false, "Admin username is already existed"),
    ADMIN_NAME_NULL_ERROR(563, false, "Null admin username"),
    ADMIN_PASSWORD_ERROR(564, false, "Two password is not match"),
    ADMIN_CREATE_ERROR(565, false, "添加管理员失败！"),
    ADMIN_PASSWORD_NULL_ERROR(566, false, "Password can not be null"),
    ADMIN_NOT_EXIT_ERROR(567, false, "Admin user not exist"),
    ADMIN_PWD_WRONG_ERROR(568, false, "Admin user password wrong"),
    ADMIN_FACE_NULL_ERROR(569, false, "人脸信息不能为空！"),
    ADMIN_FACE_LOGIN_ERROR(570, false, "人脸识别失败，请重试！"),
    ADMIN_FACE_NOT_REGISTERED_ERROR(571, false, "User didn't register by face"),
    CATEGORY_EXIST_ERROR(572, false, "This category is already existed."),

    // 媒体中心 相关错误 58x
    ARTICLE_COVER_NOT_EXIST_ERROR(580, false, "Please select a cover for article"),
    ARTICLE_CATEGORY_NOT_EXIST_ERROR(581, false, "请选择正确的文章领域！"),
    ARTICLE_CREATE_ERROR(582, false, "Fail to publish article. Please retry or contact admin"),
    ARTICLE_QUERY_PARAMS_ERROR(583, false, "文章列表查询参数错误！"),
    ARTICLE_DELETE_ERROR(584, false, "文章删除失败！"),
    ARTICLE_WITHDRAW_ERROR(585, false, "文章撤回失败！"),
    ARTICLE_REVIEW_ERROR(585, false, "文章审核出错！"),
    ARTICLE_ALREADY_READ_ERROR(586, false, "文章重复阅读！"),

    // 人脸识别错误代码
    FACE_VERIFY_TYPE_ERROR(600, false, "人脸比对验证类型不正确！"),
    FACE_VERIFY_LOGIN_ERROR(601, false, "人脸登录失败！"),

    // 系统错误，未预期的错误 555
    SYSTEM_ERROR(555, false, "Encounter a system error. Retry later or contact administrator to confirm it."),
    SYSTEM_OPERATION_ERROR(556, false, "操作失败，请重试或联系管理员"),
    SYSTEM_CONNECTION_FAIL(557, false, "Inner connection error of system occurs. Please contact admin");


    // 响应业务状态
    private final Integer status;
    // 调用是否成功
    private final Boolean success;
    // 响应消息，可以为成功或者失败的消息
    private final String msg;

    ResponseStatusEnum(Integer status, Boolean success, String msg) {
        this.status = status;
        this.success = success;
        this.msg = msg;
    }

    public Integer status() {
        return status;
    }

    public Boolean success() {
        return success;
    }

    public String msg() {
        return msg;
    }
}