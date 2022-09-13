package com.js.authentication.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Entity
@Table(name = "APPLICATION")
public class Application implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID", length = 150)
	private String id;

	@Column(name = "APPNAME", unique = true, length = 100)
	private String appName;

	@Column(name = "DESCRIPTION", length = 150)
	private String description;

	@Column(name = "ACCESS", nullable = false, length = 150)
	private String access;

	@Column(name = "CREATIONTIME", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	private Timestamp onBoardTime;

	@Column(name = "ISACTIVE", columnDefinition = "boolean default false")
	private Boolean isActive;

	@Column(name = "SALT", nullable = false, length = 50)
	private String salt;
	
	@Column(name = "USERID", length = 100)
	private String userId;

	@Transient
	private String email;

	@Transient
	private String password;

	/**
	 *
	 */
	public Application() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param id
	 * @param appName
	 * 
	 * @param description
	 * @param access
	 * @param onBoardTime
	 * 
	 */
	public Application(String id, String appName, String description, String access, Timestamp onBoardTime) {
		super();
		this.id = id;
		this.appName = appName;
		this.description = description;
		this.access = access;
		this.onBoardTime = onBoardTime;

	}

	public Application(ApplicationBuilder applicationBuilder) {
		super();
		this.id = applicationBuilder.id;
		this.appName = applicationBuilder.appName;
		this.description = applicationBuilder.description;
		this.access = applicationBuilder.access;
		this.onBoardTime = applicationBuilder.onBoardTime;
		this.salt =applicationBuilder.salt; 
		this.email =applicationBuilder.email;
		this.password =applicationBuilder.password;
		this.isActive = applicationBuilder.isActive;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the appName
	 */
	public String getAppName() {
		return appName;
	}

	/**
	 * @param appName the appName to set
	 */
	public void setAppName(String appName) {
		this.appName = appName;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the access
	 */
	public String getAccess() {
		return access;
	}

	/**
	 * @param access the access to set
	 */
	public void setAccess(String access) {
		this.access = access;
	}

	/**
	 * @return the onBoardTime
	 */
	public Timestamp getOnBoardTime() {
		return onBoardTime;
	}

	/**
	 * @param onBoardTime the onBoardTime to set
	 */
	public void setOnBoardTime(Timestamp onBoardTime) {
		this.onBoardTime = onBoardTime;
	}

	public Boolean getActive() {
		return isActive;
	}

	public void setActive(Boolean active) {
		isActive = active;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}
	
	

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Application that = (Application) o;
		return Objects.equals(id, that.id) && Objects.equals(appName, that.appName)
				&& Objects.equals(description, that.description) && Objects.equals(access, that.access)
				&& Objects.equals(onBoardTime, that.onBoardTime) && Objects.equals(isActive, that.isActive)
				&& Objects.equals(salt, that.salt);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, appName, description, access, onBoardTime, isActive, salt);
	}

	@Override
	public String toString() {
		return "Application{" + "id='" + id + '\'' + ", appName='" + appName + '\'' + ", description='" + description
				+ '\'' + ", access='" + access + '\'' + ", onBoardTime=" + onBoardTime + ", isActive=" + isActive
				+ ", salt='" + salt + '\'' + '}';
	}

	public static class ApplicationBuilder {

		private String id;
 
		private String appName;
 
		private String description;
 
		private String access;
 
		private Timestamp onBoardTime;
 
		private Boolean isActive;
 
		private String salt;
 
		private String email;
 
		private String password;

		/**
		 *
		 */
		public ApplicationBuilder() {
			this.onBoardTime =  new Timestamp(System.currentTimeMillis());
			this.isActive = Boolean.FALSE;
		}

		public ApplicationBuilder(String id, String appName, String description, String access) {
			 this.id = id;
			 this.appName = appName;
			 this.description = description;
			 this.access = access;
			 this.onBoardTime =  new Timestamp(System.currentTimeMillis());
			 this.isActive = Boolean.FALSE;
		}
		
		public ApplicationBuilder withPassword(String password) {
			this.password =  password;
			return this;
		}
		
		public ApplicationBuilder withEmail(String email) {
			this.email =  email;
			return this;
		}
		
		public ApplicationBuilder withActive(Boolean isActive) {
			this.isActive =  isActive;
			return this;
		}

		public ApplicationBuilder withOnboardTime(Timestamp timestamp) {
			this.onBoardTime =  timestamp;
			return this;
		}

		public ApplicationBuilder withDescription(String value) {
			this.description = value;
			return this;
		}

		public ApplicationBuilder withAppName(String appName) {
			this.appName = appName;;
			return this;
		}

		public ApplicationBuilder withId(String id) {
			this.id = id;
			return this;
		}

		public ApplicationBuilder withAccess(String access) {
			this.access = access;
			return this;
		}

		public ApplicationBuilder withSalt(String salt) {
			this.salt = salt;
			return this;
		}

		public Application build() {
			return new Application(this) ;
		}

		@SuppressWarnings("unused")
		private void validateUserObject(Authentication authentication) {
			// Do some basic validations to check
			// if user object does not break any assumption of system
		}

		public String buildJson() throws JsonProcessingException {
			ObjectMapper objectMapper = new ObjectMapper();
			return objectMapper.writeValueAsString(new Application(this));
		}
	}

}