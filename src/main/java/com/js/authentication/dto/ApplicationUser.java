package com.js.authentication.dto;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApplicationUser {

    private String id;
    private String name;
    private String type;
    private Date date;
    private boolean active;
    private boolean logout;
    private String applicationName;
    private String appId;

    public ApplicationUser(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.type = builder.type;
        this.date = builder.date;
        this.active = builder.active;
        this.logout = builder.logout;
        this.applicationName = builder.applicationName;
        this.appId = builder.appId;
    }

    public static class Builder {
        private String id;
        private String name;
        private String type;
        private Date date;
        private boolean active;
        private boolean logout;
        private String applicationName;
        private String appId;

        public Builder id(String value) {
            this.id = value;
            return this;
        }

        public Builder name(String value) {
            this.name = value;
            return this;
        }

        public Builder type(String value) {
            this.type = value;
            return this;
        }

        public Builder date(Date value) {
            this.date = value;
            return this;
        }

        public Builder active(boolean value) {
            this.active = value;
            return this;
        }
        public Builder logout(boolean value) {
            this.logout = value;
            return this;
        }

        public Builder applicationName(String value) {
            this.applicationName = value;
            return this;
        }

        public Builder appId(String value) {
            this.appId = value;
            return this;
        }

        public ApplicationUser build() {
            return new ApplicationUser(this);
        }
    }
}
