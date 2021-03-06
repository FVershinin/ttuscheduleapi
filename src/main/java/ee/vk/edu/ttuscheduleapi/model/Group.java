package ee.vk.edu.ttuscheduleapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Group {
    private Long id;
    private String name;

    @JsonIgnore
    private Integer type;

    public Group() {
    }

    public Group(Long id, String name, Integer type) {
        this.id = id;
        this.name = name;
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
