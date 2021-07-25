package com.upgrad.quora.service.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

@Entity
@Table(name = "user_auth" , schema = "public")
@NamedQueries({
        @NamedQuery(name = "userAuthTokenByAccessToken", query = "select ua from UserAuthEntity ua where ua.accessToken = :accessToken"),
        @NamedQuery(name = "userEntryByUserId", query = "select ua from UserAuthEntity ua where ua.user = :user")
})
public class UserAuthEntity implements Serializable {


    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private UserEntity user;

    @Column(name = "ACCESS_TOKEN")
    @NotNull
    @Size(max = 500)
    private String accessToken;

    @Column(name = "UUID")
    @NotNull
    @Size(max = 200)
    private String uuid;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Column(name = "EXPIRES_AT")
    @NotNull
    private ZonedDateTime expiresAt;

    @Column(name = "LOGIN_AT")
    @NotNull
    private ZonedDateTime loginAt;

    @Column(name = "LOGOUT_AT")
    private ZonedDateTime logoutAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public ZonedDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(ZonedDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    public ZonedDateTime getLoginAt() {
        return loginAt;
    }

    public void setLoginAt(ZonedDateTime loginAt) {
        this.loginAt = loginAt;
    }

    public ZonedDateTime getLogoutAt() {
        return logoutAt;
    }

    public void setLogoutAt(ZonedDateTime logoutAt) {
        this.logoutAt = logoutAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserAuthEntity that = (UserAuthEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(uuid, that.uuid) && Objects.equals(user, that.user) && Objects.equals(accessToken, that.accessToken) && Objects.equals(expiresAt, that.expiresAt) && Objects.equals(loginAt, that.loginAt) && Objects.equals(logoutAt, that.logoutAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, accessToken, expiresAt, loginAt, logoutAt, uuid);
    }

    @Override
    public String toString() {
        return "UserAuthEntity{" +
                "id=" + id +
                ", user=" + user +
                ", accessToken='" + accessToken + '\'' +
                ", expiresAt=" + expiresAt +
                ", loginAt=" + loginAt +
                ", logoutAt=" + logoutAt +
                ", uuid=" + uuid +
                '}';
    }
}
