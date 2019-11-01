package com.api.flightslocator.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;

import java.io.Serializable;
import java.util.Date;

@SuppressWarnings("serial")
@Entity
@Table(name = "flights")
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = {"createdAt", "updatedAt"}, 
        allowGetters = true)
public class Flights implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long airportId;

    @NotBlank
    private String airportName;

    @NotBlank
    private String city;

    @NotBlank
    private String country;

    @NotBlank
    private String IATA;

    @NotBlank
    private String ICAO;

    @NotBlank
    private String latitude;

    @NotBlank
    private String longitude;

    @NotBlank
    private String altitude;

    @NotBlank
    private String timezone;

    @NotBlank
    private String DST;

    @NotBlank
    private String tz_database_time_zone;

    @NotBlank
    private String type;

    @NotBlank
    private String source;
    
    @Column(nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date createdAt;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
    private Date updatedAt;

	public Long getAirportId() {
		return airportId;
	}

	public void setAirportId(Long airportId) {
		this.airportId = airportId;
	}

	public String getAirportName() {
		return airportName;
	}

	public void setAirportName(String airportName) {
		this.airportName = airportName;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getIATA() {
		return IATA;
	}

	public void setIATA(String iATA) {
		IATA = iATA;
	}

	public String getICAO() {
		return ICAO;
	}

	public void setICAO(String iCAO) {
		ICAO = iCAO;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getAltitude() {
		return altitude;
	}

	public void setAltitude(String altitude) {
		this.altitude = altitude;
	}

	public String getTimezone() {
		return timezone;
	}

	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}

	public String getDST() {
		return DST;
	}

	public void setDST(String dST) {
		DST = dST;
	}

	public String getTz_database_time_zone() {
		return tz_database_time_zone;
	}

	public void setTz_database_time_zone(String tz_database_time_zone) {
		this.tz_database_time_zone = tz_database_time_zone;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

}