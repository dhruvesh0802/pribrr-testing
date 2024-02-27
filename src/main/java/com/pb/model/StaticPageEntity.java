package com.pb.model;

import com.pb.constants.UserType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@Entity
@Table(name = "static_page")
@SQLDelete(sql =
        "UPDATE static_page SET deleted_date=now() " +
                "WHERE id = ?")
@Where(clause = "deleted_date IS NULL")
public class StaticPageEntity extends BaseEntity{

    @Column(name = "page_name",unique = true)
    String pageName;

    @Column(name = "content")
    @Lob
    String content;

}
