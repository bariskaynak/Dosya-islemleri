package com.fileoperations.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

@MappedSuperclass
public abstract class BaseEntity {
		@Id
	    @GeneratedValue(generator = "uuid2")
	    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
	    @Column(name = "id", columnDefinition = "VARCHAR(255)")
	    private String id;

		
		@Temporal(TemporalType.TIMESTAMP)
		@Column(name = "created_date",nullable = true, updatable = false)
		@CreationTimestamp
		private Date createdDate;

		@Column(name = "update_time")
		@UpdateTimestamp
		private Date updateAt;

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public Date getCreatedDate() {
			return createdDate;
		}

		public void setCreatedDate(Date createdDate) {
			this.createdDate = createdDate;
		}

		public Date getUpdateAt() {
			return updateAt;
		}

		public void setUpdateAt(Date updateAt) {
			this.updateAt = updateAt;
		}

		@Override
		public String toString() {
			return "BaseEntity [id=" + id + ", createdDate=" + createdDate + ", updateAt=" + updateAt + "]";
		}

		
}
