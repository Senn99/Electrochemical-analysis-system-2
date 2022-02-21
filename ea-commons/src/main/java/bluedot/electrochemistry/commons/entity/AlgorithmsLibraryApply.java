package bluedot.electrochemistry.commons.entity;

import bluedot.electrochemistry.simplespring.core.annotation.Param;

import java.time.LocalDateTime;

/**
 * @author Senn
 * @create 2022/2/2 19:14
 */
@Param
public class AlgorithmsLibraryApply {
    private Long id;
    private Long userId;
    private Long algoId;

    private String author;

    private String algoName;

    /**
     * 0 未审核
     * 1 通过
     * 2 失败
     */
    private Integer status;

    private LocalDateTime gmt_create;

    private LocalDateTime gmt_modify;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getAlgoId() {
        return algoId;
    }

    public void setAlgoId(Long algoId) {
        this.algoId = algoId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public LocalDateTime getGmt_create() {
        return gmt_create;
    }

    public void setGmt_create(LocalDateTime gmt_create) {
        this.gmt_create = gmt_create;
    }

    public LocalDateTime getGmt_modify() {
        return gmt_modify;
    }

    public void setGmt_modify(LocalDateTime gmt_modify) {
        this.gmt_modify = gmt_modify;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAlgoName() {
        return algoName;
    }

    public void setAlgoName(String algoName) {
        this.algoName = algoName;
    }
}
